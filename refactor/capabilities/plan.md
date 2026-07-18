# Feature Capability Architecture Plan

## Objective

Build the architecture described by the [Feature Capability Manifesto](../capability-manifesto.md) before continuing
capability-by-capability migration.

The architecture must discover content types, fundamental capabilities, feature integrations, derived consequences,
specialized obligations, behavioral contracts, and projections from their owning contributions. Adding a future type,
capability, or feature must change the evaluated relationship graph without adding it to a second central allowlist.

Compilation is not the organizing constraint during the architectural migration. Intermediate compile failures are
acceptable when they expose code that has not yet been moved behind the new boundaries. The build becomes green by
making the code conform to the architecture, not by weakening the architecture to preserve the old code.

## Why the Previous Sequence Was Wrong

The previous plan introduced evidence and reports, proved one Bookmarking/Downloads slice, and then began migrating
capabilities individually. The general feature-integration graph, obligation model, and graph-selected contracts were
deferred until later phases.

That order allowed each migration to create local completion logic. A hardcoded list of Open, Continue, Bookmarking,
Downloads, and Bulk Downloads was briefly introduced as a production completion contract. It required a contributor to
remember to add the next capability and therefore reproduced the original problem. Repeated type tests also restated
provider registration as capability truth.

The correction is not a larger list. The correction is to build the general relationship architecture first and make all
later migrations use it.

## Non-Negotiable Architectural Properties

### One discoverable contribution model

- Content types contribute their identity, zero or more interaction providers, media-specific adapters, and presentation
  vocabulary.
- Features contribute capability requirements, shared consequences, contextual inputs, specialized obligations,
  behavioral contracts, and projections.
- The graph assembler discovers both sides. It does not know about Manga, Anime, Book, or a curated subset of features.
- Adding a contribution automatically changes graph evaluation.

### No interaction is intrinsically mandatory

- A type contribution is valid with any subset of interaction providers, including only one during early development.
- Open and Continue are provider-backed capabilities like Downloads, Bookmarking, and future interactions.
- Their presence on every current production type does not make them an architectural requirement for future types.
- Product release requirements, if any, are separate explicit policies and do not define type validity.

### Optional capabilities are provider-backed

- Operational provider registration is authoritative evidence.
- A provider may prove multiple genuine capabilities when it implements those contracts.
- Provider absence means unsupported and requires no parallel absence declaration.
- No per-type support matrix repeats provider-presence facts.

### Features own the relationship graph

- A feature declares the capability expression and contextual inputs it consumes.
- It declares the shared behavior it supplies automatically.
- It declares any specialized adapter or fixture that compatible media must provide.
- It declares the behavioral contracts and projections selected when the integration applies.
- Content types do not opt into every consuming feature.

### Obligations are first-class graph results

- A satisfied capability expression activates its shared consequences automatically.
- Missing specialized work produces an actionable obligation associated with its owner and affected type.
- Missing prerequisites make the relationship inapplicable; they do not make the type invalid and do not create an
  obligation.
- Missing UI, worker, policy, contract fixture, or projection participation is not represented as generic unsupported.
- Unreachable, contradictory, duplicate, and unowned graph contributions fail evaluation.

### Context is not flattened

- Source-, entry-, selection-, preference-, platform-, and external-integration inputs remain contextual.
- The graph can represent conditional feature applicability without turning it into type-wide support.
- Existing source and tracker contracts remain authoritative inputs.

### Tests do not become another truth source

- Infrastructure tests verify contribution discovery, graph evaluation, obligation generation, and failure semantics.
- Shared behavioral contracts are selected by evaluated graph applicability.
- Type tests verify genuine media behavior and specialized adapters.
- Tests do not repeat lists of which types support which capabilities.

### Compilation is an outcome

- Architecture milestones may leave known compile failures while consumers and providers are unported.
- Failures must be recorded as migration obligations, not hidden with fallback flags or duplicate facades.
- A phase may require structural validation without requiring application compilation.
- Full compilation becomes mandatory only after the affected dependency path has been migrated.

## Architectural Target

The final dependency direction is:

1. Entry-type contributions provide identity and whatever fundamental interaction providers currently exist.
2. Feature contributions declare capability expressions, contextual inputs, consequences, specialized requirements,
   contracts, and projections.
