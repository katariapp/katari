# Capability Architecture Migration Obligations

Updated: 2026-07-20

This ledger records production and test code exposed by the Phase 3.5 dependency cut. These failures are expected until
their owning phases migrate them to feature contributions and evaluated graph results. They must not be hidden with a
replacement boolean, a recreated capability catalog, an empty graph contribution, or a compatibility report facade.

This is deliberately only a compile-failure ledger. It is not the complete migration scope: code that still compiles,
code outside `entry-interactions`, and already-generic consumers can still require architectural migration. The
exhaustive pre-migration register is [`migration-inventory.md`](migration-inventory.md).

## Valid Lower Boundary

The following checks pass after the cut:

- `:feature-graph:testDebugUnitTest`
- `:entry-interactions:api:compileDebugKotlin`
- `:entry-interactions:spi:compileDebugKotlin`
- Manga, Anime, and Book Entry-interaction debug Kotlin compilation

This proves that the generic kernel and the provider-contract API point in the intended direction. Compilation above the
SPI boundary is not an exit gate for Phase 3.5.

## Active Obligations

### Exposed by F12.1: raw Merge authority and host ownership

- The application-facing F12 contract now accepts user intent and returns owned editor/workflow projections; it exposes
  neither a general membership query nor group CRUD.
- Candidate, navigation, Library grouping, and backup reads have purpose-specific application contracts. Child-owner
  and Download-owner projections are root-internal so application screens cannot use them as a membership shortcut.
  Profile lifecycle and F11 replacement APIs remain absent until F12.2 defines snapshot and transaction semantics.
- The segregated host boundary makes profile selection explicit. Mutation, transaction, snapshot, and compensation
  semantics remain deliberately absent until F12.2 decides them.
- Boundary validation reports 42 existing F12 raw-authority references. They cover the legacy Domain model, repository,
  interactors, and data implementation; application composition; Entry, Library, Catalogue, History, Updates, and
  notification consumers; backup/restore; profile lifecycle; F11 migration cooperation; download notification; and
  Book/Manga ownership paths. These are assigned to F12.3–F12.6 and are not allowlisted compatibility paths.
- Three transitional `EntryCapabilityInteraction` references remain assigned jointly to F11/F12. F12.3 removes the
  Merge half; F11 retains responsibility for Migration.
- Existing F12 host ports may be used only by the root F12 coordinator and a segregated application adapter. Raw domain
  authorities cannot cross either the application contract or host contract.
- F12.1 intentionally leaves the application boundary check failing on this migration queue. Restoring compilation by
  exporting raw membership or weakening the rule is not an acceptable resolution.

### Defined by F12.2: profile, transaction, and external-effect obligations

- Every raw active-profile Merge call must be replaced by an explicitly scoped intent, projection, background event, or
  named legacy identity resolver. A profile switch cannot redirect in-flight work.
- Editor and Profile Move workflows require feature-issued optimistic snapshots and in-transaction revalidation.
- All database effects of one Merge command must be atomic. Profile Move owns its broader outer transaction while Merge
  remains responsible for deriving and applying membership changes inside it.
- Backup creation/restoration must stop using active-profile switching as Merge identity. Restore remains best-effort by
  group with structured skipped reasons.
- Download removal and cover cleanup cannot run in the database transaction or be left to the initiating screen. The
  approved design requires durable, at-least-once, idempotent consequence delivery and visible pending failure state.
- Profile clear removes its explicit Merge query and relies on the existing foreign-key cascade inside the profile data
  transaction.
- These are F12.3–F12.6 conformance obligations. F12.2 changes no runtime behavior.

### Resolved in F12.3: shared workflow and persistence authority

- Base Merge is an unconditional feature contribution for every discovered content type. Manga and Anime no longer
  bind an empty Merge marker, and Book requires no opt-in.
- Optional Download ownership/removal is derived from the real Download provider. No Merge-wide support fact implies
  Download behavior.
- The feature owns opaque editor/member references, contextual validation, explicit-profile projections, workflow
  execution, and the narrow Migration replacement operation.
- The host adapter accepts sealed owned transitions, revalidates feature-issued expectations in one transaction, and
  atomically persists membership, preparation/Library state, and durable consequence records.
