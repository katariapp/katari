# Capability Refactor Status

Updated: 2026-07-22

## Repository Snapshot

- Branch: `features-arch-refactor`
- Architecture reset commit: `666487574` (`(refactor): reset capability architecture direction`)
- Contribution semantics commit: `d89e51693` (`(feat): define generic feature contributions`)
- Discovery and assembly commit: `03d0b6422` (`(feat): assemble discovered feature graph`)
- Evaluation and obligations commit: `0a578b784` (`(feat): evaluate feature relationships`)
- Contract and projection selection commit: `f1e66f671` (`(feat): select feature contracts and projections`)
- Phase 3 completion: `c795c505c` (`(refactor): complete feature graph architecture`)
- Migration-readiness commit: `7ab311608` (`(docs): update migration plan`)
- Latest upstream reconciliation: `5e3f948b4` (`Merge branch 'upcoming' into features-arch-refactor`)
- Milestone 4.1 commit: `0accb3667` (`(refactor): migrate entry provider identity`)
- Milestone 4.2.1 commit: `62c9741a1` (`(refactor): bind independent entry providers`)
- Milestone 4.2.2 commit: `f4a6d153c` (`(refactor): decompose download providers`)
- Milestone 4.2.3 commit: `c046e1f8f` (`(refactor): split migration and merge providers`)
- Milestone 4.2.4 commit: `c2ca736e4` (`(refactor): migrate remaining entry providers`)
- Milestone 4.3 commit: `c88ff5fe9` (`(refactor): unify entry type runtime composition`)
- Phase 4 completion: `17726de20` (`(refactor): replace interaction registry`)
- Runtime-bridge boundary correction: `4b517ed53` (`(fix): discover entry runtime module bridges`)
- F01 Open completion: `106fec52e` (`(refactor): migrate open feature integration`)
- Architecture Gate 5.0: `83b2f93e7` (`(refactor): enforce feature-facing entry boundary`)
- F02 Continue completion: `0480ffeff` (`(refactor): migrate continue feature integration`)
- F03–F05 completion: `846c6029f` (`(refactor): migrate core download features`)
- F06–F08 completion: `91d57f376` (`(refactor): migrate remaining download features`)
- F09, F10, and F13 completion: `e175cbc3f` (`(refactor): migrate entry state features`)
- F14, F18, and F19 completion: `a03ff2a71` (`(refactor): migrate entry filtering and preview features`)
- F20, F21, and F22 completion: `dd8fc5106` (`(refactor): migrate immersive, related, and progress features`)
- F23 Type Presentation, F24 Library-update Notifications, F25 Viewer Settings, F26 Media-cache Maintenance, and F27
  Profile Preference Ownership are committed and validated.
- F12.3 Shared Merge coordinator commit: `5812f0e47` (`(refactor): implement shared merge coordinator`)
- F12.4 Entry ownership migration commit: `e8cd191b5` (`(refactor): migrate merge ownership consumers`)
- F12.5 Download ownership migration commit: `32043dabf` (`(refactor): migrate merge download ownership`)
- F12.6 Lifecycle ownership migration commit: `01fde94fc` (`(refactor): migrate merge lifecycle ownership`)
- F12 completion commit: `5e67ce793` (`(refactor): complete merge feature migration`)
- F11.0 Migration planning commit: `da19e7df3` (`(docs): plan entry source migration`)
- F11.1 Migration boundary commit: `4804a8b41` (`(refactor): define migration feature boundary`)
- F11.2 transaction semantics commit: `de55ae95f` (`(docs): define migration transaction semantics`)
- F11.3 primary transfer commit: `fff5aa853` (`(refactor): implement migration primary transfer`)
- F11.4 consequence delivery commit: `7e44c5c71` (`(refactor): implement migration consequences`)
- F11.5 application consumer commit: `9ffa03ca3` (`(refactor): migrate entry migration consumers`)
- Phase 6 plan commit: `ac9c46d41` (`(docs): plan contextual integration migration`)
- Phase 6.1 runtime-context commit: `7ad238af7` (`(feat): resolve runtime feature context`)
- Phase 6.2 catalogue/source-description commit: `9685d465a` (`(refactor): migrate catalogue source description`)
- Phase 6.3 source-action commit: `debbb1c29` (`(refactor): migrate source action features`)
- Phase 6.4.1 Entry Feature context commit: `0d1f833f3` (`(refactor): resolve source-backed feature context`)
- Phase 6.4.2 Download Action context commit: `756beb981` (`(refactor): resolve download action context`)
- Phase 6.4.3 Automatic Download context commit: `381ea4e11` (`(refactor): resolve automatic download context`)
- Phase 6.4.4 Entry state-mutation context commit: `c75c813d7` (`(refactor): resolve entry state mutation context`)
- Phase 6.4.5 Download Lifecycle context commit: `ceb7af876` (`(refactor): resolve download lifecycle context`)
- Phase 6.4.6 Update Eligibility context commit: `edc5600a9` (`(refactor): resolve update eligibility context`)
- Phase 6.4.7 Library Filter context commit: `d300ca7c4` (`(refactor): resolve library filter context`)
- Phase 6.4.8 Child Group Filtering disposition commit: `053e99595` (`(refactor): classify child filtering request state`)
- Phase 6.4.9 Library Progress disposition commit: `6ee097b12` (`(refactor): classify library progress request state`)
- Phase 6.4.10 Library Update Notification context commit: `a2beb6ed8` (`(refactor): resolve library update notification context`)
- Release baseline merge: `54b839168` (merged `origin/upcoming` at `131163af6`, including v1.3.2)
- Phase 6.4.11 Migration availability context commit: `50f34a2fb` (`(refactor): resolve migration availability context`)
- Phase 6.4.12 Migration preparation context commit: `e2424a968` (`(refactor): resolve migration preparation context`)
- Phase 6.4.13 Migration execution context commit: `7d729ef98` (`(refactor): resolve migration execution context`)
- Phase 6.4.14 Merge preparation context commit: `02fd4c20f` (`(refactor): resolve merge preparation context`)
- Phase 6.4.15 Merge execution context commit: `6418180b7` (`(refactor): resolve merge execution context`)
- Phase 6.4.16 Merge context audit commit: `0360453ab` (`(refactor): complete merge context audit`)
- Phase 6.4.17 Entry Feature context closure commit: `f8653cdfd` (`(refactor): complete entry feature context migration`)
- Phase 6.5.0 Media context migration plan commit: `3ff63d635` (`(refactor): define media context migration`)
- Phase 6.5.1 Cover network context commit: `dfd0de181` (`(refactor): resolve cover network context`)
- Phase 6.5.2 Child WebView context commit: `e0b7f7637` (`(refactor): resolve child web view context`)
- Phase 6.5.3 Type-owned media closure commit: `9c665de83` (`(refactor): close type-owned media context`)
- Phase 6.5.4 Media context reconciliation commit: `20aefd78f` (`(refactor): reconcile media context ownership`)
- Phase 6.6.0 Refresh migration plan commit: `eb29f2327` (`(refactor): define source refresh migration`)
- Phase 6.6.1 Source Refresh architecture commit: `5f580cc3b` (`(refactor): establish source refresh feature`)
- Phase 6.6.2 Direct/source-owned consumers commit: `a6a28b222` (`(refactor): migrate direct source refresh consumers`)
- Phase 6.6.3 Migration refresh relationship commit: `fd86a52a8` (`(refactor): move migration refresh behind feature`)
- Phase 6.6.4 Library Update refresh commit: `d20cf9fba` (`(refactor): move library refresh behind feature`)
- Phase 6.6.5 Metered-source policy commit: `f73dc2232` (`(refactor): move queue warning policy behind feature`)
- Phase 6.6.6 Refresh/network reconciliation commit: `ab0036182` (`(refactor): reconcile source refresh boundaries`)
- Phase 6.7.0 Tracking migration plan commit: `fb96dfe4a` (`(refactor): define tracking feature migration`)
- Phase 6.7.1 Tracking boundary commit: `62f68d30e` (`(refactor): establish tracking feature boundary`)
- Phase 6.7.2 Entry tracking session commit: `bf3507b53` (`(refactor): migrate entry tracking session`)
- Phase 6.7.3 Entry tracking operations commit: `3a26ce996` (`(refactor): migrate entry tracking operations`)
- Phase 6.7.4 Tracking automation commit: `bba933301` (`(refactor): migrate tracking automation`)
- Phase 6.7.5 Tracking accounts commit: `5322276d9` (`(refactor): migrate tracking accounts`)
- Phase 6.7.6 Tracking collection commit: `a3524f04c` (`(refactor): migrate tracking collection consumers`)
- Phase 6.7.7 Tracking reconciliation commit: `ac5db98eb` (`(refactor): reconcile tracking boundaries`)
- Phase 6.8 Compatibility reconciliation commit: `555d409b6` (`(refactor): reconcile contextual integration boundaries`)
- Phase 7 planning commit: `b3dc60105` (`(docs): define contract validation phase`)
- Application compilation-baseline commit: `d25aacc27` (`(fix): restore application compilation`)
- Phase 7.0 contract-execution architecture commit: `d7b363bff` (`(feat): execute selected feature contracts`)
- Phase 7.1 production contract census commit: `6be1cec52` (`(docs): census production feature contracts`)
- Phase 7.1.0 production validation host commit: `4af26ba57` (`(feat): validate production interaction contracts`)
- Phase 7.1.1 fundamental contract commit: `5a02a90e2` (`(test): execute fundamental feature contracts`)
- Phase 7.1.2 Download contract commit: `bef5f374e` (`(test): execute download feature contracts`)
- Phase 7.1.3 Library and Media contract commit: `ad53083b0` (`(test): execute library and media contracts`)
- Phase 7.1.4 Source context contract commit: `e14affa81` (`(test): execute source context contracts`)
- Phase 7.1.5 Workflow and Tracking contract commit: `26b44c8d3` (`(test): execute workflow and tracking contracts`)
- Phase 7.1.6 Contract Reconciliation commit: `f4b4382f9` (`(test): reconcile production feature contracts`)
- Phase 7.2 Boundary Enforcement commit: `691011fce` (`(test): enforce graph-selected contract validation`)
- Phase 7.3 Developer Reporting commit: `6ba6c6d79` (`(feat): report evaluated feature architecture`)
- Phase 7.4 Projection Participation commit: `6ac9481e6` (`(feat): classify optional feature projections`)
- Phase 7.4 Feature Projection commit: `f39734c78` (`(feat): project entry feature documentation`)
- Phase 7.4 Production Projection commit: `dc0c7c1cb` (`(feat): bind production documentation projection`)
- Phase 7.4 Content Reference commit: `1c2c295e8` (`(docs): generate content type reference`)
- Phase 7.4 Source SDK Coverage commit: `8597962de` (`(docs): verify source SDK consumer coverage`)
- Phase 7.5 Validation Design commit: `4897232ca` (`(docs): define entry feature validation gates`)
- Phase 7.5 Repository Wiring commit: `9869c9038` (`(build): verify entry feature architecture`)
- Phase 7.5 Register Reconciliation commit: `b82822cdf` (`(test): reconcile entry feature validation register`)
- Phase 8.0 final census commit: `450da780e` (`(docs): census entry feature legacy surface`)
- Phase 8.1 residual cleanup commit: `54bc77f2d` (`(refactor): clean entry feature legacy boundaries`)
- Latest earlier production migration: `e04b2481c` (`(refactor): derive download capabilities from providers`)
- Phase 2 completion: `918fcc4d3` (`(refactor): complete bookmark download capability proof`)
- Always verify `HEAD`, the working tree, and recent commits before relying on this snapshot.

## Active Work

- Phase: Phase 8 — Legacy Removal, Boundary Enforcement, and Build Completion
- Milestone: Phase 8 exit gate and final manifesto review
- State: Complete, pending the closing commit. The complete pre-release, release, architecture, ABI, migration, and
  documentation gates pass. Exit-gate testing corrected stale selection-presentation and tracking-boundary tests and
  restored selection snapshotting in the three split tracking selectors. The final comparison found no duplicated
  support authority, mandatory provider, current-type completion matrix, or curated Feature consequence list.

Focused Phase 6 preparation findings:

- The graph discovers typed context-input definitions but production has no runtime context-resolution path. Existing
  Features therefore own contextual policy locally without declaring those dependencies to graph evaluation.
- Phase 6 must begin with a generic runtime-context architecture gate. Migrating source/tracker casts first would leave
  contracts, reporting, projections, and delayed obligations unable to discover those relationships.
- `C01`–`C24` split by consequence owner rather than context category: catalogue/description, source actions/resolution,
  existing Entry Features, media/renderers, refresh/network policy, tracking, and compatibility reconciliation.
- Multi-owner facts such as local/stub state, Entry state, selection, preferences, and platform support remain
  operation-scoped evidence for each owning Feature; they do not become global support capabilities or one context
  manager.
- Decision `0022` is accepted. Phase 6 preparation changed planning only and introduced no production behavior.

Focused Phase 6.1 findings:

- Context inputs, the Feature-owned rule, and possible blocker definitions are declared together. Undeclared evidence
  reads and ad hoc blockers fail rather than remaining invisible to reporting.
- Static conditional relationships now expose candidate consequences without authorizing them and retain both supplied
  and pending specialized adapters.
- Runtime evidence resolution distinguishes missing, blocked, incomplete, and applicable results. Obligations appear
  only after context establishes applicability, and consequences appear only after the relationship is complete.
- Resolution uses the exact evaluated subject and module-controlled evaluation objects. Application modules remain
  unable to depend on or export the graph machinery.
- An unknown contextual contribution passes through unchanged discovery and exercises every runtime result without a
  type, feature, capability, context, or blocker registry edit.

Focused Phase 6.2 findings:

- Catalogue presence, Latest availability, language, descriptive entry types, and item orientation now enter one
  Feature-owned source description. Catalogue and Latest absence are contextual blockers rather than invalid-source
  states.
- Browse, searches, feeds, migration source selection, extensions/sources, filters, repository projection, Library,
  and Related Entries consume the same projection. Domain/data assembly uses a narrow port implemented by that Feature;
  application code is required to use the Feature itself.
- Domain source models carry optional catalogue structure instead of an independent Latest-support boolean. Source
  metadata remains descriptive and never validates or authorizes returned Entry behavior.
- Raw source discovery, paging calls, extension/stub persistence, and compatibility conversion remain owner-local.
  Immersive opt-in and its source-type pruning are deliberately left for their owning F20 contextual milestone in 6.4.
