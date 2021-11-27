package io.eontimer.gen5

import io.eontimer.util.javafx.Choice

enum class Mode(
    override val displayName: String
) : Choice {
    STANDARD("Standard"),
    C_GEAR("C-Gear"),
    ENTRALINK("Entralink"),
    ENHANCED_ENTRALINK("Entralink+")
}