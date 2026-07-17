# Capability Refactor Workspace

This directory is the durable working context for the feature capability refactor described by [`../capability-manifesto.md`](../capability-manifesto.md).

The refactor exists so that adding a feature or content-type capability does not depend on a contributor remembering every related action, screen, setting, policy, test, and documentation entry.

## Read Order

Before doing capability-refactor work, read:

1. [`../capability-manifesto.md`](../capability-manifesto.md)
2. [`plan.md`](plan.md)
3. [`status.md`](status.md)
4. [`capability-atlas.md`](capability-atlas.md)
5. The active file under [`phases/`](phases/)
6. Relevant records under [`decisions/`](decisions/)

Then inspect the current branch, recent commits, working tree, and tests rather than assuming this workspace is newer than Git.

## Working Rules

- Work on only the active milestone recorded in `status.md`.
- Do not begin the next phase implicitly.
- Preserve existing behavior unless the active phase explicitly includes an agreed correction.
- Treat provider registration as evidence of a provider-backed capability; do not add a second declaration merely to repeat that a provider exists.
- Keep type-wide, source-dependent, entry-dependent, selection-dependent, and external-integration support distinct.
- Derive cross-feature behavior when existing capabilities supply enough information.
- Keep genuine media, storage, compatibility, and wire-format differences in their owning boundaries.
- Record durable architectural decisions when they are made; do not use decision records as a running diary.
- Before stopping, compare the work with the manifesto and update `status.md`.

## Source of Truth

When sources disagree, use this priority:

1. Current code, tests, and repository state
2. Accepted decision records
3. The active phase scope and exit gate
4. `status.md`
5. Planning notes and historical descriptions

If `status.md` is stale, correct it from Git and the working tree before continuing.

## Milestone Closure

Before marking a milestone complete:

1. Check the active phase checklist.
2. Run the phase's focused validation.
3. Run broader boundary or compilation checks when required by the phase.
4. Compare the result against the manifesto's principles and success criteria.
5. Update the capability atlas and decision records when the work changed either.
6. Update `status.md` with completed work, validation, and the exact next action.
7. Stop before beginning the next milestone.

Commits are made only when explicitly authorized. When a milestone is committed, record its commit in `status.md`.

## Resume Prompt

A future continuation can use:

> Continue the capability refactor from `refactor/capabilities/status.md`. Complete only the active milestone, validate it against the manifesto, update the durable state, and stop before the next milestone.

## Workspace Files

- `plan.md`: stable phase sequence, scope, dependencies, and exit gates
- `status.md`: concise operational resume point
- `capability-atlas.md`: inventory of capability facts, consumers, duplication, and coverage
- `phases/`: bounded phase checklists and validation requirements
- `decisions/`: accepted architectural decisions with consequences
