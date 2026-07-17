# Capability Refactor Workspace

This directory is the durable working context for the feature capability refactor described by [`../capability-manifesto.md`](../capability-manifesto.md).

The refactor exists so that adding a feature or content-type capability does not depend on a contributor remembering every related action, screen, setting, policy, test, and documentation entry.

## Read Order

Before doing capability-refactor work, read:

1. [`../capability-manifesto.md`](../capability-manifesto.md)
2. [`plan.md`](plan.md)
3. [`status.md`](status.md)
4. [`capability-atlas.md`](capability-atlas.md)
5. [`legacy-artifacts.md`](legacy-artifacts.md)
6. [`migration-obligations.md`](migration-obligations.md)
7. The active file under [`phases/`](phases/)
8. Relevant records under [`decisions/`](decisions/)

Then inspect the current branch, recent commits, working tree, and tests rather than assuming this workspace is newer than Git.

## Working Rules

- Work on only the active milestone recorded in `status.md`.
- Do not begin the next phase implicitly.
- Preserve existing behavior unless the active phase explicitly includes an agreed correction.
- Build the general contribution, discovery, relationship, and obligation architecture before migrating more production
  capabilities or consumers.
- Treat provider registration as evidence of a provider-backed capability; do not add a second declaration merely to repeat that a provider exists.
- Treat every interaction as provider-backed. A type with any subset of providers is valid; provider absence means
  unsupported and needs no parallel absence declaration or per-type capability matrix.
- Accept intermediate compile failures when a planned architectural boundary exposes unported code. Record them as
  migration obligations instead of adding compatibility authorities merely to restore compilation.
- Keep type-wide, source-dependent, entry-dependent, selection-dependent, and external-integration support distinct.
- Derive cross-feature behavior when existing capabilities supply enough information.
- Keep genuine media, storage, compatibility, and wire-format differences in their owning boundaries.
- Record durable architectural decisions when they are made; do not use decision records as a running diary.
- Before stopping, compare the work with the manifesto and update `status.md`.

## Source of Truth

For current executable behavior, code and behavioral tests are authoritative. The atlas records that evidence without
turning it into the target design.

For the target architecture, use this priority:

1. The manifesto
2. Accepted decision records
3. The active phase scope and exit gate
4. `plan.md` and `legacy-artifacts.md`
5. `status.md`
6. Current prototype code and historical descriptions

Current code is migration evidence, not authority over the architecture intended to replace it. A compiling legacy API
must not override an accepted dependency direction or retirement decision.

If `status.md` is stale, correct it from Git and the working tree before continuing.

## Milestone Closure

Before marking a milestone complete:

1. Check the active phase checklist.
2. Run the phase's focused structural and behavioral validation.
3. Run broader boundary or compilation checks only when required by that phase's architectural exit gate.
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
- `legacy-artifacts.md`: committed prototype artifacts to retain, retire, or rehome
- `migration-obligations.md`: compile failures exposed by the architecture cut and their owning migration phases
- `phases/`: bounded phase checklists and validation requirements
- `decisions/`: accepted architectural decisions with consequences
