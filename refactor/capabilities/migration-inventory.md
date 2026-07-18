# Capability Architecture Migration Inventory

Updated: 2026-07-18

Status: pre-Phase 4 census complete; classifications approved

## Purpose

This inventory is the migration control surface for Phases 4 through 8. It exists because the capability atlas describes
current behavior, while the phase files previously described migration only in broad categories. Broad categories are
not enough to prevent a feature, UI integration, worker, setting, projection, or external capability from being
forgotten.

The inventory is not a runtime allowlist and must never be turned into one. Every row must eventually leave the census
by being represented through an owned content-type contribution, feature contribution, contextual input, selected
artifact, compatibility classification, or enforcement rule. New contributions remain discoverable without editing
this document.

The out-of-boundary findings and proposed classifications below were approved on 2026-07-18. This approval establishes
migration scope; it does not start Phase 4.

## Discovery Standard

The census treats all of the following as migration evidence:

- operational providers and provider registries;
- methods, defaults, empty results, and no-ops that currently imply support or absence;
- direct `EntryType` branches and maps keyed by entry type;
- source, tracker, entry, selection, preference, platform, and renderer capability checks;
- UI visibility, action availability, settings, notifications, navigation, workers, backup, migration, cleanup, and
  storage maintenance triggered by those facts;
- presentation maps, provider-to-screen maps, notification vocabulary, and documentation claims;
- tests and boundary checks that enumerate current types, providers, or supported outcomes;
- independent contribution systems that require another edit when a type, provider, or feature is added.

Snapshot evidence from this census:

- 14 processor categories are registered by `EntryInteractionRegistry`.
- 76 production files reference `EntryType` across the audited app, domain, data, source, and interaction modules.
- 20 production files outside type-interaction modules contain direct current-type constants.
- 214 production, test, and boundary-build-logic files reference `EntryType` across the audited modules.
- One second type-provider registry exists for library progress.
- Root composition separately enumerates type plugins, type runtime installers, warmups, viewer-setting providers, media
  cache buckets, and image components.
- The public entry-source API exposes 16 focused capability contracts or capability-bearing source contracts, plus
  source-factory and preference infrastructure.
- Tracker applicability and tracker sub-capabilities are maintained outside the entry-interaction boundary.

The counts are audit baselines, not architectural requirements. They may change as migration removes duplicated facts.

## Migration Classification

Every discovered item receives one of these outcomes:

- **Type contribution:** a provider, adapter, fixture, identity, or vocabulary owned by a content type.
- **Feature contribution:** prerequisites, consequences, obligations, contracts, or projections owned by a feature.
- **Contextual input:** source-, entry-, selection-, preference-, platform-, tracker-, or runtime-owned evidence composed
  by a feature.
- **Environment installation:** the single application-composition act that supplies an independently owned contributor;
  it may enumerate installed modules but must not repeat their capabilities or consequences.
- **Compatibility/media boundary:** a wire, storage, legacy, or internal media distinction that remains local and does
  not authorize a product feature.
- **Enforcement:** validation that prevents parallel authorities or undiscovered participation.

## Phase 4 — Content-Type Composition Register

Phase 4 must migrate the actual provider shape, not mechanically wrap every existing processor. Several current
processors contain multiple independently meaningful capabilities or registered no-op sub-capabilities.

Migration progress:

- Milestone 4.1 implements `T01`–`T03` in the working tree: each production plugin owns one content-type contribution,
  and its Open and Continue processor objects are both operational implementations and graph providers. Provider-owned
  installation derives operational dispatch from that contribution without a second type-module registration.
- `T04`–`T21` remain unported until their provider contracts are decomposed without flattening optional behavior or
  registered no-ops into false capability claims.
- `T22`–`T27` remain unported type-owned artifact and runtime-composition work.

