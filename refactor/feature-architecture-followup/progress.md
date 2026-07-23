# Feature Architecture Follow-up Progress

Updated: 2026-07-23

## Current state

- Audit findings approved for remediation.
- Implementation is sequential; sub-agents are not used for execution.
- Baseline worktree was clean at `94f9a0a8c`.
- Phase 1 is complete.
- Phase 2 is complete.
- Phase 3 is next and has not started.

## Phase status

| Phase | Status | Notes |
| --- | --- | --- |
| 1. Durable participation architecture | Complete | Opaque versioned envelopes, owned preparation/delivery/discard, binding coverage, tests, and documentation. |
| 2. Migration durable consequences | Complete | Migration persists and retries opaque owner-contributed participants. |
| 3. Merge durable consequences | Pending | Next phase; must consume Phase 1. |
| 4. Media-session lifecycle | Pending | Shared event seam before type migration. |
| 5. Refresh lifecycle | Pending | Preserve Library-update batching. |
| 6. Lifecycle and consumer corrections | Pending | Profile deletion and Bookmark bypass. |
| 7. Settings projections | Pending | One installation fact for navigation and search. |
| 8. Final enforcement and review | Pending | Includes repeat audit and cleanup. |

## Milestone log

### Phase 1: durable participation architecture

- Added a durable runtime binding distinct from immediate execution bindings.
- Durable participants own preparation, versioned opaque payloads, delivery, and optional discard.
- Coordinators can prepare discovered envelopes and deliver them later without switching on participant IDs.
- Runtime construction rejects missing, duplicate, orphaned, contradictory, and wrong-delivery bindings.
- Unknown persisted participant IDs fail explicitly and are not interpreted or acknowledged by the runtime.
- Production Feature modules contribute durable bindings through the same installation unit as graph contributions.
- Added Feature Graph and Entry composition tests proving unknown durable participants enter without coordinator edits.
- Documented durable consequence ownership and persistence rules.

Validation completed:

- `./gradlew --quiet spotlessApply`
- `./gradlew --quiet :feature-graph:test :entry-interactions:testDebugUnitTest --tests 'mihon.entry.interactions.validation.EntryExecutionCompositionValidationTest'`
- `./gradlew --quiet verifyEntryFeatureArchitecture`
- `./gradlew --quiet :app:compileFossKotlin`

### Phase 2: Migration durable consequences

- Replaced Migration's fixed consequence construction and artifact-ID delivery switch with one discovered durable
  execution point.
- Progress, playback preferences, Viewer Settings, Download maintenance, and custom-cover ownership now contribute
  their own participant definitions, codecs, preparation, delivery, and discard behavior.
- Download maintenance contributes Migration option discovery, so the coordinator no longer knows how to inspect
  Download state or which Download types participate.
- Tracking contributes transition preparation through an ordinary execution point rather than a fixed Migration
  dependency.
- The Migration host now persists participant ID, schema version, and opaque payload. Existing consequence rows migrate
  to schema version 1 without changing their stable routing IDs.
- Unknown persisted participants fail delivery and remain pending; successful delivery is acknowledged and then
  discarded through the generic runtime.
- Added owner-specific execution contract verification and a composition proof that an unknown future durable
  participant joins without a Migration coordinator edit.
- Kept target source refresh and replace-mode Merge participation as defining Migration workflow steps rather than
  treating them as optional consequences.

Validation completed:

- `./gradlew --quiet spotlessApply`
- `./gradlew --quiet verifySqlDelightMigration`
- `./gradlew --quiet verifyEntryFeatureArchitecture`
- `./gradlew --quiet :app:compileFossKotlin`

## Decisions preserved

- Provider absence remains valid partial support.
- Stored-state filters remain generic and are not capability-gated.
- Custom covers remain contributed host consequences.
- Catalogue domain descriptions are Feature projections, not a bypass.
- Upcoming, public `UnifiedSource` API cleanup, and the feed serializer are deferred candidates.
