# Documentation Projection Participation

Status: Accepted

## Context

Projection channels are optional architectural concepts. Making the content-type reference mandatory for every Feature
would turn a particular document into a universal product requirement. Leaving participation entirely implicit,
however, would allow a new Feature to omit its user-facing projection simply because its developer did not know that
the reference existed.

The reference also contains facts with different authorities. Most rows describe evaluated Feature relationships.
Source-dependent rows may require provider-owned metadata, while bundled-source, compatibility, and tracking rows
describe registrations owned outside the Entry interaction providers. Validation scenarios are representative samples
and cannot authorize any of those product claims.

## Decision

- Projection channels remain optional. Content-type support is still established only by provider-backed and contextual
  Feature relationships; a documentation declaration never enables behavior.
- Every discovered Feature must explicitly classify its participation in the content-type-reference channel:
  - **included** participation is inferred from one or more feature-owned projection requirements; and
  - **excluded** participation supplies a non-blank reason explaining why the Feature has no fact in this reference.
- The classification gate discovers Features from the assembled graph. It contains no list of current Feature IDs or
  content types. An unknown future Feature therefore becomes an owned documentation decision automatically.
- A Feature cannot both require and exclude the same projection channel. A promised projection without its
  implementation is incomplete even when no current content type makes the relationship applicable, because the row's
  presentation and semantics must still be owned.
- Selected documentation projections receive the same matched providers, specialized adapters, and contextual evidence
  that established applicability. They do not re-evaluate capability expressions or branch on known content types.
- External rows consume facts from their actual production owner or registration. Source descriptions do not become
  Entry behavior providers, and successful validation scenarios do not become claims of type-wide support.
- The generated section discovers content types from the graph and orders them by stable ID. A new type adds a column
  without a documentation type-list edit; unavailable cells follow from relationship inapplicability.
- Checked-in explanatory prose remains handwritten. Deterministic factual tables and derived factual notes live inside
  generated markers and are verified by the normal build workflow.

## Consequences

- Adding a Feature requires one local decision by its owner about whether this specific public reference describes it,
  without making any operation mandatory for a content type.
- Adding a provider changes every applicable row selected from that relationship and makes a stale checked-in reference
  fail verification.
- A missing row cannot hide behind an empty projection list, and an excluded Feature cannot accidentally start
  contributing the same channel without a structural conflict.
- Provider metadata may explain a conditional status such as source-dependent support, but it cannot override whether
  the underlying Feature relationship exists.

## Alternatives Rejected

- Making documentation projections globally mandatory for all projection channels
- Maintaining a central list of Features expected to appear in the reference
- Treating provider absence as an explicit unsupported declaration on every content type
- Deriving public support from validation fixtures or successful contextual test scenarios
- Keeping external registration rows as manually maintained exceptions
- Inspecting implementation class names or branching on Manga, Anime, and Book while rendering

## Affected Phases

- Phase 7.4 implements the classification gate, feature-owned projections, authoritative external inputs, and checked-in
  reference verification.
- Phase 7.5 makes projection completeness and document verification part of normal repository validation.
- Phase 8 audits for remaining handwritten capability facts and documentation-side support logic.