| ID | Provider or composition fact | Current evidence | Required migration outcome |
| --- | --- | --- | --- |
| `T01` | Content-type identity and ownership | Manga, Anime, and Book plugin factories | Each plugin contributes one owned `ContentTypeContribution`; any provider subset remains valid. |
| `T02` | Open | `EntryOpenProcessor` for all current types | Provider presence is the only type-wide support fact. No open requirement is added. |
| `T03` | Continue | `EntryContinueProcessor` for all current types | Provider presence is the only type-wide support fact; next-item absence remains entry state. |
| `T04` | Core downloads | `EntryDownloadProcessor`; Book construction can omit it | A contributed download provider is optional. Book's construction flag must not become product capability truth. |
| `T05` | Download options | Default-false/null methods inside the download processor; Anime supplies behavior | Represent option resolution as a distinct provider or owned specialized behavior so core downloads do not imply options. |
| `T06` | Download setting behavior | `settingCapabilities` set inside each downloader | Model each genuine specialized setting behavior without a central enum-to-type matrix. Settings visibility is feature-derived. |
| `T07` | Bulk candidate pool | Required method on each download processor | Preserve media-specific pool construction; bulk action selection and intersections belong to the Downloads feature. |
| `T08` | Automatic-download filtering | Required method on each download processor | Preserve media-specific filtering as specialized behavior selected by the automatic-download integration. |
| `T09` | Migration | `EntryCapabilityProcessor.supportsMigration`, default false | Split from the combined capability processor. Provider absence means unavailable; do not contribute a false result. |
| `T10` | Merge | `EntryCapabilityProcessor.supportsMerge`, default false | Split from migration. Selection shape remains a feature-owned contextual rule. |
| `T11` | Consumption mutation | `EntryConsumptionProcessor` | Contribute the provider independently from Bookmarking and Downloads. |
| `T12` | Bookmark mutation | `EntryBookmarkProcessor`; Manga only | Contribute only where implemented. Anime and Book contribute no absence declaration. |
| `T13` | Update eligibility | Three nearly identical type processors | Preserve operational behavior during type migration, then let the Library Update feature own shared policy in Phase 5. |
| `T14` | Progress snapshot/restore/copy | `EntryProgressProcessor` | Contribute independently; backup and migration are consumers, not capability authorities. |
| `T15` | Playback-preference transfer | Anime-only `EntryPlaybackPreferencesProcessor` | Contribute the provider; backup and migration must discover it without an Anime gate. |
| `T16` | Child-list ordering and construction | `EntryChildListProcessor` | Contribute actual media behavior. Shared ordering must not be copied into every type merely to claim support. |
| `T17` | Per-child progress labels | Default empty result inside child-list processor | Classify as optional presentation/behavior enrichment rather than treating child-list presence as proof. |
| `T18` | Child-group filtering | Manga operational; Anime registered false/empty/no-op; Book absent | Remove the Anime no-op provider. Contribute only the operational capability and keep entry state contextual. |
| `T19` | Outside-release-period library filter | Boolean method on three registered library-filter processors | Represent the actual optional feature provider; provider registration must no longer coexist with a false support method. |
| `T20` | Preview renderer/loader | Manga and Anime preview processors; contextual support methods | Contribute the type-owned preview implementation while source, entry, and preference facts remain contextual. |
| `T21` | Immersive renderer/loader | Manga and Anime immersive processors | Contribute the renderer; source opt-in, media resolution, and runtime state remain contextual. |
| `T22` | Library progress calculation | Separate `EntryLibraryProgressCalculator` registry and root list | Join the type contribution boundary or a graph-selected feature adapter; a new type must not require a second root list. |
| `T23` | Presentation vocabulary | `EntryTypePresentation` hardcodes three types plus a generic fallback | Type-owned vocabulary becomes projection input. It may vary without deciding support. |
| `T24` | Type runtime services | `addMangaEntryInteractionRuntime`, `addAnimeEntryInteractionRuntime`, `addBookEntryInteractionRuntime` | Install through the same owned contribution boundary or an explicitly linked runtime contribution, not an unrelated type list. |
| `T25` | Viewer-setting providers | Manga/Anime providers plus Book runtime provider list | Associate providers with their owning contributions; settings integration must not require root and UI lists. |
| `T26` | Media caches and image components | Separate cache-bucket list, Anime lazy bucket, Book runtime buckets, Manga Coil component bridge | Associate specialized runtime artifacts with their owners and feature projections; do not infer support from hardcoded keys. |
| `T27` | Warmup/startup hooks | Manga warmup, Anime warmup, shared download notifier start | Preserve startup work as selected consequences or owned runtime contributions with explicit lifecycle semantics. |

Phase 4 ends after real type composition and type-owned artifacts conform. It does not migrate the feature consumers
below, but every compile failure it exposes must be linked to one of their IDs.

## Phase 5 — Feature Consequence Register

Every feature contribution must declare all applicable consequences in its row. A consequence may already be generic;
that means it may need little production-code change, but it must still be selected, covered, or explicitly classified
so it cannot be silently omitted for the next provider.

