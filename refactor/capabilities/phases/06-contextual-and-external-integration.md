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

- [x] Compose image-page access, subtitles/playback selection, child WebView, local media formats, DRM/resolution,
  picture-in-picture, auto-scroll, and renderer support in the media Feature that owns each consequence.
- [x] Keep Manga/Anime/Book loaders, downloaders, players, readers, and Book processor selection type-owned where the
  behavior is genuinely media-specific.
- [x] Move cross-feature authorization such as cover network access, reader WebView actions, and download-option
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

#### 6.5.4 — Media Context Reconciliation

- [x] Rerun nested production probes for source media contracts, returned media shapes, playback selection, renderer
  construction, platform/viewer predicates, and Book processor selection.
- [x] Verify every `C07`, `C08`, `C12`, `C20`, `C22`, and `C23` media consumer has a Feature, type-owned mechanic,
  external/compatibility contract, or operation-data disposition.
- [x] Extend raw source-action enforcement to generic presentation modules; no current bypass was found, but future UI
  code can no longer create another image/subtitle/source authority.
- [x] Update F03, F07, F20, and F25 ownership documents plus the capability atlas, migration inventory, and phase
  continuity ledger.

The repeated census found no unclassified runtime consumer. `EntryImageSource` is confined to source compatibility,
purpose-specific root Features, and Manga media mechanics. `SubtitleSource` is confined to Anime option/downloader/player
mechanics. `ChapterWebViewSource` is confined to source compatibility and the WebView Feature. All returned media-shape
casts occur within Manga, Anime, or Book modules or source compatibility.

Live selection, downloaded/local state, platform support, renderer shape, settings values, and processor choice remain
invocation evidence. None became a static graph capability. Generic UI obtains cover, WebView, Download Options,
Immersive, and Viewer Settings consequences from their Features; media-specific runtimes retain only mechanics and
operation failure policy.

The reconciliation exposed one enforcement omission: `presentation-core` and `presentation-widget` were absent from the
raw source-action guard even though they are generic application layers. They contained no current violation. Adding
them closes a future bypass without expanding any Feature, declaring support, or restricting type-owned modules.

### 6.6 — Refresh and Network Policy (`C15`, `C16`, applicable `C17`, `C20`, `C22`)

- [x] Retain `SyncEntryWithSource` as the single owner of source refresh mechanics and its source capability contracts.
- [x] Compose refresh safety, incremental behavior, chapter-number recognition, one-shot state, local/stub access,
  update preferences, and unmetered-source policy into their F11/F13/Library-update consequences.
- [x] Keep downloader use of unmetered-source behavior inside the Download provider while removing duplicated
  application warning policy.

#### 6.6.0 — Refresh Ownership Census and Architecture Split

- [x] Classify every production `SyncEntryWithSource` and `UnmeteredSource` consumer by consequence owner.
- [x] Keep `EmptyChapterListSource`, `IncrementalChapterSource`, and `ChapterNumberRecognitionSource` interpretation
  inside `SyncEntryWithSource`; they are source refresh mechanics, not content-type capabilities.
- [x] Define one application-facing Source Refresh Feature before migrating callers, with structured source/context and
  operation results while retaining `SyncEntryWithSource` as its internal mechanics coordinator.
- [x] Assign each cross-feature refresh relationship to the consuming Feature contribution rather than maintaining a
  Source Refresh consumer list.
- [x] Assign Library-update queue-warning policy to F24 and retain Manga downloader metering checks as type-owned F03
  execution mechanics.

The census found one shared operation with several product owners, not several refresh implementations:

| Consumer | Current use | Final owner |
| --- | --- | --- |
| Entry screen | manual/automatic details and child refresh, including merged source owners | Source Refresh base consequence; F12 continues selecting concrete merge owners |
| Library update | child refresh after F13 eligibility and optional metadata refresh | F13/Library-update relationship consumes Source Refresh; F05 receives only inserted children afterward |
| Metadata update job | details-only refresh | Source Refresh base metadata consequence |
| Migration search/use and migration host | destination details/children refresh, including explicit-profile persistence | F11 declares and consumes its Source Refresh relationship |
| Immersive retry/empty-child load | children-only refresh before F20 media loading | F20 declares and consumes its Source Refresh relationship |
| Deep-link child resolution | persist resolved Entry and refresh children before matching the canonical child | Deep-link Feature declares and consumes its Source Refresh relationship |
| Library-update queue warning | exclude sources declaring unmetered access before applying per-source threshold | F24 notification consequence |
| Manga download queue | exclude unmetered sources from downloader-owned queue warning thresholds | Manga F03 mechanics; no shared application policy or Feature exposure |

`SyncEntryWithSource` remains the only implementation that fetches details/children, interprets incremental and empty-list
contracts, recognizes child numbers, updates persistence, rekeys progress, runs metadata hooks, and updates fetch
intervals. The new Feature is an application and graph boundary around that operation; it is not a second synchronizer
or a raw source facade.

