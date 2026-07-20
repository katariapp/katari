# F11 — Entry Source Migration

Status: Complete

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

- F09 owns consumption applicability and media mutation; F11 owns Migration-specific consumed-state transfer only when
  that capability composes with Migration.
- F10 owns bookmarking applicability and media mutation; F11 owns Migration-specific bookmark-state transfer only when
  that capability composes with Migration.
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
  rules for Consumption, Bookmarking, Progress, Playback Preferences, Viewer Settings, Downloads, or Merge.
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
| Execution owner | The legacy `MigrateEntryUseCase` owned the complete workflow, read ambient preferences, returned `Unit`, and swallowed every non-cancellation failure. F11.3 removed it rather than retaining a parallel coordinator; `EntryMigrationFeature` now owns primary execution. |
| Target synchronization | Source synchronization is a required precondition through a purpose-specific host operation. A sync failure cannot be reported as a successful migration. |
| Child state transfer | F11 owns source/target matching and the captured child-state option. Consumed and bookmarked transfer are independent Migration + F09/F10 relationships; provider absence must omit only that portion. Fetch-state policy remains F11-owned contextual state. Matching and resource mappings are shared policy, not Manga logic. |
| Progress | F11 supplies resource mappings to F15. Provider absence is a valid skipped result; incompatible types are rejected before execution. |
| Playback preferences | F11 invokes F16 without an Anime gate. The owning Feature decides applicability. |
| Viewer settings | F11 invokes F25. Deleting the legacy use case removed its Manga bitfield branch; F11.4 must restore the required normalization behind F25 rather than recreating that type branch. |
| Categories, notes, and Entry state | Selected categories/notes plus favorite, added-date, child flags, and replacement/copy state are part of the owned primary transition. Callers do not update these independently. |
| Tracking | Enhanced and ordinary track transfer remains tracker-owned external behavior invoked through an F11 host consequence with explicit source and target identity. |
| Downloads | The selected cleanup option invokes F08 only after F11 applicability. Unsupported Download behavior is a structured skipped relationship, not a Migration failure. |
| Custom cover | Contextual cover availability is inspected by the app host; selected copy is an explicit external consequence with a reported outcome. |
| Merge cooperation | Replace mode invokes `EntryMergeMigrationFeature`; F11 never reads or rewrites membership. F11.3 removed the two legacy raw Merge calls and made the narrow Feature participate in the primary transaction. |
| Error, cancellation, and partial work | Replace the blanket catch with explicit preparation rejection, operational failure, consequence outcomes, and cancellation propagation. F11.2 fixes the exact primary transaction and external-effect ordering before implementation. |
| Preferences | Stored migration flags and source/search preferences remain UI defaults. Execution uses the captured intent snapshot and never rereads ambient preferences midway through a workflow. |
| Tests | F11.3 replaced `MigrateEntryUseCaseTest` with a synthetic-provider shared behavior contract plus matching, state-transfer, strict persistence, replay, failure, cancellation, and transaction tests. F11.4/F11.6 extend consequence coverage. |
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

Implementation record:

- `EntryMigrationFeature` is now the application boundary for availability, Library-selection preparation, pair
  preparation, and execution. Pair preparation issues an opaque reference; execution receives that reference, explicit
  copy/replace mode, and a captured set of user-selectable options.
- Results distinguish rejection, preparation failure, stale preparation, primary-transition failure, and applied work.
  Applied work exposes only an aggregate complete/incomplete follow-up state, so callers cannot reconstruct or drive the
  internal consequence pipeline.
- The first host contract is deliberately preparation-only: an explicit profile host can inspect the authoritative
  source/target pair and custom-cover context. Mutation, synchronization, transaction, and external-effect ports remain
  absent until F11.2 establishes their semantics.
- The feature contribution derives the complete base Migration workflow from Migration provider presence. Consumption,
  Bookmarking, Progress, Playback Preferences, Viewer Settings, and Downloads are independent pairwise relationships
  selected only when both Migration and the owning capability are present.
