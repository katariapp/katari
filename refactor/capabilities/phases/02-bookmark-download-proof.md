# Phase 2 — Bookmarking and Downloads Vertical Proof

## Objective

Prove end-to-end capability composition using the known Bookmarks and Downloads integration gap.

## Production Behavior to Preserve

- Manga supports bookmarks and bookmarked bulk downloads.
- Anime does not yet support bookmarks.
- Book does not yet support bookmarks.
- Bookmark-aware download cleanup keeps its current preference semantics.

## Scope

- [ ] Make bookmark support a single fundamental fact.
- [ ] Derive bookmark actions from bookmark support.
- [ ] Derive bookmarked bulk download from Bookmarks + Downloads.
- [ ] Move generic bookmarked-child selection into shared download policy.
- [ ] Derive bookmark-protected cleanup from the same capability evidence.
- [ ] Remove `downloadBookmarkedSupported` as behavioral authority.
- [ ] Remove per-type bookmarked-download opt-ins or unsupported branches that exist only because bookmarks are absent.
- [ ] Add an end-to-end synthetic capability test that grants Anime bookmark support without changing Anime download implementation.
- [ ] Verify that all common consequences activate in that test configuration.

## Non-Goals

- Do not add production Anime or Book bookmark persistence.
- Do not migrate unrelated capabilities.
- Do not redesign download transfer behavior.

## Exit Gate

- Bookmark support is represented once.
- UI, bulk selection, cleanup, tests, and reporting consume the same fact.
- Production support is unchanged.
- A synthetic Anime bookmark provider activates common bookmark/download behavior without an Anime download change.

## Validation

- Capability foundation tests
- Entry interaction registry tests
- Manga, Anime, and Book interaction tests covering current support
- Application tests for bookmark and download action availability
- Download lifecycle tests
- `./gradlew --quiet checkEntryInteractionBoundaries`
- `./gradlew --quiet :app:compileFossKotlin`
- `git diff --check`

## Manifesto Review

This phase passes only if it demonstrates: declare once, derive common consequences, surface exceptional work.
