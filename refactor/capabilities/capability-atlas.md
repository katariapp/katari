# Capability Atlas

Status: pre-migration behavior atlas retained as historical evidence; current architecture is tracked in `status.md`

The registration/report descriptions below record the baseline that motivated the refactor. They are not descriptions
of the post-Milestone-4.4 implementation: provider bindings now feed the feature graph and a generic operational provider
index directly, and the old registry/report authority no longer exists.

This file records the current executable evidence for capability support. It is an inventory of facts to review, not a new source of behavioral truth and not a proposal for the final capability model.

The exhaustive migration control surface is now
[`migration-inventory.md`](migration-inventory.md). The wider census found additional participation systems outside the
original interaction audit: library-progress calculators, viewer-setting routing, media-cache descriptors and
auto-clear policy, profile preference ownership, runtime installation lists, tracker applicability, source SDK
capabilities, backup/migration gates, notification routing, settings navigation, and boundary-check allowlists. Those
systems must be migrated or explicitly retained through their real owners; this atlas must not be mistaken for the
complete migration checklist.

## Classification

Every capability or capability-like gate is provisionally classified as one of:

- **Type-wide:** stable for a content type
- **Provider-backed:** proven by registration of an implementation provider
- **Source-dependent:** determined by the current source
- **Entry-dependent:** determined by the current entry or media
- **Selection-dependent:** determined by a collection and its constraints
- **External integration:** determined by a tracker, extension, platform, or other integration
- **Derived:** follows from other capabilities and shared feature policy
- **Presentation-only:** vocabulary or imagery with no authority over behavior
- **Compatibility boundary:** a legitimate type or format distinction that is not feature capability gating

These inventory classifications are interpreted by the accepted Milestone 0.3 decisions. In particular, processor registration can prove that a provider exists without proving every behavioral sub-capability exposed by that provider.

## Evidence Boundaries

Milestone 0.1 inventoried declarations, defaults, unsupported results, and direct gates. Milestone 0.2 now maps their application consumers, tests, boundary enforcement, combinations, and documentation coverage without changing expected behavior.

Primary evidence locations:

- Processor contracts and defaults: `entry-interactions/spi/.../EntryInteractionPlugin.kt`
- Provider-backed dispatch and missing-provider fallbacks: `entry-interactions/spi/.../EntryInteractionComposition.kt`
- Type registrations and implementations: `entry-interactions/{manga,anime,book}`
- Runtime assembly: `entry-interactions/.../EntryInteractionRuntime.kt`
- Presentation metadata and remaining application gates: `app/src/main/java`
- Public source inputs: `entry-source-api` and legacy source contracts
- Tracker inputs: tracker capability contracts under `app/src/main/java/eu/kanade/tachiyomi/data/track`

## Processor Registration Inventory

The registry maintains one processor map per category keyed by `EntryType` and rejects duplicate registrations. A check means a provider is registered in the production plugin; it does not necessarily mean all operations on that provider are supported.

| Processor category | Manga | Anime | Book | Evidence meaning |
| ------------------ | ----- | ----- | ---- | ---------------- |
| Open | Registered | Registered | Registered | Provider-backed open behavior exists for all three types. |
| Continue | Registered | Registered | Registered | Provider-backed next-item selection and opening exist for all three types. |
| Download | Registered | Registered | Conditional | Book registers only when `downloadsEnabled`; production runtime enables it, while construction can omit it. |
| Capability | Registered | Registered | Registered | Provider exists, but migration and merge are separate default-false methods. |
| Consumption | Registered | Registered | Registered | Consumed-state behavior exists independently from bookmark mutation. |
| Bookmark | Registered | Absent | Absent | Manga's operational provider proves Bookmarking; Anime and Book carry explicit intentional-absence outcomes. |
| Update eligibility | Shared | Shared | Shared | F13 owns one unconditional policy for every runtime-contributed content type; no type provider exists. |
| Progress | Registered | Registered | Registered | Snapshot, restore, and copy behavior exist for all three types. |
| Playback preferences | Absent | Registered | Absent | Anime alone provides playback preference snapshot, restore, and copy behavior. |
| Child list | Registered | Registered | Registered | Sorting and display-list behavior exist for all three types. |
| Child-group filter | Provider | Absent | Absent | Manga contributes genuine group discovery/normalization; provider absence is the complete Anime/Book result. |
| Library filter | Registered | Registered | Registered | Provider presence does not prove the outside-release-period sub-capability. |
| Preview | Registered | Registered | Absent | Manga and Anime providers use different support evidence; Book reaches fallbacks. |
| Immersive | Registered | Registered | Absent | Manga and Anime have renderers; successful loading still depends on source/media. |

Provisional owner: registration is owned by each type plugin; processor uniqueness and missing-provider behavior are owned by the shared registry.

## Behavioral Evidence Inventory

| Capability or gate | Provisional scope | Current executable evidence | Provisional behavioral owner | Current outcome and concern |
| ------------------ | ----------------- | --------------------------- | ---------------------------- | --------------------------- |
| Open entry child | Provider-backed | Presence of `EntryOpenProcessor` | Type plugin and processor | Manga, Anime, and Book registered. |
| Continue entry | Provider-backed, entry-dependent | Presence of `EntryContinueProcessor`; result depends on stored children/progress | Type plugin and processor | Manga, Anime, and Book registered. |
| Core downloads | Provider-backed | Presence of `EntryDownloadProcessor` contributes Downloads evidence | Type plugin and downloader | Manga, Anime, and production Book are registered. Missing providers produce unresolved reports and neutral operation fallbacks. |
| Download settings | Provider-backed/type-wide | `EntryDownloadProcessor.settingCapabilities`, default empty | Downloader plus settings feature | Manga declares archive packaging, tall-image splitting, parallel source transfers, and parallel item transfers. Anime and Book declare none. |
| Download options | Entry- and source-dependent | `supportsDownloadOptions(entry)`, default false; `resolveDownloadOptions`, default null | Downloader | Anime returns true and resolves stream/dub/subtitle/quality choices subject to source/media data. Manga uses the default false. Book explicitly repeats false/null. |
| Bulk download | Provider-backed | Download-provider registration contributes Bulk Downloads evidence because every provider supplies a candidate pool | Downloader registration and shared policy | Manga, Anime, and Book report support. Constant downloader support methods and the generic capability facade are removed. |
| Bulk download: bookmarked children | Derived | Shared bulk policy requires Bulk Downloads + Bookmarking, then filters the media-specific candidate pool | Entry download feature policy | Manga is supported. Anime and Book receive structured `Unsupported` from missing Bookmarking evidence; downloaders contain no bookmark-specific branches. |
| Download cleanup | Derived policy over Downloads + Bookmarking | Shared lifecycle policy consults Bookmarking support and the remove-bookmarked preference before dispatching delete/cleanup | Entry download lifecycle feature | Bookmark protection applies automatically to bookmark-capable types; the preference preserves its override semantics. |
| Consumed state | Provider-backed | Registered `EntryConsumptionProcessor` and required mutation methods | Type processor | All three types support consumed/unconsumed behavior. Manga has custom partial-progress semantics; Anime and Book use their media-specific persistence/default eligibility. |
| Bookmark children | Provider-backed | Presence of `EntryBookmarkProcessor`; compatibility dispatch uses the same provider map | Bookmark provider | Manga registered. Anime and Book have no provider and retain explicit intentional-absence outcomes. Consumption registration alone does not prove bookmarking. |
| Migration | Entry-dependent method, currently type result | `EntryCapabilityProcessor.supportsMigration(entry)`, default false | Capability processor | Manga true, Anime true, Book inherits false. Documentation currently disagrees for Anime. |
| Merge | Entry- and selection-dependent | `supportsMerge(entry)`, default false; selection also requires at least two entries, same type, and at most one already-merged entry | Capability processor plus shared selection policy | Manga true, Anime true, Book false. Documentation agrees with this result. |
| Update eligibility | Shared, entry/policy-dependent | The runtime content type is composed; F13 evaluates shared Entry state and smart-update preferences | Update Eligibility feature | The former Manga/Anime/Book processors were identical and removed. Every contributed type participates without a provider; an uncomposed type is a runtime-composition error. |
| Progress transfer | Provider-backed | Presence of `EntryProgressProcessor` | Type processor | All three provide snapshot, restore, and copy. |
| Playback preference transfer | Provider-backed | Presence of `EntryPlaybackPreferencesProcessor` | Type processor plus Feature coordinator | Anime registered; Manga and Book receive structured inapplicability from provider absence. |
| Library progress summary | Optional provider-backed Feature | `EntryLibraryProgressProvider` bindings expose media evidence through the ordinary type plugin | Library Progress Feature plus type evidence provider | Provider presence is sole support truth. Shared counts, merges, F02/F10 relationships, UI inputs, Stats coverage, and F13 inputs are Feature-owned; absence is structured and visible. |
| Child-list construction | Provider-backed | Presence of `EntryChildListProcessor` selected by F17 | Child List feature plus type processor | All three current types contribute providers. Absence is valid and returns structured inapplicability; it does not fall back to unsorted children. |
| Per-child progress labels | Derived from independent providers | F17 requires both Child List and Child Progress provider presence | Child List feature plus progress provider | All three current types contribute both. Child List alone never implies labels, and Child Progress alone does not manufacture list behavior. |
| Outside-release-period library filter | Provider-derived, then preference/Entry-dependent | Current Library types plus enabled preference and target fetch-interval state | Library Filtering feature | Manga and Book contribute the marker; Anime contributes nothing. Unsupported targets pass through without a false/no-op provider. |
| Child-group filtering | Provider-backed | Operational provider binding | Child-group processor plus F18 Feature | Manga contributes group discovery/normalization. F18 derives state, filtering, persistence, backup, and controls; Anime and Book absence is valid and structured. |
| Entry preview | Provider-backed, then source/preference/child-context dependent | Presence of `EntryPreviewProcessor`; optional configuration provider; declared load mode; contextual availability | Preview feature plus type processor | Manga and Anime contribute Preview providers; Manga declares child-backed loading, Anime declares entry-level loading, and Book absence is valid. Configuration, Child List, and Open relationships are derived independently. |
| Immersive rendering | Provider-backed, then source/media-dependent | Presence of `EntryImmersiveProcessor`, composed by F20 with source opt-in, declared load mode, and load-time media resolution | Immersive feature plus type processor | Manga and Anime contribute child-backed providers. Manga loading requires an `EntryImageSource` and image-page media; Anime must resolve a playable stream. Book has no provider. Entry-level providers remain valid without Child List. |

