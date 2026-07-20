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
- Milestone 4.2.1 implements `T11`, `T12`, `T14`, and `T15` in the working tree. Capability-owned bindings derive graph
  providers and dispatch from one declaration, including the Manga object that independently binds Consumption and
  Bookmarking.
- Milestone 4.2.2 initially split `T04`–`T08` into core, options, per-setting, bulk-candidate, and automatic-filter
  provider claims. F05 later proved the automatic-filter claim was only repeated delegation to shared policy and removed
  it; core, options, per-setting, and genuinely media-specific bulk-candidate providers remain independent.
- Milestone 4.2.3 implements `T09` and `T10` in the working tree. Migration and Merge are independent compatibility
  providers for their shared workflows; Manga and Anime bind both, while Book binds neither.
- Milestone 4.2.4 implements `T13` and `T16`–`T21` in the working tree. Universal Update Eligibility is shared policy;
  Child List and progress labels are independent; false/no-op filter processors are removed; and real Preview and
  Immersive implementations are contributed without flattening their contextual conditions.
- Milestone 4.3 migrates `T22` and `T24`–`T27` in the working tree through one owned runtime module per type. `T23`
  presentation vocabulary is deliberately deferred to its Phase 5 projection migration because its current app-owned
  model cannot enter the runtime service contract without mixing ownership.
- Milestone 4.4 removes the transitional operational registry. One generic provider index derives typed dispatch maps
  directly from bindings, and the old registry-shaped support tests are replaced by generic composition invariants.
- Phase 5 F14 now owns generic Library filter policy and active state. Bookmark-control and outside-release-period
  applicability derive from their independent provider evidence; search/grouping and update scope remain contextual.
- Phase 5 F21 now owns Related Entries orchestration for every composed type while preserving `RelatedEntriesSource`
  and source orientation as contextual external truth. Mixed returned types, profile-aware persistence, live Library
  state, and Entry-details navigation no longer depend on an app-owned capability cast or parallel domain use case.

