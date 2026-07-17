# Evaluation and Specialized Obligations

Status: Accepted

## Context

The assembled graph records provider-backed prerequisites, contextual inputs, shared consequences, and specialized
requirements, but does not yet say how one feature integration relates to one content type. Evaluation must expose
ordinary unsupported behavior and forgotten follow-on work as different outcomes without introducing a matrix of known
types or features.

Shared consequences may represent a single-gate coordinator. Such a coordinator is feature-owned and shared across
types even though the graph must retain a separate applicability relationship for every compatible type.

## Proposed Decision

- Evaluate every discovered feature integration against every discovered content-type contribution. The evaluator has no
  product-specific type, capability, feature, or consequence branches.
- Evaluate positive capability expressions as follows:
  - `Always` is satisfied without a provider;
  - `Provided` is satisfied by the matching contributed provider;
  - `AllOf` requires every term; and
  - `AnyOf` requires at least one term.
- A missing capability prerequisite produces an inapplicable result and no obligation.
- Satisfied capability prerequisites plus unresolved contextual inputs produce a conditional result. Runtime context is
  not guessed or flattened into type-wide support. Specialized requirements remain pending and do not become obligations
  until contextual applicability is resolved.
- Satisfied statically evaluable prerequisites plus missing specialized adapters produce an incomplete result and one
  actionable obligation per missing adapter. The feature owner defines the requirement; the affected content-type owner
  is responsible for supplying the adapter.
- An integration is statically applicable only when its capability prerequisites and specialized requirements are
  satisfied and no contextual input remains unresolved.
- Every statically applicable integration creates one edge per shared consequence and affected content type. Each edge
  references the original feature-owned consequence object. Evaluation neither instantiates nor copies a coordinator per
  type.
- Evaluation retains matched provider and adapter objects for later composition. It does not select behavioral contracts
  or projections.
- Evaluation output is deterministic by content-type, feature, integration, requirement, and consequence identity.

## Consequences

- An unknown future provider changes applicability through the same evaluator without editing a completion list.
- A partial content type remains valid; every missing provider yields ordinary inapplicability rather than a validation
  failure.
- Genuine missing media work is attributed to the content-type owner only after the feature has established that the
  work applies.
- Context-dependent behavior remains explicitly conditional until Phase 6 supplies and evaluates real context.
- A shared coordinator can be installed once from a feature-owned consequence while retaining all type-specific
  applicability edges.
- Contract and projection selection can consume the same evaluation states in Milestone 3.4 without re-evaluating
  capability support.

## Deliberately Deferred

- Resolution of source, entry, selection, preference, platform, and external-integration context
- Installation, lifecycle, or event routing for shared consequences
- Behavioral contract and projection selection
- Production contributor and consumer migration

## Alternatives Rejected

- Treating a missing capability provider as an incomplete type
- Emitting specialized obligations before capability or contextual prerequisites establish applicability
- Flattening unresolved context into unconditional type-wide support or a permanent unsupported value
- Instantiating one shared coordinator per applicable type
- Turning the graph into a runtime event bus
- Selecting contracts or projections as part of applicability evaluation
