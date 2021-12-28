package io.eontimer

import io.eontimer.action.ActionMode
import io.eontimer.action.ActionSettings
import io.eontimer.action.SoundPlayer
import io.eontimer.timer.TimerSettings
import io.eontimer.util.getValue
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.map
import io.eontimer.util.javafx.onPlatform
import io.eontimer.util.javafx.subscribe
import javafx.beans.property.SimpleBooleanProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

@Service
class TimerService(
    private val state: TimerState,
    private val timerFactory: AggregateControllerTimerFactory,
    private val coroutineScope: CoroutineScope,
    private val soundPlayer: SoundPlayer,
    actionSettings: ActionSettings,
    timerSettings: TimerSettings
) {
    private var timerJob: Job? = null
    private val mode by actionSettings.mode
    private val actionInstants: List<Duration> by actionSettings.actionInstants
    private val period: Duration by timerSettings.refreshInterval
        .map { it.toLong().milliseconds }
    val actionActive = SimpleBooleanProperty(false)

    @PostConstruct
    private fun initialize() {
        state.runningProperty
            .subscribe { running ->
                val now = System.nanoTime()
                coroutineScope.launch {
                    when (running) {
                        true -> start(now)
                        false -> stop()
                    }
                }
            }
    }

    private suspend fun start(
        start: Long
    ) = coroutineScope {
        timerJob = launch {
            var elapsed = (System.nanoTime() - start).nanoseconds
            while (isActive && state.stageIndex < state.stages.size) {
                elapsed = runStage(elapsed) - state.currentStage
                onPlatform { state.stageIndex++ }
            }
            onPlatform {
                state.running = false
            }
        }
    }

    private suspend fun runStage(
        preElapsed: Duration
    ): Duration = coroutineScope {
        var elapsed = preElapsed
        val currentStage by state::currentStage
        val remaining by { currentStage - elapsed }

        var adjustedDelay = period
        var lastTimestamp = System.nanoTime()
        val actionInstants = actionInstants.asSequence()
            .filter { it < remaining }
            .toMutableList()

        while (isActive && elapsed < currentStage) {
            delay(adjustedDelay)

            val now = System.nanoTime()
            val delta = (now - lastTimestamp).nanoseconds
            adjustedDelay -= delta - period
            lastTimestamp = now
            elapsed += delta

            // update the state
            launch {
                onPlatform {
                    state.totalElapsed += delta
                    state.currentElapsed = elapsed
                }
            }

            if (currentStage.isFinite() && remaining <= (actionInstants.peek() ?: Duration.ZERO)) {
                invokeAction()
                actionInstants.pop()
            }
        }
        elapsed
    }

    private suspend fun stop() {
        val timerJob = timerJob
        if (timerJob != null) {
            if (timerJob.isActive) {
                timerJob.cancel()
            }
            this.timerJob = null
        }
        state.reset()
    }

    private suspend fun TimerState.reset() {
        onPlatform {
            stageIndex = 0
            stages = timerFactory.stages
            totalElapsed = Duration.ZERO
            currentElapsed = when (currentStage.isFinite()) {
                true -> currentStage
                false -> Duration.ZERO
            }
        }
    }

    private suspend fun invokeAction() {
        if (mode == ActionMode.AUDIO || mode == ActionMode.AV)
            soundPlayer.play()
        if (mode == ActionMode.VISUAL || mode == ActionMode.AV) {
            coroutineScope.launch {
                onPlatform {
                    actionActive.set(true)
                    delay(75)
                    actionActive.set(false)
                }
            }
        }
    }

    private fun <T> MutableList<T>.peek() = elementAtOrNull(0)

    private fun <T> MutableList<T>.pop(): T? = if (isEmpty()) null else removeAt(0)
}