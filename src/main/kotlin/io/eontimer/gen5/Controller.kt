package io.eontimer.gen5

import io.eontimer.model.TimerState
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.bindBidirectional
import io.eontimer.util.javafx.disableWhen
import io.eontimer.util.javafx.or
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.showWhen
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
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
        modeField.parent.disableWhen(timerState.running)

        calibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.calibration) }
        calibrationField.parent.disableWhen(timerState.running)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        targetDelayField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetDelay) }
        targetDelayField.parent.disableWhen(timerState.running)
        targetDelayField.parent.showWhen(
            model.mode.isEqualTo(Mode.C_GEAR)
                or model.mode.isEqualTo(Mode.ENTRALINK)
                or model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetSecond) }
        targetSecondField.parent.disableWhen(timerState.running)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        entralinkCalibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.entralinkCalibration) }
        entralinkCalibrationField.parent.disableWhen(timerState.running)
        entralinkCalibrationField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENTRALINK)
                or model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        entralinkCalibrationField.setOnFocusLost(entralinkCalibrationField::commitValue)

        frameCalibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.frameCalibration) }
        frameCalibrationField.parent.disableWhen(timerState.running)
        frameCalibrationField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        frameCalibrationField.setOnFocusLost(frameCalibrationField::commitValue)

        targetAdvancesField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetAdvances) }
        targetAdvancesField.parent.disableWhen(timerState.running)
        targetAdvancesField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        targetAdvancesField.setOnFocusLost(targetAdvancesField::commitValue)

        secondHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.secondHit) }
        secondHitField.parent.disableWhen(timerState.running)
        secondHitField.parent.showWhen(
            model.mode.isEqualTo(Mode.STANDARD)
                or model.mode.isEqualTo(Mode.ENTRALINK)
                or model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        secondHitField.setOnFocusLost(secondHitField::commitValue)
        secondHitField.text = ""

        delayHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.delayHit) }
        delayHitField.parent.disableWhen(timerState.running)
        delayHitField.parent.showWhen(
            model.mode.isEqualTo(Mode.C_GEAR)
                or model.mode.isEqualTo(Mode.ENTRALINK)
                or model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""

        actualAdvancesField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.advancesHit) }
        actualAdvancesField.parent.disableWhen(timerState.running)
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