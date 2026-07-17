# Phase 0 — Capability Atlas and Design Decisions

## Objective

Establish a reviewed current-state map before introducing a capability abstraction. This phase is inventory and decision work only.

## Non-Goals

- Do not create the capability model.
- Do not migrate consumers.
- Do not change runtime behavior.
- Do not fix documentation or implementation discrepancies before expected behavior is agreed.
- Do not classify storage formats, reader/player mechanics, backup schemas, or compatibility conversions as product capabilities merely because they branch by type.

## Milestone 0.1 — Evidence Inventory

- [x] Inventory every Entry interaction processor category and registration by content type.
- [x] Inventory `supports…` methods and capability-like properties.
- [x] Inventory explicit `Unsupported`, no-op, and default-false behavior.
- [x] Inventory presentation fields that influence behavioral availability.
- [x] Inventory direct `EntryType` checks that gate user-facing features.
- [x] Inventory source, tracker, local/stub, platform, entry, and selection inputs.
- [x] Add each fact to `../capability-atlas.md` with a provisional scope and owner.
- [x] Stop and review the evidence inventory before continuing.

## Milestone 0.2 — Consumer and Coverage Graph

- [x] Map every inventoried fact to screens, actions, settings, policies, workers, and integrations.
- [x] Map focused, registry, boundary, and application tests.
- [x] Map every row in `docs/features/content-type-reference.md` to evidence and tests.
- [x] Identify duplicate support facts and independently maintained UI flags.
- [x] Identify feature combinations currently implemented per type.
- [x] Record undocumented executable capabilities and documented claims without evidence.
- [x] Stop and review the consumer graph before continuing.

## Milestone 0.3 — Expected State and Decisions

- [ ] Review every discrepancy with product behavior as the deciding concern.
- [ ] Mark each discrepancy as implementation bug, documentation drift, intentional absence, or unresolved.
- [ ] Decide how provider-backed, intrinsic, contextual, derived, and presentation-only facts are evidenced.
- [ ] Decide how intentional unsupported and not-applicable results differ.
- [ ] Decide which public source/tracker capability contracts remain external inputs.
- [ ] Record accepted decisions under `../decisions/`.
- [ ] Assign each discrepancy to a later resolution phase.
- [ ] Complete the Phase 0 summary in the atlas.

## Exit Gate

- Every known capability or capability-like gate has an owner and scope.
- Every content-type reference row maps to executable evidence.
- All duplicated support statements are identified.
- Contextual support is separated from type-wide support.
- Discrepancies have an expected outcome or an explicit unresolved decision.
- Phase 1 has enough accepted decisions to build the foundation without guessing.

## Validation

- `git diff --check`
- Searches demonstrating coverage of processor registration, support queries, presentation flags, type gates, source/tracker inputs, tests, and docs
- `./gradlew --quiet checkEntryInteractionBoundaries` only if the inventory or decision work changes boundary rules

## Manifesto Review

Confirm that the atlas exposes the dependency graph currently left to memory and does not itself become another unverified source of support truth.
