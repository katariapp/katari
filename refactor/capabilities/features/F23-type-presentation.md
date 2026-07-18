# F23 — Type Presentation

Status: complete

## Owner and Relationships

- Feature owner: `entry-type-presentation`
- Fundamental projection input: `EntryTypePresentationCapability`
- Shared consequence: type vocabulary, icons, plurals, and formatting policy exposed through
  `EntryTypePresentationFeature`
- Context: a concrete Entry type, or an explicitly mixed/no-type selection
- Presentation relationships: Entry, Library, Updates, History, Browse, Download, Duplicate, Tracking, Immersive, and
  the vocabulary consumed by F24 Library-update Notifications
- Behavioral prerequisite: none
- Specialized requirement: none; a type may contribute any subset of interactions without contributing presentation
- Behavioral contract: synthetic projection discovery, explicit generic provenance, and valid provider absence

Presentation provider presence is solely evidence that owned vocabulary exists. It does not prove Open, Continue,
Download, Consumption, Bookmarking, Child List, Update Eligibility, or any other behavior. Those features remain the
only authorities for their applicability and actions.

## Feature Boundary

Each type module contributes one `EntryTypePresentationProvider` through its ordinary `EntryInteractionPlugin`. The
provider carries resource tokens, imagery, and presentation policy; the plugin does not enumerate consumers. F23's
discoverable contribution selects every provider automatically and exposes only `EntryTypePresentationFeature` to the
application.

The former application-owned `when (EntryType)` map is removed. App presentation helpers now resolve the Feature and
render its projection. History subtitles and partial-progress labels use sealed type-owned presentation models, so the
shared renderer contains no Manga/Anime/Book vocabulary branch or media-specific resource token.

Provider absence remains architecturally valid. Resolution returns either `Contributed(type, presentation)` or
`Generic(type?, presentation)`. The generic result always retains the requested concrete type, making emergency
vocabulary observable rather than silently pretending that another type supplied it. Mixed/no-type surfaces request
the same neutral projection explicitly. Any shipped-product requirement for owned vocabulary is a separate validation
policy, not content-type validity.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry and child lists | Selection actions, counts, missing-count rows, settings, empty/no-next states, and inline type indicators resolve contributed vocabulary. Behavioral controls still use their owning Features. |
| Library and Updates | Type groups, badges, selection download wording, consumed indicators, deletion prompts, and partial progress resolve F23. Library filters, downloads, and progress remain F14/F04/F22 decisions. |
| History | Subtitle composition and no-next wording resolve the type projection. Continue availability and execution remain F02. |
| Browse and Preview | Type filters/badges, source metadata indicators, and settings labels resolve F23. Source metadata remains descriptive and F19/F20 remain behavioral authorities. |
| Download | Amount, consumed-state, and number-sort vocabulary resolve F23 after F04/F07 decide applicability. Presentation does not expose a download-support flag. |
| Duplicate and Tracking | Child count/reason and child-list wording resolve F23. Duplicate selection and tracker applicability remain their own owners. |
| Immersive | Open wording/icon resolves F23 only after F20 selects an Open target. |
| Library-update notifications | F23 supplies channel, summary, child-description, view, consumption, number-selection, and truncation vocabulary/policy. F24 owns grouping, IDs, channels, routing, actions, and feature composition. |

## Notification Vocabulary Boundary

`EntryUpdateNotificationVocabulary` is a neutral resource-token model. It contains channel/summary resources,
generic/single/multiple child-description resources, the View label, number eligibility policy, and truncation count.
F24 can therefore share one renderer without a type map while retaining all notification behavior in its own feature.
Book contributes explicit neutral Item terminology instead of inheriting Manga terminology.

## Automatic-Participation Proof

The focused contract composes a partial type with no projection and a synthetic type with one projection. It proves a
newly discovered provider is selected without editing F23, provider absence remains a valid explicit Generic result,
and mixed/no-type vocabulary is distinguishable from a contributed concrete projection. The test does not repeat the
current Manga/Anime/Book declarations.

Boundary validation now rejects an application-owned exhaustive Manga/Anime presentation mapping, including in the
former central presentation helper. Type modules remain free to own their concrete descriptors, and compatibility,
storage, source, and media boundaries retain their reviewed exceptions.

## Manifesto Review

- Type vocabulary is declared once in the owning type contribution and discovered through the same plugin/graph path
  as other owned inputs.
- Adding a projection provider activates every current shared presentation consumer without editing a central type map.
- Presentation contains no behavioral support flags and never authorizes an action.
- Provider absence is valid; explicit generic provenance prevents another type's semantics from being silently reused.
- Shared renderers own formatting mechanics while type projections own every variable resource token and policy.
- Notification vocabulary is available to F24 without moving notification routing or action ownership into F23.
- Tests verify discovery/resolution mechanics rather than asserting a production support matrix.
- No mandatory operation, type allowlist, per-feature opt-in, compatibility facade, no-op provider, or behavioral
  fallback was introduced.

## Validation

- API, SPI, Manga, Anime, Book, and root Entry-interactions production compilation pass.
- Root Entry-interactions behavior tests and build-logic boundary tests pass.
- FOSS compilation reports no F23 diagnostic; it reaches only the separately owned F11/F12 and unrelated concurrent
  application migration errors present on the batch baseline.
- The application census contains no concrete-type presentation map and no app import of the raw presentation provider
  or operational interaction.
