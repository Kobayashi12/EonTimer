package io.eontimer.gen3

import io.eontimer.util.javafx.Choice

enum class Gen3TimerMode(
    override val displayName: String
) : Choice {
    STANDARD("Standard"),
    VARIABLE_TARGET("Variable Target");
}