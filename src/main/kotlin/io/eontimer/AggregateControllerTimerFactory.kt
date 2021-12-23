package io.eontimer

import io.eontimer.util.javafx.getValue
import javafx.beans.property.ObjectProperty
import org.springframework.stereotype.Component
import kotlin.time.Duration
import java.util.*

@Component
class AggregateControllerTimerFactory(
    selectedTimerTab: ObjectProperty<TimerTab>,
    gen3ControllerTimerFactory: ControllerTimerFactory,
    gen4ControllerTimerFactory: ControllerTimerFactory,
    gen5ControllerTimerFactory: ControllerTimerFactory,
    customControllerTimerFactory: ControllerTimerFactory,
) : ControllerTimerFactory {
    private val selectedTimerTab by selectedTimerTab
    private val timerTabControllers = EnumMap(
        mapOf(
            TimerTab.GEN3 to gen3ControllerTimerFactory,
            TimerTab.GEN4 to gen4ControllerTimerFactory,
            TimerTab.GEN5 to gen5ControllerTimerFactory,
            TimerTab.CUSTOM to customControllerTimerFactory
        )
    )

    override val stages: List<Duration>
        get() = timerTabControllers[selectedTimerTab]!!.stages

    override fun calibrate() = timerTabControllers[selectedTimerTab]!!.calibrate()

//    @PostConstruct
//    private fun initialize() {
//        model.selectedTimerTab
//            .subscribe { resetTimerState() }
//        timerSettings.console
//            .subscribe { resetTimerState() }
//        timerSettings.precisionCalibrationMode
//            .subscribe { resetTimerState() }
//    }
}