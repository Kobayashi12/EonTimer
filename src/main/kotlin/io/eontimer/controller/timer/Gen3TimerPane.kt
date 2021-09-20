package io.eontimer.controller.timer

import io.eontimer.model.TimerState
import io.eontimer.model.timer.Gen3Timer
import io.eontimer.service.CalibrationService
import io.eontimer.service.TimerRunnerService
import io.eontimer.service.factory.Gen3TimerFactory
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.bindBidirectional
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import io.eontimer.util.javafx.showWhen
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.setOnFocusLost
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.javafx.spinner.valueProperty
import io.eontimer.util.milliseconds
import io.eontimer.util.sum
import javafx.beans.property.BooleanProperty
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

@Component
@ExperimentalCoroutinesApi
class Gen3TimerPane(
    private val model: Gen3Timer,
    private val timerState: TimerState,
    private val timerFactory: Gen3TimerFactory,
    private val timerRunnerService: TimerRunnerService,
    private val calibrationService: CalibrationService,
    private val coroutineScope: CoroutineScope
) {
    // @formatter:off
    @FXML private lateinit var modeField: ChoiceBox<Gen3Timer.Mode>
    @FXML private lateinit var calibrationField: Spinner<Long>
    @FXML private lateinit var preTimerField: Spinner<Long>
    @FXML private lateinit var targetFrameField: Spinner<Long>
    @FXML private lateinit var setTargetFrameBtn: Button
    @FXML private lateinit var frameHitField: Spinner<Long>
    // @formatter:on

    private val isPrimedProperty: BooleanProperty = SimpleBooleanProperty(true)
    private var isPrimed by isPrimedProperty

    fun initialize() {
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.modeProperty)
        modeField.parent.disableProperty().bind(timerState.runningProperty)

        calibrationField.valueFactory = LongValueFactory()
        calibrationField.valueProperty!!.bindBidirectional(model.calibrationProperty)
        calibrationField.parent.disableProperty().bind(timerState.runningProperty)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        preTimerField.valueFactory = LongValueFactory(0)
        preTimerField.valueProperty!!.bindBidirectional(model.preTimerProperty)
        preTimerField.parent.disableProperty().bind(timerState.runningProperty)
        preTimerField.setOnFocusLost(preTimerField::commitValue)

        targetFrameField.valueFactory = LongValueFactory(0)
        targetFrameField.valueProperty!!.bindBidirectional(model.targetFrameProperty)
        targetFrameField.parent.disableProperty().bind(
            model.modeProperty.isEqualTo(Gen3Timer.Mode.VARIABLE_TARGET)
                .and(
                    timerState.runningProperty.not()
                        .or(isPrimedProperty.not())
                )
                .or(
                    model.modeProperty.isEqualTo(Gen3Timer.Mode.STANDARD)
                        .and(timerState.runningProperty)
                )
        )
        targetFrameField.setOnFocusLost(targetFrameField::commitValue)

        setTargetFrameBtn.showWhen(
            model.modeProperty
                .isEqualTo(Gen3Timer.Mode.VARIABLE_TARGET)
        )
        setTargetFrameBtn.disableProperty().bind(isPrimedProperty.not())
        setTargetFrameBtn.setOnAction {
            if (timerState.running) {
                val duration = calibrationService.toMillis(model.targetFrame)
                timerRunnerService.stages[1] = (duration + model.calibration).milliseconds
                timerState.totalTime = timerRunnerService.stages.sum()
                isPrimed = false
            }
        }

        frameHitField.valueFactory = LongValueFactory(0)
        frameHitField.valueProperty!!.bindBidirectional(model.frameHitProperty)
        frameHitField.parent.disableProperty().bind(timerState.runningProperty)
        frameHitField.setOnFocusLost(frameHitField::commitValue)
        frameHitField.text = ""

        coroutineScope.launch {
            timerState.runningProperty.asFlow()
                .collect { isPrimed = it }
        }
    }

    fun calibrate() {
        timerFactory.calibrate()
        frameHitField.text = ""
    }
}