- Boundary validation prevents future application code from recreating catalogue, metadata, or orientation gates with
  raw source contracts, while allowing the reviewed source-composition owners to implement those external contracts.

Focused Phase 6.3 findings:

- Source settings, source-home navigation, Entry WebView actions, and deep-link resolution are separate Feature-owned
  boundaries with structured missing, unsupported, absent, and failed outcomes. Partial source support remains valid.
- Each contribution declares its distinct UI, runtime, backup, persistence, maintenance, and adapter consequences;
  product integrations are not collapsed into an opaque generic-access consequence.
- Catalogue, feed, migration, extension, preferences, backup, Entry, WebView, and deep-link consumers no longer repeat
  raw source action casts. Build validation rejects reintroducing those casts in application/data/domain code.
- Kavita depends on the settings Feature. Suwayomi depends on an explicit tracker adapter relationship that composes
  separately owned settings, home URL, and image-client evidence with explicit blockers and unavailable reasons,
  without promoting that combination to a universal source capability.
- Deep-link resolution owns source discovery, persistence, and child lookup; the persisted returned Entry remains
  authoritative instead of being validated against descriptive source metadata.
- Source-home support no longer acts as a proxy for download participation. Manga download indexing uses installed
  non-local sources plus retained stubs, preventing an unrelated UI-navigation contract from authorizing downloads.
- Raw contracts remain only in source definitions/compatibility, the owning root Features, tests, and the separately
  assigned Manga child-WebView runtime for Phase 6.5.

Focused Phase 6.4.1 findings:

- Preview and Immersive provider presence continues to select installed type-owned runtime dispatch. Live source and
  preference evidence is declared on separate contextual integrations, so a disabled preference or unsupported source
  blocks only the affected product consequences rather than redefining type support.
- Preview source requirements are provider-owned metadata interpreted by the Preview Feature. The settings explanation
  is derived from that same metadata; Anime no longer implements a parallel availability gate in its processor.
- Immersive source presence, source opt-in, and descriptive declared-type compatibility have separate blockers.
  Declared types prune only source-level catalogue/feed surfaces, while actual returned Entry types remain authoritative.
- Related Entries consequences are contextual on installed `RelatedEntriesSource` support for the operation's origin
  type. Missing/unsupported sources remain structured results and no longer receive unconditional graph consequences.
- First-reading-child absence remains an operation result from request candidates and Child List selection. It is not
  promoted to an Entry-State or Selection capability.
- The unused Domain immersive-opt-in copy is removed. Boundary validation prevents application/data/domain code from
  reconstructing Preview, Immersive opt-in, or Related Entries gates from raw source contracts.

Focused Phase 6.4.2 findings:

- F04 context-free integrations now select only installed Download, Bulk Candidate, and Bookmark providers. Separate
  contextual integrations own the actual individual, bulk, bookmarked-bulk, and notification product consequences.
- Actual remote/local source access and actionable child/notification selection state are typed evidence with explicit
  blockers. They are not copied into type contributions or promoted to generic Source/Selection capabilities.
- The Feature resolves contextual graph state before returning the existing structured availability, action, and
  cancellation results, keeping UI and execution authorization on the same boundary.
- Empty target lists remain structured request results because no Entry type exists as a contextual subject. Empty
  media-specific candidate pools remain post-provider `NoCandidates` results rather than support declarations.

Focused Phase 6.4.3 findings:

- F05 Download provider presence remains the sole type-wide prerequisite and selects only installed downloader dispatch.
  A separate contextual integration owns policy, Library Update, and Entry-refresh consequences.
- New-child selection, active-profile enabled/unread-only values, favorite state, category-policy eligibility, and
  remaining candidates are typed contextual evidence owned by the relevant runtime concerns.
- The shared policy returns one internally consistent decision consumed by both scheduling paths and graph resolution.
  Contextual rejection is exposed as structured blockers instead of the former opaque `NoCandidates` result.
- Category and prior-consumption mechanics remain inside F05 shared policy. No per-type automatic-filter provider,
  support flag, content-type matrix, or generic Preferences/Profile capability is introduced.

Focused Phase 6.4.4 findings:

- F09 and F10 provider integrations now select only type applicability and media-specific dispatch. Their UI,
  state-transition, mutation-result, and lifecycle consequences live on contextual integrations.
- Consumption eligibility resolves current consumed/partial-progress state against the requested transition. Changed
  mutation results and marked-consumed lifecycle emission resolve independently after provider dispatch.
- Bookmark eligibility resolves the complete selection state, while concrete Entry mutation resolves the filtered
  changed-child state before persistence dispatch. Empty cross-Entry selections remain structured request results.
- Provider absence remains valid and no Entry-State/Selection capability, content-type matrix, mandatory operation, or
  type-specific product-action opt-in is introduced.

Focused Phase 6.4.5 findings:

- F06 context-free relationships now own event acceptance, type applicability, provider dispatch, and discovery of the
  Download-plus-Bookmark relationship without authorizing live cleanup policy.
- Marked-consumed cleanup, completion cleanup, download-ahead, per-owner category eligibility, physical cleanup, and the
  remove-bookmarked override resolve through separate contextual relationships.
- Actual owner Entry types authorize cleanup and Bookmark protection. The visible Entry type does not stand in for
  merged owners.
- Owner resolution, reading-order membership, download continuity, deduplication, and empty candidates remain operation
  results because they exist only during concrete event execution; no runtime-readiness capability was invented.

Focused Phase 6.4.6 findings:

- F13 remains provider-free and context-free participation is selected automatically for every contributed content
  type, including types with no interaction providers.
- Smart-update settings and the behavior contract remain context-free. Policy decisions, Library Update, and Stats move
  to one request-context relationship.
- Active-profile configuration, one-shot state, completion, consumption progress, started state, and release-window
  position are typed contextual evidence with blockers matching the existing structured skip reasons and precedence.
- Unknown progress evidence remains non-blocking. No Update Eligibility, Entry-State, Preferences, or release-window
  capability or type-specific opt-in is introduced.

Focused Phase 6.4.7 findings:

- Generic Library filtering and behavior-contract participation remain automatic for every composed type. Optional
  controls still derive independently from Progress, Bookmark, and outside-release-period providers.
- Filter configuration, aggregate Library state, and tracker evidence are typed contextual inputs for policy matching
  and active-filter consequences.
- A target failing a predicate is a normal filter result, not a blocked integration. Empty requests remain operation
  results because they have no content-type subject.
- Tracker authentication and declared type applicability are not absorbed into F14 and remain assigned to Phase 6.7.

Focused Phase 6.4.8 findings:

- F18 provider presence remains the sole applicability fact. Its member/profile IDs, group sets, child lists, and
  replacement values are operation payload and returned state, not contextual applicability evidence.
- Supported empty groups, identity filtering, and unchanged persistence remain successful results. They do not block
  the provider relationship or suppress its shared consequences.
- Live exclusion changes continue to reevaluate through the Feature-owned observation contract. Duplicating that state
  in graph evidence would create a generic registry of method arguments without exposing a missing integration.
- Contribution metadata and the default coordinator are now separated so graph ownership and runtime behavior remain
  discoverable independently.

Focused Phase 6.4.9 findings:

- F22 provider presence remains the sole base applicability fact; Continue and Bookmark relationships remain derived
  solely from their independent provider composition.
- Stored children, legacy activity time, media evidence, concrete Continue targets, and merged summaries are operation
  inputs/results. They change summary values without activating or blocking a graph relationship.
- Empty children, no next child, absent media progress, and unknown downstream state remain supported results. Empty
  merge input remains an invalid coordinator call rather than content-type inapplicability.
- Contribution metadata and the default coordinator are now separated so graph selection and summary calculation are
  independently discoverable.

Focused Phase 6.4.10 findings:

- F24 Open and Consumption participation remains derived independently from provider presence. Non-empty update
  children now contextually activate only the child-open and Mark Consumed consequences.
- Empty updates remain valid notifications targeting Entry Details. They block child-specific consequences without
  making the type or base notification relationship inapplicable.
- Download continues through F04 notification availability, so F24 does not duplicate source-access, empty-selection,
  or notification-size evidence and policy.
- Merged visible identity remains a same-type invariant; concrete child lists and descriptions remain operation data.

Focused Phase 6.4.11 findings:

- F11 provider presence remains the sole type-wide participation fact. Provider dispatch and later preparation/
  execution relationships remain context-free in this slice.
- Persisted Entry state and Library membership now contextually control availability, Entry actions, Browse source
  projection, and source-Entry selection through one F11-owned rule.
- Library selection additionally declares single-profile state. Mixed participating types remain valid, and each
  supported selected type resolves the same rule without a production type matrix.
- Empty selection remains a structured operation result because it has no content-type graph subject. Unsupported
  types have no contextual candidate and retain the existing structured unsupported result.
- Pair compatibility, current option availability, and execution-reference state remain separate F11 context work.

Focused Phase 6.4.12 findings:

- F11 target acceptance now declares source/target persistence, source Library membership, same profile/type, and
  distinct identity. Host inspection separately declares pair presence and optimistic identity stability.
- Provider absence remains ordinary inapplicability. Pair blockers preserve existing structured rejection precedence
  without changing type-wide Migration support.
- Child State option participation derives from Migration plus Consumption or Bookmarking. Category, notes, custom
  cover, and stored Downloads independently activate only their own options.
- Download provider presence remains context-free participation; no stored downloads blocks only the removal option.
- Host operational failures and opaque execution-reference/selected-option validation remain operation results or later
  F11 execution-context work rather than being folded into pair support.

Focused Phase 6.4.13 findings:

- F11 execution coordination remains context-free provider-derived participation. Live authoritative pair presence and
  captured Library authorization now resolve the actual execution consequence before and after target synchronization.
- Missing or changed authorization retains the existing `Conflict` result and blocks only the uncommitted operation.
- Unrecognized references lack a trustworthy graph subject; invalid options are request validation against the
  Feature-issued option snapshot. Neither becomes generic context.
- Replay, synchronization/inspection failures, transaction conflicts, cancellation, and consequence status remain
  operation outcomes. A committed replay intentionally returns its recorded result without current-state reevaluation.

Focused Phase 6.4.14 findings:

- Provider-free Merge coordination remains context-free and automatic for every composed type. The unconditional
  editor artifact is replaced by selection, authoritative-resolution, and membership contextual integrations.
- Mixed type/profile selection, missing or changed authoritative Entries, multiple existing groups, incomplete ordered
  membership, and insufficient expanded editor membership block only the current preparation/editor consequences.
- Empty selection remains a structured result because it has no content-type subject. Duplicate selection/preparation
  payloads and missing caller preparation remain request validation rather than support or applicability facts.
- Existing public results and rejection precedence are preserved. No Merge provider, type opt-in, content-type matrix,
  generic Selection/Membership capability, or application API is added.

Focused Phase 6.4.15 findings:

- Existing-group mutation now declares complete ordered membership and homogeneous member type. Missing or mixed live
  membership retains the existing `Conflict` outcome and blocks only that operation consequence.
- Library initialization, cover cleanup, and Download removal move out of unconditional base consequences. Each is
  activated only by the trusted workflow request that emits its durable consequence.
- Download ownership remains selected solely by the Download provider. Removal additionally requires concrete cleanup
  work, without creating a Merge opt-in or allowing Merge to authorize Download support.
- Opaque reference and editor-choice validation, missing-group idempotence, atomic transaction conflicts, operational
  failures, and consequence-delivery status remain operation outcomes. No speculative pre-transaction authority check
  weakens atomic host revalidation.

Focused Phase 6.4.16 findings:

- Migration replacement was the only remaining falsely unconditional F12 cross-feature consequence. It now derives
  from the real Migration provider, matching the already F11-only runtime call path.
- Candidate lookup, navigation, child ownership, Library grouping, metadata refresh, backup, profile move, lifecycle,
  and consequence status are shared Merge coordination rather than optional type support.
- Membership absence, empty candidates/status, partial caller Library populations, backup skips, profile-move selection,
  lifecycle idempotence, and host failures remain purpose-specific inputs/results. Copying them into graph context would
  not activate or block another relationship.
- Download and Migration remain independent provider-derived edges. No content-type list, type-specific Merge opt-in,
  generic runtime-state capability, or public API is added.

Focused Phase 6.4.17 findings:

- Every F01–F27 contribution without a dedicated Phase 6.4 slice was re-audited for unconditional state-dependent or
  cross-feature artifacts. Ordinary request/data outcomes remain purpose-specific Feature results.
- Progress, Playback Preferences, and Viewer Settings had the same false Migration implication found in F12: their own
  providers alone advertised migration copy. Each relationship now derives from its provider plus Migration.
- A type with Progress, Playback Preferences, or Viewer Settings but no Migration retains its backup/runtime behavior
  and reports migration transfer as inapplicable. Adding Migration activates the relationship automatically.
- The Progress test that enumerated declared consequence labels is removed. Focused tests now exercise base-provider and
  provider-plus-Migration behavior rather than restating capability truth.
- Phase 6.4 is complete without a type matrix, mandatory interaction, cross-feature opt-in, or global state capability.

Focused F11.0 findings:

- Provider-backed Migration preserves accepted Manga/Anime participation and valid Book absence without a type matrix.
- The five boundary findings are only the non-compiling subset; every already-compiling UI, search, execution,
  consequence, preference, test, and documentation surface now has a disposition in `features/F11-migration.md`.
- Browse currently admits unsupported Book Entries into a silent no-op, while public documentation still marks accepted
  Anime Migration behavior unavailable. Both are assigned to F11.
- The monolithic use case's ambient options, Manga branch, raw Merge access, swallowed failures, and caller-owned transfer
  pipeline are architecture obligations, not a compilation repair list.

Focused F11.1 findings:

- The application contract now covers availability, selection, mutation-free pair preparation, captured execution
  intent, explicit rejection/failure/conflict, and aggregate follow-up status without exposing an internal checklist.
- The only host port is an explicit-profile preparation inspection. Mutation, synchronization, transaction, and
  external-effect ports remain deliberately undefined until F11.2.
- Base Migration behavior is derived from provider presence. Consumption, Bookmarking, Progress, Playback Preferences,
  Viewer Settings, and Downloads are independently derived pairwise relationships; none becomes a mandatory provider
  for type validity.
- A synthetic provider proves that an unknown type receives all base consequences and that adding Progress activates
  only the applicable relationship without a production matrix.
- The boundary queue is 20 findings across seven production files: five previously visible generic/F12 findings plus 15
  F11-specific findings covering the legacy use case, consumers, ambient flags, duplicated support/selection gates, and
  concrete Manga authorization.
