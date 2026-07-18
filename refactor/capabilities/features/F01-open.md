# F01 — Open

Status: complete; committed in `106fec52e`

## Owner and Relationship

- Feature owner: `entry-open`
- Prerequisite: `EntryOpenCapability`
- Shared consequence: guarded child dispatch and Android pending-intent creation
- Context: an actual Entry and child are operation inputs, not type-wide capability facts
- Specialized requirement: none; the contributed `EntryOpenProcessor` owns its concrete reader/player/book behavior
- Presentation projection: none; action availability is derived from the selected Open integration and vocabulary remains
  presentation-owned
- Behavioral contracts: shared gate behavior is covered by the Open feature test; concrete launch behavior remains in
  the media-specific Open processor tests. Graph-selected Android launch contracts remain Phase 7 work.

Provider absence makes Open inapplicable. It does not invalidate the content type and creates no missing-work obligation.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Entry child rows | Child click and preview-page callbacks are absent when Open is inapplicable. |
| Updates rows | Per-item Open applicability prevents the update row from starting an unsupported Open request. |
| Deep links to children | The feature gate opens compatible children; an inapplicable child link falls back to Entry details. |
| Browse preview sheet | Preview tiles are non-clickable when the displayed Entry type has no Open provider. Opening Entry details is ordinary navigation and is not F01. |
| Immersive cards | The Open-child control is absent unless both the immersive item is ready and Open applies. |
| Notification direct actions | Pending intents are created only for applicable Entry types; received legacy/ID payloads pass through the same gate before dispatch. |
| Notification action handler | Returns the feature result instead of claiming success after an unsupported dispatch. |
| Debug Anime launcher | Uses the same feature gate but remains a tooling-only surface rather than completeness evidence. |
| History | A stale unproduced `OpenChapter` event was removed. Actual resume behavior uses Continue and belongs to F02. |
| Continue processors | Retained as type-owned Continue behavior. They do not consume the F01 UI feature gate. |
| Entry-detail navigation | Cover, duplicate, related-entry, feed, catalogue, library, notification-view, and browse navigation open Entry details rather than a child and are not F01. |

No direct application consumer injects the low-level `EntryOpenInteraction`; it is now an implementation detail of the
feature-owned coordinator. Runtime composition also no longer exposes the aggregate `EntryInteractions` object through
dependency injection, so application code cannot recover the low-level Open dispatcher through that route.

## Automatic-Participation Proof

The focused feature test constructs a contribution containing only one Open provider. Graph evaluation selects the Open
integration and the shared coordinator dispatches through it without any current-type support assertion. The companion
absence case proves that an empty provider set remains valid and exposes neither dispatch nor pending intent.

## Remaining Boundaries

- Runtime composition is intentionally still unable to assemble the complete production graph because providers for
  F02–F27 do not yet have feature contributors. F01 does not install placeholder contributors for them.
- Full application compilation remains blocked by the previously recorded Download Lifecycle report migration, so F01
  application sources cannot be compiled as a standalone top-level slice yet. Lower API/SPI/type boundaries remain the
  validation gate until that owner migrates.
