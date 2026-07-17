# Capability Refactor Status

Updated: 2026-07-17

## Repository Snapshot

- Branch: `features-arch-refactor`
- Architecture reset commit: `666487574` (`(refactor): reset capability architecture direction`)
- Contribution semantics commit: `d89e51693` (`(feat): define generic feature contributions`)
- Discovery and assembly commit: `03d0b6422` (`(feat): assemble discovered feature graph`)
- Evaluation and obligations commit: `0a578b784` (`(feat): evaluate feature relationships`)
- Latest earlier production migration: `e04b2481c` (`(refactor): derive download capabilities from providers`)
- Phase 2 completion: `918fcc4d3` (`(refactor): complete bookmark download capability proof`)
- Always verify `HEAD`, the working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 3 — General Relationship Architecture
- Milestone: 3.4 — Contract and projection selection
- State: complete and uncommitted; stopped before Milestone 3.5 dependency boundary and migration cut

## Why the Plan Was Reset

The prior sequence built an evidence/reporting foundation, completed a Bookmarking/Downloads vertical slice, and then
began migrating production capabilities one group at a time. General feature discovery, relationship evaluation,
specialized obligations, contract selection, and projections were deferred.

That sequence produced an uncommitted hardcoded completion contract for Open, Continue, Bookmarking, Downloads, and Bulk
Downloads. It also repeated provider registrations as per-type capability assertions. Both mechanisms required a future
contributor to remember another list and therefore contradicted the manifesto. The contract and duplicate assertions were
removed rather than expanded.

The corrected rule is architecture first, production conformance second. Intermediate compile failures are acceptable
when new boundaries expose unported code. Compilation must be restored by migrating that code, not by adding a parallel
authority or weakening the architecture.

## Durable Decisions

- Content types and features contribute through owned, discoverable boundaries.
- A content type is valid with any subset of interaction providers; no interaction, including Open or Continue, is
  mandatory for architectural validity.
- Provider presence proves support and provider absence means unsupported without a separate absence declaration.
- Features own prerequisites, contextual inputs, shared consequences, specialized obligations, behavioral contracts,
  and projections.
- Generic assembly and evaluation contain no concrete type, capability, or feature allowlist.
- Tests verify architecture mechanics, shared behavior, and genuine media behavior; they do not restate declarations.
- The existing report and Bookmarking/Downloads slice are prototypes and migration evidence, not protected architecture.

## Completed Historical Work

- [x] Phase 0 evidence inventory, consumer graph, discrepancy review, and product decisions
- [x] Phase 1 capability vocabulary, evidence composition, support outcomes, and deterministic report prototype
- [x] Phase 2 Bookmarking provider split and Bookmarking/Downloads learning slice
- [x] Initial download provider-authority migration committed in `e04b2481c`
- [x] Incorrect uncommitted completion contract removed
- [x] Per-type capability-label assertions introduced or retained for completion enforcement removed
- [x] Manifesto expanded with architecture-before-migration, valid partial type support, provider-backed applicability,
  testing, and compilation principles
- [x] Phase plan reordered around a general architecture kernel before production migration
- [x] Accepted decision `0006-architecture-before-migration.md` recorded
- [x] Architecture reset committed in `666487574`
- [x] Milestone 3.1 standalone generic contribution kernel implemented
- [x] Decision `0007-contribution-semantics.md` accepted
- [x] Milestone 3.1 committed in `d89e51693`
- [x] Milestone 3.2 generic discovery and deterministic graph assembly implemented
- [x] Decision `0008-contribution-discovery-and-assembly.md` accepted
- [x] Milestone 3.2 committed in `03d0b6422`
- [x] Milestone 3.3 generic evaluation and specialized obligations implemented
- [x] Decision `0009-evaluation-and-specialized-obligations.md` accepted
- [x] Milestone 3.3 committed in `0a578b784`
- [x] Milestone 3.4 generic contract, fixture, and projection selection implemented
- [x] Decision `0010-contract-fixture-and-projection-selection.md` accepted

## New Phase Sequence

- Phase 3: General relationship architecture
- Phase 4: Entry-type composition migration
- Phase 5: Feature integration migration
- Phase 6: Contextual and external integration
- Phase 7: Graph-selected contracts, reporting, and documentation
- Phase 8: Legacy removal, boundary enforcement, and build completion

