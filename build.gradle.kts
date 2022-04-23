import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.vadmark223.vmm-desktop"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
                implementation("org.postgresql:postgresql:" + extra["postgresql.version"])
                implementation("org.jetbrains.exposed:exposed-core:" + extra["jetbrains.exposed.version"])
                implementation("org.jetbrains.exposed:exposed-dao:" + extra["jetbrains.exposed.version"])
                implementation("org.jetbrains.exposed:exposed-jdbc:" + extra["jetbrains.exposed.version"])
                implementation("org.jetbrains.exposed:exposed-java-time:" + extra["jetbrains.exposed.version"])
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
