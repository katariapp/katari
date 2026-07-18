# Capability Refactor Status

Updated: 2026-07-18

## Repository Snapshot

- Branch: `features-arch-refactor`
- Architecture reset commit: `666487574` (`(refactor): reset capability architecture direction`)
- Contribution semantics commit: `d89e51693` (`(feat): define generic feature contributions`)
- Discovery and assembly commit: `03d0b6422` (`(feat): assemble discovered feature graph`)
- Evaluation and obligations commit: `0a578b784` (`(feat): evaluate feature relationships`)
- Contract and projection selection commit: `f1e66f671` (`(feat): select feature contracts and projections`)
- Phase 3 completion: `c795c505c` (`(refactor): complete feature graph architecture`)
- Migration-readiness commit: `7ab311608` (`(docs): update migration plan`)
- Latest upstream reconciliation: `5e3f948b4` (`Merge branch 'upcoming' into features-arch-refactor`)
- Milestone 4.1 commit: `0accb3667` (`(refactor): migrate entry provider identity`)
- Milestone 4.2.1 commit: `62c9741a1` (`(refactor): bind independent entry providers`)
- Milestone 4.2.2 commit: `f4a6d153c` (`(refactor): decompose download providers`)
- Milestone 4.2.3 commit: `c046e1f8f` (`(refactor): split migration and merge providers`)
- Milestone 4.2.4 commit: `c2ca736e4` (`(refactor): migrate remaining entry providers`)
- Milestone 4.3 commit: `c88ff5fe9` (`(refactor): unify entry type runtime composition`)
- Phase 4 completion: `17726de20` (`(refactor): replace interaction registry`)
- Runtime-bridge boundary correction: `4b517ed53` (`(fix): discover entry runtime module bridges`)
- F01 Open completion: `106fec52e` (`(refactor): migrate open feature integration`)
- Architecture Gate 5.0: `83b2f93e7` (`(refactor): enforce feature-facing entry boundary`)
- F02 Continue completion: `0480ffeff` (`(refactor): migrate continue feature integration`)
- F03–F05 completion: `846c6029f` (`(refactor): migrate core download features`)
- F06–F08 completion: `91d57f376` (`(refactor): migrate remaining download features`)
- F09, F10, and F13 completion: `e175cbc3f` (`(refactor): migrate entry state features`)
- F14, F18, and F19 completion: `a03ff2a71` (`(refactor): migrate entry filtering and preview features`)
- F20, F21, and F22 completion: `dd8fc5106` (`(refactor): migrate immersive, related, and progress features`)
- F23 Type Presentation, F24 Library-update Notifications, F25 Viewer Settings, F26 Media-cache Maintenance, and F27
  Profile Preference Ownership are committed and validated.
- Latest earlier production migration: `e04b2481c` (`(refactor): derive download capabilities from providers`)
- Phase 2 completion: `918fcc4d3` (`(refactor): complete bookmark download capability proof`)
- Always verify `HEAD`, the working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 5 — Feature Integration Migration
- Milestone: `F12.2` — Merge profile, transaction, and failure semantics
- State: F12.1 is committed in `cc2d1aa54`. F12.2 has an accepted durable decision defining explicit profile capture,
  optimistic snapshots, atomic database transitions, Profile Move participation, backup restore, and external
  consequence delivery. F12.3–F12.7 and F11 remain pending.

Focused F12.1 validation:

- `spotlessApply`, Entry-interactions API/root debug Kotlin compilation, and build-logic tests pass.
- Permanent Kotlin sources and validation messages use product architecture vocabulary; the `F12` identifier exists
  only in refactor planning records.
- The boundary census intentionally fails on exactly 45 findings: 42 raw Merge authority references assigned to
  F12.3–F12.6 and three transitional capability-facade references shared with F11.
- No persistence implementation or application consumer behavior changed in F12.1.

## Why the Plan Was Reset

The prior sequence built an evidence/reporting foundation, completed a Bookmarking/Downloads vertical slice, and then
began migrating production capabilities one group at a time. General feature discovery, relationship evaluation,
specialized obligations, contract selection, and projections were deferred.

That sequence produced an uncommitted hardcoded completion contract for Open, Continue, Bookmarking, Downloads, and Bulk
Downloads. It also repeated provider registrations as per-type capability assertions. Both mechanisms required a future
contributor to remember another list and therefore contradicted the manifesto. The contract and duplicate assertions were
removed rather than expanded.

The corrected rule is architecture first, production conformance second. Intermediate compile failures are acceptable
when new boundaries expose unported code. Compilation must be restored by migrating that code, not by adding a parallel
authority or weakening the architecture.

## Durable Decisions

- Content types and features contribute through owned, discoverable boundaries.
- A content type is valid with any subset of interaction providers; no interaction, including Open or Continue, is
  mandatory for architectural validity.
- Provider presence proves support and provider absence means unsupported without a separate absence declaration.
- Features own prerequisites, contextual inputs, shared consequences, specialized obligations, behavioral contracts,
  and projections.
- Generic assembly and evaluation contain no concrete type, capability, or feature allowlist.
- Tests verify architecture mechanics, shared behavior, and genuine media behavior; they do not restate declarations.
- The retired report and the retained Bookmarking/Downloads behavior are migration evidence, not protected architecture.

