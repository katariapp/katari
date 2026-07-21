# Contract Execution, Reporting, and Documentation Flow

Status: Accepted

## Context

The graph already selects contract and projection declarations for complete context-free relationships. That selection
is not yet an executable validation architecture:

- `FeatureBehaviorContract` identifies a contract and optional fixture requirements but cannot execute behavior;
- selected contracts retain fixtures but discard the matched providers and specialized adapters that established
  applicability;
- contextual relationships cannot select contracts or projections after runtime evidence resolves them;
- no production projection currently renders a developer report or the content-type reference; and
- a few feature tests inspect selected contract identifiers before running separately authored assertions.

Migrating features onto those shapes unchanged would reproduce the original memory problem. A declaration could claim
that a shared contract exists while no generic mechanism proves that the contract is implemented, discovers every
applicable production contribution, or verifies its documentation projection.

Behavioral validation code also must not become runtime application code merely so it can be attached to a production
graph object. Product declarations and validation implementations have different lifecycles, but both must be
discoverable and missing validation work must be reported rather than hidden by an absent test.

## Proposed Decision

This decision supersedes the statement in decision `0010` that the production
`FeatureBehaviorContract` object is itself executable. It preserves `0010`'s selection, ownership, fixture, projection,
and optionality rules while moving executable validation behavior into a separately discovered validation
contribution.

### Contract declaration and execution

- A feature contribution owns stable behavioral contract definitions. Those definitions state what behavior must be
  verified and which genuinely media-specific fixtures, if any, are required.
- Executable contract verifiers are validation contributions owned by the same feature. They bind to the exact contract
  definition and are discovered generically from the validation classpath; no central contract, feature, or type list
  selects them.
- A missing verifier for a selected contract is a feature-owner obligation. It is not interpreted as a passing test, an
  unsupported content type, or a reason to require per-type opt-in.
- The generic runner supplies the evaluated subject, matched providers, specialized adapters, and requested fixtures to
  the verifier. It does not know feature semantics and does not reconstruct support.
- A verifier runs once for every selected production relationship. Provider absence remains ordinary inapplicability;
  only an applicable relationship can create validation obligations.
- Validation results are structured and framework-neutral. The normal repository test/build integration translates
  failures into its test or task reporting; runtime modules do not ship JUnit/Kotest code or test doubles.
- Verifier discovery, scenario planning, and execution live in a validation-only module that depends on the runtime
  graph. Runtime application modules do not depend on that module.

### Contextual relationships

- Context-dependent contracts and projections are selected only from a resolved context snapshot. Static conditional
  candidates never claim runtime applicability.
- A feature-owned validation scenario supplies representative typed evidence for a contextual contract. The same
  scenario is applied to every statically compatible production contribution; content types do not enumerate or opt
  into contextual tests.
- Missing feature-owned scenarios are feature-owner obligations. Media-specific fixtures remain content-type-owner
  obligations only when the contract explicitly declares such a requirement.
- Runtime reports describe conditional inputs and possible blockers from the graph. Validation reports may additionally
  show scenario outcomes. Neither report promotes a successful sample context into unconditional type support.

### Reporting and documentation

- One neutral reporting model is derived from the graph, static evaluation, resolved validation scenarios, selected
  artifacts, and obligations. Renderers consume that model; they do not query providers or maintain support rules.
- The developer report includes discovered types and providers, integration state, matched prerequisites, conditional
  inputs and blockers, selected consequences, contracts, projections, and actionable obligations with their owner.
- User-facing documentation is produced by feature-owned documentation projections selected through the same evaluated
  relationships. Presentation vocabulary may vary by content type but cannot authorize behavior.
- The checked-in content-type reference keeps explanatory prose where useful, while its capability facts are generated
  or verified as a deterministic projection. A handwritten row cannot become an independent support declaration.
- Projection channels remain optional. A feature creates a developer or documentation obligation by declaring that
  projection requirement; the kernel does not impose one universal projection checklist on every feature.

### Validation boundary

- Normal repository validation discovers the production graph and validation contributions, rejects unresolved
  obligations, executes every selected contract, renders/verifies projections, and fails on undiscovered or duplicate
  validation bindings.
- Runtime graph obligations and validation-only obligations retain distinct categories and ownership, but enter one
  validation issue collection and one success result. A validation gate cannot pass by checking only one category or
  only the executions that were ready to run.
- Infrastructure tests verify generic discovery, selection, execution, ownership, ordering, and failure semantics with
  anonymous contributions. They do not repeat the Manga/Anime/Book support matrix.
- Type-owned tests remain for media algorithms, compatibility, serialization, storage, and specialized adapters. They
  do not substitute for graph-selected shared contracts.

## Consequences

- Adding a provider to a new or existing content type automatically enrolls it in every applicable shared verifier and
  projection without editing validation or documentation type lists.
- Adding a feature requires its owner to declare relationships and contribute any promised verifier/projection; missing
  artifacts fail with feature ownership rather than being forgotten.
- The production graph remains free of test-framework code while contract execution stays discoverable and enforceable.
- Contextual behavior is testable without encoding live source, tracker, preference, or device state as type-wide
  support.
- Reports and public documentation become views of evaluated relationships, not parallel authorities.

## Alternatives Rejected

- Leaving contracts as marker objects while individual tests merely assert their identifiers
- Embedding JUnit/Kotest suites and test doubles in runtime feature contributions
- Maintaining a central map from contract identifiers to test classes
- Requiring every content type to provide an empty contract adapter or contextual test opt-in
- Treating one successful contextual scenario as unconditional support
- Generating reports or documentation from `EntryType` enumeration and handwritten capability columns
- Making a developer or documentation projection mandatory for every integration regardless of feature ownership

## Affected Phases

- Phase 7 establishes and migrates this complete flow.
- Phase 8 verifies that no legacy test matrix, support report, or handwritten documentation authority remains.
