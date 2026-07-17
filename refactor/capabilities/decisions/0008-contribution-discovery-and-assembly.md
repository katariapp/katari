# Contribution Discovery and Graph Assembly

Status: Accepted

## Context

Phase 3.1 defined independently owned content-type and feature contributions. Phase 3.2 must collect them and build one
deterministic structural graph without teaching the kernel which product types, capabilities, or features exist.

The environment necessarily installs modules into the application, but that installation seam must not become a second
capability or feature registry. Ownership must also be enforceable rather than represented only by a string on the
submitted object.

## Proposed Decision

- Each owning module exposes an owner-scoped `FeatureGraphContributor`.
- Contributors submit top-level content-type and feature contributions through a generic sink. The sink rejects a
  contribution whose declared owner differs from the contributor owner.
- The environment supplies contributors to the kernel. Production DI, generated aggregation, or another installation
  mechanism may provide that collection later; the graph kernel does not select concrete contributors.
- Discovery collects raw contributions, and assembly produces deterministic ordered content-type, feature, capability,
  context-input, and specialized-adapter views.
- Distributed definitions with the same identity coalesce only when their owner and runtime type agree. Contradictory
  definitions fail assembly.
- Duplicate content-type or feature identities fail assembly regardless of owner.
- A supplied capability provider must be consumed by at least one feature integration. Otherwise the graph has noticed a
  claimed capability with no owned consequences and fails as unreachable.
- A supplied specialized adapter must be required by at least one feature integration.
- A feature integration must contribute at least one consequence, specialized requirement, behavioral contract, or
  projection.
- A feature may declare a relationship before any content type supplies its prerequisite provider. Preparing shared
  feature behavior for future support is valid.
- Assembly records structure only. It does not evaluate prerequisite expressions or create obligations.

## Consequences

- Adding a content type, provider, or feature relationship through an installed contributor changes the graph without
  changing the discovery or assembly implementation.
- The same graph is produced regardless of contributor ordering.
- Content-type contributors cannot impersonate feature owners, and feature contributors cannot impersonate type owners.
- A provider cannot silently exist outside the feature relationship model.
- Production contributor installation remains a dependency-wiring concern for the Phase 3.5 boundary cut, not a place
  for capability-specific policy.

## Deliberately Deferred

- Capability-expression evaluation for each content type
- Context value resolution
- Specialized-obligation materialization
- Consequence, contract, or projection selection
- Runtime provider lookup
- Production contributor aggregation and migration

## Alternatives Rejected

- A central assembler list of known content types, capabilities, or features
- Accepting duplicate identities and selecting the first registration
- Treating conflicting owner/type definitions as equivalent because their string id matches
- Allowing provider contributions that no feature owns or consumes
- Requiring at least one current provider before a feature relationship can be declared
- Hiding product-specific contributor installation inside the generic graph kernel
