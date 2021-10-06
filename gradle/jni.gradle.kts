tasks.create<Delete>("clean") {
    group = "build"
    delete = setOf(buildDir)
}

val cmake = tasks.create<Exec>("cmake") {
    group = "build"
    if (!buildDir.exists()) buildDir.mkdirs()

    workingDir = buildDir
    commandLine = listOf("cmake", "-DCMAKE_BUILD_TYPE=Release", "..")
}

tasks.create<Exec>("build") {
    group = "build"
    workingDir = buildDir
    commandLine = listOf("make", "-j")
    dependsOn(cmake)
}