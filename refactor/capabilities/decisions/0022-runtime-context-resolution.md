# Runtime Context Resolution

Status: Accepted

## Context

The feature graph discovers typed context-input definitions but intentionally leaves them unresolved. Production
Features currently resolve source, entry, selection, preference, tracker, and platform facts inside their coordinators,
so those dependencies are not visible to graph evaluation, contract selection, reporting, or obligation discovery.

Migrating each external interface cast into another coordinator without completing contextual evaluation would improve
local ownership while leaving the application-wide relationship model dependent on developer memory.

## Proposed Decision

- Context definitions remain typed, stable, discoverable declarations associated with their authoritative owner.
- Feature integrations reference every context input used to decide applicability, blockers, consequences, contracts,
  projections, or obligations and own the rule that interprets its evidence. Ordinary execution payload remains in a
  purpose-specific Feature request rather than becoming graph context.
- Static evaluation treats satisfied type prerequisites plus unresolved context as a discovered candidate. It does not
  flatten the relationship into applicable or unsupported.
- Runtime resolution evaluates one immutable operation subject and its typed evidence. It distinguishes missing
  evidence, contextual blockage, applicability, and specialized work that becomes incomplete only after context makes
  the relationship applicable.
- Resolution retains the owner and reason for enabling and blocking evidence. A Feature exposes purpose-specific
  structured results; application consumers cannot query generic capability/context state.
- Shared consequences are installed once and are candidates for conditional integrations. Runtime context never causes
  per-type coordinator installation or turns the graph into an event bus.
- Reactive Features explicitly reevaluate when authoritative context changes. A cached contextual result is not a new
  support authority.
- Conditional contract and projection selection consumes resolved applicability in Phase 7; it does not use a curated
  set of production contexts.
- Generic graph code knows only contribution identities, types, evidence completeness, and result states. It contains no
  source, tracker, Entry, selection, preference, platform, feature, or content-type rule.

## Ownership Consequences

- Source and tracker contracts remain authoritative external facts.
- Entry, selection, preference, profile, and platform values remain operation-scoped evidence rather than capabilities.
- Features own consequences, contextual interpretation, blockers, and any specialized requirements.
- Type-owned media providers may use external media contracts internally behind a Feature.
- Compatibility adapters may expose current external contracts but cannot declare application support separately.

## Alternatives Rejected

- One global context object or service locator: hides ownership, encourages undeclared reads, and cannot express
  operation-specific identity safely.
- Treating every context as a type capability: creates false unconditional support and copies external truth.
- Leaving context entirely inside coordinators: preserves runtime behavior but prevents discovery, reporting, contracts,
  and delayed obligations from seeing the relationship.
- Selecting conditional consequences as unconditionally applicable: allows UI and policies to bypass blockers.
- Waiting until Phase 7 to model context: contracts and documentation cannot repair an undiscoverable production
  relationship.

## Acceptance Proof

An anonymous content type, unknown context definition, and unknown Feature must pass through unchanged discovery and
resolution. The same path must report missing evidence, retain enabling and blocking evidence, resolve applicability,
and expose a specialized obligation only after context makes it relevant. No product identifier or completion list may
be edited for the proof.

## Affected Work

- Phase 6 runtime context resolution and all `C01`–`C24` consumer migrations
- Phase 7 conditional contract, reporting, and projection selection
- Every Feature whose applicability, blockers, consequences, or obligations depend on live external evidence

## Manifesto Review

The decision is acceptable only if declaring a contextual relationship once makes its product consequences,
explanations, obligations, contracts, reporting, and projections discoverable without another curated consumer list.
