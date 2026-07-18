# F17 — Child List

Status: complete

## Owner and Relationships

- Feature owner: `entry-child-list`
- Fundamental prerequisite: `EntryChildListCapability`
- Shared consequences: applicability, reading order, display order, display rows, merged-member headers, missing-count
  presentation, and ordered-child selection for contextual consumers
- Optional relationship: `EntryChildListCapability` plus `EntryChildProgressCapability`
- Optional consequence: per-child progress-label subscription
- Operation context: concrete Entry, current children, merge-member order and titles, and missing-row visibility
- Specialized requirement: the selected type provider owns genuine media ordering/display behavior and the optional
  progress provider owns media-specific label interpretation
- Presentation projection: child nouns and missing-count plurals remain F23 vocabulary and never authorize the list
- Behavioral contract: synthetic Child List-only, Child Progress-only, combined, and empty contributions prove the two
  providers remain independent

Child-list provider presence is the sole applicability fact. Provider absence is valid and yields structured
`Inapplicable` results; it does not cause unsorted fallback data, a placeholder list, or invalid type composition.

## Feature Boundary

Application consumers receive `EntryChildListFeature`. The feature selects the contributed Child List provider for
reading order, display order, and display construction. Its display result carries both rows and the aggregate missing
count so presentation has no content-type gate or independent calculation authority.

The aggregate preserves the existing media behavior: Manga calculates it from the request child-number sequence even
when inline missing rows are hidden, while Anime and Book return zero through their genuine Child List behavior. Inline
row emission remains independently controlled by request context.

Child Progress dispatch is a separate internal boundary. Progress labels become applicable only when the same type
contributes both Child List and Child Progress providers. A Child List provider never implies progress labels, and a
Child Progress provider alone does not manufacture a visible list.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry filtering and display order | F14-owned filters still select children from current state; F17 orders only the filtered result through the selected provider. |
| Entry reading order | Continue-relative and mark-previous flows consume the feature-selected reading sequence from Entry state. |
| Display rows | Merged headers, child rows, and optional inline missing-count rows come from the feature display result. |
| Header missing count | The app consumes the provider-owned aggregate and no longer branches on Manga. |
| Unsupported list | The Entry screen omits the child-list surface when the feature is inapplicable. |
| Per-child progress labels | The Entry model subscribes only to the derived Child List plus Child Progress relationship; absence becomes an explicit inapplicable result and renders no label. |
| Entry preview first child | F19 retains preview availability and loading; its child selection consumes F17 reading order. |
| Immersive first child | F20 retains source/renderer/load/persistence ownership; its first consumable child comes from F17 reading order. |
| Child-group filtering | Deferred to F18; group availability, exclusion, persistence, and controls do not become Child List provider behavior. |
| Vocabulary | Deferred to F23 Type Presentation; resources format selected behavior but do not authorize it. |

## Automatic-Participation Proof

The focused feature test composes anonymous content-type contributions with zero bindings, Child List alone, Child
Progress alone, and both independent providers. Child List alone activates every ordering/display consequence without
activating labels. Child Progress alone remains valid but cannot create list behavior. Contributing both activates the
progress-label consequence automatically, without an app edit or intersection opt-in.

Type tests continue to exercise genuine Manga and Anime ordering, missing-row, and label behavior. They do not assert a
production support matrix, and no duplicate Book support assertion is added merely because Book currently contributes
both providers.

## Manifesto Review

- Provider presence is the only Child List and Child Progress authority; either provider may be absent.
- The optional labels relationship is derived from two independent providers rather than declared as another opt-in.
- Application code consumes one feature contract and cannot call the raw Child List or Child Progress dispatch.
- The former missing-count type gate and duplicate presentation calculation are removed.
- F14, F18, F19, F20, and F23 retain their distinct contextual and presentation responsibilities.
- No mandatory operation, concrete type matrix, support-report resurrection, no-op provider, fallback ordering, or
  compatibility shim was introduced.

## Validation

- Formatting, API/SPI and all production type/root compilation, Feature Graph/build-logic tests, and all root
  Entry-interactions unit tests pass in the isolated F17 worktree.
- The isolated boundary census reports 14 expected later-feature violations and no raw Child List or Child Progress
  application reference. FOSS compilation reports no F17 error before stopping at later raw facades.
- Full Manga/Anime type-suite execution remains blocked by their pre-existing raw test-harness calls omitting the now
  required feature-contributor argument. F17 does not hide that obligation with an empty feature contribution or
  compatibility fallback.
