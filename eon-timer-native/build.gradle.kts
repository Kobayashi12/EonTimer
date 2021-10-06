plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("application")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

application {
//    mainClass.set("io.eontimer.audio.SoundKt")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("io.github.landerlyoung:jenny-annotation:1.2.0")
    kapt("io.github.landerlyoung:jenny-compiler:1.2.0")
}

val copyHeaders = tasks.create<Copy>("copyHeaders") {
    from(fileTree("$buildDir/generated/source/kapt/main") {
        include("**/*.h")
    }.files)
    into(file("cpp"))

    afterEvaluate {
        val kaptKotlin by tasks
        dependsOn(kaptKotlin)
    }
}

val copyNativeLibrary = tasks.create<Copy>("copyNativeLibrary") {
    dependsOn(":eon-timer-native:cpp:build")
    from(file("cpp/build/libEonTimerAudio.so"))
    from(file("cpp/build/libEonTimerAudio.dylib"))
    into(file("src/main/resources"))
}

val build by tasks
build.dependsOn(copyNativeLibrary)