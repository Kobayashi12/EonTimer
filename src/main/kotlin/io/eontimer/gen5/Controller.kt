package io.eontimer.gen5

import io.eontimer.TimerController
import io.eontimer.TimerState
import io.eontimer.TimerTab
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.disableWhen
import io.eontimer.util.javafx.or
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.showWhen
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
import io.eontimer.util.javafx.spinner.text
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component

@Component("gen5Controller")
@ExperimentalCoroutinesApi
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
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.mode)
        modeField.parent.disableWhen(state.running)

        calibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.calibration) }
        calibrationField.parent.disableWhen(state.running)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        targetDelayField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetDelay) }
        targetDelayField.parent.disableWhen(state.running)
        targetDelayField.parent.showWhen(
            model.mode.isEqualTo(Mode.C_GEAR)
                or model.mode.isEqualTo(Mode.ENTRALINK)
                or model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetSecond) }
        targetSecondField.parent.disableWhen(state.running)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        entralinkCalibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.entralinkCalibration) }
        entralinkCalibrationField.parent.disableWhen(state.running)
        entralinkCalibrationField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENTRALINK)
                or model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        entralinkCalibrationField.setOnFocusLost(entralinkCalibrationField::commitValue)

        frameCalibrationField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.frameCalibration) }
        frameCalibrationField.parent.disableWhen(state.running)
        frameCalibrationField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        frameCalibrationField.setOnFocusLost(frameCalibrationField::commitValue)

        targetAdvancesField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetAdvances) }
        targetAdvancesField.parent.disableWhen(state.running)
        targetAdvancesField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        targetAdvancesField.setOnFocusLost(targetAdvancesField::commitValue)

        secondHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.secondHit) }
        secondHitField.parent.disableWhen(state.running)
        secondHitField.parent.showWhen(
            model.mode.isEqualTo(Mode.STANDARD)
                or model.mode.isEqualTo(Mode.ENTRALINK)
                or model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        secondHitField.setOnFocusLost(secondHitField::commitValue)
        secondHitField.text = ""

        delayHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.delayHit) }
        delayHitField.parent.disableWhen(state.running)
        delayHitField.parent.showWhen(
            model.mode.isEqualTo(Mode.C_GEAR)
                or model.mode.isEqualTo(Mode.ENTRALINK)
                or model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""

        actualAdvancesField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.advancesHit) }
        actualAdvancesField.parent.disableWhen(state.running)
        actualAdvancesField.parent.showWhen(
            model.mode.isEqualTo(Mode.ENHANCED_ENTRALINK)
        )
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