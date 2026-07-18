# F03 — Download Queue and Runtime

Status: complete

## Owner and Relationship

- Feature owner: `entry-download-runtime`
- Prerequisite: `EntryDownloadCapability`
- Shared consequences: status observation, unified queue state and controls, library counts, application initialization,
  process-resilient job execution, and foreground/error notification rendering
- Context: concrete Entry and child identities, persisted files, queue contents, and network state are runtime inputs, not
  type-wide capability facts
- Specialized requirement: none; each contributed `EntryDownloadProcessor` owns its media-specific queue and persisted
  download inspection
- Presentation projection: none; queue titles, child vocabulary, and status descriptions use the shared models and
  existing presentation metadata without deciding applicability
- Behavioral contracts: a synthetic provider proves that one contribution activates shared queue state, status/count
  access, and runtime controls. Provider absence produces an empty valid runtime and no status.

Provider absence makes the runtime inapplicable to that Entry type. It does not invalidate the content type, create a
no-op provider requirement, or prevent the shared queue coordinator from existing with an empty provider set.

## Access Boundary

Application code receives `EntryDownloadRuntimeFeature`. It can observe graph-selected runtime state and operate on
already queued work, but it cannot call the provider dispatcher, execute the worker loop, or consume downloader event
streams.

The root-internal coordinator extends the feature contract with only two shared-runtime operations:

- running every applicable media downloader until its queue is idle, for the WorkManager job;
- consuming structured warning/error events, for the central notification manager.

Those operations remain inaccessible from the application's compile classpath. The shared worker and notification
manager are single coordinators; graph applicability selects their participating provider types rather than creating a
worker or renderer per type.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry child state | Download changes, queue changes, and status snapshots use the runtime feature. Starting, retrying, action-triggered cancellation, deleting, bulk selection, options, and automatic discovery remain with their owning features. |
| Updates child state | Status refresh and status snapshots use the runtime feature. Download/retry/cancel orchestration remains F04. |
| Library | Change observation and merged-member download counts use graph-gated runtime inspection; the downloaded filter and badges consume the resulting count. Bulk actions remain F04. |
| Unified download queue | Queue contents, running state, start/pause/clear, reorder, move-entry, and cancel controls use one feature contract over every applicable provider. |
| More tab | Running/paused/pending presentation is derived from the unified feature state rather than a raw downloader. |
| Main activity | Download initialization is observed from the same feature state for the indexing banner. |
| Stats | The total persisted download count is supplied by the runtime feature. |
| Download worker | The root-internal coordinator runs applicable providers until idle while the shared job retains network and foreground-service policy. |
| Foreground and event notifications | The central manager consumes feature state, queue progress/status, and internal structured events; it contains no media-type branch. |
| Notification queue controls | Resume, pause, and clear actions call the feature contract. Notification-triggered child downloading remains F04. |
| Work controller | The host runtime port remains shared infrastructure supplied once to type-owned download managers; starting it cannot advertise Download support. |

No application production consumer that only needs F03 behavior imports `EntryDownloadInteraction`. After the combined
F03–F05 migration, Library, Updates, and the notification receiver no longer import the raw facade at all. Entry retains
it only for the explicitly deferred F07 options and F08 maintenance paths in the same file.

## Explicitly Excluded Download Features

- F04 owns queuing, individual/bulk download and deletion actions, candidate resolution, and selection blockers.
- F05 owns automatic-download discovery, shared candidate policy, and queue-start orchestration after discovery.
- F06 owns lifecycle events, cleanup policy, bookmark protection, and physical cleanup dispatch.
- F07 owns option resolution/persistence and specialized setting visibility.
- F08 owns rename, removal, and cache-maintenance hooks.

The runtime feature does not expose these methods merely because the current raw provider facade contains them.

## Automatic-Participation Proof

The focused feature test installs an anonymous content-type contribution containing one Download provider. Graph
evaluation selects every F03 consequence and the shared coordinator exposes its queue, status, count, and runtime
control without a type list or application edit. The companion absence case proves that an empty provider set remains
valid and produces an empty runtime rather than a missing-work obligation.

The feature contribution names every F03 consequence. A consistency assertion rejects divergent provider selection
between those consequences, preventing one UI/background path from silently evaluating a smaller support set.

## Manifesto Review

- Download remains optional; no current-type support matrix, no-op provider, or mandatory operation was introduced.
- Provider presence is the only type-wide runtime support fact.
- One feature contribution owns the UI, count, initialization, job, and notification consequences.
- Application consumers cannot reconstruct support from SPI or graph evaluation.
- A future type gains every shared runtime consequence by contributing `EntryDownloadCapability`; no application,
  worker, notification, or queue-screen type branch is required.
- Media-specific storage and queue implementation remain with the type provider, while common orchestration is shared.
- F04–F08 are not hidden inside a broad Download facade and retain independent feature ownership.

## Validation and Remaining Boundaries

- Formatting, API compilation, and SPI compilation pass under repository JDK 21 and the configured Android SDK.
- The combined boundary checker reports 34 remaining raw application references. Download Queue, More, Main, Stats,
  Library, Updates, and Notification Receiver contain no F03–F05 raw download access. Remaining findings belong to
  F06–F27 and stay visible.
- Root compilation reaches only the three previously recorded F06 lifecycle failures: deleted
  `EntryCapabilityReport`, deleted `EntryDownloadCapabilityPolicy`, and the resulting runtime factory inference error.
- The focused F03 and migrated notification-manager tests are present but cannot execute until that independent root
  compilation blocker is integrated. F03 does not restore the deleted report or add a compatibility shim to run them.