- The raw model, repository, repository implementation, and Get/Update interactors are deleted. There is no compiling
  compatibility authority beside the Feature boundary.
- Boundary validation reports 34 remaining application/domain/type consumer migrations. These failures are the input
  to F12.4-F12.6 and F11, not exceptions or a reason to restore the deleted path.

### Resolved in F12.4: Entry ownership and navigation consumers

- Entry, Library, Catalogue, History, Updates, and library-update notification routing consume F12 intents, editor,
  candidate, and navigation projections rather than raw membership reads.
- Shared child loading and unified Library loading use narrow domain resolution ports implemented by the root F12
  coordinators. Neither port exposes membership rows, CRUD, active-profile lookup, or a caller-owned consequence list.
- Manga/Anime/Book Continue, Manga reader, Anime player, Book navigation, Entry child presentation, and Download
  lifecycle selection pass concrete Entries carrying explicit profile identity into the shared child-owner resolution.
- The active boundary queue is exactly 16 findings: four F12.5 Download/notification consumers, seven F12.6
  Library/backup/profile lifecycle consumers, and five F11 Migration consumers, including its two narrow Merge
  cooperation calls. No F12.4 consumer remains.

### Resolved in F12.5: Download ownership and notification identity

- Download maintenance derives ordered concrete owners through the root-internal Merge projection only after the real
  Download provider makes the operation applicable. Merge does not authorize Download for a type without that provider.
- Book Download code no longer imports raw Merge models/repositories or maintains a membership index. Its cache, count,
  and deletion operations are scoped to one concrete owner; shared Download coordination owns merged aggregation.
- Queue and error events retain explicit profile and real-owner identity. Rendered notification destinations retain the
  originating profile and visible Entry together rather than passing unrelated optional IDs.
- New notification navigation and actions cross the profile selection/authentication gate before dispatch. Legacy
  installed payloads use one named global-ID compatibility lookup and never fall back to the active profile.
- The active boundary queue is exactly 12 findings: seven F12.6 Library/backup/profile lifecycle consumers and five F11
  Migration consumers, including its two narrow Merge cooperation calls. No F12.5 raw consumer remains.

### Resolved in F12.6: persistence and lifecycle ownership

- Library removal and source refresh paths use purpose-specific Merge Features. The source refresh migration includes
  paths that previously compiled by borrowing editor or Library grouping data, not only the seven raw boundary findings.
- Backup creation receives stable target identity plus member position. Restore is pinned to an explicit destination
  profile, is atomic per normalized group, and reports malformed, missing, conflicting, and failed groups without
  switching active-profile state for Merge.
- Profile Move uses opaque source/destination snapshots and one reserved transaction participant. The Profile Move
  coordinator supplies conflict outcomes and factual ID mappings but cannot query or persist membership itself.
- Profile clearing relies on foreign-key cascades. Durable follow-up state is exposed as an aggregate status and retried
  through feature-owned delivery rather than exposing journal rows or a caller-owned consequence list.
- The active boundary queue is exactly five findings, all assigned to F11 Migration. No F12.6 raw consumer remains.

### Verified in F12.7: integrated Merge completeness

- The production census and executable contribution agree on every F12 consequence: workflow/editor/persistence,
  candidates, navigation, child and Library ownership, lifecycle, metadata, backup, Profile Move, cascade cleanup,
  Migration cooperation, durable delivery/status, Library initialization, cover cleanup, and the independent
  provider-derived Download relationship.
- Generic enforcement rejects raw authorities and SQL access outside the host, host-port borrowing by unrelated
  features, ambient profile authority, transitional support gates, and any concrete `EntryType.*` branch in Merge-owned
  code. No current-type list is embedded in the rule.
- The only remaining boundary queue is exactly five F11 findings. F12 supplies the narrow Migration cooperation Feature;
  F11 still owns replacing its two raw callers and the three remaining capability-facade consumers.

### Exposed by F11.0: complete Entry Source Migration ownership

- The five boundary findings are only the non-compiling subset. The complete F11 census also includes Entry, Library,
  Browse source lists, source-entry selection, automatic/manual search, configuration, dialogs, shared execution,
  transfer consequences, preferences, tests, and public documentation that still compile.
- The current Browse source path admits unsupported Book Entries and reaches a silent no-op. The content-type reference
  also still marks accepted Anime Migration behavior unavailable. Both are F11 obligations, not unrelated follow-ups.
