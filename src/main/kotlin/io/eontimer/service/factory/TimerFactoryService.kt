package io.eontimer.service.factory

import io.eontimer.model.ApplicationModel
import io.eontimer.model.TimerState
import io.eontimer.model.settings.TimerSettings
import io.eontimer.model.timer.TimerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@ExperimentalCoroutinesApi
@Component
class TimerFactoryService(
    private val timerState: TimerState,
    private val gen3TimerFactory: TimerFactory,
    private val gen4TimerFactory: TimerFactory,
    private val gen5TimerFactory: TimerFactory,
    private val customTimerFactory: TimerFactory,
    private val applicationModel: ApplicationModel,
    private val timerSettings: TimerSettings,
    private val coroutineScope: CoroutineScope
) {
    val stages: List<Duration> get() = timerFactory.stages
    private val timerFactory: TimerFactory get() = applicationModel.selectedTimerType.timerFactory

    @PostConstruct
    private fun initialize() {
        coroutineScope.launch {
            applicationModel.selectedTimerTypeProperty.asFlow()
                .collect { timerState.update(it.timerFactory.stages) }
            listOf(
                timerSettings.consoleProperty,
                timerSettings.precisionCalibrationModeProperty
            ).forEach {
                it.asFlow().collect {
                    timerState.update(stages)
                }
            }
        }
    }

    fun calibrate() = timerFactory.calibrate()

    private val TimerType.timerFactory: TimerFactory
        get() = when (this) {
            TimerType.GEN3 -> gen3TimerFactory
            TimerType.GEN4 -> gen4TimerFactory
            TimerType.GEN5 -> gen5TimerFactory
            TimerType.CUSTOM -> customTimerFactory
        }
}