# Feature Architecture Remediation Plan

## Objective

Make cross-Feature consequences executable, discovered, and validated so that adding a Feature or expanding an Entry
type does not require remembering application integration lists after the primary implementation is complete.

The governing rule remains:

> Declare a capability once. Receive every common consequence automatically. Surface every exceptional obligation
> immediately.

This work corrects gaps recorded in [`findings.md`](findings.md) and must remain aligned with
[`../capability-manifesto.md`](../capability-manifesto.md).

## Approved Architectural Decisions

1. Introduce one general execution-point and participant mechanism instead of separate ad hoc registries for Library,
   Backup, Profile movement, Metadata changes, and removal.
2. Add a versioned Feature-state envelope to Entry backups so future Feature state does not require another central
   schema checklist.
3. Generic bulk Download actions operate on currently visible filtered children. The Manga reader preference does not
   control this behavior.
4. Custom covers participate as contributed host consequences rather than a standalone Feature for now.
5. Stored child-state filters remain generic and capability-independent.

## Target Model

A coordinator owns a typed execution point. Features and host integrations contribute participants through their own
composition modules:

```kotlin
val libraryAdded = executionPoint<EntryLibraryAdded>(
    delivery = AFTER_COMMIT,
)

trackingFeature.participates(libraryAdded) { event ->
    bindAutomatically(event.entry)
}

customCoverHost.participates(libraryRemoved) { event ->
    removeCover(event.entry)
}

libraryMembershipFeature.add(command)
```

Every executable participant has:

- A stable identity and owner.
- A target execution point.
- Capability and runtime-context applicability where needed.
- A runtime implementation binding.
- Explicit delivery semantics: transactional, post-commit, durable/retryable, or best-effort.
- Explicit ordering only where a real dependency exists.
- Behavioral contract coverage.

Graph validation rejects orphan participants, duplicate bindings, unknown execution points, missing runtime
implementations, and executable consequences that have no delivery path.

A production Feature is installed as one cohesive module containing its graph contribution, runtime API factories,
participants, contracts, projections, and warmups. Production composition remains the application installation boundary,
but it must not maintain separate completion lists for those elements.

## Phase R1: Executable Participation Architecture

Introduce execution-point definitions, participant declarations, applicability, runtime bindings, delivery semantics,
ordering, evaluation, reporting, and infrastructure tests in the Feature Graph and Entry interaction composition.

No approved application workflow is migrated in this phase. Compilation may expose missing bindings; compatibility
fallbacks must not weaken the target architecture.

Milestone gate:

- Runtime participation is discovered from owning contributions.
- Declared participants and implementations are paired and validated.
- A coordinator can execute all applicable participants without an artifact-ID switch or dependency checklist.
- Tests cover discovery, missing/orphan bindings, duplicates, applicability, delivery ordering, and failures.

## Phase R2: Production Composition and Enforcement

Bundle each Feature's graph and runtime participation into one installable module. Ensure production validation builds
the actual runtime boundaries and detects declared modules absent from the production installation boundary.

Classify existing shared consequences as descriptive or executable. Executable claims must use an execution point;
descriptive reporting markers must not imply runtime delivery.

Milestone gate:

- A Feature is installed once rather than through separate contributor, factory, projection, and consequence lists.
- A newly declared production module cannot be silently omitted.
- Production validation constructs specialized runtime Features and projections, not only the graph composition.

## Phase R3: Library Membership Lifecycle

Create an always-applicable Library Membership Feature/coordinator that owns preparation, category decisions, membership
persistence, default child state, commit boundaries, and structured UI results.

Migrate Entry, History, Catalogue, and Library consumers. Tracking, Merge, Download follow-up, and custom-cover cleanup
become discovered add/remove participants. Remove `EntryRemovalCleanupInteraction` and duplicate cover cleanup paths.

Milestone gate:

- Every Library membership mutation uses one Feature boundary.
- Tracking runs only after confirmed addition.
- Removal consequences are discovered and executed once.
- UI retains responsibility only for presenting choices and acting on structured results.