## Completed Historical Work

- [x] Phase 0 evidence inventory, consumer graph, discrepancy review, and product decisions
- [x] Phase 1 capability vocabulary, evidence composition, support outcomes, and deterministic report prototype
- [x] Phase 2 Bookmarking provider split and Bookmarking/Downloads learning slice
- [x] Initial download provider-authority migration committed in `e04b2481c`
- [x] Incorrect uncommitted completion contract removed
- [x] Per-type capability-label assertions introduced or retained for completion enforcement removed
- [x] Manifesto expanded with architecture-before-migration, valid partial type support, provider-backed applicability,
  testing, and compilation principles
- [x] Phase plan reordered around a general architecture kernel before production migration
- [x] Accepted decision `0006-architecture-before-migration.md` recorded
- [x] Architecture reset committed in `666487574`
- [x] Milestone 3.1 standalone generic contribution kernel implemented
- [x] Decision `0007-contribution-semantics.md` accepted
- [x] Milestone 3.1 committed in `d89e51693`
- [x] Milestone 3.2 generic discovery and deterministic graph assembly implemented
- [x] Decision `0008-contribution-discovery-and-assembly.md` accepted
- [x] Milestone 3.2 committed in `03d0b6422`
- [x] Milestone 3.3 generic evaluation and specialized obligations implemented
- [x] Decision `0009-evaluation-and-specialized-obligations.md` accepted
- [x] Milestone 3.3 committed in `0a578b784`
- [x] Milestone 3.4 generic contract, fixture, and projection selection implemented
- [x] Decision `0010-contract-fixture-and-projection-selection.md` accepted
- [x] Milestone 3.4 committed in `f1e66f671`
- [x] Milestone 3.5 dependency boundary and legacy authority cut implemented
- [x] Decision `0011-production-boundary-cut.md` accepted
- [x] Phase 3 completed and committed in `c795c505c`
- [x] Pre-Phase 4 repository census expanded beyond the interaction registry
- [x] Type (`T01`–`T27`), feature (`F01`–`F27`), and contextual/external (`C01`–`C24`) migration registers recorded
- [x] Direct type gates, parallel provider lists, tests, reporting, documentation, and audited non-migration boundaries
  classified in `migration-inventory.md`
- [x] Migration-readiness milestone committed in `7ab311608`
- [x] Upstream `upcoming` merged in `5e3f948b4`; working tree verified clean before Phase 4
- [x] Milestone 4.1 gives Manga, Anime, and Book one owned content-type contribution each
- [x] Open and Continue provider definitions live beside their SPI contracts
- [x] Open/Continue are declared once in each contribution; provider-owned installation derives operational dispatch
- [x] Provider-to-plugin `EntryType` identity is validated generically without a concrete type list
- [x] Graph content-type identity is derived from `EntryType` rather than repeated as a type-owned string
- [x] Decision `0012-entry-plugin-provider-identity.md` accepted
- [x] Milestone 4.1 committed as `(refactor): migrate entry provider identity`
- [x] Capability-owned bindings derive graph evidence and operational dispatch from one type-owned declaration
- [x] Open and Continue moved to the binding mechanism without changing partial-support semantics
- [x] Consumption bound for Manga, Anime, and Book
- [x] Bookmarking bound only for Manga, independently from its shared Consumption implementation object
- [x] Progress transfer bound for Manga, Anime, and Book
- [x] Playback-preference transfer bound only for Anime
- [x] Existing Anime consumption lifecycle event corrected to use the episodes whose state actually changed
- [x] Decision `0013-provider-binding-and-dispatch.md` accepted
- [x] Milestone 4.2.1 committed in `62c9741a1`
- [x] Core downloads initially separated from options, settings, bulk candidates, and automatic filtering
- [x] Download support contributed through capability-owned bindings for Manga, Anime, and optional Book construction
- [x] Anime alone contributes interactive options; Manga contributes each implemented download setting independently
- [x] Manga, Anime, and Book downloaders initially contributed bulk-candidate and automatic-filter providers independently
- [x] Default false/null/empty download sub-capability declarations removed from the core downloader contract
- [x] Decision `0014-download-provider-decomposition.md` accepted
- [x] Milestone 4.2.2 committed in `f4a6d153c`
- [x] Migration and Merge split into independent provider contracts and bindings
- [x] Manga and Anime bind both capabilities independently
- [x] Book's combined default-false capability processor removed without replacement absence declarations
- [x] Shared selection constraints retained as transitional feature policy rather than type provider behavior
- [x] Decision `0015-migration-and-merge-provider-boundary.md` accepted
- [x] Milestone 4.2.3 committed in `c046e1f8f`
- [x] Universal Update Eligibility policy deduplicated from three type processors
- [x] Child List and optional child-progress labels split into independent bindings
- [x] Manga-only operational child-group filtering contributed; Anime no-op provider removed
- [x] Manga/Book outside-release-period filtering contributed; Anime false provider removed
- [x] Manga/Anime Preview and Immersive implementations contributed with contextual conditions retained
- [x] Production type plugins no longer contain direct registry calls or custom installation overrides
- [x] Decision `0016-remaining-interaction-provider-boundaries.md` accepted
- [x] Milestone 4.2.4 committed in `c2ca736e4`
- [x] One runtime module per production type becomes the sole root type aggregation
- [x] Plugins, library progress, viewer settings, caches, warmups, and image components derive from runtime contributions
- [x] Separate library-progress list and public per-type installation paths removed
- [x] Runtime plugin/calculator identity validated against the owning type
- [x] Presentation vocabulary recorded as a Phase 5 projection obligation rather than mixed into runtime services
- [x] Decision `0017-owned-type-runtime-modules.md` accepted
- [x] Capability-owned installer callbacks and the `EntryInteractionRegistry` interface/implementation removed
- [x] Operational interaction facades derive typed provider maps from one generic binding index
- [x] Provider contracts and provider-backed dispatch split into cohesive interaction-family files
- [x] `EntryInteractionPlugin` and `EntryInteractionComposition` reduced to their actual ownership boundaries
- [x] Registry-shaped, per-capability duplicate tests replaced by generic composition invariants
- [x] Superseded registry fixtures removed without adding capability-label or current-type matrix assertions
- [x] Decision `0018-generic-provider-index.md` reviewed and accepted
- [x] Stale boundary-check allowlist replaced with discovery of the accepted public runtime-module bridge contract
- [x] `F01` Open owner, prerequisite, consequence, and complete consumer disposition recorded
- [x] Open UI/notification/deep-link dispatch migrated behind one graph-derived coordinator
- [x] Synthetic single-provider and valid-absence Open feature proofs added
- [x] F01 committed in `106fec52e`
- [x] Raw operational facades and their aggregate moved from exported API to provider SPI
- [x] Root composition stopped injecting raw facades and graph-evaluation objects
- [x] Application Entry-interactions compile classpath reduced to root plus app-facing API; SPI and Feature Graph absent
- [x] Boundary validation derives every public SPI type instead of naming current interaction facades
- [x] Application-facing API validation rejects raw `Entry*Interaction` contracts generically
- [x] Decision `0019-feature-facing-application-boundary.md` reviewed and accepted
- [x] Architecture Gate 5.0 committed in `83b2f93e7`
- [x] F02 Continue owner, prerequisite, result semantics, and complete consumer disposition recorded
- [x] Entry, Library, and History Continue surfaces migrated behind one graph-derived coordinator
- [x] Synthetic provider, no-next, and valid-absence Continue proofs added
- [x] F03 queue/runtime state, counts, controls, worker, and notification consequences migrated behind one feature
- [x] F04 individual, bulk, bookmarked-bulk, retry, and notification actions migrated behind one feature
- [x] F05 automatic-download policy and orchestration made shared for every core Download provider
- [x] Redundant per-type automatic-filter capability, dispatch, bindings, and generic domain policy exposure removed
- [x] F03–F05 application consumers reconciled together; no migrated operation retains raw provider access
- [x] F06 lifecycle events, cleanup, download-ahead, category policy, and derived Bookmark protection migrated behind one
  graph-selected feature
