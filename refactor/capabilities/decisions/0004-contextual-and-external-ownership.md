# Contextual and External Capability Ownership

Status: Accepted

## Context

Preview, immersive browsing, latest feeds, related entries, tracking, download options, local/stub restrictions, selection actions, and platform features depend on inputs outside a stable content-type declaration. Source and tracker contracts are also public or integration-owned boundaries.

Copying their answers into a static type catalog would create stale claims and could break extension or tracker compatibility.

## Decision

Public source and tracker capability contracts remain authoritative for the facts they own. The Entry capability architecture consumes them as external evidence and does not redeclare their support per content type.

Contextual queries compose, as applicable:

- provider-backed or intrinsic type evidence;
- source capability interfaces and source metadata;
- the actual entry and resolved media;
- selection shape and member constraints;
- tracker support and authentication state;
- local/stub restrictions;
- user configuration; and
- platform or renderer support.

The result identifies both enabling evidence and the blocking condition. Type-level reporting may state that a feature is conditional, but it must not claim that every source or entry supports it.

`supportedEntryTypes` source metadata remains descriptive. The type of each returned entry is authoritative and source metadata does not prove feature capability support.

SDK or tracker contract changes require their own compatibility decision. The internal refactor alone does not authorize redesigning public integration APIs.

## Consequences

- Anime preview combines its preview provider with `EntryPreviewSource` and preference state.
- Immersive availability combines a source opt-in, a type renderer, and loadable media.
- Tracking combines an entry type with the available tracker's declared types.
- Merge and migration actions combine per-entry support with selection constraints and available target sources.
- Local/stub restrictions remain explainable contextual blockers rather than negative type capabilities.
- Documentation can project source-dependent support without resolving live sources.

## Alternatives Rejected

- Copying source/tracker support into the type capability declaration: creates dual truth and loses runtime context.
- Treating one supporting source as universal type support: produces false capability claims.
- Moving public source contracts into app-internal interaction processors: breaks ownership and compatibility boundaries.
- Omitting contextual reasons from support results: leaves consumers to reconstruct the same conditions independently.

## Affected Capabilities and Phases

- Preview, immersive, related entries, latest feeds, tracking, download options, local/stub restrictions, and selection behavior
- Phase 4 contextual composition
- Phase 5 feature-owned derived actions
- Phase 7 conditional reporting and documentation
