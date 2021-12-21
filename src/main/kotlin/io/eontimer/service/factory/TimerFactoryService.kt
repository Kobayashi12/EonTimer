package io.eontimer.service.factory

import io.eontimer.model.ApplicationModel
import io.eontimer.model.TimerState
import io.eontimer.model.timer.TimerTab
import io.eontimer.timer.Settings
import io.eontimer.util.javafx.easybind.subscribe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@ExperimentalCoroutinesApi
@Component
class TimerFactoryService(
    private val model: ApplicationModel,
    override val timerState: TimerState,
    private val gen3TimerFactory: TimerFactory,
    private val gen4TimerFactory: TimerFactory,
    private val gen5TimerFactory: TimerFactory,
    private val customTimerFactory: TimerFactory,
    private val timerSettings: Settings
) : TimerFactory {


    @PostConstruct
    private fun initialize() {
        model.selectedTimerTab
            .subscribe { resetTimerState() }
        timerSettings.console
            .subscribe { resetTimerState() }
        timerSettings.precisionCalibrationMode
            .subscribe { resetTimerState() }
    }

    override val stages: List<Duration>
        get() = TODO("Not yet implemented")

    override fun calibrate() = timerFactory.calibrate()

    private val TimerTab.timerFactory: TimerFactory
        get() = when (this) {
            TimerTab.GEN3 -> gen3TimerFactory
            TimerTab.GEN4 -> gen4TimerFactory
            TimerTab.GEN5 -> gen5TimerFactory
            TimerTab.CUSTOM -> customTimerFactory
        }
}