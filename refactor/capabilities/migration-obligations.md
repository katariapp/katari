# Capability Architecture Migration Obligations

Updated: 2026-07-18

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
  contains no per-type settings map or Manga presentation gate.
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

### Resolved in F13: Update Eligibility feature ownership

- F13 owns the universal library-update eligibility policy and the consequences used by the update worker and Stats.
- Eligibility is shared behavior for every composed type rather than a provider capability or per-type opt-in. An
  uncomposed type is a composition error; a composed type is evaluated from shared preferences and runtime context.
- The redundant raw Update Eligibility provider, dispatch, and interaction facade are removed rather than retained as
  an always-present declaration.

### Resolved in F15: Progress Transfer feature ownership

- F15 owns graph-derived applicability and structured snapshot, restore, and copy results for portable progress state.
- Backup create/restore and migration use the app-facing Feature; application production code has no raw
  `EntryProgressInteraction` reference.
- Internal provider dispatch is strict. Empty supported state, provider absence, and incompatible source/target types
  are no longer represented by the same empty/no-op behavior.
- Live media persistence remains type-owned; per-child labels, migration policy, and library summaries remain assigned
  to F17, F21, and F22 respectively.

### Resolved in F16: Playback-preference Transfer feature ownership

- F16 owns provider-derived backup snapshot, backup restore, and migration-copy consequences.
- Backup creation no longer uses `EntryType.ANIME` to authorize playback-preference serialization; application backup
  and migration consumers depend only on the structured Feature boundary.
- Supported data absence, provider absence, and cross-type copy are distinct outcomes. Anime download preferences and
  legacy wire conversion remain separate compatibility concerns.
- A synthetic provider activates every shared consequence without a concrete type list, while a partial type with no
  provider remains valid and returns structured inapplicability.

### Resolved in F17: Child List feature ownership

- F17 owns child-list applicability, reading/display order, display construction, merged headers, missing-count results,
  and the ordered-child consequence consumed by preview and immersive flows.
- Child List and Child Progress remain independent providers. Optional labels are derived only when both are present;
  either absence remains valid and returns a structured inapplicable result.
- Application production code has no raw `EntryChildListInteraction` or `EntryChildProgressInteraction` reference, and
  presentation no longer contains a Manga gate for missing-count behavior.

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
  content-type plugins that contribute themselves. F01–F10, F13, and F15–F17 now install their contributors; providers
  belonging to F11, F12, F14, and F18–F27 remain deliberately unreachable rather than receiving empty placeholder
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

### Partially resolved in F10/F17: `P5-ENTRY-UI`

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

- Responsible owners: Entry presentation and every feature consuming type vocabulary
- Owning phase: Phase 5
- Inventory scope: `T23` and the presentation consumers in `F01`–`F27`
- Affected path: `app/src/main/**/presentation/entry/EntryTypePresentation.kt` and its consumers
- Required outcome: type-owned vocabulary becomes projection input selected by applicable feature integrations. Icons,
  labels, and nouns may vary but cannot decide behavioral support. The generic fallback must not hide a missing shipped
  type projection.

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
