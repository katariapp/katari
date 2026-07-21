# Production Contract Migration Census

Updated: 2026-07-21

## Purpose

This census is the Phase 7.1 control surface for migrating production behavioral validation. It is derived from every
production `FeatureContribution`, its integrations, the selected contract declarations, and the existing behavioral
test suites. It is not a contract allowlist or capability matrix.

The table must disappear as an active completion mechanism after migration. Normal validation will discover contracts,
verifiers, scenarios, and production subjects directly. This file records why existing tests migrate or remain
owner-local so the transition does not omit behavior merely because no marker contract exists today.

## Census Baseline

- There are 37 production Feature contributions under `entry-interactions`.
- Fifteen Features currently declare 18 contract definitions. Migration declares four distinct contracts; each other
  declaring Feature has one.
- Four declared contracts are contextual: Related Entries, Preview, Immersive, and Source Refresh. The other fourteen
  are statically selected.
- Twenty-two Features have behavioral suites but no contract definition.
- No production validation contributor, executable verifier, applicable-context scenario, or contract fixture exists.
- Six suites inspect selected contract identifiers or subjects without executing the declared contract: Library
  Filtering, Library Progress, Media Cache, Merge, Migration, and Update Eligibility.
- The existing declarations do not cover every relationship already exercised by their owner suites. Several contracts
  are attached only to a base/provider integration while optional provider combinations and contextual consequences
  have no selected contract.

If the validation gate were connected without further migration, it would correctly report 18 missing verifier
obligations and four missing applicable-context scenario groups. That result would still be incomplete because the 22
Features without declarations and the undeclared cooperation/context relationships would remain invisible.

## Migration Progress

Phase 7.1.1 raises the production graph to 32 exact contract definitions across 23 Features. Sixteen definitions now
have discovered executable verifiers: Open, Continue, Consumption provider/context behavior, Bookmarking
provider/context behavior, Progress Transfer, Playback Preferences Transfer, Child List, Child Group Filtering, Type
Presentation, and Update Eligibility policy/context behavior.

The production host enrolls every applicable subject automatically. Provider contracts execute the shared Feature
coordinator against a recording operational boundary selected from the production provider's type; they do not retest
media-specific processor mechanics. Context contracts execute the same shared coordinator after their feature-owned
applicable scenarios resolve. The declaration-only Update Eligibility assertion was removed; its policy behavior remains
covered by executable contracts and focused policy tests.

Phase 7.1.2 raises the production graph to 56 exact contract definitions across 29 Features. All 24 Download-owned
definitions now have executable verifiers, including eleven applicable contextual scenario groups. Runtime, Actions,
Automatic Downloads, Lifecycle, Configuration, and Maintenance execute their shared coordinators for every applicable
production subject. Bookmark protection is owned by Download Lifecycle; notification action availability is owned by
Download Actions. Notification event rendering and type-owned downloader, store, cache, and transfer mechanics remain
in their focused owner suites.

Phase 7.1.3 raises the production graph to 77 exact contract definitions across 29 Features. Sixty-nine definitions now
have executable verifiers, including 23 applicable contextual scenario groups. The 29 Library, Settings, and Media
contracts cover Filtering, Progress, Update Notifications and Refresh, Preview, Immersive, Viewer Settings, and Media
Cache. Preview and Immersive execute their universal coordinators through recording processors and receive Child List
only on the relationship that selects it, so neither Feature needs a per-type media fixture.

Phase 7.1.4 raises the production graph to 89 exact contract definitions across 36 Features. Eighty-one definitions now
have executable verifiers, including 35 applicable contextual scenario groups. The twelve Source and External-Context
contracts cover Catalogue description/catalogue/latest projection, Related Entries, Cover Network, Source Settings,
Source Home, Source Refresh, Entry/child WebView, Deep Link, and Tracker Source Adapter. Each verifier executes its
shared coordinator from feature-owned applicable evidence; public source contracts remain the external facts that
produce that evidence.

## Validation Host Gate

Production verifier migration must not begin by rebuilding Manga, Anime, Book, or the Feature contributor list in a
test. The validation host must consume the same `EntryInteractionComposition` installed by the runtime environment:

