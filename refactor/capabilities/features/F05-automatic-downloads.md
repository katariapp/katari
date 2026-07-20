# F05 — Automatic Downloads

Status: complete

## Owner and Relationship

- Feature owner: `entry-automatic-download`
- Prerequisite: `EntryDownloadCapability`
- Shared consequences: category/preference policy, deferred library-update queueing and start, and immediate Entry-refresh
  scheduling
- Context: new-child selection, active-profile enabled/unread-only preferences, library membership, category-policy
  eligibility, and candidates remaining after prior-consumption filtering enter one Feature-owned contextual relationship
- Operation data: the actual Entry, category membership, previously consumed recognized child numbers, and selected child
  payloads remain inputs to the shared policy rather than type-wide support facts
- Specialized requirement: none. The F05 census found no media-specific automatic candidate logic in Manga, Anime, or
  Book; every former provider only delegated to the same shared policy.
- Presentation projection: none. F07 owns the settings UI while F05 consumes the resulting preference values.
- Behavioral contracts: the focused synthetic feature test proves both automatic-download paths, shared contextual
  policy, deferred batch start, and valid provider absence. Graph-selected production contract execution remains Phase
  7 work, consistent with F01 and F02.

Provider absence makes Automatic Downloads inapplicable. It does not invalidate the content type or create a missing
work obligation. A type that contributes core Download behavior receives the shared Automatic Downloads integration
without a second opt-in.

The context-free provider integration selects the installed downloader. A separate contextual integration authorizes
the policy, Library Update, and Entry-refresh consequences only when the current request is eligible. Its structured
blockers distinguish empty selection, disabled configuration, non-library Entries, category-policy rejection, and an
unread-only selection with no remaining candidates. These states may change for the same type and therefore never
become content-type declarations.

## T08 Correction

The Phase 4 `EntryAutomaticDownloadFilterCapability` decomposition was based on the assumption that each type owned real
media-specific filtering. The F05 implementation census disproved that assumption: all three implementations performed
only a type assertion and called `FilterEntryChaptersForDownload.await`.

The capability, SPI processor, dispatch method, type bindings, and type dependencies are therefore removed rather than
migrated. The shared policy is now internal to the F05 root module instead of remaining a domain API that application or
type modules could call around the feature boundary. Keeping the old shape would require every future downloadable type
to remember an opt-in for generic behavior and would turn identical delegation into no-op provider boilerplate.

If a future medium introduces genuine automatic-download candidate logic, that difference must be modeled from the
actual requirement. It must not restore a mandatory filter method or an identity provider for every other type.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| Library update discovery | One batch applies F05 policy to each Entry's newly inserted children and queues accepted work without starting it while source refresh is active. |
| Library update queue start | Batch completion starts download processing once only when that batch actually queued work; an empty batch cannot start unrelated queued work. |
| Entry refresh | Manually fetched new children use the same F05 policy and are scheduled through the immediate-start path. |
| Category and preference policy | The internal `EntryAutomaticDownloadPolicy` is consumed only by the F05 coordinator. It captures the active-profile values and Entry state once, produces the candidates and structured blocker, and supplies the same evidence to contextual graph resolution. |
| Type modules | Manga, Anime, and Book no longer receive the shared policy or contribute duplicate automatic-filter bindings. |
| Download settings | F07 retains ownership of preference editing and visibility. F05 only reads the policy inputs. |
| Queue/runtime | F03 retains queue state, execution, pause/resume, and worker/notification behavior. F05 invokes queue mechanics internally for its owned consequence. |
| Manual actions | F04 candidate resolution and individual/bulk actions remain unchanged. |
| Lifecycle cleanup | F06 event and cleanup policy remain unchanged. |

## Automatic-Participation Proof

The focused feature test constructs an anonymous content-type contribution containing only a core Download provider.
Graph evaluation selects the F05 integration. The same coordinator then schedules Entry-refresh work and a deferred
library-update batch without a type list or secondary provider. A contribution without Download remains valid and
produces `Inapplicable` before contextual policy is evaluated.

## Manifesto Review

- Core Download provider presence is the only type-wide prerequisite.
- Generic policy is implemented once by F05 instead of delegated through every content type.
- Favorite, categories, preferences, prior consumption, Entry data, and source/runtime outcomes remain contextual; no
  Entry-State, Selection, Preferences, or Profile capability is introduced.
- Library update and Entry refresh cannot diverge because they call one feature-owned selection pipeline.
- Contextual rejection is no longer collapsed into an opaque `NoCandidates` result.
- No current-type matrix, no-op provider, support-label test, mandatory operation, or duplicated support flag exists.
- A future downloadable type enters both automatic-download paths without application or F05 edits.
- F03, F04, F06, F07, and F08 ownership is not absorbed into the feature.

## Remaining Boundaries

- F03 owns the app-facing queue/runtime contract; F05 owns only automatic selection, deferred queueing, and the
  batch-scoped decision to start work after discovery. Their parallel implementations are reconciled without exposing
  raw queue/runtime dispatch to application consumers.
- F07 owns the settings surface. F05 consumes values from the active profile as owned contextual evidence and does not
  flatten them into graph-wide type support or take ownership of preference editing.
- Graph-selected production contract execution and documentation projection remain Phase 7 work.
