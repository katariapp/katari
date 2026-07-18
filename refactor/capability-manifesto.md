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

This requires an application-wide relationship model, not a collection of capability-specific fixes. Content-type
contributions, feature integrations, derived consequences, specialized obligations, behavioral contracts, and
projections must participate in one discoverable architecture. A solution that works only because a central list names
the capabilities or integrations considered so far has moved the memory problem; it has not solved it.

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

The same rule applies to applicability and integration ownership. They must not be reconstructed from the current Manga,
Anime, and Book matrix or repeated in tests.

### Architecture precedes migration

The general mechanism that connects content-type contributions to feature consequences must exist before capabilities
are migrated one at a time.

A vertical slice may demonstrate a problem or validate the general mechanism, but it must not become a collection of
special cases that is later mistaken for the architecture. Capability-by-capability migration cannot be allowed to invent
its own completion lists, policies, or test matrices while the common relationship model is deferred.

### Participation is discovered, not curated

Every registered content type, fundamental capability, feature integration, derived rule, specialized obligation,
contract, and projection must enter the relationship model through its owning contribution.

There must be no manually curated list of "capabilities covered by the architecture," "types that should be tested," or
"features that consume this capability." Adding a new contribution must change the evaluated graph automatically. If a
new capability, type, or feature can exist without the architecture noticing it, the architecture is incomplete.

### Partial support is valid

A content type is valid with any subset of interaction providers. No interaction is intrinsically required merely
because every current type happens to implement it. Open and Continue are subject to the same rule as Downloads,
Bookmarking, or any future interaction.

Provider presence proves support. Provider absence means that the type does not currently support that interaction; it
does not make the type invalid or incomplete and does not require a separate absence declaration. A new type may begin
with one provider and gain more over time.

If the product imposes a release requirement such as every shipped type supporting a particular interaction, that is a
separate explicit product policy. It is not part of architectural type validity.

### Features own their implications

The feature that understands a relationship between capabilities must own that relationship.

A content type should not need to know every future feature that could make use of bookmarking, consumption progress, playback, or downloads. When a new feature can operate from an existing capability, it should integrate all supporting content types through shared policy.

### Feature boundaries are enforceable

Application consumers must depend on feature-owned contracts, not on provider collections, raw type dispatch, or graph
evaluation details. Provider contracts and their operational dispatch exist so type modules and feature coordinators can
compose; they are not an alternative application API.

This dependency direction must be enforced by module visibility and build validation. Merely preferring a `Feature`
name over an `Interaction` name leaves the old path available and makes correctness depend on developer convention.
A provider may supply several different features, but only those features may expose product behavior to screens,
workers, notifications, settings, and other application consumers.

### Composition is preferred over opt-in multiplication

Support for a combination of features should be derived whenever possible. Adding another opt-in for every intersection recreates the same memory problem under a larger collection of flags.

The number of obligations should grow with genuine media differences, not with every possible pairing of otherwise compatible features.

### Missing work must be visible before release

Declaring support must cause every applicable behavioral expectation to become checkable. If satisfied prerequisites
make an integration applicable and required follow-on work is absent, development or validation should fail clearly and
identify the missing obligation.

Silently omitting behavior from an already applicable integration or relying on a future manual audit is not an
acceptable completeness mechanism. The absence of the prerequisite provider itself is ordinary unsupported behavior,
not an incomplete integration.

### Shared behavior is tested as a contract

When multiple content types claim the same capability, they inherit the same observable expectations. Those expectations should be exercised against every applicable type, including expectations created by combinations of capabilities.

Type-specific tests remain necessary for specialized media behavior, but they do not replace shared feature contracts.

Tests must not restate provider registration as assertions that a type "supports" a capability. Infrastructure tests
verify graph construction and failure semantics. Shared contracts verify feature behavior selected by the graph.
Type-specific tests verify genuine media behavior. A test matrix that repeats declarations is another source of truth and
must not be treated as completeness enforcement.

### Presentation follows behavior

The user interface must derive availability from actual capabilities and feature policy. Presentation metadata may choose wording and imagery, but it must not independently decide that a behavior is supported.

A hidden control must mean that the capability is genuinely unavailable, not that one presentation flag was forgotten.

### Documentation reflects executable truth

User-facing capability documentation remains valuable, but it should reflect the same authoritative support model used by the application. Documentation must not be the only place where parity is reviewed, nor should it require a contributor to duplicate support decisions manually.

The content type reference is a projection of product truth, not a substitute for enforcing it.

### Unsupported behavior follows provider absence

Not every capability belongs to every content type. A missing provider means the type does not currently support that
capability. This is a valid state whether the capability is meaningless, deferred, or simply not implemented yet.

The architecture does not require roadmap intent to be encoded as an absence declaration. It must instead distinguish
between a missing prerequisite, which makes a feature inapplicable, and missing follow-on work after all prerequisites
are present, which is an actionable obligation.

### Compilation follows architecture

During migration, a compiling application is not evidence that the architecture is correct. Compatibility shims,
fallbacks, and duplicated gates can preserve compilation while preserving the original problem.

The architectural model and dependency direction take priority. It is acceptable for intermediate work to expose compile
failures while old code is being made to conform. Those failures identify migration obligations; they must not be hidden
by weakening the architecture. Successful compilation is an outcome of completing the architectural migration, not the
constraint that shapes the architecture.

## Failure Modes This Work Must Reject

The following are not acceptable intermediate architectures:

- A central allowlist naming only the capabilities currently being migrated.
- A per-type matrix that repeats facts already proven by operational providers.
- Treating operations implemented by every current type as mandatory for all future types.
- Tests that assert capability labels instead of testing infrastructure or behavior.
- A feature-specific policy presented as the general capability mechanism.
- Migrating consumers before the architecture can discover consumers and obligations generally.
- Preserving compilation by keeping parallel authorities, silent fallbacks, or temporary flags with no removal path.
- Leaving raw provider dispatch callable from application code beside feature-owned contracts.
- Treating the successful completion of one vertical slice as proof that future features participate automatically.
- Deferring the relationship graph, obligation model, or contract-selection mechanism until after most features have
  already been migrated by hand.

At every milestone the decisive question is not "does this feature now work?" It is:

> If an unknown future content type, capability, or feature were contributed through its proper owner, would the
> architecture discover all applicable consequences and expose all missing obligations without another curated edit?

If the answer is no, the work is not aligned with this manifesto.

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
- The architecture discovers the feature and its relationships without adding it to a second completion list.

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
- Its owned integration declaration automatically enters graph evaluation, contracts, reporting, and projections.

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
- Missing required adaptations are detected after their feature prerequisites are satisfied, with an actionable
  explanation.
- Adding support to one content type does not require a manual search for every screen, setting, worker, policy, and menu that may depend on it.
- Adding a shared feature does not require type-specific changes where existing capabilities already provide enough information.
- Presentation cannot advertise behavior that is absent or hide behavior that is available because of a duplicated support flag.
- Missing providers yield ordinary unsupported behavior, while forgotten downstream integrations are distinguishable
  once their prerequisites are satisfied.
- The content type reference remains aligned without serving as the primary enforcement mechanism.
- Feature releases no longer depend on follow-up fixes for related behaviors that were discoverable from already declared capabilities.
- No central allowlist or test matrix determines which capabilities, content types, or feature integrations participate.
- A content type with only one interaction provider remains valid, and unsupported features remain unavailable without
  separate absence declarations.
- Removing every duplicated capability assertion from type tests does not weaken completeness enforcement.
- The architecture can be established before the application compiles, and compilation is restored by moving code into
  conformance rather than compromising the architecture.

## Non-Goals

This manifesto does not prescribe a registry format, class hierarchy, annotation system, code-generation strategy,
build task, or documentation generator. It does prescribe the dependency direction and sequencing constraint: the
discoverable relationship architecture must precede broad feature migration.

It does not require identical capabilities for Manga, Anime, and Book. It does not eliminate media-specific implementations where the media genuinely differs. It does not require every possible feature combination to exist.

It also does not remove the need for design judgment. The project must still decide whether behavior is fundamental,
derived, contextual, or specialized. The requirement is that once support exists, its consequences become systematic
and verifiable rather than dependent on memory.
