# Feature Architecture Follow-up Progress

Updated: 2026-07-23

## Current state

- Audit findings approved for remediation.
- Implementation is sequential; sub-agents are not used for execution.
- Baseline worktree was clean at `94f9a0a8c`.
- Phase 1 is complete.
- Phase 2 is complete.
- Phase 3 is complete.
- Phase 4 is complete.
- Phase 5 is complete.
- Phase 6 is complete.

## Phase status

| Phase | Status | Notes |
| --- | --- | --- |
| 1. Durable participation architecture | Complete | Opaque versioned envelopes, owned preparation/delivery/discard, binding coverage, tests, and documentation. |
| 2. Migration durable consequences | Complete | Migration persists and retries opaque owner-contributed participants. |
| 3. Merge durable consequences | Complete | Merge persists and retries opaque owner-contributed participants. |
| 4. Media-session lifecycle | Complete | Shared event seam with independently owned policy and consequences. |
| 5. Refresh lifecycle | Complete | Manual and Library refresh emit discovered new-child consequences. |
| 6. Lifecycle and consumer corrections | Complete | Profile deletion and Manga bookmarking use their Feature boundaries. |
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

### Phase 3: Merge durable consequences

- Replaced Merge's fixed consequence construction and participant-ID delivery switch with one discovered durable
  execution point carrying entry-level workflow facts.
- Tracking initialization, Download removal, and custom-cover cleanup now contribute their own participant definitions,
  payload schemas, preparation, delivery, and behavioral validation.
- The Merge coordinator no longer imports those Feature behaviors, derives a Download-capable type set, or maintains
  follow-up behavior contexts for a fixed participant list.
- The Merge host now persists participant ID, schema version, and opaque payload while retaining only the generic Entry
  target needed to commit consequences atomically with membership changes.
- Existing schema-1 queue rows retain their stable IDs and use an isolated finite compatibility adapter. Current
  owner-contributed payloads start at schema 2 and never pass through that adapter.
- Failed preparation and failed or cancelled host transitions discard prepared owner state. Unknown current
  participants fail delivery and remain pending.
- Added owner-specific execution contract verification, generic delivery coverage, and a composition proof that an
  unknown future durable participant joins without a Merge coordinator edit.
- Generalized isolated contract-subject construction so validation can bind both immediate and durable participants.

Validation completed:

- `./gradlew --quiet spotlessApply`
- `./gradlew --quiet verifySqlDelightMigration`
- `./gradlew --quiet verifyEntryFeatureArchitecture`
- `./gradlew --quiet :app:compileFossKotlin`

### Phase 4: Media-session lifecycle

- Added one operational Media Session provider contract used by Manga, Anime, Book, and both immersive runtimes to
  emit structured progress and activity facts.
- Progress persistence, History recording, Tracking synchronization, Download lifecycle behavior, and incognito policy
  now enter through independently owned execution participants rather than reader/player dependency lists.
- Incognito policy applies uniformly to all media types and suppresses Progress, History, and Tracking recording while
  leaving non-recording consequences independently selectable.
- Progress owns authoritative completion detection, Manga equivalent-child completion, and Book locator-extension
  preservation. Tracking and Download consume the authoritative completion result through declared ordering.
- Added shared behavioral validation for every selected Media Session relationship and a composition proof that an
  unknown future consequence joins without a coordinator edit.
- Added generated content-type reference projections, developer documentation, and a boundary check preventing media
  runtimes from directly restoring the migrated consequence paths.
- Kept progress reads used to restore initial reader/player position in type runtimes; only consequence writes moved
  behind the shared Feature seam.

Manifesto comparison:

- Media support is declared once by an operational provider; provider absence remains valid.
- Consequences are discovered from Feature-owned contributions, not a coordinator list or per-type matrix.
- The production report has zero unresolved contract obligations, and validation exercises behavior rather than
  restating capability labels.
- UI/runtime producers no longer own shared consequence policy, and generated documentation follows graph-selected
  truth.

Validation completed:

- `./gradlew --quiet spotlessApply`
- `./gradlew --quiet -p gradle/build-logic test --tests 'mihon.gradle.tasks.EntryInteractionBoundaryCheckTaskTest'`
- `./gradlew --quiet :entry-interactions:testDebugUnitTest :entry-interactions:manga:testDebugUnitTest :entry-interactions:anime:testDebugUnitTest :entry-interactions:book:testDebugUnitTest`
- `./gradlew --quiet verifyEntryFeatureArchitecture`
- `./gradlew --quiet :app:compileFossKotlin`

