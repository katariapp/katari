# Merge Profile, Transaction, and Consequence Semantics

Status: Accepted

## Context

The legacy Merge authority resolves most reads and writes through the mutable active profile. Interactive editors,
backup restore, notifications, profile movement, profile deletion, and background Download work can therefore begin in
one profile and complete after the active profile has changed.

The current workflows also split one user action across unrelated calls. Membership rows are usually committed first,
while Library state, cover cleanup, and Download removal happen before or after that commit without one authoritative
outcome. A database rollback cannot undo filesystem work, and a process death after the database commit can silently
skip remaining cleanup.

The Merge boundary needs stable profile identity, optimistic conflict semantics, atomic database ownership, and a
defined outcome for non-database consequences before a write-capable host port or persistence adapter is introduced.

## Proposed Decision

### Profile identity is captured once

- Every Merge operation and observation is pinned to an explicit profile ID. A selected `Entry` supplies that identity;
  an ID-only caller supplies an `EntryMergeSubject`.
- Application code may read the active profile only when constructing the initial intent. The Merge coordinator and
  host never re-read active-profile state during the operation.
- A profile switch does not retarget an in-flight command or observation. UI that follows the active profile cancels the
  old observation and starts a new explicitly scoped one.
- Background events and newly created notification payloads carry the originating profile ID. A notification that
  already contains its visible destination does not re-resolve membership when clicked.
- Legacy notification payloads without a profile may use a named compatibility resolver to locate the profile from the
  globally unique Entry ID. They never fall back to the currently active profile. Missing or ambiguous identity rejects
  the action safely.
- Backup creation captures its profile before enumeration. Backup restore captures the destination profile once per
  profile bundle and passes it through every pending Merge operation instead of switching ambient profile state.

### Feature-issued references are optimistic snapshots

- An editor reference is opaque, immutable, and issued only by the Merge feature. It binds the profile, relevant Entry
  identities, and the authoritative membership shape used to build the editor.
- A reference is not a long-running database lock and has no time-based expiry. Execution re-reads authoritative state
  inside its transaction.
- An unknown or caller-created reference is rejected as invalid input. A recognized reference whose relevant
  membership changed returns `Conflict`. Neither outcome writes data or schedules consequences.
- Metadata changes unrelated to membership do not create a false conflict. Deleted Entries, profile changes,
  type changes, or changed membership do.
- Purpose-specific profile-move snapshots follow the same rule. They expose only the source identity needed by the move
  workflow; they do not expose a reusable membership repository.

### One database transition owns each mutation

- An interactive Merge commit executes one database transaction containing reference revalidation, optional Entry
  materialization, category/favorite changes requested by the editor, removal from previous groups, target/order
  selection, and the final membership transition.
- Removing members, dissolving a group, and Merge-owned Library removal use the same rule: every database effect of that
  command either commits together or none commits.
- Expected validation or conflict outcomes are structured results. Unexpected database/infrastructure failure aborts
  the transaction and returns a structured operational failure whose retryability is explicit; no external consequence
  is started.
- The host write boundary accepts an owned transition command plus its expected snapshot. It does not expose `save`,
  `deleteGroup`, `removeMembers`, or another general-purpose CRUD surface.
- Profile deletion needs no separate Merge mutation. Membership disappears through the existing profile/Entry foreign
  key cascade inside the profile-clear transaction; the explicit raw Merge deletion is removed.

### Profile movement participates in the outer transaction

- The Profile Move feature owns the outer transaction because it changes Entries, categories, preferences, and other
  profile-scoped state in addition to Merge.
- Before mutation it asks Merge for an opaque, explicit-source-profile snapshot. Within the outer transaction it passes
  that snapshot plus the factual source-to-destination Entry ID mapping produced by the move.
- Merge revalidates the snapshot in that transaction and derives target/order/removal changes itself. Profile Move does
  not reconstruct or persist membership rows.
- Destination conflict choices remain Profile Move user intent. Merge owns the membership consequence of each choice.
- Any Merge conflict aborts the entire outer Profile Move transaction. A group is never moved with only part of its
  Merge relationship preserved, and the current all-or-nothing move behavior is retained.
