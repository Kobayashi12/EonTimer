package io.eontimer.gen5

import io.eontimer.util.javafx.Choice

enum class Gen5TimerMode(
    override val displayName: String
) : Choice {
    STANDARD("Standard"),
    C_GEAR("C-Gear"),
    ENTRALINK("Entralink"),
    ENHANCED_ENTRALINK("Entralink+")
}