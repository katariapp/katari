# Phase 3 — Core Type-Wide Capability Migration

## Objective

Migrate stable type-wide and provider-backed Entry capabilities to the authoritative foundation without leaving dual truth behind.

## Candidate Capability Groups

- [ ] Open and continue
- [ ] Consumption and partial progress
- [ ] Downloads and bulk downloads
- [ ] Merge and migration
- [ ] Child-group and library filtering
- [ ] Playback preferences
- [ ] Update eligibility

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
