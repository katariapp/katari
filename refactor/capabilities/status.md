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

Always verify the current branch, `HEAD`, working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 1 — Authoritative capability foundation
- Milestone: 1.2 — Evidence collection and validation
- State: Complete; stopped for review before Milestone 1.3

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

## Current Scope

Milestone 1.2 connects authoritative evidence to Entry interaction composition without changing dispatch or existing support APIs.

Operational provider registrations contribute evidence only where registration proves the fundamental capability. Intrinsic declarations are an exceptional type-composition mechanism for stable facts with no provider. Contextual providers remain conditional, and absent evidence remains unresolved rather than becoming implicit unsupported behavior.

## Phase 1 Milestones

- 1.1: Capability vocabulary and support semantics
- 1.2: Registration-derived evidence collection and validation
- 1.3: Deterministic reports for real type compositions
- 1.4: Compatibility, integration validation, and Phase 1 exit gate

Milestone 1.2 is complete. Milestone 1.3 has not started.

## Last Validation

- `./gradlew --quiet spotlessApply` completed successfully on 2026-07-17
- `./gradlew --quiet :entry-interactions:api:testDebugUnitTest` passed, including nine capability-model tests
- `./gradlew --quiet :entry-interactions:testDebugUnitTest` passed, including nine evidence-composition tests and all 53 existing registry tests
- Focused Manga, Anime, and Book `EntryInteractionPluginTest` suites passed
- `./gradlew --quiet checkEntryInteractionBoundaries` passed
- `./gradlew --quiet :app:compileFossKotlin` passed
- `./gradlew --quiet spotlessCheck` passed
- `git diff --check` passed
- Existing `createEntryInteractions` callers retain the same API and dispatch behavior

## Manifesto Comparison

- Registering an operational provider now supplies evidence automatically instead of requiring a parallel type support flag.
- Intrinsic evidence is restricted to stable type-wide facts and conflicts with provider authority for the same fact.
- Preview and immersive registrations remain contextual evidence; external/context evidence cannot establish unconditional type support.
- Registration of capability wrappers, universal update policy, and sub-capability processors does not masquerade as support.
- Empty composition remains valid, so missing evidence can be reported as unresolved in Milestone 1.3 rather than guessed as intentional absence.
- No derived feature combination or per-type boolean matrix was introduced.

## Exact Next Action After Review

Begin Milestone 1.3 only after explicit approval:

1. Define the complete reviewed fundamental capability catalog from the Phase 0 atlas.
2. Add explicit accepted absence and non-applicability records where registration cannot supply evidence.
3. Produce deterministic Manga, Anime, and Book capability reports from the composed evidence.
4. Surface missing facts as unresolved and keep contextual capabilities conditional.
5. Stop before Milestone 1.4.
