# Phase 4 — Entry-Type Composition Migration

## Objective

Move Manga, Anime, and Book composition onto the general contribution boundary without adding per-type support matrices
or protecting obsolete APIs merely to preserve application compilation.

## Milestones

- [ ] Port Open, Continue, and every other operational implementation through the same provider-contribution mechanism.
- [ ] Preserve valid partial compositions and verify that omitting a provider means unsupported.
- [ ] Port genuine media-specific adapters selected by applicable feature relationships.
- [ ] Remove superseded registration, intrinsic evidence, and report paths as their authority moves.
- [ ] Record unported consumer compile failures as Phase 5 or Phase 6 obligations.
- [ ] Stop before migrating feature consumers.

## Constraints

- The generic kernel must not learn concrete entry types.
- A type must not enumerate consuming features.
- Tests must verify provider behavior, valid partial construction, and genuine adapters—not support labels.
- Compilation is required for the migrated composition path, not necessarily the whole application.

## Exit Gate

- Every production type uses the same discoverable contribution contract.
- Every subset of providers, including a single provider, forms a valid contribution.
- Support follows provider presence and needs neither an explicit absence declaration nor a matrix.
- Concrete media behavior remains in its owning module.
- Remaining failures identify consumers that must conform to graph evaluation.

## Manifesto Review

Confirm that adding a future type requires implementing one contribution boundary, not editing lists of features or tests.