- Formatting, build-logic tests, API/SPI and all production type/root interaction checks pass. The boundary task fails on
  the intentional 20-item queue; FOSS compilation continues to report the already-exposed F11/F12 consumers plus
  unrelated current-branch application errors.

Focused F11.2 findings:

- Target synchronization is a non-atomic, active-profile operation whose chapter removal/insertion paths can suppress
  persistence failures. F11 needs a strict explicit-profile result; a partial sync is never an applied Migration.
- The primary transaction atomically owns F11 Entry/Library/category/normalized child state, prepared tracks, stable
  operation identity, durable consequence records, and F12 Merge replacement in Replace mode.
- Progress, playback preferences, viewer settings, Download removal, and custom-cover promotion occur after commit from
  immutable owner-produced payloads with durable at-least-once retry.
- Download owners must be captured before membership replacement. F08 must stop reporting `Performed` after unverified
  Manga/Anime filesystem deletion before F11 can acknowledge Download cleanup.
- Custom-cover bytes require pre-commit staging and idempotent post-commit promotion. A stable operation record replays a
  committed result after process death and rejects reuse of the same preparation with different intent.
- The accepted semantics and authoritative failure table are in decision `0021`; F11.2 changes documentation only and
  introduces no write host, compatibility implementation, consumer migration, or behavior change.

Focused F11.3 findings:

- The provider-selected shared coordinator now owns preparation, optimistic validation, strict synchronization, child
  matching, primary copy/replace state, stable replay, and structured results without a production type matrix.
- Strict synchronization pins Entry writes to the captured profile and verifies persistence that the ordinary chapter
  repositories may report best-effort. Partial synchronization never becomes an applied Migration.
- App-owned Entry/Library/category/child/tracking updates, operation identity, and prepared consequence rows commit in
  one transaction. Replace invokes F12 through its narrow Feature inside the same database transaction; rollback tests
  prove nested participant work cannot outlive an aborted outer transition.
- Migration operation/consequence tables are introduced by SQLDelight migration 38. F11.3 installs no external delivery
  handler and therefore does not pre-empt F11.4's owner-produced immutable payload work.
- The legacy orchestration use case and DI binding are gone. The boundary queue is now 10 findings across five files,
  all intentionally assigned to the F11.5 application-consumer migration.

Focused F11.4 findings:

- Progress, Playback Preferences, and Viewer Settings produce immutable target-bound payloads; retry restores captured
  values without rereading mutable source state.
- Viewer Settings owns provider-specific legacy flag normalization. Migration contains no production EntryType branch.
- Download cleanup captures pre-replacement owners and acknowledges only verified absence. Merge uses the same corrected
  completion result.
- Custom covers are staged before commit, promoted after commit, retained until acknowledgement, and covered by bounded
  orphan cleanup.
- Durable delivery, aggregate status/retry, and runtime retry warmup are installed. Provider absence skips only the
  owning optional relationship.
- The remaining boundary/compiler queue is still intentionally assigned to F11.5; no UI compatibility authority was
  introduced to make the application compile early.

Focused F11.5 findings:

- Entry, Library, Browse sources and Entries, automatic/manual search, configuration, dialogs, and batch execution now
  consume F11 availability, selection, preparation, and execution results.
- Explicit profile/Entry subjects survive navigation. Stored flags are presentation defaults captured as explicit
  options; they neither authorize Migration nor reconstruct its consequence pipeline.
- Unsupported Entries cannot enter a silent no-op flow. Only applied results advance workflows, while rejection,
  conflict, and operational failure remain visible.

Focused F11.6 findings:

- The obsolete capability facade, provider-backed dispatcher, composition field, and unused provider projection are
  removed. Migration provider bindings remain graph evidence only.
- Generic SPI enforcement now discovers top-level `Entry…Capability` properties as well as provider types; application
  code cannot bypass a Feature by importing either form.
- Shared and per-type interaction suites plus the complete boundary pass. The final census contains no production
  legacy authority, type matrix, ambient coordinator option read, or swallowed Migration cancellation.
- Public documentation now agrees with executable provider truth: Manga and Anime support source Migration; Book does
  not currently contribute it.

## Why the Plan Was Reset

The prior sequence built an evidence/reporting foundation, completed a Bookmarking/Downloads vertical slice, and then
began migrating production capabilities one group at a time. General feature discovery, relationship evaluation,
specialized obligations, contract selection, and projections were deferred.

That sequence produced an uncommitted hardcoded completion contract for Open, Continue, Bookmarking, Downloads, and Bulk
Downloads. It also repeated provider registrations as per-type capability assertions. Both mechanisms required a future
contributor to remember another list and therefore contradicted the manifesto. The contract and duplicate assertions were
removed rather than expanded.

The corrected rule is architecture first, production conformance second. Intermediate compile failures are acceptable
when new boundaries expose unported code. Compilation must be restored by migrating that code, not by adding a parallel
authority or weakening the architecture.

## Durable Decisions

- Content types and features contribute through owned, discoverable boundaries.
- A content type is valid with any subset of interaction providers; no interaction, including Open or Continue, is
  mandatory for architectural validity.
- Provider presence proves support and provider absence means unsupported without a separate absence declaration.
- Features own prerequisites, contextual inputs, shared consequences, specialized obligations, behavioral contracts,
  and projections.
- Generic assembly and evaluation contain no concrete type, capability, or feature allowlist.
- Tests verify architecture mechanics, shared behavior, and genuine media behavior; they do not restate declarations.
- The retired report and the retained Bookmarking/Downloads behavior are migration evidence, not protected architecture.

## Completed Historical Work

- [x] Phase 0 evidence inventory, consumer graph, discrepancy review, and product decisions
- [x] Phase 1 capability vocabulary, evidence composition, support outcomes, and deterministic report prototype
- [x] Phase 2 Bookmarking provider split and Bookmarking/Downloads learning slice
- [x] Initial download provider-authority migration committed in `e04b2481c`
- [x] Incorrect uncommitted completion contract removed
- [x] Per-type capability-label assertions introduced or retained for completion enforcement removed
- [x] Manifesto expanded with architecture-before-migration, valid partial type support, provider-backed applicability,
  testing, and compilation principles
- [x] Phase plan reordered around a general architecture kernel before production migration
- [x] Accepted decision `0006-architecture-before-migration.md` recorded
- [x] Architecture reset committed in `666487574`
- [x] Milestone 3.1 standalone generic contribution kernel implemented
- [x] Decision `0007-contribution-semantics.md` accepted
- [x] Milestone 3.1 committed in `d89e51693`
- [x] Milestone 3.2 generic discovery and deterministic graph assembly implemented
- [x] Decision `0008-contribution-discovery-and-assembly.md` accepted
- [x] Milestone 3.2 committed in `03d0b6422`
- [x] Milestone 3.3 generic evaluation and specialized obligations implemented
- [x] Decision `0009-evaluation-and-specialized-obligations.md` accepted
- [x] Milestone 3.3 committed in `0a578b784`
- [x] Milestone 3.4 generic contract, fixture, and projection selection implemented
- [x] Decision `0010-contract-fixture-and-projection-selection.md` accepted
- [x] Milestone 3.4 committed in `f1e66f671`
- [x] Milestone 3.5 dependency boundary and legacy authority cut implemented
- [x] Decision `0011-production-boundary-cut.md` accepted
- [x] Phase 3 completed and committed in `c795c505c`
- [x] Pre-Phase 4 repository census expanded beyond the interaction registry
- [x] Type (`T01`–`T27`), feature (`F01`–`F27`), and contextual/external (`C01`–`C24`) migration registers recorded
- [x] Direct type gates, parallel provider lists, tests, reporting, documentation, and audited non-migration boundaries
  classified in `migration-inventory.md`
- [x] Migration-readiness milestone committed in `7ab311608`
- [x] Upstream `upcoming` merged in `5e3f948b4`; working tree verified clean before Phase 4
- [x] Milestone 4.1 gives Manga, Anime, and Book one owned content-type contribution each
- [x] Open and Continue provider definitions live beside their SPI contracts
- [x] Open/Continue are declared once in each contribution; provider-owned installation derives operational dispatch
- [x] Provider-to-plugin `EntryType` identity is validated generically without a concrete type list
- [x] Graph content-type identity is derived from `EntryType` rather than repeated as a type-owned string
- [x] Decision `0012-entry-plugin-provider-identity.md` accepted
- [x] Milestone 4.1 committed as `(refactor): migrate entry provider identity`
- [x] Capability-owned bindings derive graph evidence and operational dispatch from one type-owned declaration
- [x] Open and Continue moved to the binding mechanism without changing partial-support semantics
- [x] Consumption bound for Manga, Anime, and Book
- [x] Bookmarking bound only for Manga, independently from its shared Consumption implementation object
- [x] Progress transfer bound for Manga, Anime, and Book
- [x] Playback-preference transfer bound only for Anime
- [x] Existing Anime consumption lifecycle event corrected to use the episodes whose state actually changed
- [x] Decision `0013-provider-binding-and-dispatch.md` accepted
- [x] Milestone 4.2.1 committed in `62c9741a1`
- [x] Core downloads initially separated from options, settings, bulk candidates, and automatic filtering
- [x] Download support contributed through capability-owned bindings for Manga, Anime, and optional Book construction
- [x] Anime alone contributes interactive options; Manga contributes each implemented download setting independently
- [x] Manga, Anime, and Book downloaders initially contributed bulk-candidate and automatic-filter providers independently
- [x] Default false/null/empty download sub-capability declarations removed from the core downloader contract
- [x] Decision `0014-download-provider-decomposition.md` accepted
- [x] Milestone 4.2.2 committed in `f4a6d153c`
- [x] Migration and Merge were split into independent provider contracts and bindings for audit; F12.3 later removed
  the empty Merge provider path
- [x] Manga and Anime retain the independent Migration binding; provider-free F12 now supplies shared Merge
- [x] Book's combined default-false capability processor removed without replacement absence declarations
- [x] Shared selection constraints retained as transitional feature policy rather than type provider behavior
- [x] Decision `0015-migration-and-merge-provider-boundary.md` accepted
- [x] Milestone 4.2.3 committed in `c046e1f8f`
- [x] Universal Update Eligibility policy deduplicated from three type processors
- [x] Child List and optional child-progress labels split into independent bindings
- [x] Manga-only operational child-group filtering contributed; Anime no-op provider removed
- [x] Manga/Book outside-release-period filtering contributed; Anime false provider removed
- [x] Manga/Anime Preview and Immersive implementations contributed with contextual conditions retained
- [x] Production type plugins no longer contain direct registry calls or custom installation overrides
- [x] Decision `0016-remaining-interaction-provider-boundaries.md` accepted
- [x] Milestone 4.2.4 committed in `c2ca736e4`
- [x] One runtime module per production type becomes the sole root type aggregation
- [x] Plugins, library progress, viewer settings, caches, warmups, and image components derive from runtime contributions
- [x] Separate library-progress list and public per-type installation paths removed
- [x] Runtime plugin/calculator identity validated against the owning type
- [x] Presentation vocabulary recorded as a Phase 5 projection obligation rather than mixed into runtime services
- [x] Decision `0017-owned-type-runtime-modules.md` accepted
- [x] Capability-owned installer callbacks and the `EntryInteractionRegistry` interface/implementation removed
- [x] Operational interaction facades derive typed provider maps from one generic binding index
- [x] Provider contracts and provider-backed dispatch split into cohesive interaction-family files
- [x] `EntryInteractionPlugin` and `EntryInteractionComposition` reduced to their actual ownership boundaries
- [x] Registry-shaped, per-capability duplicate tests replaced by generic composition invariants
- [x] Superseded registry fixtures removed without adding capability-label or current-type matrix assertions
- [x] Decision `0018-generic-provider-index.md` reviewed and accepted
- [x] Stale boundary-check allowlist replaced with discovery of the accepted public runtime-module bridge contract
- [x] `F01` Open owner, prerequisite, consequence, and complete consumer disposition recorded
- [x] Open UI/notification/deep-link dispatch migrated behind one graph-derived coordinator
- [x] Synthetic single-provider and valid-absence Open feature proofs added
- [x] F01 committed in `106fec52e`
- [x] Raw operational facades and their aggregate moved from exported API to provider SPI
- [x] Root composition stopped injecting raw facades and graph-evaluation objects
- [x] Application Entry-interactions compile classpath reduced to root plus app-facing API; SPI and Feature Graph absent
- [x] Boundary validation derives every public SPI type instead of naming current interaction facades
- [x] Application-facing API validation rejects raw `Entry*Interaction` contracts generically
- [x] Decision `0019-feature-facing-application-boundary.md` reviewed and accepted
- [x] Architecture Gate 5.0 committed in `83b2f93e7`
- [x] F02 Continue owner, prerequisite, result semantics, and complete consumer disposition recorded
- [x] Entry, Library, and History Continue surfaces migrated behind one graph-derived coordinator
- [x] Synthetic provider, no-next, and valid-absence Continue proofs added
- [x] F03 queue/runtime state, counts, controls, worker, and notification consequences migrated behind one feature
- [x] F04 individual, bulk, bookmarked-bulk, retry, and notification actions migrated behind one feature
- [x] F05 automatic-download policy and orchestration made shared for every core Download provider
- [x] Redundant per-type automatic-filter capability, dispatch, bindings, and generic domain policy exposure removed
- [x] F03–F05 application consumers reconciled together; no migrated operation retains raw provider access
- [x] F06 lifecycle events, cleanup, download-ahead, category policy, and derived Bookmark protection migrated behind one
  graph-selected feature
- [x] F07 download options and specialized setting visibility split into feature-owned contracts without a type map
- [x] F08 download maintenance routes source/title rename, cache invalidation, whole-Entry removal, and source purge through
  one feature-owned boundary
- [x] F06–F08 application consumers reconciled together; no application production source imports the raw Download facade
- [x] F09 Consumption applicability, shared transition policy, media mutation, application consumers, and F06 lifecycle
  emission migrated behind one feature
- [x] F10 Bookmark applicability, selection policy, mutation, Entry/Updates consumers, and derived Download relationships
  migrated without a support matrix
- [x] F13 Update Eligibility deduplicated into one provider-free shared feature for every composed type
- [x] F09/F10/F13 application consumers reconciled together; no application production source imports their raw facades
  or the deleted capability report/catalog
- [x] F15 Progress Transfer applicability, snapshot/restore/copy results, backup consumers, and migration copy moved behind
  one feature-owned boundary
- [x] F16 backup snapshot, restore, and migration copy consume provider-derived structured Feature results
- [x] F17 Child List ordering, display rows, missing-count results, ordered-child consumers, and optional independent
  Child Progress relationship migrated behind one feature