| ID | Provider or composition fact | Current evidence | Required migration outcome |
| --- | --- | --- | --- |
| `T01` | Content-type identity and ownership | Manga, Anime, and Book plugin factories | Each plugin contributes one owned `ContentTypeContribution`; any provider subset remains valid. |
| `T02` | Open | `EntryOpenProcessor` for all current types | Provider presence is the only type-wide support fact. No open requirement is added. |
| `T03` | Continue | `EntryContinueProcessor` for all current types | Provider presence is the only type-wide support fact; next-item absence remains entry state. |
| `T04` | Core downloads | `EntryDownloadProcessor`; Book construction can omit it | A contributed download provider is optional. Book's construction flag must not become product capability truth. |
| `T05` | Download options | Default-false/null methods inside the download processor; Anime supplies behavior | Represent option resolution as a distinct provider or owned specialized behavior so core downloads do not imply options. |
| `T06` | Download setting behavior | `settingCapabilities` set inside each downloader | Model each genuine specialized setting behavior without a central enum-to-type matrix. Settings visibility is feature-derived. |
| `T07` | Bulk candidate pool | Required method on each download processor | Preserve media-specific pool construction; bulk action selection and intersections belong to the Downloads feature. |
| `T08` | Automatic-download filtering | The three former methods only asserted type and delegated to one shared policy | Remove the artificial provider and derive Automatic Downloads from core Download support. F05 owns shared policy; future genuine media differences must be modeled from their actual requirement without restoring no-op providers. |
| `T09` | Migration | Independent `EntryMigrationProvider` bindings for Manga and Anime | Provider presence is the sole participation fact consumed by F11. Provider absence means unavailable; do not contribute a false result or operational support facade. |
| `T10` | Merge | Provider-free F12 contribution plus feature-owned runtime context | The empty marker is removed. Every composed type receives base Merge automatically; entry type, profile, selection shape, and existing membership remain feature-owned context. |
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
| `T22` | Library progress calculation | F22 binds optional media-evidence providers through the ordinary type plugin | Provider presence is sole summary support; no required runtime field, second root list, public factory, or `getValue` absence failure remains. |
| `T23` | Presentation vocabulary | `EntryTypePresentation` hardcodes three types plus a generic fallback | Type-owned vocabulary becomes projection input. It may vary without deciding support. |
| `T24` | Type runtime services | `addMangaEntryInteractionRuntime`, `addAnimeEntryInteractionRuntime`, `addBookEntryInteractionRuntime` | Install through the same owned contribution boundary or an explicitly linked runtime contribution, not an unrelated type list. |
| `T25` | Viewer-setting providers | Manga/Anime providers plus Book runtime provider list | Associate providers with their owning contributions; settings integration must not require root and UI lists. |
| `T26` | Media caches and image components | F26 binds optional non-empty cache providers through the ordinary type plugin; image components remain owned runtime artifacts | Provider presence selects shared maintenance projections; no root cache list, hardcoded key map, or empty provider remains. |
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
| `F05` | Automatic downloads | Library update discovery and queue start; entry refresh auto-download path; shared category, preference, and unread policy; removal of the redundant per-type filtering opt-in. |
| `F06` | Download lifecycle cleanup | Consumption, reader, player, and Book completion/progress events; remove-after-consumed policy; category exclusions; bookmark protection; physical delete versus cleanup dispatch. |
| `F07` | Download settings and options | Download settings screen; packaging/splitting/parallelism visibility; entry download-options dialog; stream, dub, subtitle, and quality selection; option persistence. |
| `F08` | Download maintenance | Source rename, entry-title rename, backup-restore cache invalidation, migration deletion, advanced cache invalidation, source removal, and catalogue removal paths. |
| `F09` | Consumption | Entry selection and swipes; Library selection; Updates selection; notification action; tracking-progress sync; lifecycle event emission; consumed/unconsumed vocabulary. |
| `F10` | Bookmarking | Entry and Updates actions; bookmarked child/library filters; bookmarked bulk downloads; cleanup protection; selection eligibility and vocabulary. |
| `F11` | Migration | Provider-backed Entry/Library/Browse availability; source/list/search/configuration/dialog flows; explicit-profile preparation and execution; child state and resource mappings; progress, playback preferences, viewer settings, downloads, categories, notes, tracking, covers, Entry/Library state, and F12 replacement; structured failure/cancellation; shared tests and Anime documentation correction. Execute through `features/F11-migration.md`; app-version, backup-import, profile-preference, and schema migrations are excluded. |
| `F12` | Merge | Provider-free workflow/editor/persistence; candidates; explicit-profile navigation; child and Download ownership; Library grouping, initialization, removal, and progress inputs; metadata refresh; backup/restore; Profile Move and cascade cleanup; F11 member replacement; durable Library/cover/Download follow-up and diagnostics. Every disposition is reconciled in `features/F12-merge.md`; Download and Migration cooperation derive from their providers independently of base Merge. |
| `F13` | Library update eligibility | Update worker; structured skip reasons; Stats calculations; smart-update settings; release-window policy; one unconditional feature replacing the former duplicate Manga/Anime/Book implementations. |
| `F14` | Library filtering | Downloaded, bookmarked, consumed/progress, tracker, outside-release-period, source, category, and content-type filters; only capability-dependent filters use graph applicability. |
| `F15` | Progress transfer | Progress provider presence selects backup create/restore; migration copy derives from Progress plus Migration. Reader/player/Book persistence is type-owned live media behavior; independently contributed per-child labels remain F17. |
| `F16` | Playback-preference transfer | Playback Preferences provider presence selects backup create/restore; migration copy derives from Playback Preferences plus Migration, with no direct Anime authorization. |
| `F17` | Child-list behavior | Entry sorting/filtering/display rows; merged headers; missing-count rows; immersive first-child selection; per-child progress labels. |
| `F18` | Child-group filtering | Entry state flows, live-list filtering, multi-member observation, filter controls, excluded-group persistence, backup snapshot/restore, generic storage consequences, group data source, and settings dialog integration. |
| `F19` | Preview | Entry preview state; browse long-press preview; preference/configuration; page loader and open action; contextual source and child requirements. |
| `F20` | Immersive browsing | Catalogue and feed mode entry; per-entry long-press action and source settings evidence; view-mode fallback; contextual source/entry outcomes; renderer loading; provider-derived preload radius; progress and strict lifecycle; source opt-in and metadata-only surface pruning. |
| `F21` | Related entries | Entry action, related-entry screen model/dialog, source orientation, library membership, and normal open flow for returned mixed types. |
| `F22` | Library progress summary | Unified library load; explicit absence; merged state; F02 continue target; F10 bookmark summary; badges; sort/filter inputs; Stats coverage; F13 update inputs; structural F12 adapters; boundary enforcement. |
| `F23` | Type presentation | **Resolved.** Entry, Library, Updates, History, Browse, Download, Duplicate, Tracking, and notification vocabulary/icons/plurals come from discovered type-owned projections; presentation never authorizes an action. |
| `F24` | Library-update notifications | **Migrated.** One Feature projection owns discovered routing/grouping, summary and child plans, Entry-details fallback, and F01/F09/F04-derived actions. F23 supplies vocabulary only; Book uses explicit neutral semantics and a derived route rather than Manga fallback. |
| `F25` | Viewer settings | Provider discovery; reader/player settings hubs; provider-to-screen routing; settings search indexing; entry overrides; profile ownership; reset actions; backup integration; migration copy derived from Viewer Settings plus Migration. |
| `F26` | Media-cache maintenance | Provider-derived artifact discovery, settings labels/preferences, manual and launch clear, refreshed size, and structured user-visible errors. |
| `F27` | Profile preference ownership | **Resolved.** Runtime owner factories discover static and dynamic profile/app/private keys; migration groups retain named legacy corrections; creation/deletion and explicit per-profile operations preserve their existing semantics. |

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