- [x] F07 download options and specialized setting visibility split into feature-owned contracts without a type map
- [x] F08 download maintenance routes source/title rename, cache invalidation, whole-Entry removal, and source purge through
  one feature-owned boundary
- [x] F06–F08 application consumers reconciled together; no application production source imports the raw Download facade
- [x] F09 Consumption applicability, shared transition policy, media mutation, application consumers, and F06 lifecycle
  emission migrated behind one feature
- [x] F10 Bookmark applicability, selection policy, mutation, Entry/Updates consumers, and derived Download relationships
  migrated without a support matrix
- [x] F13 Update Eligibility deduplicated into one provider-free shared feature for every composed type
- [x] F09/F10/F13 application consumers reconciled together; no application production source imports their raw facades
  or the deleted capability report/catalog
- [x] F15 Progress Transfer applicability, snapshot/restore/copy results, backup consumers, and migration copy moved behind
  one feature-owned boundary
- [x] F16 backup snapshot, restore, and migration copy consume provider-derived structured Feature results
- [x] F17 Child List ordering, display rows, missing-count results, ordered-child consumers, and optional independent
  Child Progress relationship migrated behind one feature
- [x] F15/F16/F17 application consumers reconciled together; no application production source imports their raw facades
- [x] F14 Library filter policy, active state, and capability-dependent control availability migrated behind one feature
- [x] F14 application consumers use neutral DTOs and no longer import raw Library-filter dispatch

Focused F01 validation:

- Build-logic tests pass after replacing the obsolete installer/plugin boundary allowlist with discovery of public
  functions returning `EntryTypeRuntimeModule` from owned type modules.
- `spotlessApply`, `spotlessCheck`, Feature Graph tests, Entry interactions API/SPI compilation, and Manga, Anime, and
  Book interaction compilation pass.
- Root Entry interactions compilation reaches only the previously recorded Download Lifecycle report/policy errors; it
  reports no Open migration error.

Focused Architecture Gate 5.0 validation:

- Build-logic tests pass, including rejection of direct Feature Graph/SPI dependencies, SPI export, and raw interaction
  source references.
- Entry-interactions API, SPI, Manga, Anime, and Book debug Kotlin compilation passes after the boundary move.
- The application's `fossCompileClasspath` contains the root Entry-interactions module and its API only; it contains
  neither SPI nor Feature Graph.
- Boundary validation intentionally fails on 24 application production files using 13 raw facade types. Open is absent
  from that list because F01 already consumes its feature contract.