- `MigrateEntryUseCase` reads ambient options, owns a caller-visible consequence checklist, contains a Manga type branch,
  reads raw Merge authority, returns no result, and swallows every non-cancellation failure. F11.1-F11.5 replace the
  authority rather than wrapping it.
- App-version, Mihon import, profile-preference, SQLDelight, and extension-development migrations were reviewed and remain
  outside F11.

### Exposed by F11.1: final Migration contract and dependency direction

- Application consumers now have one contract for availability, selection, pair preparation, and execution. The
  contract captures user choices but does not expose provider lookup, transfer ordering, persistence operations, or a
  caller-owned consequence checklist.
- The initial host boundary is preparation-only and profile-explicit. Mutation and external-effect access remains
  unavailable until F11.2 decides atomicity, ordering, failure, and retry semantics.
- Provider-backed base Migration consequences and optional intersections with Consumption, Bookmarking, Progress,
  Playback Preferences, Viewer Settings, and Downloads are discoverable graph relationships. An unknown future type
  participates through its owned provider without a central type list.
- Boundary validation now reports 20 findings across seven production files. Fifteen F11-specific findings augment the
  five previously visible generic/F12 findings and expose the entire legacy authority path: use-case consumers, ambient
  flags, support/selection gates, and the concrete Manga branch.
- No finding is allowlisted and no compatibility Feature implementation exists. F11.2-F11.5 must make the existing code
  conform to the accepted contract rather than weakening the boundary to regain compilation.

### Exposed by F11.2: transaction and consequence prerequisites

- Target synchronization currently spans network work and multiple active-profile repository transactions. Chapter
  removal/insertion failures can be swallowed and represented as apparent success. F11 requires a strict,
  explicit-profile synchronization result before it can authorize its primary transition.
- F11-owned Entry/Library/category/normalized child state and prepared tracking rows form one optimistic primary
  database transition. Replace mode requires F12 transaction participation so Merge membership and Library ownership
  cannot commit separately.
- Progress, playback preferences, viewer settings, Download removal, and custom-cover promotion require immutable
  prepared payloads plus durable at-least-once delivery. Retrying a source-reading `copy` operation would reinterpret the
  original Migration and is rejected.
- Current F08 Manga/Anime deletion may ignore filesystem failure while evicting cache state. Captured-owner deletion and
  verified structured completion are required before a durable F11 consequence may be acknowledged.
- Download ownership must be captured before Merge replacement. Cover content must be staged before commit because its
  source file is not a durable post-commit payload.
- A stable operation record makes committed execution replayable after process death and owns aggregate consequence
  status/retry. Callers never receive journal rows or a completion checklist.
- These findings are recorded in accepted decision
  `0021-migration-transaction-and-consequence-semantics.md`; no production mutation boundary was introduced in F11.2.

### Resolved and exposed by F11.3: primary Migration ownership

- The shared provider-selected coordinator, explicit-profile strict synchronization, optimistic primary transition,
  stable operation replay, F12 transaction participation, and operation/consequence persistence are implemented.
- `MigrateEntryUseCase` and its DI binding are removed rather than wrapped. Primary Entry/Library/category/child/tracking
  work no longer has a second orchestration authority or a blanket failure catch.
- SQLDelight migration 38 introduces the durable F11 operation/consequence boundary. Cross-feature payload creation and
  delivery deliberately remain absent until F11.4.
- Boundary validation now reports exactly 10 findings across five application files. They are the intentional F11.5
  Entry, Library, dialog, and list consumers; none is allowlisted or hidden behind a compatibility implementation.
- F11.4 still owns immutable Progress, Playback Preferences, Viewer Settings, Download, and staged-cover consequences,
  including verified Download completion. F11.5 still owns every UI, Browse, source/search, and configuration consumer.

### Resolved by F11.4-F11.5: complete behavior and consumer ownership

- Cross-feature and external consequences use owner-produced immutable payloads with durable retry and aggregate status.
  Optional provider absence skips only its owning relationship; it does not invalidate base Migration.
- Entry, Library, Browse, source selection, search, configuration, dialogs, and batch execution consume F11 results.
  Explicit profile/Entry identity and captured options survive navigation and execution.
