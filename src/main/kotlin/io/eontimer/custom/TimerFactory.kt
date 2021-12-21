package io.eontimer.custom

import io.eontimer.model.TimerState
import io.eontimer.service.factory.TimerFactory
import javafx.collections.ListChangeListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component("customTimerFactory")
@ExperimentalCoroutinesApi
class TimerFactory(
    private val model: Model,
    private val timerState: TimerState
) : TimerFactory {
    override val stages: List<Duration>
        get() = model.stages
            .map { Duration.ofMillis(it) }

    @PostConstruct
    private fun initialize() {
        model.stages.addListener(ListChangeListener {
            timerState.update(stages)
        })
    }

    override fun calibrate() = Unit
}