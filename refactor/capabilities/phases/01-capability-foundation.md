# Phase 1 — Authoritative Capability Foundation

## Objective

Create the capability vocabulary, support result, evidence model, and deterministic query/reporting foundation without changing production consumers.

## Preconditions

- Phase 0 atlas and discrepancy review are complete.
- Capability evidence and scope decisions are accepted.
- Public source/tracker contracts that remain external inputs are identified.

## Scope

- [ ] Represent fundamental capability identity without storing derived combinations as new facts.
- [ ] Represent type-wide and contextual subjects without conflating them.
- [ ] Represent supported, intentionally unsupported, not applicable, and unresolved/missing obligations as agreed in Phase 0.
- [ ] Use provider registration as evidence for provider-backed capabilities.
- [ ] Add validation for duplicate, missing, and contradictory evidence.
- [ ] Produce a deterministic capability report for registered content types.
- [ ] Preserve existing support APIs as compatibility paths.
- [ ] Add foundation and registry tests.

## Non-Goals

- No production UI migration.
- No documentation generation.
- No broad behavior corrections.
- No removal of existing support methods.
- No source SDK redesign.

## Exit Gate

- Every registered type can be inspected through the new foundation.
- Provider-backed claims cannot contradict provider registration.
- Unsupported outcomes carry the agreed semantics.
- Existing application behavior is unchanged.
- Empty, valid, duplicate, contradictory, and contextual cases are tested.

## Validation

- Focused capability API and registry tests
- Existing Entry interaction registry tests
- `./gradlew --quiet checkEntryInteractionBoundaries`
- `./gradlew --quiet :app:compileFossKotlin`
- `git diff --check`

## Manifesto Review

Confirm that the foundation centralizes evidence rather than centralizing duplicated booleans.