3. A generic assembler discovers contributions and builds the relationship graph.
4. A generic evaluator determines applicable integrations and emits obligations.
5. Runtime consumers, validation, behavioral contracts, developer reporting, and documentation consume evaluated graph
   results rather than maintaining their own type gates.

Neither the assembler nor evaluator contains capability-specific or entry-type-specific lists.

## Phase Sequence

### Phase 0 — Capability Atlas and Product Decisions — Complete

The existing atlas and accepted product decisions remain useful input. They describe current evidence, consumers,
contextual inputs, discrepancies, and expected outcomes.

Their role is diagnostic. The atlas is not the executable architecture and must not become a support matrix used by
runtime code.

See [`phases/00-capability-atlas.md`](phases/00-capability-atlas.md).

### Phase 1 — Evidence and Reporting Prototype — Complete, Subject to Replacement

The existing capability vocabulary, evidence records, support outcomes, deterministic report, and provider-derived
facts remain available as experimental components.

They are not protected as the final design. Phase 3 may replace them where they encourage a report-centric architecture,
manual completion lists, or duplicated support assertions.

See [`phases/01-capability-foundation.md`](phases/01-capability-foundation.md).

### Phase 2 — Bookmarking/Downloads Learning Slice — Complete, Not the General Architecture

The slice proved valuable product rules:

- Bookmark providers are independent from consumption providers.
- Shared features can derive common bookmark/download behavior.
- Presentation flags must not authorize behavior.
- Synthetic capability evidence can expose shared consequences.

It did not prove that unknown future features enter a general relationship graph. Its feature-specific policy and tests
are migration input, not the architecture template.

See [`phases/02-bookmark-download-proof.md`](phases/02-bookmark-download-proof.md).

### Phase 3 — General Relationship Architecture — Complete

Build the architecture kernel before migrating another real capability group.

Primary work:

- Define a valid entry-type contribution with zero or more independently discoverable interaction providers.
- Define generic fundamental-provider contributions without duplicated support declarations.
- Define feature contributions containing prerequisite expressions, contextual inputs, shared consequences, specialized
  obligations, behavioral contracts, and projections.
- Build contribution discovery, graph assembly, evaluation, and actionable obligation reporting.
- Reject duplicate, contradictory, unowned, unreachable, and manually curated participation.
- Demonstrate the mechanism with anonymous synthetic types, capabilities, and features rather than known product names.
- Establish module and dependency boundaries before adapting existing consumers.
- Retire the central catalog/report authority at the dependency cut rather than carrying it beside the new graph.

Exit gate:

- The assembler/evaluator contains no Manga, Anime, Book, Bookmarking, Downloads, or other product-specific branches.
- Adding a synthetic type, capability provider, or feature contribution changes the graph without another registry edit.
- A synthetic feature automatically selects shared consequences and contracts for every compatible synthetic type.
- Missing specialized work produces an actionable obligation.
- A synthetic type with only one provider remains valid; absent capabilities are unsupported and create no obligation.
- No central completion allowlist or per-type capability matrix exists.
- `EntryCapabilityCatalog`, `EntryCapabilityReport`, and `supportsTypeWide` are removed at the Phase 3.5 boundary cut;
  they do not survive as deprecated production facades.
- The milestone may finish with documented compile failures caused by unported production code.

See [`phases/03-general-relationship-architecture.md`](phases/03-general-relationship-architecture.md).

### Pre-Phase 4 — Migration Readiness Gate

Phase 3 completion does not authorize migration from the interaction registry alone. Before Phase 4 begins, the
[`migration inventory`](migration-inventory.md) must account for every operational provider, support-like method,
direct type gate, independent provider registry, source/tracker input, and feature consequence found by the repository
census.

The inventory is a control surface, not a runtime list. Its rows assign ownership and a migration disposition so later
phases cannot mistake compilation or an already-generic call site for complete participation.

Exit gate:

- Every finding outside the current interaction boundary has a reviewed classification.
- Every current provider and support-like default maps to a type or contextual register row.
- Every UI, policy, worker, setting, notification, navigation, backup, migration, profile, cache, and documentation
  consequence maps to a feature register row.
