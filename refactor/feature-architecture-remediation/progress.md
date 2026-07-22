# Feature Architecture Remediation Progress

## Resume Here

- Baseline branch: `features-arch-refactor`
- Baseline commit: `1d962d406` (`chore: planning cleanup`)
- Current phase: R6 — Catalogue Feature Completion
- Phase state: complete; awaiting milestone review and commit
- Last updated: 2026-07-22
- Next action: after R6 is approved and committed, begin R7 Download Policy and Context Ownership.

## Approved Decisions

- All six high-confidence audit areas are in scope.
- Use one general execution-point/participant architecture.
- Use versioned Feature-state backup envelopes.
- Bulk Downloads operate on visible filtered children.
- Custom covers are contributed host consequences, not a standalone Feature.
- Stored child-state filters remain generic.

## Phase Ledger

| Phase | State | Milestone commit | Notes |
| --- | --- | --- | --- |
| R1 — Executable Participation Architecture | Complete | `76e4341ef` | Architecture first; no app workflow migration |
| R2 — Production Composition and Enforcement | Complete | `dd58b169e` | One module per production Feature |
| R3 — Library Membership Lifecycle | Complete | `7984e5608` | One membership boundary and discovered follow-ups |
| R4 — Entry Lifecycle Operations | Complete | `197d74fa1` | Metadata, removal, Profile move |
| R5 — Backup and Restore Participation | Complete | `456eb25d0` | Includes tracker diagnostics defect |
| R6 — Catalogue Feature Completion | Complete, awaiting commit | — | One Catalogue execution authority |
| R7 — Download Policy and Context Ownership | Pending | — | Removes Manga preference leak |
| R8 — Enforcement, Secondary Audit, and Documentation | Pending | — | Final alignment and cleanup |

## Milestones

### Phase R1 milestone — 2026-07-22

- Outcome: Added typed execution points and independently owned execution participants to the Feature Graph, including
  capability/context applicability, specialized obligations, deterministic ordering, explicit delivery/failure policy,
  runtime binding coverage, execution results, and developer reporting.
- Notable changes: `EntryInteractionComposition` now constructs a `FeatureExecutionRuntime`. A declared participant with
  no implementation, an orphan implementation, duplicate binding, undeclared point, contradictory point, invalid
  ordering reference, or ordering cycle fails composition/assembly.
- Questionable actions or decisions: execution participants must declare behavioral contracts now, while automatic
  verifier/scenario selection for those contracts belongs to R2 production enforcement. Delivery classification is
  explicit metadata; each coordinator introduced by later phases must prove it emits the point at the declared
  transactional or post-commit boundary.
- Validation performed: `spotlessCheck`; complete Feature Graph, Feature Validation, and Entry Interactions unit-test
  tasks; FOSS app Kotlin compilation; production Entry Feature developer-report generation.
- Known failures or intentionally broken compilation: none.
- Manifesto comparison: unknown participants enter the same discovery pipeline without assembler knowledge; provider
  absence remains ordinary inapplicability; missing specialized work remains an obligation; executable runtime bindings
  cannot be omitted silently; no application workflow was migrated before the general mechanism existed. R1 is not the
  end state because production Feature-module bundling and automatic execution-contract validation remain R2 work.
- Documentation impact: this resumable plan, audit, and ledger were added. Enduring contributor documentation remains
  required in R8 after the production module API and migrations stabilize.
- Expected user action: review the notable/questionable points, then request a commit and continuation to R2 if
  approved. No additional design decision is currently required.

### Phase R2 milestone — 2026-07-22

- Outcome: Replaced the independent production contributor and runtime-factory paths with 36 cohesive Feature runtime
  modules. Each module owns its graph contributor, runtime registrations, executable bindings, exposed validation
  boundaries, and warmups; production composition derives all of those artifacts from the one installed module set.
