import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    `java-library`
}

group = "eu.magicksource.kiniconfig"
version = "0.1.0"

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }

}

dependencies {
    implementation(libs.jackson.kotlin)
    implementation(libs.konfig)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter)
}
