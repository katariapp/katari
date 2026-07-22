# Phase 8 Census and Final Disposition

Updated: 2026-07-22

## Purpose

This is the audit-only entry gate for Phase 8. It reruns the migration-inventory probes against the completed Phase 7
tree, classifies every family of remaining match, and separates final cleanup from valid architecture, compatibility,
storage, media, and environment-composition boundaries.

Search matches are candidates, not defects. A type name, support-like verb, or provider map is retained when it carries
identity, concrete operation state, external owner truth, or internal dispatch without independently authorizing a
product feature. It is removed or corrected when it survives as a second support authority, an obsolete migration path,
or misleading current-state guidance.

## Audit Result

- The retired catalog/report authority has no production match.
- Application code has no raw Entry interaction facade reference; raw operational dispatch is confined to the SPI and
  root Feature implementations.
- Every current type, Feature, and contextual register row has a final owner and disposition.
- `checkEntryInteractionBoundaries`, `verifyEntryFeatureArchitecture`, and `:app:compileFossKotlin` pass.
- No compile failure or unfinished production Feature migration remains.
- Final cleanup is limited to stale migration descriptions, milestone-coded production comments, and a final review of
  deliberately narrow boundary exceptions. No media processor, provider SPI, source SDK contract, or compatibility
  adapter is scheduled for blanket removal.

## Repeatable Probe Results

Counts below are matching files followed by matching lines where the probe reports lines.

| Probe | Result | Final classification |
| --- | ---: | --- |
| `EntryType` repository census | 393 files | 226 files under `src/main`, 128 test, 13 docs, 15 refactor, and 11 other source/layout files, including common-source-set SDK code. Runtime/source matches are classified below as identity/propagation, type-owned behavior, graph projection, external metadata, compatibility/storage, or enforcement. Tests and documentation are either graph-selected validation, owner behavior, compatibility coverage, or historical refactor evidence. The final count includes this census itself. |
| Concrete current types or `EntryType.entries` | 81 files / 122 lines | Type modules own type identity and media/storage invariants. The remaining matches are generic enum iteration, wire/legacy conversion, external tracker/source defaults, tooling previews, graph-ID projection, or frozen notification identity compatibility. No generic Feature applicability is inferred from a current-type branch. |
| Support-like verbs | 127 files / 374 lines | External source/tracker facts, platform/widget state, concrete operation results, and Feature-facing availability queries. The only legacy Merge/Migration names are negative boundary-rule sentinels. No parallel Entry support declaration remains. |
| Entry-type maps and sets | 52 files / 120 lines | Provider dispatch indexes, evaluated applicability/result sets, source/tracker metadata, repository filters, or UI grouping. None is a handwritten type-to-capability matrix. |
| `Entry...Interaction` names | 29 files / 79 lines | Twelve SPI files define operational provider dispatch; sixteen root Feature files consume that dispatch internally; one build rule guards the boundary. There is no application/domain/data consumer. |
| Media-cache, viewer-settings, and provider IDs | 32 files / 112 lines | Provider SPI, type-owned providers, Feature implementations, application screen projections, persistence-compatible surface IDs, and enforcement. Provider IDs identify settings surfaces; they do not assert type support. |
| Source capability contracts | 35 files / 167 lines | Public/legacy source ownership, source-manager and paging mechanics, type-owned media resolution, Feature-owned contextual interpretation, and enforcement. Application UI consumes Features; it does not reinterpret raw source contracts. |
| Retired report/catalog authority | 0 | Clean. `EntryCapabilityReport`, `EntryCapabilityCatalog`, `supportsTypeWide`, and `EntryDownloadCapabilityPolicy` do not survive. |

## Production `EntryType` Disposition

The complete production census falls into these non-overlapping ownership classes:

1. **Type-owned contributions and mechanics.** Manga, Anime, and Book modules use their own type identity when declaring
   providers and when enforcing media, queue, storage, reader, player, cache, or migration invariants. These branches do
   not authorize application Features and remain.
2. **Graph-derived projections.** Feature implementations map evaluated `ContentTypeId` results back to `EntryType` for
   typed application results. These mappings iterate `EntryType.entries`; they do not enumerate supported types and
   automatically include a new installed enum-backed type.
3. **Frozen compatibility identity.** Library-update notifications retain the shipped Manga and Anime Android channel,
   group, and notification IDs. Other types use a collision-checked derived route. This is wire/platform continuity, not
   Feature applicability.
4. **Persistence and wire compatibility.** Backup defaults, legacy backup conversion, feed decoding, library-worker
   deserialization, extension metadata decoding, and legacy Entry adapters retain concrete values required to read old
   state.