### Phase 6 Owner Classification

This classification controls migration order. “Feature migration” means product consequences move behind a Feature
result after runtime context resolution exists. “Owner-local” means the direct external contract use is the reviewed
implementation of that contract or genuine media mechanics behind a Feature; it is not an application authorization
exception.

| ID | Authoritative evidence | Consequence owner and disposition | Phase 6 milestone |
| --- | --- | --- | --- |
| `C01` | Concrete `EntryCatalogueSource` presence | Catalogue/discovery Feature migration for Browse, search, feed, and migration surfaces; source-manager lookup and paging remain owner-local mechanics. | 6.2 |
| `C02` | `EntryCatalogueSource.supportsLatest` | Catalogue/discovery Feature migration; remove repeated UI/repository booleans as authorization. | 6.2 |
| `C03` | `EntryCatalogueSource.supportsImmersiveFeed` | F20 already owns product consequences; declare its source evidence and blockers to runtime context resolution. | 6.4 |
| `C04` | `SourceMetadata.supportedEntryTypes` | Catalogue/description projection shared by source and extension filters/badges; F20 may prune source surfaces, but actual returned Entry type remains authoritative. | 6.2 |
| `C05` | Concrete `EntryPreviewSource` presence | F19 already owns product consequences; declare source and preference evidence to runtime context resolution. Anime media loading remains owner-local. | 6.4 |
| `C06` | Concrete `RelatedEntriesSource` presence | F21 already owns availability, fetch, persistence, orientation, and UI; declare source evidence to runtime context resolution. | 6.4 |
| `C07` | Concrete `EntryImageSource` and returned image-page media | Resolved by consequence: Manga reader, F03/F04 download, and F20 Immersive resolution remain type-owned mechanics; F20 structures operation failure, the tracker adapter is explicit, and generic covers consume the purpose-specific Cover Network Feature. | 6.5 |
| `C08` | Concrete `SubtitleSource`, returned streams, and selection | Resolved by consequence: F07 owns option availability; Anime player and downloader retain separate type-owned resolution/failure semantics; generic application consumers cannot access the raw source contract. No type-wide Playback flag or shared subtitle facade is introduced. | 6.5 |
| `C09` | Concrete `ConfigurableSource` presence | Source-settings Feature migration for catalogue/extension/preferences/backup surfaces; Kavita/Suwayomi use becomes an explicit tracker adapter relationship. | 6.3 |
| `C10` | Concrete `SourceHomePage` presence and returned URL | Source-home Feature migration for catalogue/feed/migration/extension navigation; tracker use remains an explicit adapter relationship. | 6.3 |
| `C11` | Concrete `WebViewSource` presence and headers/navigation | Entry/source WebView Feature migration shared by Entry actions and the WebView runtime. | 6.3 |
| `C12` | Concrete `ChapterWebViewSource` and child URL | Resolved in 6.5.2: the WebView Feature owns child availability/URL/actions through a separate contextual integration and reports a missing type-owned host adapter only when source context applies; the public source contract and legacy adapter remain external authorities. | 6.5 |
| `C13` | Concrete `ResolvableSource` presence and resolution result | Deep-link resolution Feature migration; returned Entry/child type is authoritative. | 6.3 |
| `C14` | `EntryItemOrientationProvider` with source default | Catalogue/description projection owns Browse/feed/library orientation; F21 already carries orientation in its loaded result. | 6.2 |
| `C15` | Three source-owned child-list contracts | `SyncEntryWithSource` remains the single owner-local mechanics coordinator; invoking Features declare refresh state/results as context rather than recasting the interfaces. | 6.6 |
| `C16` | Concrete `entry.UnmeteredSource` presence | Downloader policy remains type-owned F03 mechanics; F24 owns Library-update queue warning. The unused legacy source-api marker is a C24 ABI/adapter obligation, not runtime authority. | 6.6 |
| `C17` | Installed, missing, bundled Local, or stub source state | Distributed contextual evidence for F04/F14/F24 and source action/catalogue Features; every blocker is reconciled by consequence, not by a global local/stub capability. | 6.3, 6.4, 6.6 |
| `C18` | Tracker-declared `supportedEntryTypes` | New Tracking Feature composes Entry type, tracker applicability, authentication, Entry/Library actions, sync, filters, Stats, and documentation. | 6.7 |
| `C19` | Tracker-owned dates/privacy/status/scoring facts | Same Tracking Feature exposes purpose-specific fields/actions; tracker implementations remain authoritative. | 6.7 |
| `C20` | Concrete Entry/library/child/progress/media state | Distributed operation context for the Feature that owns each decision; never a global Entry-State capability. | 6.4–6.7 |
| `C21` | Concrete action selection and membership shape | F04, F11, F12, Library, and other selection Features own separate eligibility results; never a generic Selection support flag. | 6.4 |
| `C22` | Concrete preferences, profile, and authentication state | Distributed reactive context for F05/F06/F13/F14/F19/F20/F25/F27 and tracking; preference ownership remains F27, product meaning remains feature-owned. | 6.4–6.7 |
| `C23` | Concrete platform, renderer, format, protection, and resolution state | F25 owns cross-application settings surfaces; type-owned players/readers/loaders retain live mechanics. Resolved for 6.5: Immersive renderer construction is structured by F20, and Book reader/downloader share one internal processor registry while Viewer Settings remains independently optional. | 6.4, 6.5 |
| `C24` | Legacy Manga adapter and bundled Local source contracts | Compatibility-only: translate to current source contracts, retain explicit scope, and verify no application support authority depends on legacy identities. | 6.8 |

