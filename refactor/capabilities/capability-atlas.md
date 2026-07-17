# Capability Atlas

Status: Milestones 0.1 and 0.2 complete; expected-state decisions deferred to Milestone 0.3

This file records the current executable evidence for capability support. It is an inventory of facts to review, not a new source of behavioral truth and not a proposal for the final capability model.

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
- Registry dispatch and missing-provider fallbacks: `entry-interactions/spi/.../EntryInteractionRegistry.kt`
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
| Consumption | Registered | Registered | Registered | Consumed-state behavior exists; bookmark support remains a separate property. |
| Update eligibility | Registered | Registered | Registered | Each type owns its update eligibility policy. |
| Progress | Registered | Registered | Registered | Snapshot, restore, and copy behavior exist for all three types. |
| Playback preferences | Absent | Registered | Absent | Anime alone provides playback preference snapshot, restore, and copy behavior. |
| Child list | Registered | Registered | Registered | Sorting and display-list behavior exist for all three types. |
| Child-group filter | Registered | Registered | Absent | Anime's registered provider explicitly reports unsupported; Book reaches registry fallbacks. |
| Library filter | Registered | Registered | Registered | Provider presence does not prove the outside-release-period sub-capability. |
| Preview | Registered | Registered | Absent | Manga and Anime providers use different support evidence; Book reaches fallbacks. |
| Immersive | Registered | Registered | Absent | Manga and Anime have renderers; successful loading still depends on source/media. |

Provisional owner: registration is owned by each type plugin; processor uniqueness and missing-provider behavior are owned by the shared registry.

## Behavioral Evidence Inventory

| Capability or gate | Provisional scope | Current executable evidence | Provisional behavioral owner | Current outcome and concern |
| ------------------ | ----------------- | --------------------------- | ---------------------------- | --------------------------- |
| Open entry child | Provider-backed | Presence of `EntryOpenProcessor` | Type plugin and processor | Manga, Anime, and Book registered. |
| Continue entry | Provider-backed, entry-dependent | Presence of `EntryContinueProcessor`; result depends on stored children/progress | Type plugin and processor | Manga, Anime, and Book registered. |
| Core downloads | Provider-backed | Presence of `EntryDownloadProcessor`; `supportsDownloads` is processor presence | Type plugin and downloader | Manga and Anime registered; Book is conditional at assembly, though enabled in production. Missing provider produces neutral/no-op behavior. |
| Download settings | Provider-backed/type-wide | `EntryDownloadProcessor.settingCapabilities`, default empty | Downloader plus settings feature | Manga declares archive packaging, tall-image splitting, parallel source transfers, and parallel item transfers. Anime and Book declare none. |
| Download options | Entry- and source-dependent | `supportsDownloadOptions(entry)`, default false; `resolveDownloadOptions`, default null | Downloader | Anime returns true and resolves stream/dub/subtitle/quality choices subject to source/media data. Manga uses the default false. Book explicitly repeats false/null. |
| Bulk download | Provider-backed | Required `supportsBulkDownload(entry)` | Downloader | All three processors return true. The capability interaction separately derives the same answer from downloader presence/behavior. |
| Bulk download: bookmarked children | Derived candidate, currently type-specific | `resolveBulkDownloadCandidates(...BOOKMARKED...)` and `EntryBulkDownloadCandidateResult.Unsupported` | Downloader today; intended owner unresolved | Manga filters bookmarked children. Anime and Book explicitly return `Unsupported`. This intersects downloads with bookmarking and is not proven by bulk-download support alone. |
| Download cleanup | Provider-backed with shared default | `cleanup` defaults to `delete` | Downloader/lifecycle policy | All download providers inherit or specialize behavior; relationship to bookmarks must be mapped in 0.2. |
| Consumed state | Provider-backed | Registered `EntryConsumptionProcessor` and required mutation methods | Type processor | All three types support consumed/unconsumed behavior. Manga has custom partial-progress semantics; Anime and Book use their media-specific persistence/default eligibility. |
| Bookmark children | Type-wide property on provider | Required `EntryConsumptionProcessor.supportsBookmark`; registry guards mutation | Consumption processor | Manga true. Anime and Book false and their `setBookmarked` implementations are no-ops. Provider registration alone therefore does not prove bookmarking. |
| Migration | Entry-dependent method, currently type result | `EntryCapabilityProcessor.supportsMigration(entry)`, default false | Capability processor | Manga true, Anime true, Book inherits false. Documentation currently disagrees for Anime. |
| Merge | Entry- and selection-dependent | `supportsMerge(entry)`, default false; selection also requires at least two entries, same type, and at most one already-merged entry | Capability processor plus shared selection policy | Manga true, Anime true, Book false. Documentation agrees with this result. |
| Update eligibility | Provider-backed, entry/policy-dependent | Presence of `EntryUpdateEligibilityProcessor` | Type processor | All three registered; the result is a policy decision, not a simple support boolean. |
| Progress transfer | Provider-backed | Presence of `EntryProgressProcessor` | Type processor | All three provide snapshot, restore, and copy. |
| Playback preference transfer | Provider-backed | Presence of `EntryPlaybackPreferencesProcessor` | Type processor | Anime registered; Manga and Book have neutral missing-provider behavior. |
| Library progress summary | Provider-backed outside the interaction registry | `entryLibraryProgressCalculators()` explicitly lists one `EntryLibraryProgressCalculator` per type; the resolver requires a calculator | Root composition plus type calculator | Manga, Anime, and Book are registered. This is a second type-provider system whose completeness is maintained independently from interaction plugin registration. |
| Child-list construction | Provider-backed | Presence of `EntryChildListProcessor` | Type processor | All three registered. Default progress labels are empty if not specialized. |
| Child-group filtering | Provider-backed plus explicit sub-capability | `supports(entry)` and `shouldApplyFilter(entry)` | Child-group processor | Manga true. Anime registers an all-unsupported provider returning false/empty/no-op. Book has no provider and receives the same effective fallbacks. Registration is not authoritative support evidence. |
| Outside-release-period library filter | Entry-dependent method, currently type result | `supportsOutsideReleasePeriodFilter(entry)` | Library-filter processor | Manga true, Anime false, Book true. All three register providers, so registration is insufficient. |
| Entry preview | Source-dependent plus preference policy | Preview processor `isSupported(entry)` and configuration state | Preview processor and source capability | Manga processor treats Manga as supported and requires a child; enablement also depends on a preference. Anime additionally requires its source to implement `EntryPreviewSource` and does not require a child. Book has no provider. Supported and enabled are distinct facts. |
| Immersive rendering | Provider-backed, then source/media-dependent | Immersive processor `isSupported(entry)` followed by load-time media resolution | Immersive processor | Manga and Anime report their types supported. Manga loading requires an `EntryImageSource` and image-page media; Anime must resolve a playable stream. Book has no provider. This is distinct from a source opting into immersive catalogue/feed browsing. |

