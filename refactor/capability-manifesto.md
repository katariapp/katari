# Feature Capability Manifesto

## Context

Katari supports multiple content types and a growing set of features. Some concepts are universal, some are meaningful only for particular media, and some become meaningful only when two otherwise independent capabilities meet.

This creates a large, implicit relationship graph. Adding support for one capability can affect screens, actions, settings, background behavior, filtering, notifications, downloads, cleanup, migration, documentation, and tests. Today, knowledge of those relationships is distributed across the codebase and depends too heavily on developer memory.

The content type reference makes the current user-facing result more visible, but a manually maintained document cannot guarantee that the implementation is complete. It can describe known support after the fact; it cannot reliably reveal a forgotten integration before release.

As Katari grows, completeness must stop depending on any contributor remembering every place where a capability might matter.

## The Problem

Feature support is not a collection of isolated switches.

A content type may declare or implement a capability in one part of the application while related behavior remains disabled, unsupported, or silently absent elsewhere. Presentation may carry its own support flag. Another feature may independently opt into the same concept. Tests may cover only the original type. Documentation may contain a fourth representation of the same fact.

This produces several recurring failure modes:

- A capability works in its primary screen but is missing from related workflows.
- A shared feature requires another type-specific branch even though the behavior is not truly media-specific.
- User interface availability disagrees with the underlying implementation.
- Adding a content type or capability requires an informal audit of unrelated modules.
- Missing integrations are discovered through user reports rather than during development.
- Documentation drifts because it is another manually maintained statement of support.

The problem is not insufficient diligence. The problem is an architecture that treats a large dependency graph as something a person should remember.

## The Core Idea

Capability support must be declared as an authoritative fact, and the consequences of that fact must be owned by the features that understand them.

A content type should describe what it fundamentally supports. Features should derive every common integration that follows from those capabilities. When a derived behavior genuinely requires type-specific work, that obligation must become explicit and verifiable as soon as support is declared.

The desired relationship is:

> Declare a capability once. Receive every common consequence automatically. Surface every exceptional obligation immediately.

Developer memory must not be the mechanism that connects a capability to its actions, settings, policies, presentation, documentation, or tests.

## Fundamental and Derived Capabilities

Katari must distinguish between capabilities a content type genuinely implements and behaviors that emerge when capabilities are combined.

A fundamental capability describes an intrinsic fact about the content type or its media. Bookmarking, downloading, playback, image pages, publication resources, merging, and tracking are examples of concepts that may require real type-specific support.

A derived capability is a product behavior that follows from existing facts. Downloading bookmarked items, protecting bookmarked downloads during cleanup, or showing bookmark actions should follow from the presence of bookmarking and the relevant surrounding feature. They should not become separate facts that every content type must remember to opt into.

If a derived behavior can be expressed through shared models and policy, it belongs to the shared feature. Reimplementing it per content type creates artificial variability and multiplies the number of places that can be forgotten.

If a derived behavior cannot be shared because the media truly requires specialized work, that difference is legitimate. It must still be declared as an explicit requirement rather than hidden behind a type check, an unrelated presentation flag, or a silent unsupported result.

## Guiding Principles

### Declare truth once

The application should have one authoritative answer to whether a content type supports a capability. Screens, feature policies, settings, tests, and documentation must not maintain independent versions of that answer.

Labels, icons, and media terminology may vary without redefining behavioral support.

### Features own their implications

The feature that understands a relationship between capabilities must own that relationship.

A content type should not need to know every future feature that could make use of bookmarking, consumption progress, playback, or downloads. When a new feature can operate from an existing capability, it should integrate all supporting content types through shared policy.

### Composition is preferred over opt-in multiplication

Support for a combination of features should be derived whenever possible. Adding another opt-in for every intersection recreates the same memory problem under a larger collection of flags.

The number of obligations should grow with genuine media differences, not with every possible pairing of otherwise compatible features.

### Missing work must be visible before release

Declaring support must cause every applicable behavioral expectation to become checkable. If a required integration is absent, development or validation should fail clearly and identify the missing obligation.

Silently omitting an action, returning unsupported from one path, or relying on a future manual audit is not an acceptable completeness mechanism.