Source Refresh has no content-type provider and no mandatory operation. Every contributed type can request refresh from
its concrete `UnifiedSource`; installed-source presence determines runtime applicability, while retained stub metadata
is non-executable and appears as absence through the authoritative lookup. A bundled Local source remains an ordinary
installed source and is not rejected by a generic local flag. Source absence is contextual, while empty child lists,
network failures, and persistence failures are structured operation outcomes.

The Feature owns refresh-specific interpretation of profile-scoped title-update policy. Callers still supply the
product decision to fetch details, fetch children, treat the request as manual, or use a fetch window. F13 alone owns
smart-update eligibility preferences and one-shot policy; F05 alone owns automatic-download selection after successful
refresh. Live Entry/favorite state, existing children, fetch windows, and preference values remain request evidence,
not support facts.

Cross-feature participation is discovered at the consuming contribution. F11, F13/Library Update, F20, and Deep Link
each declare the refresh consequence they use; Source Refresh does not enumerate those Features. Adding a future
Feature that needs refresh therefore requires declaring that Feature's relationship once, not editing Source Refresh or
every content type.

Implementation proceeds in architecture-first slices:

1. **6.6.1 — Source Refresh architecture:** add the Feature contract, structured results, graph/context contribution,
   and root implementation over `SyncEntryWithSource` before migrating application callers.
2. **6.6.2 — Direct and source-owned consumers:** migrate Entry/manual refresh, metadata refresh, Immersive, and Deep
   Link; declare F20 and Deep Link relationships and establish the raw-sync application boundary.
3. **6.6.3 — Migration refresh relationship:** migrate migration search/use and the strict explicit-profile host path
   through the F11-owned relationship without weakening transaction/profile invariants.
4. **6.6.4 — Library-update refresh relationship:** migrate child/metadata update workers through Source Refresh after
   F13 eligibility, preserving F05 inserted-child flow and batch behavior.
5. **6.6.5 — Metered-source notification policy:** move Library queue-warning resolution behind F24, remove its raw
   `SourceManager`/`UnmeteredSource` decision, and guard generic consumers while retaining Manga downloader mechanics.
6. **6.6.6 — Refresh/network reconciliation:** rerun the nested census, update Feature documents/projections, and close
   every `C15`, `C16`, and applicable `C17`, `C20`, and `C22` disposition.

#### 6.6.1 — Source Refresh Architecture

- [x] Add one application-facing `EntrySourceRefreshFeature` contract with an explicit request, fetch window, and
  structured refreshed/source-unavailable/failed results.
- [x] Contribute Source Refresh through an unconditional graph integration selected for every contributed content type;
  installed-source presence is contextual evidence with a named unavailable blocker.
- [x] Select the Source Refresh behavioral contract from the same discovered integration without a type list or
  provider declaration.
- [x] Keep `SyncEntryWithSource` as the only mechanics implementation and adapt its result at the root Feature boundary.
- [x] Resolve title-update policy from the Entry's explicit profile and invoke strict synchronization for every request.
- [x] Install the Feature in root composition before migrating any application caller or adding raw-sync enforcement.

The public request carries only invocation choices: Entry, details/children selection, manual intent, and fetch window.
At least one fetch operation is required as an API invariant. The successful result exposes inserted visible children,
the total inserted count, updated/removed counts, metadata change, and aggregate child-change state without exporting
the domain coordinator's result type.

`SourceManager.get()` is the authoritative refresh lookup and exposes only an installed source or absence; retained stub
metadata is not executable refresh context. Absence therefore has one structured outcome and blocker rather than an
unreachable stub variant. Once an installed-source snapshot makes the integration applicable, a later source
disappearance or `SourceNotInstalledException` is an operation failure rather than retroactively contradicting graph
evaluation. `NoChaptersException` is a separate structured failure; cancellation continues propagating and other
source, network, or persistence errors retain their cause.

The Feature always calls `syncStrictly` with `entry.profileId`. Its composition dependency resolves the existing
profile-scoped title-update preference for that same profile. This removes active-profile ambiguity from the future
migration and preserves the coordinator's explicit-profile persistence checks without moving preference ownership into
the source contract.

No current consumer is migrated in this slice. Direct `SyncEntryWithSource` calls deliberately remain visible until
their owning 6.6.2–6.6.4 relationships are installed; compilation is not preserved with a fallback Feature path or
parallel refresh implementation.

#### 6.6.2 — Direct and Source-owned Consumers

- [x] Migrate Entry details/children refresh and details-only metadata refresh to `EntrySourceRefreshFeature`.
- [x] Make F20 own its children-only Source Refresh relationship and expose structured retry/empty-child outcomes to
  the Immersive screen.
- [x] Make Deep Link own its Source Refresh relationship when resolving a missing canonical child.
- [x] Reject raw `SyncEntryWithSource` references outside its domain implementation, root Feature implementation,
  Domain construction, and tests.
- [x] Leave the boundary visibly failing on the deferred F11 migration and F13/Library Update consumers rather than
  introducing a temporary allowlist.

Entry refresh retains its existing merged-member sequence: each concrete source owner is refreshed in order, the first
failure stops the sequence, and automatic-download handoff receives inserted children only after all requested refreshes
succeed. Source absence and no-child results retain their existing user-facing messages through structured Feature
outcomes. Metadata refresh requests details only and retains per-entry failure isolation.

