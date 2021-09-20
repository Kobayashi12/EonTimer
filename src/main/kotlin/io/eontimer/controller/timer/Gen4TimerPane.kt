package io.eontimer.controller.timer

import io.eontimer.model.TimerState
import io.eontimer.model.timer.Gen4Timer
import io.eontimer.service.factory.Gen4TimerFactory
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.bindBidirectional
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.setOnFocusLost
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.javafx.spinner.valueProperty
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component

@Component
@ExperimentalCoroutinesApi
class Gen4TimerPane(
    private val model: Gen4Timer,
    private val timerState: TimerState,
    private val timerFactory: Gen4TimerFactory
) {
    // @formatter:off
    @FXML private lateinit var modeField: ChoiceBox<Gen4Timer.Mode>
    @FXML private lateinit var calibratedDelayField: Spinner<Long>
    @FXML private lateinit var calibratedSecondField: Spinner<Long>
    @FXML private lateinit var targetDelayField: Spinner<Long>
    @FXML private lateinit var targetSecondField: Spinner<Long>
    @FXML private lateinit var delayHitField: Spinner<Long>
    // @formatter:on

    fun initialize() {
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.modeProperty)
        modeField.parent.disableProperty().bind(timerState.runningProperty)

        calibratedDelayField.valueFactory = LongValueFactory()
        calibratedDelayField.valueProperty!!.bindBidirectional(model.calibratedDelayProperty)
        calibratedDelayField.parent.disableProperty().bind(timerState.runningProperty)
        calibratedDelayField.setOnFocusLost(calibratedDelayField::commitValue)

        calibratedSecondField.valueFactory = LongValueFactory()
        calibratedSecondField.valueProperty!!.bindBidirectional(model.calibratedSecondProperty)
        calibratedSecondField.parent.disableProperty().bind(timerState.runningProperty)
        calibratedSecondField.setOnFocusLost(calibratedSecondField::commitValue)

        targetDelayField.valueFactory = LongValueFactory(0)
        targetDelayField.valueProperty!!.bindBidirectional(model.targetDelayProperty)
        targetDelayField.parent.disableProperty().bind(timerState.runningProperty)
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(0)
        targetSecondField.valueProperty!!.bindBidirectional(model.targetSecondProperty)
        targetSecondField.parent.disableProperty().bind(timerState.runningProperty)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        delayHitField.valueFactory = LongValueFactory(0)
        delayHitField.valueProperty!!.bindBidirectional(model.delayHitProperty)
        delayHitField.parent.disableProperty().bind(timerState.runningProperty)
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""
    }

    fun calibrate() {
        timerFactory.calibrate()
        delayHitField.text = ""
    }
}