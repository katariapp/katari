# F27 — Profile Preference Ownership

Status: complete

## Owner and Relationships

- Feature owner: profile preference infrastructure
- Fundamental prerequisite: an owner is installed against the profile-aware preference store
- Shared consequences: static-key discovery, dynamic-family matching, profile/app-state/private classification, legacy
  migration and cleanup, and deterministic profile lifecycle participation
- Context: the active or explicitly selected profile, existing legacy keys, and the host profile store
- Specialized declaration: owners whose keys are created only from runtime identities contribute stable key-family
  patterns at the same installation statement as their runtime factory
- Compatibility: named versioned ownership corrections remain explicit migration groups; they do not authorize current
  feature support
- Behavioral contract: unique owner identity, unambiguous static and dynamic ownership, sealed installation before
  evaluation, and migration/cleanup driven by a newly installed owner

Profile ownership is not a content-type capability and is never mandatory. A feature or type owns profile state only
when its ordinary runtime installation constructs a real preference owner through the profile-owner installer. Global
owners continue to use the base store and do not participate.

## Feature Boundary

`ProfilePreferenceOwnerRegistry` is populated by the same registered factories that construct production preference
owners. Evaluating ownership runs those factories against a recording store for static keys and resolves contributed
patterns against actual persisted legacy keys. The registry then classifies keys from their `Preference` wrappers.

The registered handle is bound to the actual profile-aware store, so runtime construction and ownership contribution
are one operation. Entry-interaction composition receives only this installer, not a raw profile/private store. Type
runtime modules therefore construct Viewer Settings and Book processor preferences through ordinary installation
without a second runtime field or root list. Application preference factories use the same mechanism in
`PreferenceModule`.

The former `ProfilePreferenceOwnership` constructor checklist, tracker-ID loop, and F25-only ownership artifact are
removed. Ownership evaluation seals registration. A lazily installed owner after migration fails explicitly, as do
duplicate owner IDs, duplicate static keys, overlapping patterns, and a static key captured by another owner.

## Consumer Disposition

| Surface | Disposition |
| --- | --- |
| General profile preferences | Source, Security, Library, Duplicate, Updates, Tracking, UI, and Custom owners register beside their real DI factories. |
| Feature/type preferences | Preview, Manga/Anime/Book Viewer Settings, and Book processor-selection owners register during existing Entry runtime installation. |
| Dynamic keys | Source display/feed state, Library tracker filters, tracker credentials/OAuth state, and Book processor choices use owner-local prefix patterns instead of current-ID enumeration. |
| Legacy migration/cleanup | `ProfileManager` evaluates all installed owners against existing keys and passes the result to the unchanged copy-before-cleanup mechanics. |
| Legacy ownership correction | Viewer Settings owners join a named version-11 compatibility group; the correction consumes that discovered group rather than the current Feature API or a provider list. |
| Profile creation | Explicit seed values continue to be written through a store bound to the new profile; ownership discovery does not invent defaults or mandatory state. |
| Profile deletion | Existing namespace scanning removes all actual profile/app-state/private keys, including dynamic and future keys; it does not depend on an ownership list. |
| Explicit per-profile operations | Narrow profile-management, backup, tracker, and UI paths may request a store for a concrete profile ID; they are operational access, not current ownership declarations. |
| Global preferences | Base-store owners remain outside F27, including Download, Backup, Network, global Source/Library/Tracking, and Media Cache preferences. |

## Automatic-participation Proof

The behavior test installs synthetic static and dynamic preference owners, writes legacy profile, app-state, and private
values, then proves migration creates the correct profile namespace and cleanup removes the legacy values. The migration
test does not edit a recorder list or assert a current content-type matrix. Registry mechanics separately prove duplicate
and ambiguous ownership rejection plus the late-installation seal.

## Manifesto Review

- Runtime construction and ownership discovery share one owner registration; there is no second completion checklist.
- Entry type modules receive an owner installer rather than a raw profile store, so ordinary construction cannot bypass
  F27 accidentally.
- Build validation rejects no-argument active profile/private-store access outside an installer binding while retaining
  explicit per-profile operational access.
- Dynamic identities are expressed as genuine key-family differences rather than enumerating current trackers, sources,
  feeds, or Book formats.
- Missing owners create no state and are valid; F27 declares no mandatory content-type operation.
- Versioned migration groups are persistence compatibility only and cannot become feature-support authorities.
- Global state remains distinct from profile, app-state, and private profile state.
- No type matrix, marker/no-op provider, raw application SPI access, compatibility fallback, or declaration test was
  introduced.

## Validation

- Core Common registry mechanics tests pass.
- Build-logic tests reject a singleton preference factory that bypasses its installed ownership handle.
- Entry-interactions API, Manga, Anime, Book, and root production compilation passes.
- Root Entry-interactions behavior tests pass after removing the superseded F25 ownership artifact.
- FOSS application compilation reaches the independently assigned F11/F12 migration failures and reports no F27
  production diagnostic.
- The app migration behavior test is present for combined integration validation once F11/F12 restore application test
  compilation.
