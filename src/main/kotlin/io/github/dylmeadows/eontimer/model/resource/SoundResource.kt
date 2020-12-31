package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.commonkt.core.io.Resource
import io.github.dylmeadows.commonkt.javafx.util.Choice

enum class SoundResource(
    relativePath: String
) : Resource, Choice {
    BEEP("beep.wav"),
    TICK("tick.wav"),
    DING("ding.wav"),
    POP("pop.wav");

    override val path: String = "$BASE_RESOURCE_PATH/sounds/$relativePath"
    override val displayName: String = name.toLowerCase().capitalize()
}