## Defaults, Unsupported Results, and No-Ops

Defaults are executable statements of absence and can hide missing work when treated as harmless implementation details.

| Evidence | Current fallback | Provisional interpretation |
| -------- | ---------------- | -------------------------- |
| Missing download processor | Reports Downloads and Bulk Downloads unresolved; options null, bulk candidates `Unsupported`, auto-download candidates empty, counts/statuses neutral, mutations no-op | Missing positive evidence remains visible while operational fallbacks stay safe. |
| `settingCapabilities` | Empty set | No type-specific download setting declared. |
| `supportsDownloadOptions` / option resolver | False / null | Download options absent unless the provider opts in. Book redundantly restates the default. |
| Bulk bookmarked candidates | Shared policy returns `Unsupported` unless Bulk Downloads + Bookmarking are supported | Derived result follows authoritative evidence rather than downloader-specific absence. |
| Missing capability processor or default methods | Migration false; merge false | Absence by registration/default rather than a reviewed reason. |
| Missing bookmark provider | Compatibility support and eligibility queries return false; mutation is a no-op | Safe compatibility behavior; intentional absence still requires an explicit owned outcome in the capability report. |
| Anime child-group provider | False support/apply, empty flows/sets, setter no-op | An unsupported implementation is registered, making provider presence misleading. |
| Missing child-group provider | False support/apply, empty flows/sets, setter no-op | Effective behavior matches Anime through a different evidence path. |
| Missing playback-preference processor | Structured feature inapplicability; no provider dispatch | Optional provider-backed behavior. Supported data absence is reported separately. |
| Missing preview processor | Unsupported, configuration disabled, and child required by fallback | Multiple behavioral answers are synthesized from provider absence. |
| Missing immersive processor | Structured entry inapplicability; an empty provider set also closes source-level surfaces with a distinct no-runtime result | Valid provider absence through F20; no surface advertisement, numeric fallback, or lifecycle fallback. |
| Missing Child Progress provider | F17 returns structured inapplicability for labels | Optional enrichment independently composed with Child List rather than a default empty method. |
| Download `cleanup` / `renameEntry` | Cleanup delegates to delete; rename defaults no-op | Shared operational defaults whose applicability must be verified rather than inferred as capability support. |

## Presentation and Direct Type Gates

| Gate | Provisional scope | Current evidence and owner | Concern |
| ---- | ----------------- | -------------------------- | ------- |
| Bookmarked download menu availability | Derived application policy | Entry and Library obtain availability from `EntryDownloadCapabilityPolicy`; the dropdown receives the derived result | Bulk Downloads + Bookmarking has one feature-owned behavioral answer; type presentation contributes wording only. |
| `missingChildCount(EntryType, ...)` | Type-wide behavioral gate | Application presentation code calculates gaps only for Manga | A direct `EntryType.MANGA` check owns availability outside the interaction model. Whether this is fundamental, derived, or intentional is unresolved. |
| Download settings capability lookup | Type-wide behavioral/presentation gate | Settings screen reads `settingCapabilities()[EntryType.MANGA]` specifically | The settings surface assumes Manga owns configurable download settings even though the API returns a map by type. |
| Library-update notification projection | Feature-owned routing, grouping, presentation composition, and action derivation | F24 discovers every composed type, consumes F23 vocabulary, and derives F01/F09/F04 actions independently | Manga/Anime shipped identities remain in a frozen compatibility adapter; Book/future routes derive from content identity and are collision-validated. |
| Backup snapshot and restore branches | Mixed capability integration and compatibility boundary | Playback preferences follow the provider-derived Feature; Anime download preferences still use a media gate; Manga scanlator/viewer and legacy progress conversions use `EntryType.MANGA` | The playback-preference capability consumer is resolved in F16. Remaining branches retain their separate feature or compatibility owners. |
| History subtitle and partial-progress label branches | Presentation-only | `EntryType` selects media vocabulary | Legitimate terminology differences; these must not become capability truth. |
| Icons, labels, plurals, and media nouns in `EntryTypePresentation` | Presentation-only | Presentation mapping | Legitimate type presentation, except where a field controls behavior as above. |
| Backup model defaults and legacy type conversions | Compatibility boundary | Backup wire models, legacy Manga defaults, and old playback/progress conversion branches | Legitimate wire/storage compatibility; excluded from capability modeling unless they also gate a current product feature as identified above. |
| Manga-specific viewer settings copied during migration | Compatibility/media boundary | Migration use case branches for Manga viewer flags | Media-specific data transfer, not evidence that migration itself is Manga-only. |
| Debug-only Anime launcher filtering and preview/sample data | Non-production/tooling | Debug and Compose preview branches | Excluded from user-facing capability truth. |

## External and Contextual Inputs

These inputs affect whether behavior can execute for a particular source, entry, selection, or device. They must not be flattened into unconditional type support.