- A synthetic unknown content type with a Migration provider receives every base consequence and the shared behavior
  contract. Adding its Progress provider activates Progress copy without changing its base Migration relationship or a
  production-type list.
- Build enforcement prevents application consumers from retaining the transitional capability facade, support methods,
  or legacy use case; prevents F11-owned code from reading ambient preferences or authorizing concrete `EntryType.*`
  values; and reserves F11 host ports for the root coordinator and segregated app adapters.
- The complete boundary queue is now 20 findings across seven files. Five were the earlier generic/F12 findings; the 15
  newly exposed F11 findings identify the legacy use case and its DI/UI consumers, ambient option reads, duplicated
  support/selection gates, and the Manga-specific branch. These are migration input, not allowlisted exceptions.
- F11.1 does not provide a compatibility implementation, bind a coordinator, define mutation semantics, or migrate a
  consumer. Those remain F11.2-F11.5 work.

Validation record:

- Formatting, build-logic tests, API/SPI compilation, root interaction tests, and Manga/Anime/Book interaction
  compilation pass.
- The full boundary task intentionally fails with the recorded 20-item F11 queue. Running formatting checks with that
  migration gate excluded passes.
- FOSS application compilation still reaches the already-exposed raw F11/F12 migration consumers and unrelated
  branch-level application errors. F11.1 changes no application source and adds no compatibility path to conceal them.

### F11.2 — Primary transaction and external-effect semantics

- Trace the actual transaction, idempotency, and retry behavior of Entry/child state, progress, settings, tracking,
  downloads, cover files, and Merge replacement.
- Record the authoritative order, failure result, and retry rule for every step.
- Decide the atomic primary boundary and whether any external effect requires durable retry.
- Change no production behavior before the semantic decision is accepted.

Exit gate: every failure point has one documented outcome and no step relies on the current blanket catch.

Review request: approve transaction, partial-failure, and retry semantics.

Decision: approved on 2026-07-19.

Audit and proposal record:

- Decision proposal
  [`0021-migration-transaction-and-consequence-semantics.md`](../decisions/0021-migration-transaction-and-consequence-semantics.md)
  records the complete operation order, primary database boundary, durable follow-up, retry, replay, cancellation, and
  conflict outcomes. F11.2 changes no production behavior or host mutation API.
- Preparation remains mutation-free. Execution revalidates the pair, strictly synchronizes the target against the
  captured profile, prepares immutable consequence payloads, commits one primary transaction, and only then delivers
  external or independently owned Feature consequences.
- The primary transaction owns F11 Entry/Library/category/normalized child state and prepared tracking rows. Replace
  mode requires narrow F12 transaction participation so Merge membership cannot diverge from Library ownership.
- F15 Progress, F16 Playback Preferences, F25 Viewer Settings, selected F08 Download removal, and selected custom-cover
  promotion use durable at-least-once delivery with owner-produced immutable payloads. Retries never reinterpret mutable
  source state.
- A stable Feature-issued operation identity makes a committed execution replayable after timeout or process death and
  prevents the same preparation from committing a different second intent.
- The audit found two required corrections outside the new F11 coordinator: the strict target-sync path cannot accept
  active-profile writes or swallowed chapter persistence failures, and F08 cannot acknowledge Download removal until
  type-owned deletion reports verified filesystem/cache completion.
- Download owners are captured before Replace changes Merge membership. Custom-cover bytes are staged before commit and
  promoted afterward, avoiding both pre-commit visible mutation and post-commit loss of the original input.
- An applied operation with pending or failed durable consequences remains applied and reports `INCOMPLETE`; it is never
  rolled back or mislabeled as complete. Aggregate status and retry remain Feature-owned rather than caller-driven.

### F11.3 — Shared coordinator and primary transfer

- Implement graph-selected applicability, preparation, optimistic validation, strict explicit-profile target
  synchronization, shared child matching, and the primary Entry/Library/category/child/tracking state transition.
