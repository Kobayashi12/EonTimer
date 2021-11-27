package io.eontimer.service.action

import io.eontimer.action.Mode
import io.eontimer.action.Settings
import io.eontimer.action.SoundPlayer
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
    private val actionSettings: Settings,
    private val soundPlayer: SoundPlayer,
    private val coroutineScope: CoroutineScope
) {
    final var actionInterval: List<Duration> = Collections.emptyList()
        private set

    val active = SimpleBooleanProperty(false)

    @PostConstruct
    private fun initialize() {
        coroutineScope.launch {
            actionSettings.count.asFlow()
                .zip(actionSettings.interval.asFlow()) { count, interval ->
                    createActionInterval(count.toInt(), interval.toInt())
                }.collect {
                    actionInterval = it
                }
        }
    }

    private fun createActionInterval(count: Int, interval: Int): List<Duration> =
        IntRange(0, count - 1)
            .reversed()
            .asSequence()
            .map { it * interval }
            .map(Number::toLong)
            .map(Duration::ofMillis)
            .toList()

    fun invokeAction() {
        val mode = actionSettings.mode.get()
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