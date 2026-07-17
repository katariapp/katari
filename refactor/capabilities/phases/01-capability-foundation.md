# Phase 1 — Authoritative Capability Foundation

## Objective

Create the capability vocabulary, support result, evidence model, and deterministic query/reporting foundation without changing production consumers.

## Preconditions

- Phase 0 atlas and discrepancy review are complete.
- Capability evidence and scope decisions are accepted.
- Public source/tracker contracts that remain external inputs are identified.

## Milestone 1.1 — Capability Vocabulary and Semantics

- [x] Represent fundamental capability identity without storing derived combinations as new facts.
- [x] Keep type-wide and contextual query subjects structurally distinct.
- [x] Represent all six accepted support outcomes.
- [x] Represent authoritative evidence, deliberate absence, contextual blockers, and missing obligations without relying on booleans or nulls.
- [x] Enforce semantic invariants such as explicit owners, reasons, and contextual subjects.
- [x] Add focused API tests for valid and invalid result construction.
- [x] Do not connect the vocabulary to processor registration or production consumers yet.
- [x] Stop and review the vocabulary before continuing.

## Milestone 1.2 — Evidence Collection and Validation

- [x] Make provider registration contribute provider-backed capability evidence.
- [x] Add exceptional intrinsic declarations colocated with type composition.
- [x] Reject duplicate and contradictory evidence; leave missing facts unresolved instead of inferring absence.
- [x] Keep contextual evaluators and external source/tracker evidence out of unconditional type support.
- [x] Add synthetic registry tests for valid, empty, duplicate, and contradictory compositions.
- [x] Preserve all existing interaction behavior and support APIs.
- [x] Stop and review evidence collection before continuing.

## Milestone 1.3 — Deterministic Type Reports

- [ ] Define the reviewed fundamental capability catalog used by current provider categories and intrinsic facts.
- [ ] Assemble deterministic reports from authoritative evidence for every registered content type.
- [ ] Represent accepted intentional absence and non-applicability explicitly rather than inferring them from fallbacks.
- [ ] Surface unresolved or missing evidence without treating it as unsupported.
- [ ] Verify Manga, Anime, and Book reports against the Phase 0 atlas.
- [ ] Keep contextual support conditional and derived feature combinations out of the fundamental report.
- [ ] Stop and review the reports before continuing.

## Milestone 1.4 — Foundation Integration Gate

- [ ] Expose the inspection/query foundation through the Entry interaction composition boundary.
- [ ] Verify existing compatibility APIs and production behavior remain unchanged.
- [ ] Add registry and production-composition coverage for every registered type.
- [ ] Run the complete Phase 1 validation set.
- [ ] Update the atlas with the implemented evidence owners and remaining migration work.
- [ ] Review every Phase 1 exit gate and manifesto risk.
- [ ] Stop before Phase 2.

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