- Unsupported Entries no longer reach a silent no-op. Applied, rejected, conflicted, operationally failed, and
  incomplete-follow-up outcomes remain distinct.

### Verified by F11.6: integrated Migration completeness

- The transitional capability facade, dispatcher, aggregate field, and unused provider projection are deleted.
  Migration provider bindings remain ordinary graph-discovered participation evidence.
- Generic enforcement discovers SPI provider types and top-level `Entry…Capability` properties. Legacy support/use-case
  vocabulary, ambient coordinator authority, concrete type authorization, and host-port borrowing are rejected.
- The full census, shared behavior contract, per-type interaction suites, boundary validation, and public content-type
  reference agree: Manga and Anime participate, Book provider absence is valid, and no known F11 follow-up remains.

### Resolved in Architecture Gate 5.0: application access to raw interactions

- Provider-backed operational facades and `EntryInteractions` moved from the exported API into SPI.
- The root module no longer exposes raw facades or graph evaluation through dependency injection.
- The application compile classpath receives `entry-interactions:api`, but not SPI or Feature Graph.
- Generic boundary discovery now reports 24 application production files using 13 raw facade types. These are owned
  `F02`–`F27` migration failures, not permission to re-export SPI.

### Resolved in F01: Open feature ownership

- The Open provider is consumed by the `entry-open` contribution.
- Entry, Updates, deep-link, preview, immersive, notification, and debug dispatch use the graph-derived Open coordinator.
- No application production consumer injects `EntryOpenInteraction` directly.
- Full application validation remains blocked by the independent Download Lifecycle obligation below.

### Resolved in F02: Continue feature ownership

- The Continue provider is consumed by the `entry-continue` contribution.
- Entry, Library, and History availability and dispatch use the graph-derived Continue coordinator.
- Applicable no-next state is distinct from provider absence through the structured feature result.
- No application production consumer imports `EntryContinueInteraction` directly.

### Resolved in F03–F05: shared Download runtime, actions, and automatic policy

- F03 owns queue/runtime state, inspection, controls, worker execution, and notification rendering.
- F04 owns individual, bulk, bookmarked-bulk, retry, and notification-triggered actions; provider SPI exposes only the
  genuinely media-specific bulk candidate pool.
- F05 owns one automatic-download selection and scheduling policy for every core Download provider. The redundant
  automatic-filter capability, dispatch, type bindings, and public domain policy were removed.
- No F03–F05 application operation calls `EntryDownloadInteraction`; at that milestone the 34 remaining raw application
  references were assigned to F06–F27.

### Resolved in F06–F08: remaining Download lifecycle, configuration, and maintenance boundaries

- F06 owns structured lifecycle events, cleanup policy, download-ahead, category exclusions, physical cleanup dispatch,
  and Bookmark protection derived from Download plus Bookmarking.
- F07 owns contextual options and global specialized-setting visibility through independent provider relationships; it
  contains no per-type settings map or Manga presentation gate. Persisted option selections are backed up by data
  presence rather than an Anime gate, so a future option provider receives the same portable-data consequence.
- F08 owns cache invalidation, source/title rename, whole-Entry inspection/removal, migration cleanup, and cleanup before
  source/database purge.
- Application production code has no raw `EntryDownloadInteraction` reference. The boundary census now reports 25
  references assigned to F09–F27.

### Resolved in F09: Consumption feature ownership

- F09 owns consumed-state applicability, shared transition eligibility, mutation dispatch, Entry/Library/Updates UI
  gates, notification actions, tracking synchronization, and F06 lifecycle emission.
- Media providers return their exact changed children after genuine type-specific persistence. They no longer receive
  the Download lifecycle sink.
- Application production code has no raw `EntryConsumptionInteraction` reference. Provider absence remains valid and
  returns `Inapplicable` through the feature boundary.

### Resolved in F10: Bookmarking feature ownership

- F10 owns Bookmark applicability, individual and selection availability, and mutation dispatch for Entry and Updates
  surfaces.
- Shared selection policy filters unchanged children before the Manga provider performs its genuine persistence work.
- Application production code has no raw `EntryBookmarkInteraction`, capability report, or capability catalog reference.
  Download-plus-Bookmark consequences remain derived relationships, so a future Bookmark provider activates them
  without an application or Download edit.

