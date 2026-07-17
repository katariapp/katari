# Capability Refactor Status

Updated: 2026-07-17

## Repository State at Preparation

- Branch: `features-arch-refactor`
- Manifesto commit: `394151edb` (`(chore): new manifesto`)
- Preparation commit: `26c1bcedf` (`(docs): refactor workspace`)

Always verify the current branch, `HEAD`, working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 0 — Capability atlas and design decisions
- Milestone: 0.1 — Build the current-state capability inventory
- State: Complete; stopped for review before Milestone 0.2

## Completed

- [x] Feature capability problem formulated
- [x] Capability manifesto written and committed
- [x] Large refactor split into reviewable phases
- [x] Durable workspace and resume protocol prepared
- [x] Phase files and decision-record location prepared
- [x] Processor registration, behavioral evidence, fallbacks, direct gates, and contextual inputs inventoried
- [x] Evidence classified by provisional scope and owner without changing expected behavior

## Current Scope

Phase 0 is planning and inventory only. It must not introduce the capability foundation or change runtime behavior.

Milestone 0.1 is recorded in [`capability-atlas.md`](capability-atlas.md). Do not treat its provisional classifications as accepted design decisions.

## Milestone 0.1 Findings Awaiting Review

- Registration proves provider presence but not every sub-capability; several registered processors still return false, unsupported, empty, or contextual results.
- Absence is currently expressed through missing providers, default-false methods, explicit `Unsupported`, empty results, no-ops, presentation booleans, and direct type checks.
- `downloadBookmarkedSupported` is an independent presentation statement for a combination already represented by download and bookmark behavior.
- Anime's executable migration and merge claims conflict with `content-type-reference.md`; expected behavior remains unresolved.
- Book library-update notifications fall through to Manga-specific routing and terminology because the notification type catalog has no Book entry.
- Preview, immersive browsing, tracking, local/stub restrictions, download options, platform features, entries, and selections make some support contextual rather than type-wide.
- Compatibility and media-specific branches were separated from product capability gates so the future model does not absorb every `EntryType` branch.

These are evidence findings, not accepted capability-model decisions. Milestone 0.3 must resolve their intended meaning.

## Last Validation

- `./gradlew --quiet spotlessApply` completed successfully on 2026-07-17
- Milestone 0.1 documentation: `git diff --check` passed on 2026-07-17
- Inventory searches covered processor registrations, support/default methods, unsupported/no-op behavior, presentation flags, direct type gates, and source/tracker/context inputs
- Only refactor workspace documentation changed; runtime behavior and public feature documentation were not modified
- Runtime tests: not required for an evidence-only milestone

## Exact Next Action After Review

Begin Milestone 0.2 only:

1. Verify current repository state.
2. Map each inventoried fact to its screens, actions, settings, policies, workers, and integrations.
3. Map focused, registry, boundary, application, and documentation coverage.
4. Record duplicated consumers and coverage gaps without fixing them.
5. Update this status and stop at the Milestone 0.2 boundary.

Do not begin Milestone 0.2 until the user explicitly continues. Do not start Phase 1.