## Defaults, Unsupported Results, and No-Ops

Defaults are executable statements of absence and can hide missing work when treated as harmless implementation details.

| Evidence | Current fallback | Provisional interpretation |
| -------- | ---------------- | -------------------------- |
| Missing download processor | Support queries false, options null, bulk candidates `Unsupported`, auto-download candidates empty, counts/statuses neutral, mutations no-op | Explicit operational absence, but currently spread across registry methods. |
| `settingCapabilities` | Empty set | No type-specific download setting declared. |
| `supportsDownloadOptions` / option resolver | False / null | Download options absent unless the provider opts in. Book redundantly restates the default. |
| Bulk bookmarked candidates | Anime and Book return `Unsupported` | Explicit unsupported combination, but no evidence yet distinguishes intentional product absence from unfinished integration. |
| Missing capability processor or default methods | Migration false; merge false | Absence by registration/default rather than a reviewed reason. |
| Bookmark support false | Registry does not dispatch mutation; Anime and Book mutation methods also no-op | Deliberate-looking absence duplicated by a flag, dispatch guard, and processor no-op. |
| Anime child-group provider | False support/apply, empty flows/sets, setter no-op | An unsupported implementation is registered, making provider presence misleading. |
| Missing child-group provider | False support/apply, empty flows/sets, setter no-op | Effective behavior matches Anime through a different evidence path. |
| Missing playback-preference processor | Null snapshot; restore/copy no-op | Optional provider-backed behavior. |
| Missing preview processor | Unsupported, configuration disabled, and child required by fallback | Multiple behavioral answers are synthesized from provider absence. |
| Missing immersive processor | Unsupported; preload radius zero; loading cannot proceed | Optional provider-backed renderer absence. |
| `EntryChildListProcessor.progressLabels` | Empty map | Optional child-list enrichment rather than proof that progress itself is absent. |
| Download `cleanup` / `renameEntry` | Cleanup delegates to delete; rename defaults no-op | Shared operational defaults whose applicability must be verified rather than inferred as capability support. |

