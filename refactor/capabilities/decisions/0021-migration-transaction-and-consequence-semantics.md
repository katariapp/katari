# Migration Transaction and Consequence Semantics

Status: Accepted

Accepted on 2026-07-19.

## Context

The legacy Migration use case runs synchronization, child-state changes, progress, playback preferences, viewer
settings, categories, tracking, Download deletion, custom-cover copying, Entry updates, and Merge replacement as an
ordered list of unrelated calls. Most database calls create their own small transactions, filesystem work can happen
before Library ownership changes, and every non-cancellation failure is swallowed. A returned `Unit` therefore cannot
distinguish no-op, full success, partial database mutation, or failed external cleanup.

F11.1 deliberately introduced no write-capable host port. F11.2 traced the real owners and failure behavior before
deciding what such a port is allowed to do.

## Audit Findings

| Work | Current atomicity and failure behavior | Retry and ownership conclusion |
| --- | --- | --- |
| Pair preparation | UI and use case pass mutable `Entry` values and reread active-profile repositories. Download and cover availability are inspected independently. | Preparation must be mutation-free, explicit-profile, and Feature-issued. Only state relevant to authorization is optimistic authority; mutable transfer data is captured again immediately before execution. |
| Target synchronization | Network fetch and metadata, child removal/update/insert, progress rekey, and fetch-interval writes occur across multiple transactions. Writes use repositories backed by mutable active-profile identity. Some methods return `false` or an empty list after swallowing persistence errors, so synchronization may appear successful after a partial write. | Synchronization remains an explicit-profile precondition before the Migration commit and is safe to retry only after it gains a strict result that cannot hide persistence failure. Partial sync state is not a successful Migration and does not create a Migration operation record. |
| Entry and Library state | Source/target favorite state, dates, flags, and notes are updated one Entry at a time. Boolean failures are ignored. | These F11-owned fields belong to one explicit-profile primary database transition with optimistic revalidation. |
| Categories | Mapping replacement has its own transaction and uses ambient profile identity. | Selected category transfer belongs to the same primary transition, with category existence revalidated in that profile. |
| Child matching and state | Matching is shared, while read/bookmark/date-fetch fields are written in a separate chapter transaction. Consumption and Bookmarking participation are currently assumed rather than independently derived. | Matching and normalized deltas are prepared after target sync. Only graph-selected Consumption and Bookmarking relationships contribute their respective fields; contextual fetch state remains F11-owned. Applicable shared deltas commit with the primary transition. |
| Tracking | Enhanced-track migration is a pure local identity transformation; resulting tracks are local rows with a profile/Entry/tracker uniqueness constraint. | Track rows can be prepared without mutation and upserted in the primary transaction. No tracker network operation belongs to Migration execution. |
| Progress | F15 reads source rows and performs target merge/upserts in multiple small transactions. Repeating the same captured state is safe because timestamp merge prevents older state from replacing newer target progress. The current `copy` call rereads source state on every retry. | F15 must prepare an immutable Migration payload with resource mappings before commit, then apply that payload idempotently as durable follow-up. A retry must not copy whatever source progress happens to exist later. |
| Playback preferences | F16 copies one source row to an idempotent target upsert, but `copy` rereads the source on every invocation. | Capture the F16 snapshot before commit and durably restore that snapshot after commit. No source row means no consequence record. |
| Viewer settings | F25 copies provider-owned overrides by idempotent upsert. The legacy viewer bitfield is still changed directly by the old use case with a Manga branch. | F25 must prepare the complete type-owned Migration payload, including any legacy normalized state, and restore it as durable follow-up. F11 never contains the type branch. |
| Merge replacement | F12 atomically replaces membership, but its transaction is separate from F11 Library ownership. Download ownership is also resolved through current Merge membership. | Replace mode requires a narrow F12 transaction-participation operation so F11 primary state and Merge membership commit or roll back together. Download owners are captured before membership changes. |
| Download removal | F08 resolves all current Download owners and invokes type-owned filesystem deletion. Manga and Anime can ignore a failed directory deletion while evicting cache state and reporting `Performed`. | Removal is idempotent in intent but is not yet verifiably complete. F08 must accept a captured owner set and return structured verified success/failure before F11 can acknowledge a durable consequence. |
| Custom cover | The current operation copies directly from the source file to the visible target path. It can leave a copied target after a later database failure, and a post-commit retry may lose its source bytes. | Selected cover content is staged under the Migration operation before commit, promoted idempotently after commit, and removed only after acknowledgement. Orphan staging is cleaned independently. |