F20 now owns both child selection and the decision to request children before child-backed media loading. A successful
refresh causes the app to reload persisted children; source absence and no-reading-child remain distinct contextual
unavailability, and operation failures use the existing retryable error state. Entry-level Immersive providers remain
valid and never receive this refresh request.

Deep Link requests a normal details-and-children refresh only when the resolved canonical child is absent locally, then
matches against the Feature's inserted children. The Deep Link Feature continues to expose its existing resolved,
no-match, and failed contract rather than leaking refresh-specific domain exceptions to its consumer.

The boundary has no migration exception list. Its expected remaining findings are the F11 migration host/search paths
and the F13/Library Update worker. Those findings are executable obligations for 6.6.3 and 6.6.4; the rule becomes green
by migrating those owners, not by weakening its dependency direction.

#### 6.6.3 — Migration Refresh Relationship

- [x] Add an F11-owned target-refresh operation with migration-specific structured results.
- [x] Route automatic target search, details completion, and explicit target selection through F11 rather than raw sync
  mechanics or direct Source Refresh consumption.
- [x] Move pre-execution target refresh into `DefaultEntryMigrationFeature` after replay and live-authorization checks.
- [x] Remove target synchronization from the application host so it owns only profile-scoped inspection, persistence,
  transaction, and external adapter work.
- [x] Preserve strict target-profile synchronization and cancellation while reducing the raw-sync boundary to the
  deferred F13/Library Update worker only.

F11 validates the source/target pair before candidate refresh and maps Source Refresh into refreshed, rejected,
source-unavailable, no-children, or operation-failure outcomes. The migration screen retains its existing product
behavior: automatic search may continue after a candidate refresh failure, optional details completion is best-effort,
and explicit target selection requires a successful children refresh.

Execution no longer asks an application database host to perform source synchronization. After replay protection and
live authorization succeed, F11 refreshes the authoritative target Entry returned by its explicit-profile inspection.
Source Refresh then uses that Entry's exact `profileId` and strict persistence path. Any non-successful refresh prevents
the primary transition and is reported as retryable operational failure; cancellation continues propagating. Only then
does F11 re-inspect execution state and prepare its atomic transition.

The application migration host is now a profile-scoped persistence and transaction adapter rather than a mixed
persistence/source-operation coordinator. No replacement synchronizer, F11-specific source implementation, content-type
gate, or caller-owned Source Refresh interpretation was introduced.

#### 6.6.4 — Library Update Refresh Relationship

- [x] Add one provider-less Library Update Refresh Feature selected automatically for every contributed content type.
- [x] Keep F13 as the sole eligibility-policy owner and invoke Library Update Refresh only for its queued eligible
  entries.
- [x] Map Source Refresh into library-specific updated/source-unavailable/no-children/operation-failure results.
- [x] Preserve source grouping, concurrency limits, fetch-window and metadata request context, progress, failure files,
  notification collection, and batch completion.
- [x] Pass only successfully inserted children to the existing F05 automatic-download batch.
- [x] Close the raw `SyncEntryWithSource` consumer boundary with no migration allowlist.

F13 remains a pure shared policy over Entry and Library state; it does not gain source execution responsibilities.
Library Update Refresh owns the operation that follows an eligible decision and is independently unconditional for all
runtime-contributed types. A new content type therefore participates in both eligibility and refresh without adding a
provider, type branch, worker opt-in, or support declaration.

The worker supplies only run evidence: authoritative Entry, metadata preference, and fetch-window bounds. The Feature
always requests children, delegates the mechanics to Source Refresh, preserves strict Entry-profile persistence, and
orders inserted children for the existing notification and automatic-download flow. It does not own Android worker
lifecycle, source concurrency, progress notifications, update counters, or F05 policy.

Structured source absence retains the existing loader message, no children remains a failed update without inventing a
type-specific message, and operation failures retain their cause for logging and the failure report. Only a successful
result can increment new-update counts, enter F24 notification collection, or reach F05. Batch completion still occurs
once after every source group finishes.

The generic raw-sync boundary is now green. `SyncEntryWithSource` is referenced only by its domain implementation and
construction, the root Source Refresh implementation, and tests. Future application refresh consumers must choose and
declare their owning Feature relationship rather than borrowing the mechanics coordinator.

#### 6.6.5 — Metered-source Notification Policy

- [x] Move Library Update queue-warning threshold and metered-source interpretation into F24.
- [x] Make the Android notifier render only F24's required/not-required decision.
- [x] Preserve the existing per-source threshold, missing-source treatment, warning channel, text, timeout, and help
  destination.
- [x] Guard application code from directly interpreting `UnmeteredSource` for Library queue-warning policy.
- [x] Retain Manga downloader `UnmeteredSource` checks as type-owned F03 queue-execution mechanics.

F24's unconditional base relationship now includes the queue-warning consequence for every contributed content type.
Its Feature receives actual queued Entries, groups them by source, excludes installed sources declaring unmetered
access, and returns a structured decision containing the largest metered-source group when the shared threshold is
exceeded. A missing source remains metered for warning purposes, matching existing behavior.

