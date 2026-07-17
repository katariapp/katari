# Production Dependency Boundary and Legacy Authority Cut

Status: Accepted

## Context

The generic graph kernel now defines contributions, discovery, evaluation, obligations, contracts, and projections. If
the old catalog/report API remains callable while production migration begins, every unported consumer can continue to
use the old authority and the architecture will become an optional side channel.

The boundary must also allow content-type and feature owners to contribute independently. Entry-type plugins combine
operational processor registration with their own type contribution, while feature modules must not be forced to
masquerade as entry-type plugins.

## Proposed Decision

- `entry-interactions:api` depends on and exports the lower-level `feature-graph` contract. The generic kernel has no
  dependency back toward entry interactions, concrete types, or application code.
- `EntryInteractionPlugin` implements `FeatureGraphContributor` in addition to operational registry registration. There
  is no default owner, empty contribution, or adapter preserving the old lambda-only plugin API.
- Application composition supplies independent feature contributors separately. The composition boundary combines them
  with the type plugins and gives the resulting generic collection to discovery.
- Composition assembles, evaluates, and selects artifacts before exposing `FeatureGraph`, `FeatureGraphEvaluation`, and
  `FeatureArtifactSelection` beside the operational `EntryInteractions` instance.
- Delete `EntryCapabilityCatalog`, the legacy evidence/support model, `EntryCapabilityReport`, `supportsTypeWide`, report
  assembly, and the report-driven `EntryDownloadCapabilityPolicy` implementation.
- Delete the report/model/policy tests whose purpose was validating the retired authority.
- Remove explicit unsupported outcomes from Anime and Book. Missing providers require no compensating declaration.
- Remove the production DI binding for `EntryCapabilityReport`. Do not replace it with a compatibility facade or fallback
  boolean API.
- Leave unported consumers uncompilable and record each ownership group in `migration-obligations.md`.
- Require the kernel and provider-contract API to compile and require the complete synthetic unknown-contribution proof
  to pass. SPI, concrete type, application, and full-build compilation are explicitly deferred to their migration
  phases.

## Consequences

- The graph is the only architectural path available for new capability and feature relationships.
- A content type cannot compile at the new plugin boundary until it supplies a real owned contribution.
- Independent features enter through their own contributors rather than through content-type knowledge.
- Existing report-based download, lifecycle, UI, notification, and test consumers become visible migration obligations
  instead of silently continuing behind an old API.
- Lower-boundary correctness is independently verifiable while expected production compilation failures remain.

## Deliberately Deferred

- Manga, Anime, and Book provider contributions
- Product feature contributions
- Migration of report-based runtime and UI consumers
- Contextual source, entry, selection, preference, platform, and external evidence
- Production behavioral contracts and documentation projections
- Full application compilation

## Alternatives Rejected

- Keeping the report deprecated but callable until the last consumer migrates
- Supplying empty type contributions to preserve existing plugin lambdas
- Giving feature contributors an `EntryInteractionPlugin` wrapper
- Adding a default empty feature-contributor list at production composition
- Replacing `supportsTypeWide` with another boolean facade over the graph
- Treating a green application build as the Phase 3.5 completion criterion