## Proposed Decision

### Preparation authorizes a pair; execution captures transfer data

- A Feature-issued reference binds its session identity, explicit profile, source/target content identities, type,
  source Library membership, target Library state relevant to copy/replace, and the option availability shown to the
  user.
- Preparation does not synchronize a source, copy data, stage a file, or hold a database lock.
- Execution accepts only a Feature-issued reference. Caller-created references are rejected. A relevant pair change
  before execution returns `Conflict`; unrelated source metadata changes do not.
- Transfer values that the configuration UI does not display, such as current categories, notes, child state, progress,
  settings, and tracks, are captured from authoritative state during execution rather than becoming an unnecessarily
  broad long-lived conflict snapshot.
- The reference has one durable session identity. Once a primary transition commits, a repeated execution with the same
  mode and options replays the recorded outcome; a different intent cannot reuse that reference.

### Execution has four ordered stages

1. Revalidate the explicit-profile pair and captured user intent without mutation.
2. Strictly synchronize the target against the profile captured by the reference. A network or persistence failure
   returns `OperationalFailure` and creates no F11 operation record, primary transition, or external consequence. Any
   partial synchronization remains ordinary source sync state and may be retried; it is never reported as applied
   Migration. An active-profile change cannot retarget any write.
3. Reload the synchronized target and prepare immutable child mappings, database deltas, feature-owned payloads,
   pre-change Download owners, and selected custom-cover staging. Failure to prepare any applicable non-optional or
   explicitly selected consequence aborts before the primary transition.
4. Commit the primary transition and its durable operation/consequence records together, then deliver pending
   consequences. Callers receive only the aggregate outcome.

No screen, dialog, or caller can reorder these stages or execute an omitted consequence itself.

### One primary transaction establishes Migration ownership

The primary database transition contains:

- final optimistic revalidation of the source, target, relevant child rows, category mappings, and the operation
  reference;
- source and target Library/favorite/date ownership for copy or replace;
- target Entry flags and selected notes owned by F11;
- selected categories;
- graph-selected normalized consumption/bookmark deltas plus F11 contextual child state;
- prepared local tracking rows;
- in replace mode, F12-owned Merge replacement through a narrow transaction-participation boundary;
- one durable operation result and every prepared post-commit consequence record.

All of these changes commit or roll back together. The app host receives an owned transition plus expectations; it does
not expose Entry, category, child, track, journal, or Merge CRUD. Copy mode can use the F11 host transaction directly.
Replace mode coordinates with F12 so membership logic remains F12-owned while participating in the same outer database
transaction.

The primary operation is identified by the Feature-issued session and a persisted intent fingerprint. This provides
idempotent replay after a timeout or process death and prevents the same reference from committing a second, different
Migration.

### Cross-feature and filesystem work uses durable at-least-once delivery

- F15 Progress, F16 Playback Preferences, F25 Viewer Settings, selected F08 Download removal, and selected custom-cover
  promotion run only after the primary commit.
- Their immutable, owner-produced payloads are stored with the primary transition. A pending delivery never rereads
  mutable source state to decide what the original Migration meant.
- Delivery is at least once. Every handler must be idempotent and return structured completion. A completed consequence
  is acknowledged durably; a transient failure remains pending with backoff; a repeatedly failing consequence remains
  visible to diagnostics and manual retry.
- A post-commit failure never rolls back Library ownership or Merge membership. The execution result is `Applied` with
  `COMPLETE` only when no pending/failed consequence remains; otherwise it is `Applied` with `INCOMPLETE`.
- F11 exposes aggregate consequence status and retry control through a purpose-specific Feature contract. It does not
  expose journal rows or a caller-owned checklist.

### Consequence-specific retry rules

- Progress applies a captured F15 payload and resource mappings using timestamp-aware merge.
- Playback preferences and viewer settings restore captured owner-produced snapshots with idempotent upserts.
- Download removal uses the owner set captured before Merge replacement and acknowledges only verified filesystem/cache
  absence. Provider absence omits the consequence; deletion failure does not become `Performed`.
