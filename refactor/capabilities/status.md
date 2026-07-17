# Capability Refactor Status

Updated: 2026-07-17

## Repository State at Preparation

- Branch: `features-arch-refactor`
- Manifesto commit: `394151edb` (`(chore): new manifesto`)
- Preparation commit: `26c1bcedf` (`(docs): refactor workspace`)
- Evidence inventory commit: `75c98e5b2` (`(docs): inventory capability evidence`)
- Consumer and coverage commit: `18c927736` (`(docs): map capability consumers and coverage`)
- Architecture decisions commit: `6d688b04a` (`(docs): record capability architecture decisions`)

Always verify the current branch, `HEAD`, working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 1 — Authoritative capability foundation
- Milestone: 1.1 — Capability vocabulary and semantics
- State: Complete; stopped for review before Milestone 1.2

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

## Current Scope

Milestone 1.1 introduces only the capability vocabulary, query-subject distinction, support outcomes, evidence model, and their semantic invariants in the Entry interaction API.

It must not connect the model to processor registration, enumerate production capability support, migrate consumers, generate documentation, or change runtime behavior. Those steps belong to Milestones 1.2–1.4 and later phases.

## Phase 1 Milestones

- 1.1: Capability vocabulary and support semantics
- 1.2: Registration-derived evidence collection and validation
- 1.3: Deterministic reports for real type compositions
- 1.4: Compatibility, integration validation, and Phase 1 exit gate

Milestone 1.1 is complete. Milestone 1.2 has not started.

## Last Validation

- `./gradlew --quiet spotlessApply` completed successfully on 2026-07-17
- `./gradlew --quiet :entry-interactions:api:testDebugUnitTest` passed, including eight new capability-model tests
- `./gradlew --quiet checkEntryInteractionBoundaries` passed
- `./gradlew --quiet :app:compileFossKotlin` passed
- `./gradlew --quiet spotlessCheck` passed
- `git diff --check` passed
- Runtime behavior is unchanged: the new vocabulary has no production consumer or registry connection

## Manifesto Comparison

- Fundamental identity is separate from support outcomes and contains no derived feature combinations.
- Type-wide and contextual queries cannot exchange subjects accidentally.
- Supported, deliberate absence, contextual blockers, missing obligations, and unresolved state are explicit rather than boolean or null fallbacks.
- A missing feature obligation cannot replace or downgrade a fundamental capability assessment.
- This milestone centralizes semantic vocabulary only; it does not copy current per-type support into a new matrix.

## Exact Next Action After Review

Begin Milestone 1.2 only after explicit approval:

1. Make processor registration contribute provider-backed evidence.
2. Add exceptional intrinsic declarations at the type-composition boundary.
3. Validate empty, duplicate, missing, contradictory, and contextual evidence without changing existing behavior.
4. Stop before Milestone 1.3.
