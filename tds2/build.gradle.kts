group = "pt.isel.tds2627"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.3.10"
    // 1. Adicionar plugins
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.10"
    id("org.jetbrains.compose") version "1.9.3"
}


repositories {
    mavenCentral()
    // 2.Adicionar repos
    gradlePluginPortal()
    google()
}


dependencies {
    testImplementation(kotlin("test"))
    implementation("org.mongodb:mongodb-driver-kotlin-sync:5.13.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.26.1")
    implementation("com.squareup.okio:okio:3.18.2")
    // 3. Novas dependencias
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.components.resources)
}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}

compose.resources {
    publicResClass = true
    packageOfResClass = "pt.isel.tds2627.resources"
    generateResClass = always
}