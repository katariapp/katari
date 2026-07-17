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
- Deterministic report commit: `2c6e26a52` (`(feat): report composed entry capabilities`)
- Production report commit: `8e03f7469` (`(feat): expose production capability reports`)

Always verify the current branch, `HEAD`, working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 2 — Bookmarking and downloads vertical proof
- Milestone: 2.1 — Bookmark provider authority
- State: Complete; stopped before Milestone 2.2 with changes uncommitted

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
- [x] Milestone 1.4 production boundary, production composition coverage, and Phase 1 exit gate completed
- [x] Phase 2 split into four bounded implementation milestones
- [x] Milestone 2.1 bookmark provider authority and compatibility dispatch completed

## Current Scope

Milestone 2.1 replaces the bookmark boolean sub-capability and unsupported mutation no-ops with a distinct operational
bookmark provider. Existing application consumers remain on the compatibility facade during this milestone.

Manga registers the provider and thereby produces Bookmarking evidence. Anime and Book do not register one and keep
their accepted explicit intentional-absence outcomes. Derived bulk download, cleanup, UI, and presentation behavior is
reserved for Milestones 2.2 and 2.3.

The implementation now satisfies that scope: Manga provider registration is the sole positive Bookmarking authority;
Anime and Book have neither a provider nor mutation no-ops; compatibility support and dispatch derive from the provider
map.

## Milestone Sequence

- 1.1: Capability vocabulary and support semantics
- 1.2: Registration-derived evidence collection and validation
- 1.3: Deterministic reports for real type compositions
- 1.4: Compatibility, integration validation, and Phase 1 exit gate

Phase 1 is complete. Phase 2 milestones are:

- 2.1: Bookmark provider authority and compatibility dispatch
- 2.2: Shared bookmark/download policy
- 2.3: Application and presentation derivation
- 2.4: Vertical contract and integration gate

## Last Validation

- `./gradlew --quiet spotlessApply` completed successfully on 2026-07-17
- `./gradlew --quiet :entry-interactions:api:testDebugUnitTest` passed, including six deterministic-report tests
- `./gradlew --quiet :entry-interactions:testDebugUnitTest` passed, including evidence/report composition, existing registry, and production runtime-composition tests
- Focused Manga, Anime, and Book `EntryInteractionPluginTest` suites passed with type-report assertions
- `./gradlew --quiet checkEntryInteractionBoundaries` passed
- `./gradlew --quiet :app:compileFossKotlin` passed
- `./gradlew --quiet spotlessCheck` passed
- `git diff --check` passed
- Production runtime composition reports Manga, Anime, and Book, with provider-backed download support for all three
- Existing `createEntryInteractions` callers retain the same dispatch and compatibility behavior; application features currently have no report consumer
- All Entry interaction API, registry, Manga, Anime, and Book module tests passed after the bookmark-provider split
- `./gradlew --quiet :app:compileFossKotlin` and `checkEntryInteractionBoundaries` passed for Milestone 2.1

## Manifesto Comparison

- The catalog contains fundamental facts only; automatic downloads, cleanup, update policy, and bookmark/download intersections are not new capability declarations.
- Reports are built from composed evidence and accepted outcomes rather than a manually populated per-type support matrix.
- Contextual providers produce conditional report entries and cannot become unconditional supported results.
- Anime and Book bookmark absence and Anime child-group-filter absence are explicit owned product decisions.
- Legacy facts whose current booleans are not yet authoritative evidence—such as merge, migration, bulk download, and some filters—remain visibly unresolved for later migration.
- Registration order does not affect report ordering or values.
- Production inspection reuses the report created from operational plugin registration; it does not introduce another support matrix.
- The production composition proves Book downloads from the actual enabled Book plugin path rather than a documentation or presentation flag.
- Unresolved catalog entries remain visible migration blockers for their assigned later phases and are not treated as accepted absence.
- No UI, settings, worker, policy, or public documentation behavior changed in Phase 1.
- Bookmarking is now declared by one operational provider registration rather than a consumption boolean plus type no-ops.
- The compatibility facade derives support from that registration, so it cannot disagree with capability evidence.
- Derived download, cleanup, and UI consequences have not been pulled into the content-type plugin; their feature-owned
  migration remains isolated in Milestones 2.2 and 2.3.
- Production support remains Manga-only, and public capability documentation remains behaviorally accurate.

## Exact Next Action After Review

After explicit approval, commit Milestone 2.1 and complete only Milestone 2.2: shared bookmarked-download candidate and
cleanup policy, including the synthetic Anime bookmark-provider proof. Stop before application/presentation migration.
