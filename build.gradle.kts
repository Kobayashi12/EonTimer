import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.kotlinSpring)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.openjfx)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ktlintIdea)
    id("application")
    id("java")
    id("idea")
}

group = "io.github.dylmeadows"
version = "2.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

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
    implementation(libs.kotlinCoroutinesJavaFx)
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