### Resolved in F24: Library-update notification feature ownership

- F24 owns type grouping, channel/group/summary identity, summary and child projection, merged visible targets, and
  notification action composition through one app-facing Feature.
- F01 child Open, F09 Mark Consumed, and F04 Download are independent graph-derived relationships. Provider or
  contextual absence omits only that action; normal Entry-details navigation remains available and is not F01.
- F23 supplies vocabulary and numbering policy without authorizing participation or actions.
- `Notifications.createChannels` consumes discovered routes. Manga/Anime shipped identities survive only in a frozen
  compatibility adapter; Book and future types receive stable derived, collision-validated routes.
- `LibraryUpdateNotifier` contains no type enum, Manga fallback, vocabulary map, or direct F01/F09/F04 availability
  reconstruction.

### Resolved in F13: Update Eligibility feature ownership

- F13 owns the universal library-update eligibility policy and the consequences used by the update worker and Stats.
- Eligibility is shared behavior for every composed type rather than a provider capability or per-type opt-in. An
  uncomposed type is a composition error; a composed type is evaluated from shared preferences and runtime context.
- The redundant raw Update Eligibility provider, dispatch, and interaction facade are removed rather than retained as
  an always-present declaration.

### Resolved in F14: Library Filtering feature ownership

- F14 owns generic Library filter interpretation and active-state calculation for every composed content type.
- Bookmark-filter control availability derives from Bookmark provider evidence, while outside-release-period
  applicability derives independently from its compatibility marker. Provider absence removes only that optional
  relationship.
- The app supplies neutral preference, aggregate Library-state, tracker, local, and release-period DTO fields. It no
  longer calls raw Library-filter dispatch or reconstructs shared tri-state/tracker policy.
- Search and category/source/type grouping remain contextual Library navigation; F13 update eligibility, F03 download
  inspection, F10 bookmark mutation, and F22 Library summaries remain with their owners.

### Resolved in F15: Progress Transfer feature ownership

- F15 owns graph-derived applicability and structured snapshot, restore, and copy results for portable progress state.
- Backup create/restore and migration use the app-facing Feature; application production code has no raw
  `EntryProgressInteraction` reference.
- Internal provider dispatch is strict. Empty supported state, provider absence, and incompatible source/target types
  are no longer represented by the same empty/no-op behavior.
- Live media persistence remains type-owned; per-child labels, migration policy, and library summaries remain assigned
  to F17, F11, and F22 respectively.

### Resolved in F16: Playback-preference Transfer feature ownership

- F16 owns provider-derived backup snapshot, backup restore, and migration-copy consequences.
- Backup creation no longer uses `EntryType.ANIME` to authorize playback-preference serialization; application backup
  and migration consumers depend only on the structured Feature boundary.
- Supported data absence, provider absence, and cross-type copy are distinct outcomes. Download option selections use
  their independent F07 storage and no longer require a current-type backup gate; legacy wire conversion remains a
  separate compatibility concern.
- A synthetic provider activates every shared consequence without a concrete type list, while a partial type with no
  provider remains valid and returns structured inapplicability.

### Resolved in F17: Child List feature ownership

- F17 owns child-list applicability, reading/display order, display construction, merged headers, missing-count results,
  and the ordered-child consequence consumed by preview and immersive flows.
- Child List and Child Progress remain independent providers. Optional labels are derived only when both are present;
  either absence remains valid and returns a structured inapplicable result.
- Application production code has no raw `EntryChildListInteraction` or `EntryChildProgressInteraction` reference, and
  presentation no longer contains a Manga gate for missing-count behavior.

### Resolved in F18: Child Group Filtering feature ownership

- F18 owns provider-derived applicability, multi-member state observation, live child filtering, exclusion mutation,
  and backup snapshot/restore through one generic host data source.
- Type providers own only genuine group discovery and normalization. The raw dispatcher is strict and exposes no
  support/apply booleans, empty fallbacks, or no-op mutation.
- The former backup Manga authorization is removed. Profile movement/deletion, SQL and backup wire formats, and the
  reader/download/history/sync/update/library/Updates paths are declared shared persistence consequences rather than
  separate type opt-ins.
- Application production code has no raw `EntryChildGroupFilterInteraction` reference. Provider absence remains valid
  and yields structured inapplicability through the Feature.

