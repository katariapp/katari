# F19 — Preview

Status: complete

## Owner and Relationships

- Feature owner: `entry-preview`
- Fundamental prerequisite: `EntryPreviewCapability`
- Shared consequences: contextual availability and configuration, Entry and browse surfaces, preview loading, lazy page
  loading, and strict handle release
- Optional relationship: Preview plus `EntryPreviewConfigurationCapability`
- Optional consequence: provider-derived settings and configuration changes
- Optional relationship: Preview plus Child List
- Optional consequence: first-reading-child selection for a child-backed provider
- Optional relationship: Preview plus Open
- Optional consequence: openable preview-page targets; F01 retains actual Open dispatch
- Context: concrete Entry and source, filtered child/owner candidates, merge-member order, preference state, and requested
  page count
- Specialized requirement: each Preview provider declares one stable load mode, either entry-level or
  first-reading-child-backed
- Behavioral contract: synthetic absent, fixed-config, independently configured, child-backed, and Open-composed
  contributions

Preview provider presence is the only type-wide support fact. A missing provider is valid and yields structured
`Inapplicable` results. A registered provider is not flattened into unconditional runtime availability: source support,
preference state, and child presence remain named contextual outcomes.

## Feature Boundary

Application code receives `EntryPreviewFeature`. The graph-selected coordinator owns applicability, configuration,
availability changes, loading, lazy page loading, open-target selection, and release. Raw provider dispatch remains
inside the Entry-interactions composition boundary.

The preview provider owns genuine media loading and lifecycle behavior. Manga contributes a child-backed loader that
uses reader pages and reference counting. Anime contributes an entry-level loader that consumes the source-owned
`EntryPreviewSource` contract. Book contributes no Preview provider.

Configuration is an independent provider relationship. Manga and Anime currently contribute preference-backed
configuration; a future Preview provider can omit it and receive the shared fixed configuration without a no-op
settings provider. Configuration providers also declare contextual settings requirements, such as source capability,
so the settings screen renders graph-selected entries without naming content types. F23 remains responsible for wording
only.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry preview section | Visibility and configuration follow structured Feature availability. Expand, retry, load, page-load, and release operations use the Feature. |
| Browse preview sheet | Reuses the Entry preview coordinator and lifecycle; entry-level previews remain loaded without manufacturing a child ID. |
| Browse long press | The shared fallback policy consumes F19 availability. F20 still owns Immersive evidence and the combined policy remains contextual. |
| Child selection | The app supplies filtered child/owner/source candidates; F19 consumes F17 reading order and chooses the first child for child-backed providers. |
| Open preview page | F19 derives the Preview-plus-Open target and F01 performs actual opening. Provider page metadata and a real selected child must both permit it. |
| Settings | Provider-derived configuration entries supply enable, page-count, size, and contextual-requirement metadata. Fixed-config Preview remains valid and adds no settings row. |
| Source support | Anime evaluates the concrete source as `EntryPreviewSource`; missing source support is contextual unavailability, not missing Anime Preview support. |
| Lifecycle | Handle page loading and release dispatch strictly to the producing provider; missing handle ownership is an architecture error rather than a silent no-op. |
| Vocabulary | Section, empty-state, and settings wording remain F23-owned and never authorize Preview. |

## Specialized Requirement Validation

Load mode is declared by the Preview provider itself, not by an app type switch or another opt-in. Coordinator
construction checks every child-backed Preview provider against the graph-selected Preview-plus-Child-List relation.
Missing Child List participation fails with the affected type and required relationship. Entry-level providers remain
valid without Child List.

## Automatic-Participation Proof

The focused contract composes an anonymous type with no provider, Preview alone, Preview plus independent
configuration, child-backed Preview without Child List, and Preview plus Child List plus Open. It proves that Preview
alone activates every common surface and lifecycle consequence with fixed configuration, configuration adds settings
automatically, missing child-backed architecture fails immediately, and the two optional feature combinations activate
without an application or content-type matrix edit.

Type-focused tests retain genuine Manga loader and Anime source-image behavior. Layout and long-press tests retain
generic presentation and fallback behavior; they do not restate a production support matrix.

## Manifesto Review

- Provider presence is the sole Preview support authority and provider absence remains valid.
- Source, preference, child, and Open conditions are composed outcomes rather than duplicate type flags.
- Child-backed loading declares its stable media requirement once and receives an actionable architecture check.
- Configuration and Open are independent relationships; neither is implied by Preview.
- Settings participation and contextual notes are discovered from provider bindings instead of a Manga/Anime list.
- Application production code uses only the Feature contract, while media providers retain genuine loading differences.
- No mandatory operation, concrete type matrix, support report/catalog, no-op provider, silent release, or compatibility
  facade was introduced.

## Validation

- API/SPI, Manga, Anime, Book, root Entry-interactions production compilation and the focused F19 Feature contract pass.
- Combined FOSS application compilation passes; the boundary census contains only the later F11/F12 and F20 owners.
- Manga/Anime full type-test compilation remains blocked by the pre-existing Phase 5 test-harness obligation: legacy
  `createEntryInteractions` calls omit the required feature-contributor argument. F19 does not add an empty contributor
  fallback to hide it.
