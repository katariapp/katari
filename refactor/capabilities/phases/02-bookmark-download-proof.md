# Phase 2 — Bookmarking and Downloads Vertical Proof

> Historical status: complete as a learning slice, not as the general architecture. It proved useful product rules but
> did not prove discovery of unknown future types, capabilities, features, obligations, contracts, or projections.

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

- [x] Derive bookmarked bulk-download applicability from Bookmarking + Bulk Downloads.
- [x] Move generic bookmarked-child selection into shared download policy.
- [x] Remove downloader-specific bookmarked branches that exist only because bookmark support was absent.
- [x] Derive bookmark-protected cleanup applicability from the same capability evidence while preserving its preference
  semantics.
- [x] Surface any genuine specialized downloader requirement as an explicit obligation.
- [x] Add synthetic Anime bookmark-provider coverage without changing Anime download code.
- [x] Stop before application presentation migration.

## Milestone 2.3 — Application and Presentation Derivation

- [x] Derive entry and Updates bookmark actions from Bookmarking.
- [x] Derive bookmarked bulk-download action availability from Bookmarking + Bulk Downloads.
- [x] Remove `downloadBookmarkedSupported` as behavioral authority.
- [x] Preserve labels and icons as presentation-only metadata.
- [x] Add application policy and presentation coverage driven by capability evidence.
- [x] Stop before the Phase 2 integration gate.

## Milestone 2.4 — Vertical Contract and Integration Gate

- [x] Add an end-to-end synthetic configuration that grants Anime bookmark support without changing Anime download
  implementation.
- [x] Verify mutation dispatch, UI policy, candidate selection, cleanup policy, reporting, and applicable contracts all
  activate from that single registration.
- [x] Verify current production support remains Manga-only.
- [x] Remove superseded compatibility internals within the Phase 2 slice while retaining public compatibility APIs needed
  by later phases.
- [x] Update the atlas and relevant public documentation projection or verification.
- [x] Run the complete Phase 2 validation set and review every manifesto criterion for the vertical proof.
- [x] Stop before Phase 3.

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

The later architecture review found that this criterion was necessary but insufficient. Phase 3 must now prove generic
participation and obligation discovery before this feature-specific policy can be treated as a migration pattern.

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

## Milestone 2.3 Completion Notes

Entry and Updates bookmark actions now read Bookmarking from the composed capability report. Bookmark mutation and
eligibility still dispatch through the bookmark provider compatibility API, so capability evidence decides whether the
shared action applies while the provider owns the operation.

`EntryDownloadCapabilityPolicy` is the shared owner for download consequences of Bookmarking. It derives bookmarked
bulk-download availability from Bulk Downloads + Bookmarking for both single-type and mixed-type selections, and it also
expresses the Bookmarking-derived cleanup protection introduced in Milestone 2.2. Entry and Library dropdowns consume
that policy result.

`EntryTypePresentation.downloadBookmarkedSupported` has been removed. Presentation retains type-specific labels, icons,
and plurals, but no longer stores this behavioral fact. Evidence-driven tests show synthetic Anime Bookmarking support
activating Updates bookmark actions and the bookmarked download menu option without an Anime-specific presentation
change.

Production behavior remains Manga-only because production capability evidence has not changed. No public capability
documentation statement changed in this milestone; the internal atlas and status now describe the migrated authority.

## Milestone 2.4 Completion Notes

`BookmarkDownloadVerticalContractTest` composes an existing Anime download provider with one synthetic bookmark-provider
registration. That single registration produces Bookmarking evidence and activates bookmark mutation eligibility and
dispatch, the Bulk Downloads + Bookmarking application policy, bookmarked candidate selection, cleanup protection, report
support, and selection of the shared vertical contract. The download provider receives only its unchanged candidate-pool
request and contains no bookmark-specific branch.

The production runtime-composition test now proves that Bookmarking and bookmarked-download applicability remain
Manga-only for Manga, Anime, and Book. The public content-type reference keeps those current results while explaining
that bookmarked bulk downloads and bookmark-aware cleanup are automatic consequences of individual bookmark support.

The temporary bookmark compatibility surface on `EntryConsumptionInteraction` has been removed. Bookmark eligibility
and mutation now use a distinct `EntryBookmarkInteraction` backed by the same bookmark-provider registration, while the
consumption interaction retains only consumed/unconsumed behavior. No unrelated compatibility API was migrated.

All capability foundation, registry, lifecycle, Manga, Anime, Book, and focused application policy/presentation tests
passed, as did the interaction boundary check, FOSS application compilation, Spotless, and diff validation. Phase 2
therefore demonstrates declare once, derive common consequences, and make the resulting contract applicable without a
per-type download or presentation opt-in.
