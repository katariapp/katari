# Download Provider Decomposition

Status: Accepted

## Context

The former `EntryDownloadProcessor` combined core queue and storage behavior with interactive option resolution,
specialized setting behavior, bulk candidate-pool construction, and an automatic-download filter method. Defaults for
options and settings made a registered downloader look complete even when it did not implement those behaviors.
Required bulk and automatic-filter methods had the opposite problem: core download support could not be contributed
without also implementing integrations owned by other download workflows.

Treating the old processor as one graph provider would preserve this false coupling. It would also force future content
types to implement or silently inherit behavior they do not yet support.

## Proposed Decision

- Core downloading, interactive download options, each specialized download setting, and bulk candidate construction
  are independent provider contracts.
- A type contributes any subset of those providers. None is mandatory, and core download support does not imply any of
  the others.
- One media-specific implementation object may implement and bind several contracts when it genuinely supplies them.
  The bindings remain independent graph facts.
- Type-level option availability follows the presence of an options provider. Contextual resolution may still produce
  no options for a particular entry or child.
- Each specialized setting is its own capability binding. A downloader does not return a set that becomes another
  capability authority.
- Bulk candidate providers own only media-specific pool construction. Shared action selection and intersections such as
  Bookmarking plus bulk downloads belong to feature integrations in Phase 5.
- Automatic-download policy and orchestration belong to the automatic-download feature integration in Phase 5.
- The operational registry maps are transitional installation targets for provider bindings, not sources from which a
  support report is reconstructed.

## Milestone 4.2.2 Application

- Manga initially bound core downloads, four setting capabilities, bulk candidate construction, and automatic filtering.
- Anime initially bound core downloads, interactive options, bulk candidate construction, and automatic filtering.
- Book initially bound core downloads, bulk candidate construction, and automatic filtering only when its optional
  downloader was constructed. It contributed no false options or settings provider.

The F05 census later established that the three automatic-filter providers contained no media-specific logic. Each only
asserted its current type and delegated to the same shared policy. F05 therefore removes that artificial provider
contract, makes the policy an internal feature-owned implementation, and derives Automatic Downloads from core Download
support. This correction prevents a generic feature from requiring a repeated type opt-in or exposing policy as an
alternate application/type-module API.
- The three type modules no longer register a monolithic download processor separately from their contributions.

## Consequences

- A future type can contribute core downloads without implementing settings, bulk actions, or interactive options and
  automatically receives the generic Automatic Downloads integration.
- Adding a genuinely specialized behavior later is one provider binding and automatically enters graph discovery.
- Current media-specific download logic remains unchanged and can still share one implementation object internally.
- Feature consumers still need Phase 5 migration before shared consequences, UI, settings surfaces, workers, and
  documentation are selected from evaluated relationships.

## Alternatives Rejected

- One download capability whose provider includes default-false, default-null, or default-empty sub-capabilities
- A central type-to-download-feature matrix
- A downloader-owned set as the authoritative statement of specialized setting support
- Requiring every core downloader to implement bulk behavior
- Requiring every downloadable type to repeat an automatic-download opt-in for shared policy
- Turning each derived intersection, such as bookmarked bulk downloads, into another type-owned opt-in
