# F11 — Entry Source Migration

Status: F11.0 inventory and phase split implemented; architecture decisions awaiting review

## Architectural Classification

Entry source Migration is an optional fundamental capability with a shared product workflow. Accepted decisions
`0005-current-product-outcomes.md` and `0015-migration-and-merge-provider-boundary.md`, together with inventory row
`T09`, establish the current product result: Manga and Anime participate, while Book does not. Provider presence is the
sole participation fact; provider absence is valid and must make the complete workflow unavailable without a separate
false declaration.

The media providers do not own the shared transfer algorithm. F11 owns preparation, validation, ordering, execution,
and results once a type contributes Migration. Same-type requirements, explicit profile identity, source compatibility,
Library membership, selection shape, current data, and requested transfer options are operation context rather than
additional type support flags.

Existing consequences remain independently owned:

- F15 owns progress copy and resource mappings.
- F16 owns playback-preference copy.
- F25 owns viewer-setting copy and legacy viewer-state normalization.
- F08 owns Download inspection and removal.
- F12 owns Merge-member replacement.
- Sources, trackers, categories, covers, and Entry/child persistence remain external application services reached through
  purpose-specific F11 host ports.

F11 is specifically entry-to-entry source migration. App-version migrations, Mihon backup import, profile preference
migration, SQLDelight schema migrations, and extension-development migration guides are separate systems and are not
absorbed because they share the word "migration."

## Non-Negotiable Boundary

- Application surfaces consume `EntryMigrationFeature`; they cannot query `EntryMigrationProvider`,
  `EntryMigrationCapability`, `EntryCapabilityInteraction`, provider maps, or graph evaluation.
- Callers submit concrete user intent and an explicit option snapshot. They do not choose or reconstruct the ordered
  consequence pipeline.
- F11 validates both Entries, capability applicability, type, profile, source/target identity, and optimistic references
  before executing.
- Provider absence is a structured unavailable result. It is not an exception, invalid content type, false provider, or
  reason to suppress unrelated interactions.
- Cross-feature work uses the owning Feature contract. F11 cannot dispatch raw providers or duplicate applicability
  rules for Progress, Playback Preferences, Viewer Settings, Downloads, or Merge.
- Host ports expose only source synchronization, F11-owned persistence transitions, and external effects needed by the
  workflow. Screens and other features cannot borrow them as general repositories.
- Cancellation propagates. Operational failures become structured outcomes and cannot be swallowed while callers report
  success.
- No concrete Manga/Anime/Book gate, production-type matrix, capability-label test, or caller-owned completion checklist
  may survive the migration.

## Complete Migration Control Surface

The initial boundary task reports only five failures, but they are not the complete F11 scope. The production census
also includes code that still compiles because it never consulted the former capability facade.