- Extract the production Feature contributor installation boundary so runtime composition and validation use one list.
- Build the production content-type plugins through the existing Manga, Anime, and Book runtime modules with a JVM test
  host supplying controlled dependencies. Do not create synthetic per-type provider lists for shared contracts.
- Bind a verifier to the exact production contract definition, not a copied string identifier. Existing private
  contract definitions must become owner-visible to their validation source without becoming application APIs.
- Discover validation contributors from their module-local service metadata. The service descriptor installs
  contributors; it does not decide applicability, which remains graph-selected.
- Feed the resulting production graph/evaluation into `feature-validation`. The validation module must remain absent
  from runtime application dependencies.
- Expose one validation success result covering graph issues, validation issues, verifier failures, and verifier
  crashes.

Phase 7.1.0 replaced `FeatureContractReference(feature, contractId)` with an identity reference to the exact definition.
A copied definition carrying the same identifier is rejected because it never referenced the production declaration it
claims to implement.

## Feature Disposition

“Add” means the existing owner suite proves shared behavior that is not represented by a production contract. “Extend”
means a current base contract remains but additional integration-owned expectations must become selected contracts.
“Retain” means the current declaration already sits on the relationship that owns the behavior, although its suite must
still become an executable verifier.

### Fundamental navigation, state, and presentation

| Feature | Current declaration | Selection shape | Migration disposition | Existing behavior owner |
| --- | --- | --- | --- | --- |
| Open | None | Open provider | Add provider dispatch, applicability, and action construction contract | `EntryOpenFeatureTest` |
| Continue | None | Continue provider | Add next-target and dispatch contract | `EntryContinueFeatureTest` |
| Consumption | None | Provider plus transition, mutation, and lifecycle context | Add provider contract and contextual transition/mutation contracts | `EntryConsumptionFeatureTest` |
| Bookmarking | None | Provider plus selection and mutation context | Add provider contract and contextual selection/mutation contracts | `EntryBookmarkFeatureTest` |
| Progress Transfer | `entry.progress-transfer.behavior` | Progress provider | Retain provider contract; keep Migration cooperation in the Migration owner | `EntryProgressFeatureTest` |
| Playback Preferences Transfer | None | Playback-preferences provider plus Migration cooperation | Add provider transfer contract and Migration-owned cooperation contract | `EntryPlaybackPreferencesFeatureTest`, `EntryMigrationFeatureTest` |
| Child List | None | Child-list provider | Add ordering/selection contract | `EntryChildListFeatureTest` |
| Child Group Filtering | None | Child-group provider | Add filtering contract | `EntryChildGroupFilterFeatureTest` |
| Type Presentation | None | Presentation provider | Add vocabulary/projection contract that never authorizes behavior | `EntryTypePresentationFeatureTest` |
| Update Eligibility | `entry.update-eligibility.behavior` | Unconditional policy plus decision context | Retain policy contract and add contextual decision contract | `EntryUpdateEligibilityFeatureTest` |

### Downloads

| Feature | Current declaration | Selection shape | Migration disposition | Existing behavior owner |
| --- | --- | --- | --- | --- |
| Download Runtime | None | Download provider | Add state, queue, dispatch, and structured absence contract | `EntryDownloadRuntimeFeatureTest` |
| Download Actions | None | Download/bulk/bookmark providers plus five action contexts | Add independent provider-combination and applicable-action scenario contracts | `EntryDownloadActionFeatureTest` |
| Automatic Downloads | None | Download provider plus policy/selection context | Add provider and applicable policy-coordination contracts; retain focused blocker tests | `EntryAutomaticDownloadFeatureTest` |
| Download Lifecycle | None | Download provider plus cleanup/protection contexts | Add event acceptance, provider dispatch, cleanup, and Bookmark cooperation contracts | `EntryDownloadLifecycleFeatureTest` |
| Download Configuration | None | Download-options providers and configuration relationships | Add settings/options contract | `EntryDownloadConfigurationFeatureTest` |
| Download Maintenance | None | Download provider | Add shared maintenance contract; Bookmark protection remains owned by Download Lifecycle | `EntryDownloadMaintenanceFeatureTest` |