- Add stable operation/replay records and the owned durable-consequence storage boundary without yet installing
  cross-feature payload handlers.
- Make F12 Merge replacement participate in the Replace transaction without exposing membership to F11.
- Install the purpose-specific app host adapter and remove `MigrateEntryUseCase` as an orchestration authority.
- Return structured results for rejected, failed, and applied operations.

Exit gate: primary Migration behavior is feature-owned, profile-safe, replayable, and atomic with Merge replacement,
without optional post-commit consequences or UI fallbacks.

Review request: verify shared copy/replace behavior and primary failure outcomes.

Implementation record:

- `DefaultEntryMigrationFeature` now derives Migration, Consumption-transfer, and Bookmark-transfer applicability from
  the evaluated provider graph. It owns selection, mutation-free preparation, optimistic references, strict target
  synchronization, shared child matching, copy/replace state preparation, structured failures, and cancellation.
- Execution is profile-pinned from the Feature-issued reference. The strict synchronization path uses an explicit
  profile for Entry writes and verifies Entry update counts, chapter removals, updates, and insertions instead of
  accepting the existing repositories' best-effort failure suppression.
- One purpose-specific application host prepares tracking rows and commits Entry/Library/category/child/tracking state,
  the stable operation record, and any prepared consequence records in one optimistic database transaction. It does not
  expose general Entry or child mutation APIs.
- Replace mode calls the narrow F12 Migration cooperation Feature inside that transaction. The production composition
  gives F11 and F12 the same `DatabaseHandler`; nested participant work uses SQLDelight savepoint semantics and rolls
  back with the outer transaction. F11 never reads or writes Merge membership.
- The Feature-issued operation identity and persisted intent fingerprint are checked before synchronization. A replay of
  an already committed operation returns its applied complete/incomplete aggregate even after Replace changed the source
  Library state; reuse with different intent conflicts.
- SQLDelight migration 38 adds F11 operation and durable-consequence storage. No consequence delivery handler is
  installed in F11.3, so the storage boundary exists without prematurely implementing F11.4.
- `MigrateEntryUseCase` and its dependency-injection binding are removed as parallel orchestration authority. Existing
  dialogs and screen models remain deliberately unmigrated and non-compiling until F11.5; no compatibility shim or UI
  fallback was added.
- Shared behavior tests cover provider absence, graph-derived child relationships, copy/replace primary preparation,
  Merge delegation, replay, and strict-sync failure. Domain tests prove profile-pinned Entry writes and verify swallowed
  child insertion/removal failures. Data tests prove operation/consequence persistence and outer rollback of nested
  participant work.

Validation record:

- Formatting, build-logic tests, API/root/domain/data compilation, the shared Migration tests, strict synchronization
  tests, persistence transaction tests, and SQLDelight migration verification pass.
- The boundary queue is reduced from 20 findings across seven files to 10 findings across five application consumer
  files. All remaining findings are the intentional F11.5 Entry, Library, dialog, and list migrations; no F11.3 host,
  coordinator, raw provider, ambient preference, or concrete type authorization is allowlisted.
- FOSS application compilation reports only those intentionally unmigrated F11 consumers plus the already-recorded
  unrelated debug-launcher and profile-shortcut errors. It reports no F11.3 host, schema, runtime-wiring, or strict-sync
  compilation error.

Manifesto comparison:

- Participation comes only from provider presence; absence remains valid and there is no production type matrix.
- The shared Feature owns the workflow and its ordered primary transition; type modules contribute no Migration-specific
  checklist or follow-up registration.
- Cross-feature Merge work stays behind F12, profile identity is explicit, failures are structured, replay is stable,
  and build breakage remains visible until consumers conform to the architecture.
- Optional post-commit relationships remain assigned to F11.4 rather than being approximated inside the primary path.

### F11.4 — Cross-feature and external consequences

- Integrate immutable F15 Progress, F16 Playback Preferences, F25 Viewer Settings, selected F08 Download maintenance,
  and staged custom-cover payloads according to F11.2.