| Input | Provisional scope | Current evidence | Provisional owner / implication |
| ----- | ----------------- | ---------------- | ------------------------------- |
| Source `supportedEntryTypes` | External integration, descriptive | Optional `SourceMetadata`; empty is treated as unavailable and each returned entry's type remains authoritative | Source/extension metadata used to describe/filter a provider, not proof of feature support. |
| Source latest support | Source-dependent | `EntryCatalogueSource.supportsLatest` and legacy equivalent | Source contract controls latest-update availability. |
| Source immersive-feed opt-in | Source-dependent | `EntryCatalogueSource.supportsImmersiveFeed`, default false | Source controls catalogue/feed entry into immersive browsing; distinct from type renderer support. |
| Static previews | Source-dependent | Optional `EntryPreviewSource` | Anime preview requires this source provider; type registration alone is insufficient. |
| Related entries | Source-dependent | Optional `RelatedEntriesSource` consumed by F21 | Source-provided relationship feature; missing/unsupported source is structured and related entries may have mixed authoritative types. |
| Image-page loading | Source/media-dependent | Optional `EntryImageSource` plus `EntryMedia.ImagePages` | Required by Manga image download/immersive paths; an operational media requirement, not a universal type flag by itself. Generic cover requests consume a separate purpose-specific network-access Feature result. |
| External subtitles | Source- and selection-dependent | Optional `SubtitleSource` and playback selection | Anime options, download execution, and playback retain distinct owner-local failure semantics; generic application code cannot consume the raw contract. |
| Chapter-list semantics | Source-dependent | `EmptyChapterListSource`, `IncrementalChapterSource`, `ChapterNumberRecognitionSource` markers | `SyncEntryWithSource` remains their single mechanics owner behind Source Refresh; they are orthogonal to content-type feature support. |
| Source preferences, home page, metering, and WebView contracts | External integration | `ConfigurableSource`, `SourceHomePage`, `UnmeteredSource`, and WebView source interfaces | Purpose-specific Features compose settings, home, Entry/child WebView, Source Refresh, and F24 queue-warning consequences. Downloader metering stays type-owned; none is a type-wide claim. |
| Local or stub source state | Source-dependent | `isLocalOrStub` and related checks | Blocks or removes web, browse, download, and other source-backed actions despite type capability. |
| Tracker-supported entry types | External integration | `TrackerCapabilities.supportedEntryTypes`, defaulting to Manga | Tracker availability is owned by each integration and must compose with entry type support rather than redefine it. |
| Tracker dates, privacy, and scoring | External integration | Tracker capability properties and available score list | Integration-specific behavior, not a content-type capability declaration. |
| Merge selection shape | Selection-dependent | Minimum size, homogeneous type, existing-merge constraint, and per-entry support | Shared selection policy composes with provider support. |
| Download choices | Entry/source/selection-dependent | Resolved media, stream, dub, subtitle, and quality availability | A type may support options without every entry exposing every choice. |
| Preview enabled state | Derived from support plus preference | Preview configuration combines preference and provider/source support | User configuration must not be confused with fundamental support. |
| Picture-in-picture | Platform-dependent plus type-feature-specific | Anime player checks platform feature and preference | Device/runtime capability, not stable content-type truth. |
| Viewer auto-scroll | Entry/renderer-dependent | Manga viewer layout exposes its own support predicate | Renderer capability internal to a media implementation. |

## Consumer Graph

This graph records where the inventoried facts currently have consequences. It identifies dependency edges; it does not approve the current ownership.

| Fact or capability | Primary workflow consumers | Related UI and actions | Policy, background, and integration consumers |
| ------------------ | -------------------------- | ---------------------- | --------------------------------------------- |
| Open | Entry child rows, updates, child deep links, browse preview sheet, immersive content | Notification open-child action; debug Anime launcher | F01 routes availability, dispatch, and pending intents through the graph-derived Open coordinator. History resume belongs to Continue; its stale unproduced Open event was removed. |
| Continue | Entry continue action, library continue action, history resume action | Type-specific no-next-item vocabulary | Type processors select from stored progress and merged members. |
| Download provider presence | Entry child actions and swipe actions, updates actions, library badges/counts, download queue, More tab | Entry and library selection menus; stats total; download initialization gate in `MainActivity` | Shared download job/runtime/notification manager; backup cache invalidation; source and entry rename hooks; migration deletion; advanced cache maintenance. |
| Download setting capabilities | Download settings screen | Settings visibility for packaging, splitting, and parallelism | Currently queried only for Manga, despite the API returning capabilities by type. |
| Download options | Entry child download action and options dialog | Anime stream/dub/subtitle/quality selection | Anime preference persistence and source/media resolution. |
| Bulk download | Entry and library bulk menus; library-update notification download action | Selection availability excludes local items; entry action excludes local/stub sources | Availability reads the production report; candidate-pool resolution stays media-specific and shared selection stays feature-owned. |
| Bookmarked bulk download | Entry/library download dropdown | `EntryDownloadCapabilityPolicy` derives availability from Bulk Downloads + Bookmarking for each affected selection | The same feature-owned rule now controls UI availability and runtime applicability. |
| Automatic downloads | Library-update worker | No direct UI beyond download/category preferences | Worker asks each downloader to filter new items, queues without starting, then starts the shared runtime once. |
| Consumption | Entry child selection/swipes, Updates selection, library selection | Notification mark-consumed action; tracking sync can mark children consumed | Every type processor reports `MarkedConsumed` to shared download lifecycle policy. |
| Bookmarking | Entry multi-select and swipe action; Updates selection action | Bookmarked library/child filters and capability-derived bookmarked-download menu availability | Shared download lifecycle protects bookmarked downloads only when the type report supports Bookmarking. |
| Download cleanup | No independent capability surface | Delete actions in entry, updates, migration, and cache maintenance | Shared lifecycle handles marked-consumed cleanup and completion cleanup; media downloaders own physical deletion/cleanup. |
| Migration | Entry overflow action, library selection, migration configuration/list/search | Migration flags include remove downloads and data transfer | Migration use case copies progress and playback preferences, chapters, categories, tracks, settings, and optional data after capability checks. |
| Merge | Entry add/manage-merge actions and library selection | Merge target/dialog eligibility | Shared selection policy requires same type and valid merge shape; child lists, continue, downloads, progress, and library summaries are merge-aware independently. |
| Update eligibility | Library-update worker | Stats calculations and library restriction settings | F13 applies one unconditional smart-update policy to every runtime-contributed content type; no type provider or opt-in exists. |
| Outside-release-period library filter | Library filtering | Structured filter availability and active state | F14 derives applicability from provider evidence for current Library types. It remains separate from F13 update eligibility even though both use release-period concepts. |
| Progress transfer | Backup create/restore and migration copy through F15 | Live reader/player/Book persistence remains media-owned; Entry rows obtain independently contributed labels through F17 | Portable transfer providers adapt the shared model without becoming authorities for runtime persistence or child-list presentation. |
| Playback preference transfer | Backup and migration | Provider-derived Feature boundary | Backup snapshot, restore, and migration copy follow graph-selected provider presence without concrete-type authorization. |
| Library progress summary | Unified library loading and merged library state | Optional badges, sort/filter inputs, Stats coverage, F13 inputs, and F02 Continue target | F22 discovers optional providers through type plugins; unsupported entries stay structurally visible with explicit `Inapplicable` summaries. |
| Child-list construction | Entry screen sorting, display construction, merged grouping, missing counts, and preview chapter choice | Immersive screen selects the first reading-order child | F17 selects all consequences from provider evidence and exposes one application feature; filtering policy remains F14/F18-owned. |
| Child-group filtering | Entry screen state, live-list filtering, available/excluded group flows, settings, and backup | Provider presence selects every shared consequence; merged state observes and mutates every member | Manga provider supplies only group discovery/normalization. F18 owns policy and generic persistence; Anime and Book are inapplicable by provider absence. |
| Preview | Entry screen and browse long-press preview sheet | Preview preference, size, page count, and long-press fallback order | Manga loads image preview media; Anime requires `EntryPreviewSource`; Book is absent. Enabled preference and executable support are combined at the consumer. |
| Immersive | Catalogue/feed mode, source settings evidence, and immersive screen model | Per-entry long-press availability and fallback order | F20 composes source opt-in with provider evidence, derives Child List/Open relationships, selects preload, and strictly routes load/render/progress/release. Returned Entry type remains authoritative over descriptive source metadata. |
| Related entries | Entry action and related-entry dialog | Source orientation, profile-aware persistence, live Library membership, and Entry-details navigation | F21 selects shared orchestration for every composed type, while the concrete source contract alone controls contextual availability. Returned order and types remain authoritative. |
| Source supported types | Source/extension content-type filters and badges | Extension and source detail indicators | Metadata is descriptive; returned entry type remains authoritative. |
| Source latest/home/settings/WebView/related capabilities | Browse tabs, feed presets, source details/preferences, Entry/child WebView/share, related-entry dialog | Purpose-specific Features expose structured source-context results; application and reader consumers do not cast the contracts | Public source contracts remain authoritative external facts rather than content-type capabilities. |
| Local/stub restriction | Entry download, bulk-download, WebView, and source-backed actions | Controls are removed even when type support exists | Contextual negative gate applied directly by consumers. |
| Tracker supported types | Entry tracking action/dialog, library tracking filters, track auto-add/sync | Tracker search is filtered by entry type | Tracker integrations advertise support independently; current built-in/legacy integrations resolve to Manga. |
| Platform/player/viewer capabilities | Anime picture-in-picture and Manga auto-scroll controls | Preference plus runtime/device/renderer support | Remain internal contextual predicates, not content-type catalog facts. |
| Library-update notification projection | New-update summary and child notifications | F24 routes/groups and action sets; F23 vocabulary; F01 child destination; F09/F04 actions | Book receives explicit Item vocabulary and a derived route. Missing relationships omit only the affected action and preserve notification participation. |
| Backup type gates | Backup create and restore | No direct capability UI | Playback-preference dispatch is provider-derived; remaining branches cover legacy/wire conversions, progress migration, and download preferences under their own owners. |

