# Feature Architecture Remediation Progress

## Resume Here

- Baseline branch: `features-arch-refactor`
- Baseline commit: `1d962d406` (`chore: planning cleanup`)
- Current phase: R3 — Library Membership Lifecycle
- Phase state: in progress
- Last updated: 2026-07-22
- Next action: define the Library Membership Feature boundary and inventory every current add/remove mutation and
  follow-up before moving consumers behind the coordinator.

## Approved Decisions

- All six high-confidence audit areas are in scope.
- Use one general execution-point/participant architecture.
- Use versioned Feature-state backup envelopes.
- Bulk Downloads operate on visible filtered children.
- Custom covers are contributed host consequences, not a standalone Feature.
- Stored child-state filters remain generic.

## Phase Ledger

| Phase | State | Milestone commit | Notes |
| --- | --- | --- | --- |
| R1 — Executable Participation Architecture | Complete | `76e4341ef` | Architecture first; no app workflow migration |
| R2 — Production Composition and Enforcement | Complete | `dd58b169e` | One module per production Feature |
| R3 — Library Membership Lifecycle | In progress | — | Includes current premature Tracking defect |
| R4 — Entry Lifecycle Operations | Pending | — | Metadata, removal, Profile move |
| R5 — Backup and Restore Participation | Pending | — | Includes tracker diagnostics defect |
| R6 — Catalogue Feature Completion | Pending | — | Removes raw provider dispatch |
| R7 — Download Policy and Context Ownership | Pending | — | Removes Manga preference leak |
| R8 — Enforcement, Secondary Audit, and Documentation | Pending | — | Final alignment and cleanup |

## Milestones

### Phase R1 milestone — 2026-07-22

- Outcome: Added typed execution points and independently owned execution participants to the Feature Graph, including
  capability/context applicability, specialized obligations, deterministic ordering, explicit delivery/failure policy,
  runtime binding coverage, execution results, and developer reporting.
- Notable changes: `EntryInteractionComposition` now constructs a `FeatureExecutionRuntime`. A declared participant with
  no implementation, an orphan implementation, duplicate binding, undeclared point, contradictory point, invalid
  ordering reference, or ordering cycle fails composition/assembly.
- Questionable actions or decisions: execution participants must declare behavioral contracts now, while automatic
  verifier/scenario selection for those contracts belongs to R2 production enforcement. Delivery classification is
  explicit metadata; each coordinator introduced by later phases must prove it emits the point at the declared
  transactional or post-commit boundary.
- Validation performed: `spotlessCheck`; complete Feature Graph, Feature Validation, and Entry Interactions unit-test
  tasks; FOSS app Kotlin compilation; production Entry Feature developer-report generation.
- Known failures or intentionally broken compilation: none.
- Manifesto comparison: unknown participants enter the same discovery pipeline without assembler knowledge; provider
  absence remains ordinary inapplicability; missing specialized work remains an obligation; executable runtime bindings
  cannot be omitted silently; no application workflow was migrated before the general mechanism existed. R1 is not the
  end state because production Feature-module bundling and automatic execution-contract validation remain R2 work.
- Documentation impact: this resumable plan, audit, and ledger were added. Enduring contributor documentation remains
  required in R8 after the production module API and migrations stabilize.
- Expected user action: review the notable/questionable points, then request a commit and continuation to R2 if
  approved. No additional design decision is currently required.

### Phase R2 milestone — 2026-07-22

- Outcome: Replaced the independent production contributor and runtime-factory paths with 36 cohesive Feature runtime
  modules. Each module owns its graph contributor, runtime registrations, executable bindings, exposed validation
  boundaries, and warmups; production composition derives all of those artifacts from the one installed module set.
- Notable changes: Production contract validation now enters through `addEntryInteractionRuntime`, resolves every
  module-declared runtime boundary, and then validates the resulting graph, contracts, projections, and report. The
  build boundary requires every conventionally declared Feature module and every Feature contributor to belong to the
  production installation exactly once, and rejects module declarations shaped so that this coverage could be
  bypassed. Execution-participant behavioral contracts now enter the same discovered planning, execution, obligation,
  and reporting path as integration contracts. Former shared consequences are now explicitly descriptive Behavior
  projections; their IDs cannot be used as runtime dispatch keys. Merge's existing durable queue uses a separate set of
  stable compatibility keys until R3 replaces its hard-coded handlers with discovered participants.
- Questionable actions or decisions: Production still has one explicit module topology because the application needs an
  installation boundary; it is not a second statement of Feature completeness, and the build task proves every declared
  module is represented exactly once. The graph-only topology projection remains public solely for Manga and Anime
  cross-module tests; the boundary task rejects its use from production code outside the topology. Merge's existing
  durable delivery implementation remains in place during R2, but it no longer masquerades as graph participation and
  its removal is an explicit R3 milestone. No additional design decision is required unless one of these constraints is
  unacceptable.
- Validation performed: focused build-logic module-boundary tests; `spotlessCheck`;
  `checkEntryInteractionBoundaries`; complete Feature Graph, Feature Validation, Entry Interactions, and interaction
  documentation unit-test tasks; Manga and Anime production Kotlin compilation; FOSS app Kotlin compilation; production
  Entry Feature developer-report generation. The report contains 3 content types, 36 Features, 366 evaluated
  integrations, and 0 obligations.
- Known failures or intentionally broken compilation: Manga and Anime unit-test source compilation still fails in
  unchanged plugin tests that call `childList.buildDisplayList`; that operation already belongs to `missingChildGap`, so
  this is pre-existing test drift unrelated to R2. Production code for both modules compiles successfully.
- Manifesto comparison: an unknown production Feature contributor must now belong to one installable module, a declared
  module omitted from production fails the build boundary, and graph/runtime/warmup participation is derived from that
  single installation. Provider absence remains valid and no module is required to expose an operation or runtime
  artifact merely because current Features do. Descriptive behavior is represented by `FeatureBehaviorProjection`,
  which has no delivery path; independently contributed executable work must use execution participants and is
  automatically subject to runtime-binding and behavioral-contract validation. R2 therefore removes completion-list
  duplication without turning the topology into a support matrix. Application workflows have intentionally not yet
  moved onto execution points; legacy executable paths are no longer represented by descriptive projection IDs and are
  scheduled for the sequential R3-R5 migration.
- Documentation impact: the resumable plan and ledger now describe the installed production-module boundary and its
  enforcement. Enduring contributor documentation remains scheduled for R8, after the workflow migrations stabilize
  the API and examples.
- Expected user action: review only the constraints listed under questionable actions. No answer or design decision
  is needed if they are acceptable; respond `commit and continue` to commit R2 and begin R3, or name the constraint that
  should change.

## Milestone Template

At every phase stop, replace this template with a dated entry:

### Phase RX milestone — YYYY-MM-DD

- Outcome:
- Notable changes:
- Questionable actions or decisions:
- Validation performed:
- Known failures or intentionally broken compilation:
- Manifesto comparison:
- Documentation impact:
- Expected user action:
