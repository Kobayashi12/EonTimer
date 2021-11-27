package io.eontimer.gen3;

import io.eontimer.util.javafx.Choice

enum class Mode(
    override val displayName: String
) : Choice {
    STANDARD("Standard"),
    VARIABLE_TARGET("Variable Target");
}