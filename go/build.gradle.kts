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
    implementation("org.mongodb:mongodb-driver-kotlin-sync:5.13.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.26.1")




}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}