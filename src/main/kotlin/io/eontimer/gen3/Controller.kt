package io.eontimer.gen3

import io.eontimer.model.TimerState
import io.eontimer.service.CalibrationService
import io.eontimer.service.TimerRunnerService
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.bindBidirectional
import io.eontimer.util.javafx.showWhen
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.setOnFocusLost
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.javafx.spinner.valueProperty
import io.eontimer.util.milliseconds
import io.eontimer.util.sum
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component("gen3Controller")
@ExperimentalCoroutinesApi
class Controller(
    private val model: Model,
    private val timerState: TimerState,
    private val timerFactory: TimerFactory,
    private val timerRunnerService: TimerRunnerService,
    private val calibrationService: CalibrationService,
    private val coroutineScope: CoroutineScope
) {
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
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.mode)
        modeField.parent.disableProperty().bind(timerState.runningProperty)

        calibrationField.valueFactory = LongValueFactory()
        calibrationField.valueProperty!!.bindBidirectional(model.calibration)
        calibrationField.parent.disableProperty().bind(timerState.runningProperty)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        preTimerField.valueFactory = LongValueFactory(0)
        preTimerField.valueProperty!!.bindBidirectional(model.preTimer)
        preTimerField.parent.disableProperty().bind(timerState.runningProperty)
        preTimerField.setOnFocusLost(preTimerField::commitValue)

        targetFrameField.valueFactory = LongValueFactory(0)
        targetFrameField.valueProperty!!.bindBidirectional(model.targetFrame)
        targetFrameField.parent.disableProperty().bind(
            model.mode.isEqualTo(Mode.VARIABLE_TARGET)
                .and(
                    timerState.runningProperty.not()
                        .or(primed.not())
                )
                .or(
                    model.mode.isEqualTo(Mode.STANDARD)
                        .and(timerState.runningProperty)
                )
        )
        targetFrameField.setOnFocusLost(targetFrameField::commitValue)

        setTargetFrameBtn.showWhen(model.mode.isEqualTo(Mode.VARIABLE_TARGET))
        setTargetFrameBtn.disableProperty().bind(primed.not())
        setTargetFrameBtn.setOnAction {
            if (timerState.running) {
                val duration = calibrationService.toMillis(model.targetFrame.get())
                timerRunnerService.stages[1] = (duration + model.calibration.get()).milliseconds
                timerState.totalTime = timerRunnerService.stages.sum()
                primed.set(false)
            }
        }

        frameHitField.valueFactory = LongValueFactory(0)
        frameHitField.valueProperty!!.bindBidirectional(model.frameHit)
        frameHitField.parent.disableProperty().bind(timerState.runningProperty)
        frameHitField.setOnFocusLost(frameHitField::commitValue)
        frameHitField.text = ""

        coroutineScope.launch {
            timerState.runningProperty.asFlow()
                .collect { primed.set(it) }
        }
    }

    fun calibrate() {
        timerFactory.calibrate()
        frameHitField.text = ""
    }
}