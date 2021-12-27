package io.eontimer.gen3

import io.eontimer.Calibrator
import io.eontimer.TimerController
import io.eontimer.TimerState
import io.eontimer.TimerTab
import io.eontimer.resetTimerState
import io.eontimer.util.javafx.anyChangesOf
import io.eontimer.util.javafx.filter
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.initializeChoices
import io.eontimer.util.javafx.mapToBoolean
import io.eontimer.util.javafx.onPlatform
import io.eontimer.util.javafx.or
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.setValue
import io.eontimer.util.javafx.showWhen
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.javafx.subscribe
import io.eontimer.util.toBuilder
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.milliseconds

@Component("gen3TimerController")
class Gen3TimerController(
    override val model: Gen3TimerModel,
    override val state: TimerState,
    override val timerFactory: Gen3ControllerTimerFactory,
    private val coroutineScope: CoroutineScope,
    private val calibrator: Calibrator,
) : TimerController<Gen3TimerModel, Gen3ControllerTimerFactory> {
    override val timerTab = TimerTab.GEN3

    // @formatter:off
    @FXML private lateinit var modeField: ChoiceBox<Gen3TimerMode>
    @FXML private lateinit var calibrationField: Spinner<Long>
    @FXML private lateinit var preTimerField: Spinner<Long>
    @FXML private lateinit var targetFrameField: Spinner<Long>
    @FXML private lateinit var setTargetFrameBtn: Button
    @FXML private lateinit var frameHitField: Spinner<Long>
    // @formatter:on

    private val primedProperty = SimpleBooleanProperty(false)
    private var primed by primedProperty

    fun initialize() {
        anyChangesOf(
            model.mode,
            model.preTimer,
            model.targetFrame,
            state.runningProperty.filter { !it }
        ) {
            resetTimerState()
        }

        modeField.initializeChoices()
        modeField.valueProperty()
            .bindBidirectional(model.mode)
        modeField.parent
            .disableProperty()
            .bind(state.runningProperty)

        calibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.calibration) }
        calibrationField.parent
            .disableProperty()
            .bind(state.runningProperty)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        preTimerField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.preTimer) }
        preTimerField.parent
            .disableProperty()
            .bind(state.runningProperty)
        preTimerField.setOnFocusLost(preTimerField::commitValue)

        val standardDisabled = model.mode.isEqualTo(Gen3TimerMode.STANDARD)
            .and(state.runningProperty)
        val variableTargetDisabled = model.mode.isEqualTo(Gen3TimerMode.VARIABLE_TARGET)
            .and(state.currentStageProperty.mapToBoolean { !it.isInfinite() }
                .or(!primedProperty))

        targetFrameField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetFrame) }
        targetFrameField.parent
            .disableProperty()
            .bind(standardDisabled.or(variableTargetDisabled))
        targetFrameField.setOnFocusLost(targetFrameField::commitValue)

        setTargetFrameBtn.disableProperty()
            .bind(variableTargetDisabled)
        setTargetFrameBtn.showWhen(model.mode.isEqualTo(Gen3TimerMode.VARIABLE_TARGET))
        setTargetFrameBtn.setOnAction { setTargetFrame() }

        frameHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.frameHit) }
        frameHitField.parent
            .disableProperty()
            .bind(state.runningProperty)
        frameHitField.setOnFocusLost(frameHitField::commitValue)
        frameHitField.text = ""

        state.runningProperty
            .subscribe {
                primed = it
            }
    }

    override fun calibrate() {
        timerFactory.calibrate()
        frameHitField.text = ""
    }

    private fun setTargetFrame() {
        coroutineScope.launch {
            if (state.running && state.currentStage.isInfinite()) {
                val duration = calibrator.toMillis(model.targetFrame.get())
                val newStages = state.stages.toBuilder {
                    set(state.stageIndex, (duration + model.calibration.get()).milliseconds)
                }
                onPlatform {
                    state.stages = newStages
                    primed = false
                }
            }
        }
    }
}