### Phase 5: Refresh lifecycle

- Added separate typed execution points for newly discovered children after manual Entry refresh and during Library
  update.
- Made refresh context explicit at every Source Refresh call site so a new user-facing caller cannot silently accept a
  default that omits manual-refresh consequences.
- Automatic Download now contributes independently owned participants and runtime bindings to both refresh contexts.
  Its app-facing API no longer exposes lifecycle methods that callers could invoke manually.
- Added one Library-update refresh session whose generic, participant-owned state preserves deferred Download batching.
  The Library worker creates and completes the session without naming Automatic Download or interpreting its state.
- Removed Automatic Download orchestration from the Entry screen and Library worker while retaining worker scheduling,
  progress notifications, update notifications, and failure recording in the app workflow.
- Added execution contract verification for immediate and deferred Automatic Download behavior, including one-time
  batch creation and completion.
- Added a composition proof that an unknown future consequence joins both refresh contexts solely through contribution
  and that its opaque Library-session state completes once.
- Documented refresh consequence ownership and the Library-session lifecycle.

Manifesto comparison:

- Source and Library refresh coordinators emit facts and discover consequences; neither maintains a Feature completion
  list.
- Download support remains an optional provider fact. Automatic Download participates only where that provider is
  present, with no per-type capability matrix or mandatory operation.
- Application consumers cannot bypass the contributed Automatic Download lifecycle through its public Feature API.
- Production composition has exact participant binding coverage and zero unresolved behavioral contract work.

Validation completed:

- `./gradlew --quiet spotlessApply`
- `./gradlew --quiet :entry-interactions:testDebugUnitTest :entry-interactions:manga:testDebugUnitTest :entry-interactions:anime:testDebugUnitTest :entry-interactions:book:testDebugUnitTest`
- `./gradlew --quiet verifyEntryFeatureArchitecture`
- `./gradlew --quiet :app:compileFossKotlin`

### Phase 6: lifecycle and consumer corrections

- Permanent profile deletion now loads every Entry owned by the profile and routes them through
  `EntryDestructiveRemovalFeature` before deleting the profile.
- Removed the profile database's fixed per-table cleanup list. Entry-owned and relational profile state follows foreign
  keys; the profile host continues to own its preference namespace.
- Audited the remaining profile state and found no independent profile-wide Feature state requiring another execution
  point. Future independent state must introduce a discoverable lifecycle boundary rather than extend profile SQL
  orchestration.
- Manga Reader bookmark mutation now calls `EntryBookmarkFeature` with the actual member Entry and pre-mutation child
  state instead of persisting through `EntryChapterRepository`.
- Added profile deletion behavior tests for successful discovered removal and transactional failure.
- Added build enforcement preventing type runtimes from restoring direct bookmark persistence and profile code from
  restoring a curated `deleteByProfile` list or omitting destructive removal.
- Updated developer documentation for consumer and profile lifecycle ownership. User-facing Profile behavior is
  unchanged, so the existing Profiles documentation requires no behavioral update.

Manifesto comparison:

- Existing capability and lifecycle declarations now reach the corrected consumers without another per-type or
  per-table completion list.
- Future destructive-removal participants are selected by their own contributions when a profile is deleted.
- Bookmark support remains an optional provider fact; the Manga consumer uses the shared Feature without creating a
  Manga-specific support flag.
- Boundary validation prevents the two known parallel authorities from returning.

Validation completed:

- `./gradlew --quiet spotlessApply`
- `./gradlew --quiet -p gradle/build-logic test --tests 'mihon.gradle.tasks.EntryInteractionBoundaryCheckTaskTest'`
- `./gradlew --quiet :app:testFossUnitTest --tests 'mihon.feature.profiles.core.ProfileManagerTest'`
- `./gradlew --quiet :entry-interactions:testDebugUnitTest :entry-interactions:manga:testDebugUnitTest :entry-interactions:anime:testDebugUnitTest :entry-interactions:book:testDebugUnitTest`
- `./gradlew --quiet verifyEntryFeatureArchitecture`
- `./gradlew --quiet :app:compileFossKotlin`

## Decisions preserved

- Provider absence remains valid partial support.
- Stored-state filters remain generic and are not capability-gated.
- Custom covers remain contributed host consequences.
- Catalogue domain descriptions are Feature projections, not a bypass.
- Upcoming, public `UnifiedSource` API cleanup, and the feed serializer are deferred candidates.
