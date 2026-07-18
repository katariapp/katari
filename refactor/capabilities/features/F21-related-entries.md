# F21 — Related Entries

Status: complete

## Owner and Context

- Feature owner: `entry-related-entries`
- Type prerequisite: none; every composed content type receives the shared orchestration
- Contextual authority: the concrete source implements the public `RelatedEntriesSource` contract
- Contextual presentation input: the concrete source's `EntryItemOrientationProvider`, with its source-owned default
- Shared consequences: contextual availability, fetch, identity normalization, profile-aware persistence, live Library
  membership, orientation, the Entry action/dialog, and ordinary Entry-details navigation
- Behavioral contract: an anonymous provider-less content type combined with absent, unsupported, supporting, and
  failing source contexts

The graph integration uses an always-applicable prerequisite because no type-owned behavior is required. This means
every composed origin type receives the same orchestration; it does not claim that every source supports Related
Entries. Source absence and source capability absence remain structured contextual results.

## Feature Boundary

Application code receives `EntryRelatedEntriesFeature`. The coordinator owns source resolution, contextual
availability, source-order preservation, profile-aware identity deduplication, conversion and persistence of network
results, source orientation, and observation of persisted Entry state.

`RelatedEntriesSource` remains a public source contract and the only support signal. It is not copied into a content-type
provider, report, presentation flag, or support method. The legacy Manga capability booleans remain confined to the
source-compatibility adapter, which exposes the modern source contract only for a genuine direct legacy
implementation.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry action | Visibility consumes structured Feature availability for the concrete Entry and source. No source-interface cast remains in the app. |
| Screen model | Load, retry, refresh, and stale-request mechanics consume Feature results. Source/network failures remain errors that can be retried. |
| Dialog | Orientation comes from the loaded Feature result. Persisted related Entries are observed through the Feature so favorite/Library state remains live. |
| Returned entries | Source order and each returned `SEntry.type` remain authoritative. Identity includes profile, source, URL, and type; a shared URL with different types remains distinct. |
| Persistence | The existing network-to-local path keeps current profile ownership and preserves existing favorite rows. No Related-Entries-specific storage authority is added. |
| Entry opening | Result clicks perform ordinary Entry-details navigation. This is not F01 Open. The returned Entry's own screen later derives child Open controls from its actual F01 provider. |
| Source metadata | `supportedEntryTypes` remains descriptive and never filters or validates returned results. |
| Vocabulary | Toolbar and empty-state wording remains F23 presentation and never authorizes the feature. |

The former app/domain `GetRelatedEntries` boundary is removed. Its conversion, deduplication, persistence, and external
source composition now have one Feature owner rather than remaining callable beside the Feature API.

## External and Compatibility Boundaries

The Entry SDK `RelatedEntriesSource` and `EntryItemOrientationProvider` contracts, SDK documentation, and their focused
source tests retain source ownership. The legacy source adapter retains compatibility behavior without becoming a
second application support authority. Generic repository and Entry-observation primitives remain storage inputs to the
Feature.

No F21-plus-Open relationship is declared, no same-type rule is introduced, and no source metadata is used as returned
type authorization.

## Automatic-Participation Proof

The focused contract contributes an anonymous type with no interaction providers and verifies selection of every F21
consequence. A supporting source activates the shared workflow without a type edit, while source absence and source
capability absence produce distinct structured results. Mixed Manga/Anime results from a Book origin retain order and
type, duplicate identity is persisted once, current Library state is returned and observed, source orientation is
carried to the dialog, and a genuine source failure remains retryable.

## Manifesto Review

- The external source capability is declared once and remains authoritative.
- Every composed content type receives the common Feature consequences without an opt-in or provider.
- Source missing and source unsupported are contextual absence, not invalid type declarations or exceptions.
- Returned types are authoritative and are not coerced to the origin type or checked against descriptive metadata.
- Application production code uses only the Feature boundary; compatibility contracts remain with their external
  owners.
- No type matrix, central support list, no-op provider, silent fallback, F01 coupling, or declaration test was added.

## Validation

- API and root Entry-interactions production/test compilation pass.
- The focused graph-selected F21 Feature contract passes.
- Full FOSS application compilation reaches only the already recorded F11/F12 and concurrent F20 migration failures;
  no F21 source-capability or consumer error remains.
