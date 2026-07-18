# Provider Binding and Dispatch

Status: Accepted

Milestone 4.4 supersedes the transitional installer and registry details below with decision
[`0018-generic-provider-index.md`](0018-generic-provider-index.md). The durable part of this decision is the single
capability binding: graph evidence and operational dispatch still derive from that one declaration.

## Context

Milestone 4.1 proved that one operational provider object can also be graph evidence, but its first installation shape put
dispatch behavior on the provider interface itself. That shape does not work when one implementation supplies multiple
independent capabilities: Manga's consumption implementation also supplies Bookmarking, and each capability needs a
different dispatch destination.

The content-type contribution must remain the single declaration. Type modules must not separately call a registry or
repeat a capability-to-dispatch mapping.

## Proposed Decision

- An Entry interaction capability binds three things owned by the provider contract: its typed graph definition, its
  provider type, and the way that provider enters operational dispatch.
- A content-type plugin declares `Capability.bind(implementation)` once per implemented capability.
- The plugin derives its graph `CapabilityProvider` collection and operational installation from the same bindings.
- One implementation object may bind multiple capabilities without combining their identities. Manga binds its
  consumption object independently as Consumption and Bookmarking.
- Plugin content-type identity, providers, specialized adapters, and contract fixtures are derived through the common
  plugin boundary; a type module does not construct a parallel `ContentTypeContribution` provider list.
- The current capability-owned installer targets the transitional operational registry. When the registry is replaced
  by the generic provider index, the binding remains the declaration and its installation target changes inside the
  shared contract boundary, not in every content type.
- A plugin containing only migrated bindings uses the default installation path. Existing production plugins override
  it only to retain their explicitly enumerated unported processors until later Phase 4 milestones.

## Consequences

- Graph participation and runtime dispatch cannot drift for a migrated provider.
- Adding Bookmarking to a future type is one binding, not a provider declaration plus a registry call.
- Sharing one implementation between capabilities does not imply that the capabilities are inseparable.
- The generic feature graph remains unaware of `EntryType`, processor categories, and dispatch mechanics.
- The old `registerXProcessor` interface remains migration scaffolding and can be removed after all processor families
  are represented by bindings.

## Milestone 4.2.1 Application

- Manga, Anime, and Book bind Consumption.
- Manga alone binds Bookmarking.
- Manga, Anime, and Book bind Progress transfer.
- Anime alone binds Playback-preference transfer.
- Open and Continue are moved onto the same binding mechanism established by this decision.

## Alternatives Rejected

- One `install()` implementation on every provider object, which conflicts when one object supplies multiple capabilities
- Separate contribution and registry calls in each type module
- Treating the combined Manga implementation as one Consumption-and-Bookmarking capability
- Adding a support boolean to disambiguate which interface on a shared object is active
- A registry-derived support report reconstructed after operational installation
