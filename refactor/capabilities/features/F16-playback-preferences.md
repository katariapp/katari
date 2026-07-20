# F16 — Playback-preference Transfer

## Scope

F16 owns transfer of per-entry playback preferences through backup creation, backup restore, and same-type migration. It
does not own playback itself, progress transfer, download-option preferences, viewer-setting overrides, or the policy
that decides whether an Entry can be migrated.

## Authoritative Support

`EntryPlaybackPreferencesProcessor` provider presence is the sole support fact. Anime currently contributes the genuine
repository-backed provider; Manga and Book contribute nothing for this capability and remain valid content types.

The feature contribution requires that provider and names two base consequences:

- backup snapshot;
- backup restore.

Migration copy is a separate derived relationship requiring both Playback Preferences and Migration providers.

One root coordinator derives base and migration applicability from graph evaluation. Application consumers depend only
on `EntryPlaybackPreferencesFeature`; raw provider dispatch remains an internal composition detail.

## Structured Semantics

- Snapshot distinguishes a captured value, a supported Entry with no stored preferences, and provider absence.
- Restore distinguishes an applied snapshot from provider absence.
- Copy distinguishes a completed copy, a supported source with no stored preferences, provider absence, and a
  cross-type request.

Consequently, neutral data state never masquerades as unsupported behavior, and a type mismatch is not silently folded
into provider absence.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Backup creation | Always asks the feature for a snapshot. Serialization follows the structured result rather than an Anime check and remains independent from the chapter-data option. |
| Backup restore | Converts the stable wire model and delegates restoration to the feature. A wire field may exist for a type that does not currently provide the capability without making that type invalid. |
| Migration | Requests optional preference copy through the feature after F11 has authorized the overall migration. F16 does not redefine migration support. |
| Anime download preferences | Retained separately. Their backup gate is not playback-preference transfer authorization. |
| Legacy Anime backup conversion | Retained as wire-format compatibility, not current capability evidence. |
| Player persistence | Retained inside the Anime runtime and repository. It is the behavior that creates the state F16 transfers, not another transfer consumer. |

## Automatic-Participation Proof

The focused feature test composes an anonymous content type with zero providers, a synthetic playback-preference
provider, and the Playback Preferences plus Migration combination. Zero providers produce structured inapplicability;
Playback Preferences alone activates backup without falsely activating copy; the combination activates migration and
retains the distinction between no stored data and no provider. No test restates the production support matrix.

A future content type adds backup transfer by contributing Playback Preferences. If it also contributes Migration,
migration transfer follows automatically; the consumers require no type-specific edit.

## Manifesto Review

- Provider presence is the only support authority and provider absence is valid.
- The feature owns every shared transfer consequence; Migration composes from the two real providers rather than a
  separate type opt-in.
- Application code cannot call raw dispatch or authorize transfer through a concrete type branch.
- Data absence and type mismatch are explicit outcomes rather than no-op support signals.
- F15 Progress, F07 download preferences, F11 migration policy, and backup compatibility retain their distinct owners.
- No support matrix, capability report/catalog, mandatory operation, no-op provider, or compatibility facade was added.

## Validation

- Formatting, focused compilation/tests, boundary checks, app-consumer tests, and combined-batch validation are recorded
  by the Phase 5 milestone integration.
