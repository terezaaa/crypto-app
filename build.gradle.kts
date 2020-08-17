plugins {
    kotlin("jvm") version "1.3.72"
    application
}

application {
   mainClassName = "Main"
}

kotlin {
    sourceSets["main"].apply {
        kotlin.srcDir("src/main/kotlin/Application")
    }
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}

group = "org.crypto-app"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-core:1.3.2")
    implementation("io.github.microutils:kotlin-logging:1.8.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.2")
    implementation("io.ktor:ktor-client-cio:1.3.2")
    implementation("io.ktor:ktor-client-json-jvm:1.3.2")
    implementation("io.ktor:ktor-client-logging-jvm:1.3.2")
    implementation("io.ktor:ktor-client-apache:1.3.2")
    implementation("io.ktor:ktor-client-jackson:1.3.2")
    implementation("org.koin:koin-core:2.1.6")
    implementation("io.ktor:ktor-client-websockets:1.3.2")
    implementation("io.ktor:ktor-client-cio:1.3.2")
    implementation("io.ktor:ktor-client-okhttp:1.3.2")
}