| ID | Feature-owned integration | Production consumers and consequences that must be accounted for |
| --- | --- | --- |
| `F01` | Open | Entry child rows; history and updates actions; deep links; browse preview sheets; immersive cards; notification open-child actions; Android pending intents. |
| `F02` | Continue | Entry continue action; library continue action; history resume action; no-next-item presentation. |
| `F03` | Download queue/runtime | Entry and Updates status/actions; library counts and filters; unified queue UI; More-tab running state; main-activity initialization; shared job, work controller, foreground notification, and notification event renderer. |
| `F04` | Individual and bulk download actions | Entry selection/swipes; Library selection; Updates selection; library-update notification download action; candidate resolution; local/stub and selection blockers. |
| `F05` | Automatic downloads | Library update discovery and queue start; entry refresh auto-download path; category and preference policy; provider-specific candidate filtering. |
| `F06` | Download lifecycle cleanup | Consumption, reader, player, and Book completion/progress events; remove-after-consumed policy; category exclusions; bookmark protection; physical delete versus cleanup dispatch. |
| `F07` | Download settings and options | Download settings screen; packaging/splitting/parallelism visibility; entry download-options dialog; stream, dub, subtitle, and quality selection; option persistence. |
| `F08` | Download maintenance | Source rename, entry-title rename, backup-restore cache invalidation, migration deletion, advanced cache invalidation, source removal, and catalogue removal paths. |
| `F09` | Consumption | Entry selection and swipes; Library selection; Updates selection; notification action; tracking-progress sync; lifecycle event emission; consumed/unconsumed vocabulary. |
| `F10` | Bookmarking | Entry and Updates actions; bookmarked child/library filters; bookmarked bulk downloads; cleanup protection; selection eligibility and vocabulary. |
| `F11` | Migration | Entry and Library actions; migration source/list/search/configuration UI; use-case validation; progress, playback preferences, viewer overrides, downloads, categories, tracking, and chapter transfer. |
| `F12` | Merge | Entry and Library actions; duplicate/merge dialogs; same-type and selection-shape rules; merged child lists, continue, downloads, progress, library state, metadata refresh, and backup representation. |
| `F13` | Library update eligibility | Update worker; skip reasons and notifications; Stats calculations; smart-update settings; release-window policy; current duplicate Manga/Anime/Book implementations. |
| `F14` | Library filtering | Downloaded, bookmarked, consumed/progress, tracker, outside-release-period, source, category, and content-type filters; only capability-dependent filters use graph applicability. |
| `F15` | Progress transfer | Backup create/restore; migration; reader/player/book persistence; per-child progress labels where applicable. |
| `F16` | Playback-preference transfer | Backup create/restore and migration; no direct Anime check may remain as authorization. |
| `F17` | Child-list behavior | Entry sorting/filtering/display rows; merged headers; missing-count rows; immersive first-child selection; per-child progress labels. |
| `F18` | Child-group filtering | Entry state flows, filter controls, excluded-group persistence, group data source, and settings dialog integration. |
| `F19` | Preview | Entry preview state; browse long-press preview; preference/configuration; page loader and open action; contextual source and child requirements. |
| `F20` | Immersive browsing | Catalogue and feed mode entry; long-press action; view-mode fallback; renderer loading; preload radius; progress persistence; source opt-in. |
| `F21` | Related entries | Entry action, related-entry screen model/dialog, source orientation, library membership, and normal open flow for returned mixed types. |
| `F22` | Library progress summary | Unified library load, merged state, continue target, badges, sort/filter inputs, and any empty-provider behavior. |
| `F23` | Type presentation | Entry, Library, Updates, History, Browse, Download, Duplicate, Tracking, and notification vocabulary/icons/plurals; presentation never authorizes an action. |
| `F24` | Library-update notifications | Type grouping; channel/group/ID selection; summary and child vocabulary; mark-consumed, view, and download actions; neutral or owned Book semantics instead of Manga fallback. |
| `F25` | Viewer settings | Provider discovery; reader/player settings hubs; provider-to-screen routing; settings search indexing; entry overrides; profile ownership; reset actions; backup/migration integration. |
| `F26` | Media-cache maintenance | Cache-bucket discovery; settings labels; manual clear; launch auto-clear; preferences; cache invalidation and user-visible errors. |
| `F27` | Profile preference ownership | Discovery of profile/app/private keys; legacy ownership correction; profile creation/deletion/move; new feature preferences must participate without editing a recorder list. |