Focused F02 validation:

- `spotlessApply`, Entry-interactions API/SPI compilation, and `git diff --check` pass.
- Focused synthetic Continue tests are present. Their Gradle task reaches the previously recorded Download Lifecycle
  report/policy compile failures before test execution; it reports no F02 production-source error.
- No application production source imports `EntryContinueInteraction`.
- Root compilation reaches only the previously recorded Download Lifecycle report/policy errors; it reports no Continue
  migration error.
- Full application compilation and a clean boundary check remain intentionally deferred while F11, F12, and F14–F27 raw consumers are
  inaccessible.

Focused F03–F05 validation:

- `spotlessApply`, Feature Graph tests, Entry-interactions API/SPI compilation, and Manga, Anime, and Book interaction
  compilation pass after combining the three isolated implementations.
- At that milestone, the combined boundary census reported 34 remaining raw application references, all assigned to
  F06–F27. F03-only
  screens and the shared Library, Updates, and Notification Receiver download paths no longer import raw download SPI.
- Root Entry-interactions compilation reaches only the known F06 lifecycle failures: deleted `EntryCapabilityReport`,
  deleted `EntryDownloadCapabilityPolicy`, and the resulting runtime factory inference error.
- Focused root feature tests are present but cannot execute until the independent F06 main source set compiles.

Focused F06–F08 validation:

- `spotlessApply`, Feature Graph tests, build-logic tests, SQLDelight migration verification, API/SPI and all production
  type/root interaction compilation pass after combining the three implementations.
- All 46 root Entry-interactions unit tests pass, including the graph-selected F06, F07, and F08 behavior contracts.
- The combined boundary census has 25 remaining raw application references, all assigned to F09–F27; application
  production code has no raw `EntryDownloadInteraction` reference.
- FOSS application compilation advances to the recorded F09–F27 migration queue and reports no F06–F08 symbol or
  dispatch error.

Focused F09/F10/F13 validation:

- `spotlessApply`, Feature Graph tests, build-logic tests, API/SPI and all production type/root interaction compilation,
  and all root Entry-interactions unit tests pass after combining the three isolated implementations.
- The boundary census has 16 remaining raw application references, all assigned to F11, F12, and F14–F27. Application
  production code has no raw Consumption, Bookmark, or Update Eligibility interaction and no capability report/catalog.
- FOSS application compilation advances to the recorded later-feature migration queue and reports no F09, F10, or F13
  symbol or dispatch error.

Focused F15/F16/F17 validation:

- `spotlessApply`, API/SPI and all production type/root interaction compilation, Feature Graph tests, build-logic tests,
  and all root Entry-interactions unit tests pass after combining the three implementations.
- The combined boundary census reports 8 expected later-feature violations and no raw Child List or Child Progress
  application reference; Progress and Playback Preferences are also absent from the app raw-facade census.
- FOSS compilation reports no F15, F16, or F17 symbol or dispatch error before stopping at later raw facades.
- Full Manga/Anime type-suite execution remains blocked by the pre-existing test harness calls that omit the required
  feature-contributor argument to `createEntryInteractions`; F17 does not conceal that architecture obligation with an
  empty contributor placeholder or production fallback.

Focused F14 validation:

- `spotlessApply`, API/SPI/root compilation, and all root Entry-interactions tests pass in the isolated worktree.
- The boundary census reports seven expected later-feature violations and no raw Library-filter application reference.
- FOSS compilation reports no F14 error before stopping at the expected F11/F12/F18–F20 raw-facade queue.

Focused F18 validation:

- `spotlessApply`, API/SPI/Manga/root production compilation, and all root Entry-interactions unit tests pass, including
  the synthetic Child Group Filtering contract.
- The contract proves valid provider absence, supported-empty state, strict dispatch, normalization/filtering,
  multi-member observation and mutation, profile-aware snapshot, and additive restore without a production type matrix.
- Application production code has no raw `EntryChildGroupFilterInteraction` reference. The boundary census reports 7
  expected remaining references assigned to F11, F12, F14, F19, and F20; FOSS compilation reports no F18 error before
  stopping at those raw-feature migration failures.
- The existing Manga type-test source still cannot compile because its pre-existing one-argument
  `createEntryInteractions` calls omit the now-required feature contributors. F18 adds no empty-contributor shim.

Focused F19 validation:

- Preview provider presence selects every shared surface and lifecycle consequence. Configuration, Child List, Open,
  and source-capability context remain independent relationships rather than a combined support label.
- The provider-derived settings projection contains no Manga/Anime UI switch. A provider declares contextual source
  requirements beside its settings, and generic presentation renders every discovered settings entry.
- Child-backed providers fail coordinator construction when Preview plus Child List cannot be selected; fixed-config
  and provider-less types remain valid.

Combined F14/F18/F19 validation:

- `spotlessApply`, Feature Graph and build-logic tests, API/SPI and every production type/root interaction compilation,
  all root Entry-interactions tests, and FOSS application compilation pass after conflict reconciliation.
- Application production and test code contain no raw Library Filter, Child Group Filter, or Preview interaction.
- The boundary census is exactly four later-feature references: three F11/F12 capability-facade consumers and one F20
  Immersive consumer.
