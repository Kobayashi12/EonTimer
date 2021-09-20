package io.eontimer.service.action

import io.eontimer.model.settings.ActionMode
import io.eontimer.model.settings.ActionSettings
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*
import javax.annotation.PostConstruct

@Service
@ExperimentalCoroutinesApi
class TimerActionService(
    private val actionSettings: ActionSettings,
    private val soundPlayer: SoundPlayer,
    private val coroutineScope: CoroutineScope
) {
    final var actionInterval: List<Duration> = Collections.emptyList()
        private set

    final val activeProperty: BooleanProperty = SimpleBooleanProperty(false)
    private var active by activeProperty

    @PostConstruct
    private fun initialize() {
        coroutineScope.launch {
            actionSettings.countProperty.asFlow()
                .zip(actionSettings.intervalProperty.asFlow()) { count, interval ->
                    createActionInterval(count.toInt(), interval.toInt())
                }.collect {
                    actionInterval = it
                }
        }
    }

    private fun createActionInterval(count: Int, interval: Int): List<Duration> {
        return IntRange(0, count - 1).reversed()
            .asSequence()
            .map { it * interval }
            .map(Number::toLong)
            .map(Duration::ofMillis)
            .toList()
    }

    fun invokeAction() {
        if (actionSettings.mode == ActionMode.AUDIO || actionSettings.mode == ActionMode.AV)
            soundPlayer.play()
        if (actionSettings.mode == ActionMode.VISUAL || actionSettings.mode == ActionMode.AV) {
            active = true
            coroutineScope.launch {
                delay(75)
                active = false
            }
        }
    }
}