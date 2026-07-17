# Feature Capability Refactor Plan

## Objective

Implement the architecture described by the [Feature Capability Manifesto](../capability-manifesto.md): a fundamental capability is declared or proven once, common consequences are derived by their owning features, exceptional type-specific obligations become immediately visible, and completeness no longer depends on developer memory.

This is an incremental migration. Existing behavior remains available through compatibility paths until its consumers and contracts have moved.

## Architectural Constraints

- Provider registration should be evidence of provider-backed support rather than requiring a duplicate support declaration.
- Fundamental capability facts and derived feature behavior must remain distinct.
- Type-wide support must not absorb source-, entry-, selection-, or integration-dependent conditions.
- Presentation metadata owns wording and imagery, not behavioral availability.
- Source and tracker contracts remain authoritative for capabilities they own.
- Shared feature policy owns behavior that can be expressed through shared models.
- Genuine media, storage, reader/player, backup, and compatibility differences remain in their existing boundaries.
- Unsupported behavior must be intentional and distinguishable from forgotten work.
- Each phase stops for review before the next begins.

## Phase Sequence

### Phase 0 — Capability Atlas and Design Decisions

Build a reviewed map of existing capability evidence, duplicated declarations, consumers, tests, documentation, and contextual inputs.

Classify each fact as type-wide, provider-backed, contextual, external, derived, presentation-only, or a compatibility boundary. Record discrepancies without changing runtime behavior.

Primary outcome:

- A complete capability atlas
- Agreed expected behavior for current inconsistencies
- Accepted design decisions needed by Phase 1

Exit gate:

- Every known capability has an owner and scope.
- Every content-type reference row maps to executable evidence.
- Duplicated support facts are identified.
- Contextual support is separated from type-wide support.
- Discrepancies are assigned an expected behavior and later resolution phase.

See [`phases/00-capability-atlas.md`](phases/00-capability-atlas.md).

### Phase 1 — Authoritative Capability Foundation

Introduce the capability vocabulary, structured support result, evidence model, and query surface without migrating production consumers.

Provider-backed capabilities must be derived from provider registration. Explicit declarations are reserved for intrinsic facts not proven by a provider. Compatibility APIs remain unchanged.

Primary outcome:

- A deterministic capability report for every registered content type
- Validation of duplicate, missing, or contradictory evidence
- No user-visible behavior change

Exit gate:

- Registered types produce deterministic capability reports.
- Provider registration and capability evidence cannot contradict each other.
- Support results distinguish supported, intentional absence, and non-applicability.
- Existing APIs and behavior remain unchanged.

See [`phases/01-capability-foundation.md`](phases/01-capability-foundation.md).

### Phase 2 — Bookmarking and Downloads Vertical Proof

Prove the architecture with the known `Bookmarks + Downloads` intersection.

Bookmark support must become the single fundamental fact. Bookmarked bulk download, bookmark actions, and bookmark-protected cleanup must be derived from it and the surrounding feature capabilities.

Production support remains unchanged during the proof: Manga supports bookmarks; Anime and Book do not.

Primary outcome:

- A synthetic downloadable content type gaining bookmarks automatically receives every common bookmark/download integration
- Duplicated presentation and download opt-ins are removed for this slice

Exit gate:

- Bookmark support is represented once.
- Download-bookmarked behavior is derived.
- Presentation cannot independently enable or hide it.
- A test configuration adding Anime bookmark support activates the expected UI policy, candidate selection, cleanup policy, and contracts without changing Anime download code.

See [`phases/02-bookmark-download-proof.md`](phases/02-bookmark-download-proof.md).

### Phase 3 — Core Type-Wide Capability Migration

Migrate stable Entry interaction capabilities whose support is type-wide or directly proven by processor registration.

Candidate areas include open/continue, consumption, progress, downloads, bulk download, bookmarks, merge, migration, child-group behavior, library filtering, playback preferences, and update eligibility.

Each capability moves with its consumers and tests so that dual truth is not left behind.

Primary outcome:

- Type-wide support is no longer repeated across processors, capability booleans, and presentation
- The runtime support matrix agrees with reviewed product intent

Exit gate:

- Migrated capabilities have one evidence source.
- Generic UI consumers use the capability query surface.
- No migrated behavioral support remains in presentation metadata.
- Manga, Anime, and Book outcomes are covered by matrix tests.

See [`phases/03-core-capability-migration.md`](phases/03-core-capability-migration.md).

### Phase 4 — Contextual and External Capability Composition

Compose source-, entry-, selection-, and external-integration inputs without turning them into static type claims.

Candidate areas include preview, immersive browsing, related entries, latest feeds, local/stub restrictions, tracker support, entry-specific download options, and selection-specific merge/migration actions.

Primary outcome:

- Shared queries explain support using the relevant runtime context
- Existing source and tracker contracts remain authoritative inputs

Exit gate:

