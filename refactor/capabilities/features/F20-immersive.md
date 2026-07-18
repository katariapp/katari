# F20 — Immersive Browsing

Status: complete

## Owner and Relationships

- Feature owner: `entry-immersive`
- Fundamental prerequisite: `EntryImmersiveCapability`
- Shared consequences: contextual source and entry availability, catalogue and feed surfaces, long-press evidence,
  loading, provider-derived preload radius, rendering, progress persistence, and strict handle lifecycle
- Optional relationship: Immersive plus Child List
- Optional consequence: first-reading-child selection for a child-backed provider
- Optional relationship: Immersive plus Open
- Optional consequence: openable loaded-child target; F01 retains actual Open dispatch
- External context: `EntryCatalogueSource.supportsImmersiveFeed`, optional descriptive source metadata, actual returned
  Entry type, concrete source, resolved media, and runtime state
- Specialized requirement: each Immersive provider declares one stable load mode, either entry-level or
  first-reading-child-backed
- Behavioral contract: synthetic absent, zero-preload entry-level, child-backed, source-contextual, metadata-pruned,
  Open-composed, failure, and strict-lifecycle contributions

Provider presence is the only type-wide Immersive support fact. Provider absence is valid. Source opt-in remains an
independent public source-owned fact and is composed at runtime rather than copied into a type declaration. Descriptive
source entry-type metadata may prune a source-level surface, but it never rejects an actual returned Entry or creates a
provider/completeness obligation.

## Feature Boundary

Application code receives `EntryImmersiveFeature`. The graph-selected coordinator owns source-level surface evidence,
per-entry availability, preload selection, F17 child selection, media loading, renderer dispatch, progress dispatch,
F01 open-target derivation, and release. Raw provider dispatch remains inside Entry-interactions composition.

Manga contributes a child-backed image-page loader and media-specific reading progress. Anime contributes a
child-backed playback loader and media-specific playback progress. Both own their renderers. Book contributes no
Immersive provider. A future entry-level provider remains valid without Child List, and a zero preload radius is a real
provider choice rather than missing-provider fallback behavior.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Catalogue mode | The toolbar and mode reset consume Feature-composed source availability, not the raw source opt-in. |
| Feed mode | Mode button, available modes, fallback, and navigation consume the same Feature evidence. |
| Browse long press | Shared F19/F20 fallback policy consumes per-entry Immersive availability; mixed-type sources do not start Immersive for an inapplicable returned entry. |
| Long-press settings | Source supporting text consumes source-level Feature evidence. Generic priority and profile persistence remain shared preference behavior. |
| Loading | The app supplies current source and child candidates; F20 selects provider/load mode and F17 reading order and returns distinct structured outcomes. |
| Contextual absence | Provider absence, source unavailable, source opt-out, and no reading child remain distinct from provider/media load failure. |
| Preloading | Radius comes from the selected provider. Missing providers return structured inapplicability; the app no longer hardcodes the current provider value. |
| Rendering and progress | Strictly dispatch to the provider that owns the handle. Media-specific renderer and persistence behavior remain type-owned. |
| Lifecycle | App screen state owns jobs, retry sync, retention, and disposal timing; F20 owns strict provider release. |
| Open loaded child | F20 derives Immersive-plus-Open availability and target identity; F01 performs the actual Open operation. |
| Source contracts | Public opt-in, legacy adapter, source projection, and media/source interfaces remain in their existing external owners. |
| Presentation | System bars, pager mechanics, navigation, labels, icons, and messages remain shared UI/F23 concerns and never authorize Immersive. |

## Specialized Requirement Validation

Load mode is declared by the Immersive provider itself. Coordinator construction checks only child-backed providers
against the graph-selected Immersive-plus-Child-List relation and names affected types when it is missing. Entry-level
providers need no Child List provider. No operation becomes mandatory for a content type merely because current
Immersive implementations are child-backed.

## Automatic-Participation Proof

The focused contract composes one anonymous type with no binding, Immersive alone, child-backed Immersive without and
with Child List, and Immersive plus Open. It proves that an empty provider set closes source-level surfaces with a
distinct no-runtime result, an entry-level provider receives all shared consequences without another settings or UI
opt-in, source blockers remain structured, descriptive metadata only prunes the source surface, the actual Entry type
remains authoritative, media failure stays distinct, and handle lifecycle dispatch is strict.

Type-focused tests retain genuine Manga media/progress behavior. App tests retain concurrency, identity, pager, and
fallback mechanics without restating the Manga/Anime/Book support matrix.

## Manifesto Review

- Provider presence is the sole type-wide Immersive authority and provider absence remains valid.
- An empty provider set cannot advertise a source-level Immersive surface, even when source metadata is unavailable.
- Public source opt-in and runtime media remain contextual evidence rather than duplicate type flags.
- Child List and Open are independent derived relationships, not mandatory operations or another per-type opt-in.
- Entry-level and zero-preload providers remain valid partial contributions.
- Every application authorization path consumes Feature evidence; raw SPI, silent release, and preload fallback are gone.
- Descriptive source metadata only prunes a contextual source surface and never overrides returned Entry truth.
- No concrete type matrix, declaration test, support report/catalog, no-op provider, or compatibility facade was added.

## Validation

- API/SPI and every production type/root Entry-interactions module compile; the focused F20 Feature contract passes.
- FOSS application compilation reports no F20 error before stopping at the already recorded F11/F12 and unrelated
  application migration queue.
- Application production code contains no raw `EntryImmersiveInteraction` reference.
