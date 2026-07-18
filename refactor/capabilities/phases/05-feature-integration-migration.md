# Phase 5 — Feature Integration Migration

## Objective

Move product features onto feature-owned contributions so applicable content types and follow-on obligations are derived
by the graph rather than remembered by feature authors or content-type modules.

The complete feature work register is `F01`–`F27` in
[`../migration-inventory.md`](../migration-inventory.md). Each row is a separate reviewable milestone unless a later
approved split makes it smaller.

## Per-Feature Milestone

- [ ] Identify the feature owner and its current consumers from the atlas.
- [ ] Reconcile the feature with its inventory row, including consumers whose code is already generic.
- [ ] Declare prerequisite expressions, shared consequences, specialized requirements, contracts, and projections.
- [ ] Migrate UI, policy, worker, setting, notification, and background consumers selected by that contribution.
- [ ] Remove direct type gates, presentation support flags, compatibility support queries, and local matrices.
- [ ] Verify automatic participation with a synthetic compatible type/provider.
- [ ] Record genuine missing adapters as obligations and stop before the next feature.

## Constraints

- The atlas is an inventory, not a runtime or migration allowlist.
- Feature vocabulary may remain presentation metadata but cannot authorize behavior.
- Shared consequences belong to the feature; content types do not opt into them individually.
- A migrating feature may break consumers until its entire declared integration path conforms.
- A feature is not complete merely because its main action works; its row's settings, worker, notification, navigation,
  maintenance, profile, backup, test, and documentation consequences must all have a disposition.

## Exit Gate

- Migrated features receive applicable subjects from graph evaluation.
- Compatible new providers activate shared consequences without type or feature edits.
- Specialized gaps are explicit obligations.
- No migrated feature maintains an independent support matrix.
- Every consequence in the completed feature row is graph-selected, explicitly contextual, or retained behind a named
  compatibility boundary.

## Manifesto Review

Confirm that the feature contribution, not contributor memory, names every consequence and exceptional requirement.
