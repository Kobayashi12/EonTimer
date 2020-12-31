package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.commonkt.javafx.util.Choice

enum class Gen3TimerMode(
    override val displayName: String
) : Choice {
    STANDARD("Standard"),
    VARIABLE_TARGET("Variable Target");
}