### Resolved in F19: Preview feature ownership

- F19 owns provider-derived applicability, contextual source/preference availability, configuration, Entry and browse
  surfaces, loading, lazy page loading, open targets, and strict handle release.
- Child-backed providers declare their stable load mode and fail coordinator construction when the graph cannot select
  the Preview-plus-Child-List relationship. Entry-level and fixed-config Preview remain valid partial contributions.
- Preview configuration/settings and Preview-plus-Open are independent derived relationships. The settings screen no
  longer enumerates Manga and Anime as support authority, and actual opening remains F01-owned.
- Application production code has no raw `EntryPreviewInteraction` reference. F20 long-press Immersive evidence and F23
  vocabulary remain separate migrations.

### Resolved in F20: Immersive feature ownership

- F20 owns provider-derived applicability, source/entry contextual availability, catalogue/feed and long-press
  evidence, preload selection, loading, rendering, progress, Open targets, and strict handle lifecycle.
- Source opt-in remains public source-owned context. Descriptive source type metadata may only prune a source-level
  surface; returned Entry type remains authoritative and provider absence remains valid.
- Child-backed providers declare their stable load mode and fail coordinator construction when the graph cannot select
  Immersive plus Child List. Entry-level and zero-preload providers remain valid partial contributions.
- Application production code has no raw `EntryImmersiveInteraction` reference. UI concurrency, source/media runtime,
  generic long-press preferences, and F23 vocabulary retain their distinct owners.

### Resolved in F21: Related Entries feature ownership

- F21 owns contextual source availability, source-order and authoritative-type preservation, identity deduplication,
  profile-aware persistence, live Library membership, source orientation, and the Entry action/dialog consequences.
- `RelatedEntriesSource` and `EntryItemOrientationProvider` remain public source-owned inputs. An always-applicable graph
  relationship gives every composed type the shared orchestration without claiming every source supports it.
- The parallel `GetRelatedEntries` application/domain boundary is removed. Application production code uses only
  `EntryRelatedEntriesFeature`; source missing and source unsupported are structured while network failures remain
  retryable errors.
- Returned Entry-details navigation is not F01 Open. The authoritative returned type reaches the normal Entry screen,
  where any child Open behavior remains independently provider-derived.

### Resolved in F22: Library Progress Summary feature ownership

- F22 replaces the required `EntryLibraryProgressCalculator` runtime field and second root list with optional provider
  bindings in the ordinary type plugin. Provider absence is valid and returns structured `Inapplicable` state.
- Unified Library items, including stored merged groups, remain structurally visible without a provider. No zero or false
  summary is manufactured to preserve the old non-null model.
- The Feature owns common counts, last-read and started policy, merged aggregation, badges, nullable sort/filter inputs,
  Stats coverage, and F13 update inputs; providers expose only genuine media progress evidence.
- F22 plus F02 derives a structured non-opening Continue target, and F22 plus F10 derives bookmark summary/filter
  behavior. Either independent provider may remain absent without invalidating the type.
- Progress-dependent F14 predicates reject unknown state in either polarity, sort values are missing-last, Stats uses
  all-or-unavailable summary coverage, and F13 progress skips do not fire without evidence.
- The raw calculator/resolver list, `getValue` absence crash, public type factories, and build-boundary factory exemption
  are removed. The Domain resolution port exists only to avoid reversing the API-to-Domain dependency and is wired by
  composition to the app-facing Feature.

### Resolved in F25: Viewer Settings feature ownership

- F25 replaces the parallel runtime provider list and mutable `ViewerSettingsInteraction` with one optional ordinary
  plugin provider per type. A provider may expose multiple genuine viewer surfaces; provider absence remains valid.
- Reader/Player hubs and settings search consume the same Feature destinations. Actual app-owned screen projection
  contributions are matched exactly, and missing, duplicate, or orphan surface IDs fail rather than disabling UI.
- Preference ownership derives profile/app/private keys from every provider definition and is consumed explicitly by
  profile migration. F27 retains ownership of the general preference-contribution architecture.
- Backup, restore, migration copy, and per-entry override reset use structured Feature operations. The legacy Manga
  viewer bitfield remains only a named backup/reset compatibility adapter.