- Add at-least-once delivery, aggregate consequence status/retry, and the required F08/F15/F25 cooperation contracts.
- Restore legacy viewer-state normalization behind F25 and prove no raw Merge cooperation call is reintroduced.
- Prove that provider absence skips only the owning optional relationship.

Exit gate: the coordinator owns the complete consequence pipeline without raw dispatch or a caller-supplied checklist.

Review request: verify transfer completeness and any user-visible partial-effect reporting.

Implementation record:

- F15 Progress, F16 Playback Preferences, and F25 Viewer Settings now prepare target-bound immutable values before the
  primary transition and expose idempotent apply operations. Durable delivery never calls their source-reading `copy`
  operations.
- F15 owns resource-key translation in its prepared progress snapshot. F25 owns portable override filtering and
  type-provided legacy viewer-flag normalization; the Migration coordinator contains no Manga/Anime/Book branch.
- F08 captures the concrete Download owners that currently contain downloads before Replace can change Merge
  membership. Applying that plan verifies every captured owner afterward and returns `Incomplete` while any provider
  still reports content. F12 Merge cleanup now observes the same verified result instead of acknowledging blindly.
- Selected custom-cover bytes are staged under the stable operation identity before commit, promoted from the immutable
  stage after commit, retained across failed delivery, and discarded after acknowledgement. Startup cleanup removes a
  bounded batch of old stages that have no durable consequence.
- One durable delivery coordinator dispatches artifact IDs to their owning Features, records retry time and error,
  propagates cancellation, and reports only aggregate complete/incomplete state. A separate Feature exposes aggregate
  pending/failed status and retry without exposing the consequence checklist.
- Preparation now derives custom-cover and Download-removal option availability from current owner state. Provider
  absence omits only that optional relationship; it does not invalidate the content type or the base Migration.
- The feature graph records explicit Migration cooperation contracts for Progress, Viewer Settings, and verified
  Download cleanup. The only Merge call remains the previously approved narrow transaction-participation Feature.

Validation record:

- Formatting, root interaction behavior tests, durable-consequence persistence tests, and focused API/SPI/root/data
  compilation pass.
- FOSS application compilation reaches only the intentional F11.5 consumer queue plus the previously recorded unrelated
  debug-launcher and profile-shortcut errors; it reports no F11.4 payload, host, SQL, runtime-wiring, or type-module
  compilation error.

Manifesto comparison:

- Optional relationships are discovered from the same provider evidence as their owning Features; the coordinator has
  no content-type matrix and no caller-supplied consequence list.
- Each owner defines the data needed for retry and the completion semantics of applying it. Adding a provider activates
  the relationship through graph evaluation instead of requiring another Migration-specific type edit.
- Durable records make forgotten or failed follow-up visible and retryable after release rather than silently reporting
  success. Missing providers remain ordinary unsupported behavior and skip only their own consequence.
- Presentation remains deliberately unmigrated until F11.5, so compilation is not being restored with a parallel
  authority or compatibility checklist.

### F11.5 — Entry, Library, Browse, search, configuration, and dialogs

- Migrate the three transitional facade consumers and every already-compiling Migration surface.
- Derive action visibility, selection, source rows, target acceptance, option availability, and execution from F11
  preparation/results.
- Keep source search mechanics and preferences in their owners while removing their ability to authorize Migration.

Exit gate: unsupported Entries cannot enter a silent no-op flow, and a newly contributed provider reaches every current
UI path without another type edit.

Review request: verify Entry/Library/Browse behavior for Manga, Anime, Book absence, mixed selections, and failures.

Implementation record:

- Entry actions and Library selection now consume `EntryMigrationFeature` availability and selection results. Library
  selection preserves the rule that every selected Entry must participate, while mixed participating types remain
  valid.
- Feature-issued profile/Entry subjects are retained through Entry, Library, Browse selection, configuration, search,
  and batch-list navigation. Loading a source Entry no longer silently switches identity if the active profile changes
  during the workflow.