## Coverage Graph

Coverage is classified as **shared contract**, **type-focused**, **synthetic registry**, **consumer-focused**, or **missing/weak**. A test that uses a hand-written fake does not prove that every real type claiming the capability satisfies the same contract.

| Capability area | Existing coverage | Coverage conclusion |
| --------------- | ----------------- | ------------------- |
| Registry dispatch and absence | `EntryInteractionRegistryTest` covers missing-provider behavior, duplicate registration, type dispatch, mismatched types, and synthetic support results for most processor categories | Strong registry mechanics; it does not enumerate real plugins or prove cross-feature completeness. Update eligibility has no comparable registry-focused cases. |
| Type plugin composition | Manga, Anime, and Book plugin tests exercise operational behavior and selected registrations | Capability-label assertions were removed during the architecture reset because they repeated provider declarations. Mandatory construction and generic discovery remain Phase 3 and Phase 4 work. |
| Open and continue | Focused processor, notification, and real-type behavior tests | Provider registration is current evidence. All current types provide both, but that fact creates no architectural requirement for a future partial type. |
| Consumption and bookmarks | Registry bookmark-provider dispatch and validation, media behavior tests, Updates selection consumer test, and the vertical behavioral proof | Positive provider and shared behavior paths are covered. Comprehensive graph-selected behavioral contracts remain Phase 7 work. |
| Download core and queue | Type download manager/provider/store/downloader tests; registry dispatch; shared download job/runtime/notification tests | Operational behavior is covered unevenly. Provider-backed graph participation and comprehensive contracts remain Phase 3 through Phase 7 work. |
| Bulk downloads | Shared registry selection tests; Manga and Anime behavior tests; Book candidate-pool tests | Bookmarked selection has positive Manga, negative Anime, and synthetic Anime provider coverage. Broader graph-selected contracts remain Phase 7 work. |
| Automatic downloads | Anime and Book focused tests; `LibraryUpdateJobInteractionBoundaryTest` proves routing through the interaction | Manga automatic filtering lacks focused coverage; boundary test proves architecture, not behavior for every type. |
| Mark-consumed cleanup | `EntryDownloadLifecycleManagerTest` iterates `EntryType.entries` and proves bookmark protection/category policy; Manga, Anime, and Book consumption tests prove lifecycle event emission; reader/player tests cover completion events | This is the closest current example of a shared cross-type contract. It is driven by enum values rather than declared capability providers and does not discover applicability from capability declarations. |
| Download settings/options | Registry test preserves setting ownership; Anime plugin tests option resolution/persistence; settings dialog tests cover presentation mechanics | No contract connects declared setting capabilities to every settings surface. Manga-specific settings lookup is not challenged by tests. |
| Migration and merge | Synthetic registry selection tests, Book negative plugin assertions, migration use-case tests, and entry/library merge flow tests | Real Manga/Anime positive capability declarations are not asserted by their plugin tests. Documentation disagreement for Anime is therefore not caught. |
| Update eligibility and release-period filtering | F13 and F14 synthetic graph-selected behavior contracts | Update eligibility is unconditional shared policy. F14 independently proves generic filtering, provider-derived release applicability, valid absence, and mixed-type behavior. |
| Progress and child labels | F15 progress-transfer and F17 independent-provider contracts; Manga/Anime progress and label behavior; Book progress snapshot/reader locator tests; generic entry-row label test | F15 distinguishes empty progress from absence. F17 proves Child List-only, Child Progress-only, combined, and absence semantics without a production support matrix; Book media label behavior remains weakly covered. |
| Playback preferences and backup | Synthetic graph-selected feature contract plus Anime provider behavior | The shared contract proves provider absence and automatic activation without restating current production support. |
| Child-list and child-group behavior | F17 and F18 graph-selected feature tests plus genuine Manga/Anime media tests | Child List and Child Group Filtering are independently provider-selected. F18 proves absence, supported-empty state, filtering, multi-member persistence, backup, and strict dispatch without a production matrix. |
| Preview | F19 synthetic graph-selected contract; Manga preference/loader behavior; Anime source-dependent load behavior; generic layout and fallback tests | Provider absence, fixed and contributed configuration, child-backed validation, contextual source support, Open composition, and shared consumers are feature-owned without a production matrix. |
| Immersive | Synthetic graph-selected F20 contract; Manga processor behavior; Anime renderer/preferences; feed-mode, browse-action, screen-model, and shared content mechanics | Provider absence, source blockers, child-backed validation, entry-level/zero-preload providers, metadata pruning, Open composition, media and renderer failure, and strict lifecycle are feature-owned without a production support matrix. Anime load/media resolution still lacks a focused processor contract. |
| Related entries | F21 synthetic graph-selected and external-source behavior contract; source API and legacy adapter compatibility tests; screen-model lifecycle tests | Every composed type receives common consequences without a provider. Source absence/support, mixed authoritative types, order/dedup, persistence, orientation, live Library state, and retryable source failures are covered without a production type matrix. |
| Local source | Direct Manga validation in `LocalSource` and metadata conversion | Public reference claim has no focused Local-source content-type test. |
| Legacy extension compatibility | `LegacyMangaSourceAdapterTest` verifies Manga metadata and returned entry types | Strong direct evidence for the documented Manga-only compatibility claim. |
| Tracking | Tracker profile/integration tests and tracking-sync tests assert Manga supported types | Current Manga-only claim is covered, but support remains opt-in per tracker rather than derived from a central content capability. |
| Presentation and direct gates | `EntryTypePresentationTest`, selection-action tests, browse action tests | Tests frequently assert current type matrices directly, so adding a fundamental capability does not automatically update or fail every dependent test. Book library-update notification fallback has no focused test. |
| Boundary enforcement | `checkEntryInteractionBoundaries` and its task tests prevent cross-module processor/runtime imports, legacy APIs, selected direct type mappings, and concrete download/media access | Enforces architectural isolation only. Raw source-action access is rejected in app/data/domain and generic presentation modules; reviewed source compatibility, composition, and type-owned mechanics retain narrow ownership. The checker does not itself prove feature completeness. |