## Presentation and Direct Type Gates

| Gate | Provisional scope | Current evidence and owner | Concern |
| ---- | ----------------- | -------------------------- | ------- |
| `EntryTypePresentation.downloadBookmarkedSupported` | Behavioral fact stored in presentation | Manga true; Anime, Book, and Generic false; used to decide whether the bookmarked bulk-download action is shown | Duplicates the runtime outcome of bookmark and download behavior. Presentation currently decides behavioral availability. |
| `missingChildCount(EntryType, ...)` | Type-wide behavioral gate | Application presentation code calculates gaps only for Manga | A direct `EntryType.MANGA` check owns availability outside the interaction model. Whether this is fundamental, derived, or intentional is unresolved. |
| Download settings capability lookup | Type-wide behavioral/presentation gate | Settings screen reads `settingCapabilities()[EntryType.MANGA]` specifically | The settings surface assumes Manga owns configurable download settings even though the API returns a map by type. |
| Library-update notification type | Type-wide behavioral routing and presentation | `LibraryUpdateNotifier.EntryUpdateNotificationType` defines Manga and Anime; `from(entry)` falls back to Manga for Book or any unlisted type | Book receives Manga notification channel/group IDs and chapter/read terminology instead of an explicit Book result. Whether notification semantics should be derived or specialized is unresolved. |
| Backup snapshot and restore branches | Mixed capability integration and compatibility boundary | Backup creation directly gates playback preferences and Anime download preferences by `EntryType.ANIME`; Manga scanlator/viewer and legacy progress conversions use `EntryType.MANGA` | Some branches are legitimate format compatibility; playback-preference provider use is also a capability consumer. Milestone 0.2 must map them separately rather than treating the whole backup subsystem as one category. |
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
| Related entries | Source-dependent | Optional `RelatedEntriesSource` | Source-provided relationship feature; related entries may have mixed authoritative types. |
| Image-page loading | Source/media-dependent | Optional `EntryImageSource` plus `EntryMedia.ImagePages` | Required by Manga image download/immersive paths; an operational media requirement, not a universal type flag by itself. |
| External subtitles | Source- and selection-dependent | Optional `SubtitleSource` and playback selection | Affects Anime download/playback options. |
| Chapter-list semantics | Source-dependent | `EmptyChapterListSource`, `IncrementalChapterSource`, `ChapterNumberRecognitionSource` markers | Source refresh policy capabilities, orthogonal to content-type feature support. |
| Source preferences, home page, metering, and WebView contracts | External integration | `ConfigurableSource`, `SourceHomePage`, `UnmeteredSource`, and WebView source interfaces | Operational/source UI capabilities that need their own consumers mapped in 0.2; not type-wide claims. |
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
| Open | Entry child rows, history, updates, deep links, browse preview sheet, immersive content | Notification open-child action; debug Anime launcher | `NotificationEntryActionHandler` dispatches through the shared interaction. |
| Continue | Entry continue action, library continue action, history resume action | Type-specific no-next-item vocabulary | Type processors select from stored progress and merged members. |
| Download provider presence | Entry child actions and swipe actions, updates actions, library badges/counts, download queue, More tab | Entry and library selection menus; stats total; download initialization gate in `MainActivity` | Shared download job/runtime/notification manager; backup cache invalidation; source and entry rename hooks; migration deletion; advanced cache maintenance. |
| Download setting capabilities | Download settings screen | Settings visibility for packaging, splitting, and parallelism | Currently queried only for Manga, despite the API returning capabilities by type. |
| Download options | Entry child download action and options dialog | Anime stream/dub/subtitle/quality selection | Anime preference persistence and source/media resolution. |
| Bulk download | Entry and library bulk menus; library-update notification download action | Selection availability excludes local items; entry action excludes local/stub sources | Candidate resolution stays type-specific; `EntryCapabilityInteraction` duplicates the download support query for UI consumers. |
| Bookmarked bulk download | Entry/library download dropdown | `EntryTypePresentation.downloadBookmarkedSupported` independently hides or shows the menu item | Downloader candidate resolution independently supports Manga and rejects Anime/Book. Documentation states a derived relationship that is not executable. |
| Automatic downloads | Library-update worker | No direct UI beyond download/category preferences | Worker asks each downloader to filter new items, queues without starting, then starts the shared runtime once. |
| Consumption | Entry child selection/swipes, Updates selection, library selection | Notification mark-consumed action; tracking sync can mark children consumed | Every type processor reports `MarkedConsumed` to shared download lifecycle policy. |
| Bookmarking | Entry multi-select and swipe action; Updates selection action | Bookmarked library/child filters and bookmarked-download menu presentation | Shared download lifecycle protects bookmarked downloads for every `EntryType`, even though only Manga declares bookmark mutation support. |
| Download cleanup | No independent capability surface | Delete actions in entry, updates, migration, and cache maintenance | Shared lifecycle handles marked-consumed cleanup and completion cleanup; media downloaders own physical deletion/cleanup. |
| Migration | Entry overflow action, library selection, migration configuration/list/search | Migration flags include remove downloads and data transfer | Migration use case copies progress and playback preferences, chapters, categories, tracks, settings, and optional data after capability checks. |
| Merge | Entry add/manage-merge actions and library selection | Merge target/dialog eligibility | Shared selection policy requires same type and valid merge shape; child lists, continue, downloads, progress, and library summaries are merge-aware independently. |
| Update eligibility | Library-update worker | Stats calculations and library restriction settings | Each type processor repeats the same smart-restriction policy. |
| Outside-release-period library filter | Library filtering | Filter availability in library state | Separate from update eligibility even though both use release-period concepts. Manga/Book support it; Anime does not. |
| Progress transfer | Backup create/restore and migration | Entry rows obtain per-child labels through child-list processors | Reader/player/book runtimes persist media-specific progress into the shared model. |
| Playback preference transfer | Backup and migration | No general capability surface | Migration always dispatches through the optional provider; backup creation still gates snapshot by `EntryType.ANIME` before dispatch. |
| Library progress summary | Unified library loading and merged library state | Library progress indicators and continue target | A separate calculator list in root composition must be updated for every new type. |
| Child-list construction | Entry screen sorting, filtering, merged grouping, and preview chapter choice | Immersive screen selects the first reading-order child | Every type has its own processor even where ordering behavior is shared. |
| Child-group filtering | Entry screen state, available/excluded group flows, and settings | Filter controls appear only when support/apply predicates allow them | Manga provider is operational; Anime's registered provider and Book's missing provider converge on no-op behavior. |
| Preview | Entry screen and browse long-press preview sheet | Preview preference, size, page count, and long-press fallback order | Manga loads image preview media; Anime requires `EntryPreviewSource`; Book is absent. Enabled preference and executable support are combined at the consumer. |
| Immersive | Catalogue and feed view-mode entry points; immersive screen model | Browse long-press availability and fallback order | Source opt-in exposes the mode, then type processor/media resolution determines whether an entry loads. Progress persistence returns to the type processor. |
| Source supported types | Source/extension content-type filters and badges | Extension and source detail indicators | Metadata is descriptive; returned entry type remains authoritative. |
| Source latest/home/settings/WebView/related capabilities | Browse tabs, feed presets, source details/preferences, entry WebView/share, related-entry dialog | Availability is derived by interface checks | Owned by source contracts rather than the content-type interaction registry. |
| Local/stub restriction | Entry download, bulk-download, WebView, and source-backed actions | Controls are removed even when type support exists | Contextual negative gate applied directly by consumers. |
| Tracker supported types | Entry tracking action/dialog, library tracking filters, track auto-add/sync | Tracker search is filtered by entry type | Tracker integrations advertise support independently; current built-in/legacy integrations resolve to Manga. |
| Platform/player/viewer capabilities | Anime picture-in-picture and Manga auto-scroll controls | Preference plus runtime/device/renderer support | Remain internal contextual predicates, not content-type catalog facts. |
| Library-update notification type | New-update summary and child notifications | Channel/group IDs, consumed/view labels, descriptions, and download action | Book enters the shared notification workflow but falls back to Manga routing and vocabulary. |
| Backup type gates | Backup create and restore | No direct capability UI | Mixes legitimate legacy/wire conversions with current capability dispatch for progress, playback preferences, and download preferences. |