`LibraryUpdateNotifier` no longer owns source metering or the threshold. It maps Library items to Entries, asks F24 for
the decision, and retains only Android notification construction. Both normal Library Update and metadata update use
that same path. This prevents another worker or notifier from independently rebuilding metering policy.

The Manga downloader's check is intentionally unchanged. It controls warning behavior for Manga's active download
queue and lives beside Manga's source-aware execution mechanics. Moving it into F24 would couple user-visible Library
Update policy to type-specific download execution and would erase the consequence ownership established by F03.

Application boundary validation rejects raw `UnmeteredSource` inspection. Source contracts and implementations, root
Feature policy, and type-owned interaction mechanics remain valid owners; generic application consumers must enter
through a Feature result.

#### 6.6.6 — Refresh and Network Reconciliation

- [x] Re-run the production census for raw refresh mechanics, refresh consumers, source availability, metering, Entry
  state, and profile/preference evidence.
- [x] Enforce that the three child-list source contracts remain owned by `SyncEntryWithSource`.
- [x] Verify raw synchronization and application metering boundaries are green without migration exceptions.
- [x] Reconcile every `C15`, `C16`, and applicable `C17`, `C20`, and `C22` consequence against its executable owner.
- [x] Update Feature, SDK, atlas, inventory, and status documentation from executable behavior.

| Context | Reconciled executable owner |
| --- | --- |
| `C15` refresh mechanics | `SyncEntryWithSource` alone interprets empty child lists, incremental requests, and host-side number recognition. Source Refresh is the sole root boundary over it. Build validation rejects those contracts elsewhere while allowing SDK/compatibility/source implementations. |
| `C16` metering | F24 owns Library queue concentration and the shared threshold; Manga F03 owns its download-queue mechanics. Generic application/data/domain/presentation code cannot interpret the marker. |
| `C17` installed/local/stub state | Source Refresh uses installed-source lookup and returns structured absence; Local is an installed source. F24 treats missing sources conservatively as metered. F04 retains its separate local/stub action context. No global source-state capability was added. |
| `C20` Entry/child state | F13 evaluates the queued Library snapshot; Source Refresh receives an authoritative Entry; F20/Deep Link/F11 map their own consequences; F05 receives only successful inserted children. State remains invocation evidence rather than support truth. |
| `C22` profile/preferences | Source Refresh resolves title-update policy for `entry.profileId` and always synchronizes strictly. Library metadata preference and fetch window remain request evidence; F11 execution refresh uses its profile-pinned inspected target. |

The production census finds one mechanics implementation and no raw application consumer. Direct Source Refresh use is
limited to its base Entry/metadata consumers and root Feature coordinators for F11, F20, Deep Link, and Library Update.
No consumer registry exists: each consuming Feature declares and owns its consequence.

The active `eu.kanade.tachiyomi.source.entry.UnmeteredSource` contract is the runtime metering authority. The older
`eu.kanade.tachiyomi.source.UnmeteredSource` class remains only in the legacy public source-api ABI and baseline profile;
no production decision consumes it. Whether the legacy adapter must translate that marker into the current contract is
an explicit `C24` compatibility obligation for Phase 6.8, not a second current metering authority or a hidden 6.6
allowlist.

Boundary validation now protects both levels of dependency direction: application consumers cannot borrow raw
`SyncEntryWithSource` or `UnmeteredSource`, and no Feature/type/application code can reinterpret the three C15 source
mechanics contracts. SDK/source declarations, source compatibility implementations, the mechanics coordinator, root
Feature policy, and type-owned download mechanics remain narrow named owners.

Manifesto comparison found no content-type support matrix, provider requirement, mandatory refresh operation, consumer
allowlist, compatibility synchronizer, duplicated metering rule, or presentation-owned behavior. A future contributed
type participates in Source Refresh, F13, Library Update Refresh, and F24 automatically; it implements only genuinely
type-specific interactions.

### 6.7 — Tracking Integration (`C18`, `C19`, applicable `C20`, `C22`)

- [x] Add one Tracking Feature boundary that composes actual Entry type, tracker-declared applicability,
  authentication/profile state, existing tracks, reading dates, privacy, status, and scoring.
- [ ] Migrate Entry actions/dialogs, search/register/update guards, automatic add/sync, Library tracker filters, Stats,
  and tracking documentation to the same structured results.
- [ ] Keep tracker implementations authoritative and do not turn tracker capabilities into content-type providers.

#### 6.7.0 — Tracking Ownership Census and Architecture Split

- [x] Classify every production tracker registry, applicability, authentication, source-acceptance, sub-capability,
  operation, Library, Stats, settings, backup, migration, and presentation consumer.
- [x] Define one application-facing Tracking Feature before migrating consumers, with one application host as the only
  bridge from root Feature policy to the existing tracker system.
- [x] Keep tracker contracts and implementations authoritative while preventing raw tracker objects, registry access,
  or capability casts from remaining application behavior APIs.
- [x] Assign each cross-feature relationship to its consuming Feature consequence rather than creating a tracker
  consumer list or copying tracker support into content-type contributions.
- [x] Split the implementation into architecture-first milestones and record the final enforcement/reconciliation gate.

