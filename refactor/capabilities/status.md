# Capability Refactor Status

Updated: 2026-07-17

## Repository Snapshot

- Branch: `features-arch-refactor`
- Latest committed implementation milestone: `e04b2481c` (`(refactor): derive download capabilities from providers`)
- Phase 2 completion: `918fcc4d3` (`(refactor): complete bookmark download capability proof`)
- Always verify `HEAD`, the working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: architecture reset before Phase 3 — General Relationship Architecture
- Milestone: reset manifesto, phase order, and durable workspace
- State: complete and uncommitted; stopped before Phase 3 Milestone 3.1 implementation

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

- Documentation resets the manifesto, plan, phase files, atlas, decisions, and resume instructions.
- The rejected completion-contract implementation and its tests are absent.
- The later review also rejected structural mandatory operations: a future AUDIO type with only Open is a complete and
  valid contribution, while all unsupported interactions remain unavailable.
- Runtime production code is unchanged relative to `HEAD` by this reset.
- Type plugin and production-runtime tests retain operational/identity assertions but no longer assert capability labels
  that merely mirror provider registration.
- The Bulk Downloads + Bookmarking wording correction remains because bulk selection depends on the bulk capability,
  while cleanup protection depends on Bookmarking within download lifecycle policy.
- `legacy-artifacts.md` now records which committed changes are retained behavior, retired authority, rehomed feature
  logic, or reusable only after re-justification. Git history will not be rewritten.

## Validation for This Reset

- `./gradlew --quiet spotlessApply` and `./gradlew --quiet spotlessCheck` completed successfully.
- Focused debug unit-test tasks for the shared Entry interactions and Manga, Anime, and Book interaction modules passed
  after removing duplicated capability assertions.
- `git diff --check` passed.
- Every phase link in the rewritten plan resolves to an existing phase file.
- The rejected completion-contract source and test files are absent, and searches found no obsolete phase links or claim
  that per-type capability assertions are the completeness mechanism.
- Focused compilation may be used to catch accidental test-edit damage, but a green application build is not an
  architectural acceptance criterion.
- No production implementation of Phase 3 begins in this milestone.

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
- Compilation pressure cannot justify dual authorities or fallback architecture.
- The full architecture is app-wide and not limited to Bookmarking or Downloads.

## Exact Next Action After Review

After explicit commit authorization, commit this architecture reset. Then begin only Phase 3 Milestone 3.1: define the
generic contribution semantics and ownership boundaries using anonymous synthetic concepts. Stop before implementing
discovery in Milestone 3.2. Application compilation is not required for Milestone 3.1 unless its chosen module boundary
is itself expected to compile at that point.