- [x] F15/F16/F17 application consumers reconciled together; no application production source imports their raw facades
- [x] F14 Library filter policy, active state, and capability-dependent control availability migrated behind one feature
- [x] F14 application consumers use neutral DTOs and no longer import raw Library-filter dispatch

Focused F01 validation:

- Build-logic tests pass after replacing the obsolete installer/plugin boundary allowlist with discovery of public
  functions returning `EntryTypeRuntimeModule` from owned type modules.
- `spotlessApply`, `spotlessCheck`, Feature Graph tests, Entry interactions API/SPI compilation, and Manga, Anime, and
  Book interaction compilation pass.
- Root Entry interactions compilation reaches only the previously recorded Download Lifecycle report/policy errors; it
  reports no Open migration error.

Focused Architecture Gate 5.0 validation:

- Build-logic tests pass, including rejection of direct Feature Graph/SPI dependencies, SPI export, and raw interaction
  source references.
- Entry-interactions API, SPI, Manga, Anime, and Book debug Kotlin compilation passes after the boundary move.
- The application's `fossCompileClasspath` contains the root Entry-interactions module and its API only; it contains
  neither SPI nor Feature Graph.
- Boundary validation intentionally fails on 24 application production files using 13 raw facade types. Open is absent
  from that list because F01 already consumes its feature contract.

Focused F02 validation:

- `spotlessApply`, Entry-interactions API/SPI compilation, and `git diff --check` pass.
- Focused synthetic Continue tests are present. Their Gradle task reaches the previously recorded Download Lifecycle
  report/policy compile failures before test execution; it reports no F02 production-source error.
- No application production source imports `EntryContinueInteraction`.
- Root compilation reaches only the previously recorded Download Lifecycle report/policy errors; it reports no Continue
  migration error.
- Full application compilation and a clean boundary check remain intentionally deferred while F11, F12, and F14–F27 raw consumers are
  inaccessible.

Focused F03–F05 validation:

- `spotlessApply`, Feature Graph tests, Entry-interactions API/SPI compilation, and Manga, Anime, and Book interaction
  compilation pass after combining the three isolated implementations.
- At that milestone, the combined boundary census reported 34 remaining raw application references, all assigned to
  F06–F27. F03-only
  screens and the shared Library, Updates, and Notification Receiver download paths no longer import raw download SPI.
- Root Entry-interactions compilation reaches only the known F06 lifecycle failures: deleted `EntryCapabilityReport`,
  deleted `EntryDownloadCapabilityPolicy`, and the resulting runtime factory inference error.
- Focused root feature tests are present but cannot execute until the independent F06 main source set compiles.

Focused F06–F08 validation:

- `spotlessApply`, Feature Graph tests, build-logic tests, SQLDelight migration verification, API/SPI and all production
  type/root interaction compilation pass after combining the three implementations.
- All 46 root Entry-interactions unit tests pass, including the graph-selected F06, F07, and F08 behavior contracts.
- The combined boundary census has 25 remaining raw application references, all assigned to F09–F27; application
  production code has no raw `EntryDownloadInteraction` reference.
- FOSS application compilation advances to the recorded F09–F27 migration queue and reports no F06–F08 symbol or
  dispatch error.

Focused F09/F10/F13 validation:

- `spotlessApply`, Feature Graph tests, build-logic tests, API/SPI and all production type/root interaction compilation,
  and all root Entry-interactions unit tests pass after combining the three isolated implementations.
- The boundary census has 16 remaining raw application references, all assigned to F11, F12, and F14–F27. Application
  production code has no raw Consumption, Bookmark, or Update Eligibility interaction and no capability report/catalog.
- FOSS application compilation advances to the recorded later-feature migration queue and reports no F09, F10, or F13
  symbol or dispatch error.

Focused F15/F16/F17 validation:

- `spotlessApply`, API/SPI and all production type/root interaction compilation, Feature Graph tests, build-logic tests,
  and all root Entry-interactions unit tests pass after combining the three implementations.
- The combined boundary census reports 8 expected later-feature violations and no raw Child List or Child Progress
  application reference; Progress and Playback Preferences are also absent from the app raw-facade census.
- FOSS compilation reports no F15, F16, or F17 symbol or dispatch error before stopping at later raw facades.
- Full Manga/Anime type-suite execution remains blocked by the pre-existing test harness calls that omit the required
  feature-contributor argument to `createEntryInteractions`; F17 does not conceal that architecture obligation with an
  empty contributor placeholder or production fallback.

Focused F14 validation:

- `spotlessApply`, API/SPI/root compilation, and all root Entry-interactions tests pass in the isolated worktree.
- The boundary census reports seven expected later-feature violations and no raw Library-filter application reference.
- FOSS compilation reports no F14 error before stopping at the expected F11/F12/F18–F20 raw-facade queue.

Focused F18 validation:

- `spotlessApply`, API/SPI/Manga/root production compilation, and all root Entry-interactions unit tests pass, including
  the synthetic Child Group Filtering contract.
- The contract proves valid provider absence, supported-empty state, strict dispatch, normalization/filtering,
  multi-member observation and mutation, profile-aware snapshot, and additive restore without a production type matrix.
- Application production code has no raw `EntryChildGroupFilterInteraction` reference. The boundary census reports 7
  expected remaining references assigned to F11, F12, F14, F19, and F20; FOSS compilation reports no F18 error before
  stopping at those raw-feature migration failures.
- The existing Manga type-test source still cannot compile because its pre-existing one-argument
  `createEntryInteractions` calls omit the now-required feature contributors. F18 adds no empty-contributor shim.

Focused F19 validation:

- Preview provider presence selects every shared surface and lifecycle consequence. Configuration, Child List, Open,
  and source-capability context remain independent relationships rather than a combined support label.
- The provider-derived settings projection contains no Manga/Anime UI switch. A provider declares contextual source
  requirements beside its settings, and generic presentation renders every discovered settings entry.
- Child-backed providers fail coordinator construction when Preview plus Child List cannot be selected; fixed-config
  and provider-less types remain valid.

Combined F14/F18/F19 validation:

- `spotlessApply`, Feature Graph and build-logic tests, API/SPI and every production type/root interaction compilation,
  all root Entry-interactions tests, and FOSS application compilation pass after conflict reconciliation.
- Application production and test code contain no raw Library Filter, Child Group Filter, or Preview interaction.
- The boundary census is exactly four later-feature references: three F11/F12 capability-facade consumers and one F20
  Immersive consumer.
- The settings boundary caught and rejected a hardcoded Manga/Anime Preview text map during integration. It was removed
  in favor of provider-declared context plus generic presentation; no allowlist exception was added.

Focused F20 validation:

- Immersive provider presence selects every common surface and lifecycle consequence. Source opt-in, Child List, Open,
  descriptive metadata, and resolved media remain independent contextual or derived relationships.
- Child-backed providers fail coordinator construction when Immersive plus Child List cannot be selected; entry-level
  and zero-preload providers remain valid.
- Catalogue/feed mode, per-entry long press, settings evidence, preload, loading, rendering, progress, and release use
  one application Feature boundary. Raw SPI access, silent release, and missing-provider preload fallback are removed.
- The synthetic contract proves structured provider absence, empty-runtime source closure, source unavailable, source
  opt-out, no reading child, media failure, metadata-only surface pruning, Open composition, strict lifecycle, and
  anonymous type participation.
- API/SPI, every production type/root interaction module, and focused F20 tests pass. FOSS compilation reports no F20
  error before stopping at the expected F11/F12 and unrelated application migration queue.
- The boundary census is exactly three later-feature references, all raw F11/F12 capability-facade consumers; no F20
  application reference remains.

## New Phase Sequence

- Phase 3: General relationship architecture
- Phase 4: Entry-type composition migration
- Phase 5: Feature integration migration
- Phase 6: Contextual and external integration
- Phase 7: Graph-selected contracts, reporting, and documentation
- Phase 8: Legacy removal, boundary enforcement, and build completion

Phase 3 milestones:

- 3.1: Contribution semantics and ownership
- 3.2: Discovery and graph assembly
- 3.3: Evaluation and obligations
- 3.4: Contract and projection selection
- 3.5: Dependency boundary and migration cut

No Manga, Anime, Book, Bookmarking, Downloads, or other product-specific branch may enter the generic Phase 3 kernel.
The phase uses anonymous synthetic contributions to prove unknown future participation.

Milestone 3.5 removed `EntryCapabilityCatalog`, `EntryCapabilityReport`, `supportsTypeWide`, legacy report assembly,
explicit absence compensation, and production report DI exposure rather than deprecating them behind a working facade.
Unported consumers now fail to compile and are recorded in `migration-obligations.md`. The old report does not survive
beside the graph.

## Current Working Tree Scope

- `entry-interactions:api` exports the lower-level `feature-graph` contract.
- Entry-type plugins must be owned graph contributors as well as operational registrars; no default or lambda
  compatibility contribution exists.
- Independent feature contributors remain separate composition inputs and are not forced through entry-type plugins.
- Entry-interaction composition now discovers, assembles, evaluates, and selects the graph and exposes all three results
  through runtime DI beside operational interactions.
- The catalog, legacy evidence/support model, report, `supportsTypeWide`, report assembly, report-driven download policy,
  report DI binding, and authority-focused unit tests are deleted.
- Anime and Book no longer declare explicit unsupported outcomes for missing providers.
- Remaining production/test references are owned in `migration-obligations.md`; they are compile failures rather than a
  working fallback authority.
- A complete anonymous acceptance test proves future complete, incomplete, and partial types through unchanged discovery,
  evaluation, consequence, obligation, contract, and projection paths.
- Accepted decision `0011-production-boundary-cut.md` and Phase 3.5 completion notes.
- Milestone 4.1 is committed in `0accb3667`.
- Milestone 4.2.1 is committed in `62c9741a1`.
- Milestone 4.2.2 is committed in `f4a6d153c`.
- Milestone 4.2.3 is committed in `c046e1f8f`.
- Milestone 4.2.4 is committed in `c2ca736e4`.
- Milestone 4.3 is committed in `c88ff5fe9`.
- Manga, Anime, and Book plugins expose their operational `EntryType` and one owned `ContentTypeContribution`.
- Open and Continue are the first graph-backed provider contracts. Neither is mandatory, no explicit unsupported
  declaration exists, and type plugins contain no separate Open/Continue registry call.
- `T01`–`T27` now use one owned contribution/runtime boundary or deliberate shared policy; F23 resolves the former
  `T23` presentation-projection obligation through optional type-owned providers.
- No dummy feature contribution or compatibility reachability path has been added. Production graph assembly contains
  only real migrated owners; F11 and F12 remain deliberately absent until their owning milestones.

## Pre-Phase 4 Census Findings

The interaction registry is not the complete participation boundary. The census found systems that can currently be
forgotten even when an interaction provider is added:

- a separate required-per-type library-progress calculator registry;
- parallel root lists for type plugins, runtime installers, warmups, viewer settings, media caches, and image components;
- viewer-setting provider discovery followed by a hardcoded provider-to-screen and settings-search map;
- media-cache providers followed by hardcoded keys, labels, launch auto-clear preferences, and startup wiring;
- manually enumerated profile preference ownership and legacy profile-key correction;
- source SDK capabilities with independently reconstructed consumers across browse, entry, feed, update, migration,
  download, and WebView behavior;
- tracker type applicability and tracker sub-capabilities with entry, library, sync, and dialog consequences;
- direct capability gates in backup, restore, migration, library-update notification routing, presentation, and settings;
- boundary-check allowlists that require another edit for each concrete type bridge; and
- curated settings navigation/search lists that can omit a contributed capability surface.

The complete proposed dispositions are in `migration-inventory.md`. The ledger also records the interaction-provider
sub-capabilities that would otherwise be lost by a mechanical processor migration, including download options/settings,
bulk pools, automatic filtering, migration/merge, child progress labels, child-group behavior, and library filters.

## Approved Boundary Classifications

Approved on 2026-07-18:

- All listed out-of-boundary systems participate in the application-wide graph through their real owners.
- Book's internal format-processor registry remains an internal media boundary unless a processor exposes a
  cross-feature consequence.
- Global non-feature settings navigation may remain curated; capability-owned settings screens and search participation
  must be contributed and selected.

## Last Validation

- `./gradlew --quiet spotlessApply :feature-graph:testDebugUnitTest :entry-interactions:api:compileDebugKotlin` passed.
- The complete synthetic test covers automatic future participation, shared artifact identity, missing specialized
  obligations, and a valid partial type without production vocabulary.
- `:entry-interactions:spi:compileDebugKotlin` now passes after download dispatch stopped consuming the deleted report.
- Search confirms no legacy catalog/report definition, explicit unsupported outcome, or production report DI binding
  remains.
- `git diff --check` passed.
- The pre-Phase 4 census inspected interaction processors and defaults, all production `EntryType` references in the
  audited app/domain/data/source/interaction modules, direct current-type branches, source API capability contracts,
  tracker capabilities, root composition, settings, notifications, backup/migration, profile ownership, caches, tests,
  documentation, and boundary enforcement.
- Census snapshot: 14 interaction processor categories, 76 production files with `EntryType` in the audited modules, 20
  production files outside type modules with direct current-type constants, 214 audited production/test and
  boundary-build-logic files with `EntryType`, one separate type-provider registry for library progress, and 16 source
  capability contracts or capability-bearing contracts.
- `git diff --check` passes for the migration-readiness documentation.
- Milestone 4.1 ran `./gradlew --quiet spotlessApply` successfully.
- During Milestones 4.1 and 4.2.1, `:entry-interactions:spi:compileDebugKotlin` reached only the five then-recorded
  deleted-report errors; no migrated provider contract introduced an earlier failure.
- Milestone 4.2.1 reran `spotlessApply`, the Feature Graph tests, and Entry interactions API compilation successfully.
- Milestone 4.2.2 passes `:entry-interactions:spi:compileDebugKotlin` plus Manga, Anime, and Book debug Kotlin
  compilation. The former five deleted-report errors are removed; the remaining bookmarked-bulk dispatch rule is a
  recorded Phase 5 feature-consumer migration, not a replacement authority.
- Milestone 4.2.3 reruns formatting and passes SPI plus Manga, Anime, and Book debug Kotlin compilation.
- Milestone 4.2.4 reruns formatting and passes SPI plus Manga, Anime, and Book debug Kotlin compilation.
- Milestone 4.3 passes API, SPI, and Manga, Anime, and Book debug Kotlin compilation after formatting.
- Before `F01`, `:entry-interactions:compileDebugKotlin` failed at both the intentionally unported Download Lifecycle
  report input and missing feature-contributor installation.
