# Feature Architecture Remediation Findings

## Purpose

This document preserves the post-migration audit that followed completion of the initial Entry Feature architecture.
It records the approved gaps that remain; it is not a replacement for
[`../capability-manifesto.md`](../capability-manifesto.md), which remains the architectural authority.

Audit baseline:

- Branch: `features-arch-refactor`
- Commit: `1d962d406` (`chore: planning cleanup`)
- Audit date: 2026-07-22

## Root Cause

The Feature Graph can currently declare a `SharedFeatureConsequence` and evaluate where it applies, but the consequence
does not bind an executable runtime participant. Runtime delivery is still completed through handwritten dependency
lists, application call sequences, or `when (artifactId)` dispatch.

This means the graph can report that a consequence exists without proving that the application executes it. The
architecture therefore still permits the exact failure mode rejected by the manifesto: a developer can finish a
Feature while forgetting a related lifecycle, persistence, UI, or background integration.

## Approved High-Confidence Gaps

### Library membership lifecycle

Library addition and removal are independently orchestrated by Entry, History, Catalogue, and Library screen models.
Those paths manually combine duplicate detection, category selection, default child state, Merge lifecycle, Tracking
binding, custom-cover cleanup, favorite persistence, and Download cleanup.

The category-selection paths demonstrate current drift: automatic Tracking binding can run before the user confirms
Library membership, while the actual favorite mutation happens later.

`EntryRemovalCleanupInteraction` is an app-local interaction boundary that handles only custom covers and sits beside
Feature-owned APIs. It must disappear when membership lifecycle consequences are contributed through the new
architecture.

### Catalogue execution

`EntryCatalogueFeature` owns catalogue availability and description, but application and data code still dispatch raw
source operations for search, popular/latest paging, filter resolution, global search, and Migration search.

The Catalogue Feature must own these operations so application consumers cannot use raw catalogue providers as an
alternative API.

### Backup and restore

Backup creation and restoration explicitly inject and call every Feature state integration known at the time. A new
Feature can contribute persistent Entry state to the graph and still be silently absent from backups.

Anime Download preferences are a concrete boundary leak: generic backup code understands their video-specific storage
schema rather than receiving a portable state snapshot from the owning Feature.

Backup tracker diagnostics also contain a current defect: validation enumerates only legacy Manga containers instead
of using the unified `Backup.allEntries()` projection, omitting generic, Anime, and some profile-scoped entries.

The R5 implementation inventory found four distinct layers that had to remain separate:

- `BackupCreator` and `BackupRestorer` own file/profile orchestration, user-selected sections, progress reporting, and
  compatibility with the top-level and profile-scoped containers. They must not know which Features persist Entry
  state.
- `EntryBackupCreator` and `EntryRestorer` own the Entry row plus the existing chapter, history, category, and fetch
  interval wire semantics. Before R5 they also formed a handwritten Feature list for Viewer Settings, Playback
  Preferences, Progress, child-group filters, Merge, Tracking, and Download Options; that list was the R5 target.
- `BackupEntry` is the current protobuf compatibility surface. Its typed Feature fields must remain readable while an
  additive repeated envelope carries a stable participant ID, participant-owned schema version, and opaque payload.
  The profile container already embeds `BackupEntry`, so one Entry-level addition covers ordinary and profile backups.
- Legacy Manga and Anime containers normalize through `toBackupEntry()` and therefore enter the same restore path.
  Their typed state is compatibility input, not current Feature-discovery evidence.

The Feature-state ownership and selection semantics entering R5 were:

- Viewer Settings, Playback Preferences, child-group filters, and Merge are always considered when an Entry is backed
  up; their Feature boundaries decide whether state exists or applies.
- Progress and Download Options are included only with the existing `chapters` backup selection. Chapter/history data
  remains core wire state, while Progress consumes those fields only as a legacy fallback.
- Tracking was included only with the existing `tracking` selection. Its snapshot and restoration bypassed the
  Tracking Feature and directly queried or mutated `entry_sync`.
- Download Options bypassed the Download Configuration Feature and directly used
  `DownloadPreferencesRepository`; generic backup code also owned the video quality string mapping. The Download
  Configuration module therefore had to own this portable snapshot, codec, and restore behavior.
- Merge restoration is intentionally deferred until all Entries in a destination profile exist. Discovered restore
  participation must preserve that finalize-after-entry-batch boundary rather than trying to restore each group while
  its referenced members may be absent. Deferred state belongs to the caller-owned restore session, not to the
  application composition: an interrupted or concurrent restore must not leave or share mutable pending groups in a
  production singleton.

R5 compatibility rules are therefore:

- Snapshot participants are discovered through the production Feature-module installation; Entry backup orchestration
  receives opaque envelopes and never switches on current Feature IDs.
- Restore participants prefer an envelope they own. A compatibility adapter may translate the finite set of existing
  typed fields into those same participant payloads only when the corresponding envelope is absent. This adapter is a
  legacy wire bridge, not a Feature completion list, and a future Feature needs no change to it.
- Current known state is projected back into typed fields while older releases need it. That dual-write projection is
  likewise compatibility-only and cannot decide whether a participant runs.
- Unknown envelopes remain decodable and do not invalidate or block restoration of the Entry or known envelopes.
- Participant schema versions are interpreted by the owning participant. The generic coordinator validates envelope
  identity/duplication but does not understand payload schemas.
- Existing Tracking records retain their local title, status, score, dates, URL, privacy, and total during restore;
  backup identity fields and the maximum progress are merged exactly as before the architectural migration.
- Backup diagnostics must derive Tracking service IDs from `Backup.allEntries()`, which includes current generic,
  legacy Manga, legacy Anime, and profile-scoped Entries.

