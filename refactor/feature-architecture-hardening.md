# Feature Architecture Hardening

## Status and Scope

This document records the accepted follow-up work for the Feature architecture introduced by the current refactor.
The work is intentionally sequential:

1. make execution lifecycle contracts enforceable;
2. replace the manually curated production topology with compile-time discovery.

The concrete architecture remains Entry-shaped by design. It exists to express shared and type-specific behavior across
Entry types. Generalizing it into a project-wide modular feature system is not part of this work and should happen only
if a future use case requires it.

This plan also does not introduce runtime reflection, `ServiceLoader`, or classpath scanning. Production composition must
remain deterministic, shrinker-safe, and visible to build validation.

## Phase One: Enforce Execution Lifecycles

### Finding

`FeatureExecutionPointDefinition` currently assigns every point a `FeatureExecutionDelivery` value:

- `IMMEDIATE`
- `TRANSACTIONAL`
- `AFTER_COMMIT`
- `DURABLE`
- `BEST_EFFORT`

The runtime enforces only the durable/non-durable distinction. `DURABLE` has a separate preparation and delivery path,
while every other value enters through the same `FeatureExecutionRuntime.execute` function and runs immediately on the
calling coroutine.

Consequently, `TRANSACTIONAL` and `AFTER_COMMIT` describe a required caller behavior but do not provide it:

- a coordinator can invoke a transactional point outside the host transaction;
- a coordinator can invoke an after-commit point before persistence commits;
- the compiler cannot distinguish those mistakes from correct usage;
- the runtime cannot reject them because it does not know which lifecycle phase the caller is in.

Some coordinators currently establish the intended ordering correctly. Profile move, for example, gives the app host
callbacks which the host invokes inside its SQLDelight transaction, then invokes the moved consequence after the host
returns successfully. That correctness is carried by the coordinator/host protocol rather than by the execution API.

The word `TRANSACTIONAL` is also not yet one uniform contract. Profile move uses it for work inside one database
transaction. Backup restore labels the per-Entry restore point transactional, but calls it among several repository
operations without an execution-owned transaction boundary. Before encoding the label in types, each existing point
must be classified as one of:

- work that must share the core database transaction;
- work that belongs to a logical operation but is not database-atomic;
- synchronous inline work with no transaction guarantee.

`AFTER_COMMIT` also needs an explicit reliability qualifier. The current behavior is volatile: a successful commit
followed by process death can prevent the participant from running. Work requiring eventual delivery belongs on the
existing durable path.

`BEST_EFFORT` is not a lifecycle phase and currently has no production execution point. It overlaps failure and retry
policy rather than describing when work runs.

### Resolved Inventory

The lifecycle migration classifies the 22 production points by their actual guarantee:

- Inline (9): Media Session policy and consequences, Migration option discovery and transition preparation, Backup
  snapshot/restore/finalization, and Profile Move preparation/destination inspection.
- In transaction (4): Library removal preparation, destructive removal preparation, Profile Move work before core
  mutation, and Profile Move state transfer after core mutation.
- After commit, volatile (7): Library addition/removal, metadata changes, completed Profile Move, completed destructive
  removal, manual Source Refresh children, and Library Update children.
- Durable (2): Migration consequences and Merge consequences.

Backup restore is deliberately inline. Its current repository sequence is one logical restore operation but not one
shared database transaction. Giving it the stronger phase would make the transactional contract descriptive again.

### Target Contract

Execution points must carry a phase in their type rather than only as inspectable enum data. The exact Kotlin shape can
be selected during implementation, but it must provide equivalent guarantees to:

```kotlin
sealed interface FeatureExecutionPhase

data object Inline : FeatureExecutionPhase
data object InTransaction : FeatureExecutionPhase
data object AfterCommitVolatile : FeatureExecutionPhase
data object Durable : FeatureExecutionPhase

class FeatureExecutionPointDefinition<E : Any, P : FeatureExecutionPhase>
```

The runtime surface must then accept only the point type appropriate to the lifecycle boundary:

