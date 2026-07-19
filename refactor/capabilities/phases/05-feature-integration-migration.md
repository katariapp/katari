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
- [x] `F06` — Download Lifecycle complete. See
  [`../features/F06-download-lifecycle.md`](../features/F06-download-lifecycle.md).
- [x] `F07` — Download Settings and Options complete. See
  [`../features/F07-download-settings-and-options.md`](../features/F07-download-settings-and-options.md).
- [x] `F08` — Download Maintenance complete. See
  [`../features/F08-download-maintenance.md`](../features/F08-download-maintenance.md).
- [x] `F09` — Consumption complete. See [`../features/F09-consumption.md`](../features/F09-consumption.md).
- [x] `F10` — Bookmarking complete. See [`../features/F10-bookmarking.md`](../features/F10-bookmarking.md).
- [x] `F13` — Update Eligibility complete. See
  [`../features/F13-update-eligibility.md`](../features/F13-update-eligibility.md).
- [x] `F14` — Library Filtering complete. See
  [`../features/F14-library-filtering.md`](../features/F14-library-filtering.md).
- [x] `F15` — Progress Transfer complete. See
  [`../features/F15-progress-transfer.md`](../features/F15-progress-transfer.md).
- [x] `F16` — Playback-preference Transfer complete. See
  [`../features/F16-playback-preferences.md`](../features/F16-playback-preferences.md).
- [x] `F17` — Child List complete. See [`../features/F17-child-list.md`](../features/F17-child-list.md).
- [x] `F18` — Child Group Filtering complete. See
  [`../features/F18-child-group-filtering.md`](../features/F18-child-group-filtering.md).
- [x] `F19` — Preview complete. See [`../features/F19-preview.md`](../features/F19-preview.md).
- [x] `F20` — Immersive Browsing complete. See [`../features/F20-immersive.md`](../features/F20-immersive.md).
- [x] `F21` — Related Entries complete. See
  [`../features/F21-related-entries.md`](../features/F21-related-entries.md).
- [x] `F22` — Library Progress Summary complete. See
  [`../features/F22-library-progress-summary.md`](../features/F22-library-progress-summary.md).
- [x] `F23` — Type Presentation complete. See
  [`../features/F23-type-presentation.md`](../features/F23-type-presentation.md).
- [x] `F24` — Library-update Notifications complete. See
  [`../features/F24-library-update-notifications.md`](../features/F24-library-update-notifications.md).
- [x] `F25` — Viewer Settings complete. See [`../features/F25-viewer-settings.md`](../features/F25-viewer-settings.md).
- [x] `F26` — Media-cache Maintenance complete. See
  [`../features/F26-media-cache-maintenance.md`](../features/F26-media-cache-maintenance.md).
- [x] `F27` — Profile Preference Ownership complete. See
  [`../features/F27-profile-preference-ownership.md`](../features/F27-profile-preference-ownership.md).
- [x] `F12` — Complete in `5e67ce793`. See
  [`../features/F12-merge.md`](../features/F12-merge.md).
- [ ] `F11` — `F11.0`-`F11.1` committed; `F11.2` transaction/consequence semantics accepted. See
  [`../features/F11-migration.md`](../features/F11-migration.md).

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
