# Entry Feature Repository Validation

Status: Proposed

## Context

The architecture now has separate executable surfaces for graph semantics, validation semantics, the exact production
contract host, developer reporting, generic documentation projection, and checked-in production documentation. They
have been run explicitly during Phase 7, but they are not one normal repository gate.

The current `testFossUnitTest` task executes the application test suite only. It does not execute the Feature Graph,
Feature Validation, Entry-interactions, or documentation-module test tasks. `spotlessCheck` does execute
`checkEntryInteractionBoundaries`, but formatting is not the right lifecycle or failure category for contract execution,
report generation, or production documentation verification. The documentation workflow also builds the site without
first verifying either graph-derived checked-in document.

Consequently, a contributor can currently change a contribution, relationship, verifier, projection, or generated
document and pass the normal workflow that happens not to own the affected validation surface. The architecture is
discoverable, but the repository does not yet guarantee that all of its discovered consequences are evaluated before a
change is accepted.

## Proposed Decision

### Two explicit repository gates

- The root project owns `verifyEntryFeatureDocumentation`, a narrow verification lifecycle that depends on the
  production content-type-reference verifier and source SDK consumer-coverage verifier.
- The root project owns `verifyEntryFeatureArchitecture`, the complete Entry Feature architecture lifecycle. It depends
  on:
  - `checkEntryInteractionBoundaries`;
  - Feature Graph infrastructure tests;
  - Feature Validation infrastructure tests;
  - generic documentation planner and renderer tests;
  - the Entry-interactions production contract and reporting tests;
  - deterministic developer-report generation; and
  - `verifyEntryFeatureDocumentation`.
- These tasks compose validation surfaces by architectural ownership. They do not enumerate content types, Features,
  capabilities, contracts, verifiers, projections, rows, or source SDK consumers. Discovery inside each surface remains
  the authority for what participates.

### Workflow integration

- The application build workflow executes `verifyEntryFeatureArchitecture` as a named step with its own failure
  category. Existing formatting, application tests, ABI verification, migrations, and release assembly retain their
  existing responsibilities.
- The documentation workflow executes `verifyEntryFeatureDocumentation` before building API references and the site.
  This is required independently because documentation-only pull requests do not execute the application build
  workflow.
- `verifyEntryFeatureArchitecture` is not attached to `spotlessCheck`. The existing lightweight static boundary check
  remains attached there, while behavioral and projection evaluation stays in the explicit architecture lifecycle.
- Neither aggregate is attached to `testFossUnitTest`. The application-owned documentation verifiers reuse the FOSS
  test runtime, so making the application test task depend back on the aggregate would obscure task ownership and risk
  a dependency cycle.

### Execution and artifacts

- The production contract test remains the executable success gate for all selected contracts and obligations.
- Developer-report generation remains a build artifact rather than a checked-in authority. It runs in repository
  validation so renderer failures and unresolved report obligations cannot remain an optional local check.
- The content-type reference and source SDK consumer-coverage table remain checked in. Their verification tasks compare
  them with exact production projections and never rewrite files during validation.
- Gradle task dependencies may overlap compilation inputs, but no validation rule is reimplemented in a second task.
  Gradle's task graph and up-to-date behavior provide execution reuse within one invocation.

## Consequences

- One local command evaluates every architectural completeness surface established in Phases 3 and 7.
- An unknown future type, Feature, provider, contextual integration, verifier, or projection enters validation through
  its owning contribution; the aggregate task does not need a corresponding support-list edit.
- Documentation-only changes cannot bypass projection verification simply because the application workflow is filtered
  out.
- Failures identify whether the problem is formatting, static boundaries, graph mechanics, contract execution,
  reporting, generated documentation, ordinary application behavior, or release assembly.
- Adding an entirely new architectural validation channel still requires intentionally composing that channel into the
  repository lifecycle. Adding another participant to an existing channel does not.

## Alternatives Rejected

- Assuming `testFossUnitTest` executes every module's tests
- Attaching all architecture validation to `spotlessCheck`
- Attaching the aggregate to the application test task and creating an implicit or cyclic lifecycle
- Running generated-document verification only in the application workflow
- Duplicating contract, Feature, type, projection, or documentation-consumer lists in Gradle or CI
- Checking the generated developer report into source control as another product-truth representation

## Affected Phases

- Phase 7.5 implements and reconciles these repository gates.
- Phase 8 confirms no legacy validation path or handwritten support authority can bypass them.