| Area | Existing paths and required disposition |
| --- | --- |
| Type participation | Manga and Anime Migration bindings remain the authoritative current providers; Book contributes no absence declaration. The empty compatibility dispatcher is replaced by a graph-selected Feature, not another support query. |
| Entry action | Entry action visibility and launch preparation use F11 results rather than `supportsMigration`. |
| Library action | Multi-selection readiness and selected-entry projection use F11. Preserve the existing rule that every selected Entry must participate; mixed participating types may remain selected because each Entry receives its own same-type target search. |
| Browse migration source list | Source rows and their favorite counts must exclude Entries that F11 cannot migrate. The current path exposes Book sources and reaches a silent no-op. |
| Source-entry selection | `MigrateEntriesScreenModel`, selection, and configuration receive only F11-ready Entries and retain explicit identities through navigation. |
| Automatic and manual target search | Search keeps source-owned catalogue behavior, but target filtering and acceptance use an F11 preparation result. Same-type filtering may optimize presentation but cannot be the authority. |
| Configuration and dialog options | Chapter state, categories, notes, custom cover, and Download cleanup are explicit intent options. Contextual availability comes from current Entry state and owning Features, not a type table. |
| Execution owner | `MigrateEntryUseCase` currently owns the complete workflow, reads ambient preferences, returns `Unit`, and swallows every non-cancellation failure. Its authority moves behind `EntryMigrationFeature`; it is removed rather than retained as a parallel coordinator. |
| Target synchronization | Source synchronization is a required precondition through a purpose-specific host operation. A sync failure cannot be reported as a successful migration. |
| Child state transfer | F11 owns matching source/target children and transferring consumed/bookmarked/fetch state when requested. Matching and resource mappings are shared policy, not Manga logic. |
| Progress | F11 supplies resource mappings to F15. Provider absence is a valid skipped result; incompatible types are rejected before execution. |
| Playback preferences | F11 invokes F16 without an Anime gate. The owning Feature decides applicability. |
| Viewer settings | F11 invokes F25. The Manga legacy bitfield normalization still present in `MigrateEntryUseCase` must move behind F25 rather than becoming an F11 type branch. |
| Categories, notes, and Entry state | Selected categories/notes plus favorite, added-date, child flags, and replacement/copy state are part of the owned primary transition. Callers do not update these independently. |
| Tracking | Enhanced and ordinary track transfer remains tracker-owned external behavior invoked through an F11 host consequence with explicit source and target identity. |
| Downloads | The selected cleanup option invokes F08 only after F11 applicability. Unsupported Download behavior is a structured skipped relationship, not a Migration failure. |
| Custom cover | Contextual cover availability is inspected by the app host; selected copy is an explicit external consequence with a reported outcome. |
| Merge cooperation | Replace mode invokes `EntryMergeMigrationFeature`; F11 never reads or rewrites membership. The two raw Merge calls are removed. |
| Error, cancellation, and partial work | Replace the blanket catch with explicit preparation rejection, operational failure, consequence outcomes, and cancellation propagation. F11.2 fixes the exact primary transaction and external-effect ordering before implementation. |
| Preferences | Stored migration flags and source/search preferences remain UI defaults. Execution uses the captured intent snapshot and never rereads ambient preferences midway through a workflow. |
| Tests | Replace the Manga/Anime/Book support matrix in `MigrateEntryUseCaseTest` with a synthetic-provider shared behavior contract. Retain genuine matching, state transfer, optional-consequence, failure, and cancellation behavior tests. |
| Documentation | Correct Anime Migration to supported and keep Book unavailable in the content-type reference. Document observable failure/result changes if F11.2 changes them. |

## Decisions Required Before Implementation

### Participation authority

Recommended and already consistent with accepted `T09`: retain optional provider-backed participation. Manga and Anime
contribute Migration; Book does not. The provider is the one owned participation fact, not a second UI flag. Adding a
future provider must activate Entry, Library, Browse, search, execution, shared contracts, and every applicable
cross-feature consequence without another type-specific edit.

### Failure and transaction semantics

Recommended direction:

- preparation is mutation-free and returns an opaque optimistic reference or a specific rejection;
- target synchronization completes before the primary transfer begins;
- F11.2 identifies the largest database-owned primary transition that can be atomic without pretending network, tracker,
  filesystem, or Download effects are transactional;
- inapplicable optional Features are recorded as skipped, while an invoked operation's failure is recorded as failure;
- external effects have explicit per-effect outcomes, and execution cannot return unconditional success after a partial
  failure;
- cancellation is always rethrown;
- replace mode changes Library ownership and Merge membership only through the owned transition after required transfer
  data has been prepared.

F11.2 must inventory idempotency and retry safety before choosing between immediate structured partial results and a
durable retry mechanism. It must not silently introduce a journal merely to imitate F12, nor preserve the current blanket
best-effort catch merely to avoid a product decision.

### Availability and selection semantics

Recommended preservation and correction:

- an Entry action is available only when its provider-backed F11 relationship applies;
- a Library selection is ready only when it is non-empty and every selected Entry participates;
- mixed participating types remain valid at the Library selection stage, while every source/target pair must match type
  and profile;
- unsupported Entries and source rows do not enter Migration UI and cannot reach a silent execution no-op;
- source interfaces and returned Entry type remain contextual evidence, not replacements for F11 participation.

## Sequential Milestones

Each milestone is implemented by one agent, compared with the manifesto, committed separately, and stopped for review.
An unforeseen product, transaction, or ownership decision stops the milestone before implementation expands.

### F11.0 — Census, classification, and phase split

- Reconcile the five boundary failures with the complete production/test/documentation census.
- Separate entry source Migration from unrelated systems sharing its name.
- Record the final owner, contextual inputs, cross-feature consequences, and unresolved transaction semantics.
- Define implementation milestones before changing runtime code.

