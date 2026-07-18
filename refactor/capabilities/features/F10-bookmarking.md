# F10 — Bookmarking

Status: complete

## Owner and Relationship

- Feature owner: `entry-bookmarking`
- Prerequisite: `EntryBookmarkCapability`
- Shared consequences: applicability, mutation, and individual/selection eligibility
- Operation context: concrete Entry type, current bookmark state, selected children, and requested bookmark state
- Specialized requirement: none beyond the existing media-specific `EntryBookmarkProcessor`
- Presentation projection: bookmark nouns and action labels remain F23 vocabulary; they do not authorize behavior
- Behavioral contract: one synthetic provider activates the complete shared feature while provider absence remains valid

Bookmark-provider presence is the only support fact. F10 introduces no type list, explicit absence declaration,
bookmark-action opt-in, mandatory operation, or current-production support assertion.

## Feature Boundary

Application consumers receive `EntryBookmarkFeature`. It exposes structured applicability and eligibility results and
owns the shared state-change policy. Bookmarking a selection is available when every selected Entry participates and at
least one selected child would change. Removing bookmarks preserves the existing stricter selection behavior: every
selected child must participate and already be bookmarked.

Mutation filters unchanged children once in the root feature and then dispatches only the changed children to the
selected processor. `EntryBookmarkProcessor` therefore owns only media persistence. The raw bookmark interaction is an
internal provider dispatcher and contains no support or eligibility policy.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry screen | Bookmark buttons and bookmark swipe actions use feature applicability. The deleted report/catalog gate is not replaced. |
| Entry mutation | Multi-Entry child selections are grouped by owning Entry and sent through the feature; unsupported types return structured `Inapplicable`. |
| Updates actions | Selected rows become neutral bookmark targets and the feature evaluates the complete selection. Non-Entry rows remain a contextual blocker. |
| Updates mutation | Each concrete Entry group dispatches through the same feature-owned mutation boundary. |
| Bookmarked bulk download | Remains F04-owned and derives automatically from Download + Bulk Candidate + Bookmark provider evidence. |
| Download cleanup protection | Remains F06-owned and derives automatically from Download + Bookmark provider evidence. |
| Library bookmarked filtering | Deferred to F14, which owns Library filtering and its capability-dependent filter visibility. |
| Entry child-list bookmark filtering | Deferred to the child-list/filter migration; it is filtering existing state, not bookmark mutation support. |
| Bookmark state rendering, storage models, SQL filtering, and backup | Retained as domain/persistence behavior; these paths neither decide interaction applicability nor dispatch mutation. |
| Bookmark vocabulary | Deferred to F23 Type Presentation. |

## Automatic-Participation Proof

The focused feature test composes an anonymous plugin with one synthetic bookmark processor. That single provider
activates applicability, shared selection rules, unchanged-child filtering, and persistence dispatch. The companion
absence case returns structured `Inapplicable` without making the content type invalid. No test enumerates or restates
the Bookmarking support of Manga, Anime, or Book production modules.

Consequently, adding an Anime bookmark provider later requires only the Anime type contribution. F10 Entry and Updates
actions, F04 bookmarked bulk downloads, and F06 cleanup protection all select the new provider-derived relationship
without application, downloader, or lifecycle edits.

## Manifesto Review

- Provider presence is the sole operational and applicability authority; absence is valid.
- Shared eligibility and unchanged-child selection live once in the feature, not in each processor or UI surface.
- Application code consumes a feature contract and cannot query provider SPI or rebuild a capability report.
- Existing cross-feature consequences remain owned by their surrounding features instead of being duplicated in F10.
- Filtering, persistence, and vocabulary surfaces have explicit owners, so they are not silently mistaken for completed
  bookmark integrations.
- No concrete type branch, support matrix, capability-label test, mandatory operation, compatibility facade, or manual
  opt-in was added.

## Validation

- Formatting, focused compilation/tests, boundary checks, and combined-batch application validation are recorded by the
  Phase 5 milestone integration.
