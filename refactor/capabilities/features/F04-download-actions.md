# F04 — Individual and Bulk Download Actions

Status: complete

## Owner and Relationships

- Feature owner: `entry-download-actions`
- Individual prerequisite: `EntryDownloadCapability`
- Ordinary bulk prerequisite: `EntryDownloadCapability` and `EntryBulkDownloadCandidateCapability`
- Bookmarked bulk prerequisite: `EntryDownloadCapability`, `EntryBulkDownloadCandidateCapability`, and
  `EntryBookmarkCapability`
- Shared consequences: manual child download/delete/cancel/retry dispatch, media-specific candidate-pool resolution, and
  shared next/unread/bookmarked candidate selection
- Context: the actual source access, entry, child selection, merge members, notification selection size, and current
  download state remain operation inputs rather than type-wide support facts
- Specialized requirement: none; each contributed bulk-candidate provider already owns media-specific pool construction
- Presentation projection: none; dropdown labels and type vocabulary remain presentation-owned and cannot authorize an
  action
- Behavioral contracts: synthetic partial-provider tests prove the three prerequisite expressions, shared candidate
  selection, contextual blockers, notification selection policy, and automatic bookmarked behavior

Provider absence makes only the affected action integration inapplicable. Core Downloads does not imply bulk candidate
construction, and ordinary bulk support does not imply bookmarked bulk support.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry child selection and swipes | Individual availability and manual download/delete/cancel/retry dispatch use the feature coordinator. Local and stub sources are explicit blockers. |
| Entry bulk dropdown | Ordinary and bookmarked availability come from their separate evaluated relationships. The feature resolves candidate pools and applies shared action selection. |
| Entry download options branch | Preserved as an explicit F07 branch. F04 dispatches only when the existing option resolver does not select the options dialog; selected option dispatch remains raw until F07. |
| Library selection | The complete selection is evaluated together. Empty, local, stub, and provider-incompatible selections do not expose the action. Media-specific pools are resolved per actual member Entry. |
| Updates rows and selection | Row availability and whole-selection eligibility use the feature. Mixed or contextually blocked selections are rejected rather than silently treated as universal support. |
| Library-update notification | Core Download applicability, local/stub state, empty children, and the 15-child action limit are evaluated by the feature before the action is added. The receiver dispatches through the same feature. |
| Download dropdown component | Receives the feature-selected bookmarked consequence as presentation input; it does not query providers or capability reports. |
| Provider SPI | Exposes only the media-specific bulk candidate pool. Bookmark prerequisites and next/unread/bookmarked selection no longer live in raw dispatch. |

No F04-owned application operation calls `EntryDownloadInteraction` directly. The combined F03–F05 migration also
removes now-unused raw imports from Library, Updates, and Notification Receiver. Entry retains the facade only for the
explicitly deferred F07 options and F08 maintenance paths.

## Contextual Blockers

`LOCAL_OR_STUB`, `EMPTY_SELECTION`, and `NOTIFICATION_SELECTION_TOO_LARGE` are structured request blockers. They do not
become negative content-type declarations and do not alter graph applicability. Adding a remote source or changing the
selection can therefore make the same provider-backed action available without changing its content-type contribution.

## Automatic-Participation Proof

The focused feature test builds one anonymous partial content-type contribution at a time:

- a core Download provider activates individual actions but leaves bulk actions inapplicable;
- adding a Bulk Candidate provider activates ordinary shared bulk selection;
- adding Bookmarking automatically activates bookmarked selection without changing the downloader, feature, or UI;
- removing providers remains valid and yields inapplicable behavior; and
- local/stub and selection state block applicable providers without being reported as missing capabilities.

## Manifesto Review

- No Manga/Anime/Book support matrix, mandatory download rule, no-op provider, or capability-label assertion was added.
- The three graph integrations, not application booleans, are the only source of static action applicability.
- Shared action consequences and cross-capability Bookmark behavior are feature-owned.
- Candidate construction remains genuinely media-specific while candidate selection is shared.
- A future type activates every generic F04 surface by contributing the matching providers; contextual blockers remain
  explicit operation state.
- Application code cannot use raw candidate dispatch as an alternative feature API.

## Remaining Boundaries

- F03 still owns queue flows, status/count observation, start/pause/clear/reorder, and queue-screen cancellation.
- F05 owns shared automatic candidate policy and update/refresh queue orchestration without a per-type filter opt-in.
- F06 still owns consumption/completion cleanup policy and physical cleanup dispatch.
- F07 still owns options discovery, the options dialog, selected option dispatch, and settings.
- F08 still owns rename, cache invalidation, migration/catalogue removal, and other maintenance deletion paths.
- Graph-selected production contract execution and documentation projection remain Phase 7 work.
