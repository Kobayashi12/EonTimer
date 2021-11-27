package io.eontimer.custom

import io.eontimer.model.TimerState
import io.eontimer.service.factory.TimerFactory
import io.eontimer.service.factory.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component("customTimerFactory")
@ExperimentalCoroutinesApi
class TimerFactory(
    private val model: Model,
    private val timerState: TimerState,
    private val coroutineScope: CoroutineScope
) : TimerFactory {
    override val stages: List<Duration>
        get() = model.stages
            .map { Duration.ofMillis(it) }

    @PostConstruct
    private fun initialize() {
        coroutineScope.launch {
            model.stages.asFlow()
                .collect {
                    timerState.update(stages)
                }
        }
    }

    override fun calibrate() = Unit
}