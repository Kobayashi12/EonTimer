package io.eontimer.model.settings

import io.eontimer.util.javafx.Choice

enum class Console(
    override val displayName: String,
    fps: Double
) : Choice {
    GBA("GBA", 59.7271),
    NDS("NDS", 59.8261),
    DSI("DSI", 59.8261),
    _3DS("3DS", 59.8261);

    val frameRate = 1000 / fps
}