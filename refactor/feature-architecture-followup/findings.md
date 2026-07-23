# Feature Architecture Follow-up Findings

## Purpose

This document preserves the read-only audit performed after the initial Entry Feature migration and its enforcement
phase. The architectural authority remains [`../capability-manifesto.md`](../capability-manifesto.md). This file records
concrete remaining gaps so remediation does not depend on conversation context.

Audit baseline:

- Branch: `features-arch-refactor`
- Baseline commit: `94f9a0a8c` (`(refactor): complete entry feature enforcement`)
- Audit completed: 2026-07-23

## Root cause

The current graph discovers ordinary execution participants, but several older workflows still predate that runtime
model. They declare behavior projections while executing fixed dependency lists, constructing known consequence IDs,
or relying on callers to invoke the next Feature. The architecture can therefore report those behaviors as complete
without proving that every affected Feature participates.

The remaining work is not content-type parity patching. It must remove the paths through which a future Feature or
content type can be implemented correctly in isolation while its downstream lifecycle, background, persistence, or UI
integration is silently forgotten.

## Approved gaps

### Durable Merge and Migration consequences

Merge and Migration construct durable consequence records from fixed artifact IDs and dispatch them through
`when (artifactId)`. Adding another participating Feature requires edits to the coordinator, codec, and delivery list.
Durability is legitimate; curated participation is not. The execution architecture needs an owner-contributed durable
participant model, after which Merge and Migration must consume discovered preparations and handlers.

Custom-cover transfer and cleanup remain host-owned contributed consequences. They do not become a standalone Feature.

### Media-session and consumption consequences

Manga, Anime, Book, and immersive media paths independently persist progress and history, apply incognito policy,
synchronize Tracking, and emit Download lifecycle events. This has already drifted: Anime persists progress and history
without the incognito gate used by Manga and Book.

History recording has no Feature boundary. `EntryReaderTracking` is a Manga-only bridge, and Consumption declares
Tracking synchronization while its coordinator invokes only Download lifecycle directly. Media runtimes must emit a
shared structured event; Progress, History, Tracking, Download, and policy owners must participate through their own
contributions.

### Refresh consequences

Entry refresh and Library update advertise automatic Download behavior in graph projections, but callers manually
invoke `EntryAutomaticDownloadFeature`. `EntryLibraryUpdateRefreshFeature` declares a new-child handoff without an
execution point. Refresh workflows must expose typed execution points so Automatic Download and future Features can
contribute independently.

### Profile deletion

Permanent Profile deletion clears a fixed list of Feature-owned tables and bypasses `EntryDestructiveRemovalFeature`.
It can therefore skip external Download, Merge, custom-cover, and future cleanup. Profile deletion must compose with
discovered Entry destructive-removal and profile-state participation rather than maintaining another cleanup list.

### Bookmark consumer bypass

The Manga Reader writes bookmark persistence directly even though Manga contributes `EntryBookmarkProcessor` and
`EntryBookmarkFeature` is the application boundary. The Reader must use the Feature so contracts and future Bookmark
consequences cannot be bypassed.

### Feature-owned settings discovery

Settings navigation and search maintain separate static screen lists. Viewer Settings has a discovered app projection,
but a future Feature-owned settings surface can still be omitted from either surface. A general app-owned settings
projection must provide one installation fact to navigation, search, and validation.

## Explicit non-gaps

- Stored child-state filters remain generic. Downloaded, consumed, started, bookmarked, and child-group state may be
  queried even when the current type lacks the matching mutation provider.
- Catalogue metadata on the domain `Source` model is populated through the Catalogue Feature description port. Reading
  that projection in UI or feed code is not raw source-capability dispatch.
- Source SDK and tracker service capability metadata remain external-owner facts interpreted by their Features.
- Generic notes, categories, display names, and fetch-interval editing remain ordinary Entry state.
- Compatibility adapters may switch on finite legacy participant or content-type identifiers.

## Deferred candidates

These were not approved as defects and must not be silently included in remediation:

- Upcoming/calendar could become an always-applicable Entry Feature for reporting and contracts, but it has no current
  content-type parity defect.
- Preview, Immersive, and Related Entries expose `UnifiedSource` in public Feature APIs. This is boundary hardening, not
  currently a duplicated support decision.
- `FeedItemRef` deserialization hard-codes the three current Entry types. That is a future-type extensibility defect,
  not a Feature-architecture gap.