Exit gate: every known F11 surface has a disposition and the decisions needed before contract implementation are visible.

Review request: approve participation, availability/selection semantics, and the F11.2 failure-semantics direction.

### F11.1 — Feature contract and dependency boundary

- Define app-facing preparation, selection, intent, option, execution, and result models.
- Define segregated purpose-specific host ports without repository-shaped access.
- Contribute provider-backed base applicability and the complete set of known shared consequences.
- Add boundary enforcement that forbids the transitional facade, raw provider access, concrete type authorization, and
  host borrowing.
- Do not preserve compilation with a compatibility Feature implementation.

Exit gate: the final dependency direction is enforceable even if all current consumers no longer compile.

Review request: approve the public intent/results and host ownership before orchestration is implemented.

### F11.2 — Primary transaction and external-effect semantics

- Trace the actual transaction, idempotency, and retry behavior of Entry/child state, progress, settings, tracking,
  downloads, cover files, and Merge replacement.
- Record the authoritative order, failure result, and retry rule for every step.
- Decide the atomic primary boundary and whether any external effect requires durable retry.
- Change no production behavior before the semantic decision is accepted.

Exit gate: every failure point has one documented outcome and no step relies on the current blanket catch.

Review request: approve transaction, partial-failure, and retry semantics.

### F11.3 — Shared coordinator and primary transfer

- Implement graph-selected applicability, preparation, optimistic validation, target synchronization, shared child
  matching, resource mappings, and the primary Entry/Library state transition.
- Install the purpose-specific app host adapter and remove `MigrateEntryUseCase` as an orchestration authority.
- Return structured results for rejected, failed, and applied operations.

Exit gate: primary Migration behavior is feature-owned and profile-safe without optional consequences or UI fallbacks.

Review request: verify shared copy/replace behavior and primary failure outcomes.

### F11.4 — Cross-feature and external consequences

- Integrate F15 Progress, F16 Playback Preferences, F25 Viewer Settings, F08 Download maintenance, F12 Merge replacement,
  categories, notes, tracking, and custom covers according to F11.2.
- Remove the Manga viewer-flags branch and both raw Merge cooperation calls.
- Prove that provider absence skips only the owning optional relationship.

Exit gate: the coordinator owns the complete consequence pipeline without raw dispatch or a caller-supplied checklist.

Review request: verify transfer completeness and any user-visible partial-effect reporting.

### F11.5 — Entry, Library, Browse, search, configuration, and dialogs

- Migrate the three transitional facade consumers and every already-compiling Migration surface.
- Derive action visibility, selection, source rows, target acceptance, option availability, and execution from F11
  preparation/results.
- Keep source search mechanics and preferences in their owners while removing their ability to authorize Migration.

Exit gate: unsupported Entries cannot enter a silent no-op flow, and a newly contributed provider reaches every current
UI path without another type edit.

Review request: verify Entry/Library/Browse behavior for Manga, Anime, Book absence, mixed selections, and failures.

### F11.6 — Integrated enforcement, tests, documentation, and completion

- Remove `EntryCapabilityInteraction`, its provider-backed dispatcher, obsolete composition field, support vocabulary,
  and capability-label tests.
- Run the full census and add generic rejection rules for every discovered forbidden path.
- Execute the shared behavior contract and all affected feature/application tests.
- Correct the public Anime Migration reference and reconcile the final inventory/status.
- Compare the completed feature against every manifesto rejection rule.

Exit gate: no raw Migration authority, type matrix, ambient option read, swallowed failure, caller-owned pipeline, stale
documentation, or known follow-up remains.

Review request: approve F11 and Phase 5 completion against the manifesto.

## Manifesto Review of the Plan

The plan does not organize F11 around the five current compiler errors. It starts from one provider-backed relationship
and assigns every UI, source, search, execution, transfer, external, test, and documentation consequence to an owner.
A future content type changes only its owned Migration contribution. F11 then discovers applicability, activates every
shared surface, runs the shared contract, composes every already-available optional Feature, and exposes missing
specialized obligations. Provider absence leaves the type valid and prevents the complete workflow without false
providers or presentation flags.

The plan rejects a thin wrapper around `MigrateEntryUseCase`, a new support facade, a production-type allowlist, a
caller-owned transfer checklist, and compilation-first shims. The architecture and failure semantics are established
before existing consumers are made to compile against them.
