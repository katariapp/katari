# F06 — Download Lifecycle Cleanup

Status: complete

## Owner and Relationships

- Feature owner: `entry-download-lifecycle`
- Base prerequisite: `EntryDownloadCapability`
- Derived Bookmark prerequisite: `EntryDownloadCapability` and `EntryBookmarkCapability`
- Context-free consequences: type applicability, structured event acceptance, Download provider dispatch, and discovery
  of the Download-plus-Bookmark relationship
- Contextual consequences: marked-consumed removal, completion/slot cleanup, progress-driven download-ahead, category
  eligibility, physical cleanup authorization, and Bookmark protection unless its preference override is enabled
- Operation data: actual visible and owner Entries, changed children, reading order, merged ownership, deduplication mode,
  download continuity, and concrete candidate sets
- Specialized requirement: none. Every lifecycle operation uses shared Entry/child models and the existing media-specific
  Download provider operations.
- Presentation projection: none. F07 owns preference presentation; F06 consumes the selected values.

Download-provider absence makes lifecycle handling inapplicable for that type. It does not invalidate the content type,
require a no-op lifecycle provider, or cause an event producer to disappear. The globally installed event sink remains
present and returns `Inapplicable` for unsupported types.

## Contract and Dispatch Boundary

Event producers depend only on `EntryDownloadLifecycleEventSink`, a feature-owned API contract accepting structured
`MarkedConsumed`, `Progressed`, and `Completed` events. Production delivery is non-optional: Manga, Anime, and Book
processors/viewers receive one globally installed sink and do not use nullable injection or `runCatching` to discard an
event when composition is missing.

The sink forwards lazily to `EntryDownloadLifecycleFeature` because type plugins are assembled before graph evaluation
exists. That forwarding is composition wiring, not another support authority: applicability and all policy remain in the
single graph-backed feature coordinator. The coordinator alone can access raw Download dispatch for persisted-state
inspection, queueing, starting, physical deletion, and deferred cleanup.

The context-free provider relationship authorizes event acceptance but not every possible policy consequence. Separate
contextual relationships consume active-profile cleanup/download-ahead preferences, viewer progress eligibility,
per-owner category policy, and the Bookmark-protection override. The feature resolves each relationship for the actual
Entry type that owns the consequence.

## Consumer Disposition

| Producer or consumer | Disposition |
| --- | --- |
| F09 Consumption feature | Emits `MarkedConsumed` once for the children returned by a successful consume mutation. |
| Manga reader | Emits progress and completion with number-deduplication context. |
| Anime player | Emits progress or completion for the current owner Entry and episode. |
| Book reader session | Emits progress and completion as persisted Book location changes. |
| Runtime composition | Installs the F06 contributor, graph-backed feature, and non-optional lazy event sink. |
| Lifecycle coordinator | Resolves merged-child owners, evaluates actual owner applicability, serializes events, applies shared policy, and dispatches media-specific Download operations. |

Consumption itself remains F09-owned. Type processors return the children whose persisted state changed, and the F09
coordinator emits the structured lifecycle event. F06 does not make type-specific consumption wiring a requirement.

## Shared Policy

- Marked-consumed cleanup runs only when the existing remove-after-marked-consumed preference is enabled.
- Completion cleanup selects the configured prior reading-order slot and uses deferred `cleanup`, allowing an open
  viewer to release the current file before physical removal.
- Marked-consumed cleanup uses immediate `delete` because no viewer-close deferral is required by that event.
- Excluded categories are evaluated per resolved owner Entry, including the default category.
- Bookmark protection is selected from the Download-plus-Bookmarking relationship. A Download-only future type needs no
  Bookmark opt-out and receives ordinary cleanup; adding Bookmarking automatically enables protection.
- Download-ahead verifies the current and next items are already downloaded, chooses unread missing items in reading
  order, queues per actual owner without auto-start, then starts once. Repeated progress for the same owner/child is
  deduplicated until completion.

Owner lookup, reading-order membership, already-downloaded continuity, deduplication, and empty candidate sets remain
operation results. They are known only while handling the concrete event and do not become reusable capabilities or
type-wide blockers.

## Automatic-Participation Proof

The focused feature test composes anonymous partial content-type contributions rather than asserting production type
labels:

- no Download provider is valid and returns `Inapplicable`;
- Download alone activates all base lifecycle consequences and cleans a bookmarked child normally;
- adding Bookmarking automatically protects the same child without a downloader, feature, or producer edit;
- the preference override, category exclusions, completion cleanup dispatch, and download-ahead behavior remain
  contextual shared contracts.

These are behavioral graph-selected proofs. They do not restate whether Manga, Anime, or Book currently registers a
provider.

## Manifesto Review

- Download and Bookmarking remain optional; no type matrix, mandatory operation, lifecycle opt-in, or no-op provider was
  introduced.
- Bookmark protection is derived from existing providers instead of being separately declared by each type.
- All event sources use one feature-owned boundary and cannot invoke raw Download providers or graph evaluation.
- Preferences, categories, viewer state, merge ownership, and event payloads remain contextual rather than becoming
  type-wide support flags.
- Stable policy inputs are declared graph context; transient owner-resolution and candidate outcomes remain operation
  results instead of being flattened into a synthetic runtime capability.
- A future Download provider receives cleanup, category policy, completion handling, and download-ahead without changes
  to consumers or F06; adding Bookmarking also activates protection automatically.
- The old deleted capability report and report-driven Download policy are removed rather than restored through a shim.
- F03, F04, F05, F07, F08, and F09 ownership is preserved.
