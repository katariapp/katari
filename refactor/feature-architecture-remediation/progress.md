# Feature Architecture Remediation Progress

## Resume Here

- Baseline branch: `features-arch-refactor`
- Baseline commit: `1d962d406` (`chore: planning cleanup`)
- Current phase: R1 — Executable Participation Architecture
- Phase state: milestone complete; awaiting commit and authorization to begin R2
- Last updated: 2026-07-22
- Next action: commit the R1 milestone after review, then begin R2 production module composition and enforcement.

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
| R1 — Executable Participation Architecture | Ready for review | — | Architecture first; no app workflow migration |
| R2 — Production Composition and Enforcement | Pending | — | — |
| R3 — Library Membership Lifecycle | Pending | — | Includes current premature Tracking defect |
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
