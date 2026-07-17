# Phase 6 — Capability-Driven Feature Contracts

## Objective

Make capability support select shared behavioral expectations automatically, replacing manual parity recall with enforceable contracts.

## Contract Foundation

- [ ] Define how capability evidence selects applicable contract scenarios.
- [ ] Define how type modules supply media-specific fixtures without restating support.
- [ ] Fail clearly when a supporting type lacks required contract setup.
- [ ] Cover both fundamental and derived capability combinations.
- [ ] Keep type-specific media tests separate.

## First Comprehensive Contract: Downloads

- [ ] Queue and ordinary start
- [ ] Start-now promotion
- [ ] Pause and resume
- [ ] Cancellation isolation
- [ ] Queue restoration and identity
- [ ] Automatic-download policy
- [ ] Consumption cleanup and download-ahead
- [ ] Storage publication, deletion, and reindex outcomes
- [ ] Shared presentation and failure semantics
- [ ] Bookmark-derived behavior when applicable

## Further Contract Areas

- [ ] Consumption and bookmarks
- [ ] Merge and migration
- [ ] Preview and immersive browsing
- [ ] Child and library filtering
- [ ] Other shared features identified by the atlas

## Exit Gate

- Declared support automatically selects relevant scenarios.
- Missing fixtures or specialized providers fail with an actionable obligation.
- Derived capability combinations receive their own expectations.
- Shared contracts and media-specific tests have clear, non-duplicated ownership.

## Validation

- Contract harness self-tests
- All participating type-module tests
- Existing feature tests for behavior equivalence
- `./gradlew --quiet checkEntryInteractionBoundaries`
- `./gradlew --quiet :app:compileFossKotlin`
- `./gradlew --quiet spotlessCheck`
- `git diff --check`

## Manifesto Review

Confirm that support automatically causes verification instead of relying on a contributor to add tests manually.
