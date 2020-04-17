import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.example"

plugins {
    kotlin("jvm") version Versions.kotlin
    kotlin("plugin.spring") version Versions.kotlin
    id("org.springframework.boot") version Versions.spring
    id("io.spring.dependency-management") version Versions.springDependencyManagementPlugin
    id("net.researchgate.release") version Versions.releasePlugin
}

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    val updateReadmeVersion by registering {
        group = "release"

        doFirst {
            logger.info("Updating application version in README.md file")
            file("$rootDir/README.md").replaceString(Regex("version: .*"), "version: $version")
        }
    }
    named("afterReleaseBuild") {
        dependsOn(updateReadmeVersion)
    }
}