## Content Type Reference Traceability

This table maps every claim in `docs/features/content-type-reference.md` to current evidence and coverage. **Matched** means documentation agrees with the observed result; it does not mean the architecture will keep it aligned automatically.

| Reference claim | Executable evidence | Coverage | Result |
| --------------- | ------------------- | -------- | ------ |
| Continue from saved progress: all | Continue processors for Manga, Anime, and Book | Focused tests for all three plus the production composition contract | Matched and enforced for every current `EntryType`. |
| Mark individual children consumed/unconsumed: all | Consumption processors for all three | Focused tests for all three plus registry/consumer tests | Matched. |
| Show partial child progress: all | Independent Child Progress providers combine with Child List through F17 | F17 synthetic relationship; Manga/Anime focused; generic consumer; Book label path weak | Matched and relationship-derived, with a Book media coverage gap. |
| Apply smart library-update restrictions: all | Three registered update-eligibility processors with equivalent policy | No focused real-type tests | Matched by inspection; weakly enforced. |
| Merge versions: Manga yes, Anime yes, Book no | Manga/Anime capability processors true; Book default false | Synthetic selection tests and Book negative only | Matched to executable behavior. |
| Bookmark children: Manga only | Bookmark provider evidence in the production capability report | Production composition, real-type plugin coverage, Updates policy, and the synthetic vertical contract | Matched and capability-derived. |
| Show missing-child gaps: Manga only | Manga Child List display result supplies inline rows and aggregate count; Anime/Book return zero | Manga media behavior plus F17 synthetic display contract | Matched and feature-selected; presentation owns only vocabulary. |
| Filter by release group: Manga only | Manga child-group processor true; Anime registered false/no-op; Book absent | Synthetic registry tests; real type matrix weak | Matched by inspection. |
| Migrate entry: Manga yes, Anime/Book no | Manga true, Anime true, Book false | Book negative/use-case tests; real Manga/Anime declarations not asserted | **Conflict: Anime executable support is true.** |
| Download individual children: all | Download-provider registration for Manga, Anime, and production Book | Production matrix plus media-specific download tests | Matched and capability-derived. |
| Bulk download children: all | The same providers contribute Bulk Downloads and supply candidate pools | Production matrix; real-type plugin and candidate-pool tests | Matched and capability-derived. |
| Automatically download new children: all | All three implement automatic candidate filtering; library worker dispatches generically | Anime/Book focused; Manga gap; boundary routing test | Matched with uneven coverage. |
| Delete downloads after marking consumed: all | Shared lifecycle plus per-type event emission | Shared lifecycle iterates every enum type; Manga/Anime/Book event tests | Matched and mostly shared. |
| Bookmark-based bulk downloads follow bookmark support | Manga currently has both; Anime/Book have Bulk Downloads but not Bookmarking and reject the action | Shared policy derives Bulk Downloads + Bookmarking for runtime and application availability | Matched; synthetic Anime Bookmarking activates candidate selection and presentation without Anime downloader or presentation changes. |
| Preview: Manga yes, Anime source-dependent, Book no | Manga preview provider; Anime requires `EntryPreviewSource`; Book absent | Focused Manga/Anime and registry tests | Matched. |
| Immersive: Manga/Anime source-dependent, Book no | Source immersive opt-in plus Manga/Anime processors; Book absent | Manga processor, Anime renderer, and shared UI tests | Matched at product level; Anime load coverage is incomplete. |
| Bundled Local source: Manga only | Local source requires and produces Manga entries | No focused content-type contract test | Matched by inspection; weak coverage. |
| Legacy Mihon extensions: Manga only | Legacy adapter advertises and produces Manga | Focused adapter test | Matched. |
| Tracking services: Manga only | Current trackers advertise Manga; consumers filter by tracker support | Tracker integration and sync tests | Matched for current integrations. |

## Duplicated Facts and Independently Maintained Obligations

| Concept | Independent representations that must currently agree |
| ------- | ----------------------------------------------------- |
| Bookmarked downloads | Bookmark provider registration feeds the report; shared download policy derives candidate applicability, dropdown visibility, and lifecycle protection; documentation remains a projection. |
| Download configuration | Per-downloader setting-capability set; settings screen Manga-only lookup; individual settings visibility; downloader preference consumption. |
| Migration/merge | Capability processor booleans; selection rules; entry/library action visibility; migration use-case guards; public reference. |
| Progress | Progress transfer provider and F15 Feature; independent F17 Child Progress relationship; separate library progress calculators; reader/player persistence; backup/migration consumers; public reference. |
| Playback preferences | Optional provider registration; structured Feature results; provider-derived backup and migration transfer; stable optional backup schema field. |
| Smart updates | Three equivalent update-eligibility processors; separate library-filter support methods; settings visibility; worker policy; documentation claim. |
| Preview | Processor registration; source interface for Anime; preference/config state; entry and browse availability; long-press fallback; documentation. |
| Immersive | F20 Feature integration; source opt-in; type renderer and load mode; load-time media requirements; feed/catalog modes; long-press action; provider-derived preload; strict lifecycle; behavior contract; documentation. |
| Tracking | Per-tracker supported types; entry action; library filters; sync/search guards; documentation. |
| Type presentation | F23 discovers type-owned vocabulary, icons, plurals, and formatting policy through ordinary plugin contributions. The application consumes one Feature; presentation has no behavioral authority. F24 consumes its notification vocabulary without owning a type map. |
| Type composition | Interaction plugins now own optional Library Progress evidence; tracker/source metadata, notification semantics, presentation, and remaining boundary migrations retain their assigned owners. |

## Implemented Capability Combinations

| Combination | Manga | Anime | Book | Current enforcement |
| ----------- | ----- | ----- | ---- | ------------------- |
| Bulk downloads + bookmarks: bulk bookmarked items | Derived and implemented | Not applicable today, shared policy returns unsupported and hides the action | Not applicable today, shared policy returns unsupported and hides the action | Shared policy composes Bulk Downloads + Bookmarking for runtime and UI; synthetic Anime support activates both. |
| Downloads + bookmarks: cleanup protection | Derived and implemented | Activates automatically with synthetic Bookmarking support | Activates automatically with synthetic Bookmarking support | Shared lifecycle consults Bookmarking evidence and preference policy. |
| Downloads + consumption: delete-after-consumed | Implemented | Implemented | Implemented | Shared lifecycle policy plus type event emission. |
| Downloads + library updates: automatic download | Implemented | Implemented | Implemented | Shared worker orchestration, type-specific candidate filters. |
| Downloads + merged entries | Implemented and tested | Implemented and tested | Implemented and tested despite merge capability false | Type downloaders independently implement owner-aware behavior. |
| Merge + continue/child-list/progress | Implemented | Implemented | Much supporting code is merge-aware despite merge capability false | Spread across type processors and separate library calculators. |
| Progress + backup/migration | Implemented | Implemented | Implemented | Shared progress interaction. |
| Playback preferences + backup/migration | Not applicable | Implemented | Not applicable | Both consumers use the provider-derived Feature boundary; the current support matrix is descriptive only. |
| Preview + source capability | Manga uses its image/media path | Requires `EntryPreviewSource` | No provider | Type-specific preview processors. |
| Immersive + source/media capability | Source opt-in plus image pages | Source opt-in plus playable stream | No renderer | Source, UI, and type processor must all agree. |
| Tracking + entry type | Current trackers support Manga | No current tracker support | No current tracker support | Per-tracker declarations and consumer filters. |

