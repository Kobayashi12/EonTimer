plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("application")
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("io.eontimer.audio.SoundKt")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    compileOnly("io.github.landerlyoung:jenny-annotation:1.2.0")
    kapt("io.github.landerlyoung:jenny-compiler:1.2.0")
}

val generateJniHeaders = tasks.create<Copy>("generateJniHeaders") {
    from(fileTree("$buildDir/generated/source/kapt/main") {
        include("**/*.h")
    }.files)
    into(file("native"))

    afterEvaluate {
        val kaptKotlin by tasks
        dependsOn(kaptKotlin)
    }
}

val copyJniLibrary = tasks.create<Copy>("copyJniLibrary") {
    dependsOn(":eon-timer-audio:native:build")
    from(file("native/build/libEonTimerAudio.so"))
    into(file("src/main/resources"))
}

val build by tasks
build.dependsOn(copyJniLibrary)
