# Support Result Semantics

Status: Accepted

## Context

Current absence is represented by missing providers, default false values, `Unsupported`, empty collections, nulls, and
no-ops. Provider absence is sufficient to establish that the type does not currently support the fundamental capability,
but the other representations can still hide whether an applicable feature is contextually blocked or incomplete.

The foundation and later validation need distinct outcomes without prescribing a particular sealed type or class hierarchy.

## Decision

Capability and feature-integration queries distinguish these semantic outcomes:

- **Supported:** the fundamental capability provider exists, or all prerequisites and obligations of the queried
  integration are satisfied.
- **Unsupported:** the fundamental capability provider does not exist. This is a valid state and requires no explicit
  reason or absence declaration.
- **Not applicable:** an integration's declared prerequisites are not satisfied for the subject. This creates no
  obligation and does not make the type incomplete.
- **Contextually unavailable:** fundamental/type support may exist, but a named source, entry, selection, preference, platform, or integration condition currently blocks it.
- **Missing obligation:** prerequisite evidence says the behavior should exist, but a required provider, adaptation, consumer integration, fixture, or metadata projection is absent. This is validation failure in migrated scope.
- **Unresolved:** a temporary legacy-migration state where existing evidence cannot yet be interpreted safely. It is not
  part of the intended final provider-backed model.

A missing provider is authoritative unsupported evidence for its fundamental capability. Default false, null, empty
result, and no-op behavior inside a registered provider are not equivalent to provider absence and must not masquerade as
working support. Contextual evaluators may legitimately return unavailable for a particular subject while their provider
registration remains valid.

Fundamental support and integration completeness remain separate. Once authoritative evidence proves a fundamental capability, that capability remains supported even if a consuming feature has a missing obligation. The derived integration reports the missing obligation, and the developer report shows both facts. It must never downgrade or hide the fundamental declaration merely to keep an incomplete feature looking consistent.

## Consequences

- Anime and Book lacking bookmark providers means Bookmarking is currently unsupported without an extra declaration.
- Playback-preference integrations are not applicable to types without the prerequisite provider.
- Anime preview without an `EntryPreviewSource` is contextually unavailable, not type-wide unsupported.
- A type that declares bookmarking and downloads but lacks required bookmark/download integration produces a missing obligation rather than silently hiding the action.
- The same type still reports fundamental Bookmarking and Downloads support, making the unfinished derived integration visible instead of erasing its prerequisites.
- Reports and contract failures can explain why behavior is absent.

## Alternatives Rejected

- A boolean supported flag separate from provider registration: duplicates capability truth.
- Requiring an explicit absence declaration for every missing provider: makes partial type development invalid and creates
  another matrix to maintain.
- Treating missing downstream work as ordinary unsupported after prerequisites are satisfied: forgotten integration work
  remains invisible.
- Exposing internal exception or null behavior directly as product semantics: couples the model to existing implementation accidents.

## Affected Capabilities and Phases

- Phase 1 support-result foundation
- Phase 2 bookmark/download proof
- Phase 3 relationship architecture and Phases 4–6 production migrations
- Phase 7 actionable contract failures
- Phase 7 reports and documentation states