- `F01` installs the Open feature contribution and consumes the selected shared-consequence edges through one
  coordinator. The same lower-boundary validation passes; root compilation now fails only at the independent Download
  Lifecycle report/policy migration.

## Manifesto Comparison

- The hardcoded capability list was removed, not repaired.
- The revised plan builds generic discovery, relationships, obligations, contracts, and projections before more consumer
  migration.
- The old catalog/report authority is retired at the Phase 3.5 boundary cut, not deferred until final cleanup.
- All interactions are provider-backed; current ubiquity does not make an operation mandatory for future types.
- A missing provider creates no obligation. Obligations begin only after a feature's declared prerequisites are
  satisfied.
- Tests no longer repeat current provider facts as completeness declarations.
- The decisive Phase 3 proof uses unknown synthetic contributions and rejects concrete product branches.
- Phase 3.1 uses actual provider and executable artifact objects rather than building another descriptive support report.
- The new kernel does not depend on or preserve the old catalog/report API.
- Contributor installation is an environment concern; the graph kernel contains no concrete contributor registry.
- Every supplied provider must connect to a feature-owned relationship, preventing capabilities from silently bypassing
  the consequence graph.
- Assembly records relationships without prematurely evaluating support or obligations.
- Evaluation applies every discovered relationship without a product matrix or feature-specific branch.
- Missing prerequisites remain ordinary unsupported behavior; only missing specialized work after applicability creates
  an obligation.
- Context is retained as conditional instead of being guessed or flattened into type-wide support.
- Per-type applicability edges reference one feature-owned shared consequence, preserving single-gate coordinators.
- Contracts and projections are selected from the same applicable relationships without another support matrix.
- Fixture obligations are type-owned only when a contract declares genuine media-specific validation input.
- Missing shared projections are feature-owned and aggregated across affected subjects instead of duplicated per type.
- No contract, fixture, or projection kind is globally mandatory.
- The old catalog/report authority was deleted rather than wrapped or deprecated.
- Type plugins and independent feature contributors meet only at environment composition; neither side enumerates the
  other.
- No empty type contribution or feature-contributor fallback hides unported work.
- Known compile failures are mapped to Phases 4, 5, and 7 with responsible owners.
- The lower generic and provider-contract boundaries compile even though production does not.
- Compilation pressure cannot justify dual authorities or fallback architecture.
- The full architecture is app-wide and not limited to Bookmarking or Downloads.
- Migration readiness no longer equates “all interaction processors found” with “all participation found.”
- Already-generic call sites remain in the migration register because generic code can still sit outside graph-selected
  ownership and be forgotten by a future contribution.
- Settings, workers, notifications, navigation, backup, migration, profile preferences, caches, external capabilities,
  tests, and documentation are explicit feature consequences rather than a later informal audit.
- The migration inventory is a temporary control surface with completion dispositions, not a new runtime allowlist or
  capability authority.
- Milestone 4.1 uses operational provider instances as graph evidence and derives their dispatch registration; it does
  not repeat support or participation in a matrix or second type-module call.
- The common plugin/provider boundary permits any provider subset and contains no Open/Continue requirement.
- Concrete type identity remains type-owned, while validation contains no Manga, Anime, or Book branch.
- `F01` derives Open applicability from selected shared-consequence edges and matched provider objects; it does not
  introduce a type list, support label, or Open completion matrix.
- Application consumers receive one feature-owned Open gate. The low-level dispatcher remains an implementation detail
  of that coordinator; type-owned Continue processors may still compose their own type-owned Open processor directly.
  UI and notification availability cannot reconstruct support.
- A synthetic unknown contribution with only an Open provider receives the shared consequence automatically, while an
  empty contribution remains valid and produces no action or obligation.
- Every current Open consumer in the migration census has an explicit disposition. Entry-detail navigation and Continue
  are retained outside F01 because they are different behavior, not silently omitted Open consequences.
- F02 derives Continue applicability from the selected Continue consequence and matched provider object without a type
  list or mandatory-provider rule.
- Entry, Library, and History use the same application feature gate; per-item presentation does not reconstruct support
  from unread counts, progress state, or the current production type set.
- `Inapplicable` and `NoNext` are separate outcomes, so valid provider absence is not mislabeled as provider state.
- A synthetic unknown contribution receives next-child dispatch automatically; absence remains valid and produces no
  specialized obligation.
- Architecture Gate 5.0 makes feature ownership an enforceable dependency direction rather than a naming preference.
- Application consumers can see feature contracts and shared models but cannot see raw provider dispatch, the
  `EntryInteractions` aggregate, Feature Graph evaluation, or selected artifacts.
- SPI declarations are discovered by the boundary checker, so a future provider/dispatch type is protected without
  adding its name to an enforcement list.
- Remaining raw application boundary failures are exactly the three F11/F12 `EntryCapabilityInteraction` consumers.
  F27 is complete without introducing a raw interaction-facade path. Re-exporting SPI, restoring
  raw DI, or moving provider facades back into the API would contradict the manifesto rather than fix compilation.
- Unported processor families remain visible obligations instead of being mislabeled through broad processor wrappers.
- Compilation remains subordinate to the architecture: no dummy feature or legacy plugin fallback was added to satisfy
  reachability or old tests.
- Milestone 4.2.1 eliminates the remaining two-call synchronization risk: a capability binding is the single source for
  both graph evidence and operational dispatch.
- A shared implementation object can bind multiple independent capabilities without creating a combined capability.
- Current support is represented exactly: every type binds Consumption and Progress, only Manga binds Bookmarking, and
  only Anime binds Playback-preference transfer.
- Milestone 4.2.2 does not turn the old downloader into one broad capability. Each behavior that a future type may add
  independently has its own provider contract and binding.
- Core downloads imply neither options, settings, nor bulk candidates. They do receive shared automatic-download policy
  through F05; Book remains valid when its optional downloader is omitted.
- Each Manga download setting is an individual graph-visible provider claim, not an enum set treated as another support
  authority.
- Shared bulk selection and automatic-download orchestration are feature-owned in F04/F05; type providers expose only
  the genuinely media-specific operations those features need.
- Milestone 4.2.3 removes the combined Migration/Merge boolean holder instead of wrapping it as one broad provider.
- Migration and Merge bindings are independent even though each current supporting type uses one object to carry both
  compatibility markers.
- The marker contracts state compatibility with shared feature behavior; they do not misrepresent the marker object as
  the owner of migration or merge operations.
- Book contributes neither marker. Its valid type contribution is not made incomplete by those absences.
- Milestone 4.2.4 does not manufacture an Update Eligibility capability from identical type implementations; the common
  rule applies to future types without another opt-in.
- Child-list presence no longer implies child-progress labels, even though all three current implementations bind both.
- False and no-op filtering processors are removed rather than wrapped as capability evidence.
- Preview and Immersive preserve real provider implementations while source, preference, media-resolution, and runtime
  conditions remain contextual.
- Every production type plugin now declares providers only through bindings; the transitional registry is no longer
  called directly by a type module.
- Milestone 4.3 colocates plugins, library progress, settings, caches, warmups, and image components in one installed
  type-runtime contribution collection. F22 later removes the still-required Library Progress field and second provider
  path entirely in favor of optional plugin bindings.
- Presentation vocabulary is not treated as a runtime service or support fact; its remaining concrete-type map is
  visible Phase 5 projection work.
- Milestone 4.4 removes the last two-step installation mechanism. A binding is now consumed directly by both the graph
  and the generic operational provider index; no per-capability registration method remains.
- The generic provider index is not a feature coordinator. Existing facade policies exposed by compilation remain Phase
  5 obligations and must move to feature-owned integrations rather than into the index.
- F03 declares queue, status, count, worker, initialization, and notification consequences once and exposes one
  app-facing runtime contract; adding a Download provider requires no queue/UI/worker registration.
- F04 derives bookmarked bulk behavior from Download, Bulk Candidate, and Bookmark providers. A future Bookmark provider
  activates that consequence without editing the downloader or application presentation.
- F05 removes the artificial automatic-filter opt-in after proving every current implementation was the same shared
  policy. A future Download provider receives automatic-download policy without type-specific delegation.
- Application consumers use feature contracts for every F03–F05 operation. Remaining raw download access is explicitly
  eliminated by F06–F08; provider dispatch remains internal to the root feature coordinators.
- F06 derives bookmark protection from Download plus Bookmarking and keeps event delivery non-optional. F07 derives
  options and each specialized setting independently from provider presence. F08 covers every inventoried maintenance
  event, including cleanup before source/database purge.
- F09 centralizes consumption applicability and shared state-transition policy while type providers retain only genuine
  persistence differences; its F06 lifecycle consequence is emitted by the feature coordinator.
- F10 makes Bookmark provider presence the only support fact and derives both its application actions and existing
  Download intersections without another opt-in or type list.
- F13 removes the artificial universal provider declaration: Update Eligibility is an always-applicable shared feature
  for every composed type and owns one policy across update and Stats consumers.
- F15 distinguishes available empty progress from provider absence, routes all portable transfer through one feature,
  and leaves live media persistence, child labels, migration policy, and library summaries with their real owners.
- F16 removes concrete Anime authorization from playback-preference backup creation. Backup snapshot, restore, and
  migration copy now share one provider-derived Feature boundary with explicit data-absence, provider-absence, and
  type-mismatch outcomes.
- F17 makes Child List provider presence the sole list applicability fact, derives optional labels from Child List plus
  Child Progress, and removes the app-owned missing-count type gate without introducing fallback behavior.
- F14 applies one generic filter policy to every composed type, derives Bookmark-control and outside-release-period
  applicability independently, and removes the raw application support query without absorbing F13 or contextual
  Library navigation.
- F18 makes Child Group Filtering provider presence the sole applicability fact. Shared state, multi-member
  observation, live filtering, persistence, backup, and controls are Feature-owned without Manga authorization.
- F19 makes Preview provider presence the sole type-wide support fact, derives configuration, Child List, Open, and
  contextual source requirements independently, and removes raw Preview access and settings type enumeration.
- F20 makes Immersive provider presence the sole type-wide support fact, composes public source opt-in and runtime media
  context, derives Child List and Open relationships, and routes every application authorization and lifecycle path
  through one Feature boundary.
- F21 gives every composed type shared Related Entries orchestration while leaving `RelatedEntriesSource` and source
  orientation as contextual external truth. The Feature owns source availability, fetch/persistence, live Library state,
  and Entry/dialog consequences without filtering mixed authoritative returned types or coupling details navigation to
  F01 Open.
- F22 makes Library Progress provider presence optional and authoritative, removes the mandatory calculator/runtime
  path, and leaves unsupported Library entries structurally visible with explicit inapplicability.
- F22 owns common counts and merged summaries, derives Continue and Bookmark consequences from F02/F10, and carries
  unknown summary state truthfully through F14, sort, badges, Stats, and F13 update eligibility.
- F23 discovers type-owned vocabulary and imagery through ordinary plugin contributions, removes the application type
  map, keeps generic provenance explicit, and leaves every behavioral action with its owning Feature.
- F24 gives every composed type shared Library-update notification routing and rendering, derives child Open, Mark
  Consumed, and Download independently from F01/F09/F04, and consumes F23 only for vocabulary.
- F24 removes the Manga fallback for Book. A frozen compatibility adapter preserves shipped Manga/Anime Android
  identities; Book and future routes derive from content identity and are collision-validated.
- F25 moves Viewer Settings into the ordinary optional plugin/provider graph, validates exact app-owned screen
  projections, and derives hub/search, overrides, reset, backup, and migration consequences from the same provider
  definitions; F27 owns profile preference participation.
- F26 makes optional Media Cache provider presence sufficient for discovery, settings, preferences, manual clearing,
  launch clearing, size refresh, and structured error consequences.
- F26 removes the root cache-bucket list, central cache-key map, hardcoded settings/startup policy, and dedicated
  current-type preference holder. Type modules retain only genuine cache access and owned descriptors.
- F27 replaces the manually instantiated preference-owner checklist and tracker-ID loop with owner factories registered
  at their real DI/runtime installation boundary.
- F27 binds Entry runtime preference construction to a registered installer, discovers static and dynamic keys,
  rejects ambiguous ownership and late registration, and keeps named legacy corrections separate from support truth.

Focused Phase 6.5.0 findings:

- Image pages, subtitles, live media, selected values, viewer/player state, and format/protection descriptors are not one
  capability family. They remain operation data interpreted by the Feature or type-owned media mechanic that gives each
  value product meaning.
- Generic cover fetching is the remaining `EntryImageSource` application leak. It will receive a purpose-specific
  source image-network Feature result; Manga reader/download/Immersive mechanics and the already explicit tracker
  adapter keep their separate owners.
- F07 already owns Anime download-option availability. Anime player and downloader subtitle resolution remain
  type-owned because they have distinct execution and failure semantics; no global Playback or Subtitle capability is
  added.
- The existing WebView Feature will own canonical child resolution and reader action consequences. The public
  `ChapterWebViewSource` contract and legacy adapter remain external authorities while the Manga reader stops casting it.
- Manga auto-scroll and Anime picture-in-picture are live renderer/platform mechanics inside their type-owned runtimes;
  their cross-application settings surfaces are already selected by F25 and their preference ownership by F27.
- The Book processor registry is a legitimate nested media authority shared by Book reader and downloader. Processor
  installation remains one list, optional settings remain independently provider-derived, and neither processor nor
  format support becomes an Entry-wide capability.
- The documented census globs now use `**/src/main/**`; the former single-segment form omitted nested modules and could
  have allowed future media consumers to escape the closure audit.

Focused Phase 6.5.1 findings:

- One Cover Network Feature declares source installation and `EntryImageSource` presence as contextual evidence and
  exposes call-factory and request-header consequences through one structured resolution.
- Both generic cover factories now resolve that Feature lazily. Missing and unsupported sources retain the generic
  client fallback, while source property failures remain failures; cache/file/content paths do not perform resolution.
- `EntryCoverFetcher` no longer imports `EntryImageSource` or `SourceManager`, and the boundary task rejects future raw
  image-source use in application/data/domain code.
- Tracker adaptation remains separately graph-owned, and Manga reader/download/Immersive code remains type-owned media
  mechanics. No global image/media capability or reusable source facade was added.
- Root Entry-interactions behavior tests, the boundary task, build-logic tests, formatting, and diff checks pass. FOSS
  compilation reaches only the recorded unrelated Anime debug-launch callback and More-tab coroutine errors.

