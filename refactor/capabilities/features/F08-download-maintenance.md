# F08 — Download Maintenance

Status: complete

## Owner and Relationship

- Feature owner: `entry-download-maintenance`
- Prerequisite: `EntryDownloadCapability`
- Shared consequences: aggregate cache invalidation, aggregate source-folder rename, Entry-title folder rename,
  whole-Entry download inspection, and whole-Entry download removal
- Operation context: the concrete old/new source identities, Entry identity and profile, new title, restored library state,
  and the source-purge selection are runtime inputs; none are content-type capability declarations
- Specialized requirement: none beyond the existing media-specific `EntryDownloadProcessor`. Each processor owns its
  storage layout and implements the physical cache, rename, inspection, and removal operations.
- Presentation projection: none. Prompts and settings labels consume maintenance results but never decide support.
- Behavioral contracts: a synthetic Download provider proves that one contribution activates every maintenance
  consequence. Provider absence remains valid and distinguishes aggregate `NoParticipants`, per-Entry `Inapplicable`,
  and applicable storage containing `NoDownloads`.

Core Download is the only support fact. F08 does not introduce a maintenance opt-in, a current-type list, a no-op
provider, or a mandatory capability.

## Access and Dispatch Boundary

Application consumers receive `EntryDownloadMaintenanceFeature`. Aggregate cache invalidation and source rename are
coordinated once across every graph-selected Download provider. Entry-title rename, inspection, and removal select the
provider for the concrete Entry type.

The feature accepts only concrete operation inputs. It does not carry speculative source/profile flags or reason enums
that no provider consumes. `Entry.profileId` preserves profile identity for per-Entry operations, `UnifiedSource`
preserves installed/stub source identity for rename, and aggregate invalidation deliberately visits all participating
provider caches.

The raw `EntryDownloadInteraction` maintenance methods remain root-internal dispatch used by the feature coordinator.
Application code cannot call them as an alternative maintenance API.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Source registration | Stub display-name changes call the aggregate source-rename consequence, allowing every participating media storage layout to rename its source directory. |
| Entry metadata update | The shared title-change hook calls the graph-gated per-Entry rename consequence. |
| Backup restore | Restoring library entries invalidates all participating download caches once so restored storage becomes visible. |
| Advanced settings | Manual cache invalidation calls the same aggregate consequence; the setting remains generic and does not enumerate media types. |
| Migration dialog | Remove-download preflight uses structured maintenance inspection. An unsupported type is distinct from a supported Entry with no downloads. |
| Migration execution | The selected remove-download flag dispatches whole-Entry removal through F08 before migration completes. |
| Entry removal | Ordinary library-removal prompts and confirmed delete-all operations use structured inspection and whole-Entry maintenance. |
| Merge maintenance | Entry-screen merge-member library removal and merged delete-all paths call the same per-Entry feature for every concrete member. |
| Catalogue merge maintenance | Catalogue-side merge-member removal uses the feature rather than raw provider dispatch. |
| Source/database purge | The clear-database flow now loads the exact non-library Entries selected by the existing source/read-state predicate, removes their persisted downloads while their Entry/profile records still exist, and then deletes the database rows. This closes a previously unwired maintenance consequence. |

## Ownership Boundaries

- F03 retains queue state, status, counts, worker execution, and controls for already queued work.
- F04 retains user-initiated child download/delete/retry/cancel actions and bulk candidate selection.
- F05 retains automatic discovery and scheduling.
- F06 retains consumption/completion cleanup policy and per-child cleanup dispatch.
- F07 retains options and setting-capability visibility.
- F08 owns only storage maintenance caused by identity, restore, removal, and explicit cache-maintenance events.

Whole-Entry removal in F08 is not F06 lifecycle cleanup: F08 responds to an Entry/database maintenance event and removes
all persisted downloads. F06 evaluates consumption policy and selects child-level physical deletion versus cleanup.

## Automatic-Participation Proof

The focused feature test composes an anonymous type with only `EntryDownloadCapability`. Graph evaluation selects cache,
source, title, and removal consequences without an application or type-list edit, and each operation reaches that
provider. The absence case proves that a type without Download is valid, cannot be mistaken for `NoDownloads`, and does
not force aggregate maintenance to fabricate a participant.

## Manifesto Review

- Provider presence is the sole support declaration and provider absence is valid.
- Aggregate behavior is implemented once; only physical media storage behavior remains in type modules.
- Application consumers use one feature-owned boundary and cannot reconstruct provider applicability.
- Runtime Entry/source/profile/cache facts remain operation context rather than duplicated capabilities.
- The source purge is now an explicit consequence instead of an integration a future contributor must remember after
  database deletion has already lost the information needed for cleanup.
- No type matrix, support-label test, mandatory operation, artificial opt-in, compatibility facade, or direct-type
  branch was introduced.
- F06 lifecycle and F07 settings/options ownership remain independent.

## Validation

- Formatting and diff validation pass.
- Feature Graph tests and API, SPI, data, and domain-test compilation pass with repository JDK 21 and Android SDK.
- Root Entry-interactions and FOSS application compilation reach only the three pre-existing F06 lifecycle failures:
  removed `EntryCapabilityReport`, removed `EntryDownloadCapabilityPolicy`, and the resulting runtime inference error.
- The focused F08 synthetic behavior and migrated migration-use-case tests are present but cannot execute until that
  independent root compilation blocker is integrated.
- The remaining raw `EntryDownloadInteraction` application references are F07 option/setting paths only; F08 production
  consumers contain no raw maintenance dispatch.
