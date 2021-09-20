plugins {
    kotlin("jvm") version "1.5.30"
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.5.2")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    implementation("io.github.dylmeadows:java-common-javafx:$javaCommonVersion")
//    implementation("io.github.dylmeadows:spring-boot-starter-javafx:0.0.1-20190203.122100")
    implementation("io.projectreactor:reactor-core:3.2.6.RELEASE")
    implementation("com.google.guava:guava:27.1-jre")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("de.jensd:fontawesomefx:8.9")

    testImplementation("junit:junit:4.12")
//    testImplementation("org.testfx:testfx-core:$testfxVersion")
//    testImplementation("org.testfx:testfx-junit:$testfxVersion")
}

application {
    mainClassName = "io.github.dylmeadows.eontimer.AppLauncher"
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "11"
//}