- The settings boundary caught and rejected a hardcoded Manga/Anime Preview text map during integration. It was removed
  in favor of provider-declared context plus generic presentation; no allowlist exception was added.

Focused F20 validation:

- Immersive provider presence selects every common surface and lifecycle consequence. Source opt-in, Child List, Open,
  descriptive metadata, and resolved media remain independent contextual or derived relationships.
- Child-backed providers fail coordinator construction when Immersive plus Child List cannot be selected; entry-level
  and zero-preload providers remain valid.
- Catalogue/feed mode, per-entry long press, settings evidence, preload, loading, rendering, progress, and release use
  one application Feature boundary. Raw SPI access, silent release, and missing-provider preload fallback are removed.
- The synthetic contract proves structured provider absence, empty-runtime source closure, source unavailable, source
  opt-out, no reading child, media failure, metadata-only surface pruning, Open composition, strict lifecycle, and
  anonymous type participation.
- API/SPI, every production type/root interaction module, and focused F20 tests pass. FOSS compilation reports no F20
  error before stopping at the expected F11/F12 and unrelated application migration queue.
- The boundary census is exactly three later-feature references, all raw F11/F12 capability-facade consumers; no F20
  application reference remains.

## New Phase Sequence

- Phase 3: General relationship architecture
- Phase 4: Entry-type composition migration
- Phase 5: Feature integration migration
- Phase 6: Contextual and external integration
- Phase 7: Graph-selected contracts, reporting, and documentation
- Phase 8: Legacy removal, boundary enforcement, and build completion

Phase 3 milestones:

- 3.1: Contribution semantics and ownership
- 3.2: Discovery and graph assembly
- 3.3: Evaluation and obligations
- 3.4: Contract and projection selection
- 3.5: Dependency boundary and migration cut

No Manga, Anime, Book, Bookmarking, Downloads, or other product-specific branch may enter the generic Phase 3 kernel.
The phase uses anonymous synthetic contributions to prove unknown future participation.

Milestone 3.5 removed `EntryCapabilityCatalog`, `EntryCapabilityReport`, `supportsTypeWide`, legacy report assembly,
explicit absence compensation, and production report DI exposure rather than deprecating them behind a working facade.
Unported consumers now fail to compile and are recorded in `migration-obligations.md`. The old report does not survive
beside the graph.

## Current Working Tree Scope

- `entry-interactions:api` exports the lower-level `feature-graph` contract.
- Entry-type plugins must be owned graph contributors as well as operational registrars; no default or lambda
  compatibility contribution exists.
- Independent feature contributors remain separate composition inputs and are not forced through entry-type plugins.
- Entry-interaction composition now discovers, assembles, evaluates, and selects the graph and exposes all three results
  through runtime DI beside operational interactions.
- The catalog, legacy evidence/support model, report, `supportsTypeWide`, report assembly, report-driven download policy,
  report DI binding, and authority-focused unit tests are deleted.
- Anime and Book no longer declare explicit unsupported outcomes for missing providers.
- Remaining production/test references are owned in `migration-obligations.md`; they are compile failures rather than a
  working fallback authority.
- A complete anonymous acceptance test proves future complete, incomplete, and partial types through unchanged discovery,
  evaluation, consequence, obligation, contract, and projection paths.
- Accepted decision `0011-production-boundary-cut.md` and Phase 3.5 completion notes.
- Milestone 4.1 is committed in `0accb3667`.
- Milestone 4.2.1 is committed in `62c9741a1`.
- Milestone 4.2.2 is committed in `f4a6d153c`.
- Milestone 4.2.3 is committed in `c046e1f8f`.
- Milestone 4.2.4 is committed in `c2ca736e4`.
- Milestone 4.3 is committed in `c88ff5fe9`.
- Manga, Anime, and Book plugins expose their operational `EntryType` and one owned `ContentTypeContribution`.
- Open and Continue are the first graph-backed provider contracts. Neither is mandatory, no explicit unsupported
  declaration exists, and type plugins contain no separate Open/Continue registry call.
- `T01`–`T27` now use one owned contribution/runtime boundary or deliberate shared policy; F23 resolves the former
  `T23` presentation-projection obligation through optional type-owned providers.
- No dummy feature contribution or compatibility reachability path has been added. Production graph assembly contains
  only real migrated owners; F11 and F12 remain deliberately absent until their owning milestones.

## Pre-Phase 4 Census Findings

The interaction registry is not the complete participation boundary. The census found systems that can currently be
forgotten even when an interaction provider is added:

- a separate required-per-type library-progress calculator registry;
- parallel root lists for type plugins, runtime installers, warmups, viewer settings, media caches, and image components;
- viewer-setting provider discovery followed by a hardcoded provider-to-screen and settings-search map;
- media-cache providers followed by hardcoded keys, labels, launch auto-clear preferences, and startup wiring;
- manually enumerated profile preference ownership and legacy profile-key correction;
- source SDK capabilities with independently reconstructed consumers across browse, entry, feed, update, migration,
  download, and WebView behavior;
- tracker type applicability and tracker sub-capabilities with entry, library, sync, and dialog consequences;
- direct capability gates in backup, restore, migration, library-update notification routing, presentation, and settings;
- boundary-check allowlists that require another edit for each concrete type bridge; and
- curated settings navigation/search lists that can omit a contributed capability surface.