Focused Phase 6.5.2 findings:

- Entry and child WebView support are separate contextual integrations under one Feature. A source may implement either
  supported subset without becoming invalid or creating a content-type declaration.
- Child controls are media-host-specific, so Manga supplies one specialized host adapter through the ordinary plugin.
  Phase 7.1.6 clarified that this adapter is an applicability prerequisite: types without a media host remain valid and
  do not claim child WebView consequences.
- Child resolution returns canonical URL and source identity together, with missing, unsupported, and failed outcomes.
  WebView headers retain their existing separately resolved consequence and are not an eager child-URL prerequisite.
- Manga reader actions and Android Assist now follow one active-child result. Child transitions clear stale state, and
  late asynchronous resolution cannot install a previous child's URL.
- Raw `ChapterWebViewSource` use remains only in the public source contract, legacy compatibility adapter, and owning
  root Feature. Boundary enforcement rejects future application or type-module bypasses.
- Focused WebView behavior covers available, missing, unsupported, failed, and missing-host outcomes without restating
  a type support matrix. Root tests, Manga compilation, boundary validation, build-logic tests, formatting, and diff
  checks pass.

Focused Phase 6.5.3 findings:

- F03, F07, and F20 already divide media consequences by product owner. Manga image resolution remains inside Manga
  reader/downloader/Immersive mechanics, while Anime options, downloader, and player retain distinct subtitle failure
  semantics; no generic media facade or type-wide Playback capability is warranted.
- Immersive load failure was already structured, but renderer construction still escaped through the Feature as an
  exception. It now returns `Available` or `Failed`, and the application renders failure through its existing retry UI.
- Raw `SubtitleSource` is now rejected in application/data/domain consumers. The boundary deliberately permits Anime
  type-owned player and download mechanics, public source contracts, compatibility code, and tests.
- Book runtime installs one `BookProcessorRegistry` and injects it into reader and downloader mechanics. Format support
  stays inside that registry; Viewer Settings remains independently optional and does not determine processor validity.
- The closure declares no type support labels, media capability, completion list, or new specialized obligation. It
  narrows application authority and makes an existing owner operation failure explicit.
- Root and Manga/Anime/Book interaction tests, boundary and build-logic tests, formatting, and diff checks pass. FOSS
  compilation reaches only the recorded unrelated Anime debug-launch callback and More-tab coroutine errors.

Focused Phase 6.5.4 findings:

- Nested production probes found every raw media contract and returned media-shape cast inside its recorded Feature,
  type-owned runtime, external contract, or compatibility owner. No unclassified `C07`, `C08`, `C12`, `C20`, `C22`, or
  `C23` consumer remains for Phase 6.5.
- Generic presentation modules were missing from the raw source-action boundary despite being application-facing. They
  contain no current bypass; the guard now includes both modules and a regression proves image/subtitle contracts are
  rejected there.
- F03, F07, F20, and F25 documents now record their media-context limits. The atlas and inventory record the executable
  boundary and closure without introducing a media capability, support matrix, or completion list.
- Boundary and build-logic tests pass after the reconciliation change.

Focused Phase 6.6.0 findings:

- `SyncEntryWithSource` is the sole refresh mechanics implementation. Its direct callers are Entry, Library Update,
  metadata update, migration, Immersive, Deep Link, and root migration hosting; none implements separate source refresh
  semantics.
- One Source Refresh Feature will structure source availability and operation outcomes while keeping incremental fetch,
  empty-list safety, number recognition, persistence, progress rekeying, metadata hooks, and fetch intervals inside the
  existing domain coordinator.
- Source Refresh has no content-type provider. Installed source context authorizes a request; Local remains a valid
  installed source, and retained stub metadata is non-executable absence rather than a content-type validity claim.
- F11, F13/Library Update, F20, and Deep Link will declare their own refresh relationships. Source Refresh will not keep
  a consumer registry or type matrix. Entry and details-only metadata refresh are its base product consequences.
- F24 owns the Library queue-size warning derived from `UnmeteredSource`; Manga's separate downloader warning stays
  inside Manga F03 mechanics because it governs that downloader's queue execution.
- Migration is split into six architecture-first slices, starting with the Feature and graph contract before any caller
  migration or compilation cleanup.

Focused Phase 6.6.1 findings:

- `EntrySourceRefreshFeature` exposes one structured request/result boundary and is installed through root composition.
  No application caller has been migrated and no parallel refresh mechanics were added.
- An unconditional integration automatically selects every contributed type. Installed-source presence resolves
  contextually; the selected behavior contract and consequences require no provider or type registry. Retained stub
  metadata is not modeled as executable refresh state because the authoritative lookup exposes it as absence.
- `SyncEntryWithSource` remains the only details/children, incremental/empty-list, number-recognition, persistence,
  progress-rekey, metadata-hook, and fetch-interval implementation.
- Refresh always uses strict persistence against `entry.profileId`; the Feature resolves title-update policy for that
  exact profile rather than borrowing active-profile state from an application caller.
- A source disappearing after an available snapshot is an operation failure. Source-unavailable preflight, no children,
  general operation failure, and cancellation therefore retain consistent and distinct semantics.
- API/root compilation, focused behavior tests, formatting, and boundary validation pass. FOSS compilation reaches only
  the recorded unrelated Anime debug-launch callback and More-tab coroutine errors.

Focused Phase 6.6.2 findings:

- Entry and details-only metadata refresh consume structured Source Refresh results; merged Entry refresh preserves
  sequential stop-on-first-failure behavior and automatic-download handoff.
- F20 owns children-only refresh for child-backed Immersive providers. Source absence and no children map to existing
  contextual states; operation failure remains retryable. Entry-level providers remain unaffected.
- Deep Link owns refresh used to recover a missing canonical child and keeps refresh details behind its existing public
  resolution result.
- A build boundary now rejects raw `SyncEntryWithSource` consumption without a temporary migration allowlist. Its
  remaining findings identify only the deliberately deferred F11 migration and F13/Library Update owners.
- Focused root and build-logic tests pass. FOSS compilation reaches only the recorded unrelated Anime debug-launch
  callback and More-tab coroutine errors.

Focused Phase 6.6.3 findings:

- Automatic target search, optional details completion, and explicit target selection now invoke an F11-owned refresh
  operation with migration-specific structured results; the application owns no refresh interpretation outside F11.
- Mandatory execution refresh moved from the application host into `DefaultEntryMigrationFeature`, after replay and
  live-authorization checks and before execution-state inspection.
- The target refreshed during execution comes from the explicit-profile host inspection. Source Refresh therefore uses
  strict persistence against the target's exact profile without a host callback or ambient title preference lookup.
- Replay and changed authorization continue to bypass refresh, any refresh failure prevents the primary transition,
  and cancellation propagates.
- The raw-sync boundary now reports exactly one deferred owner: F13/Library Update. Focused F11 tests and formatting
  pass; FOSS compilation reaches only the two recorded unrelated application errors.

Focused Phase 6.6.4 findings:

- A provider-less Library Update Refresh Feature is selected for every runtime-contributed type and consumes Source
  Refresh after F13 eligibility without adding a type opt-in or making F13 execute source operations.
- The worker supplies metadata and fetch-window evidence and receives library-specific structured outcomes. Source
  grouping, concurrency, progress, counters, notification collection, and batch completion remain application-owned.
- Only successful inserted children reach the existing F05 batch and F24 notification collection. Source absence,
  no-children, and operation failures retain their existing failure/reporting behavior.
- Application production code has no raw `SyncEntryWithSource` consumer. The generic boundary is green without a
  migration allowlist.
- Formatting and focused Library Update Refresh behavior tests pass. FOSS compilation reaches only the two recorded
  unrelated application errors.

Focused Phase 6.6.5 findings:

- F24 now owns the Library queue-warning threshold and `UnmeteredSource` interpretation for every contributed type.
  The Android notifier renders only a structured required/not-required result.
- Existing source grouping, strict greater-than threshold, missing-source treatment, notification rendering, timeout,
  and help destination are preserved for both Library and metadata update callers.
- Manga downloader metering remains untouched inside type-owned F03 queue mechanics.
- Application production code cannot inspect `UnmeteredSource`; focused build validation enforces the F24 boundary
  without blocking source contracts, root Feature policy, or type-owned mechanics.

Focused Phase 6.6.6 findings:

- The production census finds one C15 mechanics owner, one root Source Refresh boundary, no raw application sync
  consumer, and no unclassified refresh caller.
- Build validation now rejects direct interpretation of empty-list, incremental-list, and number-recognition source
  contracts outside SDK/source compatibility/implementation code and `SyncEntryWithSource`.
- Current C16 runtime authority is `entry.UnmeteredSource`, interpreted only by F24 and Manga F03. The unused legacy
  source-api marker is recorded as an explicit C24 compatibility obligation for 6.8.
- Installed-source absence, Local behavior, Entry/child state, metadata/fetch-window preferences, and explicit profiles
  are owned by their individual consequences; no global context capability or support matrix was introduced.
- The raw synchronization, source-mechanics, and generic metering boundaries pass without migration allowlists.

Focused Phase 6.7.0 findings:

- Tracking is an external integration system, not a content-type provider. `TrackerManager` remains its single registry
  and each tracker remains authoritative for type applicability, authentication, operations, and sub-capabilities.
- One split application-facing Tracking Feature will compose every product consequence. One application host is the
  only bridge to raw tracker contracts; it is composition infrastructure and cannot become a second consumer API.
- Entry availability/session, dialog operations, automatic binding/sync, F11 preparation, F14 evidence, Library score
  sorting, Stats, settings, backup validation, presentation, and documentation are all assigned migration owners.
- The settings screen currently hardcodes ordinary services and separately discovers enhanced services. It must derive
  account rows from the authoritative registration so adding a tracker does not require a second UI-list edit.
- Tracker implementations, registry construction, credential storage, the single host adapter, and concrete OAuth
  callback parsing remain owner-local. No UI, worker, Library/Stats model, or backup policy qualifies for raw access.
- `EntryTracker`/`LegacyEntryTrackerAdapter` and `TrackMediaType` have no production consumers. They are recorded for
  removal or compatibility disposition during 6.7.7 rather than used as the new boundary.
- Tests that merely repeat a built-in tracker's declared Entry types are not completeness checks; reconciliation
  replaces them with unknown-type/tracker Feature behavior and keeps type-specific tests only for real tracker behavior.
- Architecture-first slices are 6.7.1 boundary/host, 6.7.2 Entry session, 6.7.3 operations, 6.7.4 automatic sync,
  6.7.5 accounts/settings/backup, 6.7.6 Library/Stats, and 6.7.7 reconciliation.

Focused Phase 6.7.1 findings:

- One injected `EntryTrackingFeature` now exposes registered availability and reactive authenticated Entry sessions
  through neutral descriptors/results. It exports no tracker registry, implementation, or raw contract object.
- One app `EntryTrackingHost` extracts registered type support, tracker presentation/sub-capabilities, login state,
  source acceptance, and persisted tracks. Root policy alone interprets those facts.
- Registry, availability/session/operations, automatic binding, synchronization, Library filters/scores, Stats,
  accounts, backup diagnostics, presentation, and documentation all enter graph discovery before consumer migration.
- A provider-less BOOK contribution becomes trackable solely from a synthetic external BOOK tracker. Logging out keeps
  declared availability and blocks only the reactive session.
- Boundary validation derives all public host-package declarations and rejects both the host and its snapshot models as
  application APIs outside the owned host/root/composition structure.
- Existing raw tracker consumers remain intentionally visible without a temporary file allowlist. The raw-tracker cut
  is enforced only after their assigned 6.7.2–6.7.6 migrations.
- Source organization is split across Tracking API models, host contracts, artifacts, context declaration/resolution,
  coordinator, contributor, app host, and mirrored behavior tests; no module-root file accumulation was introduced.

Focused Phase 6.7.2 findings:

- Entry action availability now comes from registered tracker applicability through `EntryTrackingFeature`; the live
  session determines whether the action opens account settings or the tracker dialog.
- Entry badge count and dialog rows consume the same reactive authenticated, type-compatible, source-compatible
  session, removing two independent registry/source filtering paths.
- Dialog presentation receives neutral service identity, status labels, score formatting, date/privacy support, and
  automatic-binding evidence. The former raw tracker-bearing `TrackItem` application model is removed.
- The shared logo primitive accepts neutral presentation evidence. Its raw tracker overload remains temporarily for
  account settings, which is assigned to 6.7.5.
- Dialog operations still resolve raw tracker services and are the exact next migration scope in 6.7.3. They were not
  hidden behind a compatibility facade or temporary boundary allowlist.
- Focused Tracking Feature tests, Entry-interaction boundary validation, formatting, and FOSS compilation pass.

Focused Phase 6.7.3 findings:

- One operations facet on `EntryTrackingFeature` now owns refresh, search, manual/automatic registration, mutations,
  unregistration, and remote deletion; it is not a separately injected or registered behavior authority.
- Every command resolves current registration, Entry type, authentication, source acceptance, existing track state, and
  relevant tracker-owned sub-capability before host dispatch.
- Search candidates and mutation intents are neutral. Raw tracker lookup, casts, search records, refresh mechanics, and
  local deletion are confined to the application Tracking host.
- All Entry dialog operation screens consume the Feature. The former 836-line dialog file is split into cohesive home,
  status, progress, score, date, search, removal, and feedback files.
- Remote deletion is attempted before local removal, and local removal still occurs after remote failure. The structured
  result preserves that partial failure for existing UI logging instead of running two uncoordinated coroutines.
- Background synchronization, settings/account/backup, Library/Stats, and the temporary settings logo adapter remain
  visible assignments for 6.7.4–6.7.6; no migration allowlist was introduced.
- Focused Feature/operation behavior tests, formatting, boundary validation, build-logic tests, and FOSS compilation
  pass.

Focused Phase 6.7.4 findings:

- One automatic-behavior facet on `EntryTrackingFeature` owns enhanced binding, progress inspection/synchronization,
  remote-progress reconciliation, and Migration track preparation; it is not separately injected or registered.
- Catalogue, History, Entry, Merge, reader, and delayed worker paths submit Entries to the Feature instead of selecting
  trackers or invoking raw synchronization interactors.
- Registration and refresh return bound/refreshed track facts to the root Feature, which explicitly invokes
  reconciliation. Lower-level interactors no longer trigger that cross-feature consequence implicitly.
- F11 asks Tracking to prepare target rows after live authorization and persists them in its existing optimistic
  transaction. Tracker-specific local transformation moved out of the Migration host and composition callback.
