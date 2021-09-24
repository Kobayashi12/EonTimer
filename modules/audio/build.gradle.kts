plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    compileOnly("io.github.landerlyoung:jenny-annotation:1.2.0")
    kapt("io.github.landerlyoung:jenny-compiler:1.2.0")
}

tasks.create<Copy>("copyJniGenerated") {
    from(file("build/generated/source/kapt/main"))
    into(file("src/main/cpp/generated"))
    afterEvaluate {
        val kaptKotlin by tasks
        dependsOn(kaptKotlin)
    }
}
