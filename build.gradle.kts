plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.gorylenko.gradle-git-properties") version "2.5.0"
}

group = "org.deblock"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:4.3.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