- Browse source rows and counts are projected from Entries with available Migration behavior. Source-entry selection
  applies the same availability result, so a source containing only unsupported Entries is absent instead of leading to
  a no-op screen.
- Automatic, global, and source-specific target paths retain their source-owned search mechanics and same-type
  presentation optimization, but a target is accepted only after F11 preparation succeeds.
- The dialog renders only Feature-prepared options. Stored flags remain UI defaults and are translated once at the
  presentation boundary, intersected with current available options, captured for execution, and never used as
  authorization or as a consequence checklist.
- Single and batch execution now prepare and execute through F11. Applied results alone advance or close the workflow;
  rejection, conflict, and operational failure remain visible instead of being swallowed as success. Batch execution
  snapshots defaults once and reports any failed item rather than navigating away as if the whole batch completed.

Validation record:

- Formatting and the Entry interaction boundary task pass with no legacy Migration consumer findings.
- FOSS application compilation reports only the previously recorded unrelated debug-player callback and profile
  shortcut errors. It reports no F11.5 consumer, navigation, option, or result-handling error.

Manifesto comparison:

- UI support follows Feature results; no Manga/Anime/Book matrix or parallel support query was introduced.
- A future Migration provider automatically reaches Entry actions, Library selection, Browse source/Entry filtering,
  search acceptance, option preparation, and execution through the same Feature boundary.
- Preferences remain defaults rather than support truth, unsupported Entries do not enter a silent no-op path, explicit
  profile identity survives navigation, and failures are not converted to unconditional completion.
- Search and source mechanics remain with their existing owners while Migration authorization and orchestration remain
  exclusively Feature-owned.

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

Implementation record:

- `EntryCapabilityInteraction`, its provider-backed dispatcher, the aggregate `EntryInteractions.capability` field, and
  the unused provider-index projection are removed. The Migration provider binding remains the one authoritative
  participation fact consumed by graph evaluation, not an operational facade available beside F11.
- Application and lower-layer consumers are generically blocked from public SPI provider contracts. The generic rule
  now also discovers public top-level `Entry…Capability` properties, closing a gap where a caller could import the
  binding property even though provider classes and interfaces were already protected.
- Migration-specific enforcement retains only rejection of known legacy orchestration/support vocabulary, ambient
  authority inside F11, concrete Entry-type authorization, and host-port borrowing. Type modules remain allowed to own
  ordinary Migration provider participation.
- The final cancellation audit corrected two target-search synchronization catches so cancellation cannot be converted
  into a missing match. Search failures remain source-search outcomes; F11 preparation and execution failures remain
  structured Feature results.
- The public content-type reference now records the executable result: Manga and Anime support source Migration, while
  Book's missing provider remains valid unavailable behavior.

Validation record:

- Formatting, build-logic enforcement tests, the repository Entry interaction boundary, the shared F11 behavior
  contract, and Manga/Anime/Book interaction suites pass.
- The production census finds no legacy facade, dispatcher, use case, or support method outside the enforcement fixtures
  that deliberately prove those paths are rejected.
- FOSS application compilation continues to report only the pre-recorded unrelated debug-player callback and profile
  shortcut errors; no F11 production error remains.

Manifesto comparison:

- No central content-type or capability allowlist was added. Capability-property protection is discovered from SPI
  declarations, and provider presence remains the sole Migration participation fact.
- No per-type matrix, mandatory interaction, capability-label assertion, feature-specific substitute architecture,
  compatibility authority, raw application dispatch, or type branch remains in the completed Migration path.
- The relationship graph, owner-produced optional consequences, durable obligations, shared behavioral contract,
  Feature-facing presentation, and public documentation all describe the same executable support result.
- An unknown future type activates the complete workflow by contributing its owned Migration provider. Missing optional
  providers skip only their own relationships; missing follow-on work after satisfied prerequisites remains visible to
  graph/enforcement validation.

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
