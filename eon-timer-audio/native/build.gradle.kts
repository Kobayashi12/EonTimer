val other = project(":eon-timer-audio")
val copyJniLibrary by other.tasks
val generateJniHeaders by other.tasks

tasks.create<Delete>("clean") {
    group = "build"
    delete = setOf(buildDir)
}

val cmake = tasks.create<Exec>("cmake") {
    group = "build"
    dependsOn(generateJniHeaders)
    if (!buildDir.exists()) buildDir.mkdirs()

    workingDir = buildDir
    commandLine = listOf("cmake", "-DCMAKE_BUILD_TYPE=Release", "..")
}

tasks.create<Exec>("build") {
    group = "build"
    workingDir = buildDir
    commandLine = listOf("make", "-j")
    dependsOn(cmake)
    finalizedBy(copyJniLibrary)
}