Rows with multiple milestones are closure ledgers, not shared implementation owners. Each assigned Feature records its
own evidence and consequence; Phase 6.8 verifies that no consumer was lost between them.

Phase 6.5 reconciliation reran the nested production census for `C07`, `C08`, `C12`, `C20`, `C22`, and `C23` and found
no unclassified media consumer. Raw image/subtitle source contracts are rejected across app, data, domain, and both
generic presentation modules; type-owned media mechanics, source compatibility, and purpose-specific root Features
remain explicit owners rather than exceptions available to generic code.

Phase 6.6.0 classifies all refresh/network consumers. `SyncEntryWithSource` remains the internal source-contract and
persistence mechanics owner behind a new Source Refresh Feature. Entry/metadata are base refresh consequences; F11,
F13/Library Update, F20, and Deep Link declare their own relationships. F24 owns the Library queue-size warning derived
from metered source context. Manga downloader use of `UnmeteredSource` remains a type-owned F03 operation mechanic.

Phase 6.6.1 installs the Source Refresh Feature and graph/context contract without migrating consumers. Installed-source
absence is contextual; retained stub metadata is not an executable state of the authoritative refresh lookup. Operation
failures are structured after an available snapshot. Every request uses strict Entry-profile persistence and
Feature-owned resolution of the existing title-update preference. The domain coordinator remains the sole interpreter
of `C15` source contracts.

Phase 6.6.2 moves direct Entry and metadata consumers behind Source Refresh and makes F20 and Deep Link own their
cross-feature refresh relationships. Raw sync mechanics are now guarded at the architecture boundary. The remaining
F11 migration and F13/Library Update findings are deliberately unresolved obligations assigned to 6.6.3 and 6.6.4;
they are not boundary exceptions.

Phase 6.6.3 makes target refresh an F11-owned relationship for automatic search, optional details, explicit target
selection, and mandatory pre-execution synchronization. Execution refreshes the authoritative target from its
profile-pinned inspection; the application host no longer mixes source operations with persistence. The raw-sync
boundary now identifies only F13/Library Update for 6.6.4.