## Phase R4: Entry Lifecycle Operations

Introduce separate execution points for Metadata changes, destructive Entry removal, and Profile movement.

Migrate `EntryMetadataUpdateHooks`, Clear Database deletion, Profile movement, Merge participation, Download maintenance,
custom-cover cleanup, and Feature-owned profile state. Profile movement must distinguish transaction-safe participants
from post-commit external consequences.

Milestone gate:

- A Feature declares its own Metadata, removal, and Profile-move participation.
- Core coordinators no longer enumerate Feature-owned tables or cleanup implementations.
- Transactional and external work have explicit failure semantics.

## Phase R5: Backup and Restore Participation

Introduce Feature-owned snapshot and restore participants plus an additive Entry backup envelope containing stable
participant ID, schema version, and serialized payload.

Existing typed fields remain readable. New restoration prefers Feature envelopes and falls back to legacy fields.
Known state may be written in both formats while backward compatibility requires it. Unknown future envelopes do not
invalidate an otherwise usable backup.

Migrate existing Viewer Settings, Playback Preferences, Progress, Child Group Filter, Merge, Tracking, and Download
Options state. Fix tracker diagnostics to consume `Backup.allEntries()`.

Milestone gate:

- New Feature state can join backup/restore without editing creator and restorer aggregators.
- Download configuration owns its portable codec.
- Current, legacy, Anime, and profile backup diagnostics are covered.

## Phase R6: Catalogue Feature Completion

Expand `EntryCatalogueFeature` to own search, popular/latest paging, filters, global search, Migration search, normalized
failure handling, and source availability.

Raw catalogue sources remain inside approved Feature host adapters. Migrate application and data consumers and add
boundary enforcement for indirect raw dispatch through `UnifiedSource` and `SourceManager`.

Milestone gate:

- Application and data consumers cannot execute catalogue providers directly.
- Catalogue availability and behavior have one Feature authority.
- Shared contracts exercise every installed catalogue provider for applicable operations.

## Phase R7: Download Policy and Context Ownership

Move source-access resolution, availability evidence, and bulk candidate selection into existing Download Features.
Replace caller-constructed policy targets with domain requests resolved by the Feature.

Remove `MangaReaderSettingsProvider` from generic Entry Download behavior. Bulk actions use currently visible filtered
children.

Milestone gate:

- Source-access policy is resolved once.
- UI, workers, and notifications do not manufacture Download applicability evidence.
- Manga, Anime, Book, and local/stub behavior have focused contract coverage.

## Phase R8: Enforcement, Secondary Audit, and Documentation

Strengthen validation for app-local Interaction boundaries, indirect provider dispatch, executable consequences without
participants, missing runtime modules, manually duplicated integration lists, and unbound projections.

Re-audit the secondary findings from `findings.md` before classifying or changing them. Update developer architecture
documentation and executable content-type reporting. Do not exclude documentation or reporting surfaces from the audit.

Milestone gate:

- The discovered omission classes fail validation before release.
- Secondary findings have explicit accepted or rejected classifications.
- Architecture documentation describes how new types, Features, participants, and backup state are contributed.
- The implementation matches the manifesto and no migration-only scaffolding remains.

## Working Rules

- Complete phases sequentially; do not run concurrent implementation through sub-agents.
- Stop after every phase milestone before starting the next phase.
- Before stopping, compare the phase result against the manifesto and record the comparison in `progress.md`.
- State explicitly whether the user is expected to review, decide, commit, or authorize continuation.
- Compilation is an outcome of completing the architecture migration, not a reason to preserve parallel authorities.
- Do not fix current defects through standalone patches when the approved phase removes their architectural cause.
- Tests verify graph infrastructure and observable behavior; they must not restate capability declarations.
- Keep source directories grouped by cohesive ownership and mirror production structure in tests.
- Update documentation when the enduring architecture or contributor workflow changes.

