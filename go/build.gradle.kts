plugins {
    kotlin("jvm") version "2.3.10"
}

group = "pt.isel.tds2627"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.squareup.okio:okio:3.18.2")
}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}