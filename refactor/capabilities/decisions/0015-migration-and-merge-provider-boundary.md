# Migration and Merge Provider Boundary

Status: Accepted

## Context

The former `EntryCapabilityProcessor` combined Migration and Merge behind two default-false methods. Manga and Anime
overrode both methods only to return `true`; Book inherited both false defaults. It contained no media-specific migration
or merge operation.

Migration and Merge are nevertheless independent product capabilities. A type can be compatible with either shared
workflow without supporting the other. Their selection and entry-state restrictions are feature policy, not additional
type capability declarations.

## Proposed Decision

- Migration and Merge are independent provider contracts and capability bindings.
- These contracts are markers of a type's fundamental compatibility with the corresponding shared workflow. They do
  not pretend that the type owns operations which are actually implemented by the feature.
- Marker-provider presence is the single type-owned compatibility fact. There is no default false method, support
  matrix, or explicit unsupported provider.
- The Migration feature owns source availability, current/target compatibility, library state, transfer composition,
  and other contextual rules.
- The Merge feature owns minimum selection size, homogeneous-type selection, existing merged-group shape, and other
  contextual rules.
- One type-owned object may implement both marker contracts while binding them independently. Implementing both
  interfaces does not contribute either capability until its corresponding binding is present.

## Milestone 4.2.3 Application

- Manga and Anime independently bind both Migration and Merge using one shared type-owned provider object each.
- Book contributes neither provider; its former registered object containing two default false results is removed.
- Operational compatibility queries dispatch through separate Migration and Merge provider maps during the transition.
- The combined `EntryCapabilityProcessor` and its type-module registration calls are removed.

## Consequences

- A future type may contribute Migration, Merge, both, or neither without affecting architectural validity.
- Adding Migration does not silently opt the type into Merge, and the reverse is also true.
- Phase 5 can attach every shared Migration or Merge consequence to its corresponding capability without adding type
  branches.
- The transitional combined interaction facade still evaluates selection rules until the owning feature integrations
  replace it.

## Alternatives Rejected

- Keeping one combined processor with independent booleans
- Registering false providers for unsupported types
- Treating same-type or selection-shape rules as type-owned capability facts
- Claiming that the marker providers contain media-specific operations
- Deriving one capability from the presence of the other