The census finds one external integration system with several product consequences, not a content-type interaction
provider. `TrackerManager` remains the authoritative runtime registry, and each `Tracker` remains authoritative for its
supported Entry types, authentication, remote operations, statuses, scoring, dates, privacy, deletion, and enhanced
source matching. Tracking support is never copied into Manga, Anime, Book, or a future type contribution.

The target boundary is one injected `EntryTrackingFeature`. Its API is divided into cohesive Entry session, operation,
automatic synchronization, account, Library, and Stats contracts rather than one giant source file. A root coordinator
resolves graph relationships and returns neutral service IDs, descriptors, requests, and structured results. It never
exports `Tracker`, `TrackerManager`, `EnhancedTracker`, `DeletableTracker`, or tracker-owned database/network models.

An application-supplied `EntryTrackingHost` is the sole adapter to the current tracker system. The host is composition
infrastructure, not an alternate consumer API: only the root Tracking Feature may call it. Its implementation may be
split by registry, account, Entry operation, synchronization, and analytics responsibilities, but all raw tracker
access remains inside that owned adapter or the tracker system itself. This preserves the existing module direction:
`entry-interactions` does not depend on the app module and tracker contracts do not move merely to make the refactor
compile.

| Current consumer group | Final owner and boundary |
| --- | --- |
| `TrackerManager`, built-in trackers, network clients, credentials, and tracker DTO conversion | Tracker-owned external implementation. The registry is the single service-discovery authority; no second Tracking Feature service list is maintained. |
| Entry tracking action and reactive badge state | Tracking Feature availability/session results composed from actual Entry type, registered tracker applicability, login state, source identity, and enhanced source acceptance. |
| Tracking dialog, search, register, refresh, status/progress/score/date/private mutation, and remote deletion | Tracking Feature operations with service-ID requests and structured success, unavailable, rejected, and failed results. Presentation receives neutral descriptors and capability-specific controls. |
| Automatic enhanced matching from Catalogue, History, Entry refresh, Merge initialization, and application callbacks | Tracking Feature automatic-binding consequence. Source matching remains host mechanics; every caller consumes one result instead of filtering raw trackers. |
| Reader/Entry progress update, delayed retry, remote refresh, and enhanced progress reconciliation through F09 | Tracking Feature synchronization consequences. Existing track rows and child state are request evidence; F09 remains the only Consumption mutation owner. |
| F11 tracking transfer | F11 keeps transaction ownership and consumes a Tracking Feature preparation consequence. Enhanced tracker identity transformation remains host mechanics and performs no network operation. |
| Library tracker filters | F14 continues owning filter policy. Tracking Feature supplies the reactive logged-in service projection and tracker applicability evidence; F14 does not inspect the registry. |
| Library score sorting | Tracking Feature supplies normalized per-Entry score evidence and whether the current logged-in integrations apply to each Entry type; Library retains sort ordering only. |
| Stats | Tracking Feature supplies the logged-in integration count and normalized tracked/scored Entry summary; Stats does not resolve tracker identities or scoring conversions. |
| Tracking settings and tracker preference rows | Tracking Feature account projection derives from the authoritative registry. The current hardcoded built-in service list and separate enhanced-service list are removed; login/logout requests use structured account results. |
| Backup validation | Tracking Feature resolves referenced service IDs to missing-login names; backup parsing and missing-source validation remain backup-owned. |
| OAuth callback activities | Tracker-owned platform adapters may parse and persist a concrete service callback. They do not decide Entry applicability or expose a second service catalogue. |
| Tracker logos and tracking vocabulary | Tracking Feature descriptors carry tracker-owned presentation evidence; F23 continues owning Entry-type vocabulary and never authorizes tracking. |

Tracker applicability is contextual external evidence. The base Tracking relationship is shared for every contributed
content type, but an Entry consequence is applicable only when the authoritative tracker snapshot declares that exact
type. Login state independently controls authenticated session, automatic binding, filters, sync, and Stats
consequences; being logged out does not erase the fact that a tracker supports the Entry type. Enhanced source
acceptance is a narrower operation blocker and never becomes a type capability.

Tracker sub-capabilities are also contextual facts, not specialized obligations. A service with no scores, dates,
privacy, remote deletion, or enhanced matching is a valid tracker. Its descriptor simply omits the corresponding
control/consequence. Existing tracks, Library membership, child progress, profile ID, preference values, and remote
results remain operation evidence rather than static support declarations.

The current settings screen is part of this migration even though the earlier summary did not name it explicitly. It
hardcodes individual built-in trackers and independently discovers enhanced trackers, so adding a service can require a
follow-up UI edit. The authoritative tracker registration must carry enough account/presentation metadata for settings,
backup diagnostics, and Entry surfaces to derive automatically. This is an owner declaration, not a central list of
capabilities or content types.

The following direct uses remain reviewed owner-local boundaries after migration:

- tracker contract declarations and built-in tracker/network implementations;
- `TrackerManager` registry construction and tracker-owned credential/preferences storage;
- the single application `EntryTrackingHost` implementation and root composition wiring; and
- tracker-specific OAuth callback parsing that performs the external contract operation without deciding product
  applicability.

