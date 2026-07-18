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
- Latest earlier production migration: `e04b2481c` (`(refactor): derive download capabilities from providers`)
- Phase 2 completion: `918fcc4d3` (`(refactor): complete bookmark download capability proof`)
- Always verify `HEAD`, the working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 5 — Feature Integration Migration
- Milestone: `F02` — Continue
- State: Architecture Gate 5.0 approved; F02 not started

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
- [x] Core downloads separated from options, settings, bulk candidates, and automatic filtering
- [x] Download support contributed through capability-owned bindings for Manga, Anime, and optional Book construction
- [x] Anime alone contributes interactive options; Manga contributes each implemented download setting independently
- [x] Manga, Anime, and Book downloaders contribute bulk-candidate and automatic-filter providers independently
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
- Milestone 4.3 production and refactor-workspace changes are uncommitted and awaiting review.
- Manga, Anime, and Book plugins expose their operational `EntryType` and one owned `ContentTypeContribution`.
- Open and Continue are the first graph-backed provider contracts. Neither is mandatory, no explicit unsupported
  declaration exists, and type plugins contain no separate Open/Continue registry call.
- `T01`–`T22` and `T24`–`T27` now use one owned contribution/runtime boundary or deliberate shared policy. `T23` is an
  explicit Phase 5 presentation-projection obligation.
- No dummy feature contribution or compatibility reachability path has been added. Production graph assembly remains
  blocked until real feature owners contribute relationships in their owning phase.

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
- Architecture Gate 5.0 makes feature ownership an enforceable dependency direction rather than a naming preference.
- Application consumers can see feature contracts and shared models but cannot see raw provider dispatch, the
  `EntryInteractions` aggregate, Feature Graph evaluation, or selected artifacts.
- SPI declarations are discovered by the boundary checker, so a future provider/dispatch type is protected without
  adding its name to an enforcement list.
- Remaining application failures are the expected migration queue for F02–F27. Re-exporting SPI, restoring raw DI, or
  moving provider facades back into the API would contradict the manifesto rather than fix compilation.
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
- Core downloads imply neither options, settings, bulk candidates, nor automatic filtering. Book remains valid when its
  optional downloader is omitted.
- Each Manga download setting is an individual graph-visible provider claim, not an enum set treated as another support
  authority.
- Shared bulk selection and automatic-download orchestration remain feature-owned Phase 5 work; type providers expose
  only the genuinely media-specific operations those features need.
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
- Milestone 4.3 removes the separate root lists for plugins, library progress, settings, caches, warmups, and image
  components. Each list is derived from the one installed type-runtime contribution collection.
- Presentation vocabulary is not treated as a runtime service or support fact; its remaining concrete-type map is
  visible Phase 5 projection work.
- Milestone 4.4 removes the last two-step installation mechanism. A binding is now consumed directly by both the graph
  and the generic operational provider index; no per-capability registration method remains.
- The generic provider index is not a feature coordinator. Existing facade policies exposed by compilation remain Phase
  5 obligations and must move to feature-owned integrations rather than into the index.

## Exact Next Action After Review

Inventory and migrate `F02` Continue behind a feature-owned application contract. Stop after the complete F02 consumer
disposition and manifesto review; do not begin `F03`.
