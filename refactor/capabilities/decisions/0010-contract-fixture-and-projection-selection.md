# Contract, Fixture, and Projection Selection

Status: Accepted

## Context

Applicable feature relationships must automatically select their shared behavioral contracts and developer or
user-facing projections. Selection cannot use another per-type matrix, and missing validation or projection work cannot
be hidden by omitting a test or document manually.

Contracts sometimes need genuine media-specific fixture input. Projections, by contrast, are shared feature-owned
implementations that run once for every applicable relationship. The architecture must distinguish those ownership and
multiplicity rules.

## Proposed Decision

- Select contracts and projections only from complete, context-free applicable results produced by graph evaluation.
  Selection consumes those results and never re-evaluates capability expressions.
- Verify that the evaluation covers exactly every content-type/feature/integration relationship in the supplied graph.
  Reject duplicate, missing, unexpected, or stale relationships rather than accepting a curated subset.
- A feature-owned behavioral contract is an executable object. It is selected once per applicable content type while
  retaining the same contract object.
- Shared contracts consume matched providers and adapters directly unless they explicitly declare a typed
  media-specific fixture requirement. Content types do not provide a fixture merely to opt into a shared contract.
- A content type may supply a fixture implementation for a feature-owned fixture definition. A missing required fixture
  produces one obligation for the affected content-type owner and identifies every selected contract using it.
- A feature integration may declare zero or more typed projection requirements and supply executable implementations for
  them. Missing implementations become obligations only when at least one relationship is applicable.
- A missing shared projection produces one obligation owned by the feature, with all affected content-type subjects. It
  is not multiplied into one implementation task per type.
- A supplied projection is selected once per applicable content type while every selection retains the same
  feature-owned implementation object.
- Contracts, fixtures, and projections remain optional architectural concepts. No contract, fixture, developer
  projection, or documentation projection is globally mandatory merely because current features commonly use it.
- Artifact identity, ownership, runtime type, reachability, and deterministic ordering are validated generically.
- The kernel selects opaque executable objects but does not execute contracts, render reports, or write documentation.

## Consequences

- Adding a compatible provider automatically enrolls its content type in every declared shared contract and projection
  after the relationship becomes applicable.
- Contract fixtures grow only with genuine media differences, not with every supporting type.
- Missing validation inputs and projection implementations are explicit owned work rather than absent tests or stale
  documentation discovered after release.
- Developer reporting and documentation can later execute the selected projection objects without maintaining a support
  matrix.
- A single contract or projection object may serve many type-specific selections, matching the same single-gate rule as
  shared runtime consequences.

## Deliberately Deferred

- Production contract fixtures and projection implementations
- Contract execution and fixture lifecycle
- Developer-report rendering
- Documentation generation or verification
- Normal-build enforcement of unresolved obligations
- The production dependency boundary cut

## Alternatives Rejected

- Selecting artifacts from a caller-supplied subset of graph relationships
- Re-running support checks inside test, reporting, or documentation consumers
- Requiring every content type to opt into shared contracts with an empty fixture
- Treating every current feature as requiring the same projection channels
- Emitting one shared projection implementation obligation per affected type
- Using tests or documentation rows as the authority for capability support