## Coverage Graph

Coverage is classified as **shared contract**, **type-focused**, **synthetic registry**, **consumer-focused**, or **missing/weak**. A test that uses a hand-written fake does not prove that every real type claiming the capability satisfies the same contract.

| Capability area | Existing coverage | Coverage conclusion |
| --------------- | ----------------- | ------------------- |
| Registry dispatch and absence | `EntryInteractionRegistryTest` covers missing-provider behavior, duplicate registration, type dispatch, mismatched types, and synthetic support results for most processor categories | Strong registry mechanics; it does not enumerate real plugins or prove cross-feature completeness. Update eligibility has no comparable registry-focused cases. |
| Type plugin composition | `MangaEntryInteractionPluginTest`, `AnimeEntryInteractionPluginTest`, and `BookEntryInteractionPluginTest` exercise selected registrations | Uneven. Manga/Anime smoke tests touch continue/download; Book's plugin test intentionally constructs downloads disabled, so production Book composition is not covered as a complete plugin contract. |
| Open and continue | Manga/Anime plugin tests; Book continue and `BookOpenProcessorTest`; notification action tests cover Manga/Anime opening | Type-focused behavior exists, but no shared all-supporting-types contract and no Book notification open-action case. |
| Consumption and bookmarks | Manga, Anime, and Book consumption tests; registry bookmark dispatch; Updates selection consumer test | Consumption is covered per type. Bookmark support is tested positively for Manga and negatively for Book, but Anime's intentional absence is mostly incidental and no shared capability contract drives consumer tests. |
| Download core and queue | Type download manager/provider/store/downloader tests; registry dispatch; shared download job/runtime/notification tests | Strong media implementation and shared runtime coverage, but no single contract discovers all registered download types. |
| Bulk downloads | Anime and Book candidate tests; synthetic registry dispatch | Manga bulk candidate policy and the `BOOKMARKED` action have no focused test. Unsupported Anime/Book bookmarked results are also untested. |
| Automatic downloads | Anime and Book focused tests; `LibraryUpdateJobInteractionBoundaryTest` proves routing through the interaction | Manga automatic filtering lacks focused coverage; boundary test proves architecture, not behavior for every type. |
| Mark-consumed cleanup | `EntryDownloadLifecycleManagerTest` iterates `EntryType.entries` and proves bookmark protection/category policy; Manga, Anime, and Book consumption tests prove lifecycle event emission; reader/player tests cover completion events | This is the closest current example of a shared cross-type contract. It is driven by enum values rather than declared capability providers and does not discover applicability from capability declarations. |
| Download settings/options | Registry test preserves setting ownership; Anime plugin tests option resolution/persistence; settings dialog tests cover presentation mechanics | No contract connects declared setting capabilities to every settings surface. Manga-specific settings lookup is not challenged by tests. |
| Migration and merge | Synthetic registry selection tests, Book negative plugin assertions, migration use-case tests, and entry/library merge flow tests | Real Manga/Anime positive capability declarations are not asserted by their plugin tests. Documentation disagreement for Anime is therefore not caught. |
| Update eligibility and release-period filtering | Synthetic library-filter registry test | Type-specific smart-update policies have no focused tests despite three duplicated implementations. Real Manga/Anime/Book outside-release support is not covered as a capability matrix. |
| Progress and child labels | Manga/Anime progress and label tests; Book progress snapshot/reader locator tests; generic entry-row label test; registry synthetic label dispatch | Book's `BookChildListProcessor.progressLabels` and Book library progress calculator lack focused tests. Shared progress-transfer expectations are not enumerated from declared providers. |
| Playback preferences and backup | Anime plugin snapshot/restore/copy tests; backup creator test explicitly expects Anime-only gating | The test freezes a direct type check instead of proving that backup follows provider support. It would not begin covering a newly registered type automatically. |
| Child-list and child-group behavior | Manga/Anime child-list tests; registry synthetic child-group tests | Book display/label behavior and real Manga/Anime child-group support matrix are weakly covered. Anime's all-no-op registered processor is not established as an intentional product decision. |
| Preview | Manga preference/config test; Anime source-dependent support/load tests; registry dispatch; generic layout tests | Good focused evidence for current Manga/Anime behavior, but no shared contract links provider/source support to all entry and browse consumers. |
| Immersive | Manga processor behavior; Anime renderer/preferences; feed-mode, browse-action, screen-model, and shared content tests | Source opt-in and UI mechanics are covered. Anime load/media resolution lacks a focused processor contract, and tests manually encode Manga/Anime/Book outcomes. |
| Local source | Direct Manga validation in `LocalSource` and metadata conversion | Public reference claim has no focused Local-source content-type test. |
| Legacy extension compatibility | `LegacyMangaSourceAdapterTest` verifies Manga metadata and returned entry types | Strong direct evidence for the documented Manga-only compatibility claim. |
| Tracking | Tracker profile/integration tests and tracking-sync tests assert Manga supported types | Current Manga-only claim is covered, but support remains opt-in per tracker rather than derived from a central content capability. |
| Presentation and direct gates | `EntryTypePresentationTest`, selection-action tests, browse action tests | Tests frequently assert current type matrices directly, so adding a fundamental capability does not automatically update or fail every dependent test. Book library-update notification fallback has no focused test. |
| Boundary enforcement | `checkEntryInteractionBoundaries` and its task tests prevent cross-module processor/runtime imports, legacy APIs, selected direct type mappings, and concrete download/media access | Enforces architectural isolation only. Its broad allowlists include presentation, backup, source, migration, and type modules, so it neither detects duplicated support truth nor proves feature completeness. |

