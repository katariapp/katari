# F25 — Viewer Settings

Status: complete

## Owner and Relationships

- Feature owner: `entry-viewer-settings`
- Fundamental prerequisite: `EntryViewerSettingsCapability`
- Shared consequences: provider discovery, Reader/Player settings hubs, provider-to-screen projection, settings search,
  entry overrides, override reset, and backup restore
- Migration relationship: copy requires both Viewer Settings and Migration providers
- Context: active profile, stored per-entry overrides, and app-owned Compose settings screens
- Specialized obligation: every surface declared by a Viewer Settings provider must have exactly one app-owned screen
  projection; the Feature fails with the surface ID when it is missing, duplicated, or orphaned
- Compatibility adapter: reset and backup restoration of the legacy Manga viewer bitfield; the bitfield is not current
  support evidence
- Behavioral contract: provider absence, multi-surface discovery, exact projection matching, override validation,
  snapshot/restore and reset; focused combination behavior covers Migration copy

One optional type provider owns the Viewer Settings surfaces genuinely supplied by that type. The provider may expose
multiple viewer engines, as Book does for Readium EPUB and HTML prose. Provider absence is valid and adds no settings
surface or projection obligation.

## Feature Boundary

Application consumers receive `EntryViewerSettingsFeature`. The former mutable `ViewerSettingsInteraction`, parallel
`EntryTypeRuntimeContribution.viewerSettingsProviders` list, and hardcoded provider-ID-to-screen map are removed.
Viewer Settings now binds through the ordinary type plugin and generic operational provider index.

Compose settings screens remain app-owned projection implementations because the provider modules cannot depend on the
application's `SearchableSettings` and `Preference` types. Runtime composition supplies the actual screen
implementations, not an ID allowlist. The Feature matches them exactly to provider surface IDs before exposing resolved
destinations. Reader/Player hubs and settings search consume those same destinations, so they cannot drift.

F27 binds every real Viewer Settings preference owner to the profile store during this ordinary runtime installation.
The Viewer Settings Feature does not expose a second preference-ownership artifact. Its named version-11 correction group
is persistence compatibility only and is resolved by F27 from the same installed owners.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Type/runtime installation | Viewer Settings is an optional ordinary plugin binding. The second runtime provider list and mutable registration facade are removed. |
| Reader/Player hubs | Both hubs render resolved Feature destinations by category. Surface support is never reconstructed from provider IDs. |
| Screen routing | Each real app screen implements one projection contribution. Missing, duplicate, and orphan projections fail with exact surface IDs. |
| Settings search | Search indexes the same resolved destinations used by the hubs; there is no second Viewer Settings screen list or map. |
| Entry overrides | Snapshot, restore, copy, and reset go through the Feature. Definitions and override scope validate persisted values before they participate. |
| Profile ownership | F27 discovers every installed provider preference owner from the same runtime construction statement. |
| Legacy preference migration | A named F27 owner group drives the version-11 profile-key correction without a provider-specific key list or a current Feature support artifact. |
| Backup | Backup snapshots only definitions applicable to the Entry type. Restore validates provider, setting ID, override scope, codec, and value. |
| Migration | When Migration also participates, copy derives the shared override definitions of the source and target Viewer Settings providers. No raw repository copy bypass remains. |
| Reset | The Feature clears every contributed provider's per-entry overrides and invokes the named legacy Manga viewer-flags reset adapter. Profile preference reset remains a separate profile concern. |

## Automatic-Participation Proof

The focused Feature behavior composes a partial type with no provider, a synthetic provider with two surfaces, and the
Viewer Settings plus Migration combination. It proves absence is valid, surfaces reach destinations, screens are
required exactly, Viewer Settings alone does not imply migration, and the combination reuses the same definitions.
Separate behavior covers unknown/non-override value rejection rather than restating support labels.

## Manifesto Review

- Provider presence is the only type-wide Viewer Settings support fact; absence is valid.
- A type contributes one provider through its ordinary plugin, while genuine multiple viewer engines remain surfaces
  inside that provider rather than duplicate capabilities or runtime lists.
- Hub, navigation, and search consume one exact provider-to-projection result; the provider-ID `when` map is gone.
- Profile preference construction enters F27 ownership discovery at the same ordinary provider installation boundary;
  entry overrides, reset, and backup derive from provider definitions; migration additionally derives from the real
  Migration provider.
- A future provider cannot be silently omitted from UI: its missing real screen projection fails with its surface ID.
- No mandatory operation, type matrix, availability flag, central support allowlist, no-op provider, silent fallback,
  or capability-label test was introduced.

## Validation

- Viewer Settings API, Entry-interactions API/SPI, Manga, Anime, Book, and root production compilation pass.
- Root Entry-interactions behavior tests pass, including multi-surface discovery and every F25 consequence.
- FOSS compilation reports no F25 diagnostic and reaches only independently owned F11/F12 and unrelated concurrent
  application failures.
- SQLDelight migration validation passes after removal of unused direct override copy/replace queries.
