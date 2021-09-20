package io.eontimer.service

import io.eontimer.model.TimerState
import io.eontimer.model.settings.TimerSettings
import io.eontimer.service.action.TimerActionService
import io.eontimer.util.Stack
import io.eontimer.util.asStack
import io.eontimer.util.getStage
import io.eontimer.util.isIndefinite
import io.eontimer.util.milliseconds
import io.eontimer.util.sum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
@ExperimentalCoroutinesApi
class TimerRunnerService(
    private val timerState: TimerState,
    private val timerSettings: TimerSettings,
    private val timerActionService: TimerActionService,
    private val coroutineScope: CoroutineScope
) {
    private lateinit var timerJob: Job
    final var stages: MutableList<Duration> = Collections.emptyList()
        private set
    private var mStages: List<Duration> = Collections.emptyList()

    private var totalTime by timerState::totalTime
    private var totalElapsed by timerState::totalElapsed
    private var currentStage by timerState::currentStage
    private var currentRemaining by timerState::currentRemaining
    private var nextStage by timerState::nextStage
    private var isRunning by timerState::running

    private val actionInterval: Stack<Duration>
        get() = timerActionService.actionInterval
            .filter { it < currentStage }
            .asStack()

    private val period: Duration get() = timerSettings.refreshInterval.milliseconds

    fun start(stages: List<Duration> = mStages) {
        if (!isRunning) {
            resetState(stages)
            timerJob = coroutineScope.launch {
                var stageIndex = 0
                var preElapsed = Duration.ZERO
                while (isActive && stageIndex < stages.size) {
                    preElapsed = runStage(this, stageIndex, preElapsed) - stages.getStage(stageIndex)
                    stageIndex++
                }
                isRunning = false
                resetState()
            }
            isRunning = true
        }
    }

    private suspend fun runStage(scope: CoroutineScope, stageIndex: Int, preElapsed: Duration): Duration {
        var elapsed = preElapsed
        var adjustedDelay = period
        var lastTimestamp = Instant.now()
        currentStage = stages.getStage(stageIndex)

        val actionInterval = this.actionInterval
        while (scope.isActive && elapsed < currentStage) {
            delay(adjustedDelay.toMillis())

            val now = Instant.now()
            val delta = Duration.between(lastTimestamp, now)
            adjustedDelay -= delta - period
            lastTimestamp = now
            elapsed += delta

            updateState(stageIndex, delta, elapsed)
            if (!currentStage.isIndefinite && currentRemaining <= actionInterval.peek()) {
                timerActionService.invokeAction()
                actionInterval.pop()
            }
        }
        return elapsed
    }

    fun stop() {
        if (timerState.running) {
            timerJob.cancel()
            isRunning = false
            resetState()
        }
    }

    private fun updateState(
        stageIndex: Int = 0,
        delta: Duration = Duration.ZERO,
        elapsed: Duration = Duration.ZERO
    ) {
        currentStage = stages.getStage(stageIndex)
        currentRemaining = if (!currentStage.isIndefinite)
            currentStage - elapsed
        else
            elapsed
        totalElapsed += delta
    }

    private fun resetState(stages: List<Duration> = this.mStages) {
        this.mStages = stages
        this.stages = stages.toMutableList()

        totalTime = mStages.sum()
        totalElapsed = Duration.ZERO
        currentStage = mStages.getStage(0)
        currentRemaining = if (!currentStage.isIndefinite) currentStage else Duration.ZERO
        nextStage = mStages.getStage(1)
    }
}