## Content Type Reference Traceability

This table maps every claim in `docs/features/content-type-reference.md` to current evidence and coverage. **Matched** means documentation agrees with the observed result; it does not mean the architecture will keep it aligned automatically.

| Reference claim | Executable evidence | Coverage | Result |
| --------------- | ------------------- | -------- | ------ |
| Continue from saved progress: all | Continue processors for Manga, Anime, and Book | Focused tests for all three | Matched; not a shared enumerated contract. |
| Mark individual children consumed/unconsumed: all | Consumption processors for all three | Focused tests for all three plus registry/consumer tests | Matched. |
| Show partial child progress: all | Child-list progress labels for all three; shared progress state | Manga/Anime focused; generic consumer; Book label path weak | Matched with Book coverage gap. |
| Apply smart library-update restrictions: all | Three registered update-eligibility processors with equivalent policy | No focused real-type tests | Matched by inspection; weakly enforced. |
| Merge versions: Manga yes, Anime yes, Book no | Manga/Anime capability processors true; Book default false | Synthetic selection tests and Book negative only | Matched to executable behavior. |
| Bookmark children: Manga only | Consumption support property | Manga positive, Book negative, Updates matrix; Anime absence weak | Matched. |
| Show missing-child gaps: Manga only | Direct Manga gate in presentation code | `EntryTypePresentationTest` asserts all three outcomes | Matched, but presentation owns behavior. |
| Filter by release group: Manga only | Manga child-group processor true; Anime registered false/no-op; Book absent | Synthetic registry tests; real type matrix weak | Matched by inspection. |
| Migrate entry: Manga yes, Anime/Book no | Manga true, Anime true, Book false | Book negative/use-case tests; real Manga/Anime declarations not asserted | **Conflict: Anime executable support is true.** |
| Download individual children: all | Download processor present for Manga/Anime and production-enabled Book | Media-specific download tests; Book plugin production composition gap | Matched in production. |
| Bulk download children: all | All three download processors return true | Anime/Book focused; Manga candidate coverage weak | Matched. |
| Automatically download new children: all | All three implement automatic candidate filtering; library worker dispatches generically | Anime/Book focused; Manga gap; boundary routing test | Matched with uneven coverage. |
| Delete downloads after marking consumed: all | Shared lifecycle plus per-type event emission | Shared lifecycle iterates every enum type; Manga/Anime/Book event tests | Matched and mostly shared. |
| Bookmark-based bulk downloads follow bookmark support | Manga currently has both; Anime/Book have neither and reject the action | No `BOOKMARKED` candidate tests; presentation has a separate flag | Current result happens to match, but the relationship is neither derived nor enforced. |
| Preview: Manga yes, Anime source-dependent, Book no | Manga preview provider; Anime requires `EntryPreviewSource`; Book absent | Focused Manga/Anime and registry tests | Matched. |
| Immersive: Manga/Anime source-dependent, Book no | Source immersive opt-in plus Manga/Anime processors; Book absent | Manga processor, Anime renderer, and shared UI tests | Matched at product level; Anime load coverage is incomplete. |
| Bundled Local source: Manga only | Local source requires and produces Manga entries | No focused content-type contract test | Matched by inspection; weak coverage. |
| Legacy Mihon extensions: Manga only | Legacy adapter advertises and produces Manga | Focused adapter test | Matched. |
| Tracking services: Manga only | Current trackers advertise Manga; consumers filter by tracker support | Tracker integration and sync tests | Matched for current integrations. |