## Phase 6 — Contextual and External Register

These contracts remain authoritative in their existing owners. Migration means feature contributions consume and explain
them; it does not mean copying them into type-wide capability declarations.

| ID | Context or external owner | Current consumers that must be composed |
| --- | --- | --- |
| `C01` | `EntryCatalogueSource` presence | Catalogue browse, search, filters, language handling, migration source search, source manager/repository. |
| `C02` | `supportsLatest` | Sources UI, catalogue latest action/presets, feeds and built-in latest feed, migration presentation. |
| `C03` | `supportsImmersiveFeed` | Catalogue/feed mode availability, long-press action, mode reset, and type renderer applicability. |
| `C04` | `SourceMetadata.supportedEntryTypes` | Source/extension badges, content-type filters, extension metadata persistence; descriptive only, never returned-entry validation. |
| `C05` | `EntryPreviewSource` | Anime preview applicability and loading; combined with type preview provider and preferences. |
| `C06` | `RelatedEntriesSource` | Related-entry action and fetch; absence removes the feature without a second flag. |
| `C07` | `EntryImageSource` and image-page media | Manga download, immersive loading, reader loading, and cover-fetch network client/headers. |
| `C08` | `SubtitleSource` and playback selection | Anime playback and download option resolution. |
| `C09` | `ConfigurableSource` | Source settings availability, source preference screen, extension details, and tracker integrations that reuse source preferences. |
| `C10` | `SourceHomePage` | Catalogue, feed, migration, extension details/store links, and tracker integrations requiring a source URL. |
| `C11` | `WebViewSource` | Entry WebView/share/open-in-browser actions and WebView activity headers/navigation. |
| `C12` | `ChapterWebViewSource` | Manga reader WebView availability, source ID, canonical child URL, and reader activity launch; source-owned applicability remains contextual. |
| `C13` | `ResolvableSource` | Deep-link source discovery, URI classification, and entry/child resolution. |
| `C14` | `EntryItemOrientationProvider` | Browse cards, feeds, related entries, library covers, source metadata conversion, and orientation defaults. |
| `C15` | `EmptyChapterListSource`, `IncrementalChapterSource`, `ChapterNumberRecognitionSource` | Source refresh safety, incremental fetch, and number recognition in `SyncEntryWithSource`. |
| `C16` | `UnmeteredSource` | Library-update metered-source warning policy. |
| `C17` | Local or stub state | Download/bulk actions, WebView/source-backed actions, catalogue removal, library filters/badges, and missing-source UI. |
| `C18` | Tracker `supportedEntryTypes` | Entry tracking action/dialog, search/register guards, Library tracker filters, automatic add/sync, and documentation. |
| `C19` | Tracker reading dates, private tracking, and scoring | Track info/search fields and actions; external integration capabilities, not content-type facts. |
| `C20` | Entry state | Favorite/library membership, merged membership, stored children, progress, downloadable media, and available option values. |
| `C21` | Selection shape | Homogeneous type, selection size, existing merge membership, local items, mixed capability providers, and action-specific eligibility. |
| `C22` | Preferences and profile | Preview enabled state, auto-download/cleanup policy, immersive preferences, viewer/player settings, and profile-scoped ownership. |
| `C23` | Platform and renderer | Picture-in-picture, viewer auto-scroll, WebView availability, reader format/DRM support, and runtime media resolution. |
| `C24` | Legacy/local source integration | Legacy Manga adapter and bundled Local source advertise/produce Manga; preserve as compatibility contracts, not general type support. |

## Approved Findings Outside the Current Interaction Contribution Boundary

These classifications were approved on 2026-07-18. “Include” means the behavior must participate in the
application-wide graph through its real owner; it does not mean moving all code into the `entry-interactions` module.

