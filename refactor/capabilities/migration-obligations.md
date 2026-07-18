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

### `P4-TYPE-CONTRIBUTIONS` — Entry-type plugins do not yet contribute to the graph

- Responsible owners: `entry-interactions.manga`, `entry-interactions.anime`, `entry-interactions.book`
- Owning phase: Phase 4
- Affected production paths:
  - `entry-interactions/*/src/main/**/MangaEntryInteractionPlugin.kt`
  - `entry-interactions/*/src/main/**/AnimeEntryInteractionPlugin.kt`
  - `entry-interactions/*/src/main/**/BookEntryInteractionPlugin.kt`
  - `entry-interactions/src/main/**/EntryInteractionRuntime.kt`
- Milestone 4.1 status: Manga, Anime, and Book now contribute owned identity plus their actual Open and Continue provider
  objects. Provider-owned installation puts those same objects into operational dispatch without a second type-module
  registration. `EntryInteractionPlugin` validates provider type identity generically and does not require either
  provider.
- Milestone 4.2.1 status: Consumption, Bookmarking, Progress transfer, and Playback-preference transfer now use
  capability-owned bindings. Graph evidence and dispatch derive from the same declaration, including implementations
  that supply more than one independent capability.
- Milestone 4.2.2 status: Downloads are decomposed into core, options, individual settings, bulk candidates, and
  automatic filtering. Each is contributed independently from the same concrete implementation where appropriate.
- Milestone 4.2.3 status: Migration and Merge are split into independent compatibility providers. Manga and Anime bind
  both; Book's combined default-false processor is removed without an absence declaration.
- Remaining outcome: migrate `T13` and `T16`–`T21` without mechanically treating processor registration,
  default methods, false support methods, or no-op processors as capability evidence; then migrate `T22`–`T27`
  type-owned artifacts.

### `P4-REMAINING-PROVIDER-CONTRACTS` — Operational processors are not yet graph providers

- Responsible owners: Entry interaction SPI plus Manga, Anime, and Book provider implementations
- Owning phase: Phase 4
- Inventory scope: `T13`, `T16`–`T21`
- Exposed condition: Update Eligibility, Child List, Child Group Filter, Library Filter, Preview, and Immersive
  processors still enter only the operational registry.
- Required outcome: define provider-backed contracts at their true granularity and contribute the same implementation
  objects used for dispatch. Split child progress labels, registered no-op child-group behavior, and false-returning
  library-filter behavior before claiming support.
- Forbidden shortcut: a generic legacy-registration adapter or one capability definition per current processor category.

### `P4-PLUGIN-TEST-HARNESS` — Operational plugin fixtures use the old lambda-only boundary

- Responsible owners: entry-interaction SPI and the Manga, Anime, and Book test suites
- Owning phase: Phase 4
- Affected test paths:
  - `entry-interactions/src/test/**/EntryInteractionRegistryTest.kt`
  - `entry-interactions/manga/src/test/**/MangaEntryInteractionPluginTest.kt`
  - `entry-interactions/anime/src/test/**/AnimeEntryInteractionPluginTest.kt`
  - `entry-interactions/book/src/test/**/BookEntryInteractionPluginTest.kt`
  - `entry-interactions/book/src/test/**/BookDownloadProcessorTest.kt`
- Required outcome: test contributors use the same owned contribution boundary while retaining behavioral processor
  tests. They must not add per-type capability-value assertions.

### `P5-DOWNLOAD-REGISTRY` — Bulk and bookmarked download consequences remain in transitional dispatch

- Responsible owner: Downloads feature
- Owning phase: Phase 5
- Affected path: `entry-interactions/spi/src/main/**/EntryInteractionRegistry.kt`
- Exposed condition: Milestone 4.2.2 selects media-specific bulk candidate construction by provider presence and
  temporarily checks Bookmarking-provider presence before applying the shared bookmarked action. This removed the
  deleted report dependency and restored SPI compilation without recreating a support authority.
- Required outcome: the download feature owns the prerequisite relationships and shared bulk/bookmark consequences. The
  registry consumes selected feature behavior instead of retaining the transitional cross-provider rule.

### `P5-FEATURE-CONTRIBUTOR-INSTALLATION` — Runtime has no migrated feature contributors to install yet

- Responsible owners: feature modules and application composition
- Owning phase: Phase 5
- Affected path: `entry-interactions/src/main/**/EntryInteractionRuntime.kt`
- Exposed condition: `createEntryInteractionComposition` requires independent feature contributors separately from the
  content-type plugins that contribute themselves. The runtime intentionally supplies no empty placeholder list.
- Required outcome: each migrated feature installs its owned contributor through application composition. Feature
  contributors must not be forced to masquerade as entry-type plugins or be selected from a central feature allowlist.

### `P5-DOWNLOAD-LIFECYCLE` — Cleanup bookmark protection still consumes the deleted report

- Responsible owner: Downloads lifecycle feature
- Owning phase: Phase 5
- Affected production paths:
  - `entry-interactions/src/main/**/EntryDownloadLifecycleManager.kt`
  - `entry-interactions/src/main/**/EntryInteractionRuntime.kt`
- Required outcome: bookmark-aware cleanup is installed from the download feature's evaluated relationship and receives
  operational events; it does not query a type report or become a type branch.

### `P5-ENTRY-UI` — Entry actions still reconstruct capability support

- Responsible owner: Entry-screen feature
- Owning phase: Phase 5
- Affected path: `app/src/main/**/ui/entry/EntryScreen.kt`
- Required outcome: open/download/bookmark controls consume selected feature applicability and presentation metadata,
  with no independent support flags or catalog queries.

### `P5-LIBRARY-INTEGRATIONS` — Library actions and notifications still query the deleted authority

- Responsible owners: Library feature and Downloads feature
- Owning phase: Phase 5
- Affected production paths:
  - `app/src/main/**/ui/library/LibraryTab.kt`
  - `app/src/main/**/ui/library/LibraryScreenModel.kt`
  - `app/src/main/**/data/library/LibraryUpdateNotifier.kt`
- Required outcome: bulk actions and download notification policy consume feature-owned evaluated consequences while
  preserving contextual selection, local-entry, and queue constraints.

### `P5-UPDATES-INTEGRATIONS` — Updates actions still query the deleted authority

- Responsible owner: Updates feature
- Owning phase: Phase 5
- Affected path: `app/src/main/**/ui/updates/UpdatesScreenModel.kt`
- Required outcome: download and bookmark actions consume evaluated feature relationships and actual operational
  interactions without recreating a type support matrix.

### `P7-GRAPH-SELECTED-BEHAVIORAL-TESTS` — Retained behavioral proofs still construct report fixtures

- Responsible owners: Downloads, Entry UI, Library, and Updates feature tests
- Owning phase: Phase 7, after their Phase 5 production migrations
- Affected test paths:
  - `entry-interactions/src/test/**/BookmarkDownloadVerticalContractTest.kt`
  - `entry-interactions/src/test/**/EntryDownloadLifecycleManagerTest.kt`
  - `app/src/test/**/DownloadDropdownMenuTest.kt`
  - `app/src/test/**/UpdatesSelectionActionsTest.kt`
- Required outcome: preserve behavioral selection and cleanup assertions as graph-selected contracts. Remove synthetic
  report construction and do not replace it with hardcoded production type expectations.

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
