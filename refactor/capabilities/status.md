# Capability Refactor Status

Updated: 2026-07-17

## Repository State at Preparation

- Branch: `features-arch-refactor`
- Manifesto commit: `394151edb` (`(chore): new manifesto`)
- Preparation changes: uncommitted

Always verify the current branch, `HEAD`, working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 0 — Capability atlas and design decisions
- Milestone: 0.1 — Build the current-state capability inventory
- State: Ready; not started

## Completed

- [x] Feature capability problem formulated
- [x] Capability manifesto written and committed
- [x] Large refactor split into reviewable phases
- [x] Durable workspace and resume protocol prepared
- [x] Phase files and decision-record location prepared

## Current Scope

Phase 0 is planning and inventory only. It must not introduce the capability foundation or change runtime behavior.

Start with the checklist in [`phases/00-capability-atlas.md`](phases/00-capability-atlas.md).

## Known Leads to Verify

- Processor registration already proves several operational capabilities.
- Support is also expressed through `supports…` methods, presentation booleans, explicit `Unsupported` branches, source/tracker contracts, tests, and documentation.
- `downloadBookmarkedSupported` duplicates behavior owned elsewhere.
- Anime's current migration and merge processor claims appear inconsistent with `content-type-reference.md`.
- Capability scope varies: some facts are type-wide, while preview, immersive browsing, tracking, local/stub restrictions, download options, and selection actions are contextual.
- `checkEntryInteractionBoundaries` is a potential enforcement point but must not become a substitute for behavioral contracts.

These are investigation leads, not accepted conclusions. Phase 0 must verify them against current code and intended product behavior.

## Last Validation

- Preparation documents: `git diff --check` passed on 2026-07-17
- Durable workspace contains the plan, status, atlas scaffold, phase files, and decision-record protocol
- `AGENTS.md` was not changed, as requested
- Runtime tests: not required for preparation-only changes

## Exact Next Action

Begin Milestone 0.1 only:

1. Verify current repository state.
2. Inventory capability evidence and consumers using the atlas schema.
3. Classify each fact by scope and ownership.
4. Record discrepancies without fixing them.
5. Update this status and stop at the Milestone 0.1 boundary.

Do not start Phase 1.
