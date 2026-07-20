# Phase 6 — Contextual and External Integration

## Objective

Resolve source-, entry-, selection-, preference-, platform-, tracker-, and other external conditions through the same
discoverable relationship architecture without flattening them into unconditional type-wide support.

The complete contextual and external work register is `C01`–`C24` in
[`../migration-inventory.md`](../migration-inventory.md). Public source and tracker contracts remain owned by their
current systems. This phase changes how product features consume, explain, and react to those facts; it does not copy
them into content-type contributions.

## Preparation Findings

The static graph already retains typed `ContextInputDefinition` instances and deliberately evaluates integrations with
unresolved inputs as conditional. It does not yet have a production mechanism that supplies those inputs, records their
evidence, resolves feature-owned contextual rules, activates consequences, or exposes blockers and delayed obligations.

Existing feature migrations therefore use two different shapes:

- type prerequisites and relationships are selected by the graph; and
- source, entry, selection, preference, and runtime conditions are evaluated directly inside individual Feature
  coordinators without being declared to the graph.

Those coordinators are valid owners of their product policy, but the relationship model cannot currently discover their
context dependencies. Migrating individual casts or booleans before closing that architectural gap would preserve the
same developer-memory problem under Feature APIs.

The contextual register also contains several different ownership shapes. It must not become one global context object
or one source-capability facade:

- source and tracker interfaces are authoritative external facts;
- feature coordinators own the consequences and the meaning of blockers;
- concrete entry, selection, preference, and platform state is operation-scoped evidence;
- type-owned media implementations may consume source/media contracts internally when that is genuine implementation
  mechanics rather than application authorization; and
- compatibility adapters translate legacy/local contracts into current external facts but never declare product
  support independently.

## Architecture Gate — Runtime Context Resolution

No `C01`–`C24` consumer migration starts until the graph can carry a contextual relationship from discovery through
runtime resolution.

The gate must establish these semantics:

- A feature integration declares every contextual input it uses, with a stable identity, value type, and authoritative
  owner. An integration cannot use undeclared context to decide applicability, blockers, or obligations.
- Static evaluation still decides whether type-owned prerequisites are absent. A satisfied integration with context is
  a discovered candidate, not statically applicable and not unsupported.
- A feature-owned contextual rule consumes typed evidence for one operation subject. Generic graph code transports and
  validates evidence but does not learn source, tracker, entry, selection, preference, or platform semantics.
- Runtime resolution distinguishes missing evidence, a contextual blocker, an applicable relationship, and a genuinely
  incomplete specialized requirement after context establishes applicability.
- Enabling evidence and blockers retain their owning input and can be projected by the Feature without consumers
  reconstructing the decision.
- Shared coordinators are installed once. Candidate consequences are not interpreted as permission until the Feature
  resolves the current context.
- Context snapshots are immutable for one decision. Features that depend on changing preferences, source installation,
  authentication, selection, or runtime state expose an explicit reevaluation or reactive result rather than caching a
  stale answer.
- Application consumers receive only Feature-owned requests and structured results. They do not receive the context
  registry, graph evaluation, raw provider collections, or a generic capability-query API.
- Context-dependent contracts and projections retain enough resolved applicability information for Phase 7 to select
  them without introducing a production type/source/tracker matrix.

The architecture proof must add an anonymous future content type, an unknown context definition, and an unknown feature
integration through their owners. The unchanged assembly and resolution path must discover the candidate, report
missing evidence, resolve enabling and blocking evidence, and expose a delayed specialized obligation only after the
context makes it applicable.

## Ownership Rules for Migration

Each consumer is migrated according to the consequence it owns, not merely according to the kind of context it reads.
The same source fact may legitimately feed several Features, but its extraction and meaning must not be independently
reconstructed by their screens.

Context resolution is not a registry of every method argument. Ordinary execution payload such as an Entry ID, selected
child, URL, or persisted value remains in the purpose-specific Feature request when it does not decide whether a graph
relationship applies. Only evidence that changes applicability, blockers, consequences, projections, contracts, or
specialized obligations enters contextual graph resolution.

A direct source/tracker contract use may remain only when it is one of these reviewed boundaries:

- the SDK contract or its source/tracker implementation;
- a legacy/local compatibility adapter that exposes the current contract;
- a type-owned media provider performing genuine media mechanics behind a Feature; or
- a single external-system coordinator whose responsibility is the contract operation itself and which does not decide
  application feature availability.

UI, workers, notifications, settings, and application orchestration do not qualify. If they decide whether or how a
product behavior is available, they must consume the owning Feature result.

## Milestones

### 6.0 — Census and Ownership Split

- [x] Approve the `C01`–`C24` classifications and the owner-based sequence below.
- [x] Record every context that spans more than one Feature; do not assign it to a global context manager.
- [x] Record reviewed owner-local and compatibility uses so they cannot become an expanding allowlist.
- [x] Keep this milestone planning-only; no runtime behavior changes.