Every other UI, worker, reader binding, Library/Stats model, backup policy, and application coordinator must consume the
Tracking Feature. Boundary validation will derive the forbidden raw tracker surface by package/interface ownership; it
will not maintain an allowlist of migrated consumer filenames or current tracker implementations.

Implementation proceeds in architecture-first slices:

1. **6.7.1 — Tracking boundary and host architecture:** add the split Feature API, host port, neutral models/results,
   graph/context integrations, root implementation, and composition binding before migrating callers.
2. **6.7.2 — Entry tracking session:** migrate Entry action availability, reactive logged-in/source-compatible service
   projection, dialog rows, and tracker presentation evidence.
3. **6.7.3 — Entry tracking operations:** migrate search/register/refresh and status, progress, score, date, privacy,
   unregistration, and remote-deletion operations behind structured Feature results.
4. **6.7.4 — Automatic binding and synchronization:** migrate Catalogue, History, Entry, Merge, reader, delayed update,
   progress reconciliation, and F11 preparation relationships without moving F09 or F11 ownership.
5. **6.7.5 — Account, settings, and backup integration:** derive account rows from authoritative registrations,
   migrate login/logout and backup missing-login diagnostics, and retain OAuth callback parsing as owner-local mechanics.
6. **6.7.6 — Library and Stats integration:** supply F14 tracking-filter inputs, score-sort evidence, and Stats summaries
   from the Feature rather than the raw registry.
7. **6.7.7 — Tracking reconciliation:** remove unused parallel adapters/helpers and declaration-restatement tests,
   enforce the raw-tracker boundary, add unknown-type/tracker behavioral contracts, rerun the complete census, update
   behavior/docs/projections, and close `C18`, `C19`, and assigned `C20`/`C22` rows.

Manifesto comparison found no type-level Tracking provider, mandatory interaction, per-type tracker matrix, copied
authentication fact, static consumer registry, or application-visible raw tracker facade. A future content type becomes
trackable when a registered tracker declares it; all common Entry, sync, Library, Stats, settings, backup, and
documentation consequences then flow through the same Feature without editing that type.

#### 6.7.1 — Tracking Boundary and Host Architecture

- [x] Add a split application `EntryTrackingFeature` API with neutral service identity, capability, availability, and
  reactive session results.
- [x] Add one segregated `EntryTrackingHost` and an app adapter that extracts facts from the authoritative tracker
  registry, source context, authentication flows, and persisted tracks.
- [x] Declare registry, Entry availability/session/operation, automatic-binding, synchronization, Library, Stats,
  account, backup, presentation, and documentation consequences before migrating callers.
- [x] Select the Tracking behavior contract and every relationship for a provider-less contributed type without a
  type-level Tracking capability.
- [x] Install the Feature and host through root runtime composition while keeping the app dependent only on the root
  Entry-interactions module.
- [x] Derive host-package declarations in boundary validation and reject them as application APIs outside their owned
  host/root/composition structure.
- [x] Leave existing raw tracker consumers visible for later milestones without a migration allowlist.

The initial Feature separates registered applicability from authenticated execution. `availability(type)` evaluates
the exact requested Entry type against authoritative tracker declarations. `observeSession(entry)` reevaluates login,
source acceptance, and existing track state reactively, returning only authenticated applicable services or a
structured unsupported/not-logged-in/source-rejected reason. Logging out therefore cannot erase declared support, and
an enhanced source mismatch cannot redefine the content type.

The host snapshot carries only facts: stable service identity, tracker-owned presentation, supported Entry types,
status/score/date/privacy/deletion/automatic-binding properties, authentication, source acceptance, and an optional
persisted track. The root coordinator alone interprets those facts through declared context inputs. Neither the Feature
API nor graph exports `Tracker`, `TrackerManager`, `EnhancedTracker`, `DeletableTracker`, or tracker database/network
models.

All eight planned relationship groups enter static graph discovery. Registry and Migration preparation apply for every
contributed type; Entry availability/session/operations, automatic binding, synchronization, Library, and Stats remain
conditional on their owning runtime evidence. This makes later consumer work a migration into known consequences
rather than a series of hand-added tracker integrations.

The boundary rule derives all public host-package declarations from source ownership. It does not enumerate host model
names or current consumers. Existing raw tracker references remain unguarded only until 6.7.7 because enforcing that cut
now would require a temporary file allowlist; their complete census remains the executable migration ledger instead.

The behavioral proof supplies a synthetic BOOK tracker while BOOK contributes no Tracking provider. Availability and
session behavior activate through the unchanged Feature path, and a logged-out snapshot blocks only the session. The
test also proves every Tracking integration is discovered for the provider-less contributed type without repeating the
built-in tracker/type matrix.

Manifesto comparison found one external support authority, one application Feature, and one non-consumer host bridge.
There is no mandatory type operation, copied support declaration, type/provider matrix, second tracker registry,
consumer allowlist, raw application facade, or test that restates current built-in declarations.

#### 6.7.2 — Entry Tracking Session