- Notable changes: Production contract validation now enters through `addEntryInteractionRuntime`, resolves every
  module-declared runtime boundary, and then validates the resulting graph, contracts, projections, and report. The
  build boundary requires every conventionally declared Feature module and every Feature contributor to belong to the
  production installation exactly once, and rejects module declarations shaped so that this coverage could be
  bypassed. Execution-participant behavioral contracts now enter the same discovered planning, execution, obligation,
  and reporting path as integration contracts. Former shared consequences are now explicitly descriptive Behavior
  projections; their IDs cannot be used as runtime dispatch keys. Merge's existing durable queue uses a separate set of
  stable compatibility keys until R3 replaces its hard-coded handlers with discovered participants.
- Questionable actions or decisions: Production still has one explicit module topology because the application needs an
  installation boundary; it is not a second statement of Feature completeness, and the build task proves every declared
  module is represented exactly once. The graph-only topology projection remains public solely for Manga and Anime
  cross-module tests; the boundary task rejects its use from production code outside the topology. Merge's existing
  durable delivery implementation remains in place during R2, but it no longer masquerades as graph participation and
  its removal is an explicit R3 milestone. No additional design decision is required unless one of these constraints is
  unacceptable.
- Validation performed: focused build-logic module-boundary tests; `spotlessCheck`;
  `checkEntryInteractionBoundaries`; complete Feature Graph, Feature Validation, Entry Interactions, and interaction
  documentation unit-test tasks; Manga and Anime production Kotlin compilation; FOSS app Kotlin compilation; production
  Entry Feature developer-report generation. The report contains 3 content types, 36 Features, 366 evaluated
  integrations, and 0 obligations.
- Known failures or intentionally broken compilation: Manga and Anime unit-test source compilation still fails in
  unchanged plugin tests that call `childList.buildDisplayList`; that operation already belongs to `missingChildGap`, so
  this is pre-existing test drift unrelated to R2. Production code for both modules compiles successfully.
- Manifesto comparison: an unknown production Feature contributor must now belong to one installable module, a declared
  module omitted from production fails the build boundary, and graph/runtime/warmup participation is derived from that
  single installation. Provider absence remains valid and no module is required to expose an operation or runtime
  artifact merely because current Features do. Descriptive behavior is represented by `FeatureBehaviorProjection`,
  which has no delivery path; independently contributed executable work must use execution participants and is
  automatically subject to runtime-binding and behavioral-contract validation. R2 therefore removes completion-list
  duplication without turning the topology into a support matrix. Application workflows have intentionally not yet
  moved onto execution points; legacy executable paths are no longer represented by descriptive projection IDs and are
  scheduled for the sequential R3-R5 migration.
- Documentation impact: the resumable plan and ledger now describe the installed production-module boundary and its
  enforcement. Enduring contributor documentation remains scheduled for R8, after the workflow migrations stabilize
  the API and examples.
- Expected user action: review only the constraints listed under questionable actions. No answer or design decision
  is needed if they are acceptable; respond `commit and continue` to commit R2 and begin R3, or name the constraint that
  should change.

### Phase R3 milestone — 2026-07-22

- Outcome: Added an always-applicable Library Membership Feature that owns duplicate preparation, profile-scoped
  category/default-state decisions, atomic persistence, commit boundaries, and structured add/remove results. Entry,
  History, Catalogue/Feed, and Library consumers now use this boundary; the former `SetEntryFavorite` and
  `EntryRemovalCleanupInteraction` paths were removed.
- Notable changes: Tracking contributes an after-commit addition participant; Merge contributes a fail-fast
  transactional removal participant; Download Maintenance contributes a capability-derived post-removal inspection;
  and custom covers contribute a host-owned post-removal participant. Category selection no longer triggers Tracking
  before membership exists. Download presence is returned as a structured UI decision instead of being independently
  probed after removal. The production module boundary now supports additional independently owned contributors while
  still installing and enforcing them through one Feature module.