The complete proposed dispositions are in `migration-inventory.md`. The ledger also records the interaction-provider
sub-capabilities that would otherwise be lost by a mechanical processor migration, including download options/settings,
bulk pools, automatic filtering, migration/merge, child progress labels, child-group behavior, and library filters.

## Approved Boundary Classifications

Approved on 2026-07-18:

- All listed out-of-boundary systems participate in the application-wide graph through their real owners.
- Book's internal format-processor registry remains an internal media boundary unless a processor exposes a
  cross-feature consequence.
- Global non-feature settings navigation may remain curated; capability-owned settings screens and search participation
  must be contributed and selected.

## Last Validation

- `./gradlew --quiet spotlessApply :feature-graph:testDebugUnitTest :entry-interactions:api:compileDebugKotlin` passed.
- The complete synthetic test covers automatic future participation, shared artifact identity, missing specialized
  obligations, and a valid partial type without production vocabulary.
- `:entry-interactions:spi:compileDebugKotlin` now passes after download dispatch stopped consuming the deleted report.
- Search confirms no legacy catalog/report definition, explicit unsupported outcome, or production report DI binding
  remains.
- `git diff --check` passed.
- The pre-Phase 4 census inspected interaction processors and defaults, all production `EntryType` references in the
  audited app/domain/data/source/interaction modules, direct current-type branches, source API capability contracts,
  tracker capabilities, root composition, settings, notifications, backup/migration, profile ownership, caches, tests,
  documentation, and boundary enforcement.
- Census snapshot: 14 interaction processor categories, 76 production files with `EntryType` in the audited modules, 20
  production files outside type modules with direct current-type constants, 214 audited production/test and
  boundary-build-logic files with `EntryType`, one separate type-provider registry for library progress, and 16 source
  capability contracts or capability-bearing contracts.
- `git diff --check` passes for the migration-readiness documentation.
- Milestone 4.1 ran `./gradlew --quiet spotlessApply` successfully.
- During Milestones 4.1 and 4.2.1, `:entry-interactions:spi:compileDebugKotlin` reached only the five then-recorded
  deleted-report errors; no migrated provider contract introduced an earlier failure.
- Milestone 4.2.1 reran `spotlessApply`, the Feature Graph tests, and Entry interactions API compilation successfully.
- Milestone 4.2.2 passes `:entry-interactions:spi:compileDebugKotlin` plus Manga, Anime, and Book debug Kotlin
  compilation. The former five deleted-report errors are removed; the remaining bookmarked-bulk dispatch rule is a
  recorded Phase 5 feature-consumer migration, not a replacement authority.
- Milestone 4.2.3 reruns formatting and passes SPI plus Manga, Anime, and Book debug Kotlin compilation.
- Milestone 4.2.4 reruns formatting and passes SPI plus Manga, Anime, and Book debug Kotlin compilation.
- Milestone 4.3 passes API, SPI, and Manga, Anime, and Book debug Kotlin compilation after formatting.
- Before `F01`, `:entry-interactions:compileDebugKotlin` failed at both the intentionally unported Download Lifecycle
  report input and missing feature-contributor installation.
- `F01` installs the Open feature contribution and consumes the selected shared-consequence edges through one
  coordinator. The same lower-boundary validation passes; root compilation now fails only at the independent Download
  Lifecycle report/policy migration.

## Manifesto Comparison

- The hardcoded capability list was removed, not repaired.
- The revised plan builds generic discovery, relationships, obligations, contracts, and projections before more consumer
  migration.
- The old catalog/report authority is retired at the Phase 3.5 boundary cut, not deferred until final cleanup.
- All interactions are provider-backed; current ubiquity does not make an operation mandatory for future types.
- A missing provider creates no obligation. Obligations begin only after a feature's declared prerequisites are
  satisfied.
- Tests no longer repeat current provider facts as completeness declarations.
- The decisive Phase 3 proof uses unknown synthetic contributions and rejects concrete product branches.
- Phase 3.1 uses actual provider and executable artifact objects rather than building another descriptive support report.
- The new kernel does not depend on or preserve the old catalog/report API.
- Contributor installation is an environment concern; the graph kernel contains no concrete contributor registry.
- Every supplied provider must connect to a feature-owned relationship, preventing capabilities from silently bypassing
  the consequence graph.
- Assembly records relationships without prematurely evaluating support or obligations.
- Evaluation applies every discovered relationship without a product matrix or feature-specific branch.
- Missing prerequisites remain ordinary unsupported behavior; only missing specialized work after applicability creates
  an obligation.
- Context is retained as conditional instead of being guessed or flattened into type-wide support.
- Per-type applicability edges reference one feature-owned shared consequence, preserving single-gate coordinators.
- Contracts and projections are selected from the same applicable relationships without another support matrix.
- Fixture obligations are type-owned only when a contract declares genuine media-specific validation input.
- Missing shared projections are feature-owned and aggregated across affected subjects instead of duplicated per type.
- No contract, fixture, or projection kind is globally mandatory.
- The old catalog/report authority was deleted rather than wrapped or deprecated.
- Type plugins and independent feature contributors meet only at environment composition; neither side enumerates the
  other.
