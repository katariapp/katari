# Entry Plugin and Provider Identity

Status: Accepted

## Context

Phase 4 must connect operational interaction providers to content-type contributions without creating two declarations
that a developer must keep synchronized. The registry currently receives processor objects, while the graph receives
typed capability providers. Constructing separate objects or repeating a support flag would restore the memory problem.

The migration also needs an explicit association between the product's `EntryType` identity and the generic graph's
`ContentTypeId`. The generic kernel must not know that mapping, but the content-type owner must supply it.

## Proposed Decision

- Each `EntryInteractionPlugin` owns exactly one `ContentTypeContribution` and exposes its operational `EntryType`.
- The graph `ContentTypeId` is derived generically from that stable `EntryType` identity and validated at composition;
  a type module does not maintain a second identity string.
- The plugin's graph owner is the contribution owner; the default contribution path submits that one owned content type.
- Interaction processor contracts share an `EntryInteractionProvider` marker carrying their operational `EntryType`.
- A migrated dispatch-provider contract owns how its implementation is installed into the operational registry. The
  plugin installs its contributed providers generically; type modules do not repeat an Open or Continue registration.
- A plugin containing only migrated providers needs no registration override. Current plugins override registration
  only while explicitly unported processor families remain.
- A contributed interaction implementation must carry the same `EntryType` as its plugin. Composition validates this
  generically without naming Manga, Anime, Book, Open, or Continue.
- Capability definitions live beside the provider contracts they describe. `EntryOpenCapability` and
  `EntryContinueCapability` are the first migrated definitions; their presence does not make either provider mandatory.
- Manga, Anime, and Book construct each Open and Continue processor once and declare it only in their contribution. That
  same object is installed for operational dispatch by the provider contract.
- Remaining processor families stay explicit Phase 4 obligations until their contracts are decomposed correctly. They
  must not be wrapped mechanically when one processor currently hides multiple optional capabilities or no-op results.
- No dummy feature contribution is added merely to make these providers reachable before their feature owners migrate.
  Graph assembly may remain blocked until real feature relationships exist.

## Consequences

- A future content type may contribute Open, Continue, either one, or neither; contribution validity does not change.
- Migrated support is proven by the operational implementation itself, not by a second boolean or type matrix.
- Adding a migrated provider to a type does not require a second registry call.
- Incorrect cross-type provider installation is rejected before graph assembly.
- The transitional plugin still registers unported processors operationally, but that gap is named and bounded in the
  migration inventory and obligations ledger rather than hidden by an architectural fallback.

## Deliberately Deferred

- Provider definitions and contract decomposition for `T04`–`T21`
- Removal of the operational registry after every processor family has a graph-backed dispatch path
- Feature-owned Open and Continue relationships and consumer migration
- Type-owned runtime artifacts in `T22`–`T27`
- Test harness migration from the removed lambda-only plugin contract

## Alternatives Rejected

- A content-type support matrix derived separately from processor registration
- Empty production contributions used only to satisfy the plugin interface
- Separate graph and dispatch instances for the same provider
- Treating Open or Continue as mandatory because every current type implements them
- A generic legacy-registration escape hatch that could survive the migration
- Dummy consequences used only to bypass graph reachability validation