- inline points execute through an inline API;
- in-transaction points require a transaction scope created by the lifecycle orchestrator;
- volatile after-commit points can only be queued from that lifecycle and are released after successful commit;
- durable points retain their preparation, persistence, delivery, and discard protocol.

A type parameter alone is insufficient if any caller can manufacture the required scope. Scope construction must be
restricted to one lifecycle orchestrator. That orchestrator must own the ordering protocol:

1. enter the host transaction;
2. execute transactional participants and the core mutation;
3. collect volatile after-commit events;
4. release them only after the transaction reports a successful commit;
5. discard them on rollback, conflict, or exception.

The persistence host remains responsible for implementing the actual database transaction. The improvement is that this
responsibility is integrated once at the host/lifecycle adapter, while individual coordinators can no longer select an
arbitrary execution API at an arbitrary moment.

Names must state the real guarantee. In particular:

- use `AfterCommitVolatile`, not an unqualified `AfterCommit`;
- use `Durable` when eventual execution is required;
- treat `Inline` as “run synchronously here,” not as proof that persistence has not started;
- remove `BEST_EFFORT` from delivery; introduce it later as an explicit failure/retry policy only if a real use case
  requires it.

### Implementation Sequence

1. Inventory every execution point and call site. Record its required atomicity, ordering, failure behavior, and
   process-death expectation.
2. Resolve every ambiguous `TRANSACTIONAL` point before introducing the final phase types. Rename logically grouped but
   non-atomic points instead of weakening the meaning of `InTransaction`.
3. Introduce phase-typed point definitions and phase-specific runtime entry points in `feature-graph`.
4. Introduce the transaction/lifecycle orchestration API and integrate it with the app persistence hosts.
5. Migrate transactional and volatile after-commit coordinators as complete host protocols, starting with one
   representative workflow before applying the pattern to the remaining workflows.
6. Migrate inline and durable definitions, preserving durable envelope compatibility.
7. Remove the generic non-durable `execute` entry point, the delivery enum, and any temporary migration bridge.
8. Add an architecture check which prevents production coordinators from bypassing the phase-specific surfaces.

Temporary migration adapters are acceptable only while the phase is actively being migrated. They must be internal,
deprecated immediately, and removed before this phase is considered complete.

### Required Verification

Lifecycle contract tests must prove behavior through the real coordinator/host protocol:

- transactional participants run inside the same host transaction as the core mutation;
- transactional participant failure rolls back the core mutation;
- a host rollback, conflict, or exception prevents volatile after-commit delivery;
- a successful commit releases volatile after-commit participants only after the transaction returns successfully;
- ordering and failure policies continue to apply within each phase;
- cancellation is propagated rather than converted into a participant failure;
- durable envelopes survive the commit-to-delivery gap and remain compatible with existing durable work;
- a point cannot be passed to an API for a different phase at compile time.

The first implementation slice should include both a successful commit and rollback test. A happy-path-only migration
would preserve the main lifecycle risk.

### Completion Criteria

Phase one is complete only when:

- no execution point relies on a coordinator comment or call placement as its only lifecycle enforcement;
- every former `TRANSACTIONAL` point has a documented and enforced atomicity meaning;
- unqualified `AFTER_COMMIT` and delivery-level `BEST_EFFORT` no longer exist;
- production code has no generic non-durable execution escape hatch;
- lifecycle tests cover commit, rollback, ordering, cancellation, and the volatile/durable distinction.

## Phase Two: Generate Production Discovery

### Finding

The graph kernel discovers every contribution supplied to `discoverFeatureGraphContributions`, but it cannot discover a
contributor which production composition never supplies. Production currently maintains two explicit lists:

- `productionEntryTypeRuntimeModules`;
- `productionEntryFeatureRuntimeModules`.

Feature completeness is protected by `EntryFeatureRuntimeModuleBoundaryRules`, which scans Kotlin source text and
requires declarations matching a naming and formatting convention to appear exactly once in the production list. This
catches many omissions, but the build is recognizing source syntax with regular expressions rather than consuming a
compiler-visible registration model. A semantically valid declaration can escape the check if its name, declaration
shape, initializer shape, or topology formatting no longer matches those expressions.