- The 6.7.1 census documented F11 preparation but omitted it from executable Tracking integrations. This milestone adds
  that unconditional relationship and updates discovery proof from seven to eight relationship groups.
- Focused Tracking, Migration, and Merge behavior tests and formatting pass. FOSS compilation reaches only two
  unrelated `HEAD` source issues in the anime debug launcher and profile shortcut UI; this milestone introduces no
  remaining compiler diagnostics.

Focused Phase 6.7.5 findings:

- One account facet on `EntryTrackingFeature` exposes current/reactive neutral accounts, structured login/logout, and
  missing-login diagnostics; it is not separately injected or registered.
- The settings screen no longer hardcodes eight ordinary services or casts the registry for enhanced services. Every
  registered tracker supplies a row through tracker-owned login and account-presentation metadata.
- Credential identity, external authorization initiation, passive enhanced login, installed-source availability,
  display username, login state, and existing row order are preserved through the host projection.
- Stored credentials are fetched only after selecting a credential account and do not enter the observable account
  snapshot. Settings preference/widget/logo presentation no longer receives raw tracker objects.
- Backup validation resolves referenced service IDs through Tracking and no longer queries `TrackerManager`.
- OAuth callback decoding remains in `TrackLoginActivity` as reviewed tracker-owned platform mechanics; it neither
  decides Entry support nor constructs account rows.
- Focused Tracking tests, all Entry-interactions tests, formatting, and boundary validation pass. FOSS compilation still
  reaches only the unrelated unchanged `HEAD` issues in the anime debug launcher and profile shortcut UI.

Focused Phase 6.7.6 findings:

- One collection facet on `EntryTrackingFeature` exposes reactive authenticated services, per-Entry service membership,
  normalized scores, scored-state evidence, and score-applicable Entry types; it is not separately injected.
- The app host combines the authoritative logged-in registry with persisted tracks. Logged-out and removed services are
  excluded before neutral evidence reaches consumers.
- Library settings derive tracker filter rows from Feature accounts. `LibraryScreenModel` supplies membership to F14
  and uses normalized evidence for ordering without importing trackers, raw tracks, or score conversion.
- Stats supplies current library Entry IDs and receives one summary preserving tracked-title count, per-Entry mean score,
  overall mean score, and authenticated-service count semantics.
- Synthetic behavior tests cover projection and aggregation without asserting the built-in tracker/type declaration
  matrix.
- Focused Tracking tests, all Entry-interactions tests, formatting, and boundary validation pass. FOSS compilation still
  reaches only the unrelated unchanged `HEAD` issues in the anime debug launcher and profile shortcut UI.

Focused Phase 6.7.7 findings:

- The complete production census finds no raw tracker import outside tracker ownership, lower-level tracker mechanics,
  the single application Tracking host, or root composition.
- Package-derived boundary validation rejects raw tracker imports and fully qualified access without naming consumers,
  implementations, or content types. Host declarations remain separately inaccessible to application consumers.
- The public Feature now projects `EntryTrackingRecord`; persisted `EntryTrack` remains private host/Migration evidence
  and cannot leak back into the public Tracking API.
- Unused `EntryTracker`, `LegacyEntryTrackerAdapter`, `EntryTrackSearch`, `TrackMediaType`, and aggregate support helper
  are removed. Stable service-ID coverage remains, while duplicated built-in type assertions are removed.
- Neutral previews no longer instantiate tracker implementations. `DummyTracker` now lives beside tracker tests, and
  OAuth callback activities live under tracker-owned protocol structure instead of requiring a UI exception.
- Existing synthetic BOOK/future-service tests cover automatic participation and contextual absence without a current
  type matrix. Tracking operation, automation, account, collection, and graph tests remain behavioral.
- `C18`, `C19`, and Tracking's assigned `C20`/`C22` relationships are closed with no unclassified production consumer.

Focused Phase 6.8 findings:

- Bundled Local now advertises Manga through current descriptive `SourceMetadata`; its returned Entry type remains the
  runtime authority.
- The legacy Manga adapter translates the old unmetered marker into the current source contract while preserving its
  exact catalogue, preferences, WebView, image, home, and related-entry interface shape.
- Legacy adapter inspection and async-filter compatibility operations live in `source-compat`. Application code cannot
  import the concrete adapter or interpret the legacy metering marker.
- Backup tracking restore and download-option selection backup/restore no longer contain Manga/Anime authorization
  gates. They follow actual stored data, so future tracker and F07 provider participation requires no backup edit.
- Manga viewer-bit and page-progress conversion remains named wire compatibility. Raw image/subtitle resolution,
  Local/stub state, Entry/selection/preferences, and platform/renderer conditions retain their already assigned Feature,
  compatibility, or type-owned mechanics disposition.
- Boundary validation replaces directory-wide exemptions with exact reviewed composition/compatibility files. New
  source, backup, migration, tracking, or presentation files cannot silently inherit type-gate authority.
- The repeated inventory probes find no deleted capability authority and no unclassified `C01`–`C24` production
  consumer.
- Formatting, compatibility tests, the Local build, Entry-interactions tests, build-logic boundary tests, and boundary
  validation pass. Focused app testing reaches only the same unchanged Anime debug-launcher and `MoreTab` compile
  failures recorded before this milestone.

Focused Phase 7.0 preparation findings:

- Production behavioral contracts are currently identifier/fixture declarations, not executable validators. Static
  selection also drops matched providers and adapters, so feature-by-feature test migration cannot begin on the current
  artifact shape.
- Context resolution has no contract/projection selection path. Static conditional candidates must not be reported or
  tested as unconditional support; feature-owned validation scenarios must resolve typed evidence first.
- No production feature supplies a developer-report or documentation projection. The current content-type reference is
  therefore still handwritten product documentation rather than a verified graph projection.
- Decision `0023` proposes separate production contract definitions and validation-only verifier contributions,
  discovered without a central suite list. Missing verifiers and contextual scenarios are feature-owner obligations;
  only explicitly requested media fixtures are content-type-owner obligations.
- Phase 7 is split into architecture, production contracts, declaration-test removal, developer reporting,
  documentation projection, and repository-validation reconciliation. No product migration starts before the generic
  unknown-contribution proof passes.

Focused application compilation-baseline findings:

- The Anime debug launcher now explicitly discards the Boolean result returned by the Open Feature when adapting it to
  a `Unit` UI callback. Applicability and dispatch remain owned by the Feature.
- The More-tab profile shortcut again imports the coroutine `launch` extension that its suspend profile operation
  requires. No download or profile behavior changed.
- Formatting and `:app:compileFossKotlin` pass. The previously recorded compilation exceptions are closed before
  contract-execution architecture work begins.

Focused Phase 7.0 contract-execution findings:

- Runtime `feature-graph` selections now retain matched providers, supplied specialized adapters, selected fixtures, and
  resolved context evidence. Contextual artifacts can be selected only from an exact applicable context evaluation.
- The new `feature-validation` module owns verifier/scenario discovery, planning, and execution. Runtime application
  modules do not depend on it, and validators contain no JUnit/Kotest coupling.
- Feature-owned verifiers and applicable-context scenarios are discovered through the validation classpath. Duplicate,
  foreign-owner, and unreachable bindings fail generically; there is no contract, Feature, or content-type suite list.
- Missing verifiers and applicable-context scenarios are Feature-owner obligations. Missing declared media fixtures and
  delayed specialized adapters remain obligations of the affected content-type owner. Both categories enter one plan
  issue collection and one validation success result; provider absence creates neither.
- A verifier receives typed access only to graph-selected providers, adapters, fixtures, and evidence. Execution returns
  structured pass, failure, or crash results rather than relying on a particular test framework.
- Anonymous proofs cover unknown future types, automatic multi-type enrollment, ordinary unsupported types, contextual
  applicability, delayed media obligations, classpath discovery, ownership rejection, and deterministic execution.
- Formatting, Feature Graph tests, Feature Validation tests, Entry Interactions tests, and FOSS application compilation
  pass.

Focused Phase 7.1 census findings:

- Thirty-seven production Features have owner behavior suites. Fifteen Features currently declare 18 contracts; 22
  Features declare none. Four declarations are contextual, fourteen static, and no production verifier, scenario, or
  contract fixture exists.
- Existing marker declarations are not complete coverage. Base contracts for Library Filtering/Progress, Notifications,
  Preview, Immersive, Merge, Migration, Update Eligibility, Viewer Settings, and Tracking omit already-tested optional
  or contextual relationships.
- Six suites inspect selected contract identifiers/subjects without executing them. These assertions are removed only
  after their behavior enters discovered verifiers; surrounding behavior and compatibility tests remain.
- The production validation host must consume the runtime `EntryInteractionComposition` and actual type-module
  contributions. Synthetic Manga/Anime/Book provider lists would recreate a second support authority.
- Verifier binding must change from copied Feature/contract identifiers to the exact production contract definition
  before production migration. Module-local service metadata remains installation, while graph applicability selects
  subjects.
- Preview, Immersive, and possibly WebView are fixture candidates. A typed media fixture is added only when an actual
  production verifier cannot execute from selected providers/adapters and Feature-owned context evidence.
- Contract migration is split into the validation host, fundamental providers, Downloads, Library/media, Source/context,
  workflows/tracking, and final reconciliation. Reporting and documentation remain later sequential workstreams.

Focused Phase 7.1.0 validation-host findings:

- Contract verifier and scenario references now retain the exact `FeatureBehaviorContract` object. Equality is
  identity-based, so recreating a definition with the same Feature and artifact identifiers cannot satisfy the binding.
- Runtime installation and validation now share one production type-module boundary and one production Feature
  contributor boundary. These lists compose owners; they do not encode type support or contract applicability.
- The validation-only host builds Manga, Anime, and Book through their production runtime modules with controlled host
  services, then validates the resulting `EntryInteractionComposition`. It does not synthesize provider lists.
- The unmodified production composition exposes every currently declared definition as a missing-verifier obligation,
  exposes missing contextual scenarios, schedules no execution, and returns one unsuccessful aggregate result.
- `feature-validation` is a test-only dependency of `entry-interactions`; it remains absent from application runtime
  dependencies. No production verifier or fixture was added.
- Formatting, Feature Graph tests, Feature Validation tests, Entry Interactions tests, and FOSS application compilation
  pass.

Focused Phase 7.1.1 fundamental-contract findings:

- The production graph now declares 32 exact contracts across 23 Features. Sixteen fundamental definitions have
  discovered verifiers, including six applicable contextual scenario groups.
- Open, Continue, Consumption, Bookmarking, Progress Transfer, Playback Preferences Transfer, Child List, Child Group
  Filtering, Type Presentation, and Update Eligibility now execute shared Feature behavior for every graph-selected
  production subject.
- Provider selection enrolls a production subject, while the contract executes the shared coordinator against a
  recording operational boundary. Type-owned processors, repositories, readers, and media mechanics remain covered by
  their owner suites rather than being reconstructed in shared contracts.
- Consumption and Bookmarking contextual contracts execute the same shared coordinator after applicable state-change
  scenarios resolve. Update Eligibility executes both its unconditional policy and its contextual eligible decision.
- Validation contributors are installed through module-local service metadata. No central contract suite, type matrix,
  or per-type fixture was added.
- The declaration-only Update Eligibility contract/type assertion was removed. Focused policy, structured absence, and
  type-owned behavior tests remain.
- Contract validation files mirror their production feature groups and are split by ownership; no catch-all verifier
  file was introduced.
- Formatting, Feature Graph tests, Feature Validation tests, Entry Interactions tests, and FOSS application compilation
  pass.

Focused Phase 7.1.2 Download-contract findings:

- The production graph now declares 56 exact contracts across 29 Features. All 24 Download-owned definitions have
  discovered executable verifiers, including eleven applicable contextual scenario groups.
- Download Runtime, Actions, Automatic Downloads, Lifecycle, Configuration, and Maintenance execute their shared
  coordinators against graph-selected production subjects and recording operational boundaries.
- Download Actions derives individual, bulk, bookmarked-bulk, and notification availability from the exact provider
  combinations and operation context already owned by the Feature. No type list or action-support matrix was added.
- Download Lifecycle owns cleanup, completion, download-ahead, category policy, and Bookmark protection. Download
  Maintenance remains a Download-provider relationship; it does not duplicate Bookmark cooperation.
- Download Configuration validates options and each independently provided setting through the shared configuration
  Features. A type may provide any subset without becoming invalid.
- Notification rendering/job behavior and media-specific downloader, store, cache, and transfer mechanics remain in
  their focused owner suites. Contract execution does not absorb or simulate those implementation details.
- Validation contributors and support files mirror the Download production boundaries; no combined Download verifier
  or central content-type fixture was introduced.

Focused Phase 7.1.3 Library, Settings, and Media contract findings:

- The production graph now declares 77 exact contracts across 29 Features. Sixty-nine definitions have executable
  verifiers, including 23 applicable contextual scenario groups.
- Library Filtering derives generic participation, policy execution, Progress controls, Bookmark controls, and
  release-period controls independently. Library Progress separately derives Continue targets and Bookmark summaries.
- Library Update Notifications derive presentation, Open destinations, Consumption actions, and Download actions from
  their respective relationships. Empty-child blockers remain contextual instead of changing notification
  participation. Library Update Refresh verifies its source-refresh handoff independently.
- Preview and Immersive contracts execute shared coordinators with recording operational processors. Their child-backed
  contracts receive graph-selected Child List providers; no type enumeration or media fixture was introduced.
- Viewer Settings validates provider projections and its independently selected Migration relationship. Media Cache
  validates discovery, settings, and clearing through a recording artifact without touching production caches.
- The declaration-only Library Filtering, Library Progress, and Media Cache assertions were removed. Their focused
  behavior, structured absence, failure, and compatibility tests remain.
- Preview, Immersive, and Viewer Settings now separate graph contributions from coordinator implementations instead of
  accumulating both responsibilities in oversized Feature files.

Focused Phase 7.1.4 Source and External-Context contract findings:

- The production graph now declares 87 exact contracts across 36 Features. Eighty-one definitions have executable
  verifiers, including 35 applicable contextual scenario groups.
- Catalogue description, catalogue availability, and Latest availability are independently contracted projections of
  source evidence. They do not become Entry-type capabilities or require a source to implement every public contract.
- Related Entries, Cover Network, Source Settings, Source Home, Source Refresh, Deep Link, and Tracker Source Adapter
  execute their shared coordinators from applicable external context without enumerating content types.