Phase 3 milestones:

- 3.1: Contribution semantics and ownership
- 3.2: Discovery and graph assembly
- 3.3: Evaluation and obligations
- 3.4: Contract and projection selection
- 3.5: Dependency boundary and migration cut

No Manga, Anime, Book, Bookmarking, Downloads, or other product-specific branch may enter the generic Phase 3 kernel.
The phase uses anonymous synthetic contributions to prove unknown future participation.

At Milestone 3.5, `EntryCapabilityCatalog`, `EntryCapabilityReport`, `supportsTypeWide`, legacy report assembly, explicit
absence compensation, and production report DI exposure are removed rather than deprecated behind a working facade.
Unported consumers may fail to compile and become migration obligations. The old report must not survive beside the
graph.

## Current Working Tree Scope

- Generic contract and projection selection consumes evaluated applicability without rechecking provider support.
- Selection rejects incomplete, duplicate, unexpected, or stale evaluation coverage rather than accepting a curated
  contract/documentation subset.
- Every applicable type selects the same feature-owned executable contract and projection objects.
- Contracts declare typed media fixture requirements only when direct providers/adapters are insufficient.
- Missing contract fixtures become obligations attributed to the affected content-type owner.
- Missing shared projections become one feature-owned obligation listing every affected applicable subject.
- Contracts, fixtures, developer projections, and documentation projections are optional unless declared by the owning
  feature; there is no universal artifact list.
- Accepted decision `0010-contract-fixture-and-projection-selection.md` and Phase 3.4 completion notes.
- No existing production source or consumer changed in this milestone.

## Last Validation

- `./gradlew --quiet spotlessApply :feature-graph:testDebugUnitTest` passed during implementation.
- Selection tests cover automatic artifact enrollment, actual execution of selected opaque objects, exclusion of
  unsupported/incomplete relationships, fixture ownership, aggregated projection obligations, deterministic ordering,
  and rejection of curated evaluation coverage.
- Search confirms the new selector and tests contain no concrete product type, feature, `EntryType`, or legacy
  capability-report references.
- `./gradlew --quiet checkEntryInteractionBoundaries spotlessCheck` passed.
- `git diff --check` passed.

## Manifesto Comparison

- The hardcoded capability list was removed, not repaired.
- The revised plan builds generic discovery, relationships, obligations, contracts, and projections before more consumer
  migration.
- The old catalog/report authority is retired at the Phase 3.5 boundary cut, not deferred until final cleanup.
- All interactions are provider-backed; current ubiquity does not make an operation mandatory for future types.
- A missing provider creates no obligation. Obligations begin only after a feature's declared prerequisites are
  satisfied.
- Tests no longer repeat current provider facts as completeness declarations.
- The decisive Phase 3 proof uses unknown synthetic contributions and rejects concrete product branches.
- Phase 3.1 uses actual provider and executable artifact objects rather than building another descriptive support report.
- The new kernel does not depend on or preserve the old catalog/report API.
- Contributor installation is an environment concern; the graph kernel contains no concrete contributor registry.
- Every supplied provider must connect to a feature-owned relationship, preventing capabilities from silently bypassing
  the consequence graph.
- Assembly records relationships without prematurely evaluating support or obligations.
- Evaluation applies every discovered relationship without a product matrix or feature-specific branch.
- Missing prerequisites remain ordinary unsupported behavior; only missing specialized work after applicability creates
  an obligation.
- Context is retained as conditional instead of being guessed or flattened into type-wide support.
- Per-type applicability edges reference one feature-owned shared consequence, preserving single-gate coordinators.
- Contracts and projections are selected from the same applicable relationships without another support matrix.
- Fixture obligations are type-owned only when a contract declares genuine media-specific validation input.
- Missing shared projections are feature-owned and aggregated across affected subjects instead of duplicated per type.
- No contract, fixture, or projection kind is globally mandatory.
- Compilation pressure cannot justify dual authorities or fallback architecture.
- The full architecture is app-wide and not limited to Bookmarking or Downloads.

## Exact Next Action After Review

Review Milestone 3.4 selection semantics and proposed decision `0010`. After explicit approval, mark the decision
accepted and commit Milestone 3.4. Then begin only Milestone 3.5: establish the production dependency boundary, remove
the legacy catalog/report authority, and record rather than conceal unported compile failures.