- No empty type contribution or feature-contributor fallback hides unported work.
- Known compile failures are mapped to Phases 4, 5, and 7 with responsible owners.
- The lower generic and provider-contract boundaries compile even though production does not.
- Compilation pressure cannot justify dual authorities or fallback architecture.
- The full architecture is app-wide and not limited to Bookmarking or Downloads.
- Migration readiness no longer equates “all interaction processors found” with “all participation found.”
- Already-generic call sites remain in the migration register because generic code can still sit outside graph-selected
  ownership and be forgotten by a future contribution.
- Settings, workers, notifications, navigation, backup, migration, profile preferences, caches, external capabilities,
  tests, and documentation are explicit feature consequences rather than a later informal audit.
- The migration inventory is a temporary control surface with completion dispositions, not a new runtime allowlist or
  capability authority.
- Milestone 4.1 uses operational provider instances as graph evidence and derives their dispatch registration; it does
  not repeat support or participation in a matrix or second type-module call.
- The common plugin/provider boundary permits any provider subset and contains no Open/Continue requirement.
- Concrete type identity remains type-owned, while validation contains no Manga, Anime, or Book branch.
- `F01` derives Open applicability from selected shared-consequence edges and matched provider objects; it does not
  introduce a type list, support label, or Open completion matrix.
- Application consumers receive one feature-owned Open gate. The low-level dispatcher remains an implementation detail
  of that coordinator; type-owned Continue processors may still compose their own type-owned Open processor directly.
  UI and notification availability cannot reconstruct support.
- A synthetic unknown contribution with only an Open provider receives the shared consequence automatically, while an
  empty contribution remains valid and produces no action or obligation.
- Every current Open consumer in the migration census has an explicit disposition. Entry-detail navigation and Continue
  are retained outside F01 because they are different behavior, not silently omitted Open consequences.
- F02 derives Continue applicability from the selected Continue consequence and matched provider object without a type
  list or mandatory-provider rule.
- Entry, Library, and History use the same application feature gate; per-item presentation does not reconstruct support
  from unread counts, progress state, or the current production type set.
- `Inapplicable` and `NoNext` are separate outcomes, so valid provider absence is not mislabeled as provider state.
- A synthetic unknown contribution receives next-child dispatch automatically; absence remains valid and produces no
  specialized obligation.
- Architecture Gate 5.0 makes feature ownership an enforceable dependency direction rather than a naming preference.
- Application consumers can see feature contracts and shared models but cannot see raw provider dispatch, the
  `EntryInteractions` aggregate, Feature Graph evaluation, or selected artifacts.
- SPI declarations are discovered by the boundary checker, so a future provider/dispatch type is protected without
  adding its name to an enforcement list.
- Remaining raw application boundary failures are exactly the three F11/F12 `EntryCapabilityInteraction` consumers.
  F27 is complete without introducing a raw interaction-facade path. Re-exporting SPI, restoring
  raw DI, or moving provider facades back into the API would contradict the manifesto rather than fix compilation.
- Unported processor families remain visible obligations instead of being mislabeled through broad processor wrappers.
- Compilation remains subordinate to the architecture: no dummy feature or legacy plugin fallback was added to satisfy
  reachability or old tests.
- Milestone 4.2.1 eliminates the remaining two-call synchronization risk: a capability binding is the single source for
  both graph evidence and operational dispatch.
- A shared implementation object can bind multiple independent capabilities without creating a combined capability.
- Current support is represented exactly: every type binds Consumption and Progress, only Manga binds Bookmarking, and
  only Anime binds Playback-preference transfer.
- Milestone 4.2.2 does not turn the old downloader into one broad capability. Each behavior that a future type may add
  independently has its own provider contract and binding.
- Core downloads imply neither options, settings, nor bulk candidates. They do receive shared automatic-download policy
  through F05; Book remains valid when its optional downloader is omitted.
- Each Manga download setting is an individual graph-visible provider claim, not an enum set treated as another support
  authority.
- Shared bulk selection and automatic-download orchestration are feature-owned in F04/F05; type providers expose only
  the genuinely media-specific operations those features need.
- Milestone 4.2.3 removes the combined Migration/Merge boolean holder instead of wrapping it as one broad provider.
- Migration and Merge bindings are independent even though each current supporting type uses one object to carry both
  compatibility markers.
- The marker contracts state compatibility with shared feature behavior; they do not misrepresent the marker object as
  the owner of migration or merge operations.
- Book contributes neither marker. Its valid type contribution is not made incomplete by those absences.
- Milestone 4.2.4 does not manufacture an Update Eligibility capability from identical type implementations; the common
  rule applies to future types without another opt-in.
- Child-list presence no longer implies child-progress labels, even though all three current implementations bind both.
- False and no-op filtering processors are removed rather than wrapped as capability evidence.
- Preview and Immersive preserve real provider implementations while source, preference, media-resolution, and runtime
  conditions remain contextual.
- Every production type plugin now declares providers only through bindings; the transitional registry is no longer
  called directly by a type module.
- Milestone 4.3 colocates plugins, library progress, settings, caches, warmups, and image components in one installed
  type-runtime contribution collection. F22 later removes the still-required Library Progress field and second provider
  path entirely in favor of optional plugin bindings.
- Presentation vocabulary is not treated as a runtime service or support fact; its remaining concrete-type map is
  visible Phase 5 projection work.