## Duplicated Facts and Independently Maintained Obligations

| Concept | Independent representations that must currently agree |
| ------- | ----------------------------------------------------- |
| Bookmarked downloads | Bookmark support property; Manga candidate filtering; Anime/Book `Unsupported`; presentation `downloadBookmarkedSupported`; dropdown visibility; documentation callout; lifecycle bookmark protection. |
| Bulk download | Download processor support; capability facade forwarding; entry/library selection predicates; notification action predicate; local/stub exclusions. |
| Download configuration | Per-downloader setting-capability set; settings screen Manga-only lookup; individual settings visibility; downloader preference consumption. |
| Migration/merge | Capability processor booleans; selection rules; entry/library action visibility; migration use-case guards; public reference. |
| Progress | Progress transfer processor; child-list labels; separate library progress calculators; reader/player persistence; backup/migration consumers; public reference. |
| Playback preferences | Optional provider registration; neutral registry fallback; direct Anime backup gate; Anime-only backup schema field; migration dispatch. |
| Smart updates | Three equivalent update-eligibility processors; separate library-filter support methods; settings visibility; worker policy; documentation claim. |
| Preview | Processor registration; source interface for Anime; preference/config state; entry and browse availability; long-press fallback; documentation. |
| Immersive | Source opt-in; type renderer support; load-time media requirements; feed/catalog modes; long-press action; tests with manual type outcomes; documentation. |
| Tracking | Per-tracker supported types; entry action; library filters; sync/search guards; documentation. |
| Type presentation | Central vocabulary/icons plus behavioral `downloadBookmarkedSupported`; notification vocabulary is maintained in a separate two-type enum. |
| Type composition | Interaction plugins, library progress calculator list, tracker/source metadata, notification type list, backup branches, and boundary allowlists each maintain their own view of relevant types. |