- Questionable actions or decisions: Library Membership is deliberately always applicable because it needs no
  media-specific provider. Tracking, Merge, and custom-cover participation likewise run through shared Features/hosts
  for every type; Download participation alone is conditional on the Download provider. Merge, Migration, restore, and
  Profile-move workflows retain their own atomic membership transitions because they are separate single-gate
  coordinators; their remaining lifecycle participation belongs to R4/R5 rather than being forced through a UI-oriented
  add/remove command. No design decision is required unless that workflow boundary is unacceptable.
- Validation performed: `spotlessApply`; SQLDelight migration verification; complete Feature Graph, Feature Validation,
  Entry Interactions API, and Entry Interactions unit tests; focused build-logic tests; full FOSS unit tests; FOSS app
  Kotlin compilation; telemetry/updater Release Kotlin compilation; Entry interaction boundary enforcement; production
  Entry Feature developer-report generation. The report contains 3 content types, 37 Features, 3 execution points, 369
  evaluated integrations, all selected execution contracts passing, and 0 obligations.
- Known failures or intentionally broken compilation: none. One combined validation command incorrectly applied the
  telemetry property to the FOSS variant and failed at `processFossGoogleServices`; rerunning FOSS and Release validation
  as separate commands passed.
- Manifesto comparison: an unknown future content type receives Library membership, Tracking, Merge, and custom-cover
  behavior from discovered always-applicable relationships and receives Download follow-up only when it supplies the
  Download provider. Adding another participant through its owning production module automatically enters graph
  evaluation, runtime binding enforcement, behavioral-contract validation, reporting, and all applicable content types.
  Screens no longer reconstruct this consequence list, capability absence remains valid, tests exercise sequencing and
  behavior rather than declaring type support, and the content-type reference derives Library membership from the same
  Feature integration. R3 is aligned with the manifesto within its approved lifecycle scope.
- Documentation impact: the executable content-type projection now includes Library membership and the resumable ledger
  records the stable lifecycle boundary. Enduring contributor workflow documentation remains scheduled for R8 after all
  lifecycle and persistence APIs stabilize.
- Expected user action: review only the workflow-boundary decision under questionable actions. No answer is needed if it
  is acceptable; respond `commit and continue` to commit R3 and begin R4, or name the boundary that should change.

### Phase R4 milestone — 2026-07-22

- Outcome: Added three separate lifecycle Features for persisted Metadata transitions, destructive Entry removal, and
  Profile movement. Source synchronization now publishes every persisted metadata change through a host-neutral port;
  Clear Database uses the destructive-removal coordinator; and Library profile movement uses the Feature boundary
  instead of an app service that knows Merge and Feature-owned tables.
- Notable changes: Download Maintenance contributes Metadata rename, removal preparation/application, and Profile-move
  cleanup. Merge contributes transactional removal plus Profile-move preparation, destination inspection, detachment,
  and reconstruction. Tracking, child-group filtering, and cover hashing each move their own profile state. Source
  visibility and custom covers contribute post-commit consequences. Direct destructive methods were removed from
  `EntryRepository`, and the bulk SQL deletion path was removed, leaving lifecycle hosts as the application mutation
  boundary.
- Questionable actions or decisions: synchronous Profile-move planning now has an explicit `IMMEDIATE` delivery class,
  because it is neither transactional nor a post-commit/best-effort consequence. Duplicate Entries removed while
  resolving a Profile-move conflict remain part of the one Profile-move transaction rather than invoking the separate
  destructive-removal coordinator; the Profile-move plan exposes those removed Entries to discovered Download and
  custom-cover participants. This preserves atomicity without hiding their consequences. No answer is needed unless
  either classification should change.
- Validation performed: `spotlessCheck`; complete FOSS unit tests; SQLDelight migration verification; Entry interaction
  boundary enforcement; production developer-report generation; telemetry/updater Release Kotlin compilation; focused
  Domain Metadata and Entry lifecycle tests; and build-logic tests. The report contains 3 content types, 40 Features,
  11 execution points, 378 evaluated integrations, all selected contracts passing, and 0 obligations.