- Every direct type branch is assigned to migration, projection, compatibility, storage, or tooling.
- Phase 4 has not started.

### Phase 4 — Entry-Type Composition Migration

Move real Manga, Anime, and Book composition onto the architectural boundary.

Primary work:

- Execute the complete `T01`–`T27` register in `migration-inventory.md`, not only the interaction-processor list.
- Contribute Open, Continue, and every other existing interaction through the same provider-backed model.
- Preserve genuine media-specific implementations and adapters.
- Replace provider registration and composition patterns superseded by the generic contribution model.
- Let compile errors enumerate old consumers and providers that still violate the boundary.

Exit gate:

- Every production entry type is composed through the same generic contribution contract.
- Any subset of providers forms a valid contribution.
- Capability support comes from providers, not explicit absence declarations or a matrix.
- The generic architecture remains unaware of concrete entry types.
- The Entry interaction composition path compiles; unrelated application consumers may remain unported.

See [`phases/04-entry-type-composition-migration.md`](phases/04-entry-type-composition-migration.md).

### Phase 5 — Feature Integration Migration

Move UI, policies, workers, settings, notifications, and cross-feature behavior into feature-owned graph contributions.

Primary work:

- Establish the application feature-access boundary before continuing after the first Open slice: application code sees
  feature contracts, shared models, and host-implemented runtime ports, while provider SPI, graph evaluation, and raw
  operational dispatch remain behind the root composition module.
- Execute the complete `F01`–`F27` register in `migration-inventory.md`; an already-generic consumer still requires an
  explicit selected or compatibility disposition.
- Use the atlas and inventory to identify feature ownership, not to create a runtime allowlist.
- Migrate each feature as a contribution with prerequisites, consequences, obligations, contracts, and projections.
- Replace direct type checks, presentation support flags, compatibility facades, and local capability queries.
- Make shared consequences operate over every compatible entry type discovered by the graph.
- Keep feature vocabulary and imagery in presentation-owned metadata.

Exit gate:

- Migrated features receive applicable types from graph evaluation.
- Adding a compatible provider activates the migrated feature without editing that feature or the content type again.
- Missing specialized adapters and consumer paths are explicit obligations.
- No migrated feature keeps its own support matrix.

See [`phases/05-feature-integration-migration.md`](phases/05-feature-integration-migration.md).

### Phase 6 — Contextual and External Integration

Add source, entry, selection, preference, platform, tracker, and other external inputs to the same feature graph without
flattening them into type-wide facts.

Primary work:

- Execute the complete `C01`–`C24` register in `migration-inventory.md`.
- Model contextual subjects and blockers.
- Preserve existing source and tracker ownership.
- Migrate preview, immersive, download options, related entries, latest feeds, local/stub restrictions, and selection
  constraints.
- Represent genuine media-specific adapters as obligations selected by feature applicability.

Exit gate:

- Contextual results name their enabling evidence and blockers.
- Features consume evaluated contextual integrations rather than rebuilding conditions in screens.
- Source-dependent behavior is never reported as unconditional type support.
- External integration contracts remain authoritative.

See [`phases/06-contextual-and-external-integration.md`](phases/06-contextual-and-external-integration.md).

### Phase 7 — Graph-Selected Behavioral Contracts and Projections

Complete behavioral enforcement, developer reporting, and documentation projection using the graph declarations already
owned by features.

Primary work:

- Execute the test, reporting, and documentation register in `migration-inventory.md`.
- Execute shared contracts selected by evaluated feature applicability.
- Require specialized fixtures only when an applicable feature declares them.
- Remove type tests that merely restate capability declarations.
- Produce developer reports listing automatic consequences and unmet obligations.
- Generate or verify user-facing capability documentation from projections owned by the same feature contributions.

Exit gate:

- Supporting types automatically enter every applicable shared behavioral contract.
- Missing fixtures or specialized adapters fail with actionable obligations.
- Reports answer which types, features, consequences, and obligations are involved.
- Public documentation cannot drift from evaluated product truth.

See [`phases/07-contracts-reporting-and-documentation.md`](phases/07-contracts-reporting-and-documentation.md).

