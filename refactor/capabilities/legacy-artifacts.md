# Legacy Artifact Disposition

This inventory prevents committed prototype code from being mistaken for the target architecture. Git history remains
unchanged; the resulting source is retired or migrated at the phase named below.

## Retain as Operational Behavior

These changes reduce duplicated behavior or preserve genuine media implementations. They are not architectural support
authorities:

- Distinct bookmark-provider registration and `EntryBookmarkInteraction` dispatch
- Provider-owned Manga, Anime, and Book interaction implementations
- Shared bulk-download candidate selection over provider-supplied candidate pools
- Shared bookmark-aware cleanup behavior and download lifecycle events
- Removal of constant download-support methods and presentation-owned behavioral flags
- Behavioral tests for mutation, selection, cleanup, queues, storage, and media-specific mechanics

Their wiring may change, but the behavior is migration input rather than code to revert indiscriminately.

## Retire at the Phase 3.5 Boundary Cut

These artifacts encode the evidence/report prototype and must stop acting as production architecture before real types
and features migrate:

- `EntryCapabilityCatalog` as a central list of known capabilities
- `EntryCapabilityReport`, type reports, and `supportsTypeWide` as the application support authority
- Report assembly from `EntryCapabilityEvidenceSnapshot`
- `EntryInteractions.capabilityReport` and its production DI exposure
- Explicit `IntentionallyUnsupported` declarations used to compensate for missing providers
- `Unresolved` as the result of ordinary provider absence
- Report tests and fixtures whose purpose is to enumerate current type/capability outcomes

The cut removes these definitions and production exposure rather than leaving deprecated but callable facades. It is
allowed to break every unported consumer. Those failures are recorded as migration obligations for Phases 4–7. The old
authority must not remain beside the new graph merely to keep compilation green.

## Rehome Through Feature Migration

The behavior represented by these artifacts remains useful, but their current report-driven form is not the target:

- `EntryDownloadCapabilityPolicy` rules become download-feature contributions and evaluated relationships.
- UI, worker, notification, settings, lifecycle, and selection calls to `supportsTypeWide` become consumers of evaluated
  feature applicability.
- Synthetic Bookmarking/Downloads proofs become graph-selected behavioral contracts rather than a template for a
  feature-specific kernel.
- Documentation and developer reporting become projections from feature contributions rather than consumers of a
  central catalog.

## Reuse Requires Re-Justification

Capability identity, provider evidence, result types, and validation helpers may be reused only if they satisfy the new
generic contribution and discovery model. Existing names or passing tests are not evidence that they belong in the final
architecture. No Phase 3 design may depend on preserving their API shape.

## History Policy

Do not rewrite or wholesale-revert the Phase 1, Phase 2, or download commits. They record useful learning and contain
valid operational improvements. The current manifesto, plan, accepted decisions, this disposition, and resulting source
define the target—not an intermediate commit message.