- Application production code has no raw Viewer Settings collection/registration facade, hardcoded screen map, or
  direct override repository operation outside application composition.

### Resolved in F26: Media-cache Maintenance feature ownership

- Optional media-cache providers bind through the ordinary type plugin and expose non-empty type-owned artifacts;
  provider absence remains valid and produces no setting or startup work.
- F26 owns discovery, labels, preferences, manual clear, launch auto-clear, refreshed size, and structured per-artifact
  failures. Provider presence is sufficient for all shared consequences.
- Application settings and startup consume only `EntryMediaCacheFeature`. The root bucket list, central cache keys,
  hardcoded label/launch maps, raw maintenance registry, and dedicated current-type preference holder are removed.
- Stable preference compatibility is provider-described and Feature-applied without becoming support evidence. F27
  retains ownership of general profile preference discovery and movement.

### Resolved in Milestone 4.4: `P4-PLUGIN-TEST-HARNESS`

- Responsible owners: entry-interaction SPI and the Manga, Anime, and Book test suites
- Owning phase: Phase 4
- Resolution: all registry-method fixtures were removed. Generic composition tests now exercise partial construction,
  binding dispatch, duplicate claims, and type ownership. Type-specific processor behavior remains in the type suites;
  tests that need real feature contributors remain Phase 5/7 obligations rather than receiving empty placeholders.

### Resolved in F04: `P5-DOWNLOAD-REGISTRY`

- Responsible owner: Downloads feature
- Owning phase: Phase 5
- Affected path: `entry-interactions/spi/src/main/**/EntryInteractionComposition.kt`
- Exposed condition: Milestone 4.2.2 selects media-specific bulk candidate construction by provider presence and
  temporarily checks Bookmarking-provider presence before applying the shared bookmarked action. The provider-backed
  facade no longer uses a registry, but this cross-provider feature policy is still transitional. This removed the
  deleted report dependency and restored SPI compilation without recreating a support authority.
- Resolution: F04 declares separate Download, Download-plus-Bulk-Candidate, and
  Download-plus-Bulk-Candidate-plus-Bookmark relationships. Shared selection moved into the feature, and raw dispatch
  now resolves only the media-specific candidate pool.

### `P5-FEATURE-CONTRIBUTOR-INSTALLATION` — Remaining feature contributors are not migrated yet

- Responsible owners: feature modules and application composition
- Owning phase: Phase 5
- Affected path: `entry-interactions/src/main/**/EntryInteractionRuntime.kt`
- Exposed condition: `createEntryInteractionComposition` requires independent feature contributors separately from the
  content-type plugins that contribute themselves. F01–F10 and F13–F26 now install their contributors; providers
  belonging to F11, F12, and F27 remain deliberately unreachable rather than receiving empty placeholder
  contributions.
- Required outcome: each migrated feature installs its owned contributor through application composition. Feature
  contributors must not be forced to masquerade as entry-type plugins or be selected from a central feature allowlist.

### Resolved in F06: `P5-DOWNLOAD-LIFECYCLE`

- Responsible owner: Downloads lifecycle feature
- Owning phase: Phase 5
- Former affected production paths: the removed `EntryDownloadLifecycleManager.kt` and
  `entry-interactions/src/main/**/EntryInteractionRuntime.kt`
- Resolution: bookmark-aware cleanup is installed from the Download-plus-Bookmarking relationship and receives
  non-optional structured events. The coordinator does not query a type report or contain a concrete type branch.

### Partially resolved in F10/F17/F18/F21: `P5-ENTRY-UI`

- Responsible owner: Entry-screen feature
- Owning phase: Phase 5
- Affected path: `app/src/main/**/ui/entry/EntryScreen.kt`
- F01 resolution: Open child and preview-page controls consume selected Open applicability.
- F04 resolution: download and bookmarked-bulk controls consume selected action applicability with contextual source and
  selection blockers.
- F07 resolution: the contextual options branch resolves and executes through the graph-selected options feature.
- F10 resolution: Bookmark-owned controls consume feature applicability and mutation; the former report/catalog gate is
  removed. Presentation vocabulary remains F23-owned.
- F17 resolution: list ordering/construction, merged and missing rows, aggregate missing count, and optional progress
  labels consume the selected Child List feature. Missing Child List support hides the list surface without a fallback.
