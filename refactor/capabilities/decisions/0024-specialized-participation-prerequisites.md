# Specialized Participation Prerequisites

Status: Accepted

## Context

Specialized adapters originally had one meaning: once a feature's capability and context prerequisites established
applicability, a missing adapter became actionable type-owned work. That remains correct when the shared relationship
already applies and only exceptional media integration is missing.

It is not correct when supplying the adapter is itself the authoritative declaration that a media-specific host
participates. Child WebView controls live inside a reader or player. Treating source support alone as sufficient made
every content type enter the relationship and turned absent Anime and Book reader/player integrations into obligations.
Adding empty adapters would have claimed behavior those hosts do not implement; filtering validation scenarios by the
current type matrix would have created a second support authority.

## Decision

- A feature integration may declare specialized prerequisites separately from specialized requirements.
- A missing specialized prerequisite makes the relationship inapplicable and creates no obligation. The adapter's
  presence is the owned participation fact, and the selected adapter remains available to contracts and projections.
- A missing specialized requirement retains the existing semantics: after capability and specialized prerequisites plus
  context establish applicability, the missing adapter becomes an obligation owned by the affected content type.
- Features use a specialized prerequisite only when the type-owned integration is genuine media-specific participation,
  not to require another opt-in for behavior derivable from existing providers.
- Child WebView host participation is a specialized prerequisite. Manga contributes its real reader host; Anime and Book
  do not claim that relationship. Source support remains contextual evidence only for participating hosts.

## Consequences

- A partial or new content type remains valid without reader/player integrations it has not implemented.
- Contributing a real specialized host automatically enrolls that type in the feature's contextual consequences and
  behavioral contracts without editing a type list or validation scenario.
- Genuine forgotten follow-on work remains visible through specialized requirements; optional participation is not
  weakened into a silent requirement failure.
- Validation can apply one contextual scenario to every statically compatible subject without synthetic adapters or
  per-type filtering.

## Alternatives Rejected

- Adding marker capability providers solely to gate specialized UI participation
- Supplying empty Anime and Book adapters that do not correspond to reader/player behavior
- Filtering contract scenarios through a handwritten content-type matrix
- Treating every source capability as a mandate to add new media-host product UI