### Catalogue execution

The R6 execution audit found that Catalogue description already has a Feature authority, but Catalogue behavior still
bypasses it through several parallel paths:

- `CatalogSourceRepositoryImpl` and the three data paging sources resolve `EntryCatalogueSource` from `SourceManager`
  and dispatch popular, latest, and search operations directly.
- `CatalogFilterLoader` resolves raw filter contracts and source-compat asynchronous filters outside the Feature.
- `GlobalSearchScreenModel` and the older `SearchScreenModel` independently enumerate sources, choose background
  filters, dispatch first-page searches, normalize exceptions, deduplicate Entries, and persist matches.
- Manual Migration search repeats the global-search path, while `SmartSourceSearchEngine` and
  `MigrationListScreenModel` form a second direct Migration-search path with different candidate-persistence behavior.
- `CatalogScreenModel`, chronological feeds, feed presentation, Migration configuration, browse-action settings, and
  Anime download-cache recovery ask `SourceManager` or a raw `CatalogSource` wrapper to decide Catalogue availability.
- `SourceManager.getCatalogueSource(s)` and `UnifiedSource.getPopularContent/getLatestUpdates/getSearchContent` leave
  the raw dispatch path available to any future application consumer, even after existing callers are migrated.

R6 must preserve two distinctions:

- Catalogue paging and interactive global/manual search persist returned Entries through `NetworkToLocalEntry`, while
  automatic smart Migration search keeps candidates ephemeral until it selects a target. Moving dispatch behind the
  Feature must not silently persist every smart-search candidate.
- General source lookup used by readers, downloads, tracking, source refresh, and other Feature adapters is not
  Catalogue behavior. R6 removes Catalogue-specific lookup and execution from those consumers without turning
  `EntryCatalogueFeature` into a catch-all source registry.

The target boundary therefore exposes Feature-owned source facts and normalized Catalogue operations by source ID.
Raw `EntryCatalogueSource`, filter compatibility helpers, and provider page calls remain confined to a Feature host
adapter. Application and data consumers receive Feature models/results and cannot regain the old execution path through
`UnifiedSource`, `CatalogSource`, or Catalogue-specific `SourceManager` methods.

### Download policy and context

Generic bulk Download selection depends on `MangaReaderSettingsProvider.skipFiltered`, allowing a Manga reader setting
to control Anime and Book behavior.

Download source-access context is also reconstructed by several UI, notification, and worker consumers. The existing
Download Feature must resolve this context and own generic bulk selection policy.

Approved product policy: bulk Download actions operate on the currently visible filtered children. They must not be
controlled by the Manga reader preference.

### Entry lifecycle operations

Metadata changes, destructive deletion, and Profile movement remain fixed integration lists:

- `EntryMetadataUpdateHooks` hardcodes only Download title maintenance.
- Clear Database remembers Download cleanup and database deletion but exposes no general removal consequence point.
- `EntryProfileMoveService` knows the current profile-scoped tables and separately invokes Merge behavior.

These are separate cohesive lifecycle operations, not one catch-all service. Each must expose discovered transactional,
post-commit, or durable participants as appropriate.

The R4 implementation inventory identified the following concrete ownership split:

- Metadata title changes are persisted by source synchronization. Download Maintenance is the only current external
  participant, but it must join through an after-commit Metadata event rather than an app hook that names Downloads.
- Destructive deletion is currently performed by Clear Database. Entry-row deletion and relational cascade are core
  persistence; Download artifacts and custom-cover files are independently owned post-commit consequences. Merge must
  transactionally preserve its grouping invariants before the rows are deleted.
- Profile movement owns the Entry row and destination Library category assignment. Tracking state, child-group filter
  state, cover-hash state, and Merge membership are independent transactional participants. Source visibility,
  Download maintenance, and custom-cover cleanup are post-commit consequences and must not be treated as rollback-safe
  database work.
- Conflict resolution during Profile movement can remove a source or destination Entry. Those removals are part of the
  Profile-move event and must be visible to its participants; they must not become an undiscoverable second destructive
  deletion workflow.

The existing foreign-key cascades remain valid persistence mechanics. They do not replace lifecycle participation for
external state or for Feature invariants that require more than deleting rows directly referencing an Entry.

### Custom covers

Custom-cover behavior participates in Library removal, Merge, Migration, Profile movement, and destructive deletion.
It will remain a host-owned contributed consequence for now, not a standalone user-facing Feature or content-type
capability. This decision can be revisited if custom covers later need independent availability, contracts, or product
policy.

## Approved Non-Gap

Stored child-state filters remain generic. Filtering on Downloaded, Consumed, or Bookmarked state does not require the
corresponding mutation provider to exist for the current content type. No capability gate will be added to those
filters as part of this remediation.

## Secondary Findings Requiring Later Verification

These were discovered but were not included in the approved primary migration scope:

- WebView declares navigation, sharing, assist, URL, and header consequences, while its contract primarily validates
  URL and header resolution and UI wiring remains manual.
- Source Refresh evaluates source availability but exposes no separate availability projection to presentation.
- Viewer-settings screen projections are manually composed and may fail only during runtime construction.
- Production Feature contributors are installed through a central list, and omission detection must distinguish a
  legitimate composition root from an accidental second completion list.

They must be re-audited during the final enforcement phase rather than silently absorbed into an earlier migration.

## Investigated Legitimate Boundaries

The following are not evidence of missing Feature ownership by themselves:

- Legacy Manga and Anime backup wire-format conversion.
- Tracker-owned supported Entry types.
- Type-specific debug tools.
- Source compatibility adapters.
- Generic Entry notes, categories, display names, and fetch-interval editing.
- Stored child-state filtering described above.
