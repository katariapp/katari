# Migration and Merge Provider Boundary

Status: Accepted

## Context

The former `EntryCapabilityProcessor` combined Migration and Merge behind two default-false methods. Manga and Anime
overrode both methods only to return `true`; Book inherited both false defaults. It contained no media-specific migration
or merge operation.

Milestone 4.2.3 split those flags into independent marker providers so their ownership could be audited separately. That
transition exposed different final classifications. Migration remains assigned to F11. Merge has no type-owned
operation or compatibility behavior and is therefore a shared product workflow rather than a fundamental type
capability. Its selection and entry-state restrictions are contextual feature policy.

## Decision

- Migration retains its independent transitional provider until F11 establishes its final operational and feature
  boundary.
- F12 removes the Merge provider, capability binding, and compatibility dispatch rather than preserving an empty marker.
- One provider-free Merge feature contribution applies to every composed content type.
- Entry type, profile, selection size, same-type requirements, and existing membership are request context. They reject
  particular operations; they do not classify a content type as unsupported.
- The Merge feature owns minimum selection size, homogeneous-type selection, existing merged-group shape, and other
  contextual rules.
- Optional Open, Continue, Download, Consumption, Bookmarking, Child List, Preview, Immersive, Library Progress, and
  other consequences remain independently provider-backed. Missing one omits only that relationship and never disables
  base Merge.

## Milestone 4.2.3 Application

- Manga and Anime independently bind both Migration and Merge using one shared type-owned provider object each.
- Book contributes neither provider; its former registered object containing two default false results is removed.
- Operational compatibility queries dispatch through separate Migration and Merge provider maps during the transition.
- The combined `EntryCapabilityProcessor` and its type-module registration calls are removed.

## F12 Application

- The audited Merge marker, binding, provider map, capability item, and support/selection compatibility methods are now
  removed. Manga and Anime retain only the separate Migration provider described above.
- One provider-free Merge contribution applies its base workflow to every discovered content type.
- Selection shape and membership are evaluated by the Merge feature for each request. Optional Download consequences
  are selected independently from the Download provider.
- Boundary validation rejects restoration of the transitional Merge support vocabulary.

## Consequences

- A future composed type receives base Merge without adding a marker or type-module edit.
- Adding Migration does not authorize or disable Merge.
- Merge consumers derive optional consequences from the owning feature graph rather than a Merge/type matrix.
- The Milestone 4.2.3 marker split remains historical scaffolding and is not the target architecture.

## Alternatives Rejected

- Keeping one combined processor with independent booleans
- Registering false providers for unsupported types
- Treating same-type or selection-shape rules as type-owned capability facts
- Claiming that the marker providers contain media-specific operations
- Deriving one capability from the presence of the other
- Retaining an empty Merge marker as type compatibility evidence