- The transaction-participation port is available only to the Profile Move coordinator and the segregated host adapter;
  it is not an application membership API.

### Backup restore is explicit-profile and best-effort by group

- Backup wire compatibility remains expressed through stable Entry identity (`source`, `url`, and type), not database
  IDs or a new support declaration.
- After a destination profile's Entries have been restored, the backup adapter submits normalized group restore intents
  with that explicit profile. Merge resolves the restored IDs and applies each valid group atomically.
- Duplicate rows are normalized. A missing target, fewer than two resolved members, or mixed types skips that group with
  a structured reason rather than corrupting membership or aborting unrelated backup entries.
- Applying a valid restored group replaces conflicting membership for those members, preserving current restore
  behavior. The result reports applied and skipped groups.
- Switching the application's active profile is never part of Merge restore semantics.

### Non-database consequences use durable, at-least-once delivery

- Filesystem and cache effects never run before or inside the membership transaction.
- The transaction stores feature-owned consequence records for applicable external effects, such as Download removal
  and cover cleanup, together with the database transition. Provider absence omits only that optional consequence.
- After commit, the Merge coordinator delivers structured events to the owning feature coordinators. Callers do not
  receive or execute a consequence checklist.
- Delivery is at least once. Handlers must be idempotent, completed records are acknowledged durably, and pending records
  are retried after transient failure or process restart.
- A committed membership transition is never rolled back because a filesystem effect failed. The operation result
  distinguishes `Applied` with all consequences complete from `Applied` with durable follow-up pending.
- A permanently blocked consequence remains visible to diagnostics and retry controls; it is not silently reported as
  full success.
- External Library-removal workflows send a structured lifecycle event to Merge so membership is adjusted by its owner.
  Their unrelated Library/Download consequences remain owned by those workflows. The Merge editor's own removal dialog
  remains a single Merge-owned workflow because its booleans are direct user choices.

## Authoritative Outcomes

| Point of failure or change | Authoritative result |
| --- | --- |
| Invalid selection or caller-built reference | Rejected; no database or external effect. |
| Membership/profile/type changed since preparation | Conflict; no database or external effect. |
| Database failure before commit | Operational failure with explicit retryability; transaction rolled back; no external effect. |
| Database commit succeeds, all external events complete | Applied and complete. |
| Database commit succeeds, an external event is pending or transiently fails | Applied with durable follow-up pending; automatic retry. |
| Process dies after database commit | Durable pending events resume later; membership remains committed. |
| Optional consequence provider is absent | Base Merge operation remains applied; that consequence is omitted. |
| Profile switches during work | In-flight work remains pinned to its captured profile. |
| Profile is deleted before a pending command commits | Conflict/failure with no write; work is not redirected. |
| Malformed backup group | That group is skipped with a reason; unrelated restore work continues. |
| Profile-move Merge revalidation fails | The affected outer move transaction aborts; no partial group move. |

## Consequences

- The write-capable host port must provide owned transaction operations and durable consequence storage, not raw row
  mutation methods.
- Existing active-profile repository methods, notification re-resolution, per-call backup switching, and caller-ordered
  cleanup become explicit migration failures.
- The consequence journal adds persistence work, but it is required to distinguish a committed database change from
  unfinished external cleanup across process death.
- At-least-once delivery requires idempotent feature handlers. Exact-once filesystem execution is neither promised nor
  required.
- Cross-feature lifecycle events remain narrow and directional; Merge does not absorb Download, cover-cache, Library,
  Backup, or Profile Move policy.

## Alternatives Rejected

- Looking up the active profile inside each repository call
- Holding a database transaction open while an editor is visible
- Treating every Entry metadata update as an editor conflict
- Running filesystem cleanup inside the database transaction
- Committing membership and relying on the initiating screen to remember follow-up cleanup
- Best-effort in-memory cleanup with no restart recovery
- Rolling back committed membership after an external effect fails
- Giving Profile Move a raw group snapshot to rewrite itself
- Rebuilding backup groups against whichever profile is active when the final entry finishes
- Making an absent Download or other optional provider invalidate base Merge
