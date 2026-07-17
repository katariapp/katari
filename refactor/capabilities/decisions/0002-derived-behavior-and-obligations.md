# Derived Behavior and Specialized Obligations

Status: Accepted

## Context

Feature intersections are currently represented by additional type-specific branches and flags. Bookmark-based downloads require downloader logic, presentation metadata, documentation, and tests to agree even though the common selection policy can operate on shared bookmark and download models.

Replacing each intersection with another opt-in would preserve the same memory problem under a larger capability vocabulary.

## Decision

The feature that understands an implication owns a rule describing:

- the fundamental capabilities and contextual inputs it requires;
- the shared consequences it can supply automatically;
- any genuine specialized provider or adapter required to complete the behavior; and
- the shared contracts selected when the rule applies.

Derived behavior is computed from the rule and authoritative evidence. Content types do not declare derived combinations.

When shared models are sufficient, the feature supplies the behavior automatically to every compatible type. When media-specific work is genuinely required, the rule emits an explicit specialized obligation. Declaring the prerequisite capability makes that obligation visible immediately; a missing obligation is an incomplete integration, not an unsupported result that may be silently ignored.

Contract selection, developer reporting, and user-facing documentation consume the same rule outcome. They must not keep parallel lists of applicable types.

## Consequences

- Bookmark actions follow bookmark support.
- Bookmark-based download selection follows Bookmarking plus Bulk Downloads, while cleanup protection follows
  Bookmarking within the download lifecycle, without downloader-specific opt-ins where shared child models are sufficient.
- Selection actions compose fundamental capability evidence with selection constraints.
- Source-backed actions compose type/provider evidence with source capability and local/stub restrictions.
- A future capability declaration activates shared behavior and tests; it either succeeds or reports the exact missing specialized obligation.
- Feature rules may produce named report entries, but those names are not additional capabilities a type declares.

## Alternatives Rejected

- One capability flag for every feature intersection: obligations grow combinatorially and remain memory-driven.
- Requiring each content type to opt into every consuming feature: reverses ownership and makes types aware of future features.
- Treating every specialized path as shared: erases legitimate media differences.
- Allowing missing specialized work to return a neutral no-op or generic `Unsupported`: cannot distinguish deliberate absence from forgotten integration.

## Affected Capabilities and Phases

- Bookmarking and downloads in Phase 2
- Selection and contextual composition in Phase 6
- Remaining cross-feature integrations in Phase 5
- Graph-selected contracts in Phase 7
- Reporting and documentation in Phase 7
