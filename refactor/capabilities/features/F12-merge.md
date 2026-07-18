# F12 — Merge

Status: planned; no production implementation is active on `features-arch-refactor`

## Architectural Classification

Merge is a shared product workflow, not a fundamental content-type capability. The transitional Manga and Anime Merge
providers contain no media-specific operation, while Book already participates in merge-aware behavior without one.
F12 therefore applies to every composed content type. Entry type, profile, selection shape, and existing membership are
request context, not type support declarations.

Optional relationships remain independently provider-backed. Merge may derive Open, Continue, Download, Consumption,
Bookmarking, Child List, Preview, Immersive, Library Progress, notification, or other consequences only when the owning
feature is applicable. Absence of one of those providers omits only that consequence and never makes the content type or
base Merge workflow invalid.

## Non-Negotiable Boundary

- Application consumers submit user intent and consume feature-owned results or projections.
- Application consumers do not receive a general-purpose membership repository, raw group CRUD, persistence models, or
  a checklist from which they can reconstruct Merge behavior.
- Host ports carry only the operations required to execute an owned workflow. They are separated from the application
  feature contract and unavailable to ordinary screens, workers, and other features.
- Profile identity is explicit throughout a workflow. Resolving the active profile once must not permit later effects
  to drift to another profile.
- Cross-feature consumers receive purpose-specific projections, not raw membership access.
- Files are split by ownership: application contract, workflow models, projections, host ports, coordination, and
  persistence adaptation do not accumulate in one implementation file.

## Migration Control Surface

The pre-implementation census found the following existing ownership paths. This is a temporary migration ledger, not a
runtime participation list. F12.7 reruns the census so a newly discovered path cannot be silently omitted.

| Area | Existing paths that require disposition |
| --- | --- |
| Type composition | Manga/Anime Merge markers, capability bindings, raw dispatch, composition maps, and Book's current marker absence. |
| Membership authority | `MergedEntryRepository`, `GetMergedEntry`, `UpdateMergedEntry`, `EntryMerge`, SQL queries, data implementation, and dependency injection. |
| Merge editors | Entry, Library, Catalogue, and Feed selection/edit dialogs, ordering, removal, target selection, and duplicate discovery. |
| Visible-entry navigation | Entry, History, Updates, notification actions, and any pending-intent destination derived from a member. |
| Child ownership and ordering | `GetEntryWithChapters`, Manga reader ordering, merged child rows, and shared child-owner consumers in Open, Continue, Preview, and Immersive. |
| Downloads | Shared notification navigation, Manga/Anime ownership paths, Book download cache/index membership, actions, and lifecycle cleanup. |
| Library state | Unified Library loading, merged collapse, progress aggregation, removal, favorite/category state, and metadata refresh. |
| Backup and restore | Backup group projection, restore ordering/identity, and profile-scoped membership persistence. |
| Migration cooperation | F11 source migration member replacement and transfer cleanup through a narrow F12 operation; F11 itself remains separate. |
| Profile lifecycle | Profile move snapshot/restore and profile deletion without ambient-profile access or caller-rebuilt groups. |
| Enforcement and proof | Boundary checks, repository/feature behavior tests, every affected Feature contract, merged-entry documentation, and the content-type reference. |

## Sequential Milestones

Each milestone is implemented by one agent, compared with the manifesto, committed separately, and stopped for review.
An unforeseen boundary, transaction, or product-semantics decision stops the milestone before implementation expands.

### F12.1 — Contract and ownership boundary

- Define the user intents, owned results, and purpose-specific projection vocabulary.
- Declare profile, selection, and membership as contextual inputs to the F12 relationship rather than recomputing
  applicability independently in consumers.
- Define a narrow, segregated host boundary without exposing general membership CRUD to application consumers.
- Make explicit which facts belong to the caller and which decisions belong to F12.
- Strengthen boundary validation so a raw Merge service cannot be introduced beside the Feature contract.

Exit gate: the intended dependency direction is enforceable even if existing consumers no longer compile.

Review request: approve the public intents/projections, host ownership, and prohibited API surface before persistence or
consumer migration begins.

### F12.2 — Profile, transaction, and failure semantics

- Define explicit-profile behavior for ordinary workflows, profile moves, backup, notifications, and background work.
- Define feature-issued snapshots for operations that cross profile or transaction boundaries.
- Decide which database changes are atomic and how non-database effects are ordered, compensated, or reported.
- Reject ambient-profile lookup after a workflow has selected its profile.

Exit gate: profile switching and every failure point have a documented authoritative outcome before implementation.

Review request: approve profile identity, transaction boundaries, and compensation semantics; these are product and
architecture decisions.

### F12.3 — Shared Merge coordinator and persistence conformance

- Remove the empty Merge provider, capability binding, and raw compatibility dispatch.
- Install one provider-free F12 contribution for every composed type.
- Implement selection, target, membership transition, replacement, and workflow execution behind the approved boundary.
- Keep context rules reactive without converting them into type-wide support facts.

Exit gate: base Merge behavior is feature-owned, profile-safe, and inaccessible through a parallel raw authority.

Review request: verify the implemented workflow against the approved F12.1/F12.2 contracts; no new decisions are
expected unless implementation exposes a contradiction.

### F12.4 — Entry ownership and navigation projections

- Migrate visible-entry, concrete child-owner, member ordering, and selection consumers.
- Reconcile Entry and Library actions, duplicate/merge dialogs, Open, Continue, Child List, Preview, Immersive, and
  Library Progress without exposing raw groups.

Exit gate: these consumers obtain only the F12 projection needed for their own feature consequence.

Review request: report any observable behavior difference; no support-matrix or type-specific opt-in decision is
expected.

### F12.5 — Download, lifecycle, and notification projections

- Migrate queue identity, actions, lifecycle cleanup, and notification navigation using explicit profile and real-owner
  context.
- Preserve every independent Download applicability rule; Merge does not authorize Download behavior.

Exit gate: merged downloads retain profile and real media ownership without Download code reconstructing membership.

Review request: verify identity and user-visible navigation semantics, especially during profile switching.

### F12.6 — Library state, metadata, backup, and profile lifecycle

- Migrate Library removal, metadata refresh, backup representation, restore, profile move, and deletion consequences.
- Use feature-issued projections or snapshots for every cross-boundary operation.
- Do not fold F11 Migration into this milestone; any F11 cooperation remains an explicit narrow boundary until F11.

Exit gate: persistence and lifecycle consumers cannot supply or rebuild F12's membership checklist.

Review request: verify backup/profile behavior and decide any newly discovered compatibility requirement before it is
implemented.

### F12.7 — Integrated enforcement and completion review

- Run the F12 behavior contracts and every affected feature's focused tests.
- Rerun the raw-boundary census and add generic enforcement for every rejected access path.
- Reconcile the complete F12 inventory row and document every consequence.
- Compare the result with every manifesto rejection rule before marking F12 complete.

Exit gate: no raw Merge authority, ambient-profile dependency, caller-owned consequence list, or known follow-up remains.
F12 may still leave the application blocked by the independently unfinished F11 boundary.

Review request: approve completion against the manifesto. This review is not merely a compilation or test-results check.

## Salvage Reference

The rejected combined implementation and its unreviewed follow-up are preserved only for selective reference on
`salvage/f12-merge-audit` at `8bdc5c607`. That branch is not an implementation baseline and contributes no completion
status to F12.

## Manifesto Review

The plan declares Merge once as a shared workflow, derives optional cross-feature consequences from their real
providers, makes profile and transaction obligations explicit before implementation, prevents consumers from rebuilding
membership behavior, and uses no content-type matrix or mandatory provider.