### Shared behavior is tested as a contract

When multiple content types claim the same capability, they inherit the same observable expectations. Those expectations should be exercised against every applicable type, including expectations created by combinations of capabilities.

Type-specific tests remain necessary for specialized media behavior, but they do not replace shared feature contracts.

### Presentation follows behavior

The user interface must derive availability from actual capabilities and feature policy. Presentation metadata may choose wording and imagery, but it must not independently decide that a behavior is supported.

A hidden control must mean that the capability is genuinely unavailable, not that one presentation flag was forgotten.

### Documentation reflects executable truth

User-facing capability documentation remains valuable, but it should reflect the same authoritative support model used by the application. Documentation must not be the only place where parity is reviewed, nor should it require a contributor to duplicate support decisions manually.

The content type reference is a projection of product truth, not a substitute for enforcing it.

### Unsupported behavior is explicit

Not every capability belongs to every content type. Absence is valid when the concept is meaningless, impossible, or intentionally out of scope.

Such absence should be deliberate and explainable. It must be distinguishable from an integration that was simply never considered.

## What Feature Completeness Means

A feature is not complete merely because its primary workflow works for the content type being used during development.

It is complete when:

- Its fundamental capability requirements are known.
- Every currently supporting content type receives the shared behavior.
- Every applicable interaction with other capabilities is derived or explicitly accounted for.
- Required media-specific adaptations are visible obligations.
- User interface availability agrees with behavior.
- Settings and background policies apply to every relevant type.
- Shared contracts exercise all applicable content types and capability combinations.
- User-facing support documentation agrees with the executable result.

Completeness is therefore a property the project can evaluate, not a conclusion a contributor reaches from memory.

## End Goal

The end state is a project in which adding a feature or expanding a content type does not require remembering a hidden checklist of related integrations.

When a content type gains a capability:

- Shared actions and policies that follow from it become available automatically.
- Existing features discover the new capability without type-specific presentation changes.
- Applicable cross-feature behavior is included automatically.
- Shared tests begin exercising the new type and capability combinations.
- Any genuinely required specialized work is reported before the feature can be considered complete.
- User-facing capability documentation remains aligned with the actual product.

When a new feature is introduced:

- It states which fundamental capabilities it consumes.
- It applies shared behavior to every compatible content type.
- It makes exceptional media requirements explicit.
- It does not require editing every content type merely to opt into behavior that can already be derived.

From a contributor's perspective, the project should answer:

- Which content types are affected by this feature?
- Which existing capabilities compose with it?
- Which behavior is supplied automatically?
- Which specialized obligations remain?
- Which contracts prove that the integration is complete?

Those answers must come from the architecture and validation process, not from institutional memory.

## Success Criteria

This work is successful when:

- A fundamental capability is declared once rather than repeated across behavior, presentation, and documentation.
- Common cross-feature integrations are derived from capabilities instead of separately opted into per content type.
- Declaring support automatically subjects the content type to every relevant shared contract.
- Missing required adaptations are detected during development or validation with an actionable explanation.
- Adding support to one content type does not require a manual search for every screen, setting, worker, policy, and menu that may depend on it.
- Adding a shared feature does not require type-specific changes where existing capabilities already provide enough information.
- Presentation cannot advertise behavior that is absent or hide behavior that is available because of a duplicated support flag.
- Unsupported combinations are deliberate and distinguishable from forgotten integrations.
- The content type reference remains aligned without serving as the primary enforcement mechanism.
- Feature releases no longer depend on follow-up fixes for related behaviors that were discoverable from already declared capabilities.

## Non-Goals

This manifesto does not prescribe a registry format, class hierarchy, annotation system, code-generation strategy, build task, documentation generator, or migration sequence.

It does not require identical capabilities for Manga, Anime, and Book. It does not eliminate media-specific implementations where the media genuinely differs. It does not require every possible feature combination to exist.

It also does not remove the need for design judgment. The project must still decide whether a behavior is fundamental, derived, intentionally unsupported, or specialized. The requirement is that once this decision is made, its consequences become systematic and verifiable rather than dependent on memory.
