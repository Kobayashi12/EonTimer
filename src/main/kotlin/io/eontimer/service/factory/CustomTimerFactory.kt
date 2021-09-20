package io.eontimer.service.factory

import io.eontimer.model.TimerState
import io.eontimer.model.timer.CustomTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component
@ExperimentalCoroutinesApi
class CustomTimerFactory(
    private val customTimer: CustomTimer,
    private val timerState: TimerState,
    private val coroutineScope: CoroutineScope
) : TimerFactory {

    override val stages: List<Duration>
        get() = customTimer.stages
            .map { Duration.ofMillis(it.length) }

    @PostConstruct
    private fun initialize() {
        coroutineScope.launch {
            customTimer.stages.asFlow()
                .collect {
                    timerState.update(stages)
                }
        }
    }

    override fun calibrate() = Unit
}