- Entry and child WebView are separate contracts. Child WebView validation consumes the graph-selected specialized host
  adapter only for participating media hosts; it neither supplies a synthetic adapter nor makes hosting universal.
- Source SDK compatibility, source implementations, network/media failures after applicability, and legacy adapter ABI
  remain in their existing owner tests. No media fixture or source-support matrix was introduced.
- Validation contributors mirror their production catalogue, child, and source ownership; no combined external-context
  verifier or central type fixture was introduced.

Focused Phase 7.1.5 Workflow and Tracking contract findings:

- The production graph now declares 116 exact contracts across 36 Features. All 116 definitions have discovered
  executable verifiers, including 57 applicable contextual scenario groups.
- Merge independently contracts its universal workflow, Download ownership, Migration replacement, preparation
  authority/membership, existing-group mutation, and conditional initialization/cleanup consequences.
- Migration independently contracts provider participation, source/selection/pair/execution context, every optional
  state projection, and its Consumption, Bookmark, Progress, Playback Preferences, Viewer Settings, and Download
  relationships. An `anyOf` relationship can inspect whichever optional providers graph selection supplied without
  requiring an absent alternative.
- Tracking contracts its external registry, availability/session context, automatic binding, synchronization,
  migration preparation, and Library/Stats evidence. External tracker registration remains the authority; no tracking
  capability was added to content-type plugins.
- Declaration-only Merge, Migration, and Tracking assertions were removed. Durable transactions, consequence replay,
  backup conversion, source operations, and storage compatibility remain in focused owner tests.
- The production plan has no missing verifier or scenario. Its only remaining issues are the previously exposed
  child-WebView specialized host obligations for Anime and Book, which Phase 7.1.6 must reconcile explicitly.

Focused Phase 7.1.6 Contract Reconciliation findings:

- The census baseline contained 36 production Feature contributions, not 37; the earlier count and corresponding
  no-contract count were corrected without adding a completion list or changing runtime participation.
- Specialized adapters now have two explicit graph roles. A specialized prerequisite makes participation optional and
  its absence ordinarily inapplicable; a specialized requirement still becomes an owned obligation after the other
  prerequisites establish applicability. Decision `0024` records when each role is valid.
- Child WebView uses Manga's real reader-host contribution as a specialized prerequisite. Anime and Book remain valid
  without claiming media-host behavior, and an applicable Manga source context still selects the shared child WebView
  contract and consequences.
- Production validation is now a successful gate: all 116 exact definitions have one discovered verifier, all 57
  contextual scenario groups cover their statically compatible subjects, and every selected execution passes.
- Manga and Anime provider behavior tests reuse the production Feature composition boundary. Their synthetic Features,
  copied capability enumeration, and synthetic specialized-requirement reconstruction were removed; type-owned media
  behavior remains covered.
- Validation-kernel coverage proves unresolved plan issues make the aggregate unsuccessful. Remaining declaration-only
  contract assertions were not found.
- The migration-inventory probes found no deleted capability authority or new unclassified product gate. Feature-graph,
  feature-validation, root/Anime/Manga interaction tests, boundary enforcement, build-logic tests, formatting, and FOSS
  application compilation pass.

Focused Phase 7.2 Declaration-Test Removal and Boundary Enforcement findings:

- The remaining Manga and Anime tests that only asserted current Continue and Download plugin registration are
  removed. Provider absence remains valid; type-owned media, progress, child-list, compatibility, failure, and
  coordinator behavior tests remain.
- Validation contributors must operate on graph-selected subjects and cannot name Manga, Anime, or Book. This prevents
  the validation layer from becoming a current-type support matrix while leaving concrete types available to genuine
  owner behavior and compatibility tests.
- Ordinary Entry-interaction tests cannot inspect contract declarations or invoke artifact selection as a substitute
  for behavior. Central contract-suite maps and validation-host feature/contract switches are rejected.
- The production validation test must execute the single evaluated validation host; direct planning, selection, or
  runner calls are rejected there. Feature-owned contributors and service installation remain discovery mechanisms,
  not completeness lists.
- Formatting, Feature Graph, Feature Validation, root/Anime/Manga interaction tests, build-logic tests, repository
  boundary validation, and FOSS application compilation pass.

Focused Phase 7.3 Developer Reporting findings:

- `feature-validation` owns a neutral report snapshot because scenario and contract execution results are
  validation-only facts. Runtime application modules do not depend on the reporting model and receive no graph query
  API.
- The report derives discovered content types and providers plus all 357 production integration evaluations across 36
  Features. Each integration retains prerequisites, matched providers, specialized work, context inputs, possible
  blockers, consequences, contracts, projections, execution outcomes, and responsible owners.
- Contextual integrations remain labeled conditional even when an applicable validation scenario passes. The renderer
  explicitly identifies scenario results as samples rather than type-wide support.
- Contextual artifact selection now retains missing projection obligations instead of discarding them after selecting a
  contract. A projection obligation still appears only when its Feature declared the requirement and applicable context
  establishes the relationship.
- `generateEntryFeatureReport` evaluates the exact production composition through the shared validation host, writes
  `entry-interactions/build/reports/entry-features/developer-report.txt`, and fails after rendering if validation or
  obligations are unresolved. A clean task run produces a deterministic 3,090-line report with zero current
  obligations.
- Formatting, Feature Graph and Feature Validation tests, all Entry-interactions tests, build-logic tests, boundary
  validation, clean/root report-task execution, and FOSS application compilation pass.

Focused Phase 7.4 Feature-Owned Projection findings:

- Review deferred all production exclusions so that the current migration assesses every Feature before deciding that
  any fact is outside this reference. All 36 production Features currently contribute an implemented projection.
  Explicit justified exclusions remain valid architecture, and the completeness gate still has no Feature or
  content-type list.
- Forty public comparison rows and two derived Download notes are owned by the Feature relationships that establish
  them. Selected projections receive matched providers, specialized adapters, and resolved context; they do not
  reconstruct support from Manga, Anime, or Book.
- Conditional source relationships may contribute only a source-dependent cell from their discovered conditional
  state. They do not need an invented source snapshot and cannot claim unconditional support. External registrations
  still require authoritative resolved evidence because they describe current type-wide product facts.
- The existing missing-child-gap row exposed a real authority mismatch: every Child List provider also implemented the
  display path even though only Manga supplied missing-gap behavior. Missing-gap behavior is now its own optional
  provider relationship, while the shared Child List Feature builds the ordinary display for types without it.
- Local-source, legacy-extension, and tracker availability remain external registration facts. Contextual projection
  relationships consume evidence from those owners; source metadata and validation scenarios do not become Entry
  support declarations.
- A build-only documentation module plans rows, notes, statuses, and owner-attributed issues from graph selection. Its
  generic tests prove that unknown future types, Features, providers, and contextual evidence participate without a
  current-type matrix.
- Formatting, boundary and build-logic tests, root Entry-interactions tests, documentation-planner tests, Local-source
  Android and legacy source-compat compilation, and FOSS application compilation pass.

Focused Phase 7.4 Production Projection Binding findings:

- The production interaction validation environment is now an Android test fixture shared by its existing validation
  suites and the application-owned documentation host. Production graph composition is not copied into the
  documentation path.
- The application-owned host derives external evidence from `LOCAL_SOURCE_SUPPORTED_ENTRY_TYPES`,
  `LEGACY_MANGA_SOURCE_SUPPORTED_ENTRY_TYPES`, and the actual `TrackerManager` registrations. It contains no separate
  content-type or capability matrix.
- The reusable host returns the neutral documentation plan that the renderer task will consume. Its production test
  requires a complete plan without asserting individual rows, cells, or current content-type support declarations.
- The application unit-test compilation baseline also exposed and restores the accidentally removed `io.mockk.Runs`
  import from the earlier Entry Consumption migration; no test behavior changes.
- Formatting, production-plan execution, test-fixture compilation, and FOSS unit-test compilation pass.

Focused Phase 7.4 Generated Reference findings:

- `generateContentTypeReference` replaces only the marked factual region in
  `docs/features/content-type-reference.md`; `verifyContentTypeReference` independently fails when that checked-in
  region differs from the exact production projection. Handwritten introduction and explanatory prose remain outside
  the markers.
- The renderer discovers and sorts content types by stable ID, renders unavailable cells from absent selected
  relationships, and emits Feature-owned rows and derived notes by their declared section and order. It contains no
  Feature, row, or content-type registry.
- The checked-in reference now contains all 40 current projected rows and both derived Download notes across all 36
  participating production Features. In particular, Anime migration is now shown from runtime truth instead of the
  stale handwritten value.
- Generic renderer tests cover unknown future content types, deterministic ordering, status rendering, Markdown
  escaping, incomplete-plan rejection, and exact marker replacement without asserting current product support.
- Formatting, renderer tests, production generation, independent checked-in verification, FOSS unit-test compilation,
  and Entry-interaction boundary validation pass.

Focused Phase 7.4 Source SDK Consumer Coverage findings:

- Context inputs now carry neutral owner-defined metadata. Source-owned inputs must classify each consuming Feature
  integration as reading one or more public SDK contracts or as a justified non-contract runtime fact; a future
  integration that reuses an input without exact classification becomes an owner-attributed planning issue.
- The production graph discovers 20 contextual consumers across 11 public source SDK contracts. Consumer identity is
  rendered from the exact Feature, integration, and evidence-input IDs rather than from a handwritten consumer list.
- The verifier separately requires every contextually consumed contract to remain described in the handwritten SDK
  documentation after removing the generated region. Contracts used only by source lifecycle or media implementations
  remain normal contract documentation and are not given synthetic graph relationships.
- `SourceMetadata` is classified only for Catalogue description and Immersive contextual pruning. The generated prose
  explicitly rejects interpreting it as Entry behavior support, and no content-type cells are derived from SDK
  metadata.
- A combined catalogue evidence object exposed the need for relationship-level precision. Contract classifications can
  therefore be scoped to exact integrations; Availability and Latest no longer falsely appear to consume orientation
  or descriptive type metadata.
- `generateSourceSdkConsumerCoverage` replaces only the marked coverage table, while
  `verifySourceSdkConsumerCoverage` checks both graph coverage and handwritten public contract presence without
  rewriting the document.
- Formatting, Feature Graph tests, generic planner/renderer tests, production generation, independent source SDK and
  content-reference verification, FOSS unit-test compilation, and boundary validation pass.

Focused Phase 7.5 Repository Validation Wiring findings:

- Root `verifyEntryFeatureArchitecture` composes static boundary enforcement, Feature Graph and Feature Validation
  infrastructure tests, generic documentation tests, the exact production contract/reporting suite, deterministic
  report generation, and checked-in production documentation verification.
- Root `verifyEntryFeatureDocumentation` is the narrower projection gate used by the documentation workflow. It catches
  stale documentation-only changes even when workflow path filters intentionally skip the application build.
- Neither aggregate is attached to formatting or `testFossUnitTest`. Their existing responsibilities remain intact,
  and explicit workflow steps give architecture and documentation failures their own category.
- The aggregate names architectural validation surfaces only. It contains no content-type, Feature, capability,
  contract, verifier, projection, row, or source-consumer registry; participation within each surface remains
  contribution-discovered.
- The complete gate passes, produces the current 3,413-line developer report with no unresolved work, and independently
  verifies both checked-in production projections.

Focused Phase 7.5 Register Reconciliation findings:

- Every migration-inventory validation surface now has an explicit final owner and repository-validation route. The
  reconciliation record describes validation channels, not a Feature or support allowlist.
- Generic composition and every shared relationship run through the architecture gate. App menu/selection,
  presentation, grouping, notification, and backup compatibility tests remain in `testFossUnitTest`; genuine media,
  storage, wire, and post-applicability failures remain focused owner tests.
- Type Presentation is the behavioral vocabulary authority. Source indicators remain projections of external
  repository/source metadata, and Library grouping remains structural enum behavior; neither is treated as Entry
  Feature support.
- Focused app reconciliation found and corrected two stale test expectations. Updates now mocks consumed state as
  non-partial, matching the production status model. Notification boundary coverage now accepts the modern
  `ACTION_OPEN_CHILD` route and its legacy merge-aware fallback. Production code is unchanged.
- The focused Download menu, Updates, source indicator, Library grouping, notification action/boundary, backup wire,
  and restore suites pass together after correction.

Focused Phase 7.5 Unknown-Contribution Acceptance findings:

- One anonymous future Audio-like contribution supplies a previously unknown capability and Feature with independent
  static and contextual relationships. It uses normal content-type, Feature, validation, scenario, and projection owner
  contributions; no production type or Feature is named.
- Static and contextual exact contract definitions both execute successfully. The contextual scenario supplies source
  evidence while the developer report continues to label the relationship conditional rather than promoting it to
  type-wide support.
- The same graph produces complete content-reference rows for the unknown type and discovers its contextual source SDK
  contract consumer. Rendering succeeds for the developer report, content-type reference, and SDK coverage table.
- Static boundary enforcement rejected the first filename because ordinary tests cannot inspect contract declarations.
  The proof was renamed to declare its contract-validation role; the enforcement rule and its allowlists are unchanged.
- The focused acceptance test and the complete `verifyEntryFeatureArchitecture` repository gate pass.
- Manifesto alignment is complete for Phase 7: provider absence remains valid, artifact channels remain optional,
  scenarios remain conditional evidence, and no central completion list participates in execution or projection.

Focused Phase 8 Exit-Gate findings:

- The full JDK 21 pre-release gate passes: `spotlessCheck`, `verifyEntryFeatureArchitecture`, `verifyLegacySourceAbi`,
  `testFossUnitTest`, and `verifySqlDelightMigration`.
- The telemetry/updater release assembly and extension runtime ABI verification pass under JDK 21.
- Production documentation verification, both SDK Dokka publications, and the VitePress build pass with Node 24 and
  pnpm 10.
- Selection action label tests no longer encode the current Manga, Anime, and Book vocabulary as a matrix. They inject
  `EntryTypePresentationFeature` and prove only generic versus contributed projection behavior.
- The stale tracking boundary test now follows the score, progress, and status selector owners. Its migration exposed
  and corrected a real race by capturing each user selection before non-cancellable dispatch.
- The corrections consume existing Feature ownership and preserve user intent; they add no provider requirement,
  content-type branch, support declaration, Feature allowlist, or architecture exception.
- The Phase 8 census and every manifesto rejection rule were reviewed. Provider absence remains valid, downstream
  participation remains contribution-discovered, and compilation succeeds because consumers follow the architecture.

## Exact Next Action After Review

Review and commit the Phase 8 exit-gate corrections and completion record. No implementation phase remains after this
commit.
