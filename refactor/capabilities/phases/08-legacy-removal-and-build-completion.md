# Phase 8 — Legacy Removal, Boundary Enforcement, and Build Completion

## Objective

Remove residual compatibility artifacts and restore the complete build by making remaining production code conform to
the architecture. The central catalog/report authority must already have been retired at Phase 3.5.

The audit-only entry census and final candidate classifications are recorded in
[`../phase8-census.md`](../phase8-census.md). Cleanup must follow that disposition rather than treating every search
match as obsolete code.

## Cleanup and Enforcement

- [x] Verify that no superseded catalog, report, or report-query authority survived the Phase 3.5 cut.
- [x] Remove residual support APIs and presentation flags; retain only verified compatibility adapters.
- [x] Remove direct type gates and per-type derived behavior from generic feature code.
- [x] Strengthen boundaries against curated participation and parallel support declarations.
- [x] Resolve every recorded compile failure through architectural migration.
- [x] Run an unknown type/capability/feature acceptance scenario across graph evaluation, obligations, contracts,
  reporting, and documentation.
- [x] Review every manifesto failure mode and success criterion.
- [x] Rerun every repeatable census probe in `../migration-inventory.md` and classify every remaining candidate.
- [x] Verify every `T`, `F`, and `C` register row and every out-of-boundary finding has a final disposition.

## Exit Gate

- The full application compiles because code follows the architecture.
- No fallback or duplicate authority exists solely to preserve the migration.
- Generic feature code does not branch on content type for applicability.
- Unknown contributions participate automatically through their owners.
- No unclassified support-like method, direct type gate, manual participation list, or feature consequence remains.
- Full CI-style and documentation validation passes.

## Validation

- `./gradlew --quiet spotlessCheck`
- `./gradlew --quiet verifyLegacySourceAbi`
- `./gradlew --quiet testFossUnitTest`
- `./gradlew --quiet checkEntryInteractionBoundaries`
- `./gradlew --quiet :app:compileFossKotlin`
- Documentation build and capability-reference verification
- `git diff --check`

## Manifesto Review

Review every success criterion explicitly and reject any remaining mechanism that depends on a contributor remembering a
second list or follow-up integration.

## Completion Result

- The full pre-release gate passes under JDK 21: formatting, Entry Feature architecture, legacy source ABI, FOSS unit
  tests, and SQLDelight migration verification.
- The telemetry/updater release assembly and extension runtime ABI verification pass under JDK 21.
- The Entry Feature documentation projection, both SDK Dokka publications, and the VitePress documentation build pass
  with the repository-required Node 24 and pnpm 10 toolchain.
- Exit-gate testing removed a stale test matrix that asserted current type vocabulary directly. Selection action labels
  are now tested through an injected `EntryTypePresentationFeature`, proving generic versus contributed projection
  behavior without restating Manga, Anime, and Book declarations.
- Exit-gate testing also moved the tracking selection boundary proof from the deleted monolithic dialog to the three
  current selector owners. That audit found and corrected a real race: score, progress, and status selections are
  captured before non-cancellable dispatch rather than reread after launch.
- The final manifesto comparison found no parallel support authority, current-type completion matrix, mandatory
  provider, or curated Feature consequence list. Provider absence remains valid, and unknown contributions continue to
  enter evaluation, obligations, contracts, reporting, and documentation through their owners.