- Custom-cover delivery promotes the operation's immutable staged content through atomic replacement where supported.
  Retrying an already promoted identical stage is successful.
- Custom-cover staging that never receives a committed operation is an orphan, not an applied consequence, and is
  removed by bounded cleanup.

### Cancellation and failures remain truthful

- Cancellation always propagates.
- Cancellation before the primary commit rolls back the transaction and leaves no deliverable consequence. Temporary
  staging may be cleaned immediately or later as an orphan.
- Cancellation after the primary commit cannot make the Migration uncommitted. Durable delivery remains pending, and a
  retry of the same intent returns the recorded applied outcome.
- Validation and unavailable-provider conditions are structured rejections/inapplicability, not operational failures.
- An optimistic change returns `Conflict` with no F11 primary write.
- Unexpected pre-commit infrastructure failure returns `OperationalFailure(retryable = ...)` with no committed F11
  operation.
- Optional provider absence skips only its relationship. Failure of an applicable provider operation is never reported
  as absence.

## Authoritative Outcomes

| Failure or interruption point | Result and durable state |
| --- | --- |
| Invalid selection, option, or caller-created reference | Rejected; no sync, primary write, or consequence. |
| Pair authorization changed before execution | Conflict; no sync, primary write, or consequence. |
| Target synchronization fails | Operational failure; no F11 operation committed. Partial ordinary sync state may exist and is retryable. |
| Consequence payload or cover staging cannot be prepared | Operational failure; no primary write or deliverable consequence. |
| Pair/child/category/Merge state changes before commit | Conflict; primary transaction rolls back; no deliverable consequence. |
| Database failure before primary commit | Operational failure; all primary changes and journal rows roll back. |
| Primary commits and every delivery completes | Applied with `COMPLETE`. |
| Primary commits and a consequence fails or is interrupted | Applied with `INCOMPLETE`; durable retry continues. |
| Process dies after primary commit but before returning | Retrying the same reference and intent replays the committed outcome; pending delivery resumes. |
| Same committed reference is reused with different mode/options | Rejected as invalid reuse; the original operation is not repeated or altered. |
| Optional relationship is absent | Base Migration commits without that consequence; the content type remains valid. |

## Required Follow-on Changes

- F11.3 adds a strict explicit-profile synchronization adapter, stable operation identity, primary transition,
  explicit-profile host, and replay semantics. It must not wrap the current best-effort use case or temporarily switch
  ambient profile state.
- F11.4 adds owner-produced immutable payloads and a durable consequence coordinator.
- F12's narrow Migration cooperation API gains transaction participation; raw membership remains inaccessible to F11.
- F08 Download maintenance gains captured-owner removal and verified structured failure. Existing deletion behavior
  cannot be treated as durable merely by putting it behind a queue.
- F15 and F25 gain purpose-specific prepared Migration payloads where their current copy/restore contracts cannot retain
  exact source-to-target mapping or legacy state.
- F11 adds aggregate pending/failed status and retry control without exposing its internal consequence list.
- Target synchronization must stop suppressing child persistence failures on the strict Migration path. Whether the
  general synchronization path adopts the same strict result is an implementation decision for that owner, but F11
  cannot accept ambiguous success.

## Alternatives Rejected

- Preserve the current ordered best-effort calls and return success after swallowing failures.
- Put network synchronization or filesystem operations inside the database transaction.
- Run external effects before the primary commit and attempt compensation later.
- Treat every database-backed feature call as transaction-safe merely because current providers happen to use SQLite.
- Retry `copy(source, target)` by rereading mutable source state after the original operation.
- Resolve Download owners after Merge membership has already changed.
- Put Merge membership logic in the F11 host to obtain atomicity.
- Report Download removal complete when a provider ignored filesystem deletion failure.
- Expose consequence ordering or retry as a checklist owned by the initiating screen.
- Generate a new operation identity for every retry and rely on current favorite state to detect duplicates.

## Manifesto Alignment

The decision is expressed in terms of owned relationships, not Manga/Anime/Book behavior. Provider absence remains
valid; applicable relationships prepare their own payloads; specialized gaps become explicit obligations; and the
coordinator owns ordering and aggregate outcomes. An unknown future Migration provider receives the same primary
semantics, while any genuinely media-specific persistence or retry requirement must be contributed by its owning
feature instead of hidden behind a type branch.
