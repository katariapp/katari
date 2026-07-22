import org.gradle.api.tasks.testing.Test

plugins {
    alias(mihonx.plugins.android.library)
    alias(mihonx.plugins.spotless)
}

android {
    namespace = "mihon.entry.interactions"

    testFixtures {
        enable = true
    }
}

dependencies {
    api(projects.entryInteractions.api)
    api(projects.entryViewerSettingsApi)

    implementation(projects.core.common)
    implementation(projects.entryInteractions.spi)
    implementation(projects.entryInteractions.downloadNotification)
    implementation(projects.entryInteractions.manga)
    implementation(projects.entryInteractions.anime)
    implementation(projects.entryInteractions.book)
    implementation(projects.sourceCompat)
    implementation(projects.sourceLocal)
    implementation(libs.androidx.core)
    implementation(libs.androidx.work)
    implementation(libs.injekt)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.serialization)
    implementation(libs.bundles.coil)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.serialization)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.unifile)
    testImplementation(projects.featureValidation)
    testRuntimeOnly(libs.junit.platform.launcher)

    testFixturesImplementation(libs.bundles.test)
    testFixturesImplementation(libs.bundles.serialization)
    testFixturesImplementation(libs.kotlinx.coroutines.test)
    testFixturesImplementation(libs.unifile)
    testFixturesImplementation(platform(libs.androidx.compose.bom))
    testFixturesApi(projects.entryInteractions.spi)
}

val entryFeatureReportFile = layout.buildDirectory.file("reports/entry-features/developer-report.txt")

val generateEntryFeatureReport = tasks.register<Test>("generateEntryFeatureReport") {
    group = "reporting"
    description = "Renders the evaluated Entry feature graph and validation results for developers"

    testClassesDirs = files(
        providers.provider { tasks.named<Test>("testDebugUnitTest").get().testClassesDirs },
    )
    classpath = files(
        providers.provider { tasks.named<Test>("testDebugUnitTest").get().classpath },
    )
    filter {
        includeTestsMatching(
            "mihon.entry.interactions.validation.ProductionEntryInteractionDeveloperReportTest",
        )
    }
    systemProperty(
        "mihon.entry.feature.report.output",
        entryFeatureReportFile.get().asFile.absolutePath,
    )
    outputs.file(entryFeatureReportFile)
    testLogging.showStandardStreams = true
}