| Finding | Evidence | Proposed classification for verification |
| --- | --- | --- |
| Library progress is a second required-per-type provider system | `EntryLibraryProgressRuntime.kt`, `EntryLibraryProgressResolver.kt`, `DomainModule.kt` | **Include.** Join type contribution discovery; absence must be a feature result, not `getValue` failure caused by a forgotten root edit. |
| Viewer settings are discovered operationally but routed through a hardcoded UI map | `ViewerSettingsInteraction`, `EntryInteractionRuntime.kt`, `SettingsViewerHubScreens.kt`, `SettingsSearchScreen.kt` | **Include.** Provider contribution must carry/select its UI projection and search participation. |
| Media caches use a provider list but hardcoded keys, labels, and auto-clear policy | `EntryRuntimeContracts.kt`, `EntryInteractionRuntime.kt`, `SettingsDataScreen.kt`, `AutoClearMediaCache.kt`, `MainActivity.kt` | **Include.** Cache maintenance is a feature with provider-owned descriptors and selected settings/startup consequences. |
| Profile preference ownership manually instantiates known preference owners | `ProfilePreferenceOwnership.kt`, `ProfileManager.kt` | **Include.** Feature preferences need an owned contribution so a new provider cannot be omitted from profile migration. |
| Runtime type installation has several parallel lists | `EntryInteractionRuntime.kt`, `EntryLibraryProgressRuntime.kt`, `AppModule.kt`, `App.kt` | **Include.** Keep one environment installation boundary; remove parallel type/runtime/artifact lists. |
| Tracking has its own applicability system | `TrackerCapabilities.kt`, `TrackerManager.kt`, entry/library/track consumers | **Include as external input.** Trackers retain ownership; tracking feature composes every consequence. |
| Source SDK capabilities drive many UI and policy paths directly | `entry-source-api`, source/browse/entry/domain consumers | **Include as contextual inputs.** Preserve public contracts while removing repeated consumer composition. |
| Library-update notification semantics enumerate Manga and Anime only | `LibraryUpdateNotifier.kt`, `Notifications.kt` | **Include.** Notification behavior and presentation are feature projections; Book fallback is a known bug. |
| Backup and migration contain current capability gates | `EntryBackupCreator.kt`, `EntryRestorer.kt`, `MigrateEntryUseCase.kt` | **Include capability consumers.** Keep wire/legacy/media conversion branches as compatibility boundaries. |
| Entry-type presentation is a central three-type map | `EntryTypePresentation.kt` and its consumers | **Include as type-owned projection input.** Retain generic emergency vocabulary only if it cannot hide a missing shipped-type projection. |
| Missing-child gaps are authorized in presentation code | `EntryScreen.missingChildCount` | **Include.** Move applicability to child-list/feature behavior; keep wording in presentation. |
| Source content-type filters and badges depend on descriptive metadata | source/extension filter and indicator files | **Include as a projection of external metadata.** Do not treat metadata as proof of entry feature support. |
| Boundary enforcement contains broad exclusions and a concrete root import allowlist | `EntryInteractionBoundaryCheckTask.kt` | **Include as enforcement work.** Replace lists that require edits for each type/bridge and add rules against new parallel authorities. |
| Book format processors form a nested media-specific registry | `BookProcessorRegistry.kt`, `BookRuntimeModule.kt`, EPUB/prose processors | **Verify scope.** Proposed: keep internal media selection local unless a processor capability has cross-feature consequences; expose only selected adapters/artifacts that cross the type boundary. |
| Child WebView support is composed inside the Manga reader | `ChapterWebViewSource`, `ReaderViewModel.kt`, `ReaderActivity.kt`, legacy adapters | **Include as contextual input.** Preserve the source contract; make the reader/WebView consequence graph-selected instead of hiding it inside one media implementation. |
| Settings navigation/search contain curated screen lists | `SettingsMainScreen.kt`, `SettingsSearchScreen.kt`, viewer settings screen map | **Include feature-owned settings projections where capability features contribute screens.** Global non-feature settings may remain application navigation. |

## Direct-Type Branch Disposition

Every production direct-type branch found by the census must have one of these dispositions:

- **Migrate to a contribution/projection:** `EntryTypePresentation`, missing-child gaps, download settings lookup, library
  update notification type/channel selection, tracker default support, playback-preference backup gate, and feature UI
  availability.
- **Keep as compatibility/media behavior:** legacy Manga source adaptation, legacy backup model conversion, Manga viewer
  flag conversion, legacy feed payload decoding, and internal media-specific reader/player logic.
- **Keep as storage default with explicit compatibility coverage:** backup/default model values and serialized enum/string
  conversion.
- **Keep as tooling only:** debug Anime launcher filters and Compose/sample providers.
- **Review during migration:** any newly encountered direct-type branch not already assigned above. It must not be silently
  added to an allowlist.

## Test, Reporting, and Documentation Register

The following validation surfaces must migrate after their production owners:

- Registry and plugin tests retain duplicate detection, dispatch, partial composition, and real behavior; they remove
  support-label matrices and no-op providers.
