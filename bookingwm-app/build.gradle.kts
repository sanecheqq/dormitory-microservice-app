val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgresql_driver_version: String by project
val exposed_version: String by project
val codec_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

group = "ru.missclick3"
version = "0.0.1"

application {
    mainClass.set("ru.missclick3.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-serialization-gson-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //postgres
    implementation("org.postgresql:postgresql:$postgresql_driver_version")

    //exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    //apache codec
    implementation("commons-codec:commons-codec:$codec_version")

    //apache comons validator
    implementation("commons-validator:commons-validator:1.8.0")

    //consul
    implementation("com.orbitz.consul:consul-client:1.5.3")

    //cors
    implementation("io.ktor:ktor-server-cors:$ktor_version")

    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("io.ktor:ktor-client-json:$ktor_version")

    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
}

val mainClassName = "ru.missclick3.ApplicationKt"
tasks.jar {
    manifest {
        attributes["Main-Class"] = mainClassName
    }
}
tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    mainClass.set("ru.missclick3.ApplicationKt")

    environment("DB_DRIVER", "org.postgresql.Driver")
    environment("DB_USER", "postgres")
    environment("DB_PW", "postgres")
    environment("DB_URL", "jdbc:postgresql://localhost:5432/booking_wm_app_db")
}
