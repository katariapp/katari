# Phase 2 — Bookmarking and Downloads Vertical Proof

## Objective

Prove end-to-end capability composition using the known Bookmarks and Downloads integration gap.

## Production Behavior to Preserve

- Manga supports bookmarks and bookmarked bulk downloads.
- Anime does not yet support bookmarks.
- Book does not yet support bookmarks.
- Bookmark-aware download cleanup keeps its current preference semantics.

## Milestone 2.1 — Bookmark Provider Authority

- [x] Represent bookmark mutation as its own operational provider registration rather than a boolean sub-capability on
  consumption providers.
- [x] Make bookmark provider registration contribute the fundamental Bookmarking evidence.
- [x] Register the Manga bookmark provider; leave Anime and Book without one and retain their explicit intentional
  absence outcomes.
- [x] Preserve the existing `EntryConsumptionInteraction` bookmark compatibility API by deriving its answers and dispatch
  from bookmark provider presence.
- [x] Remove unsupported Anime and Book bookmark no-ops.
- [x] Add focused registry and real-type coverage for positive, absent, duplicate, and contradictory bookmark authority.
- [x] Stop before derived download behavior changes.

## Milestone 2.2 — Shared Bookmark/Download Policy

- [x] Derive bookmarked bulk-download applicability from Bookmarking + Downloads.
- [x] Move generic bookmarked-child selection into shared download policy.
- [x] Remove downloader-specific bookmarked branches that exist only because bookmark support was absent.
- [x] Derive bookmark-protected cleanup applicability from the same capability evidence while preserving its preference
  semantics.
- [x] Surface any genuine specialized downloader requirement as an explicit obligation.
- [x] Add synthetic Anime bookmark-provider coverage without changing Anime download code.
- [x] Stop before application presentation migration.

## Milestone 2.3 — Application and Presentation Derivation

- [ ] Derive entry and Updates bookmark actions from Bookmarking.
- [ ] Derive bookmarked bulk-download action availability from Bookmarking + Downloads.
- [ ] Remove `downloadBookmarkedSupported` as behavioral authority.
- [ ] Preserve labels and icons as presentation-only metadata.
- [ ] Add application policy and presentation coverage driven by capability evidence.
- [ ] Stop before the Phase 2 integration gate.

## Milestone 2.4 — Vertical Contract and Integration Gate

- [ ] Add an end-to-end synthetic configuration that grants Anime bookmark support without changing Anime download
  implementation.
- [ ] Verify mutation dispatch, UI policy, candidate selection, cleanup policy, reporting, and applicable contracts all
  activate from that single registration.
- [ ] Verify current production support remains Manga-only.
- [ ] Remove superseded compatibility internals within the Phase 2 slice while retaining public compatibility APIs needed
  by later phases.
- [ ] Update the atlas and relevant public documentation projection or verification.
- [ ] Run the complete Phase 2 validation set and review every manifesto criterion for the vertical proof.
- [ ] Stop before Phase 3.

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

## Milestone 2.1 Completion Notes

`EntryBookmarkProcessor` registration is now the sole positive operational authority for Bookmarking. Consumption
registration proves only Consumption. Manga registers its existing persistence implementation as both providers; Anime
and Book register only consumption and retain their explicit, owned intentional-absence outcomes.

The existing `EntryConsumptionInteraction` bookmark methods remain as a compatibility facade. Support, mutation
eligibility, and dispatch derive from bookmark-provider presence, so adding a provider activates all three without a
second support boolean. Missing providers remain safely unavailable, and the unsupported Anime and Book mutation no-ops
have been removed.

No downloader, cleanup, application action, presentation flag, or public documentation behavior changed in this
milestone. Those consequences remain deliberately assigned to Milestones 2.2–2.4.

## Milestone 2.2 Completion Notes

Download processors now provide a media-specific candidate pool without interpreting the requested bulk action. The
shared registry policy owns Next, Unread, and Bookmarked selection. Bookmarked selection is applicable only when the
composed type report supports both Downloads and Bookmarking; otherwise it returns the existing structured Unsupported
result.

Manga, Anime, and Book no longer contain bookmark-specific downloader branches. A synthetic Anime bookmark provider
therefore activates bookmarked candidate selection without changing or wrapping Anime download behavior. No specialized
bookmark/downloader adapter was found necessary, so this combination has no missing specialized obligation.

Shared cleanup policy now protects bookmarked children only for types with authoritative Bookmarking support. The
existing remove-bookmarked preference continues to override that protection. Tests cover Manga production support,
unsupported Anime behavior, synthetic Anime support, and preference override semantics.

Application action visibility and `downloadBookmarkedSupported` remain unchanged for Milestone 2.3. Production behavior
therefore remains Manga-only at the UI boundary.
