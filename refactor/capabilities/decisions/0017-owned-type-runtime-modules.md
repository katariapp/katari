# Owned Type Runtime Modules

Status: Accepted

## Context

Type participation remained split after provider migration. Root composition separately installed three runtimes,
constructed three plugins, listed three library-progress calculators, assembled viewer settings and cache buckets through
different paths, invoked Manga/Anime warmups, and installed Manga image components directly. A new type could therefore
contribute providers correctly while being omitted from several runtime integrations.

Presentation vocabulary is also type-owned, but its current app model combines vocabulary, icons, and app resources. It
is a feature projection input rather than a runtime service and cannot be moved into the runtime contract without
creating another mixed-purpose authority.

## Proposed Decision

- Each content type owns one `EntryTypeRuntimeModule` installed from the environment's single type-module list.
- Installing a module returns one validated contribution containing its interaction plugin, library-progress
  calculator, viewer-setting providers, media-cache buckets, startup warmups, and image-component installers.
- Root composition derives all corresponding runtime collections from those contributions. It does not separately list
  types for plugins, calculators, settings, caches, warmups, or image components.
- The environment still names each installed type module once. This is contributor aggregation, not a capability or
  feature allowlist.
- Shared feature lifecycle work, such as starting the download notification coordinator, remains shared and is not
  attributed to a type module.
- Presentation vocabulary remains an explicit Phase 5 projection obligation. Its type-owned inputs and feature-owned
  consumers must migrate together rather than putting app presentation resources into the runtime module.

## Milestone 4.3 Application

- Manga, Anime, and Book each expose one runtime module.
- Manga owns its plugin, library progress, reader settings, download warmup, and Coil image components through that
  module.
- Anime owns its plugin, library progress, player settings, playback cache, and download warmup through that module.
- Book owns its plugin, library progress, format settings, and materialization cache through that module.
- The old separate library-progress root list and public per-type runtime installation paths are removed.
- Runtime contribution identity is validated against both the plugin type and library-progress calculator type.

## Consequences

- Adding a type to environment composition once brings all of its currently owned runtime artifacts with it.
- A type cannot silently return a plugin or calculator for another type.
- Feature integrations can later select settings, caches, warmups, and presentation projections without rediscovering
  concrete types.
- The remaining transitional registry and test fixtures can be removed in Milestone 4.4 without another runtime list.

## Alternatives Rejected

- Retaining one root list per runtime artifact family
- Moving the existing app presentation model wholesale into the runtime contract
- Treating shared notification startup as a per-type warmup
- Inferring caches or settings from capability names or hardcoded keys
- Runtime classpath scanning instead of explicit environment contributor aggregation
