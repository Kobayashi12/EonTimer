package io.eontimer.gen3

import io.eontimer.TimerController
import io.eontimer.model.TimerState
import io.eontimer.model.timer.TimerTab
import io.eontimer.service.CalibrationService
import io.eontimer.service.TimerRunnerService
import io.eontimer.util.javafx.and
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.disableWhen
import io.eontimer.util.javafx.onChange
import io.eontimer.util.javafx.or
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.showWhen
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.milliseconds
import io.eontimer.util.sum
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component

@Component("gen3Controller")
@ExperimentalCoroutinesApi
class Controller(
    override val model: Model,
    override val timerState: TimerState,
    private val timerFactory: TimerFactory,
    private val timerRunnerService: TimerRunnerService,
    private val calibrationService: CalibrationService,
) : TimerController<Model> {
    override val timerTab = TimerTab.GEN3

    // @formatter:off
    @FXML private lateinit var modeField: ChoiceBox<Mode>
    @FXML private lateinit var calibrationField: Spinner<Long>
    @FXML private lateinit var preTimerField: Spinner<Long>
    @FXML private lateinit var targetFrameField: Spinner<Long>
    @FXML private lateinit var setTargetFrameBtn: Button
    @FXML private lateinit var frameHitField: Spinner<Long>
    // @formatter:on

    private val primed = SimpleBooleanProperty(true)

    fun initialize() {
        modeField.asChoiceField()
            .valueProperty
            .bindBidirectional(model.mode)
        modeField.parent.disableWhen(timerState.running)

        calibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.calibration) }
        calibrationField.parent.disableWhen(timerState.running)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        preTimerField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.preTimer) }
        preTimerField.parent.disableWhen(timerState.running)
        preTimerField.setOnFocusLost(preTimerField::commitValue)

        targetFrameField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetFrame) }
        targetFrameField.parent.disableWhen(
            (model.mode.isEqualTo(Mode.VARIABLE_TARGET) and !(timerState.running or primed))
                or (model.mode.isEqualTo(Mode.STANDARD) and timerState.running)
        )
        targetFrameField.setOnFocusLost(targetFrameField::commitValue)

        setTargetFrameBtn.showWhen(model.mode.isEqualTo(Mode.VARIABLE_TARGET))
        setTargetFrameBtn.disableWhen(!primed)
        setTargetFrameBtn.setOnAction {
            if (timerState.running.get()) {
                val duration = calibrationService.toMillis(model.targetFrame.get())
                timerRunnerService.stages[1] = (duration + model.calibration.get()).milliseconds
                timerState.totalTime.set(timerRunnerService.stages.sum())
                primed.set(false)
            }
        }

        frameHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.frameHit) }
        frameHitField.parent.disableWhen(timerState.running)
        frameHitField.setOnFocusLost(frameHitField::commitValue)
        frameHitField.text = ""

        timerState.running.onChange(fn = primed::set)
    }

    override fun calibrate() {
        timerFactory.calibrate()
        frameHitField.text = ""
    }
}