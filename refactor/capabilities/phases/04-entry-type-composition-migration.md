# Phase 4 — Entry-Type Composition Migration

## Objective

Move Manga, Anime, and Book composition onto the general contribution boundary without adding per-type support matrices
or protecting obsolete APIs merely to preserve application compilation.

This phase may begin only after the findings and classifications in
[`../migration-inventory.md`](../migration-inventory.md) are reviewed. Its bounded source of work is the `T01`–`T27`
register, including type-owned artifacts currently outside the interaction registry.

## Milestones

- [x] 4.1 — Give every production plugin one owned type contribution and migrate Open/Continue provider identity without
  making either provider mandatory.
- [x] 4.2 — Decompose and migrate the remaining operational provider contracts in `T04`–`T21`:
  - [x] 4.2.1 — Introduce capability-owned bindings and migrate Consumption, Bookmarking, Progress transfer, and
    Playback-preference transfer (`T11`, `T12`, `T14`, `T15`).
  - [x] 4.2.2 — Decompose and migrate Downloads (`T04`–`T08`).
  - [x] 4.2.3 — Split and migrate Migration and Merge (`T09`, `T10`).
  - [x] 4.2.4 — Migrate Update Eligibility and decompose Child List, filtering, Preview, and Immersive contracts
    (`T13`, `T16`–`T21`).
- [ ] 4.3 — Join type-owned runtime artifacts and parallel composition paths in `T22`–`T27`.
- [ ] 4.4 — Remove superseded registration paths, reconcile the test harness, and close the Phase 4 exit gate.

Phase-level checks:

- [ ] Port Open, Continue, and every other operational implementation through the same provider-contribution mechanism.
- [ ] Account for every `T01`–`T27` row; record a completion, deliberate deferral, or corrected classification.
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
- Do not use an interaction-processor inventory as proof that type composition is complete; library progress, runtime
  services, viewer settings, caches, image components, warmups, and presentation inputs are also in scope.
- Feature consequences in `F01`–`F27` remain Phase 5 work even when Phase 4 compile failures expose them.

## Exit Gate

- Every production type uses the same discoverable contribution contract.
- Every subset of providers, including a single provider, forms a valid contribution.
- Support follows provider presence and needs neither an explicit absence declaration nor a matrix.
- Concrete media behavior remains in its owning module.
- Remaining failures identify consumers that must conform to graph evaluation.
- Every type-owned inventory row has an explicit disposition and no parallel root list remains as an unnoticed second
  participation boundary.

## Manifesto Review

Confirm that adding a future type requires implementing one contribution boundary, not editing lists of features or tests.
