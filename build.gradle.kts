import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    signing
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.release)
    `java-library`
}

group = "eu.magicksource.kiniconfig"
val artifact = "kiniconfig"
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

mavenPublishing {
    coordinates(group.toString(), artifact, version.toString())
    publishToMavenCentral()
    signAllPublications()

    pom {
        organization {
            name.set(findProperty("POM_ORGANIZATION") as String)
            url.set(findProperty("POM_ORGANIZATION_URL") as String)
        }
    }
}

signing {
    sign(publishing.publications)
    useGpgCmd()
}
