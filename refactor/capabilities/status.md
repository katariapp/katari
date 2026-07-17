# Capability Refactor Status

Updated: 2026-07-17

## Repository State at Preparation

- Branch: `features-arch-refactor`
- Manifesto commit: `394151edb` (`(chore): new manifesto`)
- Preparation commit: `26c1bcedf` (`(docs): refactor workspace`)
- Evidence inventory commit: `75c98e5b2` (`(docs): inventory capability evidence`)
- Consumer and coverage commit: `18c927736` (`(docs): map capability consumers and coverage`)
- Architecture decisions commit: `6d688b04a` (`(docs): record capability architecture decisions`)
- Capability semantics commit: `471978d3d` (`(feat): define capability support semantics`)
- Evidence composition commit: `a3ae2c6b5` (`(feat): collect capability evidence from composition`)

Always verify the current branch, `HEAD`, working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 1 — Authoritative capability foundation
- Milestone: 1.3 — Deterministic type reports
- State: Complete; stopped for review before Milestone 1.4

## Completed

- [x] Feature capability problem formulated
- [x] Capability manifesto written and committed
- [x] Large refactor split into reviewable phases
- [x] Durable workspace and resume protocol prepared
- [x] Phase files and decision-record location prepared
- [x] Processor registration, behavioral evidence, fallbacks, direct gates, and contextual inputs inventoried
- [x] Evidence classified by provisional scope and owner without changing expected behavior
- [x] Capability evidence mapped to screens, actions, policies, workers, settings, and integrations
- [x] Registry, type-focused, consumer, boundary, and documentation coverage mapped
- [x] Every content-type reference row traced to executable evidence and tests
- [x] Duplicate facts, implemented capability combinations, and coverage gaps recorded
- [x] Evidence authority, derivation, support semantics, and contextual ownership decisions accepted
- [x] Current product discrepancies classified and assigned to later phases
- [x] Decision records accepted by the user
- [x] Phase 1 split into four bounded implementation milestones
- [x] Milestone 1.1 capability vocabulary and semantic invariants implemented
- [x] Milestone 1.2 registration-derived evidence and composition validation implemented
- [x] Milestone 1.3 reviewed catalog and deterministic type reports implemented

## Current Scope

Milestone 1.3 defines the reviewed fundamental capability catalog and projects composed evidence into deterministic per-type reports.

The report distinguishes supported, conditional, explicitly unavailable, not applicable, and unresolved facts. It is available from the composition result for validation and tests but is not yet exposed through the production `EntryInteractions` facade or consumed by application features.

## Phase 1 Milestones

- 1.1: Capability vocabulary and support semantics
- 1.2: Registration-derived evidence collection and validation
- 1.3: Deterministic reports for real type compositions
- 1.4: Compatibility, integration validation, and Phase 1 exit gate

Milestone 1.3 is complete. Milestone 1.4 has not started.

## Last Validation

- `./gradlew --quiet spotlessApply` completed successfully on 2026-07-17
- `./gradlew --quiet :entry-interactions:api:testDebugUnitTest` passed, including six deterministic-report tests
- `./gradlew --quiet :entry-interactions:testDebugUnitTest` passed, including twelve evidence/report composition tests and all existing registry tests
- Focused Manga, Anime, and Book `EntryInteractionPluginTest` suites passed with type-report assertions
- `./gradlew --quiet checkEntryInteractionBoundaries` passed
- `./gradlew --quiet :app:compileFossKotlin` passed
- `./gradlew --quiet spotlessCheck` passed
- `git diff --check` passed
- Existing `createEntryInteractions` callers retain the same API and dispatch behavior; reports currently have no production consumer

## Manifesto Comparison

- The catalog contains fundamental facts only; automatic downloads, cleanup, update policy, and bookmark/download intersections are not new capability declarations.
- Reports are built from composed evidence and accepted outcomes rather than a manually populated per-type support matrix.
- Contextual providers produce conditional report entries and cannot become unconditional supported results.
- Anime and Book bookmark absence and Anime child-group-filter absence are explicit owned product decisions.
- Legacy facts whose current booleans are not yet authoritative evidence—such as merge, migration, bulk download, and some filters—remain visibly unresolved for later migration.
- Registration order does not affect report ordering or values.

## Exact Next Action After Review

Begin Milestone 1.4 only after explicit approval:

1. Expose the report/query foundation through the production Entry interaction composition boundary.
2. Verify compatibility APIs and application behavior remain unchanged.
3. Add production-composition coverage for every registered type, including production-enabled Book downloads.
4. Resolve or explicitly carry forward every Phase 1 exit-gate gap.
5. Run the complete Phase 1 validation set, update the atlas, and stop before Phase 2.