Download notification manager/job tests remain coordinator behavior tests. Downloader, store, cache, and media transfer
tests remain type-owned mechanics unless a shared contract later declares a genuine media fixture requirement.

### Library, settings, and media surfaces

| Feature | Current declaration | Selection shape | Migration disposition | Existing behavior owner |
| --- | --- | --- | --- | --- |
| Library Filtering | `entry.library-filtering.behavior` | Unconditional participation plus policy, Bookmark, Progress, and release-period relationships | Retain participation contract; add relationship contracts instead of one matrix-style suite | `EntryLibraryFilterFeatureTest` |
| Library Progress | `entry.library-progress.behavior` | Summary provider plus Continue and Bookmark cooperation | Retain summary contract; add Continue and Bookmark cooperation contracts | `EntryLibraryProgressFeatureTest` |
| Library Update Notifications | `entry.library-update-notifications.behavior` | Unconditional notification plus presentation, Open, Consumption, Download, and child contexts | Retain base rendering contract; add action/cooperation contracts to their owning relationships | `EntryLibraryUpdateNotificationFeatureTest` |
| Library Update Refresh | `entry.library-update-refresh.behavior` | Unconditional shared refresh handoff | Retain | `EntryLibraryUpdateRefreshFeatureTest` |
| Preview | `entry.preview.behavior` | Preview provider, source/preference context, configuration, Child List, and Open | Retain contextual behavior contract; add provider/configuration/Child List/Open cooperation contracts | `EntryPreviewFeatureTest` |
| Immersive | `entry.immersive.behavior` | Immersive provider, source/entry context, Child List, Source Refresh, and Open | Retain contextual behavior contract; add provider and cooperation contracts | `EntryImmersiveFeatureTest` |
| Viewer Settings | `entry.viewer-settings.behavior` | Viewer-settings provider plus Migration cooperation | Retain provider/projection contract; add Migration cooperation contract | `EntryViewerSettingsFeatureTest` |
| Media Cache | `entry.media-cache.behavior` | Media-cache provider | Retain | `EntryMediaCacheFeatureTest` |

Preview and Immersive do not require media fixtures. Their shared coordinator behavior executes against recording
processors, while graph-selected Child List providers enter only the child-backed relationship contracts. Renderer,
decoder, player, and image-loading mechanics remain type-owned behavior rather than fixture-shaped contract opt-ins.

### Source and external context

| Feature | Current declaration | Selection shape | Migration disposition | Existing behavior owner |
| --- | --- | --- | --- | --- |
| Catalogue | None | Catalogue/latest/description contexts | Add contextual projection and blocker contracts | `EntryCatalogueFeatureTest` |
| Related Entries | `entry.related-entries.behavior` | Installed/source-support context | Retain | `EntryRelatedEntriesFeatureTest` |
| Cover Network | None | Source/client context | Add contextual selection contract | `EntryCoverNetworkFeatureTest` |
| Source Settings | None | Source/settings context | Add contextual availability/operation contract | `EntrySourceSettingsFeatureTest` |
| Source Home | None | Source/home context | Add contextual navigation contract | `EntrySourceHomeFeatureTest` |
| Source Refresh | `entry.source-refresh.behavior` | Installed-source context | Retain | `EntrySourceRefreshFeatureTest` |
| Entry WebView | None | Source/child context plus specialized media host | Add contextual contract that preserves the delayed host-adapter obligation | `EntryWebViewFeatureTest` |
| Deep Link | None | Source-resolution context | Add contextual resolution/persistence contract | `EntryDeepLinkFeatureTest` |
| Tracker Source Adapter | None | Tracker/source settings, home, and image-client context | Add contextual adapter contract | `EntryTrackerSourceAdapterFeatureTest` |

Source SDK compatibility, source implementations, network/media failures after applicability, and legacy adapter ABI
tests remain owner-local. Context scenarios describe applicable validation snapshots; they do not declare unconditional
source support.

### Shared workflows and external services

