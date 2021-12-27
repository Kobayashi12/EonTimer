package io.eontimer

import io.eontimer.action.TimerActionService
import io.eontimer.util.getValue
import io.eontimer.util.javafx.map
import io.eontimer.util.javafx.subscribe
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.onPlatform
import javafx.application.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import io.eontimer.action.ActionSettings as ActionSettings
import io.eontimer.timer.Settings as TimerSettings

@Service
class TimerRunner(
    private val state: TimerState,
    private val coroutineScope: CoroutineScope,
    private val timerActionService: TimerActionService,
    actionSettings: ActionSettings,
    timerSettings: TimerSettings
) {
    private var timerJob: Job? = null
    private val actionInstants: List<Duration> by actionSettings.actionInstants
    private val period: Duration by timerSettings.refreshInterval
        .map { it.toLong().milliseconds }

    @PostConstruct
    private fun initialize() {
        state.runningProperty
            .subscribe { running ->
                coroutineScope.launch {
                    when (running) {
                        true -> start()
                        false -> stop()
                    }
                }
            }
    }

    private suspend fun start() {
        state.reset()
        timerJob = coroutineScope.launch {
            var elapsed = Duration.ZERO
            while (isActive && state.stageIndex < state.stages.size) {
                elapsed = runStage(elapsed) - state.currentStage
                onPlatform { state.stageIndex++ }
            }
            onPlatform {
                state.running = false
                state.reset()
            }
        }
    }

    private suspend fun CoroutineScope.runStage(
        preElapsed: Duration
    ): Duration {
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
            Platform.runLater {
                state.totalElapsed += delta
                state.currentElapsed = elapsed
            }

            if (currentStage.isFinite() && remaining <= (actionInstants.peek() ?: Duration.ZERO)) {
                timerActionService.invokeAction()
                actionInstants.pop()
            }
        }
        return elapsed
    }

    private suspend fun stop() {
        if (timerJob?.isActive == true) {
            timerJob?.cancel()
            timerJob = null
        }
        state.reset()
    }

    private suspend fun TimerState.reset() =
        onPlatform {
            stageIndex = 0
            totalElapsed = Duration.ZERO
            currentElapsed = when (currentStage.isFinite()) {
                true -> currentStage
                false -> Duration.ZERO
            }
        }

    private fun <T> MutableList<T>.peek() = elementAtOrNull(0)

    private fun <T> MutableList<T>.pop(): T? = if (isEmpty()) null else removeAt(0)
}