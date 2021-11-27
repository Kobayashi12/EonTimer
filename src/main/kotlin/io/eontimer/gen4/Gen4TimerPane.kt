package io.eontimer.gen4

import io.eontimer.model.TimerState
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
    @FXML private lateinit var targetDelayField: Spinner<Long>
    @FXML private lateinit var targetSecondField: Spinner<Long>
    @FXML private lateinit var calibratedDelayField: Spinner<Long>
    @FXML private lateinit var calibratedSecondField: Spinner<Long>
    @FXML private lateinit var delayHitField: Spinner<Long>
    // @formatter:on

    fun initialize() {
        calibratedDelayField.valueFactory = LongValueFactory()
        calibratedDelayField.valueProperty!!.bindBidirectional(model.calibratedDelay)
        calibratedDelayField.parent.disableProperty().bind(timerState.runningProperty)
        calibratedDelayField.setOnFocusLost(calibratedDelayField::commitValue)

        calibratedSecondField.valueFactory = LongValueFactory()
        calibratedSecondField.valueProperty!!.bindBidirectional(model.calibratedSecond)
        calibratedSecondField.parent.disableProperty().bind(timerState.runningProperty)
        calibratedSecondField.setOnFocusLost(calibratedSecondField::commitValue)

        targetDelayField.valueFactory = LongValueFactory(0)
        targetDelayField.valueProperty!!.bindBidirectional(model.targetDelay)
        targetDelayField.parent.disableProperty().bind(timerState.runningProperty)
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(0)
        targetSecondField.valueProperty!!.bindBidirectional(model.targetSecond)
        targetSecondField.parent.disableProperty().bind(timerState.runningProperty)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        delayHitField.valueFactory = LongValueFactory(0)
        delayHitField.valueProperty!!.bindBidirectional(model.delayHit)
        delayHitField.parent.disableProperty().bind(timerState.runningProperty)
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""
    }

    fun calibrate() {
        timerFactory.calibrate()
        delayHitField.text = ""
    }
}