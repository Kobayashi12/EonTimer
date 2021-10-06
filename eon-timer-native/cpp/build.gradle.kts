apply(from = "$rootDir/gradle/jni.gradle.kts")
val eonTimerNative = project(":eon-timer-native")

val build by tasks
val copyHeaders by eonTimerNative.tasks
build.dependsOn(copyHeaders)