- Known failures or intentionally broken compilation: none.
- Manifesto comparison: a future lifecycle participant is contributed by its owning Feature module and automatically
  enters applicability, runtime binding, contract validation, reporting, and execution. The lifecycle coordinators no
  longer name current Feature implementations or their tables. Always-valid lifecycle behavior applies to an unknown
  content type, Download consequences appear only when its Download provider exists, and child-group state movement
  appears only when that capability exists. Metadata participants receive the complete persisted transition rather
  than a title-only signal. R4 therefore removes the fixed integration lists identified in the audit without creating
  a new type matrix or mandatory capability.
- Documentation impact: the executable content-type projection now includes Metadata propagation, destructive removal,
  and Profile movement. The resumable findings and ledger record their ownership and failure semantics. Enduring
  contributor workflow documentation remains scheduled for R8 after Backup, Catalogue, and Download-policy APIs
  stabilize.
- Expected user action: review only the two choices under questionable actions. No response is needed if they are
  acceptable; respond `commit and continue` to commit R4 and begin R5, or identify the classification that should be
  changed.

### Phase R5 milestone — 2026-07-22

- Outcome: Added an always-applicable Entry Backup Feature with discovered snapshot, transactional restore, and
  post-entry finalization execution points. `EntryBackupCreator` and `EntryRestorer` now invoke only this generic
  boundary; they no longer inject or enumerate Viewer Settings, Playback Preferences, Progress, child-group filters,
  Merge, Tracking, or Download Configuration.
- Notable changes: `BackupEntry` now carries additive opaque Feature-state envelopes with a stable owner ID, an
  owner-defined schema version, and payload bytes. Each current state model, ID, version, participant declaration, and
  runtime binding lives with its owning Feature. Current state is dual-written to the older typed fields, and legacy
  Manga, Anime, generic, and profile-scoped records are translated into envelopes only when a current envelope is
  absent. Merge accumulates its own restore state by session/profile/type and finalizes after every referenced Entry is
  available; that mutable state belongs to the caller-owned restore session rather than a production singleton.
  Tracking now owns profile-scoped backup persistence through its host while preserving the former merge semantics for
  existing records, and Download Configuration owns the portable quality/state mapping instead of generic backup code.
  Tracker diagnostics now use `Backup.allEntries()`.
- Questionable actions or decisions: the finite compatibility adapter deliberately enumerates the seven historical
  typed wire fields so older backups remain readable and older Katari releases can read current known state. It is not
  consulted for participant discovery or execution, and future Features do not edit it. Restore finalization is
  classified `IMMEDIATE` because it occurs after Entry batches rather than inside their transaction; Merge performs
  each resulting group transition atomically through its own host. A valid envelope owned by an unknown future Feature
  is ignored, while a known participant rejects a schema version it does not support. No answer is needed unless one
  of these compatibility/failure rules should change.
- Validation performed: `spotlessCheck`; full FOSS unit tests; focused current, legacy, Anime, profile, unknown-envelope,
  diagnostics, and Feature-state compatibility tests; Entry Interactions tests; SQLDelight migration verification;
  Entry interaction boundary enforcement; FOSS app Kotlin compilation; telemetry/updater Release Kotlin compilation;
  build-logic tests; production developer-report and content-type-reference generation. The report contains 3 content
  types, 41 Features, 14 execution points, 381 evaluated integrations, all selected contracts passing, and 0
  obligations.
- Known failures or intentionally broken compilation: none.
- Manifesto comparison: a new Feature adds its state contract, schema version, participant declarations, and bindings
  only in its owning module. The generic backup coordinator, creator, restorer, wire envelope, production topology, and
  content-type list require no Feature-specific edit. Capability absence remains ordinary inapplicability; unknown
  state remains harmless; current Feature codecs and restoration behavior are selected from the same graph and runtime
  installation; and tests cover observable round trips and sequencing rather than declaring type support. R5 is
  aligned with the manifesto.
- Documentation impact: the generated content-type reference now includes the completed shared lifecycle and backup
  Features. The backup/restore guide documents Feature-owned state, unknown-state behavior, dual writing, and legacy
  compatibility. Enduring contributor workflow documentation remains scheduled for R8 after remaining Feature APIs
  stabilize.
