# Phase 5 — Feature Integration Migration

## Objective

Move product features onto feature-owned contributions so applicable content types and follow-on obligations are derived
by the graph rather than remembered by feature authors or content-type modules.

The complete feature work register is `F01`–`F27` in
[`../migration-inventory.md`](../migration-inventory.md). Each row is a separate reviewable milestone unless a later
approved split makes it smaller.

## Per-Feature Milestone

Current progress:

- [x] `F01` — Open committed in `106fec52e`. See [`../features/F01-open.md`](../features/F01-open.md).
- [x] `5.0` — Application feature-access boundary committed in `83b2f93e7`.
- [x] `F02` — Continue complete. See [`../features/F02-continue.md`](../features/F02-continue.md).
- [x] `F03` — Download Runtime complete. See [`../features/F03-download-runtime.md`](../features/F03-download-runtime.md).
- [x] `F04` — Download Actions complete. See [`../features/F04-download-actions.md`](../features/F04-download-actions.md).
- [x] `F05` — Automatic Downloads complete. See
  [`../features/F05-automatic-downloads.md`](../features/F05-automatic-downloads.md).
- [ ] `F06`–`F27` — Not started.

## Architecture Gate 5.0 — Application Feature Access

- [x] Keep app-facing feature contracts, shared operation models, and host-implemented runtime ports in
  `entry-interactions:api`.
- [x] Move provider-backed operational facades and their aggregate into `entry-interactions:spi`.
- [x] Keep SPI and Feature Graph dependencies off the application's Entry-interactions compile classpath.
- [x] Remove raw facade and graph-evaluation dependency-injection exposure from root composition.
- [x] Derive forbidden application symbols from public SPI declarations instead of maintaining an interaction allowlist.
- [x] Reject raw interaction dispatch contracts declared in the application-facing API.
- [x] Make remaining raw application consumers explicit boundary failures for later `F02`–`F27` migration.

No product feature is migrated as part of this gate. Compilation failures created by the access cut are its intended
output and must be resolved through feature-owned contracts rather than by exporting SPI again.

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

- Application code cannot compile against provider SPI, graph evaluation, or raw operational facades through the root
  Entry-interactions dependency.
- Migrated features receive applicable subjects from graph evaluation.
- Compatible new providers activate shared consequences without type or feature edits.
- Specialized gaps are explicit obligations.
- No migrated feature maintains an independent support matrix.
- Every consequence in the completed feature row is graph-selected, explicitly contextual, or retained behind a named
  compatibility boundary.

## Manifesto Review

Confirm that the feature contribution, not contributor memory, names every consequence and exceptional requirement.
