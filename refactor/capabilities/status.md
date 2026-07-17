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
- Bookmark provider commit: `5a3c13b37` (`(refactor): make bookmarking provider-backed`)
- Bookmark/download policy commit: `d1b1d8b49` (`(refactor): derive bookmark download policy`)
- Bookmark action availability commit: `4273281bd` (`(refactor): derive bookmark action availability`)

Always verify the current branch, `HEAD`, working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 2 — Bookmarking and downloads vertical proof
- Milestone: 2.4 — Vertical contract and integration gate
- State: Phase complete; stopped before Phase 3 with changes uncommitted

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
- [x] Milestone 2.2 shared bookmark/download candidate and cleanup policy completed
- [x] Milestone 2.3 capability-derived application and presentation availability completed
- [x] Milestone 2.4 vertical contract, production projection, compatibility cleanup, and Phase 2 exit gate completed

## Current Scope

Phase 2 is complete. One synthetic Anime bookmark-provider registration now exercises the whole vertical consequence
chain: report support, bookmark mutation, application policy, bookmarked candidate selection, cleanup protection, and
capability-selected contract applicability.

Production composition proves Bookmarking and bookmarked-download applicability remain Manga-only. Bookmark operations
use a distinct `EntryBookmarkInteraction`; the temporary consumption compatibility surface is removed. The public
content-type reference retains the current Manga/Anime/Book results and describes the derived download consequences of
individual bookmark support.

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
- `./gradlew --quiet :entry-interactions:api:testDebugUnitTest` passed, including seven deterministic-report/query tests
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
- All Entry interaction API, registry, Manga, Anime, and Book module tests passed after the shared candidate-policy split
- Synthetic Anime Bookmarking tests passed for shared candidate selection and cleanup protection
- `./gradlew --quiet :app:compileFossKotlin`, `checkEntryInteractionBoundaries`, and `spotlessCheck` passed for Milestone 2.2
- Focused API and application tests passed for shared download capability policy, Updates bookmark actions, download-menu
  presentation, and unchanged type presentation metadata
- Synthetic Anime Bookmarking evidence activates Updates and bookmarked-download menu availability without Anime UI or
  downloader changes
- `./gradlew --quiet :entry-interactions:test :entry-interactions:spi:test :app:compileFossKotlin checkEntryInteractionBoundaries`
  passed for Milestone 2.3
- `./gradlew --quiet spotlessApply` and `git diff --check` passed for Milestone 2.3
- Full capability API, registry/lifecycle, Manga, Anime, and Book debug unit-test suites passed for the Phase 2 exit gate
- Focused FOSS application tests passed for Updates bookmark availability, bookmarked-download menu actions, and
  presentation metadata
- The synthetic vertical contract passed with one Anime bookmark-provider registration and an unchanged Anime-typed
  download provider
- Production runtime composition asserts Bookmarking and bookmarked-download applicability are Manga-only
- `./gradlew --quiet checkEntryInteractionBoundaries :app:compileFossKotlin spotlessCheck` passed for Milestone 2.4
- `git diff --check` passed for Milestone 2.4

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
- Bookmark eligibility and mutation dispatch through the provider-backed `EntryBookmarkInteraction`; the temporary
  consumption compatibility facade is gone.
- Derived download, cleanup, and application consequences remain feature-owned rather than being pulled into
  content-type plugins.
- Production support remains Manga-only, and public capability documentation remains behaviorally accurate.
- Bookmarked download selection is derived from Downloads + Bookmarking rather than separately implemented per downloader.
- Cleanup consumes the same Bookmarking truth and keeps preference semantics in the feature that owns them.
- A synthetic Anime provider receives both shared consequences without an Anime downloader change.
- No new derived capability flag or per-type opt-in was introduced.
- Entry, Updates, and Library availability now follows capability evidence rather than a compatibility support query or
  presentation flag.
- Presentation retains terminology and imagery but no longer owns bookmarked-download behavior.
- Public support remains unchanged, and the content-type reference now states that bookmark-based download behavior is
  an automatic consequence rather than another content-type opt-in.
- One synthetic provider registration selects and passes the complete vertical contract, so the common consequence chain
  no longer depends on a contributor remembering each consumer.
- No specialized bookmark/downloader obligation exists because shared child and download models satisfy the combination;
  a future combination that needs media-specific work must surface that requirement explicitly.

## Exact Next Action After Review

After explicit approval, commit Milestone 2.4. Do not begin Phase 3 until the user explicitly asks to continue.