### 6.1 — Context Resolution Architecture

- [x] Implement the architecture gate above in `feature-graph` before migrating production consumers.
- [x] Define immutable operation subjects, typed evidence, feature-owned resolution, enabling/blocking results, delayed
  obligations, and candidate consequence semantics.
- [x] Extend discovery, consistency validation, evaluation, and synthetic unknown-contribution coverage without adding a
  product identifier or central context catalog.
- [x] Establish the Feature-facing dependency boundary before application migrations begin.

Completion notes:

- A contextual integration now declares typed inputs, one Feature-owned rule, and every possible blocker definition.
  Construction rejects a rule without inputs, inputs without a rule, foreign rule ownership, duplicate blockers, and
  blockers that cite undeclared evidence.
- Static evaluation keeps capability absence as ordinary inapplicability. Satisfied contextual integrations retain
  matched providers, supplied and pending specialized adapters, and candidate consequence edges without granting
  runtime applicability.
- Runtime resolution accepts a complete typed evidence snapshot for one evaluated content-type/Feature/integration
  subject. It rejects unexpected, contradictory, duplicate, and undeclared reads and returns distinct missing-evidence,
  blocked, incomplete, or applicable states.
- Missing evidence and blockers activate neither consequences nor obligations. An applicable result exposes delayed
  specialized obligations when required work is absent; only a complete applicable result activates consequence edges.
- Applicable and blocked results retain the authoritative evidence definitions and values. Blockers must be selected
  from the Feature's declared definitions, so reporting cannot receive an ad hoc runtime-only reason.
- Resolution is stateless and reevaluates each snapshot. Evaluation/result constructors are module-controlled, and the
  public resolver locates the exact discovered subject rather than accepting a caller-fabricated applicability object.
- The synthetic acceptance proof adds previously unknown types, a capability, context definition, Feature, blocker,
  adapter, and consequence through unchanged contributors. It proves missing, blocked, applicable, and delayed-obligation
  outcomes without a product registry edit.
- The existing app dependency check continues to reject direct `feature-graph` access. Entry Feature API does not export
  the context machinery, so later consumers must receive purpose-specific Feature results.
- Feature-graph, root Entry-interactions compilation/tests, formatting, build-logic tests, and boundary checks pass. FOSS
  compilation reaches only the already recorded unrelated `AnimePlayerLauncherScreen` callback and `MoreTab` coroutine
  errors; it reports no context-resolution or Entry-interactions failure.

### 6.2 — Catalogue and Source Description (`C01`, `C02`, `C04`, `C14`)

- [x] Compose catalogue presence, Latest, source orientation, and descriptive entry-type metadata once for Browse,
  search, feeds, migration source selection, extension/source presentation, repository projection, and filters.
- [x] Keep returned Entry type authoritative. Descriptive metadata may filter or label a source surface but may not
  authorize Entry behavior or reject returned data.
- [x] Remove repeated application casts and booleans while retaining source-manager and paging mechanics behind the
  source-owned boundary.

Completion state:

- One Catalogue Feature now owns the application projection of catalogue presence, Latest availability, language,
  descriptive entry types, and source-item orientation. Catalogue and Latest absence are contextual blockers; a source
  without either remains a valid source.
- Browse, global and migration search, feeds, migration source selection, extension/source presentation, content-type
  filters, repository projection, Library orientation, and Related Entries orientation consume that projection or its
  Domain assembly port.
- The Domain source projection represents catalogue presence structurally instead of carrying a free-standing Latest
  authorization boolean. Descriptive entry-type metadata remains presentation/filter input only; returned Entry types
  are not checked against it.
- Source-manager discovery, extension loading/stub persistence, compatibility conversion, and paging calls retain raw
  source contracts as owner-local mechanics. Immersive source opt-in and its metadata pruning remain assigned to 6.4.
- Boundary validation rejects new application imports of raw catalogue, metadata, and orientation contracts outside
  source composition owners, and rejects application use of the Domain assembly port instead of the Catalogue Feature.

### 6.3 — Source Actions and Resolution (`C09`, `C10`, `C11`, `C13`, applicable `C17`)

- [x] Give source settings, source-home navigation, Entry WebView actions, and deep-link resolution purpose-specific
  Feature boundaries and structured absence/failure results.
- [x] Reuse those results across catalogue, feed, migration, extension details, Entry, WebView, backup, and tracker
  consumers instead of repeating interface casts.
- [x] Preserve tracker-specific use of source configuration/home/image contracts as an explicit tracker adapter
  relationship, not an unchecked cast or a claim that every source supports it.

Completion state:

- Source settings, source-home navigation, Entry WebView actions, and deep-link resolution each have a distinct Feature
  boundary. Missing sources, unsupported contracts, absent URLs or matches, and operational failures remain distinct
  results; none makes a source or content type architecturally invalid.
