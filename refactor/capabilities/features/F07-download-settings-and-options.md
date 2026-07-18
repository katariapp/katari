# F07 — Download Settings and Options

Status: complete

## Owner and Relationships

- Feature owner: `entry-download-configuration`
- Interactive options prerequisite: `EntryDownloadOptionsCapability`
- Specialized setting prerequisites: archive packaging, tall-image splitting, parallel source transfers, and parallel
  item transfers each require only their own independently contributed setting provider
- Shared consequences: settings-surface visibility and search indexing for each selected setting; contextual option
  resolution; selected-option download execution and persistence
- Operation context: the actual Entry, child, source availability, media descriptor, streams, dubs, subtitles, quality
  choices, and current stored selection
- Specialized requirement: option providers resolve and persist genuinely media-specific choices; setting providers
  identify real downloader behavior that consumes each preference
- Presentation projection: settings labels and option group labels describe selected behavior but never authorize it
- Behavioral contracts: synthetic partial contributions prove independent settings, options without a core downloader,
  contextual absence, provider absence, and selected-option execution

None of the five providers is mandatory. Core Download support does not imply interactive options or specialized
settings, and an options or setting provider does not require a redundant core-Download prerequisite to prove the
behavior it directly supplies.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Download settings screen | Receives the feature-selected set of specialized settings. It does not query a type map or name Manga as the settings authority. |
| Settings search | Uses the same generated preference list as the screen, so unavailable specialized settings cannot remain independently searchable. |
| Generic download settings | Wi-Fi-only, automatic-download, lifecycle cleanup, and download-ahead preferences remain unconditional or with their owning feature. They do not require content-type opt-in. |
| Entry download action | The options dialog branch follows options-feature applicability. Types without the provider continue through the ordinary F04 action. |
| Options dialog | Renders resolved groups and selections only. It has no provider, graph, or content-type authorization logic. |
| Contextual option absence | A present provider may resolve no choices for the current source/media. The screen falls back to the ordinary F04 download path instead of inventing a negative type declaration. |
| Selected option dispatch | Entry invokes `EntryDownloadOptionsFeature`; application code no longer calls raw download-option dispatch. |
| Anime options provider | Resolves stream, dub, subtitle, and quality choices from the actual source/media and persists the selected values while queueing. It remains behind the feature boundary. |
| Manga settings providers | Four independent bindings describe the preferences actually consumed by Manga archive/page download behavior. No combined settings set or empty/default provider is registered. |

## Independent Setting Semantics

The four specialized preferences are not generic merely because their controls share one screen. Their current values
are consumed by Manga downloader behavior: archive output, tall-image splitting, concurrent sources, and concurrent
pages. Each therefore remains an independent provider-backed fact. The feature projects the union of selected setting
consequences into a neutral Download Behavior group without exposing a per-type support matrix.

The rest of the download settings screen is generic policy owned elsewhere. Requiring every type to opt into those
controls would recreate the automatic-filter mistake corrected in F05.

## Automatic-Participation Proof

The focused contract constructs anonymous partial content-type contributions:

- an Options provider without core Download activates contextual resolution and selected execution;
- a present Options provider can return contextual absence without changing static applicability;
- removing Options remains valid and yields an inapplicable result; and
- contributing any subset of specialized setting providers selects exactly those settings without a downloader,
  default provider, current-type table, or combined setting declaration.

## Manifesto Review

- No Manga/Anime/Book matrix, mandatory setting, default-empty provider, Manga UI hardcode, or support-label test was
  added.
- Graph-selected provider consequences are the only authority for specialized setting visibility and option
  applicability.
- Source/media data and available choices remain contextual rather than becoming type-wide flags.
- Generic settings remain generic instead of gaining repeated type opt-ins.
- Application code depends on separate feature-owned settings and options contracts; raw F07 dispatch is coordinator
  internal.
- A future type contributes only the specialized behaviors it implements. Existing F07 settings surfaces and option
  execution discover those contributions without another application edit.

## Validation

- Formatting, focused compilation, behavioral tests, and the raw-boundary census are recorded in the integration
  milestone that combines F06–F08.
- Any root compilation failure owned by an adjacent incomplete feature remains visible rather than being hidden by an
  F07 compatibility path.

## Remaining Boundaries

- F03 owns queue/runtime state and controls.
- F04 owns ordinary individual and bulk actions. F07 falls back to F04 when contextual options are unavailable.
- F05 owns automatic-download policy and scheduling.
- F06 owns lifecycle cleanup policy.
- F08 owns rename, cache invalidation, migration/catalogue removal, and other download maintenance paths.
