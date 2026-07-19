# Repo Notes

## Layout
- `app/` is the runtime app. Shared code lives in `core/*`, `data`, `domain`, `presentation-*`, `source-*`, `i18n`, `telemetry`, and `macrobenchmark`.
- Custom Gradle plugins and tasks live in `gradle/build-logic/`. `settings.gradle.kts` enables type-safe project accessors and rejects project-level repositories, so add repos only there and use catalog/accessor entries instead of hardcoded versions or string project paths.

## Source organization
- A source directory must represent one cohesive responsibility. Group files by the feature, bounded context, or runtime layer that owns them; do not accumulate unrelated feature contracts, implementations, providers, and helpers in a module-root directory.
- Keep only genuine module-wide entry points and composition roots at the source root. When a module contains multiple responsibilities, create clearly named subdirectories for them as part of the same change that introduces or exposes the split.
- Mirror the production directory structure in tests so behavior and its coverage remain discoverable together.
- Avoid catch-all directories such as `common`, `misc`, or `utils`. Name structural groups after concrete ownership, and place narrowly shared helpers with the feature that owns their semantics.
- Before finishing a change, inspect every touched source directory. If the new files make ownership harder to understand from the tree alone, reorganize that area before committing rather than leaving cleanup for a follow-up.

## Toolchain
- Use the Gradle wrapper (`9.4.1`) and JDK `21` from `.github/.java-version`.
- Android SDK/NDK and Java compatibility come from `gradle/mihon.versions.toml` plus build logic; do not hardcode them per module.
- Build scripts shell out to `git` for commit count/SHA/build time, so `git` must be on `PATH`.
- Do not run commands in a way that will result in the full output from gradlew to prevent context from being filled with unnecessary information. Use --quiet when possible

## Validation
- Run `./gradlew spotlessApply` from the repo root.
- CI runs `spotlessCheck` -> `verifyLegacySourceAbi` -> `testFossUnitTest` -> `verifySqlDelightMigration` -> `assembleRelease -Pinclude-telemetry -Penable-updater`.
- App unit tests run on the `foss` buildType (`testBuildType = "foss"`); focused example: `./gradlew :app:testFossUnitTest --tests '...'`.
- FOSS compilation can be verified with `:app:compileFossKotlin`; telemetry-enabled release compilation uses `:app:compileReleaseKotlin -Pinclude-telemetry`.
- After touching `data/src/main/sqldelight`, run `./gradlew verifySqlDelightMigration`.
- Briefly verify if applied changes require docs update. If there's no docs coverage - ask user if docs should be updated

## Generated Files
- Edit `app/src/main/shortcuts.xml`; the variant task generates `xml/shortcuts.xml` with `${applicationId}` substituted.
- `i18n/src/commonMain/moko-resources/**/strings.xml` and `plurals.xml` drive the generated `@xml/locales_config`; `base` maps to `en`, and empty locale resources are skipped.
- App namespace is `eu.kanade.tachiyomi`, but `applicationId` is `app.katari`.
- Gradle properties toggle build features: `include-telemetry`, `enable-updater`, `include-dependency-info`.
- Releases are tag-driven: `v*` publishes telemetry-enabled universal and ABI-specific APKs plus a universal FOSS APK; `sdk-*` warms JitPack for `source-api`, which JitPack builds on OpenJDK 17 via `:source-api:publishToMavenLocal`.
