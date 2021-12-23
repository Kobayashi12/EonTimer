package io.eontimer.action

import io.eontimer.action.Mode
import io.eontimer.action.Settings
import io.eontimer.action.SoundPlayer
import io.eontimer.util.javafx.getValue
import javafx.beans.property.SimpleBooleanProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class TimerActionService(
    actionSettings: Settings,
    private val soundPlayer: SoundPlayer,
    private val coroutineScope: CoroutineScope
) {
    private val mode by actionSettings.mode
    val active = SimpleBooleanProperty(false)

    fun invokeAction() {
        if (mode == Mode.AUDIO || mode == Mode.AV)
            soundPlayer.play()
        if (mode == Mode.VISUAL || mode == Mode.AV) {
            active.set(true)
            coroutineScope.launch {
                delay(75)
                active.set(false)
            }
        }
    }
}