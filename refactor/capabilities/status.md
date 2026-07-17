# Capability Refactor Status

Updated: 2026-07-17

## Repository State at Preparation

- Branch: `features-arch-refactor`
- Manifesto commit: `394151edb` (`(chore): new manifesto`)
- Preparation commit: `26c1bcedf` (`(docs): refactor workspace`)
- Evidence inventory commit: `75c98e5b2` (`(docs): inventory capability evidence`)

Always verify the current branch, `HEAD`, working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 0 — Capability atlas and design decisions
- Milestone: 0.2 — Build the consumer and coverage graph
- State: Complete; stopped for review before Milestone 0.3

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

## Current Scope

Phase 0 is planning and inventory only. It must not introduce the capability foundation or change runtime behavior.

Milestones 0.1 and 0.2 are recorded in [`capability-atlas.md`](capability-atlas.md). Do not treat their provisional classifications or gap descriptions as accepted design decisions.

## Milestone 0.2 Findings Awaiting Review

- Registration proves provider presence but not every sub-capability; several registered processors still return false, unsupported, empty, or contextual results.
- Absence is currently expressed through missing providers, default-false methods, explicit `Unsupported`, empty results, no-ops, presentation booleans, and direct type checks.
- `downloadBookmarkedSupported` is an independent presentation statement for a combination already represented by download and bookmark behavior.
- Anime's executable migration claim conflicts with `content-type-reference.md`; expected behavior remains unresolved. The initial merge lead was disproven during row-by-row traceability because both code and documentation report Anime merge support.
- Book library-update notifications fall through to Manga-specific routing and terminology because the notification type catalog has no Book entry.
- Preview, immersive browsing, tracking, local/stub restrictions, download options, platform features, entries, and selections make some support contextual rather than type-wide.
- Compatibility and media-specific branches were separated from product capability gates so the future model does not absorb every `EntryType` branch.
- Most coverage proves registry dispatch or one media implementation. It does not discover every real type that declares a capability and run a shared feature contract against it.
- Shared marked-consumed download cleanup is the strongest current cross-type example, but it iterates enum values rather than capability declarations.
- The public bookmark-based bulk-download rule is currently coincidental: the downloader branches, presentation flag, and documentation are independently maintained.
- Book progress labels/library summaries, Manga bulk/automatic-download policy, real update-eligibility matrices, Anime immersive loading, and Book update notifications have missing or weak focused coverage.
- `checkEntryInteractionBoundaries` protects module ownership but cannot detect a forgotten UI, policy, worker, notification, backup, test, or documentation integration.

These are evidence findings, not accepted capability-model decisions. Milestone 0.3 must resolve their intended meaning.

## Last Validation

- `./gradlew --quiet spotlessApply` completed successfully on 2026-07-17 for Milestone 0.2
- Milestone 0.2 documentation: `git diff --check` passed on 2026-07-17
- Inventory and graph searches covered processor registrations, support/default methods, consumers, tests, boundary rules, documentation claims, direct type gates, and source/tracker/context inputs
- Only refactor workspace documentation changed; runtime behavior and public feature documentation were not modified
- `checkEntryInteractionBoundaries` was not run because no boundary rule or runtime source changed
- Runtime tests: not required for an evidence-only milestone

## Exact Next Action After Review

Begin Milestone 0.3 only:

1. Verify current repository state.
2. Review every discrepancy and gap against intended product behavior.
3. Classify facts as fundamental, derived, contextual, intentionally unsupported, specialized, presentation-only, or compatibility-only.
4. Record accepted decisions and assign later implementation/documentation phases.
5. Complete the Phase 0 summary and stop at the Milestone 0.3 boundary.

Do not begin Milestone 0.3 until the user explicitly continues. Do not start Phase 1.
