# Support Result Semantics

Status: Accepted

## Context

Current absence is represented by missing providers, default false values, `Unsupported`, empty collections, nulls, and no-ops. These representations do not say whether a concept is meaningless, intentionally out of scope, contextually blocked, or simply unfinished.

The foundation and later validation need distinct outcomes without prescribing a particular sealed type or class hierarchy.

## Decision

Capability queries and reports distinguish these semantic outcomes:

- **Supported:** authoritative evidence required by the queried capability or integration exists, and every specialized obligation of that query is satisfied.
- **Intentionally unsupported:** the concept is meaningful for the subject, but product scope deliberately does not provide it. The outcome requires a durable reason and owner.
- **Not applicable:** the concept does not meaningfully apply to the subject. The outcome requires an explanation and must not be used merely because implementation is absent.
- **Contextually unavailable:** fundamental/type support may exist, but a named source, entry, selection, preference, platform, or integration condition currently blocks it.
- **Missing obligation:** prerequisite evidence says the behavior should exist, but a required provider, adaptation, consumer integration, fixture, or metadata projection is absent. This is validation failure in migrated scope.
- **Unresolved:** a temporary migration state for evidence whose product meaning has not been decided. It cannot satisfy a phase exit gate or be treated as intentional absence.

A missing provider, default false, null, empty result, or no-op is not by itself evidence of intentional unsupported or not applicable behavior. Those outcomes must be explicit decisions. Contextual evaluators may legitimately return unavailable for a particular subject while their provider registration remains valid.

Fundamental support and integration completeness remain separate. Once authoritative evidence proves a fundamental capability, that capability remains supported even if a consuming feature has a missing obligation. The derived integration reports the missing obligation, and the developer report shows both facts. It must never downgrade or hide the fundamental declaration merely to keep an incomplete feature looking consistent.

## Consequences

- Anime and Book lacking bookmark support can be recorded as intentional current product absence rather than inferred from no-op processors.
- Playback preferences for non-playback media can be recorded as not applicable when that semantic judgment is accepted.
- Anime preview without an `EntryPreviewSource` is contextually unavailable, not type-wide unsupported.
- A type that declares bookmarking and downloads but lacks required bookmark/download integration produces a missing obligation rather than silently hiding the action.
- The same type still reports fundamental Bookmarking and Downloads support, making the unfinished derived integration visible instead of erasing its prerequisites.
- Reports and contract failures can explain why behavior is absent.

## Alternatives Rejected

- A boolean supported flag: collapses deliberate absence, context, and incomplete work.
- Treating missing registration as intentional unsupported: forgotten work remains invisible.
- Treating every unavailable combination as not applicable: hides product-scope and implementation choices.
- Exposing internal exception or null behavior directly as product semantics: couples the model to existing implementation accidents.

## Affected Capabilities and Phases

- Phase 1 support-result foundation
- Phase 2 bookmark/download proof
- Phases 3–5 capability and composition migrations
- Phase 6 actionable contract failures
- Phase 7 reports and documentation states
