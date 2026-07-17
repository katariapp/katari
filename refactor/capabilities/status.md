# Capability Refactor Status

Updated: 2026-07-17

## Repository State at Preparation

- Branch: `features-arch-refactor`
- Manifesto commit: `394151edb` (`(chore): new manifesto`)
- Preparation commit: `26c1bcedf` (`(docs): refactor workspace`)
- Evidence inventory commit: `75c98e5b2` (`(docs): inventory capability evidence`)
- Consumer and coverage commit: `18c927736` (`(docs): map capability consumers and coverage`)

Always verify the current branch, `HEAD`, working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 0 — Capability atlas and design decisions
- Milestone: Phase complete
- State: Decisions accepted; awaiting explicit authorization to prepare and begin Phase 1

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

## Current Scope

Phase 0 was planning and inventory only. It did not introduce the capability foundation or change runtime behavior.

The complete Phase 0 atlas and accepted expected-state classification are recorded in [`capability-atlas.md`](capability-atlas.md). The records under [`decisions/`](decisions/) are accepted architecture constraints for later phases.

## Accepted Milestone 0.3 Decisions

- Provider registration is authoritative for provider-backed capabilities; explicit intrinsic declarations are reserved for stable facts no provider can prove.
- Universal behavior belongs to shared feature policy, contextual behavior composes runtime inputs, derived behavior is never another type opt-in, and presentation cannot own availability.
- Support results distinguish supported, intentionally unsupported, not applicable, contextually unavailable, missing obligation, and unresolved.
- Public source and tracker contracts remain authoritative external inputs rather than being copied into the type catalog.
- Specialized media work is an explicit obligation selected by a feature rule; missing work fails validation instead of silently returning unsupported.
- Anime migration remains supported and its public reference is stale.
- Book downloads are production-supported; `downloadsEnabled` is a construction/testing seam rather than product capability state.
- Anime child-group filtering remains intentionally unsupported; its registered no-op provider is not support evidence.
- Book's Manga-style library-update notification fallback is an implementation bug; Book should receive shared behavior with explicit neutral/Book semantics.

These decisions are Phase 1 preconditions and set later product behavior.

## Last Validation

- `./gradlew --quiet spotlessApply` completed successfully on 2026-07-17 for the Milestone 0.3 records
- Milestone 0.3 documentation: `git diff --check` passed on 2026-07-17
- Inventory and graph searches covered processor registrations, support/default methods, consumers, tests, boundary rules, documentation claims, direct type gates, and source/tracker/context inputs
- Only refactor workspace documentation changed; runtime behavior and public feature documentation were not modified
- `checkEntryInteractionBoundaries` was not run because no boundary rule or runtime source changed
- Runtime tests: not required for an evidence-only milestone

## Exact Next Action

1. Split Phase 1 into reviewable implementation milestones.
2. Record only the first bounded milestone as active.
3. Begin that milestone after explicit user authorization.