- Contextual results identify their enabling or blocking evidence.
- Source-dependent behavior is not reported as universally type-supported.
- Screens no longer duplicate the same source/type composition.
- Extension and tracker compatibility is preserved.

See [`phases/04-contextual-capabilities.md`](phases/04-contextual-capabilities.md).

### Phase 5 — Remaining Derived Feature Integrations

Move cross-feature implications into shared feature policy.

Each derived behavior must identify its fundamental requirements, common policy, and any genuine specialized provider requirement. Examples include download settings, merge actions, tracking actions, immersive actions, preview actions, and missing-child presentation.

Primary outcome:

- Feature combinations are computed rather than stored as additional opt-ins
- Specialized obligations become explicit

Exit gate:

- Derived support is not independently declared.
- Adding fundamental evidence activates every common consequence.
- Specialized requirements produce actionable unsupported results.
- Silent feature-specific omissions are removed in migrated scope.

See [`phases/05-derived-integrations.md`](phases/05-derived-integrations.md).

### Phase 6 — Capability-Driven Feature Contracts

Create the enforcement layer that automatically subjects supporting content types and capability combinations to shared behavioral expectations.

Downloads is the first comprehensive contract target because its product policy has already been unified. Further contracts cover consumption, bookmarks, merge/migration, preview, immersive browsing, and filtering.

Primary outcome:

- Capability claims automatically select relevant shared contracts
- Missing fixtures or required adapters fail clearly

Exit gate:

- Fundamental and derived support select applicable contract scenarios.
- Declaring support cannot silently avoid shared behavioral tests.
- Type-specific tests remain focused on genuine media behavior.
- Failure messages identify the missing capability obligation.

See [`phases/06-feature-contracts.md`](phases/06-feature-contracts.md).

### Phase 7 — Documentation and Capability Reporting

Make user-facing capability documentation a projection of executable product truth and provide a developer-readable integration report.

The documentation path must remain deterministic and must not require starting the Android application or resolving live sources.

Primary outcome:

- `content-type-reference.md` is generated or verified from capability metadata
- Contributors can inspect fundamental, contextual, derived, and unmet support

Exit gate:

- Validation fails when documentation disagrees with executable capability metadata.
- Supported, unavailable, and source-dependent states are represented.
- Reports show affected types, automatic consequences, and specialized obligations.
- Public documentation remains user-facing rather than exposing provider internals.

See [`phases/07-documentation-reporting.md`](phases/07-documentation-reporting.md).

### Phase 8 — Compatibility Removal and Boundary Enforcement

Remove superseded support APIs and duplicated facts after every consumer has migrated. Strengthen automated boundary enforcement against new ad hoc capability gates while preserving legitimate media and compatibility branches.

Primary outcome:

- The capability architecture is the only support authority in migrated scope
- Future omissions are prevented by validation rather than convention

Exit gate:

- Superseded `supports…` APIs and behavioral presentation flags are removed.
- Generic feature code does not branch directly on content type.
- Legitimate storage, backup, source compatibility, and media branches remain explicitly allowed.
- A simulated new capability automatically reaches behavior, presentation, contracts, reporting, and documentation.
- Every manifesto success criterion is reviewed and full validation passes.

See [`phases/08-enforcement-cleanup.md`](phases/08-enforcement-cleanup.md).

## Milestone Protocol

Every phase can contain multiple milestones. `status.md` identifies the only active milestone.

At each milestone:

1. Inspect current Git state and durable workspace files.
2. Restate active scope and non-goals.
3. Implement only the active checklist.
4. Run focused tests.
5. Run required integration, boundary, compilation, or documentation checks.
6. Compare the changes against the manifesto.
7. Update the atlas, decisions, phase checklist, and status.
8. Stop before the next milestone.

## Validation Ladder

Use validation in proportion to the active phase:

- `git diff --check`
- Focused unit tests for changed capability owners and consumers
- Entry interaction API, registry, and type-module unit tests
- Application unit tests for migrated UI or policy
- `./gradlew --quiet checkEntryInteractionBoundaries`
- `./gradlew --quiet :app:compileFossKotlin`
- `./gradlew --quiet spotlessCheck`
- Documentation build and capability-reference verification when documentation infrastructure changes
- Broader CI-style validation at integration and final milestones

The exact commands and results belong in `status.md` at each milestone.

## Main Risks

- **Capability explosion:** storing every feature combination as another capability
- **False centralization:** replacing scattered booleans with a central set of duplicated booleans
- **Lost context:** representing source- or entry-dependent support as type-wide
- **Circular ownership:** making content types aware of every consuming feature
- **Big-bang migration:** removing compatibility paths before consumers move
- **Documentation authority:** allowing documentation to authorize behavior instead of reflect it
- **False confidence:** treating a capability matrix as a substitute for behavioral contracts
- **Over-classification:** forcing media formats, backup schemas, or compatibility branches into feature capability policy

Each phase review must explicitly check for these risks.
