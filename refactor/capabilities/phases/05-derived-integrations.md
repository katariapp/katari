# Phase 5 — Remaining Derived Feature Integrations

## Objective

Move cross-feature implications into shared feature policy so content types do not opt into every compatible feature combination independently.

## Candidate Integrations

- [ ] Download settings from Downloads plus media transfer capabilities
- [ ] Merge actions from Merge plus valid selection
- [ ] Tracking actions from entry type plus available tracker
- [ ] Preview actions from preview provider plus source support
- [ ] Immersive actions from renderer plus source support
- [ ] Missing-child behavior from child-list semantics
- [ ] Other combinations discovered and approved in the capability atlas

## Per-Integration Checklist

- [ ] State the fundamental capability requirements.
- [ ] Determine whether shared models provide enough information.
- [ ] Put shared behavior in the consuming feature.
- [ ] Define an explicit specialized obligation only when media differences require it.
- [ ] Remove independent type opt-ins for derived support.
- [ ] Add positive, negative, and missing-obligation tests.
- [ ] Update capability reporting evidence.

## Non-Goals

- Do not invent derived capabilities solely for naming convenience when a rule is sufficient.
- Do not make content types aware of every consuming feature.
- Do not force genuine specialized behavior into a lowest-common-denominator implementation.

## Exit Gate

- Derived support is computed from fundamental evidence.
- Common behavior activates without a type-specific follow-up change.
- Specialized requirements are explicit and actionable.
- Silent unsupported outcomes are removed in migrated scope.

## Validation

- Feature policy and capability composition tests
- Relevant UI and interaction tests
- Capability report assertions
- `./gradlew --quiet checkEntryInteractionBoundaries`
- `./gradlew --quiet :app:compileFossKotlin`
- `git diff --check`

## Manifesto Review

Confirm that the number of obligations grows with genuine differences rather than every feature pairing.
