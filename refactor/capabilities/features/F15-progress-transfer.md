# F15 — Progress Transfer

Status: complete

## Owner and Relationship

- Feature owner: `entry-progress-transfer`
- Prerequisite: `EntryProgressCapability`
- Shared consequences: feature dispatch, backup creation, backup restoration, and migration copy
- Operation context: the concrete source and target Entries, portable progress snapshots, and source-to-target resource
  mappings
- Specialized requirement: the media provider owns conversion between portable progress state and its persisted child,
  resource, revision, and locator identities
- Behavioral contract: the shared contract is selected for every provider-backed type and exercises snapshot, restore,
  copy, valid absence, and type-mismatch behavior
- Presentation projection: none. F17 owns optional per-child labels and F23 owns progress vocabulary.

Progress-provider absence is valid. It makes transfer unavailable without making the content type invalid, requiring an
absence declaration, or suppressing unrelated migration/backup behavior.

## Feature and Provider Boundaries

Application consumers receive `EntryProgressFeature`. It is the only application-facing authority for progress-transfer
applicability and operations. Raw `EntryProgressInteraction` dispatch remains internal to provider SPI and root
composition so the coordinator can reach the selected type implementation.

The feature returns structured results:

- snapshot returns `Available(snapshot)` or `Inapplicable(type)`;
- restore returns `Applied` or `Inapplicable(type)`; and
- copy returns `Applied`, `Inapplicable(types)`, or `IncompatibleTypes(sourceType, targetType)`.

An applicable provider returning an empty snapshot is therefore distinct from provider absence. The internal dispatcher
requires the selected provider and matching Entry types; it no longer manufactures an empty snapshot, silently ignores
restore/copy, or hides a mismatched copy. Valid absence and mismatch are interpreted before dispatch by the feature.

Manga, Anime, and Book retain genuine type-owned providers. They map portable resource/child keys to stored children,
preserve media locators and revisions, merge restored state, synchronize child completion, and remap copied state to a
target Entry.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Backup creation | Requests a snapshot only when child data is included. `Available(empty)` serializes an empty supported state; `Inapplicable` leaves progress absent without fabricating support. |
| Backup restoration | Converts current and legacy wire representations at the compatibility boundary, then submits one portable snapshot to F15. Provider absence is an explicit valid result. |
| Migration | F11 retains migration eligibility and orchestration. Its use case supplies resource mappings to F15 and continues other transfers when progress is inapplicable. Mismatched Entry types remain invalid migration context. |
| Backup models | Portable snapshot-to-wire conversion remains in the backup model boundary. Wire shape and legacy conversion do not decide applicability. |
| Live persistence | Manga reader, Anime player/immersive, and Book reader persistence remain media-owned runtime behavior. They create shared progress state but do not authorize or dispatch portable transfer. |
| Child-row progress | F17 owns the independently contributed `EntryChildProgressCapability`, its labels, and Entry-screen observation. Shared storage does not make labels an F15 consequence. |
| Library summary | F22 owns contributed library-progress calculation, merged summaries, badges, and sorting/filtering inputs. |

## Automatic-Participation Proof

The focused feature test composes a partial content type with one anonymous Progress provider. That single binding
selects all four consequences and the shared behavior contract, and activates snapshot, restore, and copy without a type
list or consumer edit. The same contributed type without the provider remains valid and returns structured
`Inapplicable` results. The proof distinguishes an available empty snapshot from absence and rejects cross-type copy
before provider dispatch.

Type tests remain focused on genuine resource mapping, portable restore, and persisted media behavior. They do not
assert which production types carry a Progress label.

## Manifesto Review

- Provider presence is the sole Progress-transfer support fact; provider absence is ordinary unsupported behavior.
- Every shared consumer is named by the F15 contribution and uses one app-facing contract.
- Media-specific mapping/persistence remains in the provider; shared result semantics remain in the coordinator.
- Live playback/reading, F17 labels, F11 migration policy, F22 library summaries, and F23 vocabulary stay with their
  actual owners rather than being absorbed because they share progress data.
- A future provider automatically enters backup and migration transfer consequences without application or root type
  edits.
- No mandatory operation, per-type matrix, report/catalog, no-op provider, compatibility facade, or presentation gate
  was introduced.

## Validation

- Formatting, API/SPI/root compilation, focused root feature tests, type main compilation, application compilation,
  boundary validation, raw-consumer census, and diff validation are recorded with the combined F15/F16/F17 milestone.
