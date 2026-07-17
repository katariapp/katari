# Phase 3 — Core Type-Wide Capability Migration

## Objective

Migrate stable type-wide and provider-backed Entry capabilities to the authoritative foundation without leaving dual truth behind.

## Candidate Capability Groups

- [ ] Open and continue
- [ ] Consumption and partial progress
- [x] Downloads and bulk downloads
- [ ] Merge and migration
- [ ] Child-group and library filtering
- [ ] Playback preferences
- [ ] Update eligibility

## Milestone Sequence

### Milestone 3.1 — Downloads and Bulk Downloads

- [x] Make download-provider registration authoritative for Downloads and Bulk Downloads.
- [x] Remove constant per-downloader bulk-support declarations.
- [x] Migrate Entry, Updates, Library, and library-update notification availability to the capability report.
- [x] Remove superseded download and generic-capability support methods.
- [x] Assert the Manga, Anime, and Book production matrix.
- [x] Stop before Open and Continue migration.

### Milestone 3.2 — Open and Continue

- [ ] Confirm provider registration is the only support authority.
- [ ] Migrate any support consumers and remove duplicate compatibility queries.
- [ ] Assert the production matrix and stop before Consumption and Progress.

### Milestone 3.3 — Consumption and Progress

- [ ] Separate type-wide capability authority from per-child mutation eligibility and progress state.
- [ ] Migrate generic consumers and remove duplicate support facts.
- [ ] Assert the production matrix and stop before Merge and Migration.

### Milestone 3.4 — Merge and Migration

- [ ] Replace constant capability processors with authoritative evidence and shared selection policy.
- [ ] Preserve accepted Manga/Anime support and Book absence.
- [ ] Migrate consumers, correct focused internal projections, and stop before filtering.

### Milestone 3.5 — Child-Group and Library Filtering

- [ ] Remove Anime's misleading no-op child-group provider and retain explicit intentional absence.
- [ ] Make operational or intrinsic filtering evidence authoritative.
- [ ] Migrate consumers, assert the production matrix, and stop before Playback Preferences.

### Milestone 3.6 — Playback Preferences

- [ ] Confirm optional provider registration as authority and migrate type-wide consumers.
- [ ] Preserve media-specific snapshot and restore behavior.
- [ ] Assert the production matrix and stop before Update Eligibility.

### Milestone 3.7 — Update Eligibility and Phase Gate

- [ ] Replace equivalent type processors with shared universal Entry policy unless a real specialization is found.
- [ ] Migrate consumers and verify the production matrix.
- [ ] Run the complete Phase 3 exit gate and stop before Phase 4.

Split this phase into smaller milestones in `status.md`. Migrate one coherent capability group at a time.

## Per-Capability Checklist

- [ ] Confirm the Phase 0 expected state.
- [ ] Identify the single evidence source.
- [ ] Add or update capability matrix tests.
- [ ] Migrate feature consumers.
- [ ] Remove duplicate support flags or constant processors made obsolete by the migration.
- [ ] Verify Manga, Anime, and Book behavior.
- [ ] Update the atlas and status.
- [ ] Stop before starting the next capability group.

## Non-Goals

- Do not migrate source-, entry-, selection-, or tracker-dependent capabilities in this phase.
- Do not remove compatibility APIs still used by unmigrated consumers.
- Do not move genuine media behavior into the generic capability layer.

## Exit Gate

- Migrated capabilities have one evidence source.
- Generic consumers use the capability query surface.
- Presentation contains no migrated behavioral support flags.
- Runtime and reviewed product matrices agree.
- Compatibility paths remain only for unmigrated scope.

## Validation

- Focused owner and consumer tests per milestone
- All Entry interaction module unit tests at phase completion
- Relevant application tests
- `./gradlew --quiet checkEntryInteractionBoundaries`
- `./gradlew --quiet :app:compileFossKotlin`
- `./gradlew --quiet spotlessCheck`
- `git diff --check`

## Manifesto Review

Confirm that each migration reduces facts a contributor must remember and does not merely relocate them.

## Milestone 3.1 Completion Notes

Registering an `EntryDownloadProcessor` is now the sole positive authority for both Downloads and Bulk Downloads. Every
download provider is already required to supply the media-specific bulk candidate pool, so the three constant
`supportsBulkDownload` implementations were duplicate declarations rather than genuine specialization. Registration
contributes both capability facts, while intentionally reduced compositions without a provider report both as unresolved.

Entry child actions and Updates rows query Downloads from the composed report. Entry and Library bulk menus plus the
library-update notification action query Bulk Downloads from the same report, retaining their existing local-source,
selection, and queue-size contextual restrictions. The download registry also checks the report before shared bulk
selection.

The superseded `EntryDownloadInteraction.supportsDownloads`, `EntryDownloadInteraction.supportsBulkDownload`, and
`EntryCapabilityInteraction.supportsBulkDownload` methods have been removed. Bookmarked bulk downloads now compose the
more precise Bulk Downloads + Bookmarking facts. No downloader, presentation object, or application consumer keeps a
parallel type-support boolean.

Production composition and real Manga, Anime, and Book plugin tests assert both capabilities supported for every current
type. All Entry interaction module tests, focused application policy tests, the FOSS application compilation, boundary
checks, Spotless, and diff validation passed. Production behavior and public documentation are unchanged.
