package io.eontimer.gen3

import io.eontimer.Calibrator
import io.eontimer.TimerController
import io.eontimer.TimerRunner
import io.eontimer.TimerState
import io.eontimer.TimerStateProxy
import io.eontimer.TimerTab
import io.eontimer.resetTimerState
import io.eontimer.util.isIndefinite
import io.eontimer.util.javafx.and
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.disableWhen
import io.eontimer.util.javafx.easybind.subscribe
import io.eontimer.util.javafx.or
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.showWhen
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.milliseconds
import io.eontimer.util.toBuilder
import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import org.springframework.stereotype.Component
import kotlin.time.Duration

@Component("gen3Controller")
class Controller(
    override val model: Model,
    override val state: TimerState,
    override val timerFactory: ControllerTimerFactory,
    private val calibrator: Calibrator,
    private val timerRunner: TimerRunner,
) : TimerController<Model, ControllerTimerFactory> {
    override val timerTab = TimerTab.GEN3
    private val stateProxy = TimerStateProxy(state)

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
        model.mode.subscribe { resetTimerState() }

        modeField.asChoiceField()
            .valueProperty
            .bindBidirectional(model.mode)
        modeField.parent.disableWhen(state.running)

        calibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.calibration) }
        calibrationField.parent.disableWhen(state.running)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        preTimerField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.preTimer) }
        preTimerField.parent.disableWhen(state.running)
        preTimerField.setOnFocusLost(preTimerField::commitValue)

        targetFrameField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetFrame) }
        targetFrameField.parent.disableWhen(
            (model.mode.isEqualTo(Mode.VARIABLE_TARGET) and !(state.running or primed))
                or (model.mode.isEqualTo(Mode.STANDARD) and state.running)
        )
        targetFrameField.setOnFocusLost(targetFrameField::commitValue)

        setTargetFrameBtn.showWhen(model.mode.isEqualTo(Mode.VARIABLE_TARGET))
        setTargetFrameBtn.disableWhen(!primed)
        setTargetFrameBtn.setOnAction {
            if (stateProxy.running && stateProxy.stage.isIndefinite) {
                val duration = calibrator.toMillis(model.targetFrame.get())
                stateProxy.stages = stateProxy.stages.toBuilder {
                    set(stateProxy.stageIndex, (duration + model.calibration.get()).milliseconds)
                }
                // state.totalTime.set(timerRunnerService.stages.sum())
                primed.set(false)
            }
        }

        frameHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.frameHit) }
        frameHitField.parent.disableWhen(state.running)
        frameHitField.setOnFocusLost(frameHitField::commitValue)
        frameHitField.text = ""

        state.running.subscribe { running ->
            Platform.runLater {
                primed.set(running)
            }
        }
    }

    override fun calibrate() {
        timerFactory.calibrate()
        frameHitField.text = ""
    }
}