package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.commonkt.javafx.util.Choice

enum class Gen5TimerMode(
    override val displayName: String
) : Choice {
    STANDARD("Standard"),
    C_GEAR("C-Gear"),
    ENTRALINK("Entralink"),
    ENHANCED_ENTRALINK("Entralink+");
}