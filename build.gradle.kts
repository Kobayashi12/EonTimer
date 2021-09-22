import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.spring") version "1.5.30"
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.openjfx.javafxplugin") version "0.0.10"
    id("org.jlleitschuh.gradle.ktlint") version "8.2.0"
    id("org.jlleitschuh.gradle.ktlint-idea") version "8.2.0"
    id("application")
    id("java")
    id("idea")
}

group = "io.github.dylmeadows"
version = "2.0.1"

javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.fxml", "javafx.media")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":eon-timer-audio"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.5.2")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    kapt("org.springframework.boot:spring-boot-autoconfigure-processor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("de.jensd:fontawesomefx:8.9")

    testImplementation("junit:junit:4.12")
//    testImplementation("org.testfx:testfx-core:$testfxVersion")
//    testImplementation("org.testfx:testfx-junit:$testfxVersion")
}

application {
    mainClass.set("io.eontimer.AppLauncher")
}

tasks.withType<KotlinCompile>  {
    kotlinOptions.jvmTarget = "11"
}