- Their contributions enumerate the follow-on product consequences rather than hiding them behind one generic access
  flag: settings availability/screen/backup/tracker reuse, home navigation/search/cookie/tracker reuse, Entry WebView
  availability/URL/navigation/share/assist/runtime headers, and deep-link discovery/classification/persistence/child
  resolution.
- Catalogue, feed, migration, extension, preferences, backup, Entry, WebView, and deep-link consumers use those Feature
  results. Application/data/domain code can no longer cast the raw source action contracts directly.
- Kavita consumes source settings explicitly. Suwayomi consumes a tracker adapter that composes settings, home URL, and
  image-client relationships with separately owned evidence, blockers, and unavailable reasons, without making that
  combination a general source capability.
- Source-home presence no longer defines download-source participation. The Manga download cache discovers installed
  non-local sources and stubs directly, so adding or removing a home URL cannot silently opt a source into or out of
  download indexing.
- Compatibility adapters, source contract definitions, and the owning root Features retain raw source mechanics.
  Chapter WebView remains assigned to the media/renderer context work in 6.5.

### 6.4 — Existing Entry Feature Context (`C03`, `C05`, `C06`, `C20`, `C21`, `C22`, applicable `C17`/`C23`)

- [x] Declare and resolve the contextual inputs already owned by F01–F27, starting with the completed F19 Preview, F20
  Immersive, and F21 Related Entries boundaries.
- [x] Reconcile entry state, source access, selection shape, preferences/profile, and platform conditions separately in
  every Feature that gives them meaning. Do not create Entry-State, Selection, or Preferences support capabilities.
- [x] Preserve the completed F04, F11, F12, F13, F14, F18–F22, F24–F27 coordinator ownership while replacing their
  locally undiscoverable contextual dependencies with declared evidence and structured results.

#### 6.4.1 — Preview, Immersive, and Related Entries Context

- [x] Separate installed Preview/Immersive provider dispatch from live contextual applicability, so provider presence
  remains fundamental support while source and preference state decide only the affected product consequences.
- [x] Move Preview source requirements into provider-owned metadata interpreted by the Preview Feature; settings derive
  their explanatory projection from the same requirement instead of restating it.
- [x] Declare Immersive source presence, source opt-in, and descriptive source-surface compatibility separately. Source
  metadata may prune catalogue/feed surfaces but never rejects an authoritative returned Entry type.
- [x] Make Related Entries source presence/support contextual for every discovered type and remove unconditional
  consequences for source combinations that cannot provide related entries.
- [x] Remove the duplicate Domain `supportsImmersiveFeed` projection and reject new application/data/domain bypasses of
  Preview, Immersive opt-in, and Related Entries Feature ownership.

This slice intentionally leaves first-reading-child absence as an operation result: the child candidates are request
payload evaluated through the existing Child List Feature, not a type-wide support fact. Successful Preview/Immersive
loads authorize their subsequent page, renderer, progress, open-target, and lifecycle operations without turning live
handles into global context capabilities.

#### 6.4.2 — Download Action Source and Selection Context

- [x] Separate F04 provider dispatch from contextual product consequences for individual, bulk, bookmarked-bulk, and
  Library-update notification actions.
- [x] Declare source access and actionable selection state as independently owned inputs consumed only by the Download
  Action relationships that give them meaning.
- [x] Resolve local/stub, empty-child, and notification-size blockers through the graph while preserving the existing
  Feature-owned structured availability and operation results.
- [x] Keep an empty target list and an empty media-specific candidate pool as operation results: the former has no Entry
  type that can be a contextual graph subject, while the latter is known only after an applicable provider executes.

Core Download, Bulk Candidate, and Bookmark provider presence remain the only type-wide support facts. No Source,
Selection, Download, or Bookmark support matrix is introduced, and application consumers continue to depend only on
the F04 Feature API.

#### 6.4.3 — Automatic Download Entry and Preference Context

- [x] Separate the context-free F05 Download provider dispatch from the contextual Automatic Download policy, Library
  Update, and Entry-refresh consequences.
- [x] Declare new-child selection, active-profile enabled/unread-only configuration, library membership, category-policy
  eligibility, and filtered candidate presence as independently owned contextual evidence.
- [x] Replace the opaque `NoCandidates` outcome with structured empty-selection, disabled, non-library,
  category-policy, and no-unread-candidate blockers shared by both automatic-download paths.
- [x] Keep category lookup and prior-consumption filtering inside the single shared F05 policy without restoring a
  type-specific automatic-filter provider.

Core Download provider presence remains the only type-wide prerequisite. The active profile supplies current preference
values but is not itself a Download capability, and F07 retains ownership of the settings surface.

#### 6.4.4 — Consumption and Bookmark State-Mutation Context

- [x] Separate F09/F10 context-free type applicability and provider dispatch from state- and selection-dependent product
  consequences.
- [x] Declare Consumption transition eligibility, changed-child mutation results, and marked-consumed lifecycle emission
  as distinct contextual relationships.