## Implemented Capability Combinations

| Combination | Manga | Anime | Book | Current enforcement |
| ----------- | ----- | ----- | ---- | ------------------- |
| Downloads + bookmarks: bulk bookmarked items | Implemented | Not applicable today, explicitly unsupported | Not applicable today, explicitly unsupported | Independent downloader branches and presentation flag; not derived from the two capabilities. |
| Downloads + bookmarks: cleanup protection | Implemented by shared policy | Shared policy would protect pre-existing/bookmarked data | Shared policy would protect pre-existing/bookmarked data | Shared lifecycle iterates all entry types, independent of declared bookmark mutation support. |
| Downloads + consumption: delete-after-consumed | Implemented | Implemented | Implemented | Shared lifecycle policy plus type event emission. |
| Downloads + library updates: automatic download | Implemented | Implemented | Implemented | Shared worker orchestration, type-specific candidate filters. |
| Downloads + merged entries | Implemented and tested | Implemented and tested | Implemented and tested despite merge capability false | Type downloaders independently implement owner-aware behavior. |
| Merge + continue/child-list/progress | Implemented | Implemented | Much supporting code is merge-aware despite merge capability false | Spread across type processors and separate library calculators. |
| Progress + backup/migration | Implemented | Implemented | Implemented | Shared progress interaction. |
| Playback preferences + backup/migration | Not applicable | Implemented | Not applicable | Migration uses provider dispatch; backup uses a direct Anime check. |
| Preview + source capability | Manga uses its image/media path | Requires `EntryPreviewSource` | No provider | Type-specific preview processors. |
| Immersive + source/media capability | Source opt-in plus image pages | Source opt-in plus playable stream | No renderer | Source, UI, and type processor must all agree. |
| Tracking + entry type | Current trackers support Manga | No current tracker support | No current tracker support | Per-tracker declarations and consumer filters. |