## Coverage and Documentation Gaps

- The Phase 1 executable catalog and report enumerate reviewed fundamental capability facts, but consumer selection, shared contracts, and documentation projection have not migrated yet.
- No shared contract yet instantiates every real content-type provider claiming migration or merge support. F01–F10 and
  F13–F26 use graph-selected feature contracts rather than support-label matrices.
- `EntryType.entries` is used by one strong shared lifecycle test, but enum membership is not the same as capability applicability.
- The content reference is entirely hand-maintained. Anime migration already demonstrates that executable support and documentation can disagree without validation failing.
- The bookmark-based bulk-download rule drives shared runtime and application presentation; public documentation verification remains assigned to the Phase 2 integration gate.
- Book's update notification fallback, Book partial-progress label path, Book library progress calculator, Manga bulk/automatic-download policies, real update-eligibility matrices, and Anime immersive loading have missing or weak focused coverage.
- `checkEntryInteractionBoundaries` protects module ownership but cannot detect forgotten UI, policy, worker, backup, notification, test, or documentation integrations.
- Several tests preserve manual type matrices or direct type gates. They can remain green when a type gains a capability because the test inputs do not discover that declaration.
- Executable features not represented as rows in the content reference include download options/settings, playback-preference transfer, outside-release-period library filtering, related entries, source WebView/home/settings/latest capabilities, and platform/renderer features. Some are intentionally contextual or media-specific, but there is no executable classification that explains the omission.

## Evidence Conclusions for Review

- Provider registration is authoritative only for the capability it contributes. Contextual source, preference, entry,
  media, and runtime conditions remain feature inputs rather than being flattened into provider absence or duplicate
  support methods.
- The same absence is represented by missing providers, default-false methods, explicit `Unsupported` values, empty results, no-ops, presentation booleans, and direct type checks.
- Some facts that look type-wide are contextual. Anime preview depends on the source, immersive loading depends on resolvable media, tracking depends on the tracker, and selection actions depend on the whole selection.
- Downloading bookmarked children and exposing its menu action are both derived by shared download policy; presentation owns terminology only.
- Presentation vocabulary is now an independently contributed projection that cannot authorize executable behavior;
  documentation projection remains later work.
- Cross-feature routing can omit a type even when the underlying workflow is shared: Book update notifications currently fall through to Manga-specific notification semantics.
- Compatibility, media format, storage, and vocabulary branches must remain visible but outside the capability catalog unless they independently gate a user-facing feature.

These inventory findings are interpreted by the accepted Milestone 0.3 architecture decisions below.

## Accepted Expected-State Classification

These classifications apply the accepted decision records and guide the later implementation phases.

| Area | Expected classification and authority | Later phase |
| ---- | ------------------------------------- | ----------- |
| Open, continue, downloads, bulk download, consumption, progress transfer, playback preferences, merge, and migration | Provider-backed fundamental behavior. Operational registration is evidence; constant support declarations must not duplicate it. Sub-capabilities that require distinct behavior need distinct evidence. | 1 and 3 |
| Bookmarking | Provider-backed fundamental behavior because mutation/persistence is real type work. Current outcome is Manga supported; Anime and Book intentionally unsupported. | 1–3 |
| Update eligibility | Shared universal Entry policy for the current common restrictions, with specialized providers only if a real type difference appears. Three equivalent processors are duplication, not three independent capability decisions. | 3 |
| Library progress summary and child-list construction | Provider-backed behavior. Their providers must contribute to the same inspected type composition instead of remaining separate invisible type lists. | 1 and 3 |
| Child-group filtering | Provider-backed when operational. Manga supported; Anime and Book are validly unsupported by provider absence. F18 derives all shared consequences without explicit absence declarations. | 3 and 5 |
| Outside-release-period library filtering | Type-wide feature-policy support with one authoritative evidence source. Preserve Manga/Book supported and Anime unsupported until a separate product decision changes it. | 3 |
| Download setting capabilities | Fundamental media-transfer facts only where a setting corresponds to real specialized behavior. Settings visibility is derived by the settings feature. | 3 and 5 |
| Download options | Entry/source/selection-dependent composition over a download provider and resolvable media. Anime currently supplies the specialized option resolver. | 4 |
| Bookmark actions, bookmarked downloads, and bookmark cleanup protection | Derived from Bookmarking plus the surrounding feature capability. Shared feature policy owns the consequence; missing specialized work is an obligation. | 2 |
| Automatic downloads and consumption cleanup | Shared feature policy plus a downloader/consumption provider. Media-specific candidate or storage behavior remains specialized; applicability is not another type opt-in. | 3, 5, and 6 |
| Preview | Contextual composition of preview provider, source/media evidence, entry state, and preference. Enabled state is not fundamental support. | 4 and 5 |
| Immersive browsing | Contextual composition of source opt-in, type renderer, entry media, and platform/runtime state. | 4 and 5 |
| Related entries, latest feeds, source home/settings/WebView, and source supported types | External source-owned inputs. They are composed by features and are not duplicated as type capabilities. | 4 |
| Tracking | External tracker-owned type support composed with the entry and tracker state. | 4 and 5 |
| Local/stub, selection, preference, platform, and renderer conditions | Contextual blockers or enablers reported by the owning query, never negative type capabilities. | 4 |
| Missing-child gaps | Derived presentation/child-list behavior. Manga-only current behavior is preserved, but availability must not remain a direct presentation type gate. | 5 |
| Library-update notification semantics and actions | Derived from update participation, open/consumption/download capabilities, and presentation vocabulary. Book's Manga fallback is a missing integration. | 5 and 6 |
| Labels, icons, nouns, and history/progress wording | Presentation-only. They may vary by type but cannot establish behavioral support. | 3, 5, and 8 |
| Backup wire defaults, legacy conversions, media formats, reader/player internals | Compatibility or media boundaries. Current capability consumers inside backup/migration must query authoritative evidence, while genuine format branches remain local. | 3, 4, and 8 |

## Accepted Decision Records

- `decisions/0001-capability-evidence-and-authority.md`
- `decisions/0002-derived-behavior-and-obligations.md`
- `decisions/0003-support-result-semantics.md`
- `decisions/0004-contextual-and-external-ownership.md`
- `decisions/0005-current-product-outcomes.md`
- `decisions/0006-architecture-before-migration.md`
- `decisions/0007-contribution-semantics.md`
- `decisions/0008-contribution-discovery-and-assembly.md`

## Discrepancies

Observed disagreements are recorded without changing implementation or public documentation.

| Capability | Conflicting statements | Expected behavior | Resolution phase | Status |
| ---------- | ---------------------- | ----------------- | ---------------- | ------ |
| Anime migration | `AnimeCapabilityProcessor` returns true; `docs/features/content-type-reference.md` describes migration as unavailable | Anime migration remains supported; documentation is stale | Provider/feature migration in Phases 4–5; docs projection/correction in Phase 7 | Accepted: documentation drift |
| Book downloads as a registered capability | Production runtime enables the Book download plugin, but the plugin defaults `downloadsEnabled` to false and can be assembled without its provider | Production Book downloads are supported; the flag is a construction/testing seam | Registration evidence prototype in Phase 1; composition in Phase 4; contracts in Phase 7 | Accepted: non-product ambiguity |
| Anime child-group filtering | Anime formerly registered a false/empty/no-op processor while Book expressed absence by not registering | Anime remains intentionally unsupported; provider absence is the sole representation | Provider cleanup in Phase 4 and F18 feature migration in Phase 5 | Resolved: no-op provider removed and Feature returns structured inapplicability |
| Book library-update notification semantics | **Resolved in F24:** the shared Feature projection discovers Book and composes its F23 Item vocabulary | Book receives shared behavior with explicit neutral/Book semantics, never Manga fallback | F24 behavior contract covers derived routing and relationship absence; Phase 7 runs it across applicable production types | Accepted: implementation bug corrected |