- [x] Declare Bookmark selection eligibility and changed-child mutation as distinct contextual relationships while
  preserving the existing selection rules and unchanged-child filtering.
- [x] Keep empty cross-Entry selections as structured request results when no Entry type exists as a contextual subject.

Consumption and Bookmark provider presence remain the only type-wide support facts. No Entry-State or Selection
capability, type matrix, or per-type product-action opt-in is introduced.

#### 6.4.5 — Download Lifecycle Policy Context

- [x] Separate context-free event acceptance and provider dispatch from marked-consumed cleanup, completion cleanup,
  download-ahead, category eligibility, physical cleanup authorization, and Bookmark-protection policy.
- [x] Declare active-profile cleanup/download-ahead preferences, viewer-progress eligibility, per-owner category policy,
  and the remove-bookmarked override as Feature-owned contextual inputs.
- [x] Resolve owner-specific cleanup and Bookmark protection using each actual owner Entry type rather than the visible
  Entry type or a global Download assumption.
- [x] Keep owner resolution, reading-order membership, download continuity, deduplication, and concrete candidate sets as
  operation results known only during event execution.

Download and Bookmark provider presence remain the only type-wide facts. No Lifecycle, Viewer-State, Category,
Preferences, or runtime-readiness capability is introduced.

#### 6.4.6 — Update Eligibility Policy and Request Context

- [x] Keep F13 participation, policy availability, smart-update settings, and its behavior contract context-free for
  every contributed content type.
- [x] Move policy decisions, Library Update eligibility, and Stats eligibility onto a contextual relationship using the
  active-profile configuration and normalized request state.
- [x] Represent one-shot, completed, not-caught-up, not-started, and outside-release-period outcomes as matching graph
  blockers and existing structured skip reasons with the same precedence.
- [x] Preserve unknown progress evidence as eligible unless another known condition blocks the request.

F13 remains provider-free and universally participates through runtime content-type composition. No Update Eligibility,
Entry-State, Preferences, or release-window capability is introduced.

#### 6.4.7 — Library Filter State and Tracking Context

- [x] Keep generic filter participation, behavior-contract selection, and Progress/Bookmark/release-period control
  availability context-free.
- [x] Move policy interpretation, target matching, and active-filter state to one contextual relationship consuming
  filter configuration, aggregate Library state, and tracker evidence.
- [x] Treat item exclusion as a successful filter result rather than a blocked integration; the contextual relationship
  remains applicable for every non-empty request containing composed types.
- [x] Keep empty Library requests as operation results because no content type exists as a contextual graph subject.

No Library-State, Filter-Preferences, Tracking, or matching-result capability is introduced. Tracker authentication and
tracker-declared type applicability remain assigned to their Tracking owner in Phase 6.7.

#### 6.4.8 — Child Group Filtering Request-State Disposition

- [x] Audit F18 state, observation, filtering, persistence, backup, and shared-storage consequences for contextual
  applicability or cross-feature activation.
- [x] Keep member/profile IDs, group sets, child lists, and mutation values as purpose-specific Feature payload and
  returned state because they do not decide whether a graph relationship applies.
- [x] Preserve provider presence as the sole applicability fact; supported empty state, identity filtering, and
  unchanged persistence remain successful operation results rather than contextual blockers.
- [x] Keep live reevaluation inside the F18 observation contract instead of duplicating mutable state as graph evidence.

F18 requires no contextual integration. This is an explicit owner-based disposition, not an omission: registering
ordinary method arguments as context would violate the Phase 6 boundary and create a generic request-state registry.

#### 6.4.9 — Library Progress Request-State Disposition

- [x] Audit F22 loading, merged aggregation, Continue/Bookmark composition, badges, filters, sorting, Stats, and update
  inputs for contextual applicability or cross-feature activation.
- [x] Keep stored children, legacy activity time, media progress evidence, concrete Continue targets, and merged
  summaries as purpose-specific calculation inputs and results.
- [x] Preserve Library Progress provider presence as the sole base applicability fact and Continue/Bookmark provider
  composition as the sole cross-feature applicability facts.
- [x] Treat empty children, no next child, absent media progress, and unknown downstream values as supported operation
  results rather than contextual blockers.

F22 requires no contextual integration. Live Library data changes computed summary values but neither activates nor
blocks a discovered relationship, so duplicating it as graph evidence would not expose any missing integration.

#### 6.4.10 — Library Update Notification Child-Action Context

- [x] Keep shared notification participation, routing, rendering, and provider-derived Presentation/Open/Consumption/
  Download participation context-free.
- [x] Declare non-empty update children as F24-owned contextual evidence for child-open and Mark Consumed consequences.
- [x] Preserve an empty update as a valid Entry-details notification while blocking only child-specific consequences.
- [x] Continue consuming F04's notification availability for Download instead of duplicating its source-access,
  empty-selection, or notification-size policy in F24.