### Phase 8 — Legacy Removal, Boundary Enforcement, and Build Completion

Remove residual compatibility code and complete enforcement after the central catalog/report authority was already
retired at the Phase 3.5 boundary cut and production contributions and consumers have migrated.

Primary work:

- Rerun the repeatable census probes from `migration-inventory.md` and classify every remaining match.
- Remove residual support APIs, presentation flags, migration adapters, and compatibility paths.
- Strengthen module boundaries against new type gates and parallel support declarations.
- Resolve remaining compile failures by moving code into architectural conformance.
- Run full product validation only after the architecture owns the complete dependency path.

Exit gate:

- The application compiles because production code follows the architecture.
- No fallback or duplicated authority exists solely to preserve the migration.
- Generic feature code does not branch directly on content type.
- A simulated unknown type, capability, and feature automatically enter graph evaluation, obligations, contracts,
  reporting, and documentation.
- Every manifesto failure mode and success criterion is reviewed.
- No census candidate is unclassified, including candidates outside `entry-interactions`.
- Full CI-style validation passes.

See [`phases/08-legacy-removal-and-build-completion.md`](phases/08-legacy-removal-and-build-completion.md).

## Milestone Protocol

Every milestone must answer these questions before implementation:

1. Does this change extend the general contribution/graph/evaluation model, or only name current capabilities?
2. Would an unknown future type, capability, or feature participate without another curated edit?
3. Is a feature owner declaring its consequences, or is a central coordinator learning feature-specific behavior?
4. Are tests verifying machinery or behavior, or merely restating declarations?
5. Is compilation pressure introducing a second authority, fallback, or compatibility shape that violates the target?

If any answer is unfavorable, stop and redesign before editing production consumers.

At each milestone:

1. Inspect Git state, manifesto, plan, status, and the active phase.
2. State the architectural invariant being established.
3. Implement only that invariant and the minimum proof required to challenge it.
4. Record known compile failures as migration obligations when compilation is not an exit gate.
5. Run structural checks and architecture-mechanism tests appropriate to the phase.
6. Compare the result with every manifesto rejection test.
7. Update decisions, phase state, and status.
8. Stop before the next milestone.

## Validation Policy

Validation serves the architecture; it does not define capability truth.

Always appropriate:

- `git diff --check`
- Formatting for changed files
- Focused tests of generic graph mechanics and failure semantics
- Focused behavioral tests for genuine media implementations
- Static dependency and boundary checks that apply to migrated modules

Conditionally appropriate:

- Module compilation when the module is expected to conform at the current milestone
- Application compilation only after the affected consumer path has been migrated
- Existing legacy tests only when they do not freeze architecture that is being replaced

Not acceptable as completion evidence:

- Per-type capability assertion matrices
- Tests that repeat provider registration as `supports == true`
- A green build achieved through duplicate compatibility paths
- Passing one feature-specific vertical slice without generic discovery proof

Every known failing command must be recorded with the architectural obligation that keeps it failing. Build restoration is
required by Phase 8, not used to constrain Phase 3's design.

## Main Risks and Rejection Rules

- **Curated participation:** reject any architecture requiring a second list of known capabilities, types, or features.
- **Accidental mandatory behavior:** reject treating support shared by all current types as part of future type validity.
- **Feature-specific kernel:** reject generic modules that contain Bookmarking, Downloads, Manga, Anime, Book, or other
  product-specific policy.
- **Report-centric design:** reject a read-only matrix presented as the architecture when features and obligations do not
  participate.
- **Test duplication:** reject capability-value assertions that repeat provider presence or current type outcomes.
- **Compile-first compromise:** reject compatibility code whose only purpose is to keep old consumers compiling across a
  boundary that should break.
- **Consumer-first migration:** reject moving more screens or workers before the graph can discover feature consequences.
- **Capability explosion:** reject derived feature combinations represented as new type opt-ins.
- **Lost context:** reject source-, entry-, selection-, or integration-dependent facts flattened into type-wide support.
- **Circular ownership:** reject content types that enumerate the features consuming them.
- **Premature generalization from a slice:** reject assuming one successful feature policy proves unknown features are
  automatically covered.

These rules override the convenience of incremental compilation and local feature completion.
