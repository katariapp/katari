# Phase 3 — General Relationship Architecture

## Objective

Build the content-type/feature relationship architecture before migrating another production capability or consumer.
This phase defines how independently owned contributions are discovered, assembled, evaluated, and turned into explicit
obligations. It is intentionally generic and may break production compilation at its new boundaries.

## Milestone 3.1 — Contribution Semantics and Ownership

- [x] Define a valid content-type contribution containing identity and zero or more interaction providers.
- [x] Define provider contributions without a separate support or absence declaration.
- [x] Define a feature contribution containing prerequisite expressions, contextual inputs, shared consequences,
  specialized prerequisites and requirements, behavioral contracts, and projections.
- [x] Define ownership and identity rules for every contribution and relationship.
- [x] Define unsupported prerequisites, applicability, and incomplete downstream obligations without inferring them from
  current types.
- [x] Prove the model is expressible using anonymous synthetic concepts only.
- [x] Stop before implementing discovery.

## Milestone 3.2 — Discovery and Graph Assembly

- [x] Discover content-type and feature contributions from their owning composition boundaries.
- [x] Build one graph without a central list of known types, capabilities, or features.
- [x] Reject duplicate identities, contradictory ownership, unowned relationships, and unreachable contributions.
- [x] Verify that adding a synthetic contribution changes assembly without editing an assembler registry.
- [x] Stop before graph evaluation.

## Milestone 3.3 — Evaluation and Obligations

- [x] Evaluate capability expressions and contextual requirements generically.
- [x] Activate feature-owned shared consequences for every compatible type.
- [x] Emit actionable specialized obligations with the responsible owner and affected subject.
- [x] Keep deliberate non-applicability distinct from missing implementation.
- [x] Reject feature-specific branches in the evaluator.
- [x] Stop before contract and projection selection.

## Milestone 3.4 — Contract and Projection Selection

- [x] Select feature-owned behavioral contracts from evaluated applicability.
- [x] Select developer and documentation projections from the same evaluated relationships.
- [x] Make missing required fixtures or projections explicit obligations rather than manual checklist items.
- [x] Verify that tests exercise selection mechanics and behavior, not duplicated capability values.
- [x] Stop before production boundary changes.

## Milestone 3.5 — Dependency Boundary and Migration Cut

- [x] Establish module dependencies so contribution owners point toward the generic kernel.
- [x] Switch the intended composition boundary even if unported production code stops compiling.
- [x] Remove `EntryCapabilityCatalog`, `EntryCapabilityReport`, `supportsTypeWide`, legacy report assembly, and production
  report DI exposure rather than deprecating them behind a working facade.
- [x] Remove explicit unsupported declarations whose only purpose is to compensate for an absent provider.
- [x] Do not preserve old report APIs as a parallel authority; allow their unported consumers to fail compilation.
- [x] Record compile failures as concrete migration obligations owned by later phases.
- [x] Reject compatibility facades, fallback registries, and dual authorities introduced only to keep the build green.
- [x] Run the complete synthetic unknown-type/unknown-feature proof.
- [x] Stop before migrating Manga, Anime, or Book.

## Non-Goals

- Do not migrate real content types or feature consumers.
- Do not encode Manga, Anime, Book, Bookmarking, Downloads, or any other product-specific policy in the kernel.
- Do not preserve compilation through a parallel old/new authority.
- Do not treat the existing capability report as protected architecture.

## Exit Gate

- Unknown synthetic types, providers, and features participate without a curated edit.
- Any subset of providers is valid; provider presence means supported and absence means unsupported.
- Feature owners contribute prerequisites, consequences, obligations, contracts, and projections.
- Missing specialized work after satisfied prerequisites is actionable; a missing prerequisite creates no obligation.
- The assembler and evaluator contain no concrete product list or branch.
- The legacy catalog/report definitions and production exposure are removed; remaining references are recorded migration
  failures, not a working fallback path.
- Known compile failures, if any, correspond to recorded production migration obligations.

## Validation

- Generic contribution, assembly, evaluation, and failure-semantics tests
- Synthetic unknown type/capability/feature acceptance scenarios
- Static dependency and boundary checks for the new kernel
- Formatting and `git diff --check`
- Compilation only for modules whose migration is an explicit milestone exit gate

## Manifesto Review

Ask whether an unknown future contribution would be discovered and all its consequences and missing obligations exposed
without another curated edit. A feature-specific proof or green application build cannot substitute for that answer.

## Milestone 3.1 Completion Notes

The standalone `feature-graph` module defines only generic contribution semantics. It has no dependency on `EntryType`,
Entry interactions, concrete content types, or known product capabilities.

`ContentTypeContribution` accepts zero or more typed `CapabilityProvider` implementations and specialized adapters. No
provider is mandatory, and there is no absence declaration. `CapabilityDefinition` is owned and defined beside its
provider contract rather than added to a central catalog.

`FeatureContribution` owns integrations containing positive capability expressions, typed contextual inputs, shared
executable consequences, specialized adapter prerequisites or requirements, behavioral contracts, and projections.
Missing capability or specialized prerequisites mean inapplicable; specialized requirements are structurally separate
so Milestone 3.3 can turn missing adapters into obligations only after applicability is established.