- [x] Keep same-type visible Entry resolution as a Merge invariant and concrete children/descriptions as operation data.

Open and Consumption provider presence remain independent type-wide facts. Selection state affects only F24's current
notification projection and does not become an Entry-Selection capability.

#### 6.4.11 — Migration Availability and Selection Context

- [x] Keep F11 provider dispatch, target search, configuration, execution, synchronization, and transfer relationships
  context-free once a Migration provider is present.
- [x] Declare persisted state and Library membership for Migration availability, Entry actions, Browse source
  projection, and source-Entry selection.
- [x] Declare single-profile selection independently for the Library-selection consequence while preserving mixed-type
  selections across independently participating providers.
- [x] Keep empty selection as a structured request result because no content type exists as a contextual graph subject.
- [x] Leave pair identity/type/profile validation, current transfer-option availability, and execution reference state
  to later F11 context slices.

Migration provider presence remains the only type-wide support fact. Persisted, Library, profile, and selection state
are operation evidence owned by F11 and do not become Migration or Entry-State capabilities.

#### 6.4.12 — Migration Pair Preparation and Option Context

- [x] Declare source/target persistence, source Library membership, same profile, same type, and distinct Entry identity
  for F11 target acceptance.
- [x] Declare authoritative pair presence and optimistic identity stability after host inspection while preserving
  operational failures as operation results.
- [x] Derive Child State option participation from Migration plus either Consumption or Bookmarking without another
  type opt-in.
- [x] Declare category, notes, custom-cover, and stored-Download state independently for their option consequences.
- [x] Keep Download provider presence context-free and require actual stored downloads only for the removal option.
- [x] Leave opaque-reference recognition and selected-option validation to a separate execution-context slice.

Pair and option blockers suppress only their owned preparation/option consequences. They do not redefine Migration
support, require another content-type contribution, or create generic Entry-State and Selection capabilities.

#### 6.4.13 — Migration Execution Authorization Context

- [x] Keep execution coordination installed context-free from Migration provider presence.
- [x] Declare authoritative pair presence and captured Library authorization before synchronization and after the
  synchronized execution reload.
- [x] Preserve missing/changed authorization as the existing execution `Conflict` while blocking only the current
  execution consequence.
- [x] Keep unrecognized references and invalid selected options as request-validation results; the former has no trusted
  content-type subject and the latter is checked against Feature-issued option state.
- [x] Keep replay, synchronization/inspection failures, transaction conflicts, cancellation, and consequence-delivery
  status as operation outcomes.
- [x] Preserve committed replay semantics: an already applied operation returns its recorded result without reapplying
  current authorization policy.

No Execution, Replay, Synchronization, Transaction, or Consequence-Delivery capability is introduced. Live
authorization is the only execution-stage fact that changes contextual applicability.

#### 6.4.14 — Merge Preparation Selection and Membership Context

- [x] Keep shared Merge coordination installed context-free for every composed content type.
- [x] Declare homogeneous selection type and profile before F12 accepts a non-empty preparation request.
- [x] Declare authoritative selected-Entry presence and stable type/profile identity after host resolution.
- [x] Declare single-group membership, complete ordered membership, and sufficient editor membership before producing an
  editor projection.
- [x] Preserve empty selection, duplicate selections/preparations, and missing preparation payloads as request results.
- [x] Leave opaque edit validation, commit choices, existing-group mutation, transaction conflict, and consequence
  delivery to later F12 execution-context slices.

The provider-free Merge relationship remains automatic for every composed type. Selection and membership facts block
only the current preparation/editor consequences; they do not create Merge, Selection, Membership, or Entry-State
capabilities and do not require a type-specific Merge contribution.

#### 6.4.15 — Merge Execution Membership and Consequence Context

- [x] Keep opaque edit-reference recognition, editor ordering/removal validation, missing-group idempotence, and atomic
  transaction conflicts as structured operation outcomes.
- [x] Declare complete ordered membership and homogeneous member type before an existing-group mutation is coordinated.
- [x] Move Library initialization and cover cleanup out of unconditional base consequences and resolve each from the
  trusted workflow request that actually requires it.
- [x] Keep Download ownership provider-derived and context-free while resolving Download removal independently from
  provider presence plus a concrete removal request.
- [x] Preserve consequence journaling, delivery retries, and aggregate status as installed coordination and operation
  outcomes rather than runtime support capabilities.

Atomic host revalidation remains the race-safe authority for optimistic edit and membership snapshots. Its conflict
result is not guessed by a pre-transaction context check. Per-operation effects are nevertheless graph-discoverable and
activate only when the trusted F12 workflow requests them.

#### 6.4.16 — Remaining Merge Coordinator Disposition

- [x] Derive F11 replacement cooperation from the real Migration provider instead of granting it unconditionally with
  base Merge coordination.
