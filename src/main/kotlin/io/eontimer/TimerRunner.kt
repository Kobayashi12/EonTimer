package io.eontimer

import io.eontimer.action.TimerActionService
import io.eontimer.util.isFinite
import io.eontimer.util.javafx.easybind.map
import io.eontimer.util.javafx.easybind.subscribe
import io.eontimer.util.javafx.getValue
import io.eontimer.util.milliseconds
import io.eontimer.util.nanoseconds
import io.eontimer.util.peek
import io.eontimer.util.pop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import kotlin.time.Duration
import java.time.Instant
import javax.annotation.PostConstruct
import kotlin.system.measureTimeMillis
import io.eontimer.action.Settings as ActionSettings
import io.eontimer.timer.Settings as TimerSettings

@Service
class TimerRunner(
    private val state: TimerState,
    private val coroutineScope: CoroutineScope,
    private val timerActionService: TimerActionService,
    actionSettings: ActionSettings,
    timerSettings: TimerSettings
) {
    private val stateProxy = TimerStateProxy(state)

    private var timerJob: Job? = null
    private val actionInstants: List<Duration> by actionSettings.actionInstants
    private val period: Duration by timerSettings.refreshInterval
        .map { it.toLong().milliseconds }

    @PostConstruct
    private fun initialize() {
        state.running
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
        stateProxy.reset()
        val stages = stateProxy.stages
        val stage by stateProxy::stage
        var stageIndex by stateProxy::stageIndex
        timerJob = coroutineScope.launch {
            var elapsed = Duration.ZERO
            while (isActive && stageIndex < stages.size) {
                elapsed = runStage(elapsed) - stage
                stageIndex++
            }
            withContext(Dispatchers.JavaFx) {
                stateProxy.running = false
                stateProxy.reset()
            }
        }
    }

    private suspend fun CoroutineScope.runStage(
        preElapsed: Duration
    ): Duration {
        var adjustedDelay = period
        var lastTimestamp = System.nanoTime()

        val stage by stateProxy::stage
        var elapsed by stateProxy::elapsed
        val remaining by stateProxy::remaining
        var totalElapsed by stateProxy::totalElapsed

        elapsed = preElapsed
        val actionInstants = actionInstants.asSequence()
            .filter { it < remaining }
            .toMutableList()

        while (isActive && elapsed < stage) {
            delay(adjustedDelay)

            val now = System.nanoTime()
            val delta = (lastTimestamp - now).nanoseconds
            adjustedDelay -= delta - period
            lastTimestamp = now
            // update the state
            withContext(Dispatchers.JavaFx) {
                totalElapsed += delta
                elapsed += delta
            }

//            stateProxy.update(delta, elapsed)
            if (stage.isFinite && remaining <= (actionInstants.peek() ?: Duration.ZERO)) {
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
        stateProxy.reset()
    }

    private suspend fun TimerStateProxy.update(
        delta: Duration = Duration.ZERO,
    ) {
        withContext(Dispatchers.JavaFx) {
//            this@update.elapsed = when (stage.isFinite) {
//                true -> stage - elapsed
//                false -> elapsed
//            }
            elapsed += delta
            totalElapsed += delta
        }
    }

    private suspend fun TimerStateProxy.reset() =
        withContext(Dispatchers.JavaFx) {
            stageIndex = 0
            totalElapsed = Duration.ZERO
            elapsed = when (stage.isFinite) {
                true -> stage
                false -> Duration.ZERO
            }
        }
}