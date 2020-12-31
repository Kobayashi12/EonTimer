package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.commonkt.javafx.util.Choice

enum class ActionMode(
    override val displayName: String
) : Choice {
    AUDIO("Audio"),
    VISUAL("Visual"),
    AV("A/V"),
    NONE("None");
}