- [x] Keep candidate lookup, navigation, child ownership, Library grouping, metadata refresh, backup snapshot, profile
  move, lifecycle handling, and consequence status as shared F12 coordination for every composed type.
- [x] Keep absent membership, empty candidates/status, caller-supplied partial Library populations, missing backup
  identities, malformed portable groups, empty profile-move selections, and idempotent lifecycle events as operation
  inputs or structured results.
- [x] Keep backup/profile-move/Library-lifecycle transaction conflicts and delivery failures as operation outcomes from
  their authoritative host boundaries.
- [x] Preserve Download and Migration as independently provider-derived relationships without introducing another
  cross-feature opt-in.

This completes the F12 context audit. The shared projections describe Merge membership semantics even when a type has no
optional media provider; Download and Migration are the provider-dependent edges owned by F12 itself. Registering
membership contents, candidate lists, backup rows, or status counts as context would duplicate method payloads and
returned state without changing relationship applicability.

#### 6.4.17 — Existing Entry Feature Context Closure

- [x] Reconcile every F01–F27 contribution that did not require its own Phase 6.4 production slice.
- [x] Keep Open requests, Continue target absence, Download runtime state, maintenance events/results, child lists,
  portable snapshots, presentation vocabulary, Viewer Settings values, media-cache state, and preference ownership as
  provider-backed behavior or purpose-specific operation state.
- [x] Split Progress, Playback Preferences, and Viewer Settings migration consequences from their base provider
  relationships; each now requires the independently contributed Migration provider.
- [x] Preserve backup participation from each owning provider without making backup data presence or Migration a
  prerequisite for ordinary feature support.
- [x] Remove the focused Progress test assertion that restated consequence labels and verify derived migration behavior
  through actual provider combinations instead.

Phase 6.4 is complete. No Entry-State, Selection, Preferences, Runtime, Backup, or Migration meta-capability was added.
Data absence and request outcomes remain structured Feature results, while cross-feature behavior follows from the real
provider intersection without a content-type edit.

### 6.5 — Media and Renderer Context (`C07`, `C08`, `C12`, applicable `C20`, `C22`, `C23`)

- [ ] Compose image-page access, subtitles/playback selection, child WebView, local media formats, DRM/resolution,
  picture-in-picture, auto-scroll, and renderer support in the media Feature that owns each consequence.
- [ ] Keep Manga/Anime/Book loaders, downloaders, players, readers, and Book processor selection type-owned where the
  behavior is genuinely media-specific.
- [ ] Move cross-feature authorization such as cover network access, reader WebView actions, and download-option
  availability behind the appropriate Feature result; keep playback/platform controls owner-local only where their
  entire decision and UI remain inside one type-owned runtime.

#### 6.5.0 — Media Ownership Census and Architecture Split

- [x] Classify every production `EntryImageSource`, `SubtitleSource`, and `ChapterWebViewSource` consumer as an external
  contract, compatibility adapter, Feature-owned consequence, or genuine type-owned media mechanic.
- [x] Reconcile format, protection, renderer, playback-selection, picture-in-picture, auto-scroll, and local-media
  decisions without creating a global Media, Renderer, Platform, or Playback-State capability.
- [x] Verify the Book processor registry as one nested type-local authority shared by Book reader and downloader
  mechanics; keep optional Viewer Settings participation independently provider-derived.
- [x] Split the production migration by consequence owner before changing runtime behavior.
- [x] Correct the repeatable census probes to include nested modules; `*/src/main/**` omitted paths such as
  `entry-interactions/anime/src/main`, while `**/src/main/**` covers the full production tree.

The census found three ownership shapes, not one general media abstraction:

| Context | Production consumers | Disposition |
| --- | --- | --- |
| Image pages and image requests (`C07`) | Manga online reader, Manga downloader, F20 Manga Immersive, generic cover fetching, and the tracker-source adapter | Reader/downloader/Immersive calls are type-owned mechanics behind F01/F03/F04/F20. The tracker adapter is already an explicit contextual Feature relationship. Generic cover fetching is the remaining application authorization leak and receives a purpose-specific cover-network Feature result. |
| Playback media and external subtitles (`C08`) | Anime player resolution, F07 Download Options, and Anime downloader execution | F07 already owns option visibility and structured contextual absence. Player and downloader resolution are separate type-owned mechanics with different failure semantics; they remain behind their owning Open/Download paths rather than being forced through a shared subtitle helper or type-wide Playback flag. |
| Canonical child WebView (`C12`) | Manga reader toolbar actions and the legacy source adapter | The public contract and legacy translation remain external authorities. The existing WebView Feature gains child resolution and reader consequences; the reader consumes its structured result instead of casting the source or independently deciding availability. |
| Live entry/media/selection state (`C20`) | Downloaded/local/source media choice, selected stream/subtitle, current viewer/player state, and prepared Book sessions | These values remain request payloads or operation results. They do not become static graph capabilities merely because they influence one invocation. |
| Viewer/player/processor preferences (`C22`) | F07 stored download choices, F25 reader/player settings, Manga auto-scroll, Anime picture-in-picture, and Book processor preference | F07, F25, and F27 retain product and ownership consequences. Type-owned runtimes consume the resolved live values; preference state does not become support truth. |
| Platform, renderer, format, protection, and resolution (`C23`) | Anime platform/player controls, Manga viewers/local loaders, and Book processor selection | Renderer and platform predicates that are wholly inside a type-owned player/reader stay implementation mechanics. Cross-application surfaces continue to derive from F25. Book reader and downloader share one processor registry, so installing a processor changes both mechanics once; a processor is not required to contribute settings. |

