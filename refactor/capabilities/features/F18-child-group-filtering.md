# F18 — Child Group Filtering

Status: Complete

## Ownership

Child Group Filtering is a provider-backed feature. Provider presence is the only applicability fact; Manga currently
contributes the operational provider, while Anime and Book validly contribute nothing.

The type provider owns only genuine media interpretation: discovering and normalizing a child's group key. The shared
Feature owns application availability, multi-member state observation, filtering, exclusion persistence, and portable
snapshot/restore through the generic host data source. The application cannot call the raw provider dispatcher.

## App-facing contract

`EntryChildGroupFilterFeature` exposes structured state, observation, filtering, mutation, snapshot, and restore
results. Supported empty group sets remain distinct from provider absence, and unchanged persistence remains distinct
from an applied mutation. The internal dispatcher is strict and has no support boolean, apply boolean, empty result, or
no-op mutation fallback.

Merged entries are one feature scope. Available and excluded groups are unioned across every displayed member, changes
from every member are observed, and a replacement exclusion set is written to every member. A bypassed member uses a
single-member scope. Filtering occurs over the live child list before F17 orders and constructs display rows, preventing
an unfiltered observation from replacing the filtered initial list.

## Context disposition

F18 requires no contextual graph relationship. Member IDs, profile IDs, available and excluded groups, child lists,
and requested replacement values are operation payload or returned state. They change the data processed by an
applicable provider, but they do not decide whether the F18 relationship applies, activate another consequence, or
create a specialized obligation. Empty groups and identity filtering remain successful supported results.

This disposition is deliberate: moving those values into graph evidence would turn context resolution into a registry
of ordinary Feature method arguments. Provider presence therefore remains the complete applicability fact, while the
Feature request/result boundary owns live state and reevaluation.

## Consequence disposition

| Consequence | Disposition |
| --- | --- |
| Entry state, active indicator, filter control, and exclusion dialog | Selected by the F18 provider integration and consumed only through the Feature. |
| Available groups and excluded-group changes | Loaded and observed for every member through the generic host data source; type code supplies group discovery/normalization only. |
| Child visibility | Shared F18 filtering precedes F17 ordering/display. F17 does not acquire group-filter ownership. |
| Backup create/restore | Snapshot and additive restore use the same Feature; the former direct Manga authorization is removed. |
| Profile move/deletion, SQL schema, and backup wire fields | Retained as generic persistence/compatibility infrastructure, not capability authorities. |
| Reader, download candidates, next-child lookup, source sync, fetch interval, library counts, and Updates filtering | Declared shared storage consequences. They consume the same exclusion store automatically and do not add type opt-ins. |
| Scanlator/release-group wording and icons | Remain F23 presentation vocabulary and do not authorize filtering. |

## Contract coverage

- A synthetic provider automatically selects state, apply, persistence, and backup consequences.
- A plugin with no provider is valid and every operation returns structured inapplicability.
- Supported empty state is not confused with absence.
- Filtering and normalization are provider-selected while policy remains shared.
- Multi-member observation and replacement mutation use every distinct member ID.
- Snapshot honors the requested profile and restore merges portable exclusions without replacing existing state.
- Raw wrong-type dispatch fails instead of returning empty/no-op behavior.

## Manifesto check

F18 adds no mandatory operation, current-type matrix, support report/catalog, no-op provider, compatibility shim, or app
raw Interaction access. Adding a future provider activates every shared F18 consequence without an application gate or
another per-type opt-in. The Phase 6 context audit adds no generic context capability and does not duplicate live
request state in graph evidence.