Construction enforces stable identities, contribution-local uniqueness, and feature ownership of specialized
requirements. Cross-contribution validation is intentionally deferred to Milestone 3.2 graph assembly. Anonymous
alpha/beta tests prove valid empty and one-provider types, relationship composition, context retention, adapter supply,
and invalid ownership/identity cases without encoding current product names.

## Milestone 3.2 Completion Notes

Owner-scoped `FeatureGraphContributor` instances now submit content-type and feature contributions through a generic sink.
The sink enforces top-level ownership, while the environment remains responsible only for installing contributor modules.
The kernel contains no concrete contributor, type, capability, or feature registry.

Assembly produces a deterministic `FeatureGraph` containing discovered types, features, and consistent distributed
capability, context-input, and specialized-adapter definitions. Contributor ordering does not change graph ordering.
Duplicate type/feature identities, contradictory definitions, foreign top-level ownership, unconsumed providers, unused
adapters, and effectless integrations fail with actionable messages.

Synthetic tests add types, providers, and feature contributions through an unchanged discovery/assembly pipeline and
observe the graph expand automatically. A feature may prepare for a provider no current type implements. No prerequisite
expression is evaluated and no obligation, consequence, contract, or projection is selected; those operations remain
Milestones 3.3 and 3.4.

## Milestone 3.3 Completion Notes

The generic evaluator derives one result for every discovered content-type and feature-integration pair. Positive
`Always`, `Provided`, `AllOf`, and `AnyOf` expressions are evaluated only from contributed providers. A missing provider
produces inapplicability without an obligation; no current operation is treated as mandatory for type validity.

Satisfied capability and specialized prerequisites lead to one of three distinct states. Integrations with unresolved
contextual inputs remain conditional, retaining pending specialized requirements without prematurely turning them into
failures. Missing specialized adapters declared as requirements on statically applicable integrations produce
actionable obligations attributed to the affected content-type owner. Complete integrations become applicable and
expose their matched provider and adapter objects.

Every applicable content-type/integration pair creates edges to its feature-owned shared consequences. The edges retain
the original consequence object, so multiple types can point to one single-gate coordinator without evaluation copying,
instantiating, or routing through it. Behavioral contracts and projections are not selected; that boundary remains
Milestone 3.4.

Anonymous tests cover expression composition, ordinary inapplicability, conditional context, specialized obligations,
adapter completion, deterministic ordering, and multiple type edges referencing the same shared consequence instance.

## Milestone 3.4 Completion Notes

Artifact selection consumes the complete graph evaluation rather than asking capability questions again. It rejects a
curated, duplicate, unexpected, or stale set of evaluated relationships. Only complete context-free applicable
integrations select contracts or projections; inapplicable, conditional, and runtime-incomplete relationships select
nothing.

Feature-owned behavioral contracts are selected once per applicable content type while retaining the same executable
contract object. Contracts can operate directly from matched providers and adapters. A contract declares a typed fixture
requirement only for genuine media-specific validation input. Missing fixtures become obligations attributed to the
affected content-type owner; a shared contract requires no empty per-type opt-in.

Feature integrations declare typed projection requirements and may supply executable developer or documentation
projection implementations. A missing implementation becomes one feature-owned obligation listing all applicable
subjects it affects. Supplied projections create per-type selection edges to the same implementation object. Nothing in
the kernel makes a contract, fixture, or projection channel universally mandatory.

Anonymous tests prove that every complete compatible contribution selects and executes the same contract and projection
objects, while unsupported and incomplete relationships do not. They also cover fixture ownership, aggregated missing
projection obligations, deterministic ordering, ownership constraints, and rejection of a curated evaluation subset.
No test restates which production type supports which capability.

## Milestone 3.5 Completion Notes

`entry-interactions:api` now points toward and exports the standalone graph kernel. `EntryInteractionPlugin` combines its
existing operational registration responsibility with an owned `FeatureGraphContributor`; no default contribution or
lambda compatibility wrapper was added. Independent feature contributors remain separate inputs to application
composition, preventing feature ownership from being folded into content-type plugins.

The composition boundary now discovers and assembles all supplied contributors, evaluates the graph, selects contracts
and projections, and exposes those results beside operational interactions. The runtime DI path is switched to this
composition object. Production content-type and feature contributors are intentionally absent until Phases 4 and 5, so
their old construction paths do not compile.

The central catalog, evidence/support report vocabulary, report assembly, `supportsTypeWide`, report-driven download
policy, report DI binding, and their authority-focused tests were deleted. Anime and Book explicit unsupported outcomes
were removed because missing providers are sufficient. No deprecated API, empty contribution, fallback query, or
replacement boolean was introduced.

`migration-obligations.md` records the remaining report consumers and plugin/test boundaries by responsible owner and
phase. `:entry-interactions:spi:compileDebugKotlin` fails specifically at the retired report-based download policy. The
generic kernel tests and `:entry-interactions:api:compileDebugKotlin` pass, including a complete synthetic proof where an
unknown future type automatically receives shared consequences, contracts, and projections while another future type
automatically produces its missing specialized obligation.