- Milestone 4.4 removes the last two-step installation mechanism. A binding is now consumed directly by both the graph
  and the generic operational provider index; no per-capability registration method remains.
- The generic provider index is not a feature coordinator. Existing facade policies exposed by compilation remain Phase
  5 obligations and must move to feature-owned integrations rather than into the index.
- F03 declares queue, status, count, worker, initialization, and notification consequences once and exposes one
  app-facing runtime contract; adding a Download provider requires no queue/UI/worker registration.
- F04 derives bookmarked bulk behavior from Download, Bulk Candidate, and Bookmark providers. A future Bookmark provider
  activates that consequence without editing the downloader or application presentation.
- F05 removes the artificial automatic-filter opt-in after proving every current implementation was the same shared
  policy. A future Download provider receives automatic-download policy without type-specific delegation.
- Application consumers use feature contracts for every F03–F05 operation. Remaining raw download access is explicitly
  eliminated by F06–F08; provider dispatch remains internal to the root feature coordinators.
- F06 derives bookmark protection from Download plus Bookmarking and keeps event delivery non-optional. F07 derives
  options and each specialized setting independently from provider presence. F08 covers every inventoried maintenance
  event, including cleanup before source/database purge.
- F09 centralizes consumption applicability and shared state-transition policy while type providers retain only genuine
  persistence differences; its F06 lifecycle consequence is emitted by the feature coordinator.
- F10 makes Bookmark provider presence the only support fact and derives both its application actions and existing
  Download intersections without another opt-in or type list.
- F13 removes the artificial universal provider declaration: Update Eligibility is an always-applicable shared feature
  for every composed type and owns one policy across update and Stats consumers.
- F15 distinguishes available empty progress from provider absence, routes all portable transfer through one feature,
  and leaves live media persistence, child labels, migration policy, and library summaries with their real owners.
- F16 removes concrete Anime authorization from playback-preference backup creation. Backup snapshot, restore, and
  migration copy now share one provider-derived Feature boundary with explicit data-absence, provider-absence, and
  type-mismatch outcomes.
- F17 makes Child List provider presence the sole list applicability fact, derives optional labels from Child List plus
  Child Progress, and removes the app-owned missing-count type gate without introducing fallback behavior.
- F14 applies one generic filter policy to every composed type, derives Bookmark-control and outside-release-period
  applicability independently, and removes the raw application support query without absorbing F13 or contextual
  Library navigation.
- F18 makes Child Group Filtering provider presence the sole applicability fact. Shared state, multi-member
  observation, live filtering, persistence, backup, and controls are Feature-owned without Manga authorization.
- F19 makes Preview provider presence the sole type-wide support fact, derives configuration, Child List, Open, and
  contextual source requirements independently, and removes raw Preview access and settings type enumeration.
- F20 makes Immersive provider presence the sole type-wide support fact, composes public source opt-in and runtime media
  context, derives Child List and Open relationships, and routes every application authorization and lifecycle path
  through one Feature boundary.
- F21 gives every composed type shared Related Entries orchestration while leaving `RelatedEntriesSource` and source
  orientation as contextual external truth. The Feature owns source availability, fetch/persistence, live Library state,
  and Entry/dialog consequences without filtering mixed authoritative returned types or coupling details navigation to
  F01 Open.
- F22 makes Library Progress provider presence optional and authoritative, removes the mandatory calculator/runtime
  path, and leaves unsupported Library entries structurally visible with explicit inapplicability.
- F22 owns common counts and merged summaries, derives Continue and Bookmark consequences from F02/F10, and carries
  unknown summary state truthfully through F14, sort, badges, Stats, and F13 update eligibility.
- F23 discovers type-owned vocabulary and imagery through ordinary plugin contributions, removes the application type
  map, keeps generic provenance explicit, and leaves every behavioral action with its owning Feature.
- F24 gives every composed type shared Library-update notification routing and rendering, derives child Open, Mark
  Consumed, and Download independently from F01/F09/F04, and consumes F23 only for vocabulary.
- F24 removes the Manga fallback for Book. A frozen compatibility adapter preserves shipped Manga/Anime Android
  identities; Book and future routes derive from content identity and are collision-validated.
- F25 moves Viewer Settings into the ordinary optional plugin/provider graph, validates exact app-owned screen
  projections, and derives hub/search, overrides, reset, backup, and migration consequences from the same provider
  definitions; F27 owns profile preference participation.
- F26 makes optional Media Cache provider presence sufficient for discovery, settings, preferences, manual clearing,
  launch clearing, size refresh, and structured error consequences.
- F26 removes the root cache-bucket list, central cache-key map, hardcoded settings/startup policy, and dedicated
  current-type preference holder. Type modules retain only genuine cache access and owned descriptors.
- F27 replaces the manually instantiated preference-owner checklist and tracker-ID loop with owner factories registered
  at their real DI/runtime installation boundary.
- F27 binds Entry runtime preference construction to a registered installer, discovers static and dynamic keys,
  rejects ambiguous ownership and late registration, and keeps named legacy corrections separate from support truth.

## Exact Next Action After Review

Begin F12.3 by adding the write-capable host transition, consequence journal, shared coordinator, and persistence adapter
conforming to accepted decision 0020. Use one agent and stop at the F12.3 milestone before consumer migration.
