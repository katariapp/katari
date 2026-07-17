# Phase 8 — Compatibility Removal and Boundary Enforcement

## Objective

Remove superseded compatibility paths and enforce the capability ownership model after all production consumers have migrated.

## Cleanup

- [ ] Remove superseded `supports…` compatibility APIs.
- [ ] Remove behavioral booleans from presentation models.
- [ ] Remove constant or empty capability processors made obsolete by evidence-based support.
- [ ] Remove per-type implementations of derived shared behavior.
- [ ] Remove temporary migration adapters and dual-read paths.
- [ ] Update atlas entries to their final owners.

## Enforcement

- [ ] Strengthen boundary validation against new ad hoc capability gates.
- [ ] Preserve a narrow, explained allowance for media, storage, backup, source compatibility, and wire-format branches.
- [ ] Add a simulated new-capability acceptance scenario.
- [ ] Verify that behavior, presentation, contracts, reporting, and documentation follow automatically.
- [ ] Review every manifesto success criterion.

## Exit Gate

- The capability architecture is the only behavioral support authority in migrated scope.
- Generic feature code does not branch on content type for capability availability.
- Legitimate type-specific mechanics remain in their owning boundaries.
- A simulated capability addition requires no remembered follow-up integration changes.
- Full validation passes.

## Validation

- All capability and feature contracts
- All Entry interaction module tests
- Relevant application unit tests
- `./gradlew --quiet spotlessCheck`
- `./gradlew --quiet verifyLegacySourceAbi`
- `./gradlew --quiet testFossUnitTest`
- `./gradlew --quiet checkEntryInteractionBoundaries`
- `./gradlew --quiet :app:compileFossKotlin`
- Documentation build and capability-reference verification
- `git diff --check`

## Manifesto Review

Review each success criterion explicitly. Record any deliberate remaining limitation before declaring the refactor complete.
