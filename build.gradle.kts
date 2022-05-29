import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.4.21"
}

group = "com.vadmark223.vmm-desktop"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation("org.apache.logging.log4j:log4j-api:" + extra["log4j.version"])
                implementation("org.apache.logging.log4j:log4j-core:" + extra["log4j.version"])
                implementation("org.apache.logging.log4j:log4j-slf4j-impl:" + extra["log4j.version"])

                implementation("io.ktor:ktor-client-core:2.0.0")
                implementation("io.ktor:ktor-client-cio:2.0.0")
                implementation("io.ktor:ktor-client-websockets:2.0.0")
                // Ktor plugins
                implementation("io.ktor:ktor-client-content-negotiation:2.0.0")
                implementation("ch.qos.logback:logback-classic:1.3.0-alpha14")
                implementation("io.ktor:ktor-client-logging:2.0.0")

                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.2")

                implementation("org.jetbrains.compose.components:components-splitpane-desktop:" + extra["compose.version"])

            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "vmm-desktop"
            packageVersion = "1.0.0"
        }
    }
}
