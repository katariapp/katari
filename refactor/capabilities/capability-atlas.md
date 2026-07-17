# Capability Atlas

Status: Milestone 0.1 evidence inventory complete; consumer and coverage mapping deferred to Milestone 0.2

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

These classifications are provisional until Milestone 0.3. In particular, processor registration can prove that a provider exists without proving every behavioral sub-capability exposed by that provider.

## Evidence Boundaries for Milestone 0.1

This milestone inventories declarations, defaults, unsupported results, and direct gates. Application consumers, tests, boundary enforcement, and documentation coverage are intentionally deferred to Milestone 0.2. The tables therefore describe current evidence and ownership without claiming that all downstream consequences are covered.

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
| Merge | Entry- and selection-dependent | `supportsMerge(entry)`, default false; selection also requires at least two entries, same type, and at most one already-merged entry | Capability processor plus shared selection policy | Manga true, Anime true, Book false. Documentation currently disagrees for Anime. |
| Update eligibility | Provider-backed, entry/policy-dependent | Presence of `EntryUpdateEligibilityProcessor` | Type processor | All three registered; the result is a policy decision, not a simple support boolean. |
| Progress transfer | Provider-backed | Presence of `EntryProgressProcessor` | Type processor | All three provide snapshot, restore, and copy. |
| Playback preference transfer | Provider-backed | Presence of `EntryPlaybackPreferencesProcessor` | Type processor | Anime registered; Manga and Book have neutral missing-provider behavior. |
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

## Evidence Conclusions for Review

- Processor registration is useful provider evidence but is not a universal capability declaration. Capability, consumption, child-group, library-filter, preview, and immersive processors all contain meaningful sub-capability or contextual checks.
- The same absence is represented by missing providers, default-false methods, explicit `Unsupported` values, empty results, no-ops, presentation booleans, and direct type checks.
- Some facts that look type-wide are contextual. Anime preview depends on the source, immersive loading depends on resolvable media, tracking depends on the tracker, and selection actions depend on the whole selection.
- At least one likely derived combination, downloading bookmarked children, is independently encoded in downloader behavior and presentation.
- Presentation and documentation already contain support statements independent from executable processor behavior.
- Cross-feature routing can omit a type even when the underlying workflow is shared: Book update notifications currently fall through to Manga-specific notification semantics.
- Compatibility, media format, storage, and vocabulary branches must remain visible but outside the capability catalog unless they independently gate a user-facing feature.

These are inventory findings, not accepted architecture decisions. Milestone 0.3 will decide which facts are fundamental, derived, contextual, intentionally unsupported, or specialized.

## Discrepancies

Observed disagreements are recorded without changing implementation or public documentation.

| Capability | Conflicting statements | Expected behavior | Resolution phase | Status |
| ---------- | ---------------------- | ----------------- | ---------------- | ------ |
| Anime migration | `AnimeCapabilityProcessor` returns true; `docs/features/content-type-reference.md` describes migration as unavailable | Unresolved | 0.3 decision, later implementation/docs phase | Open |
| Anime merge | `AnimeCapabilityProcessor` returns true; `docs/features/content-type-reference.md` describes merge as unavailable | Unresolved | 0.3 decision, later implementation/docs phase | Open |
| Book downloads as a registered capability | Production runtime enables the Book download plugin, but the plugin defaults `downloadsEnabled` to false and can be assembled without its provider | Unresolved: construction seam versus meaningful optional capability | 0.3 decision | Open |
| Anime child-group filtering | Anime registers a processor but the processor reports unsupported and implements empty/no-op behavior; Book expresses absence by not registering | Unresolved: explicit unsupported provider versus missing provider | 0.3 decision | Open |
| Book library-update notification semantics | The shared update notifier supports the Book workflow but its notification type catalog defines only Manga and Anime, with Manga as the fallback | Unresolved: derive shared behavior and type presentation, or add an explicit specialized obligation | 0.3 decision, later implementation phase | Open |

## Phase 0 Completion Summary

Not yet complete. Milestone 0.1 establishes the evidence inventory. Consumer/coverage mapping and expected-state decisions remain in Milestones 0.2 and 0.3.
