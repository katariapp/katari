# F26 — Media-cache Maintenance

Status: complete

## Owner and Relationships

- Feature owner: `entry-media-cache`
- Fundamental prerequisite: `EntryMediaCacheCapability`
- Shared consequences: artifact discovery, Data settings projection, manual clear, launch auto-clear, preference
  construction, cache-size invalidation, and structured failure reporting
- Context: the host supplies concrete cache storage and each type provider adapts the artifacts it genuinely owns
- Persistence compatibility: Anime's auto-clear preference is seeded once from the former shared Manga preference when
  its stable key is absent; this rule does not authorize support
- Specialized requirement: none; one provider may expose multiple non-empty artifacts and provider absence is valid
- Behavioral contract: synthetic provider presence and absence, complete shared participation, refreshed size,
  independent launch failures, legacy preference seeding, and duplicate/empty contribution rejection

Media-cache provider presence is sufficient for every shared maintenance consequence. There is no separate manual-clear
or auto-clear support flag. A type without a provider simply contributes no cache setting and remains valid.

## Feature Boundary

Application consumers receive `EntryMediaCacheFeature`. Settings obtain cache labels, current readable size, and the
auto-clear preference from its projections. Startup asks the same Feature to clear every enabled contributed artifact.
Both paths receive structured `Cleared`, `Failed`, or `Inapplicable` results and never access providers, cache keys, or
concrete cache implementations.

The raw artifact/provider contracts remain in SPI. Manga adapts the host page-image cache, Anime lazily adapts the player
cache, and Book adapts its materialized-resource cache through their ordinary type plugins. The former runtime
`mediaCacheBuckets` list, central bucket-key object, maintenance registry, dedicated preference holder, hardcoded settings
label map, and launch key selector are removed.

Stable artifact IDs and preference keys are owned by the type provider as persistence/presentation descriptors, not by
root composition. The shared Feature rejects blank or duplicate identities, rejects providers with no genuine artifact,
constructs preferences, applies named compatibility seeding, refreshes readable sizes after clearing, and isolates each
launch failure so later artifacts still run.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Cache discovery | Graph-selected providers expose all owned artifacts through the ordinary plugin boundary; no root cache list remains. |
| Data settings labels | Provider-owned clear and auto-clear resources reach one Feature projection; the application has no ID-to-label map. |
| Manual clear | The settings screen calls `EntryMediaCacheFeature.clear` and maps its structured result to success/error feedback. |
| Launch auto-clear | Startup calls `clearEnabledOnLaunch`; every contributed artifact participates from the same shared preference policy. |
| Preferences | The Feature constructs provider-described stable preferences. Book now receives the same shared auto-clear consequence automatically. |
| Cache invalidation | A fresh settings projection reads the artifact's current size after a successful clear. |
| Errors | Each artifact failure is returned explicitly; one launch failure does not suppress unrelated cache clears. |
| Profile ownership | F27 remains responsible for general discovery/movement of profile-scoped preferences. Current media-cache preferences remain global. |

## Automatic-participation Proof

The focused contract composes a valid partial type without Media Cache and a synthetic type with only a Media Cache
provider. Provider presence activates discovery, settings, preferences, manual and launch clearing, invalidation, error
reporting, and the behavioral contract without an application or root-composition edit. Multiple artifacts demonstrate
independent failure handling. Empty providers and colliding stable IDs fail explicitly.

## Manifesto Review

- Provider presence is the only support authority; absence is valid and does not require a declaration.
- Manual clear and launch auto-clear are shared consequences rather than provider opt-ins.
- Type modules own only real cache access plus persistence/presentation descriptors; shared policy stays in the Feature.
- The root parallel bucket list, hardcoded key/label maps, app-owned exception handling, and dedicated current-type launch
  selector are removed.
- Legacy preference seeding is a named one-time compatibility rule and cannot create support without a provider.
- No mandatory operation, current-type matrix, silent fallback, no-op provider, compatibility facade, or central cache
  list was introduced.

## Validation

- API/SPI, Manga, Anime, Book, and root Entry-interactions compilation passes.
- The root behavior suite covers graph-selected presence/absence, every shared consequence, preference compatibility,
  refresh, isolated errors, empty providers, and duplicate identity rejection.
- Build-logic boundary validation requires application settings/UI to use `EntryMediaCacheFeature` and rejects concrete
  cache access as well as direct use of the host page/player cache ports.
- FOSS compilation reaches only the independently assigned F11/F12 and existing application migration errors; it
  reports no F26 settings, startup, provider, or Feature API error.
- Full Manga/Anime/Book test compilation remains blocked by the pre-existing Phase 5 test-harness obligation: legacy
  `createEntryInteractions` calls omit required feature contributors. It reports no F26 production-source diagnostic.
- The global boundary/Spotless check reaches only the same F11/F12 raw-capability violations; `spotlessApply`, focused
  module formatting, and `git diff --check` pass for F26.