## Phase 0 Completion Summary

Milestones 0.1 and 0.2 establish the evidence inventory and dependency graph. Milestone 0.3 accepts:

- evidence authority based on provider registration, exceptional intrinsic declarations, and feature-owned derivation;
- distinct supported, intentional absence, not-applicable, contextual, missing-obligation, and unresolved semantics;
- preservation of public source/tracker ownership;
- reviewed classifications for every inventoried capability area; and
- expected outcomes and later phases for every discrepancy.

Phase 0 is complete. Its accepted decisions are the preconditions for Phase 1, which must be split into reviewable implementation milestones before runtime work begins.

## Phase 1 Foundation Progress

Milestone 1.1 introduced the capability identity, query scope and subject distinction, structured evidence, six support outcomes, and construction invariants. It does not enumerate current type support or serve production consumers.

Milestone 1.2 connects evidence collection to Entry interaction composition:

- open, continue, downloads, consumption, progress, playback preferences, and child-list processor registration contribute type-wide provider evidence;
- preview and immersive processor registration contribute contextual provider evidence, never unconditional type support;
- intrinsic declarations are restricted to stable type-wide facts with an owner and reason;
- duplicate authority, provider/intrinsic conflicts, and conflicting scopes fail composition;
- capability processors, update-eligibility processors, child-group processors, and library-filter processors do not contribute support merely by registration because their current registration does not prove their sub-capabilities; and
- empty or absent evidence remains valid input for the unresolved reporting work in Milestone 1.3 rather than being interpreted as intentional unsupported behavior.

The existing `createEntryInteractions` API and runtime dispatch remain unchanged. The composed evidence snapshot is an inspection foundation; production feature consumers have not migrated yet.

Milestone 1.3 adds the reviewed fundamental catalog and deterministic type reports. The catalog includes provider-backed core interactions, distinct download-setting facts, contextual download options, bookmarking, library progress, filtering, merge/migration, preview, and immersive capability. Universal policy and derived combinations remain outside it.

Current report interpretation is deliberately stricter than current fallback behavior:

- provider evidence produces supported type-wide results;
- preview and immersive provider evidence produces conditional results;
- Manga bookmark and download-setting support follows positive processor evidence;
- Anime and Book bookmark absence and Anime child-group-filter absence are explicit owned decisions;
- missing catalog evidence produces an unresolved result, never implicit unsupported; and
- merge, migration, library progress, download options, and filter facts remain unresolved where their current APIs do not yet provide authoritative composition evidence.

Manga, Anime, and Book plugin tests assert their current report projections. Book downloads report supported whenever the production download provider is present; omitting that provider in the existing construction/testing seam leaves the fact unresolved instead of misclassifying Book downloads as intentionally unsupported.

The report is deterministic across plugin registration order and rejects evidence outside the reviewed catalog or any positive-evidence/explicit-absence conflict.

Milestone 1.4 exposes that same immutable report through `EntryInteractions` and registers it as the production
`EntryCapabilityReport`. The report is not reconstructed by DI and no independent content-type matrix was added. Existing
interaction properties, compatibility support methods, and dispatch paths remain unchanged.

A production-composition test resolves the actual runtime Manga, Anime, and Book plugin builders with operational
dependencies replaced by test doubles. It proves that every production type is inspectable and that all three production
download registrations—including Book's enabled construction path—produce supported Downloads and Bulk Downloads
evidence.

### Implemented evidence owners

- The Entry interaction registry owns provider-registration evidence and rejects duplicate or contradictory authority.
- Type plugins own exceptional intrinsic facts and explicit intentional-absence decisions.
- The reviewed catalog owns stable capability identity and scope, not per-type support values.
- The deterministic report owns unresolved migration visibility and never converts missing evidence into unsupported.
- The production `EntryInteractions` composition owns access to the resulting report; DI only exposes the same instance.

### Remaining migration work

- Merge, migration, library progress, download options, and filter facts remain unresolved in the report prototype until
  their owners are migrated through Phases 4–6.
- Unresolved values do not satisfy the exit gate of the later phase that owns the capability. Phase 1 records those gaps;
  it does not classify them as completed or intentional absence.
- Application actions, settings, policies, workers, presentation, shared contracts, and public documentation outside the
  completed Bookmarking and Downloads slice still use compatibility paths until their assigned phases migrate them.
- Derived bookmark/download behavior is deliberately absent from the fundamental catalog and is the Phase 2 vertical
  proof.

## Phase 2 Vertical Proof Progress

Milestone 2.1 separates Bookmarking from Consumption at the operational boundary. `EntryBookmarkProcessor` registration
is the only positive provider-backed Bookmarking fact:

- Manga registers its bookmark persistence implementation and automatically contributes Bookmarking evidence.
- Anime and Book register no bookmark provider, implement no unsupported bookmark no-op, and retain explicit owned
  intentional-absence outcomes.
- The existing consumption compatibility facade derives bookmark support, eligibility, and dispatch from the bookmark
  provider map.
- Consumption registration alone cannot accidentally advertise Bookmarking.
- Duplicate bookmark providers and a provider combined with an explicit absence fail composition.

At the end of Milestone 2.1, derived download selection, cleanup, application actions, and presentation still exposed the
duplication recorded above. Milestone 2.2 then moved the shared bookmark/download policy.

Milestone 2.2 moves bookmark/download implications into the download feature:

- media-specific download processors provide candidate pools but do not interpret bulk action types;
- the shared registry derives bookmarked applicability from Bulk Downloads + Bookmarking and owns generic selection;
- Manga, Anime, and Book contain no bookmark-specific downloader branches;
- synthetic Anime Bookmarking support activates shared bookmarked selection without Anime downloader changes;
- lifecycle cleanup uses the same Bookmarking report to decide whether bookmark protection applies; and
- the existing remove-bookmarked preference retains its override semantics.

No specialized bookmark/downloader adapter is required by the current shared models, so no missing specialized obligation
exists for this combination.

Milestone 2.3 removes the remaining application and presentation duplication:

- Entry and Updates bookmark actions read Bookmarking support from the composed report;
- Entry and Library bookmarked-download actions use the shared Bulk Downloads + Bookmarking policy;
- `EntryTypePresentation` no longer stores behavioral support for bookmarked downloads;
- type-specific labels, icons, and plurals remain presentation metadata; and
- synthetic Anime evidence activates the shared Updates and download-menu policies without Anime-specific UI changes.

Milestone 2.4 closes the vertical proof:

- one synthetic Anime bookmark-provider registration activates report support, mutation dispatch, application policy,
  bookmarked candidate selection, cleanup protection, and the capability-selected vertical contract;
- production composition proves Bookmarking and bookmarked-download applicability remain Manga-only;
- bookmark eligibility and mutation use the distinct `EntryBookmarkInteraction`, removing the temporary consumption
  compatibility facade; and
- the content-type reference describes bookmarked bulk downloads and bookmark-aware cleanup as automatic consequences
  of individual bookmark support.

## Architecture Reset After the Initial Phase 3.1 Migration