- Download dropdown, lifecycle, and Updates selection tests become graph-selected feature contracts.
- Backup and migration tests prove provider-selected behavior while preserving separate wire-compatibility tests.
- Preview, immersive, source, tracker, viewer settings, media cache, notification, and library progress tests are selected
  from applicable contributions or remain focused owner tests where behavior is genuinely contextual.
- `EntryTypePresentationTest`, source indicator tests, library grouping tests, and notification tests distinguish
  presentation projections from behavioral authority.
- `checkEntryInteractionBoundaries` expands from import isolation to detecting parallel support facts, forbidden
  type-feature gates, and undiscovered provider/UI registries.
- The developer graph report explains providers, applicable features, consequences, blockers, and obligations.
- `docs/features/content-type-reference.md` is generated or verified from selected feature projections.
- Source SDK capability documentation remains contract documentation, but consumer coverage is verified against the
  contextual-feature register.

Tests that exist solely for legacy serialization, backup protobuf compatibility, database mapping, legacy source ABI,
or media internals remain ordinary compatibility tests and are not selected as feature-completeness contracts.

## Audited Non-Migration Boundaries

The following are not capability authorities by themselves and should not be forced into the graph:

- `Entry.type` persistence, repository filtering, SQL string encoding, and identity keys;
- backup wire defaults and legacy Manga/Anime model conversion;
- legacy source API adapters and SDK family compatibility;
- internal Manga reader, Anime player, and Book processor/media algorithms;
- source-returned media descriptors and runtime failures after a feature is already applicable;
- generic data propagation of `EntryType` through history, updates, library, browse, download queue, and backup models;
- debug launchers, previews, sample providers, and test fixtures;
- generic iteration over `EntryType.entries` for content-type filters or grouping, provided behavior is not inferred from
  enum membership.

If any of these boundaries begins deciding whether a user-facing feature exists, that decision becomes migration scope.

## Completeness Gates

The inventory is complete only when all of these checks pass:

1. Every operational provider and every support-like method has a type-contribution or contextual classification.
2. Every interaction consumer is assigned to a feature consequence, even when its code is already generic.
3. Every source and tracker capability is connected to all of its product consumers.
4. Every direct current-type branch is classified as migration, projection, compatibility, storage, or tooling.
5. Every manual provider/type/artifact list is classified as the single environment installation boundary or removed as
   a second participation list.
6. Every settings, notification, backup, migration, profile, cache, worker, and navigation integration is owned by a
   feature row.
7. Every current support claim in tests and documentation is selected from executable graph results or explicitly kept
   as compatibility coverage.
8. Boundary enforcement fails on new parallel support declarations and feature-specific type gates.
9. An unknown future type with one provider remains valid, while every feature consuming that provider receives its
   shared consequences and exposes missing specialized work.
10. Adding a new feature contribution selects its UI, policy, worker, setting, notification, contract, report, and
    documentation artifacts without editing content-type modules or a second feature list.

## Repeatable Census Probes

These searches are review aids. They locate candidates; they do not decide architecture mechanically.

```shell
rg -l --glob '!**/build/**' '\bEntryType\b'
rg -n --glob '*/src/main/**' 'EntryType\.(MANGA|ANIME|BOOK)|EntryType\.entries'
rg -n --glob '*/src/main/**' 'supports[A-Z]|supportedEntryTypes|isSupported\(|can[A-Z]'
rg -n --glob '*/src/main/**' 'Map<EntryType|Set<EntryType|associateBy.*(type|entryType)'
rg -n --glob '*/src/main/**' 'Entry(Open|Continue|Download|Capability|Consumption|Bookmark|UpdateEligibility|Progress|PlaybackPreferences|ChildList|ChildGroupFilter|LibraryFilter|Preview|Immersive)Interaction'
rg -n --glob '*/src/main/**' 'EntryMediaCacheBucketKeys|ViewerSettingsProvider|PROVIDER_ID'
rg -n --glob '*/src/main/**' 'EntryCatalogueSource|RelatedEntriesSource|EntryPreviewSource|EntryImageSource|SubtitleSource|ConfigurableSource|ResolvableSource|SourceHomePage|WebViewSource|UnmeteredSource'
rg -n --glob '*/src/main/**' 'EntryCapabilityReport|EntryCapabilityCatalog|supportsTypeWide|EntryDownloadCapabilityPolicy'
```

Before Phase 8 closes, rerun the probes and classify every remaining match. A clean compile is not a substitute for that
review.
