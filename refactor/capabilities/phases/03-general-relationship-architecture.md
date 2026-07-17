# Phase 3 — General Relationship Architecture

## Objective

Build the content-type/feature relationship architecture before migrating another production capability or consumer.
This phase defines how independently owned contributions are discovered, assembled, evaluated, and turned into explicit
obligations. It is intentionally generic and may break production compilation at its new boundaries.

## Milestone 3.1 — Contribution Semantics and Ownership

- [x] Define a valid content-type contribution containing identity and zero or more interaction providers.
- [x] Define provider contributions without a separate support or absence declaration.
- [x] Define a feature contribution containing prerequisite expressions, contextual inputs, shared consequences,
  specialized requirements, behavioral contracts, and projections.
- [x] Define ownership and identity rules for every contribution and relationship.
- [x] Define unsupported prerequisites, applicability, and incomplete downstream obligations without inferring them from
  current types.
- [x] Prove the model is expressible using anonymous synthetic concepts only.
- [x] Stop before implementing discovery.

## Milestone 3.2 — Discovery and Graph Assembly

- [ ] Discover content-type and feature contributions from their owning composition boundaries.
- [ ] Build one graph without a central list of known types, capabilities, or features.
- [ ] Reject duplicate identities, contradictory ownership, unowned relationships, and unreachable contributions.
- [ ] Verify that adding a synthetic contribution changes assembly without editing an assembler registry.
- [ ] Stop before graph evaluation.

## Milestone 3.3 — Evaluation and Obligations

- [ ] Evaluate capability expressions and contextual requirements generically.
- [ ] Activate feature-owned shared consequences for every compatible type.
- [ ] Emit actionable specialized obligations with the responsible owner and affected subject.
- [ ] Keep deliberate non-applicability distinct from missing implementation.
- [ ] Reject feature-specific branches in the evaluator.
- [ ] Stop before contract and projection selection.

## Milestone 3.4 — Contract and Projection Selection

- [ ] Select feature-owned behavioral contracts from evaluated applicability.
- [ ] Select developer and documentation projections from the same evaluated relationships.
- [ ] Make missing required fixtures or projections explicit obligations rather than manual checklist items.
- [ ] Verify that tests exercise selection mechanics and behavior, not duplicated capability values.
- [ ] Stop before production boundary changes.

## Milestone 3.5 — Dependency Boundary and Migration Cut

- [ ] Establish module dependencies so contribution owners point toward the generic kernel.
- [ ] Switch the intended composition boundary even if unported production code stops compiling.
- [ ] Remove `EntryCapabilityCatalog`, `EntryCapabilityReport`, `supportsTypeWide`, legacy report assembly, and production
  report DI exposure rather than deprecating them behind a working facade.
- [ ] Remove explicit unsupported declarations whose only purpose is to compensate for an absent provider.
- [ ] Do not preserve old report APIs as a parallel authority; allow their unported consumers to fail compilation.
- [ ] Record compile failures as concrete migration obligations owned by later phases.
- [ ] Reject compatibility facades, fallback registries, and dual authorities introduced only to keep the build green.
- [ ] Run the complete synthetic unknown-type/unknown-feature proof.
- [ ] Stop before migrating Manga, Anime, or Book.

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
executable consequences, specialized adapter requirements, behavioral contracts, and projections. Missing prerequisites
mean inapplicable; specialized requirements are structurally separate so Milestone 3.3 can turn missing adapters into
obligations only after applicability is established.

Construction enforces stable identities, contribution-local uniqueness, and feature ownership of specialized
requirements. Cross-contribution validation is intentionally deferred to Milestone 3.2 graph assembly. Anonymous
alpha/beta tests prove valid empty and one-provider types, relationship composition, context retention, adapter supply,
and invalid ownership/identity cases without encoding current product names.