- F18 resolution: group state, live filtering, active state, controls, and multi-member persistence consume the selected
  Child Group Filtering feature. The former raw support/apply gates and unfiltered live-list overwrite are removed.
- F21 resolution: the Related Entries action, load, orientation, persistence, and live Library state consume one
  contextual Feature boundary. The app no longer casts the source capability or calls a parallel domain use case.

### Resolved in F03–F05: `P5-LIBRARY-INTEGRATIONS`

- Responsible owners: Library feature and Downloads feature
- Owning phase: Phase 5
- Affected production paths:
  - `app/src/main/**/ui/library/LibraryTab.kt`
  - `app/src/main/**/ui/library/LibraryScreenModel.kt`
  - `app/src/main/**/data/library/LibraryUpdateNotifier.kt`
- F01 resolution: library-update notification Open-child intents consume the selected Open feature gate.
- Resolution: Library counts use F03; whole-selection bulk actions and notification eligibility/dispatch use F04; the
  library-update automatic-download batch uses F05. Contextual selection, local/stub, queue, and notification-size
  constraints remain structured inputs rather than support flags.

### Resolved in F03/F04/F10: `P5-UPDATES-INTEGRATIONS`

- Responsible owner: Updates feature
- Owning phase: Phase 5
- Affected path: `app/src/main/**/ui/updates/UpdatesScreenModel.kt`
- F01 resolution: Updates row Open availability and dispatch consume the selected Open feature gate.
- F03/F04 resolution: download status and action availability/dispatch consume the runtime and action features.
- F10 resolution: Bookmark actions and mutation consume structured feature selection and provider-derived
  applicability without recreating a type support matrix.

### Partially resolved in F04/F06/F10: `P7-GRAPH-SELECTED-BEHAVIORAL-TESTS`

- Responsible owners: Downloads, Entry UI, Library, and Updates feature tests
- Owning phase: Phase 7, after their Phase 5 production migrations
- Affected test paths:
  - the cleanup portion formerly covered by `entry-interactions/src/test/**/BookmarkDownloadVerticalContractTest.kt`;
    F06 now covers graph-selected protection, while later contract reporting remains Phase 7 work
  - `app/src/test/**/UpdatesSelectionActionsTest.kt`
- F04 resolution: `DownloadDropdownMenuTest` now tests only presentation of the feature-selected bookmarked consequence;
  it no longer constructs a support report.
- F06 resolution: cleanup assertions now use anonymous graph-selected Download and Bookmark providers; the old lifecycle
  manager/report fixture was removed.
- F10 resolution: Bookmark selection assertions now exercise one graph-selected synthetic provider and valid provider
  absence. The stale report fixture and hardcoded production support expectations were removed.

### `P5-PRESENTATION-PROJECTION` — Type vocabulary remains an app-owned concrete-type map

Status: resolved by F23

- Responsible owners: Entry presentation and every feature consuming type vocabulary
- Owning phase: Phase 5
- Inventory scope: `T23` and the presentation consumers in `F01`–`F27`
- Affected path: `app/src/main/**/presentation/entry/EntryTypePresentation.kt` and its consumers
- Required outcome: type-owned vocabulary becomes projection input selected by applicable feature integrations. Icons,
  labels, and nouns may vary but cannot decide behavioral support. The generic fallback must not hide a missing shipped
  type projection.
- Resolution: each type owns one optional presentation provider in its normal plugin contribution; F23 discovers and
  exposes it through an application Feature. Concrete and generic results retain provenance, the application map is
  removed, formatting tokens are type-owned, and boundary validation rejects recreating the map.

## Removed Rather Than Migrated

The following artifacts were deleted at the cut and are not obligations to recreate:

- `EntryCapabilityCatalog`
- `EntryCapabilityModel` evidence/report vocabulary
- `EntryCapabilityReport`, `supportsTypeWide`, and report assembly
- `EntryDownloadCapabilityPolicy` in its report-driven form
- explicit `IntentionallyUnsupported` outcomes from Anime and Book plugins
- report/model/policy unit tests and the evidence-registry/runtime-DI identity tests

## Completion Rule

An obligation leaves this ledger only when its owning production or validation path consumes contributions, evaluated
relationships, selected consequences, contracts, or projections directly. Making the build green through a parallel
authority does not complete an obligation.
