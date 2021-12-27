package io.eontimer.action

import io.eontimer.util.javafx.getValue
import javafx.beans.property.SimpleBooleanProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class TimerActionService(
    actionSettings: ActionSettings,
    private val soundPlayer: SoundPlayer,
    private val coroutineScope: CoroutineScope
) {
    private val mode by actionSettings.mode
    val active = SimpleBooleanProperty(false)

    fun invokeAction() {
        if (mode == ActionMode.AUDIO || mode == ActionMode.AV)
            soundPlayer.play()
        if (mode == ActionMode.VISUAL || mode == ActionMode.AV) {
            active.set(true)
            coroutineScope.launch {
                delay(75)
                active.set(false)
            }
        }
    }
}