# Contributing viewer settings

Viewer settings are optional content-type behavior. A content type with no Viewer Settings provider is valid; Katari
does not render a Reader or Player settings destination for it. Do not add a boolean or an intentional-absence entry.

## Type-owned provider

The type runtime creates its concrete settings definitions and contributes one `EntryViewerSettingsProvider` through
the type's ordinary `EntryInteractionPlugin`. One type provider may contain several real viewer surfaces when the type
has several independent rendering engines.

```kotlin
val viewerSettings = DefaultEntryViewerSettingsProvider(
    type = EntryType.AUDIO,
    surfaces = listOf(AudioPlayerSettings(preferenceStore)),
)

override val providerBindings = buildList {
    add(EntryViewerSettingsCapability.bind(viewerSettings))
    // Other independent providers for this type.
}
```

Each surface owns a globally stable ID, category, display metadata, and every `ViewerSettingDefinition` used by that
viewer. A definition's `providerId` must match its surface ID. Include profile-only definitions as well as settings that
allow entry overrides: the Feature derives profile preference ownership from the complete definition collection.

Provider presence is the only type-wide support fact. Contextual values belong in setting resolution or renderer
behavior; they must not become a second availability switch on the surface.

## App-owned screen projection

Compose settings screens remain application-owned because they use the app's `SearchableSettings` and `Preference`
presentation contracts. The real screen implements `AppEntryViewerSettingsScreenProjection` and declares the same
surface ID:

```kotlin
object SettingsAudioPlayerScreen : AppEntryViewerSettingsScreenProjection {
    override val surfaceId = AudioPlayerSettings.PROVIDER_ID

    // SearchableSettings title and preference content
}
```

Runtime composition supplies the collection of actual projection implementations to `EntryViewerSettingsFeature`.
This collection is not a support allowlist: the Feature matches it exactly against contributed surfaces and fails with
the surface ID when a projection is missing, duplicated, or has no provider. The production validation environment uses
the same app projection resolver, and the build rejects a screen implementation omitted from that resolver, duplicate
entries, unknown entries, or a composition root that bypasses it. Once matched, the same resolved destination
automatically feeds the Reader/Player hub and settings search index.

Adding a provider without its genuine screen therefore cannot silently ship a working renderer with missing settings
UI. A type with no provider creates no projection obligation.

## Derived integrations

`EntryViewerSettingsFeature` owns the integrations that follow from the provider definitions:

- profile, app-state, and private preference-key ownership;
- validated per-entry override snapshot and restore;
- backup serialization and restoration;
- compatible override copy during entry migration;
- clearing every contributed surface's entry overrides.

Consumers use the Feature rather than enumerating provider IDs or calling the override repository. Legacy Manga viewer
flags are handled only by a named backup/reset compatibility adapter and are not evidence of current support.

Profile-wide preference reset is separate from clearing per-entry overrides. The general discovery and lifecycle of
preference-owner contributions belongs to the profile architecture; Viewer Settings only exposes its owned artifact.
