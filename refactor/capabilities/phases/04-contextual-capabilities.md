# Phase 4 — Contextual and External Capability Composition

## Objective

Represent and compose support that depends on a source, entry, selection, tracker, platform, or other external input without turning it into a static content-type declaration.

## Candidate Areas

- [ ] Source-dependent preview
- [ ] Source-dependent immersive browsing
- [ ] Related entries and latest feeds
- [ ] Local and stub restrictions
- [ ] Tracker-supported entry types
- [ ] Entry-specific download options
- [ ] Selection-specific merge and migration availability

Split this phase into coherent contextual groups in `status.md`.

## Per-Group Checklist

- [ ] Identify every authoritative contextual input.
- [ ] Preserve public source or tracker ownership.
- [ ] Compose contextual evidence with type capabilities in the owning feature.
- [ ] Return an explainable support result.
- [ ] Migrate duplicate screen-level composition.
- [ ] Test supported and blocked contexts.
- [ ] Preserve reactivity when sources, trackers, or entries change.

## Non-Goals

- Do not duplicate source or tracker capability declarations in the Entry capability model.
- Do not publish app-internal capability architecture through the extension SDK without a separate compatibility decision.
- Do not claim universal type support from a single supporting source.

## Exit Gate

- Contextual results identify their enabling and blocking evidence.
- Source-dependent support remains visibly conditional.
- Repeated source/type/selection checks are removed from migrated screens.
- Extension and tracker compatibility remains intact.

## Validation

- Capability composition tests
- Source metadata and compatibility tests
- Tracker capability tests where affected
- Relevant browse, entry, and selection application tests
- `./gradlew --quiet checkEntryInteractionBoundaries`
- `./gradlew --quiet :app:compileFossKotlin`
- `git diff --check`

## Manifesto Review

Confirm that one authoritative fact is composed across features without being copied into a static matrix.
