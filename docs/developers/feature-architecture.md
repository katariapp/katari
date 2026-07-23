# Entry Feature architecture

Katari models Entry behavior as a discovered relationship between content-type providers and application Features.
The purpose is to make support and its consequences follow from executable contributions instead of from a checklist
that every developer must remember.

## Layers and ownership

The architecture has four boundaries:

1. A type module contributes the providers it genuinely implements. Provider presence means support; provider absence
   is valid and means unsupported in the current version.
2. A Feature contribution declares prerequisites, common behavior, specialized requirements, contracts, projections,
   execution points, and participants. This is where relationships between capabilities are owned.
3. A Feature runtime module installs that contribution together with its application-facing Feature implementation,
   executable participant bindings, and warmups. Production installs the module once and derives those artifacts from
   it.
4. Screens, workers, notifications, and other app consumers call the application-facing `Entry…Feature`. They do not
   dispatch type providers, inspect the Feature Graph, or recreate availability rules.

In simplified form:

```kotlin
// entry-interactions:audio
class AudioPlugin : EntryInteractionPlugin {
    override val type = EntryType.AUDIO
    override val providerBindings = listOf(
        EntryOpenCapability.bind(AudioOpenProvider()),
        // No Download provider yet, so Download is simply unavailable for Audio.
    )
}

// entry-interactions root
val ExampleFeatureModule = EntryFeatureRuntimeModule(
    contributor = ExampleFeatureContribution,
    installRuntime = {
        install<EntryExampleFeature>(DefaultEntryExampleFeature(graphEvaluation, interactions))
    },
)

// app
val result = entryExampleFeature.execute(request)
```

Adding Download support later changes the Audio type contribution. Existing Download integrations, policies,
contracts, reporting, and generic UI availability are then selected from that provider automatically. Extra work is
required only when Download declares a genuine Audio-specific adapter or another specialized obligation; validation
names that missing artifact.

## Adding a content type

Create one type interaction module and contribute only the providers currently implemented. Do not add false providers,
intentional-absence markers, or a matrix saying which operations the type lacks. A type with only Open support is a
valid contribution.

Type-specific mechanics stay behind the provider SPI. Generic app code must not import the type module or branch on
the new `EntryType` to execute behavior. If adding the type reveals such a branch, move that relationship into the
Feature that owns it or contribute a specialized adapter when the mechanics cannot be shared.

## Adding a Feature

A new Feature owns one cohesive contribution and runtime module:

```kotlin
object EntryExampleFeatureContribution : FeatureGraphContributor {
    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            exampleIntegration(
                required = EntryExampleCapability,
                behavioralContracts = listOf(exampleBehaviorContract),
            ),
        )
    }
}

internal val EntryExampleFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry-example",
    contributor = EntryExampleFeatureContribution,
    installRuntime = { context ->
        val feature = DefaultEntryExampleFeature(/* graph-selected providers and host dependencies */)
        addSingletonFactory<EntryExampleFeature> { feature }
        EntryFeatureRuntimeArtifacts(
            runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { feature }),
        )
    },
)
```

Declare shared behavior from provider prerequisites rather than naming current content types. If the Feature requires a
media-specific implementation, declare a specialized requirement so a supporting type becomes incomplete visibly
instead of silently losing the behavior.

Behavioral contracts belong to the Feature and run for every graph-selected subject. Their validation contributor is
loaded through the `FeatureValidationContributor` service registry; the build rejects a declaration omitted from that
registry, duplicate entries, and unknown registrations. Tests should exercise observable behavior, not repeat which
types have providers.

The production topology remains the application installation boundary. Build validation discovers Feature contributors
and runtime modules and requires each exactly once, so the topology cannot silently become a second completeness list.

## Contributing cross-Feature consequences

When one coordinator must invoke independently owned work, it exposes a typed execution point. The affected Feature or
host contributes its own participant and runtime binding:

```kotlin
val libraryAdded = featureExecutionPointDefinition<EntryLibraryAddedEvent>(
    delivery = AFTER_COMMIT,
    failurePolicy = CONTINUE_AND_REPORT,
)

val trackingParticipant = FeatureExecutionParticipantDefinition(
    point = libraryAdded,
    prerequisites = TrackingCapability,
    behavioralContracts = listOf(trackingAfterLibraryAddContract),
)

FeatureExecutionParticipantBinding(
    definition = trackingParticipant,
    handler = FeatureExecutionHandler { event ->
        trackingFeature.bindAutomatically(event.entry)
    },
)
```

