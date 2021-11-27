package io.eontimer.action

import io.eontimer.util.javafx.Choice

enum class Mode(
    override val displayName: String
): Choice {
    AUDIO("Audio"),
    VISUAL("Visual"),
    AV("A/V"),
    NONE("None")
}