apply(from = "$rootDir/gradle/jni.gradle.kts")
val eonTimerAudio = project(":eon-timer-audio")

val build by tasks
val copyHeaders by eonTimerAudio.tasks
build.dependsOn(copyHeaders)