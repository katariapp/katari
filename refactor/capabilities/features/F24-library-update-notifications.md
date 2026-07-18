# F24 — Library-update Notifications

Status: complete

## Owner and Relationships

- Feature owner: `entry-library-update-notifications`
- Base prerequisite: none; every composed content type that actually participates in a Library update receives shared
  routing, grouping, summary, child-notification, and Entry-details behavior
- Presentation relationship: F23 Type Presentation supplies vocabulary and numbering policy; presentation never
  authorizes notification participation or an action
- Open relationship: F01 Open adds the child-open destination independently
- Consumption relationship: F09 Consumption adds Mark Consumed independently
- Download relationship: F04 Download Actions adds Download independently, with its existing source, empty-selection,
  and 15-child contextual policy
- Always-available action: normal Entry-details navigation is not F01 Open
- Context: actual updated Entries/children, merged visible Entry identity, concrete source access, hidden-content privacy,
  and Android notification rendering
- Behavioral contract: partial action combinations, contextual Download rejection, vocabulary-driven description
  construction, frozen legacy routes, and derived neutral routing

Notification participation is shared behavior over every composed type. A partial future type with no Open,
Consumption, or Download provider is still valid: it receives grouped notifications whose child tap and View action
open Entry details, while unsupported actions are absent.

## Feature Boundary

Application code receives `EntryLibraryUpdateNotificationFeature`. Its projection is the complete platform-neutral
render plan: routes, groups, summaries, resolved visible Entries, child descriptions, destination policy, and action
set. `LibraryUpdateNotifier` supplies only actual update/source context and renders Android builders and pending intents.
`Notifications.createChannels` consumes the Feature's discovered routes rather than maintaining a second type list.

F23 supplies resource tokens plus child-number policy through `EntryTypePresentationFeature`. F24 applies those tokens
with one shared formatter. A contributed presentation is graph-accounted; explicit generic presentation remains an
observable F23 result for a valid partial type and does not become behavioral authorization.

## Routing and Compatibility

Manga and Anime keep their previously shipped channel, group, and summary identifiers through the explicitly frozen
`LegacyLibraryUpdateNotificationRouteCompatibility` adapter. It is a compatibility boundary, not a support catalog,
and its contract forbids adding future types.

Book and every future contributed type use stable routing derived from the content-type identity. Channel, group, and
summary-ID collisions are validated across every discovered route before the Feature is exposed. Book therefore uses
its own neutral Item route and F23 vocabulary; it can never fall through to Manga chapter/read semantics.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Update worker | Continues reporting actual `(Entry, children)` updates. The notifier converts source access to structured F04 context and calls only the F24 Feature for notification policy. |
| Notification channels | Created from `EntryLibraryUpdateNotificationFeature.routes()`. Adding a contributed type produces a route without editing `Notifications.kt`. |
| Summary grouping | F24 groups by authoritative `Entry.type`, selects a stable route, and supplies F23-owned summary vocabulary. |
| Merged visible identity | F24 resolves the visible Entry before projecting destinations. F12 only permits same-type merges; a mismatched visible type is a failed invariant rather than a route/action reinterpretation. |
| Child description | F24 owns one generic description algorithm. F23 supplies nouns, plural resources, recognized-number policy, and display limit without authorizing behavior. |
| Child tap | F01 plus F24 yields Open Child. If F01 is absent, the explicit destination is Entry Details; notification participation is retained. |
| Mark Consumed | Present only for the F24 plus F09 relationship and non-empty children. Execution remains the F09 Feature path, including its F06 lifecycle consequence. |
| View Entry | Always opens normal Entry details for the resolved visible Entry. It is not F01 Open. |
| Download | Present only for the F24 plus F04 relationship after F04 accepts concrete source access and selection size. Execution remains F04. |
| F03 runtime | No direct relationship: notification downloads use F04 actions; queue execution and foreground download notifications remain F03-owned. |
| F05 automatic downloads | No direct relationship: library-update automatic selection and scheduling remain F05-owned and do not authorize a user notification action. |
| F06 lifecycle | Composes transitively through F09 Mark Consumed; F24 does not duplicate cleanup policy. |
| F07/F08 | No direct relationship: options/settings and maintenance do not affect this notification projection. |
| Progress/error notifications | Remain shared Library-update operational notifications; their unused Anime duplicate IDs are removed. |

## Automatic-participation Proof

The F24 base integration is always applicable to every discovered content contribution. Independent graph relations
select presentation vocabulary, Open, Consumption, and Download consequences from their real providers. Tests exercise
a type with presentation only, then add each behavioral provider without an F24 opt-in. Missing relationships preserve
the notification and omit only their action; contextual Download rejection also omits only Download. Route validation
uses every discovered type and contains no current-type support matrix.

## Manifesto Review

- Actual type contribution plus update participation activates shared notification behavior automatically.
- F01, F09, and F04 relationships are derived from their providers; F24 declares no combined provider or per-type
  action flag.
- Normal Entry-details navigation is kept distinct from F01 child opening.
- F23 vocabulary varies independently and never decides behavioral support.
- Book receives explicit neutral Item vocabulary and derived routing, never a Manga fallback.
- The only Manga/Anime branch is a frozen shipped-identity compatibility adapter with an explicit no-growth contract.
- New routes are derived and collision-validated; channel creation consumes Feature output instead of a type list.
- The app notifier consumes one Feature projection and does not rebuild type grouping, action eligibility, or vocabulary
  selection.
- No mandatory operation, no-op/fallback provider, type support matrix, capability declaration test, or silent action
  fallback was added.

## Validation

- Focused Feature behavior and relationship tests pass against the integrated F23 API, including presentation-provider
  absence, independent action relationships, contextual Download rejection, shared descriptions, and route identities.
- Feature Graph, build logic, Domain, API, SPI, Manga, Anime, Book, root production, and root behavior tasks pass.
- The boundary census reports exactly the three known F11/F12 raw `EntryCapabilityInteraction` references and no F24
  leak. FOSS compilation reports no F24 diagnostic; it reaches only those F11/F12 and unrelated concurrent application
  migration errors already present on the batch baseline.
- No emulator or physical device is used.
