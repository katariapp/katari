# F09 — Consumption

Status: complete

## Owner and Relationship

- Feature owner: `entry-consumption`
- Prerequisite: `EntryConsumptionCapability`
- Context-free consequences: type applicability and media-specific mutation dispatch
- Contextual consequences: Entry, Library, Updates, notification, and tracking eligibility from the requested state
  transition; changed-mutation results; and F06 lifecycle emission only for changed consume operations
- Operation context: the concrete Entry, selected children, current consumed/partial-progress state, merged-member
  ownership, requested consumed state, and the processor's exact changed-child result
- Specialized requirement: none beyond the media-specific Consumption provider
- Presentation projection: F23 owns consumed/unconsumed vocabulary. F09 decides availability and behavior only.

Consumption-provider absence is valid and makes every F09 surface inapplicable. It does not make a content type invalid,
require an absence declaration, or introduce a mandatory operation.

## Feature and Provider Boundaries

Application consumers receive `EntryConsumptionFeature`. It is the only application-facing authority for applicability,
state-transition eligibility, and mutation. Raw Consumption dispatch remains internal to root composition and type
providers.

The identical consumed/unconsumed state-transition rule is defined once in provider SPI so the graph-backed coordinator
and media providers use the same semantics. That pure predicate does not decide support: provider presence remains the
sole support fact, and application code cannot depend on SPI.

The context-free provider integration selects only type applicability and dispatch. A separate eligibility relationship
uses the current state and requested transition to authorize product actions. Mutation results and F06 event emission
are resolved independently after the media provider reports exact changed children; an unconsume or no-change result
therefore cannot accidentally acquire a marked-consumed lifecycle consequence.

Manga, Anime, and Book providers retain their genuine media differences when locating partial progress, persisting
completion, resetting locators, and synchronizing child state. Each provider returns exactly the children it changed.
The F09 coordinator interprets that structured result and emits one non-optional F06 `MarkedConsumed` event only when
the request marks children consumed and the returned list is non-empty. Type processors no longer know about Download
lifecycle policy.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry | Selection, mark-previous, and ToggleRead swipe controls derive availability from F09; merged/member mutations remain grouped by the concrete owner Entry. |
| Library | A selection containing an applicable member exposes the shared action; unsupported members receive an ordinary `Inapplicable` result. A wholly unsupported selection hides it. |
| Updates | Contextual action visibility calls F09 eligibility, and mutation routes through the same feature. |
| Notifications | Mark-consumed actions load the concrete Entry/children and call F09; provider absence is a safe inapplicable outcome. |
| Tracking sync | Enhanced-tracker progress marks the selected children through F09 rather than raw provider dispatch. |
| Download lifecycle | F09 alone turns successful consumed-state mutations into F06 events. F06 alone owns download applicability and cleanup policy. |

## Automatic-Participation Proof

The focused feature test composes an anonymous partial content type. Adding only a Consumption provider activates the
shared eligibility, mutation, and lifecycle consequences without a type list or consumer edit. A contributed type with
no Consumption binding remains valid and returns `Inapplicable`. The behavior proof also distinguishes no-change and
unconsume results and verifies that neither emits a marked-consumed event.

Type tests remain focused on genuine persistence behavior and the exact changed-child result. They do not restate which
production types support Consumption.

## Manifesto Review

- Provider presence is the only support declaration; absence is ordinary unsupported behavior.
- Shared eligibility and event orchestration are implemented once instead of repeated in app consumers or type modules.
- Entry state and selection outcomes are declared contextual evidence with explicit no-change and unconsume blockers;
  they are not Entry-State or Selection capabilities.
- Media-specific persistence remains with providers and is communicated through a structured result.
- UI, notifications, and background synchronization use one feature-owned contract.
- F06 cleanup and F23 vocabulary stay in their own owners; F09 does not absorb them or add opt-ins.
- No type matrix, mandatory operation, capability-label test, compatibility facade, or direct-type authorization branch
  was introduced.
- A future Consumption provider automatically enters all F09 consequences and reports any real persistence differences
  only in its provider.

## Validation

- Formatting, API, SPI, root main/test compilation, the focused graph-selected F09 behavior test, and diff validation
  pass with repository JDK 21 and Android SDK.
- Manga, Anime, and Book main-source compilation passes with their lifecycle-free Consumption processors.
- Older type-wide test suites retain their pre-existing Phase 5/7 harness obligations; F09 does not add compatibility
  defaults to conceal them.
- Full application compilation continues to expose only the remaining F10–F27 raw feature migrations; no application
  production source references raw Consumption dispatch.
