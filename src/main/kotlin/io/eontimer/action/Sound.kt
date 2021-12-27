package io.eontimer.action

import io.eontimer.util.BASE_RESOURCE_PATH
import io.eontimer.util.Resource
import io.eontimer.util.javafx.Choice
import java.util.Locale

enum class Sound(
    fileName: String
) : Resource, Choice {
    BEEP("beep.wav"),
    TICK("tick.wav"),
    DING("ding.wav"),
    POP("pop.wav");

    override val path = "$BASE_RESOURCE_PATH/sounds/$fileName"
    override val displayName = name.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}