- [x] Derive Entry tracking-action availability from registered tracker support through the Tracking Feature.
- [x] Derive Entry tracking count and authenticated/source-compatible session presence from the reactive Feature result.
- [x] Build tracking-dialog rows from neutral session services and existing track state instead of the raw registry.
- [x] Render tracker identity, status labels, formatted score, date/privacy controls, and automatic-binding choice from
  Feature-owned presentation evidence.
- [x] Remove the application `TrackItem` wrapper that exported a raw tracker into presentation.
- [x] Leave mutations, search, refresh, and registration behind their existing operation owners for 6.7.3.

The Entry action preserves the existing split semantics: registered type support decides whether the action exists,
while the live session decides whether selecting it opens the tracker dialog or account settings. The Entry badge and
dialog now share the same reactive authenticated, type-compatible, and source-compatible service projection, so those
consumers cannot reconstruct different tracker filters.

Dialog rows carry stable service identity, presentation metadata, sub-capability evidence, existing track state, and
tracker-formatted score text. Status labels are resolved from the tracker-owned status projection. Presentation no
longer receives `Tracker` or calls capability methods. The shared logo primitive accepts neutral name/resource evidence;
its temporary raw-tracker overload remains only for the account settings surface assigned to 6.7.5.

Search, automatic registration, refresh, status/progress/score/date/privacy mutation, unregistration, and remote
deletion still resolve raw services inside the existing dialog operation code. This is an explicit 6.7.3 obligation,
not a second session authority or compatibility fallback. No raw-tracker boundary allowlist is introduced before the
complete consumer cut in 6.7.7.

Manifesto comparison found no new type opt-in, type matrix, copied authentication/source gate, presentation support
flag, consumer registry, or mandatory tracker sub-capability. A future type declared by a tracker receives the Entry
action, badge, and dialog rows through the unchanged Feature path; absent trackers or sub-capabilities remain valid.

#### 6.7.3 — Entry Tracking Operations

- [x] Add cohesive Tracking Feature operations for refresh, search, manual/automatic registration, mutations, and
  local/remote removal with neutral requests and structured outcomes.
- [x] Revalidate exact service registration, Entry-type support, authentication, and source acceptance at command time.
- [x] Validate status, score, date, privacy, automatic-binding, existing-track, and remote-deletion requirements from
  tracker-owned evidence before invoking mechanics.
- [x] Keep raw tracker objects, tracker search models, registry lookup, capability casts, and domain interactors inside
  the application host adapter.
- [x] Migrate every Entry tracking dialog and selector to the Feature operation boundary.
- [x] Split the former monolithic tracking dialog into home, status, progress, score, date, search, removal, and feedback
  files with mirrored Tracking operation coverage.

`EntryTrackingFeature` remains the single injected behavior boundary and now implements a cohesive operations facet;
the facet is not separately registered or injected. Each command carries the authoritative Entry and stable service ID.
The coordinator resolves a fresh host snapshot before dispatch, so a screen opened before logout, source change, or
service removal cannot bypass current applicability by retaining a tracker object.

Search candidates are neutral immutable values. The host converts tracker-owned search models at the boundary and
reconstructs them only when registration executes; application presentation no longer imports the raw tracker search
model. Each candidate retains its originating service identity, and cross-service registration is rejected. Mutations
are structured intents, and the coordinator obtains the current persisted track from the same resolved session rather
than trusting a stale UI-supplied record.

Remote removal is now one coordinated operation. When requested and supported, remote deletion is attempted first;
local removal still runs if the remote call fails, preserving the prior user-visible local-removal outcome while making
the partial failure explicit. Cancellation still propagates. Other failures and operation-time unavailability are
structured and retain the existing UI logging or error presentation.

The raw registry remains in automatic synchronization, account/settings/backup, Library/Stats, and the temporary
settings logo adapter assigned to 6.7.4–6.7.6. Global raw-tracker enforcement therefore remains deferred to 6.7.7
without introducing a file allowlist. The migrated Entry tracking package itself has no raw tracker dependency.

Manifesto comparison found one external mechanics adapter, one Feature authority, operation-time contextual
revalidation, and no type matrix, type provider, copied capability flag, raw application model, operation facade, or
consumer registry. Tracker sub-capability absence produces structured unavailability and never invalidates a type.

#### 6.7.4 — Automatic Binding and Synchronization

- [x] Add an automatic-behavior facet to the single `EntryTrackingFeature`; do not register or inject another behavior
  authority.
- [x] Select eligible automatic-binding and progress-synchronization services from fresh host snapshots using exact
  Entry type, authentication, source acceptance, automatic-binding support, and existing-track evidence.
- [x] Migrate Catalogue, History, Entry library initialization, Merge initialization, reader updates, delayed retries,
  remote refresh reconciliation, and manual registration reconciliation to structured Feature operations.
- [x] Make F11 consume Tracking-owned track preparation before its optimistic transaction while leaving transaction and
  persistence ownership in Migration.
- [x] Confine raw tracker selection, enhanced matching, progress mechanics, and tracker-specific migration transforms
  to the application Tracking host and tracker implementation.

Automatic binding now has one policy gate. Callers submit an Entry and do not enumerate trackers, cast enhanced
services, test types, or reproduce source acceptance. The Feature derives the eligible services from current external
evidence, the host performs matching, and each bound track is reconciled through the same Tracking synchronization
relationship. Failures, unmatched services, and contextual unavailability remain structured results.

