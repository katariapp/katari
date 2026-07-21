# Entry Tracking

Status: architecture established; Entry session, operations, automatic binding, and synchronization migrated

## Owner and Relationships

- Feature owner: `entry-tracking`
- Shared prerequisite: unconditional participation for every contributed content type
- External evidence: registered tracker declarations, exact Entry type, authentication, source acceptance, existing
  track state, and tracker-owned status/score/date/privacy/deletion/automatic-binding facts
- Context-free consequences: service discovery/presentation, account settings, backup diagnostics, Migration
  preparation, and behavioral contract selection
- Contextual consequences: Entry action/session/operations, automatic binding, progress synchronization, Library
  filter/score evidence, Stats evidence, and documentation applicability
- Specialized requirement: none; missing tracker sub-capabilities are valid external integration differences
- Type provider: none; content types never opt into Tracking

`TrackerManager` and each registered tracker remain authoritative. A tracker declares the Entry types it supports and
the operations it implements. The Tracking Feature composes those facts with the actual Entry and live authentication
or source state; it does not copy them into type plugins or maintain a Manga/Anime/Book matrix.

## Feature Boundary

Application consumers receive one `EntryTrackingFeature`. Its API and implementation are split into cohesive tracking
files, but its facets are not separately registered behavior authorities. Neutral service IDs, descriptors, sessions,
requests, and structured outcomes cross the boundary; raw tracker objects and registry types do not.

The initial boundary exposes:

- type-specific availability from registered tracker declarations; and
- a reactive Entry session containing only authenticated, type-compatible, source-compatible services and their
  existing track state, tracker-owned presentation capabilities, and formatted score evidence.

Availability and authenticated session are deliberately separate. Logging out blocks a live session but does not
claim that the tracker stopped supporting the Entry type. An enhanced tracker that rejects the Entry's source blocks
only that service/session relationship.

One application `EntryTrackingHost` adapts the existing tracker system to neutral snapshots. Only the root Feature may
consume this port. The app host extracts tracker facts; the Feature owns applicability, blockers, consequences, and
product results. Tracker implementations, network clients, credentials, registry construction, and OAuth callback
parsing remain external-system mechanics.

The Entry action, badge, and tracking-dialog rows consume these results directly. Registered availability controls
whether the action exists; the authenticated session controls whether it opens the dialog and which rows/count appear.
The dialog no longer receives a raw tracker-bearing application model or reconstructs type, login, source, status,
score, date, privacy, or automatic-binding presentation gates. Operation execution enters the same Feature through
structured commands.

The operations facet covers refresh, search, manual and automatic registration, status/progress/score/date/privacy
mutation, local unregistration, and optional remote deletion. Every request re-resolves the exact service against the
current Entry session before host dispatch. Capability-specific operations are checked against tracker-owned evidence,
and mutations use the current persisted track rather than a UI-supplied tracker or track record.

The host alone converts neutral search candidates to and from tracker-owned models and invokes the raw registry,
tracker methods, refresh coordinator, and local deletion interactor. UI operation screens consume neutral descriptors,
candidates, mutation intents, and structured results. Candidates remain bound to their originating service, preventing
one service's result from being registered through another.

The automatic-behavior facet covers enhanced binding, progress inspection/update, remote-progress reconciliation, and
Migration track preparation without becoming a separately injected Feature. Catalogue, History, Entry, Merge, reader,
and delayed-retry consumers submit Entries and structured requests; they do not select trackers. The Feature selects
services from current type/authentication/source/track evidence, while the host retains enhanced matching, remote
mechanics, and tracker-specific local Migration transformations. F09 still owns Consumption mutation, and F11 still
owns the optimistic transaction that persists prepared track rows.

## Discovered Consequences

The graph declares every censused relationship before consumer migration:

| Relationship | Consequences |
| --- | --- |
| Registry | Service presentation, account settings, backup diagnostics, and shared behavior contract |
| Entry availability | Entry action and documentation applicability |
| Authenticated Entry session | Session rows and Entry operations |
| Automatic source match | Enhanced automatic binding |
| Existing authenticated track | Progress synchronization |
| Migration preparation | Tracker-specific local identity transformation for target track rows |
| Authenticated Library integration | F14 tracker-filter evidence and score-sort evidence |
| Authenticated Stats integration | Tracker and normalized score evidence |

Later Phase 6.7 milestones resolve the remaining account, Library, and Stats relationships through this
same boundary. They do not add another tracker facade, consumer registry, type provider, or settings service list.

## Boundary Enforcement

The build derives every public declaration in the Tracking host package and rejects its use outside the API
declaration, root Tracking implementation/runtime composition, app host implementation, composition root, and tests.
This prevents a neutral host snapshot from becoming a parallel application API just as strongly as it prevents direct
host invocation.

Raw tracker consumer enforcement remains deliberately deferred until the migration is complete. Existing consumers
stay visible as migration work; they are not placed in a temporary allowlist.

## Behavioral Proof

The focused contract contributes BOOK with no Tracking provider and supplies a synthetic external tracker that declares
BOOK support. The unchanged graph and Feature expose availability, an authenticated session, automatic binding, and
progress synchronization. Changing only login state preserves availability while returning structured unavailability
without invoking host mechanics. The same provider-less type discovers every Tracking relationship, and Migration
preparation works without a type opt-in.

The proof tests behavior and architecture. It does not assert which Entry types a built-in tracker currently declares.

## Manifesto Review

- External tracker declarations remain the single support authority.
- Every contributed type participates without a Tracking provider or mandatory operation.
- Tracker absence and sub-capability absence are valid, not incomplete type contributions.
- Common consequences are declared by the Tracking Feature rather than opted into by types or consumers.
- Application code cannot use the host as an alternate behavior boundary.
- No production type matrix, tracker consumer list, copied authentication flag, compatibility facade, or
  declaration-restatement test was introduced.
