# F02 — Continue

Status: complete

## Owner and Relationship

- Feature owner: `entry-continue`
- Prerequisite: `EntryContinueCapability`
- Shared consequence: graph-gated next-child selection and dispatch through one application coordinator
- Context: the actual Entry and its current child/progress state are operation inputs, not type-wide capability facts
- Specialized requirement: none; the contributed `EntryContinueProcessor` owns media-specific next-child selection and
  reader, player, or book opening
- Presentation projection: none; Start/Resume and no-next-child vocabulary remain presentation-owned and do not decide
  applicability
- Behavioral contracts: a synthetic provider proves automatic shared dispatch, a provider with no next child produces
  `NoNext`, and provider absence remains valid `Inapplicable` behavior

Provider absence makes Continue inapplicable. It does not invalidate the content type. `Inapplicable` is deliberately
distinct from `NoNext`, which can only be returned after an applicable provider evaluates the current Entry.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry Start/Resume FAB | The callback and control are absent when Continue is inapplicable. Applicable dispatch uses the shared coordinator. |
| Library Continue control | Availability is evaluated per item, in addition to the preference and actual `canContinue` entry state. A mixed-type page does not expose the control for an inapplicable type. |
| History row Resume | The row-level Resume action is absent for an inapplicable type; cover navigation and deletion remain available. |
| History-tab reselect | Uses the structured feature result. `NoNext` selects the type-owned no-next-child label; `Inapplicable` uses the generic no-next-item outcome instead of pretending a provider found no child. |
| Entry screen model | Direct raw dispatch and the unused public `findNext` wrapper are removed. |
| Type-owned processors | Retained as the fundamental providers. Their internal reader/player/book opening is part of Continue behavior and does not expose raw dispatch to the application. |

No application production consumer imports `EntryContinueInteraction`.

## Automatic-Participation Proof

The focused feature test constructs an anonymous contribution containing a single Continue provider. Graph evaluation
selects the Continue consequence and the coordinator both finds and opens the returned child without a current-type
list. The companion cases distinguish applicable no-next state from valid provider absence.

## Manifesto Review

- Provider presence remains the only type-wide Continue support fact.
- The feature contribution, not Manga/Anime/Book branches, selects applicability.
- Entry, Library, and History presentation follows the same feature gate.
- No current-type matrix, support-label test, no-op provider, or mandatory Continue rule was introduced.
- A future type gains every shared Continue surface by contributing its provider; no application consumer requires a
  type-specific edit.

## Remaining Boundaries

- Production application compilation remains blocked by unrelated unported F03–F27 raw interactions and the Download
  Lifecycle report migration. F02 does not restore SPI visibility or add compatibility shims.
- Graph-selected production contract execution and generated documentation projections remain Phase 7 work.