Progress synchronization follows the same split: the Feature chooses authenticated, type-compatible services with an
existing track; the host updates only those service IDs and retains delayed-retry mechanics. Reader, Entry, and worker
callers no longer reach the raw progress interactor. Remote refresh and registration return refreshed/bound track facts
to the Feature, which explicitly invokes reconciliation instead of letting lower-level interactors trigger a hidden
cross-feature consequence. The existing reconciliation mechanic still delegates local Consumption changes to F09.

Migration keeps ownership of execution, optimistic validation, and atomic row persistence. It now asks Tracking to
prepare target rows after live authorization succeeds. The Tracking host performs the tracker-specific local identity
transform without network work, then Migration commits the returned neutral rows. This milestone also corrects the
static graph census: Migration preparation had been described in 6.7.1 but was missing from executable Tracking
integrations; it is now an unconditional shared Tracking consequence for every contributed type.

Manifesto comparison found no content-type Tracking provider, type matrix, mandatory tracker operation, consumer
service selection, copied source/authentication gate, or compatibility callback. A future type becomes eligible for
binding and synchronization solely when an external tracker declares support and the relevant live evidence exists;
Migration preparation requires no additional type opt-in.

#### 6.7.5 — Account, Settings, and Backup Integration

- [x] Add a cohesive account/diagnostics facet to the single `EntryTrackingFeature`; do not register another account
  service or expose the application host to consumers.
- [x] Derive ordinary and enhanced settings rows from authoritative tracker registrations instead of hardcoding built-in
  services and separately discovering enhanced services.
- [x] Move login method, credential identity, OAuth initiation, enhanced availability, account presentation order, and
  current account state behind tracker-owned declarations and the Tracking host.
- [x] Route credential login, passive login, and logout through structured Feature results while retaining concrete
  OAuth callback parsing as tracker-owned platform mechanics.
- [x] Migrate backup missing-login diagnostics from direct registry access to stable service IDs resolved by Tracking.
- [x] Remove the temporary raw-tracker settings preference, widget, and logo adapter.

The account facet projects every registered service through neutral account rows. Trackers declare whether login uses
credentials, external authorization, or passive source-backed activation; credential trackers declare username versus
email identity, and external trackers provide their own authorization URI. Enhanced source installation remains host
evidence. The settings screen only groups and presents these results, so a future tracker appears without another UI
branch or registry copy.

Account operations re-resolve stable service IDs before dispatch and return completed, external-authorization,
unavailable, or failed results. Stored credentials are requested only for the selected credential service rather than
being exposed in the observable account snapshot. Existing tracker implementations continue owning credentials,
network login, logout, and OAuth state. `TrackLoginActivity` retains concrete callback decoding because it completes an
external tracker protocol and does not decide Entry applicability or construct the service catalogue.

Backup validation supplies referenced service IDs to Tracking and receives sorted names for registered services that
are currently logged out. Missing/removed service IDs retain the previous ignored behavior. Backup parsing and source
validation remain backup-owned; the validator no longer interprets the tracker registry.

Manifesto comparison found one authoritative registration, automatically derived settings rows, purpose-specific
account metadata, one Feature boundary, and no hardcoded service catalogue, content-type support copy, UI tracker cast,
backup registry query, or second login authority. Tracker-specific OAuth implementation remains explicit external work,
not a forgotten application integration.

#### 6.7.6 — Library and Stats Integration

- [x] Add a collection-evidence facet to the single `EntryTrackingFeature`; do not expose raw tracks, trackers, or the
  host to Library and Stats consumers.
- [x] Project reactive logged-in service descriptors, per-Entry service membership, normalized score evidence, and
  score-applicable Entry types from authoritative tracker and track state.
- [x] Keep F14 responsible for Library filter policy and Library responsible for sort ordering while removing their
  registry lookup, tracker casts, and score conversion.
- [x] Derive Library filter/settings rows from reactive Feature account/service projections.
- [x] Supply Stats with one Tracking-owned summary of logged-in service count, tracked Entry count, and mean normalized
  score.

The collection facet exposes only facts that collection consumers need. Its host combines persisted tracks with the
current logged-in registry, drops tracks belonging to logged-out or removed services, and converts tracker-specific
scores to the shared ten-point scale. The root Feature projects stable service IDs and derives the union of Entry types
supported by the authenticated services. Library passes service membership to F14, retains preference and tri-state
filter policy, and retains comparator/sort-direction ownership.

Stats submits the current library Entry IDs and receives an aggregate result. Tracking counts only Entries with an
authenticated persisted track, averages scored services within each Entry before averaging across Entries, preserves
the existing no-score result, and reports the authenticated service count. Stats no longer resolves services or knows
how their score scales work.

Manifesto comparison found one authoritative registry, one Feature projection shared by Library and Stats, automatic
participation for any future tracker/type declaration, and no copied login state, type matrix, tracker consumer list,
raw track model, or presentation support flag. Missing tracker support remains ordinary absence; F14 and Stats do not
opt content types into Tracking.

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