## Coverage and Documentation Gaps

- No executable catalog enumerates fundamental capability declarations and automatically supplies the applicable consumer/test/documentation set.
- No shared contract instantiates every real content-type provider claiming open, continue, download, bulk download, consumption, bookmark, migration, merge, update, progress, preview, or immersive support.
- `EntryType.entries` is used by one strong shared lifecycle test, but enum membership is not the same as capability applicability.
- The content reference is entirely hand-maintained. Anime migration already demonstrates that executable support and documentation can disagree without validation failing.
- The bookmark-based bulk-download callout describes a derived rule that the implementation does not encode as a rule.
- Book's update notification fallback, Book partial-progress label path, Book library progress calculator, Manga bulk/automatic-download policies, real update-eligibility matrices, and Anime immersive loading have missing or weak focused coverage.
- `checkEntryInteractionBoundaries` protects module ownership but cannot detect forgotten UI, policy, worker, backup, notification, test, or documentation integrations.
- Several tests preserve manual type matrices or direct type gates. They can remain green when a type gains a capability because the test inputs do not discover that declaration.
- Executable features not represented as rows in the content reference include download options/settings, playback-preference transfer, outside-release-period library filtering, related entries, source WebView/home/settings/latest capabilities, and platform/renderer features. Some are intentionally contextual or media-specific, but there is no executable classification that explains the omission.

## Evidence Conclusions for Review

- Processor registration is useful provider evidence but is not a universal capability declaration. Capability, consumption, child-group, library-filter, preview, and immersive processors all contain meaningful sub-capability or contextual checks.
- The same absence is represented by missing providers, default-false methods, explicit `Unsupported` values, empty results, no-ops, presentation booleans, and direct type checks.
- Some facts that look type-wide are contextual. Anime preview depends on the source, immersive loading depends on resolvable media, tracking depends on the tracker, and selection actions depend on the whole selection.
- At least one likely derived combination, downloading bookmarked children, is independently encoded in downloader behavior and presentation.
- Presentation and documentation already contain support statements independent from executable processor behavior.
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
| Child-group filtering | Provider-backed when operational. Manga supported; Anime intentionally unsupported; Book absence must be explicitly classified rather than inferred from registry fallback. | 3 |
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

## Discrepancies

Observed disagreements are recorded without changing implementation or public documentation.

| Capability | Conflicting statements | Expected behavior | Resolution phase | Status |
| ---------- | ---------------------- | ----------------- | ---------------- | ------ |
| Anime migration | `AnimeCapabilityProcessor` returns true; `docs/features/content-type-reference.md` describes migration as unavailable | Anime migration remains supported; documentation is stale | Capability matrix in Phase 3; docs projection/correction in Phase 7 | Accepted: documentation drift |
| Book downloads as a registered capability | Production runtime enables the Book download plugin, but the plugin defaults `downloadsEnabled` to false and can be assembled without its provider | Production Book downloads are supported; the flag is a construction/testing seam | Registration evidence in Phase 1; production provider contracts in Phase 6 | Accepted: non-product ambiguity |
| Anime child-group filtering | Anime registers a processor but the processor reports unsupported and implements empty/no-op behavior; Book expresses absence by not registering | Anime remains intentionally unsupported; registered no-op is not positive evidence | Explicit absence and provider cleanup in Phase 3 | Accepted: intentional absence with ambiguous representation |
| Book library-update notification semantics | The shared update notifier supports the Book workflow but its notification type catalog defines only Manga and Anime, with Manga as the fallback | Book receives shared behavior with explicit neutral/Book semantics, never Manga fallback | Derived notification integration in Phase 5; contract in Phase 6 | Accepted: implementation bug |

## Phase 0 Completion Summary

Milestones 0.1 and 0.2 establish the evidence inventory and dependency graph. Milestone 0.3 accepts:

- evidence authority based on provider registration, exceptional intrinsic declarations, and feature-owned derivation;
- distinct supported, intentional absence, not-applicable, contextual, missing-obligation, and unresolved semantics;
- preservation of public source/tracker ownership;
- reviewed classifications for every inventoried capability area; and
- expected outcomes and later phases for every discrepancy.

Phase 0 is complete. Its accepted decisions are the preconditions for Phase 1, which must be split into reviewable implementation milestones before runtime work begins.