| Feature | Current declaration | Selection shape | Migration disposition | Existing behavior owner |
| --- | --- | --- | --- | --- |
| Merge | `entry.merge.behavior` | Unconditional workflow plus Download, Migration, preparation, execution, lifecycle, backup, and profile context | Retain base workflow contract; add cooperation/context contracts for the independently owned paths | `EntryMerge*Test` |
| Migration | Four base/progress/viewer-settings/download contracts | Migration provider plus optional providers and preparation/execution context | Retain four declarations; add missing Consumption, Bookmark, Playback Preferences, options, preparation, and execution contracts | `EntryMigration*Test` |
| Tracking | `entry.tracking.behavior` | Unconditional registry plus availability, session, automation, synchronization, Library, Stats, and Migration relationships | Retain registry contract; add contextual/external relationship contracts | `EntryTracking*Test` |

Backup wire conversion, database mapping, transaction compatibility, and durable consequence-delivery tests remain
ordinary owner tests. Shared workflow contracts validate selection, authorization, coordination, and structured results
without absorbing storage compatibility into the capability graph.

## Tests to Remove or Retain

Remove only assertions that restate a selected contract identifier or current subject after the executable verifier
covers the behavior. The surrounding behavior tests are migration input, not wholesale deletion candidates.

Retain independently:

- feature-graph and feature-validation kernel semantics;
- type-owned provider/media algorithms;
- legacy source and backup compatibility;
- serialization, database, storage, cache, and download-transfer mechanics;
- notification coordinator event handling after Feature selection;
- runtime failures that occur after applicability; and
- focused UI rendering tests that consume an already selected Feature projection without deciding support.

The Manga/Anime provider-behavior test helpers currently create synthetic test Features to satisfy graph reachability.
Replace those helpers with the production validation host where shared contract execution covers them; retain only
genuine media behavior assertions in the type modules.

## Migration Sequence

### 7.1.0 — Production Validation Host and Exact Binding

- [x] Bind verifiers/scenarios to exact production definitions.
- [x] Reuse the runtime Feature contributor installation and actual type-module contributions.
- [x] Establish module-local validation contributor discovery and one normal validation entry point.
- [x] Prove the unmodified production graph reports all currently missing verifier/scenario obligations before adding the
  first implementation.

### 7.1.1 — Fundamental Provider Contracts

- [x] Open, Continue, Consumption, Bookmarking, Progress, Playback Preferences, Child List, Child Group Filtering, Type
  Presentation, and Update Eligibility.
- [x] Remove only replaced contract-ID/support assertions.

### 7.1.2 — Download Contracts

- [x] Runtime, Actions, Automatic Downloads, Lifecycle, Configuration, Maintenance, and their Bookmark/notification
  cooperation.
- [x] Keep notification manager and type-owned downloader mechanics separate.

### 7.1.3 — Library, Settings, and Media Contracts

- [x] Library Filtering, Library Progress, Library Update Notifications/Refresh, Preview, Immersive, Viewer Settings, and
  Media Cache.
- [x] Decide typed Preview/Immersive fixtures from actual verifier requirements, never from current type enumeration.

### 7.1.4 — Source and External-Context Contracts

- [x] Catalogue, Related Entries, Cover Network, Settings, Home, Refresh, WebView, Deep Link, and Tracker Source Adapter.
- [x] Preserve public source contracts as external context authority.

### 7.1.5 — Workflow and Tracking Contracts

- Merge, Migration, and Tracking base/cooperation/context relationships.
- Keep wire/storage/transaction compatibility suites outside shared contracts.

### 7.1.6 — Contract Reconciliation

- Verify every production contract has exactly one discovered verifier and every contextual contract has an applicable
  scenario.
- Verify every selected production subject executes and one unresolved issue makes the aggregate result unsuccessful.
- Remove remaining declaration-only assertions and synthetic provider-support Features.
- Re-run the migration-inventory test register before developer reporting begins.

## Manifesto Check

This sequence does not make contracts mandatory for every possible integration. It adds contracts where current shared
behavior already establishes an observable expectation, and keeps genuine media/compatibility mechanics with their
owners. Production applicability selects subjects; neither this census nor a test suite names the types that must run.

The validation host and exact-definition gate are required to prevent the census, service metadata, copied identifiers,
or synthetic test Features from becoming a second support authority.
