# Capability Atlas

Status: Phase 0 not started

This file will become the reviewed inventory of capability facts, their owners, their consumers, and their enforcement coverage. It is deliberately a scaffold during the preparation milestone.

## Classification

Every capability or capability-like gate must be classified as one of:

- **Type-wide:** stable for a content type
- **Provider-backed:** proven by registration of an implementation provider
- **Source-dependent:** determined by the current source
- **Entry-dependent:** determined by the current entry or media
- **Selection-dependent:** determined by a collection and its constraints
- **External integration:** determined by a tracker, extension, platform, or other integration
- **Derived:** follows from other capabilities and shared feature policy
- **Presentation-only:** vocabulary or imagery with no authority over behavior
- **Compatibility boundary:** a legitimate type or format distinction that is not feature capability gating

## Inventory Schema

| Capability or gate | Scope | Current evidence | Behavioral owner | Consumers | Duplicated facts | Tests | Documentation | Intended state | Notes |
| ------------------ | ----- | ---------------- | ---------------- | --------- | ---------------- | ----- | ------------- | -------------- | ----- |

## Areas to Inventory

### Entry interaction registration

- Open and continue
- Downloads, bulk download, download options, and download settings
- Consumption and bookmarks
- Progress and playback preferences
- Merge and migration
- Update eligibility
- Child-list and child-group behavior
- Library filtering
- Preview and immersive browsing

### Application consumers

- Entry, library, updates, browse, and settings screens
- Selection and swipe actions
- Background jobs and lifecycle policy
- Tracking and migration flows
- Presentation models containing support flags
- Direct `EntryType` gates outside media and compatibility boundaries

### External and contextual inputs

- Source capabilities and supported entry types
- Tracker-supported entry types
- Local and stub source restrictions
- Platform-dependent capabilities
- Entry- and selection-dependent predicates

### Enforcement and documentation

- Registry and plugin tests
- Type-module tests
- Application behavior tests
- `checkEntryInteractionBoundaries`
- `docs/features/content-type-reference.md`
- Other feature documentation that makes capability claims

## Discrepancies

Record observed disagreements here during Phase 0. Do not fix them until expected behavior is reviewed.

| Capability | Conflicting statements | Expected behavior | Resolution phase | Status |
| ---------- | ---------------------- | ----------------- | ---------------- | ------ |

## Phase 0 Completion Summary

To be completed when the atlas exit gate is satisfied.