This classification deliberately rejects an `EntryMediaCapability` or generic media-context facade. Source media is
heterogeneous operation data, and the product consequences already have different owners and failure semantics.
Provider absence still means only that the corresponding Entry Feature is unsupported; runtime media absence or an
unsupported format remains a structured result of an otherwise applicable operation.

The implementation proceeds in these bounded slices:

1. **6.5.1 — Cover network context:** add a purpose-specific Feature result for source image client/headers, migrate
   both generic cover fetcher factories, and preserve the already separate tracker adapter consequence.
2. **6.5.2 — Child WebView context:** extend the WebView Feature with canonical child resolution, source identity,
   headers, and reader action consequences; migrate the Manga reader and harden the raw-contract boundary.
3. **6.5.3 — Type-owned media closure:** verify F03/F07/F20 ownership of image/subtitle resolution, record player and
   renderer operation failures as owner-local results, harden application boundaries, and retain the single Book
   processor registry for reader/download mechanics.
4. **6.5.4 — Media context reconciliation:** rerun the media/context census, update Feature documents and projections,
   and close every `C07`, `C08`, `C12`, `C20`, `C22`, and `C23` row assigned to this milestone.

#### 6.5.1 — Cover Network Context

- [x] Add one purpose-specific Cover Network Feature that resolves the source image call factory and headers together.
- [x] Declare installed-source and image-source evidence, missing/unsupported blockers, and separate call-factory/header
  consequences without making image pages a content-type capability.
- [x] Migrate both generic Entry cover fetcher factories from `SourceManager` and `EntryImageSource` to one lazy,
  structured Feature resolution.
- [x] Preserve the generic-client fallback for missing or unsupported sources and preserve operational failure when a
  supported source cannot expose its client or headers.
- [x] Reject future application/data/domain use of raw `EntryImageSource` while retaining source contracts,
  compatibility adapters, root Feature composition, and type-owned media mechanics.

The Feature resolves source-specific network access only when a remote cover request actually reaches the network.
Cached, custom, file, and content-URI covers do not resolve source context. An `Available` result supplies client and
headers from one snapshot; `Missing` and `Unsupported` retain the existing generic-client behavior; `Failed` retains the
source error rather than silently changing network identity.

The tracker-source adapter remains a separate explicit relationship because it composes settings, home URL, and image
client access for a different external integration. Manga reader, downloader, and Immersive processors retain direct
`EntryImageSource` mechanics inside their type-owned modules. This slice therefore removes the application leak without
turning a consequence-specific result into a reusable raw-source facade.

#### 6.5.2 — Child WebView Context

- [x] Add a separate child-source contextual integration to the existing WebView Feature; ordinary `WebViewSource`
  support does not imply `ChapterWebViewSource` support.
- [x] Resolve canonical child URL and authoritative source identity through one structured result with distinct
  missing, unsupported, and failed outcomes.
- [x] Require a type-owned media-host adapter only after source context makes child WebView behavior applicable; Manga
  contributes the current reader adapter, while unrelated types and unsupported sources remain valid.
- [x] Migrate Manga reader WebView, browser, share, and Android Assist availability from raw source casts and separate
  getters to the same active-child resolution.
- [x] Clear and replace the active resolution at each child transition and reject an asynchronously completed result
  for a child that is no longer current.
- [x] Reject raw `ChapterWebViewSource` use outside source/local compatibility, the owning root Feature, and tests,
  including inside type modules.

The child integration and the existing Entry WebView integration remain separate relationships under one Feature.
`ChapterWebViewSource` extends the public source contract, but a source implementing only Entry WebView remains valid and
receives no child consequences. Runtime WebView headers continue through the existing header result; resolving a child
URL does not eagerly require headers needed only after WebView launch.

Because child controls live inside a media-specific reader/player rather than a shared application screen, the child
integration has one specialized host requirement. Source support alone discovers the shared URL/action consequences;
if the affected content type has not supplied its host adapter, contextual resolution reports the missing adapter rather
than claiming those UI consequences are complete. Provider absence or a source without child WebView support remains an
ordinary blocker and creates no adapter obligation.

