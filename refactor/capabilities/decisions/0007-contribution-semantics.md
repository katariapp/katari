# Generic Contribution Semantics

Status: Accepted

## Context

Phase 3 needs a product-agnostic kernel before any real content type or feature migrates. The kernel must represent a
partially implemented content type as valid, derive support only from actual providers, and let features own the
relationships that turn those providers into shared consequences and exceptional obligations.

The committed evidence/report prototype cannot define this model: it depends on `EntryType`, a central capability
catalog, explicit absence outcomes, and type-wide reports.

## Proposed Decision

- The kernel lives in a standalone `feature-graph` module with no dependency on Entry interactions or concrete product
  types.
- A content-type contribution contains stable identity, an owner, zero or more typed capability providers, and zero or
  more specialized adapters.
- Every provider-backed capability is defined by its owning API as a stable typed definition. There is no central catalog.
- Any provider subset is valid. Provider presence means supported; absence means unsupported and creates no declaration
  or obligation.
- A feature contribution owns one or more integrations. Each integration declares a positive capability expression,
  contextual inputs, shared executable consequences, specialized adapter prerequisites or requirements, behavioral
  contracts, and projections.
- A missing specialized prerequisite makes the relationship inapplicable, just like a missing capability provider. It
  represents genuine type-owned participation without turning that participation into missing work.
- A missing prerequisite makes an integration inapplicable. A missing specialized adapter becomes an obligation only
  when it was declared as a requirement and the prerequisites are satisfied.
- Content types do not list consuming features. They reference a feature only when supplying genuinely specialized work
  through an adapter contract owned by that feature.
- Consequences, contracts, and projections are opaque executable objects to the kernel, not descriptive support labels.
- Contribution-local duplicate identities and foreign ownership of specialized requirements are rejected at
  construction. Cross-contribution validation belongs to graph assembly in Milestone 3.2.

## Consequences

- A synthetic type with no providers or one provider is valid.
- A feature can express intersections and alternatives without creating derived capability flags.
- Context is retained as a typed input rather than flattened into type-wide support.
- The kernel can later select behavior, contracts, and projections from the same relationship.
- The existing catalog, report, and explicit absence model are not dependencies of the new module.

## Deliberately Deferred

- Contribution discovery
- Cross-contribution identity validation
- Graph assembly and applicability evaluation
- Obligation materialization
- Runtime dispatch or dependency injection
- Migration of any production content type or feature

## Alternatives Rejected

- Reusing `EntryCapabilityCatalog` as the kernel registry
- Making Open, Continue, or any other provider mandatory
- Requiring explicit unsupported declarations
- Letting content types enumerate every compatible feature
- Representing consequences only as report metadata
- Keeping the old report as a fallback query surface