The type-module list has no equivalent exact-once production installation check. The build can discover content-type
directories and validate their boundaries without proving that every valid type runtime module is installed.

The runtime result is deterministic, but the route to completeness still depends on a central topology edit plus
source-format-sensitive validation. This is weaker than the manifesto's requirement that participation enter through
its owning contribution and be discovered rather than curated.

### Target Contract

Feature and type owners must declare installable modules locally. A compile-time generator must produce the sole
production registry for the active variant from those declarations.

The generated registry must:

- include every valid owner-local feature runtime module exactly once;
- include every valid owner-local Entry type runtime module exactly once;
- reject duplicate module IDs and duplicate contributor ownership;
- preserve deterministic ordering;
- be variant-aware when a module is not present in every build;
- drive runtime installation and validation from the same generated result;
- fail the build for a declaration that cannot be represented or installed;
- require no central edit when a new conforming module is added.

KSP annotations and Gradle-owned metadata/code generation are both plausible mechanisms. The choice should be made
after a small proof establishes which approach can see all relevant source sets and variants without exposing internal
runtime modules or parsing Kotlin text. The requirement is compiler/build-model discovery, not one specific tool.

The generated Kotlin registry remains an explicit composition artifact. This change removes manual curation; it does
not make production discovery runtime-dynamic.

### Implementation Sequence

This phase starts only after lifecycle enforcement is complete.

1. Define the owner-local registration marker and stable module identity for both feature and Entry type modules.
2. Build a generator proof for the main production variant and assert deterministic output.
3. Generate and consume the feature runtime-module registry.
4. Generate and consume the Entry type runtime-module registry.
5. Make graph validation, execution binding installation, runtime boundaries, and warmups derive from those generated
   registries.
6. Add compile/build tests for unknown modules, duplicates, omissions, variant exclusion, and a newly introduced module
   requiring no central topology edit.
7. Remove the regex rules that infer declarations and list membership from Kotlin formatting.
8. Retain semantic boundary rules that are still useful, such as forbidding graph-only validation views in production
   runtime code and preventing behavior projection IDs from becoming dispatch keys.

### Implemented Design

Production registration is now owner-local metadata rather than a central Kotlin list:

- `*.entry-feature-module` declares a stable Feature module ID and a fully qualified module symbol;
- `*.entry-type-module` declares a stable Entry type ID and a fully qualified factory symbol;
- the `entry-interactions` Android variant discovers `src/main` plus its active variant source set;
- `GenerateEntryInteractionTopologyTask` validates and sorts the declarations, then generates direct Kotlin references
  for the sole production registry;
- Kotlin compilation proves every declared symbol exists and has the required module type;
- generated registration guards prove descriptor IDs match the resolved Feature and Entry type module IDs;
- production installation and the validation environment consume the same generated functions;
- runtime installation retains the semantic duplicate-contributor and duplicate-boundary ownership checks.

The previous build rule no longer parses `EntryFeatureRuntimeModule` declarations or a formatted
`productionEntryFeatureRuntimeModules` body. It retains only the independent semantic checks for validation-only graph
views and descriptive behavior IDs. The generated result remains deterministic and contains no runtime discovery.

### Completion Criteria

Phase two is complete only when:

- adding a conforming feature or Entry type module changes generated composition without editing a central list;
- malformed, duplicate, or unowned registrations fail during the build;
- production and validation consume the same generated topology;
- neither feature nor type completeness depends on declaration names or Kotlin source formatting;
- runtime composition remains deterministic and uses no reflection or service loading.

## Explicitly Deferred

The following is not a defect to fix in these phases:

> The graph kernel is generic, while the concrete architecture is predominantly an Entry architecture.

That shape is intentional. The current purpose is to make multi-type Entry features complete and consistent without
repeating support logic for Manga, Anime, Book, or future Entry types. Extraction into a broader project-wide feature
system should be evaluated only when another bounded context has a concrete need and can demonstrate which abstractions
are genuinely shared.
