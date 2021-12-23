package io.eontimer.gen4

import io.eontimer.TimerController
import io.eontimer.TimerState
import io.eontimer.TimerTab
import io.eontimer.util.javafx.disableWhen
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
import io.eontimer.util.javafx.spinner.text
import javafx.fxml.FXML
import javafx.scene.control.Spinner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component

@Component("gen4Controller")
@ExperimentalCoroutinesApi
class Controller(
    override val model: Model,
    override val state: TimerState,
    override val timerFactory: ControllerTimerFactory
) : TimerController<Model, ControllerTimerFactory> {
    override val timerTab = TimerTab.GEN4

    // @formatter:off
    @FXML private lateinit var targetDelayField: Spinner<Long>
    @FXML private lateinit var targetSecondField: Spinner<Long>
    @FXML private lateinit var calibratedDelayField: Spinner<Long>
    @FXML private lateinit var calibratedSecondField: Spinner<Long>
    @FXML private lateinit var delayHitField: Spinner<Long>
    // @formatter:on

    fun initialize() {
        calibratedDelayField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.calibratedDelay) }
        calibratedDelayField.parent.disableWhen(state.running)
        calibratedDelayField.setOnFocusLost(calibratedDelayField::commitValue)

        calibratedSecondField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.calibratedDelay) }
        calibratedSecondField.parent.disableWhen(state.running)
        calibratedSecondField.setOnFocusLost(calibratedSecondField::commitValue)

        targetDelayField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetDelay) }
        targetDelayField.parent.disableWhen(state.running)
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetSecond) }
        targetSecondField.parent.disableWhen(state.running)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        delayHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.delayHit) }
        delayHitField.parent.disableWhen(state.running)
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""
    }

    override fun calibrate() {
        timerFactory.calibrate()
        delayHitField.text = ""
    }
}