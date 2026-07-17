# Current Product Outcomes

Status: Accepted

## Context

The capability atlas found four discrepancies or ambiguous representations that later phases cannot safely resolve from architecture alone. Phase 0 must establish the intended current product result while preserving runtime behavior until the assigned phase.

## Decision

### Anime migration

Anime migration is supported product behavior. The capability processor returns supported, migration source search preserves Anime type, and migration use-case tests exercise Manga and Anime. The content-type reference is stale.

Resolution:

- Preserve Anime migration support.
- Assert the real Manga/Anime/Book capability matrix during Phase 3.
- Correct and later derive/verify the public reference in Phase 7.

### Book downloads

Book downloads are supported product behavior. Production runtime composition always enables and registers the Book download processor. `downloadsEnabled = false` is a construction/testing seam, not an optional product capability declaration.

Resolution:

- Phase 1 derives production download evidence from actual provider registration.
- Tests must distinguish intentionally reduced fixtures from production composition.
- Phase 6 makes every production-registered downloader enter the shared download contracts.

### Anime child-group filtering

Anime child-group filtering is intentionally unsupported in the current product. The content reference and effective behavior agree. The registered all-no-op provider is not valid positive capability evidence.

Resolution:

- Preserve the unavailable user-facing result.
- Record the absence explicitly under the agreed support semantics.
- Phase 3 removes the ambiguity between an unsupported registered provider and a missing provider/default fallback.

### Book library-update notifications

Book entries participate in library updates, but their notifications fall through to Manga channel/group identifiers and Manga chapter/read vocabulary. This is an implementation defect, not an intentional product limitation.

Resolution:

- Preserve shared library-update notification behavior for Book.
- Phase 5 derives common notification actions from update, consumption, open, and download capabilities.
- Book receives explicit neutral/Book presentation rather than an implicit Manga fallback.
- Phase 6 adds notification contract coverage for every applicable type.

## Consequences

- Phase 1 can model evidence without guessing whether the Book download seam is a product flag.
- Phase 3 has an accepted migration and child-group matrix.
- Phase 5 has an explicit Book notification correction rather than treating the fallback as compatibility behavior.
- Phase 7 can correct the Anime migration reference from accepted executable truth.

## Alternatives Rejected

- Disabling Anime migration to match documentation: contradicts implemented, tested generic behavior without a product reason.
- Treating Book downloads as optionally unsupported: confuses a test construction seam with production composition.
- Treating Anime's no-op child-group provider as support: registration would become misleading evidence.
- Keeping Manga as a generic notification fallback: silently gives new types incorrect semantics and recreates the memory problem.

## Affected Capabilities and Phases

- Migration and child-group filtering in Phase 3
- Book notifications and derived action composition in Phase 5
- Download contracts in Phase 6
- Anime migration documentation in Phase 7