Phase 6.6.4 adds a provider-less Library Update Refresh Feature after F13 eligibility. It preserves run context and maps
Source Refresh into library-specific results while passing only successful inserted children to F05 and notification
collection. The raw-sync application boundary is now green with no migration exceptions.

Phase 6.6.5 moves the Library queue-size threshold and `UnmeteredSource` interpretation into F24. Android notification
code renders only the structured decision, and application boundary validation rejects raw metering inspection. Manga
downloader metering remains type-owned F03 execution mechanics.

Phase 6.6.6 closes the nested census. `SyncEntryWithSource` is the only C15 mechanics interpreter; Source Refresh is the
only root boundary and has no raw application consumer. F24 and Manga F03 are the only current C16 interpreters. C17,
C20, and C22 remain consequence-specific invocation evidence with strict Entry-profile refresh. The unused legacy
source-api metering marker is recorded for C24 compatibility reconciliation in 6.8.

Phase 6.7.0 classifies the complete tracker surface before migration. `TrackerManager` and tracker implementations
remain the external authorities, while one split Tracking Feature and one application host boundary will own all
application consequences. Entry/session/operation, automatic synchronization, F11 preparation, F14 inputs, Library
scores, Stats, settings, backup diagnostics, presentation, and documentation are included. Tracker implementations,
credential storage, registry construction, the single host adapter, and tracker-specific OAuth callback parsing remain
reviewed owner-local mechanics. The hardcoded settings service list is an explicit migration obligation rather than an
acceptable external-system exception.

Phase 6.7.1 establishes the single Tracking Feature/host boundary before migrating consumers. Registered applicability
and authenticated Entry sessions now resolve from neutral host snapshots; every censused downstream relationship is a
discovered graph consequence for provider-less contributed types. The app host is the only raw tracker adapter exposed
to root policy, and build validation derives every host declaration to prevent it from becoming a parallel consumer
API. Existing raw tracker consumers remain visible obligations without a temporary allowlist.

Phase 6.7.2 moves Entry action availability, badge/session state, dialog rows, and row presentation behind that Feature.
The Entry screen and dialog no longer filter the tracker registry independently, and presentation receives neutral
service identity, capabilities, track state, status labels, and tracker-formatted score evidence. The raw tracker-bearing
`TrackItem` wrapper is removed. Dialog commands remain explicitly assigned to 6.7.3; no temporary consumer allowlist or
parallel session adapter was added.

## Approved Findings Outside the Current Interaction Contribution Boundary

These classifications were approved on 2026-07-18. “Include” means the behavior must participate in the
application-wide graph through its real owner; it does not mean moving all code into the `entry-interactions` module.