The Manga reader now hides all canonical-child actions until an `Available` result exists for the current child. A
failed or unavailable transition clears the prior result, so a previous chapter URL cannot authorize actions for the
new chapter. Browser, share, WebView navigation, and Android Assist all read the same URL snapshot, while the WebView
launch reads its source identity from that snapshot instead of looking up the provider again.

#### 6.5.3 — Type-owned Media Closure

- [x] Keep Manga image-page resolution inside its reader, downloader, Preview, and Immersive implementations; F20
  normalizes load and renderer failures without exposing `EntryImageSource` to generic application consumers.
- [x] Keep Anime stream/subtitle resolution separate in Download Options, downloader execution, and player execution;
  each retains its own request data and failure semantics instead of sharing a type-wide playback/media facade.
- [x] Return Immersive renderer construction as an explicit available/failed Feature result and render the failure on
  the existing retryable Immersive error surface.
- [x] Reject future application/data/domain access to raw `SubtitleSource`, while preserving source contracts, tests,
  and the owning Anime runtime mechanics.
- [x] Verify Book reader and downloader processor selection use the same injected `BookProcessorRegistry`; processor
  format support and optional Viewer Settings participation remain independent.

This closure adds no media or renderer capability declaration. A Manga provider that cannot obtain image pages, an
Anime player that cannot resolve a stream, and a Book processor that cannot open a format remain operation outcomes in
their owning paths. The shared Features expose only consequences they own: F07 resolves download-option availability,
F20 resolves load/render lifecycle, and F03 executes each type's download mechanics.

Anime intentionally resolves external subtitles separately for options, download execution, and playback. Those calls
have different failure policy: options may omit unavailable choices, a selected download subtitle must fail explicitly,
and playback may continue without external subtitles. Sharing their raw resolution would erase those product
differences. Generic application code is nevertheless prevented from creating a fourth authority.

Immersive renderer construction now mirrors Immersive loading at the Feature boundary. A type-owned renderer may still
validate its own handle shape and fail locally, but application UI receives `Available` or `Failed` and does not depend
on an exception escaping composition. This is operation failure handling, not a support declaration or specialized
adapter obligation.

The Book registry remains a nested media authority installed once by the Book runtime and injected into both reader and
downloader paths. Adding a processor therefore affects both mechanics through Book ownership, while contributing Viewer
Settings remains an independent optional provider decision.

### 6.6 — Refresh and Network Policy (`C15`, `C16`, applicable `C17`, `C20`, `C22`)

- [ ] Retain `SyncEntryWithSource` as the single owner of source refresh mechanics and its source capability contracts.
- [ ] Compose refresh safety, incremental behavior, chapter-number recognition, one-shot state, local/stub access,
  update preferences, and unmetered-source policy into their F11/F13/Library-update consequences.
- [ ] Keep downloader use of unmetered-source behavior inside the Download provider while removing duplicated
  application warning policy.

### 6.7 — Tracking Integration (`C18`, `C19`, applicable `C20`, `C22`)

- [ ] Add one Tracking Feature boundary that composes actual Entry type, tracker-declared applicability,
  authentication/profile state, existing tracks, reading dates, privacy, status, and scoring.
- [ ] Migrate Entry actions/dialogs, search/register/update guards, automatic add/sync, Library tracker filters, Stats,
  and tracking documentation to the same structured results.
- [ ] Keep tracker implementations authoritative and do not turn tracker capabilities into content-type providers.

### 6.8 — Compatibility Reconciliation and Context Census

- [ ] Verify `C24` remains confined to the legacy Manga adapter and bundled Local source and that both expose current
  source contracts rather than a parallel support authority.
- [ ] Reconcile every multi-owner row (`C07`, `C17`, `C20`–`C23`) against every assigned Feature consequence.
- [ ] Re-run the inventory probes and classify every new direct source/tracker/type/context gate before Phase 7.
- [ ] Stop with no unclassified `C01`–`C24` consumer and no broad exception that future code can silently join.

Each numbered milestone is a review-and-commit stop. A later milestone does not begin until the previous milestone is
accepted.

## Exit Gate

- Contextual results identify enabling evidence and blockers through their authoritative owners.
- No source-, tracker-, entry-, selection-, preference-, profile-, local/stub-, platform-, or runtime-specific fact is
  presented as unconditional type support.
- Every product consequence is available only through its Feature; reviewed owner-local mechanics do not decide UI or
  workflow availability.
- External compatibility contracts remain authoritative and returned Entry types remain authoritative.
- Reactive consumers do not retain stale contextual answers.
- Every `C01`–`C24` row and every discovered consumer has a final Feature, owner-local, or compatibility disposition.
- An unknown contextual integration participates without an edit to a context catalog, type matrix, consumer list, or
  exception list.

## Manifesto Review

This phase is aligned only if a future type, source contract, tracker fact, or runtime context enters through its proper
owner; the relevant Feature then discovers it, explains its blockers, activates every common consequence, and exposes
specialized work without a developer remembering another screen, worker, setting, or integration list.
