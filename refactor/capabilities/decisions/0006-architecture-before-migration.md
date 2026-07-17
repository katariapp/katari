# Architecture Before Production Migration

Status: Accepted

## Context

The first implementation sequence built an evidence report, completed a Bookmarking/Downloads vertical slice, and began
migrating capabilities one group at a time. The general feature relationship graph, obligation model, and automatic
contract/projection selection were deferred.

That sequence enabled a hardcoded production completion contract listing the capabilities considered so far, plus tests
that repeated current provider registrations as support assertions. Although intended to catch forgotten work, both
mechanisms required the next contributor to remember to extend another list. They reproduced the problem this refactor
exists to remove.

## Decision

Build the general discoverable relationship architecture before migrating another production capability or consumer.

- Content types and features contribute through owned, discoverable boundaries.
- A content type is valid with any subset of interaction providers.
- Fundamental support is proven by provider presence; provider absence means unsupported and needs no separate
  declaration.
- Features own prerequisite expressions, shared consequences, specialized obligations, contracts, and projections.
- Generic assembly and evaluation contain no curated product-type or capability lists.
- Tests verify discovery, evaluation, failure semantics, shared behavior, and genuine media behavior; they do not repeat
  capability declarations.
- Intermediate compilation failure is acceptable when the intended boundary exposes unported code. Compilation is
  restored by migrating that code, not by keeping parallel authorities.

## Consequences

- The former capability-by-capability Phase 3 plan is superseded.
- Existing reports and feature-specific policy are prototypes and migration inputs, not protected architecture.
- The central catalog/report authority is retired at the Phase 3 dependency cut, before production type and feature
  migration, even when doing so breaks unported consumers.
- Production type composition, feature consumers, contextual integration, contracts, reporting, and documentation move
  only after the generic kernel passes synthetic unknown-contribution proofs.
- Compile failures created by architectural boundary changes are recorded as migration obligations.
- The rejected hardcoded completion contract is not retained.

## Rejected Alternatives

- Expanding the completion contract whenever a capability is migrated.
- Keeping a manually maintained required/optional matrix for production types.
- Treating Open, Continue, or another currently ubiquitous operation as mandatory for future type validity.
- Using per-type capability assertions as completeness enforcement.
- Preserving the old and new authorities in parallel until the whole application compiles.
- Deferring retirement of the old catalog/report authority to final cleanup after production migration.
- Deferring discovery, obligations, and contract selection until after feature migrations.
