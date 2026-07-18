# F14 — Library Filtering

Status: complete

## Owner and Relationships

- Feature owner: `entry-library-filtering`
- Shared prerequisite: unconditional generic filter policy for every composed content type
- Derived relationship: `EntryBookmarkCapability` contributes bookmarked-filter control availability
- Optional prerequisite: `EntryOutsideReleasePeriodFilterCapability` contributes outside-release-period applicability
- Shared consequences: filter matching, active-filter state, and capability-dependent control availability
- Context: current Library item types, aggregate progress/download/bookmark state, Entry status and fetch interval,
  tracker records, logged-in tracker preferences, local state, and current filter preferences
- Specialized requirement: none; filtering interprets shared Library state and the outside-release-period provider is a
  compatibility marker rather than media-specific filter code
- Behavioral contract: synthetic provider-less and partially capable content types prove generic filtering, optional
  control availability, mixed-type semantics, and valid provider absence

Library filtering itself is shared product policy and therefore applies to every composed content type. Bookmark and
outside-release-period controls are narrower relationships selected from provider evidence. Their absence removes only
the corresponding control/applicability; it does not invalidate the content type or require a no-op provider.

## Feature Boundary

Application consumers receive `EntryLibraryFilterFeature`. The application supplies neutral DTOs containing current
preference values and already-owned Library item state. The feature alone interprets downloaded-only precedence,
tri-state predicates, tracker inclusion/exclusion, outside-release-period applicability, and active-filter state.

The result contains matching target indices and structured control availability for the content types actually present
in the current Library. It does not expose provider collections or a static production support matrix. The raw
`EntryLibraryFilterInteraction` facade and its provider-backed boolean dispatch are removed.

Bookmark control availability is derived from F10's fundamental provider, but F14 filters the aggregate bookmark state
already supplied by the Library progress owner. In a mixed Library, unsupported types retain the existing false-state
semantics: a positive bookmark filter excludes them and a negative bookmark filter includes them.

Outside-release-period filtering preserves the previous partial behavior. Supported targets evaluate their fetch-
interval state; unsupported targets pass through the filter. A hidden or disabled control does not silently continue
filtering or claim that filters are active.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Library filtering | The screen maps aggregate Library items and tracker state to API DTOs, then consumes the feature-selected item indices. It contains no filter policy or raw provider query. |
| Filter toolbar state | `hasActiveFilters` comes from the same feature result, so unavailable or hidden capability filters cannot advertise active filtering. |
| Filter settings dialog | Generic controls remain available. Bookmark and outside-release-period controls consume structured availability for current Library types. Preference reads/writes remain storage/UI mechanics, not policy interpretation. |
| Downloaded filter | F03 supplies graph-gated persisted counts and local state remains contextual; F14 interprets the resulting boolean without a second Download opt-in. |
| Consumed/not-started filters | F22/current Library summary supplies aggregate progress; F14 filters it without treating F09 mutation support as progress evidence. |
| Bookmark filter | F10 provider evidence selects control availability; F14 owns filtering existing aggregate state. Mutation remains F10. |
| Tracking filter | Logged-in trackers, per-tracker preferences, and Entry tracks are contextual external evidence. No Entry-type tracking capability is invented. |
| Outside-release-period filter | Provider presence selects applicability per type. F14 interprets the filter preference and fetch-interval state. |
| Merged items | Existing same-type collapsed Library items supply aggregate downloads/progress/bookmarks and the target Entry's release-period state. F14 adds no merge-specific branch or selection policy. |
| Search and category/source/type grouping | Remain contextual Library navigation. Their disposition is recorded here, but they are not made graph capabilities or physically moved into the filter coordinator. |
| Library-update category/source/type scope | Remains update-workflow context. F14 does not select update candidates. |
| Update eligibility | Remains F13. The two features share a preference concept but F14 neither emits update skip reasons nor changes the unconditional F13 policy. |
| Vocabulary | Remains F23 presentation and never authorizes filtering. |

## Automatic-Participation Proof

The focused feature contract composes anonymous types with no optional providers, Bookmark alone, and
Outside-release-period filtering. Provider-less types receive every generic filter consequence. Contributing Bookmark
or Outside-release-period evidence activates its owned control/applicability automatically for current Library types.
Mixed applicable/inapplicable targets prove that no type list, false provider, or app-side gate is required.

The contract exercises downloaded-only precedence, generic progress/status filtering, tracker inclusion/exclusion,
inactive unavailable filters, aggregate target state, and failure for a target type absent from runtime composition. It
does not assert which production types support either optional capability.

## Manifesto Review

- Generic Library filtering is contributed once for every composed type; it is not repeated by type modules.
- Bookmark and Outside-release-period participation derive from their existing provider evidence.
- Provider absence removes only the optional relationship and remains valid.
- The application consumes API DTOs and structured results, not SPI dispatch, graph evaluation, or support labels.
- Filters consume state from F03, F10, F22, trackers, and Library context without taking over those owners.
- F13 update eligibility, contextual grouping/search, and F23 vocabulary remain separate and explicitly accounted for.
- No mandatory operation, production type matrix, no-op provider, report/catalog, compatibility facade, or duplicated
  filter-policy implementation was introduced.

## Validation

- `spotlessApply`, API/SPI/root compilation, and all root Entry-interactions tests pass in the isolated F14 worktree.
- The boundary census reports seven expected later-feature violations and no raw Library-filter application reference.
- FOSS application compilation advances through F14 and stops only at the already recorded F11/F12/F18–F20 migration
  failures; it reports no Library-filter symbol, DTO, or consumer error.
