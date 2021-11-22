rootProject.name = "eon-timer"

include("eon-timer-audio")
project(":eon-timer-audio").projectDir = file("modules/audio")

enableFeaturePreview("VERSION_CATALOGS")