- Expected user action: review only the two compatibility/finalization choices under questionable actions. No answer
  is needed if they are acceptable; respond `commit and continue` to commit R5 and begin R6, or identify the rule that
  should change.

### Phase R6 milestone — 2026-07-22

- Outcome: Expanded `EntryCatalogueFeature` from source description into the one application boundary for source
  discovery, availability, descriptions, filters, background search, popular/latest/search paging, and normalized
  operation failures. Raw `EntryCatalogueSource` and `UnifiedSource` catalogue execution now exists only in the
  Feature-owned provider host adapter.
- Notable changes: Catalogue screens, chronological feeds, global search, manual and automatic Migration search,
  extension metadata, Library orientation, feed presentation, browse settings, and Anime download-cache recovery now
  consume source-ID-based Feature models/results. Paging and interactive search still persist displayed Entries through
  `NetworkToLocalEntry`; automatic smart Migration still keeps candidates ephemeral until selecting a winner. The old
  data repository/paging implementation, `GetRemoteCatalog`, `CatalogSource`, conversion helper, and Catalogue-specific
  `SourceManager` methods were removed. Stable serialized popular/latest query values remain in an app-owned transport
  adapter so existing feed/navigation state remains readable without retaining the old interactor.
- Questionable actions or decisions: Anime Download Cache is constructed while type runtime modules are installed,
  before the root Catalogue Feature can be resolved. It therefore holds a lazy Feature accessor and resolves it only
  when cache renewal runs, after composition is complete. The cache still has no raw Catalogue path. The neutral
  `EntrySourceDescriptionResolutionPort` also remains for Domain source assembly, but application consumers cannot use
  it and build enforcement reserves it for that assembly seam. No answer is needed unless either boundary should be
  redesigned.
- Validation performed: `spotlessCheck`; full FOSS unit tests; focused Catalogue Feature, filter-loader, and smart
  Migration tests; Entry interaction boundary enforcement and its build-logic tests; SQLDelight migration verification;
  FOSS and telemetry/updater Release Kotlin compilation; production contract validation; developer-report and
  content-type-reference generation/verification. The report contains 3 content types, 41 Features, 14 execution
  points, 381 evaluated integrations, all selected contracts passing, and 0 obligations.
- Known failures or intentionally broken compilation: none. One combined validation command incorrectly applied the
  telemetry property to FOSS documentation generation and failed at `processFossGoogleServices`; rerunning the FOSS
  and Release validations separately passed.
- Manifesto comparison: installing an unknown future `EntryCatalogueSource` makes it discoverable through the same
  Feature without edits to screens, search models, feeds, Migration, cache recovery, or `SourceManager`. Provider
  absence remains valid missing/unsupported availability. Returned Entry types remain authoritative, shared Catalogue
  contracts execute through the Feature for every graph-selected content type, and build validation rejects future
  direct or indirect raw provider dispatch. Availability, behavior, presentation facts, failure normalization, and
  source documentation now derive from one Feature authority rather than parallel wrappers and repositories. R6 is
  aligned with the manifesto.
- Documentation impact: the SDK capability guide now states that one Catalogue provider registration derives all
  application discovery surfaces; the generated content-type reference remains current. The detailed general Feature
  contributor workflow remains scheduled for R8 after the remaining Download policy migration stabilizes.
- Expected user action: review only the lazy Anime cache boundary and retained Domain assembly port described under
  questionable actions. No decision or additional feedback is required if both are acceptable. Respond
  `commit and continue` to commit R6 and begin R7, or identify the boundary that should change.

## Milestone Template

At every phase stop, replace this template with a dated entry:

### Phase RX milestone — YYYY-MM-DD

- Outcome:
- Notable changes:
- Questionable actions or decisions:
- Validation performed:
- Known failures or intentionally broken compilation:
- Manifesto comparison:
- Documentation impact:
- Expected user action:
