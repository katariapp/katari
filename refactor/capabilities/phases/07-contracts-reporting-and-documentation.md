# Phase 7 — Graph-Selected Contracts, Reporting, and Documentation

## Objective

Use evaluated feature relationships to drive behavioral contracts, developer reporting, and user-facing documentation
without turning tests or documents into another support authority.

This phase executes the test, reporting, and documentation register in
[`../migration-inventory.md`](../migration-inventory.md); it must cover source, tracker, settings, cache, profile,
notification, backup, and library-progress integrations in addition to entry interactions.

## Preparation Findings

Phase 3 established deterministic artifact selection, but deliberately deferred artifact execution. The current
production state therefore is not yet a completeness mechanism:

- behavioral contracts are opaque declarations with identifiers and optional fixture requirements;
- static selection retains the subject and fixtures but not the matched providers or specialized adapters needed by an
  executable verifier;
- contextual resolution activates consequences and delayed obligations but has no corresponding artifact selection;
- production feature contributions currently supply no developer-report or documentation projection implementation;
- only a small number of feature tests inspect selected contract identifiers, then execute separately authored tests;
  and
- `EntryInteractionComposition.featureArtifacts` has no production consumer beyond those tests.

The first Phase 7 milestone must close this architecture gap before migrating feature tests or documentation. Otherwise
the project could declare contract and projection participation without proving that any executable validator or
renderer exists.

## Architecture Gate

Decision [`0023`](../decisions/0023-contract-execution-reporting-and-documentation.md) defines the gate. Each migration
stream begins only after its corresponding generic mechanism proves the relevant properties with anonymous
contributions:

- production contract definitions and validation-only verifiers are separate but bind by exact discovered identity;
- a verifier receives the evaluated subject, matched providers, specialized adapters, and only its declared fixtures;
- every applicable production relationship executes automatically, including an unknown future content type;
- missing verifiers, contextual scenarios, fixtures, or projections become owned obligations rather than omissions;
- contextual artifacts are selected only from resolved evidence and never become unconditional type support;
- contract validation discovers validation contributions without a central suite list; and
- reporting and documentation later render a neutral evaluated model instead of querying providers or enumerating
  current types/features.

## Milestones

### 7.0 — Execution and Projection Architecture

- [x] Approve decision `0023` and the Phase 7 sequence.
- [x] Define production contract definitions, validation contribution discovery, structured execution
  results, and owned missing-artifact obligations.
- [x] Preserve the accepted optionality rule: a contract or projection is required only when its owning feature declares
  it; partial content-type support remains valid.
- [x] Carry matched providers, specialized adapters, fixtures, and resolved context through selection without exposing a
  generic capability-query API to application consumers.
- [x] Prove contract discovery and execution with anonymous future types, features, providers, context, fixtures, and
  verifiers before migrating product suites.

### 7.1 — Production Contract Migration

- [x] Census every production Feature, declared contract, undeclared shared expectation, and behavioral suite in the
  test register in [`../contract-migration-census.md`](../contract-migration-census.md).
- [x] Establish the production validation host and exact-definition binding before adding a production verifier.
- [x] Implement each feature-owned verifier once and execute it for every statically or contextually applicable
  production contribution.
- [x] Request content-type fixtures only for genuine media-specific validation input.
- [x] Keep compatibility, storage, serialization, and media-algorithm tests outside shared feature contracts.

### 7.2 — Declaration-Test Removal and Boundary Enforcement

- [x] Remove tests that merely restate provider registration, contract identifiers, or the current type matrix.
- [x] Retain infrastructure tests only for generic graph/runner semantics and owner tests for genuine behavior.
- [x] Extend boundary validation to reject central contract-suite maps, per-type support expectations, and validation
  paths that bypass evaluated selection.

### 7.3 — Developer Reporting

- [x] Derive a neutral report from graph discovery, static evaluation, contextual scenario results, artifact selection,
  execution outcomes, and obligations.
- [x] Report providers, integration states, consequences, declared blockers, contracts, projections, and responsible
  owners without converting conditional state into type-wide support.
- [x] Add deterministic human-readable rendering and a normal developer task without introducing a report query API for
  application code.

### 7.4 — Documentation Projection

- [x] Establish explicit included/excluded participation for the optional content-type-reference projection channel,
  with automatic missing classification and implementation reporting for an unknown future Feature.
- [x] Retain matched providers, specialized adapters, and contextual evidence in selected projection inputs so renderers
  do not reconstruct applicability.
- [x] Contribute feature-owned user-facing projections for the capability facts intended for the public content-type
  reference.
- [x] Bind the production projection plan to the exact production interaction composition and the authoritative Local,
  legacy-extension, and tracker registrations without copying a type or support matrix.
- [x] Generate or verify the deterministic capability section of `docs/features/content-type-reference.md` while
  retaining appropriate explanatory prose.
- [x] Verify source SDK contract documentation against contextual consumer coverage without treating descriptive source
  metadata as Entry behavior support.

### 7.5 — Repository Validation and Register Reconciliation

- [x] Integrate unresolved-obligation checks, contract execution, report generation, and documentation verification with
  normal repository validation.
- [x] Reconcile every test, reporting, documentation, and boundary surface listed in the migration inventory.
- [x] Run the unknown type/capability/feature acceptance path across static and contextual contracts, reporting, and
  documentation before handing the branch to Phase 8.

#### Preparation findings

- `testFossUnitTest` currently executes only the application test task. It does not execute the Feature Graph, Feature
  Validation, Entry-interactions production validation, or documentation-module tests.
- `spotlessCheck` already owns the lightweight `checkEntryInteractionBoundaries` dependency. Contract execution,
  reporting, and production projection verification should remain a separately named validation lifecycle rather than
  becoming formatting side effects.
- The application build workflow currently has no Entry Feature architecture step. The documentation workflow builds
  the site without verifying either graph-derived checked-in document, and documentation-only changes do not enter the
  application workflow.
- Decision [`0025`](../decisions/0025-entry-feature-repository-validation.md) proposes one root architecture aggregate
  and one narrower documentation aggregate. The architecture aggregate composes existing owner tasks without listing
  types, Features, contracts, projections, rows, or consumers.
- Decision `0025` is accepted. The two root aggregates and their explicit application/documentation workflow steps are
  implemented. Register reconciliation followed that wiring; the remaining work is the unknown-contribution acceptance
  path.
- The complete register is reconciled in [`../migration-inventory.md`](../migration-inventory.md). Shared relationships
  enter graph-selected contracts; app projections and compatibility remain app tests; media, storage, wire, and runtime
  failures after applicability remain focused owner tests rather than capability evidence.
- Reconciliation corrected two stale app-test expectations without changing production behavior: Updates consumed
  state no longer pretends to have partial progress, and notification routing recognizes both the modern payload path
  and its legacy merge-aware fallback.
- One cross-channel acceptance proof contributes an anonymous future media type, capability, Feature, static
  relationship, contextual source relationship, exact contract verifiers, applicable scenario, and feature-owned
  projections through their normal owner APIs.
- The same discovered graph executes both contracts, produces a complete obligation-free developer report, renders the
  future type and both relationships in the content-type reference, and discovers the contextual source SDK consumer.
  No layer receives a copied type, Feature, contract, row, or consumer list.
- Boundary validation initially rejected the acceptance proof as an ordinary test inspecting contract declarations.
  Naming it as contract-validation ownership made the exception structural and reviewable; no boundary rule or
  allowlist was weakened.

## Exit Gate

- Supporting contributions automatically enter every applicable shared behavioral contract.
- Missing fixtures, adapters, consumers, or projections fail with actionable ownership.
- Developer reports explain the evaluated graph rather than print a manually completed matrix.
- Public documentation cannot drift from executable feature projections.
- Tests and boundary checks cannot silently omit a newly discovered provider, projection, or external input.

## Manifesto Review

Confirm that removing duplicated capability assertions and manual documentation decisions does not weaken enforcement.
In particular, verify that validation contribution discovery does not become a renamed central checklist, contextual
scenarios do not become support declarations, and optional artifact channels do not become mandatory type operations.

Phase 7 satisfies this review. Provider absence remains ordinary inapplicability, validation contributors bind exact
feature-owned contracts, scenarios retain conditional semantics, and projection participation never enables behavior.
The acceptance proof begins only with owner contributions and reaches execution, reporting, and both documentation
projections without a current-type or current-Feature registry.
