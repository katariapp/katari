# F13 — Update Eligibility

Status: complete

## Owner and Relationship

- Feature owner: `entry-update-eligibility`
- Prerequisite: unconditional (`CapabilityExpression.Always`) for every content type contributed to runtime composition
- Context-free consequences: participation in the shared policy, policy availability, smart-update setting
  interpretation, and selection of the fixture-free behavior contract
- Contextual consequences: the concrete policy decision consumed by Library Update and Stats
- Operation context: active-profile smart-update configuration, the concrete Entry, stored child counts, whether
  consumption has started, and the current fetch-window upper bound
- Specialized requirement: none. The previous Manga, Anime, and Book processors implemented the same policy and were
  removed during Phase 4.
- Presentation projection: none. F23 owns general type vocabulary and F24 owns library-update notification vocabulary.
- Behavioral contract: the fixture-free shared behavior contract is selected for every contributed content type. The
  focused synthetic test exercises the structured policy results against a provider-less type contribution.

Update eligibility is product policy over shared Entry state, not a fundamental content-type capability. There is no
Update Eligibility provider, opt-in, applicability query, type list, or unsupported result. A future type participates
as soon as its runtime module contributes that content type, even if it currently contributes no interaction providers.

## Access and Composition Boundary

Application consumers receive `EntryUpdateEligibilityFeature`. Its request contains only runtime facts: Entry identity
and state, child counts, consumption-started state, and an optional fetch-window boundary. The feature reads and
interprets the current smart-update preferences itself, so workers and statistics cannot translate preference keys
independently.

`DefaultEntryUpdateEligibilityFeature` verifies that the request Entry's content-type identity has the graph-selected
context-free F13 participation consequence. This makes `Always` operational: provider-less contributed types participate, while an Entry
type missing from runtime composition is a composition error rather than ordinary unsupported behavior.

The request decision is a separate contextual relationship. One-shot state, completed state, unconsumed state,
not-started state, release-window position, and current configuration enter as owned evidence. Only an eligible decision
receives the policy, Library Update, and Stats consequences; each skip reason has a matching contextual blocker.

The raw `EntryUpdateEligibilityInteraction`, its dispatcher, and its member on the operational `EntryInteractions`
aggregate are removed. F13 has no media provider or SPI operation to dispatch.

## Structured Policy

Evaluation returns `Eligible` or `Skipped(reason)`. The feature owns these skip reasons in their current precedence:

1. a source-defined one-shot Entry that has already fetched children;
2. a completed Entry when the non-completed restriction is enabled;
3. an Entry with unconsumed children when the caught-up restriction is enabled;
4. an unstarted Entry with existing children when the started restriction is enabled;
5. an Entry whose next update lies beyond the supplied fetch window when the release-period restriction is enabled.

Moving the one-shot rule into F13 removes a previous divergence: the worker skipped such Entries before calling the old
interaction, while Stats counted them as eligible. Both consumers now receive the same structured result from the same
feature.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Library update | Calls F13 once per candidate and records the returned structured skip reason. Eligible queued entries then use the separate provider-less Library Update Refresh Feature; F13 never performs source operations. Fetch-window bounds remain runtime context. |
| Stats | Calls the same feature after category filtering, so its global-update count matches worker eligibility, including one-shot Entries. |
| Smart-update settings | The existing generic settings surface writes the shared preference. F13 alone interprets those keys; settings do not gate the feature by content type. |
| Library outside-release-period filter | Remains F14. It shares a preference concept but has a distinct, capability-dependent library-filter applicability rule. |
| Skip-reason wording and notification routing | Remain presentation work for F23/F24. F13 supplies semantic reasons and does not choose type-specific text, channels, or icons. |

## Automatic-Participation Proof

The focused test composes a content type with no provider bindings and the F13 contribution. Graph evaluation selects
all four shared consequences and the behavior contract for that type, and policy evaluation succeeds. The same test
also verifies that evaluating an Entry type absent from runtime composition fails instead of fabricating an unsupported
capability result.

The behavior contract covers one-shot, completed, caught-up, started, release-window, disabled-restriction, and eligible
outcomes. It does not assert a current Manga/Anime/Book support matrix or duplicate any provider declaration.

## Manifesto Review

- A generic product rule is implemented once and requires no type-specific opt-in.
- Provider absence is valid because F13 has no provider; runtime content-type contribution is the composition boundary.
- Workers and Stats use one feature-owned contract and cannot call raw SPI or reinterpret smart-update preference keys.
- Structured contextual results distinguish policy decisions without creating type support labels.
- Smart-update settings remain context-free because configuration exists independently of any concrete Entry decision.
- A future contributed type receives the shared consequences and selected contract without editing F13 or application
  consumers.
- No capability matrix, mandatory operation, no-op provider, support-label test, compatibility facade, or direct-type
  branch was introduced.
- F14 filter applicability, F23 vocabulary, and F24 notification presentation remain with their owners.
- Source Refresh execution remains with the Library Update Refresh Feature. F13 supplies the eligibility consequence
  without becoming a worker, source coordinator, or automatic-download owner.

## Validation

- Formatting, focused compilation/tests, boundary validation, and diff validation are recorded with the combined
  `F09`, `F10`, and `F13` integration milestone.
