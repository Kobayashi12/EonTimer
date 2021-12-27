package io.eontimer.gen5

import io.eontimer.TimerController
import io.eontimer.TimerState
import io.eontimer.TimerTab
import io.eontimer.resetTimerState
import io.eontimer.util.javafx.anyChangesOf
import io.eontimer.util.javafx.initializeChoices
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.showWhen
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
import io.eontimer.util.javafx.spinner.text
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import org.springframework.stereotype.Component

@Component("gen5TimerController")
class Controller(
    override val model: Model,
    override val state: TimerState,
    override val timerFactory: ControllerTimerFactory
) : TimerController<Model, ControllerTimerFactory> {
    override val timerTab = TimerTab.GEN5

    // @formatter:off
    @FXML private lateinit var modeField: ChoiceBox<Mode>
    @FXML private lateinit var calibrationField: Spinner<Long>
    @FXML private lateinit var targetDelayField: Spinner<Long>
    @FXML private lateinit var targetSecondField: Spinner<Long>
    @FXML private lateinit var entralinkCalibrationField: Spinner<Long>
    @FXML private lateinit var frameCalibrationField: Spinner<Long>
    @FXML private lateinit var targetAdvancesField: Spinner<Long>
    @FXML private lateinit var secondHitField: Spinner<Long>
    @FXML private lateinit var delayHitField: Spinner<Long>
    @FXML private lateinit var actualAdvancesField: Spinner<Long>
    // @formatter:on

    fun initialize() {
        anyChangesOf(
            model.mode,
            model.targetDelay,
            model.targetSecond,
            model.targetAdvances,
            model.calibration,
            model.frameCalibration,
            model.entralinkCalibration
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

        targetDelayField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetDelay) }
        targetDelayField.parent
            .disableProperty()
            .bind(state.runningProperty)
        targetDelayField.parent
            .showWhen(
                model.mode.isEqualTo(Mode.C_GEAR)
                    .or(model.mode.isEqualTo(Mode.ENTRALINK))
                    .or(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
            )
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetSecond) }
        targetSecondField.parent
            .disableProperty()
            .bind(state.runningProperty)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        entralinkCalibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.entralinkCalibration) }
        entralinkCalibrationField.parent
            .disableProperty()
            .bind(state.runningProperty)
        entralinkCalibrationField.parent
            .showWhen(
                model.mode.isEqualTo(Mode.ENTRALINK)
                    .or(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
            )
        entralinkCalibrationField.setOnFocusLost(entralinkCalibrationField::commitValue)

        frameCalibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.frameCalibration) }
        frameCalibrationField.parent
            .disableProperty()
            .bind(state.runningProperty)
        frameCalibrationField.parent
            .showWhen(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
        frameCalibrationField.setOnFocusLost(frameCalibrationField::commitValue)

        targetAdvancesField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetAdvances) }
        targetAdvancesField.parent
            .disableProperty()
            .bind(state.runningProperty)
        targetAdvancesField.parent
            .showWhen(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
        targetAdvancesField.setOnFocusLost(targetAdvancesField::commitValue)

        secondHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.secondHit) }
        secondHitField.parent
            .disableProperty()
            .bind(state.runningProperty)
        secondHitField.parent
            .showWhen(
                model.mode.isEqualTo(Mode.STANDARD)
                    .or(model.mode.isEqualTo(Mode.ENTRALINK))
                    .or(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
            )
        secondHitField.setOnFocusLost(secondHitField::commitValue)
        secondHitField.text = ""

        delayHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.delayHit) }
        delayHitField.parent
            .disableProperty()
            .bind(state.runningProperty)
        delayHitField.parent
            .showWhen(
                model.mode.isEqualTo(Mode.C_GEAR)
                    .or(model.mode.isEqualTo(Mode.ENTRALINK))
                    .or(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
            )
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""

        actualAdvancesField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.advancesHit) }
        actualAdvancesField.parent
            .disableProperty()
            .bind(state.runningProperty)
        actualAdvancesField.parent
            .showWhen(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
        actualAdvancesField.setOnFocusLost(actualAdvancesField::commitValue)
        actualAdvancesField.text = ""
    }

    override fun calibrate() {
        timerFactory.calibrate()
        secondHitField.text = ""
        delayHitField.text = ""
        actualAdvancesField.text = ""
    }
}