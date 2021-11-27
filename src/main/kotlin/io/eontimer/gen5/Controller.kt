package io.eontimer.gen5

import io.eontimer.model.TimerState
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.bindBidirectional
import io.eontimer.util.javafx.showWhen
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.setOnFocusLost
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.javafx.spinner.valueProperty
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component

@Component("gen5Controller")
@ExperimentalCoroutinesApi
class Controller(
    private val model: Model,
    private val timerState: TimerState,
    private val timerFactory: TimerFactory
) {
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
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.mode)
        modeField.parent.disableProperty().bind(timerState.runningProperty)

        calibrationField.valueFactory = LongValueFactory()
        calibrationField.valueProperty!!.bindBidirectional(model.calibration)
        calibrationField.parent.disableProperty().bind(timerState.runningProperty)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        targetDelayField.valueFactory = LongValueFactory(0)
        targetDelayField.valueProperty!!.bindBidirectional(model.targetDelay)
        targetDelayField.parent.disableProperty().bind(timerState.runningProperty)
        targetDelayField.parent.showWhen(
            model.mode.isEqualTo(Mode.C_GEAR)
                .or(model.mode.isEqualTo(Mode.ENTRALINK))
                .or(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
        )
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(0)
        targetSecondField.valueProperty!!.bindBidirectional(model.targetSecond)
        targetSecondField.parent.disableProperty().bind(timerState.runningProperty)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        entralinkCalibrationField.valueFactory = LongValueFactory()
        entralinkCalibrationField.valueProperty!!.bindBidirectional(model.entralinkCalibration)
        entralinkCalibrationField.parent.disableProperty().bind(timerState.runningProperty)
        entralinkCalibrationField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENTRALINK)
                .or(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
        )
        entralinkCalibrationField.setOnFocusLost(entralinkCalibrationField::commitValue)

        frameCalibrationField.valueFactory = LongValueFactory()
        frameCalibrationField.valueProperty!!.bindBidirectional(model.frameCalibration)
        frameCalibrationField.parent.disableProperty().bind(timerState.runningProperty)
        frameCalibrationField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        frameCalibrationField.setOnFocusLost(frameCalibrationField::commitValue)

        targetAdvancesField.valueFactory = LongValueFactory(0)
        targetAdvancesField.valueProperty!!.bindBidirectional(model.targetAdvances)
        targetAdvancesField.parent.disableProperty().bind(timerState.runningProperty)
        targetAdvancesField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        targetAdvancesField.setOnFocusLost(targetAdvancesField::commitValue)

        secondHitField.valueFactory = LongValueFactory(0)
        secondHitField.valueProperty!!.bindBidirectional(model.secondHit)
        secondHitField.parent.disableProperty().bind(timerState.runningProperty)
        secondHitField.parent.showWhen(
            model.mode.isEqualTo(Mode.STANDARD)
                .or(model.mode.isEqualTo(Mode.ENTRALINK))
                .or(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
        )
        secondHitField.setOnFocusLost(secondHitField::commitValue)
        secondHitField.text = ""

        delayHitField.valueFactory = LongValueFactory(0)
        delayHitField.valueProperty!!.bindBidirectional(model.delayHit)
        delayHitField.parent.disableProperty().bind(timerState.runningProperty)
        delayHitField.parent.showWhen(
            model.mode.isEqualTo(Mode.C_GEAR)
                .or(model.mode.isEqualTo(Mode.ENTRALINK))
                .or(model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK))
        )
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""

        actualAdvancesField.valueFactory = LongValueFactory(0)
        actualAdvancesField.valueProperty!!.bindBidirectional(model.advancesHit)
        actualAdvancesField.parent.disableProperty().bind(timerState.runningProperty)
        actualAdvancesField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        actualAdvancesField.setOnFocusLost(actualAdvancesField::commitValue)
        actualAdvancesField.text = ""
    }

    fun calibrate() {
        timerFactory.calibrate()
        secondHitField.text = ""
        delayHitField.text = ""
        actualAdvancesField.text = ""
    }
}