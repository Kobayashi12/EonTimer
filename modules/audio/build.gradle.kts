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
    implementation(libs.kotlinCoroutinesCore)
    compileOnly(libs.jennyAnnotations)
    kapt(libs.jennyCompiler)
}

tasks.create<Copy>("copyJniGenerated") {
    from(file("build/generated/source/kapt/main"))
    into(file("src/main/cpp/generated"))
    afterEvaluate {
        val kaptKotlin by tasks
        dependsOn(kaptKotlin)
    }
}
