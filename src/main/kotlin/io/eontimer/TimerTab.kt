package io.eontimer

import io.eontimer.util.ReadOnlyArray

enum class TimerTab {
    GEN5, GEN4, GEN3, CUSTOM;

    companion object {
        val values = ReadOnlyArray(values())
    }
}