Milestone 3.1 makes download-provider registration the single positive authority for both Downloads and Bulk Downloads:

- every registered download provider already supplies its media-specific bulk candidate pool, so registration proves
  both facts without a second constant support declaration;
- Entry and Updates individual actions query Downloads from the composed report;
- Entry and Library bulk actions plus library-update notifications query Bulk Downloads from the same report while
  retaining their contextual selection, local-source, and queue-size constraints;
- the download registry checks Bulk Downloads before applying its shared candidate policy;
- bookmarked bulk availability now composes the precise Bulk Downloads + Bookmarking facts; and
- superseded download-support methods and per-downloader bulk-support constants have been removed.

Production Manga, Anime, and Book currently register download providers. A composition without a provider leaves the
prototype report unresolved rather than creating an implicit absence. Product behavior and public documentation did not
change in the committed download migration.

The proposed next milestone attempted to enforce Open and Continue as required and Bookmarking, Downloads, and Bulk
Downloads as optional through a hardcoded production completion contract. That design was rejected before commit and has
been removed. It named only capabilities remembered so far, required future capabilities to be added manually, and was
paired with real-type tests that repeated provider registration as support truth.

The architecture review also exposed a sequencing error: the plan had deferred general contribution discovery, feature
relationships, obligations, and contract selection until after capability-by-capability consumer migration. That made
local completion lists and feature-specific policies likely even when each individual step appeared consistent.

The corrected Phase 3 therefore migrates no further production capability. It first builds the generic architecture for
owned contributions, discovery, graph evaluation, shared consequences, specialized obligations, contracts, and
projections. Existing evidence reports and the Bookmarking/Downloads policy remain useful prototypes and behavioral
input, but they are not protected as the final architecture.

`legacy-artifacts.md` classifies committed results explicitly. Provider splits, shared operational behavior, removed
support booleans, and behavioral tests are retained as migration input. The central catalog, type report,
`supportsTypeWide`, explicit absence compensation, report assembly, and production report exposure are retired as
authority at the Phase 3.5 dependency cut. Report-driven feature policies are rehomed through feature contributions.

Capability-label assertions removed during this reset are not replaced. Support comes from provider presence, while
completeness applies only to feature relationships whose prerequisites are satisfied. Generic discovery, obligation
failures, and graph-selected behavioral contracts enforce those consequences. A partial type remains valid and absent
providers create no obligation. Compilation may temporarily fail when the new dependency boundary exposes unported
production code; those failures become explicit migration obligations for later phases.

## Phase 3.1 Generic Contribution Semantics

The new standalone `feature-graph` module establishes the target model without adapting current product types:

- a content type has stable identity and may contribute zero or more typed capability-provider implementations;
- a missing provider means unsupported and requires no explicit absence declaration;
- capability definitions are owned beside their provider contracts rather than listed in a central catalog;
- a feature owns integrations containing positive capability prerequisites, contextual inputs, shared executable
  consequences, specialized adapter requirements, behavioral contracts, and projections;
- specialized requirements are separate from prerequisites so missing adapters can become obligations only after the
  feature applies; and
- content types never enumerate consuming features except when they supply genuinely specialized adapters defined by
  those features.

Construction currently validates stable identities, local duplicates, and specialized-requirement ownership. Discovery,
cross-contribution validation, graph assembly, applicability evaluation, and obligation materialization remain explicitly
outside Milestone 3.1. Anonymous alpha/beta tests prove the semantics without using existing product types or capability
names.

## Phase 3.2 Generic Discovery and Assembly

The kernel now accepts owner-scoped contributors through one generic sink. Contributors may supply any number of owned
content-type or feature contributions; the sink rejects attempts to submit another owner's top-level contribution. The
environment installs contributor modules, while the graph kernel remains unaware of their concrete identities.

`FeatureGraph` assembly is deterministic across contributor order and coalesces identical distributed capability,
context-input, and specialized-adapter definitions. It rejects duplicate content-type or feature identities,
contradictory definitions, capability providers not consumed by any feature integration, specialized adapters no feature
requires, and integrations that contribute no consequence, requirement, contract, or projection.

Synthetic tests expand an existing discovery pipeline with new types, providers, and feature contributions without
changing discovery or assembly code. Feature relationships may exist before their first supporting type. Assembly does
not yet determine which relationships apply to which types and does not materialize obligations or select artifacts;
that remains Phase 3.3 and Phase 3.4 work.

## Phase 3.3 Generic Evaluation and Obligations

The assembled graph is now evaluated across every discovered content type and feature integration without a product
matrix or feature-specific evaluator branch. `Always`, `Provided`, `AllOf`, and `AnyOf` prerequisites use the actual
provider contributions. Missing prerequisites are ordinary inapplicability and create no obligation.

Satisfied prerequisites produce separate conditional, incomplete, and applicable states. Context-bearing integrations
remain conditional until Phase 6 supplies their real source, entry, selection, preference, platform, or external inputs.
Missing specialized adapters become actionable obligations only for statically applicable integrations and identify the
affected content type, its responsible owner, the feature relationship, and the feature-owned requirement.

Applicable integrations produce per-type edges to their shared consequences while retaining the exact feature-owned
consequence object. This lets a notification manager or another single-gate coordinator remain one shared runtime object
rather than being instantiated per type. The graph describes applicability; it does not become an event-routing layer.

Contract and projection selection remains absent. Milestone 3.4 will consume these evaluated relationships rather than
reconstructing capability support independently.

## Phase 3.4 Generic Contract and Projection Selection

The kernel now selects feature-owned behavioral contracts and typed projection implementations directly from complete,
context-free applicable relationships. Selection verifies that its evaluation covers the entire assembled graph; a
caller cannot omit relationships to create a smaller manual contract or documentation matrix.

Shared contracts use matched provider and adapter objects without per-type fixture declarations by default. When a
contract genuinely needs media-specific validation input, it owns a typed fixture requirement and the content type may
supply that fixture. Missing fixtures produce obligations assigned to the affected content-type owner and name every
contract that needs them.

Projection definitions express feature-owned developer or user-facing output requirements. Implementations remain
opaque executable objects to the kernel. A missing shared implementation produces one feature-owned obligation listing
all applicable subjects, while a supplied implementation is selected for each subject without being copied.

Contracts, fixtures, and projection channels are all optional unless an owning feature contribution declares them. The
architecture introduces no universal artifact list. Contract execution, developer-report rendering, documentation
generation, and production validation remain Phase 7 work; Milestone 3.4 proves only generic automatic selection and
owned failure semantics.

## Phase 3.5 Dependency Boundary and Legacy Cut

The provider-contract API now exports the standalone graph kernel, and the SPI composition boundary requires every
entry-type plugin to be an owned graph contributor as well as an operational processor registrar. Independent feature
contributors are supplied separately and joined only at application composition, so feature modules do not become
content-type plugins and type modules do not enumerate consuming features.

Composition now discovers, assembles, evaluates, and selects the graph before exposing its `FeatureGraph`, evaluation,
and artifact-selection results beside operational interactions. There is no default type contribution, empty feature
contributor fallback, or concrete contributor list in the kernel.

The central capability catalog, evidence/report model, report assembly, report DI exposure, `supportsTypeWide`, and the
report-driven download capability policy were deleted. Explicit unsupported outcomes were removed from Anime and Book;
provider absence now stands alone. Tests that existed to validate the retired authority were deleted rather than ported
to another capability matrix.

The lower architecture boundary compiles and the complete synthetic unknown-contribution proof passes. Production SPI
and application compilation intentionally fail where download policy, lifecycle cleanup, screens, actions,
notifications, type plugins, and behavioral tests still consume the old boundary. These failures are owned in
`migration-obligations.md`; they are not reasons to restore a compatibility authority.
