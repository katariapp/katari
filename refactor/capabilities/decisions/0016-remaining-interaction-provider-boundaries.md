# Remaining Interaction Provider Boundaries

Status: Accepted

## Context

The remaining interaction registry categories did not share one capability shape. Update Eligibility had three copies of
the same policy. Child List included optional progress-label behavior behind an empty default. Anime registered a
child-group filter whose complete implementation was false/empty/no-op. Library filtering used true/false methods on
three registered processors. Preview and Immersive contained real media implementations plus contextual checks.

Wrapping every category as one provider would preserve duplicated policy, false registrations, and hidden optional
behavior. The boundaries must follow actual ownership instead.

## Proposed Decision

- Update Eligibility is shared feature policy, not a type capability. Its identical type processors are removed and the
  transitional interaction evaluates the common rule for every type.
- Child List construction/ordering and child progress labels are independent providers. One implementation may bind
  both when it supplies both behaviors.
- Child-group filtering is contributed only by an operational provider. Provider presence replaces type-level
  `supports` and `shouldApplyFilter` booleans; contextual entry state may be added only when a real condition exists.
- Outside-release-period filtering is an independent compatibility marker for the shared library filter. Provider
  absence is the unsupported state.
- Preview and Immersive are provider-backed media implementations. Provider presence establishes type-level
  availability, while source support, preferences, media resolution, entry state, and runtime state remain contextual.
- Production type plugins use only capability bindings after this milestone. The remaining registry installers are
  shared migration scaffolding and are not called directly by type modules.

## Milestone 4.2.4 Application

- Manga, Anime, and Book bind Child List and Child Progress independently using their current shared implementation
  objects.
- Manga alone binds Child-group filtering; Anime's false/empty/no-op processor is removed and Book remains absent.
- Manga and Book bind Outside-release-period filtering; Anime's false processor is removed.
- Manga and Anime bind Preview and Immersive; Book binds neither.
- The three duplicated Update Eligibility processors and their registration path are removed. Existing common behavior
  is preserved in one transitional shared evaluator.
- Manga, Anime, and Book no longer override the plugin installation path to register remaining processor families.

## Consequences

- A future type gains the shared update-eligibility policy without another type implementation.
- Adding Child List no longer falsely claims progress-label support.
- Unsupported filtering is represented by provider absence instead of registered false/no-op objects.
- Feature integrations can discover Preview and Immersive providers without flattening contextual source or runtime
  conditions into type-wide support.
- Phase 5 still owns consumer applicability, shared consequences, and the final home of Update Eligibility policy.

## Alternatives Rejected

- One capability per old registry category regardless of defaults and no-op behavior
- Three type-owned copies of universal update policy
- Treating child progress labels as an intrinsic consequence of Child List
- Keeping Anime false/no-op filtering providers for compatibility
- Flattening contextual Preview or Immersive support into provider absence
- Retaining direct type-module registration beside provider bindings