The coordinator executes applicable participants selected by the evaluated graph; it does not switch on participant or
Feature IDs. Delivery timing and failure policy belong to the execution point. Ordering is declared only for real
dependencies. Composition rejects missing, orphaned, duplicate, unknown, unreachable, or uncontracted executable
participants.

Descriptive projections are different: they report or render already-derived truth and have no execution path. Do not
use a projection ID as a runtime dispatch key.

### Durable consequences

Work that must survive process death uses the same discovered participant model with a `DURABLE` execution point. A
durable participant owns three operations:

- preparation of an opaque, versioned payload from the typed workflow event;
- delivery of that payload after the workflow host has committed it;
- optional discard of prepared state when the workflow cannot commit.

```kotlin
val migrated = featureExecutionPointDefinition<EntryMigratedEvent>(
    delivery = DURABLE,
    failurePolicy = FAIL_FAST,
)

val exampleParticipant = FeatureExecutionParticipantDefinition(
    point = migrated,
    prerequisites = ExampleCapability,
    behavioralContracts = listOf(exampleMigrationContract),
)

FeatureDurableExecutionParticipantBinding(
    definition = exampleParticipant,
    preparer = FeatureDurableExecutionPreparer { event ->
        exampleFeature.prepare(event)?.let { state ->
            FeatureDurableExecutionPayload(schemaVersion = 1, value = codec.encode(state))
        }
    },
    deliveryHandler = FeatureDurableExecutionDeliveryHandler { payload ->
        exampleFeature.apply(codec.decode(payload.schemaVersion, payload.value))
    },
)
```

The workflow coordinator asks the execution runtime to prepare all applicable participants. Its persistence host stores
each returned envelope as participant ID, schema version, and opaque payload. Delivery routes through the installed
participant binding; the coordinator never switches on participant IDs and never interprets payload schemas.

Unknown persisted participant IDs are an explicit delivery failure and remain pending for retry or compatibility
handling. They must not be acknowledged or silently discarded. If persistence of a prepared plan fails, the
coordinator asks the runtime to discard the prepared envelopes so participant-owned staging can be cleaned up.

An ordinary `FeatureExecutionParticipantBinding` cannot bind a durable point, and a durable binding cannot bind an
ordinary point. Production composition rejects missing, duplicate, orphaned, contradictory, and uncontracted durable
participants in the same way as immediate participants.

## Contributing Entry backup state

Feature-owned Entry state participates in the Backup Feature's snapshot and restore execution points. The participant
owns a stable ID, payload codec, and schema version and writes an opaque `EntryFeatureStateEnvelope`:

```kotlin
snapshot.add(
    EntryFeatureStateEnvelope(
        participantId = EXAMPLE_BACKUP_PARTICIPANT_ID,
        schemaVersion = 1,
        payload = exampleCodec.encode(state),
    ),
)
```

Generic backup orchestration does not know the Feature's fields or codec. Restore prefers the envelope owned by the
participant. Unknown future envelopes do not invalidate the rest of a backup. Existing typed fields may remain in a
finite compatibility adapter while older backups or older Katari releases require them, but they must never select
current participants.

Use a finalization participant only when restoration genuinely depends on other Entries already existing. Mutable
restore accumulation belongs to the caller-owned restore session, not a production singleton.

## App-owned projections

Some behavior requires a real app implementation that cannot live in a type module, such as a Compose Viewer Settings
screen. Such projections are specialized obligations, not separate support flags. Production composition registers the
real implementations, runtime validation matches them exactly to provider-owned surfaces, and build validation rejects
an unregistered, duplicated, or unknown projection.

Generic navigation, labels, and side effects should instead project directly from the Feature result. They do not need
another contribution when the Feature already returns all required data.

Likewise, an operation may own current-context availability and return a structured unavailable result. Presentation
may keep an action visible and handle that result without declaring separate content-type support. Add a preflight
projection only when another consumer genuinely needs availability before invocation, not merely to duplicate volatile
operation context.

## Verification and reporting

Run the complete architecture gate after changing a type, Feature, participant, contract, or projection:

```bash
./gradlew verifyEntryFeatureArchitecture
```

The gate checks boundaries and production installation, runs graph and behavioral validation, verifies generated
documentation, and generates the developer report. The content-type reference is a projection of the same evaluated
graph; do not edit generated capability truth by hand.

Before adding an allowlist or exception, ask whether an unknown future type or Feature could now be omitted without the
architecture noticing. If so, extend discovery or validation rather than documenting a new checklist.
