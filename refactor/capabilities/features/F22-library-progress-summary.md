# F22 — Library Progress Summary

Status: complete

## Owner and Relationships

- Feature owner: `entry-library-progress`
- Fundamental prerequisite: `EntryLibraryProgressCapability`
- Shared consequences: unified Library load, explicit summary availability, merged aggregation, badges and progress
  indicators, progress sort/filter inputs, Stats coverage, and Library Update eligibility inputs
- Optional relationship: Library Progress plus Continue
- Optional consequence: a structured non-opening next-child target owned by F02
- Optional relationship: Library Progress plus Bookmarking
- Optional consequence: aggregate bookmark counts and bookmarked-filter applicability
- Context: stored children, legacy last-read time, merged-member order, and concrete media progress state
- Specialized requirement: the type provider exposes only genuine media progress evidence such as Manga page state,
  Anime playback state, or Book locator state
- Behavioral contract: synthetic provider presence and absence, shared counts, Continue and Bookmark composition, merged
  aggregation, and explicit unknown downstream inputs

Library Progress provider presence is the sole summary-support fact. Absence is valid and never invalidates the content
type or removes its structural Library item. It yields `Inapplicable`, not an empty summary whose zeros could be mistaken
for a supported entry with no progress.

## Feature Boundary

Application consumers receive `EntryLibraryProgressFeature`. `GetLibraryEntries` uses the same implementation through a
neutral Domain resolution port because `entry-interactions:api` already depends on Domain; composition alone binds the
Feature to that port. Product consumers do not receive the port, raw provider dispatch, or a provider collection.

The former `EntryLibraryProgressCalculator` system is removed. `EntryTypeRuntimeContribution` no longer requires a
calculator, root composition no longer creates a second per-type list, resolver dispatch no longer calls `getValue`, and
the build boundary no longer exempts public calculator factories. Providers bind through the ordinary type plugin and
generic provider index.

Providers return media evidence only. The Feature owns chapter totals, consumed counts, started state, last-read
combination, optional bookmark counts, merged aggregation, relationship availability, and structured results. This
prevents common policy from being copied into every type while preserving real media differences.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Unified Library load | Every favorite remains visible. A provider yields `Available(summary)`; absence yields `Inapplicable(type)` without fabricated counts. |
| Merged Library items | Storage membership still collapses structurally. The Feature aggregates available same-type summaries; unavailable members produce an explicitly unavailable merged summary rather than zeros. F12 retains merge ownership. |
| Continue | F02 exposes a structured non-opening target query. F22 derives cached Library targets only for Continue plus Library Progress; F02 still authorizes and executes the action. |
| Bookmark summary | Bookmark count derives from Library Progress plus F10 Bookmarking and stored child state. Adding Bookmarking to a summary-capable type activates the consequence without editing its provider. |
| Badges and progress indicator | Progress-derived visuals render only for an available summary. Download, local, language, and type badges remain independent. |
| F14 filters | Summary-derived inputs are nullable. Progress controls derive availability from F22; Bookmark filtering requires F22 plus F10. An active positive or negative predicate never classifies unknown state as false. |
| Sorting | Last-read, total, and unconsumed keys are nullable and unavailable values remain last in either direction. Progress sort controls hide when the current Library has no applicable summary type. |
| Stats | Structural title count remains available. Summary-derived completed, started, total, and consumed metrics use all-or-unavailable coverage and never present partial sums as universal. |
| F13 update eligibility | Unknown totals, unconsumed state, and started state do not trigger progress-dependent skips. Status and release-window policy remain independently applicable. |
| Entry merge target adapter | F12's temporary structural `LibraryItem` adapter uses explicit `Inapplicable` summary state instead of manufacturing zero progress. |

## Automatic-Participation Proof

The focused contract composes a partial type without Library Progress and a synthetic type with Library Progress alone,
then adds Continue and Bookmark independently. It proves provider absence is structured and valid, provider presence
activates every shared consequence, relationships are derived without another opt-in, and common merge/count policy is
Feature-owned. Type tests exercise genuine media evidence rather than restating production support labels.

## Manifesto Review

- The mandatory calculator field, second root list, `getValue` failure path, public factory exception, and public
  factories are removed.
- Provider presence is the only summary-support authority; provider absence leaves a valid, visible Library entry.
- Common counts, merge policy, badges, filters, sorts, Stats, and update inputs are Feature consequences rather than
  type opt-ins.
- Continue and Bookmark participation derive from their independent providers, so adding either capability activates
  the applicable summary behavior automatically.
- Unknown state is not converted to zero or false by UI, policy, Stats, or update consumers.
- No mandatory operation, type matrix, support-declaration test, no-op provider, fallback summary, compatibility facade,
  or central list was introduced.

## Validation

- Domain and root Entry-interactions behavior suites pass, including absence, shared summary, cross-feature, merged,
  optional filter, F13 unknown-input, and missing-last sort contracts.
- API/SPI, Manga, Anime, Book, root Entry-interactions, Feature Graph, and boundary build logic compile and validate.
- Full Manga/Anime/Book test compilation remains blocked by the pre-existing Phase 5 test-harness obligation: legacy
  `createEntryInteractions` calls omit required feature contributors. F22 adds no empty contributor fallback.
- FOSS compilation reaches only the separately owned F20/F21 and existing unrelated application errors; it reports no
  F22 production error.
