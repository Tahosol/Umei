import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "org.umei"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("org.jsoup:jsoup:1.18.3")
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    implementation("cafe.adriel.voyager:voyager-navigator:1.0.1")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:1.0.1")
    implementation("cafe.adriel.voyager:voyager-transitions:1.0.1")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.AppImage, TargetFormat.Deb)
            packageName = "Umei"
            packageVersion = "1.0.0"
        }
        buildTypes.release.proguard {
            isEnabled = false
        }
    }
}