5. **External owner truth.** Source metadata and tracker registrations may carry `Set<EntryType>`. They are contextual
   inputs owned by those integrations and never validate a returned Entry or a type plugin.
6. **Structural product data.** Repositories, content filters, Library tabs, upcoming queries, badges, and source
   indicators propagate or group the enum without deriving behavioral support.
7. **Tooling.** Debug launchers and Compose/sample providers may name a concrete type without participating in runtime
   support.
8. **Enforcement.** Build logic names current suspicious patterns only to reject them or to preserve an exact reviewed
   compatibility boundary.

No production concrete-type match remains unclassified.

## Support-Like Verb Disposition

- `supportedEntryTypes`, `supportsLatest`, and `supportsImmersiveFeed` are source metadata or source-owned contextual
  facts. Tracker `supportedEntryTypes`, reading dates, privacy, automatic binding, remote deletion, and scoring are
  tracker-owned facts. Their application consequences are composed by the owning Entry Features.
- `isSupported` and the remaining `supports...`/`can...` names in core, reader, player, WebView, permission, navigation,
  backup, and widgets describe concrete runtime or UI state. They neither register a provider nor decide type-wide
  Feature applicability.
- Application names such as `canUseDownloadActions`, `canDownloadSelection`, `canSetConsumed`, `canContinue`, and
  `supportsTracking` are presentation-facing queries over Feature results. The names do not conceal a type switch or a
  second support flag.
- `canOpen` in preview data is a returned page action property, not Open-provider evidence.
- `supportsMigration`, `canMigrate`, `supportsMerge`, and `canMergeSelection` occur only in build-rule forbidden-symbol
  sets so reintroduction fails validation.
- The tracker progress-sync host defensively checks the selected external tracker registration after the Tracking
  Feature has selected the service. This is owner-local execution validation, not an alternate Entry-type authority.

No support-like production match requires a capability migration.

## Provider, Interaction, and Collection Disposition

- `EntryInteractionComposition` builds typed provider maps from plugin bindings. The maps are internal operational
  dispatch, and absence remains valid. They do not publish support to application code.
- `EntryInteractions` and the individual `Entry...Interaction` interfaces remain SPI vocabulary. Feature coordinators
  are their only product-behavior consumers; module dependencies and boundary validation prevent application use.
- Viewer-settings and media-cache providers are optional plugin bindings. Their Features discover applicable types and
  validate the separately installed application projection or cache artifact. Stable provider/surface IDs exist for
  routing and preference compatibility, not support declaration.
- Result types carrying `Set<EntryType>` report the exact applicable or inapplicable subjects selected by graph
  evaluation. Repository filters and source/tracker metadata sets carry data, not capability truth.
- `productionEntryTypeRuntimeModules` is the one environment installation list for independently owned content-type
  modules. `productionEntryFeatureContributors` is the corresponding Feature installation list. Neither repeats
  providers, consequences, contracts, projections, or support outcomes.
- The viewer-settings screen projection list is an application composition boundary for concrete navigation objects.
  Provider relationships select whether a surface applies, and missing applicable projections are validated as
  obligations.

These are retained architecture, not legacy registries.

## Source Contract Disposition

- `entry-source-api` owns current source contracts and `source-compat` owns the legacy Manga translation. Their
  definitions and adapter checks remain external/compatibility authority.
- `AndroidSourceManager`, `SourceExtensions`, `ExtensionLoader`, `SourceManager`, `CatalogSource`, and
  `CatalogPagingSource` retain raw catalogue mechanics needed to discover and page sources. Feature consumers receive
  the resolved Catalogue projection.
- Manga image pages, Anime subtitles/streams, reader loaders, downloaders, and media renderers retain raw source
  contracts inside their type-owned mechanics. Failures after applicability remain operational results.
- Catalogue, Preview, Immersive, Related Entries, source settings/home/WebView/deep-link/refresh, cover network,
  notification metering, and tracker adapters interpret raw context only inside their owning root Feature.
- Baseline-profile matches are generated runtime profile entries and carry no source-capability semantics.

Every `C01`–`C24` row is therefore either migrated to a contextual Feature, retained by its external owner, or retained
as a compatibility/media/runtime-state boundary exactly as recorded in the Phase 6 owner classification.

## Register Reconciliation

### Type register

- `T01` and `T24` are satisfied by the single owned type-runtime-module contribution and environment installation.
- Provider-backed rows `T02`–`T07`, `T09`, `T11`–`T12`, `T14`–`T22`, and `T25`–`T26` are supplied independently through
  plugin bindings; provider absence is valid.