| Finding | Evidence | Proposed classification for verification |
| --- | --- | --- |
| Library progress was a second required-per-type provider system | Former calculator factories, runtime-contribution field, root resolver list, and Domain consumer | **Resolved in F22.** Optional plugin-bound evidence providers feed one Feature; absence is structured and Library items remain visible. |
| Viewer settings were discovered operationally but routed through a hardcoded UI map | Former `ViewerSettingsInteraction`, `EntryInteractionRuntime.kt`, `SettingsViewerHubScreens.kt`, `SettingsSearchScreen.kt` | **Resolved in F25.** An optional ordinary plugin provider contributes one or more surfaces; exact app-owned projections feed both hubs and search, and missing projections fail by surface ID. |
| Media caches use a provider list but hardcoded keys, labels, and auto-clear policy | Former `EntryRuntimeContracts.kt`, `EntryInteractionRuntime.kt`, `SettingsDataScreen.kt`, `AutoClearMediaCache.kt`, and `MainActivity.kt` paths | **Resolved in F26.** Optional plugin-bound providers expose owned artifacts; one Feature derives settings, preferences, clearing, refresh, and errors without a root/key map. |
| Profile preference ownership manually instantiates known preference owners | Former `ProfilePreferenceOwnership.kt` recorder and `ProfileManager.kt` | **Resolved in F27.** General and Entry feature/type preference owners register beside their actual runtime factory; static/dynamic ownership drives migration without a recorder list. |
| Runtime type installation has several parallel lists | `EntryInteractionRuntime.kt`, `EntryLibraryProgressRuntime.kt`, `AppModule.kt`, `App.kt` | **Include.** Keep one environment installation boundary; remove parallel type/runtime/artifact lists. |
| Tracking has its own applicability system | `TrackerCapabilities.kt`, `TrackerManager.kt`, entry/library/track consumers | **Include as external input.** Trackers retain ownership; tracking feature composes every consequence. |
| Source SDK capabilities drive many UI and policy paths directly | `entry-source-api`, source/browse/entry/domain consumers | **Include as contextual inputs.** Preserve public contracts while removing repeated consumer composition. |
| Library-update notification semantics enumerate Manga and Anime only | `LibraryUpdateNotifier.kt`, `Notifications.kt` | **Resolved in F24.** The notifier renders one Feature-owned projection and channels come from discovered routes. A frozen compatibility adapter preserves the two shipped identities; Book/future routes derive from content identity and are collision-validated. |
| Backup and migration contain current capability gates | `EntryBackupCreator.kt`, `EntryRestorer.kt`, `MigrateEntryUseCase.kt` | **Include capability consumers.** Keep wire/legacy/media conversion branches as compatibility boundaries. |
| Entry-type presentation is a central three-type map | Former `EntryTypePresentation.kt` map and its consumers | **Resolved in F23.** Type-owned providers enter ordinary plugin discovery; the app consumes one Feature, and contributed versus generic provenance remains explicit. |
| Missing-child gaps are authorized in presentation code | `EntryScreen.missingChildCount` | **Include.** Move applicability to child-list/feature behavior; keep wording in presentation. |
| Source content-type filters and badges depend on descriptive metadata | source/extension filter and indicator files | **Include as a projection of external metadata.** Do not treat metadata as proof of entry feature support. |
| Boundary enforcement contains broad exclusions and a concrete root import allowlist | `EntryInteractionBoundaryCheckTask.kt` | **Include as enforcement work.** Replace lists that require edits for each type/bridge and add rules against new parallel authorities. |
| Book format processors form a nested media-specific registry | `BookProcessorRegistry.kt`, `BookRuntimeModule.kt`, EPUB/prose processors, Book reader and downloader | **Verified for Phase 6.5.** Keep one internal processor registry as the format/protection authority shared by reader and downloader mechanics. Viewer Settings remains an independent optional F25 provider; a processor without settings is valid. Expose no processor registry or format-support flag across the type boundary. |
| Child WebView support is composed inside the Manga reader | `ChapterWebViewSource`, `ReaderViewModel.kt`, `ReaderActivity.kt`, legacy adapters | **Resolved in 6.5.2.** The source contract and legacy adapter remain external; the existing WebView Feature resolves canonical child URL/source identity, selects reader actions, and requires Manga's media-host adapter only after source context applies. |
| Settings navigation/search contain curated screen lists | `SettingsMainScreen.kt`, `SettingsSearchScreen.kt`, former viewer settings screen map | **Partially resolved in F25.** Viewer Settings destinations are exact feature-owned projections shared by hubs and search. Global non-feature settings may remain application navigation. |

## Direct-Type Branch Disposition

Every production direct-type branch found by the census must have one of these dispositions:

- **Migrate to a contribution/projection:** `EntryTypePresentation`, missing-child gaps, download settings lookup, library
  update notification type/channel selection, tracker default support, and feature UI availability. The former
  playback-preference backup gate is resolved by F16.
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
rg -n --glob '**/src/main/**' 'EntryType\.(MANGA|ANIME|BOOK)|EntryType\.entries'
rg -n --glob '**/src/main/**' 'supports[A-Z]|supportedEntryTypes|isSupported\(|can[A-Z]'
rg -n --glob '**/src/main/**' 'Map<EntryType|Set<EntryType|associateBy.*(type|entryType)'
rg -n --glob '**/src/main/**' 'Entry(Open|Continue|Download|Capability|Consumption|Bookmark|UpdateEligibility|Progress|PlaybackPreferences|ChildList|ChildGroupFilter|LibraryFilter|Preview|Immersive)Interaction'
rg -n --glob '**/src/main/**' 'EntryMediaCache(Provider|Artifact|Feature)|ViewerSettingsProvider|PROVIDER_ID'
rg -n --glob '**/src/main/**' 'EntryCatalogueSource|RelatedEntriesSource|EntryPreviewSource|EntryImageSource|SubtitleSource|ConfigurableSource|ResolvableSource|SourceHomePage|WebViewSource|UnmeteredSource'
rg -n --glob '**/src/main/**' 'EntryCapabilityReport|EntryCapabilityCatalog|supportsTypeWide|EntryDownloadCapabilityPolicy'
```

Before Phase 8 closes, rerun the probes and classify every remaining match. A clean compile is not a substitute for that
review.