- `T08` was removed because automatic filtering was shared policy rather than a fundamental provider.
- `T10` is a provider-free shared Merge Feature; Download and Migration relationships remain independently
  provider-derived.
- `T13` is shared Update Eligibility policy, `T23` is type-owned presentation input selected by its Feature, and `T27`
  is discovered preference ownership.

All `T01`–`T27` rows have final dispositions.

### Feature register

`F01`–`F27` are complete. Their application consumers use Feature APIs; provider dispatch and graph evaluation remain
inside the root module. Entry Tracking, which was classified outside the numbered register, is likewise migrated as an
external-registration Feature with owner-local host execution.

All UI, worker, setting, notification, navigation, backup, migration, profile, cache, and documentation consequences in
the register are owned. An already-generic call site is retained only where its final owner is structural,
compatibility, media, or external context rather than Feature applicability.

### Context register

`C01`–`C24` have the final Phase 6 dispositions recorded in `migration-inventory.md`: catalogue/description, source
actions, existing contextual Entry Features, type-owned media, refresh/network policy, Tracking, and compatibility.
No contextual fact was flattened into a global Entry capability.

## Final Cleanup Queue

Phase 8.1 completed this queue:

1. **Completed:** replaced milestone codes in production comments with durable ownership language:
   - migration intent comments describe target-discovery ownership without a register code;
   - the stale Entry screen migration TODO was removed;
   - the empty Main Activity profile-routing conditional and its migration comment were removed.
2. **Completed:** reconciled `migration-obligations.md`. Its chronological sections remain useful evidence, while
   `P5-FEATURE-CONTRIBUTOR-INSTALLATION`, `P5-ENTRY-UI`, and `P7-GRAPH-SELECTED-BEHAVIORAL-TESTS` now record their final
   resolutions and the ledger states that no active compile obligation remains.
3. **Completed:** updated the active Phase/status text and migration-inventory snapshot status so future work cannot
   mistake the pre-Phase-4 counts or old registry description for current architecture.
4. **Completed:** reviewed every exact boundary exception while preserving fail-closed behavior. The live exception
   sets cover source composition/ports, two media-resolution owners, frozen notification routing, and required public
   Android entry points. The obsolete Manga `DownloadJob` exception and the unused broad `AppModule` type-branch
   exception were removed. No directory exemption was broadened.
5. **Completed:** reran all eight probes after cleanup. The broad `EntryType` count increased only because this census
   file is now part of the refactor workspace; production and every focused probe count remain unchanged, and no new
   candidate appeared.

## Manifesto Comparison

- **Declare truth once:** provider presence, external registrations, and runtime context remain the respective owners;
  no report, catalog, UI flag, or type matrix survives as parallel truth.
- **Participation is discovered:** type and Feature contributions enter through the two environment installation seams;
  downstream relationships, obligations, contracts, reporting, and documentation are discovered without another
  product list.
- **Partial support is valid:** operational maps and Feature results accept provider absence; the Book construction seam
  can omit Downloads without invalidating the plugin, while production currently installs it.
- **Features own implications:** application `can...` queries consume Feature decisions, and external/source checks are
  interpreted by their owning Features or remain owner-local post-selection mechanics.
- **Missing work is visible:** the architecture gate executes discovered contracts, obligations, reporting, and
  documentation; the current production graph reports no unresolved obligation.
- **Compilation follows architecture:** the application compiles with the retired authority absent and without a
  compatibility facade.

The census is aligned with the manifesto. Its cleanup queue removes misleading residue; it does not redefine valid
partial support or convert legitimate media/context distinctions into central capabilities.

## Exit-Gate Result

- The full pre-release validation gate passes under JDK 21: `spotlessCheck`, `verifyEntryFeatureArchitecture`,
  `verifyLegacySourceAbi`, `testFossUnitTest`, and `verifySqlDelightMigration`.
- The release gate passes under JDK 21: `assembleRelease` and `verifyExtensionRuntimeAbi` with telemetry and updater
  enabled.
- Documentation validation passes using the repository toolchain: production projection verification, both public SDK
  Dokka publications, and the VitePress build under Node 24 and pnpm 10.
- Selection-label coverage now tests the `EntryTypePresentationFeature` projection boundary instead of restating the
  current content-type vocabulary. This removes a test-level parallel statement of product truth.
- Tracking selection coverage now follows the three current selector owners. The audit restored the invariant that the
  user's score, progress, or status selection is captured before asynchronous non-cancellable mutation dispatch.

No exit-gate correction introduced a content-type gate, support flag, Feature allowlist, or provider requirement. The
application, validation, reporting, and documentation all consume the completed owner-discovered architecture.
