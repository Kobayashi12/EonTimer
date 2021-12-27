package io.eontimer.gen4

import io.eontimer.TimerController
import io.eontimer.TimerState
import io.eontimer.TimerTab
import io.eontimer.resetTimerState
import io.eontimer.util.javafx.anyChangesOf
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
import io.eontimer.util.javafx.spinner.text
import javafx.fxml.FXML
import javafx.scene.control.Spinner
import org.springframework.stereotype.Component

@Component("gen4TimerController")
class Gen4TimerController(
    override val model: Gen4TimerModel,
    override val state: TimerState,
    override val timerFactory: Gen4TimerControllerFactory
) : TimerController<Gen4TimerModel, Gen4TimerControllerFactory> {
    override val timerTab = TimerTab.GEN4

    // @formatter:off
    @FXML private lateinit var targetDelayField: Spinner<Long>
    @FXML private lateinit var targetSecondField: Spinner<Long>
    @FXML private lateinit var calibratedDelayField: Spinner<Long>
    @FXML private lateinit var calibratedSecondField: Spinner<Long>
    @FXML private lateinit var delayHitField: Spinner<Long>
    // @formatter:on

    fun initialize() {
        anyChangesOf(
            model.targetDelay,
            model.targetSecond,
            model.calibratedDelay,
            model.calibratedSecond
        ) {
            resetTimerState()
        }

        calibratedDelayField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.calibratedDelay) }
        calibratedDelayField.parent
            .disableProperty()
            .bind(state.runningProperty)
        calibratedDelayField.setOnFocusLost(calibratedDelayField::commitValue)

        calibratedSecondField.valueFactory = LongValueFactory()
            .also { it.bindBidirectional(model.calibratedDelay) }
        calibratedSecondField.parent
            .disableProperty()
            .bind(state.runningProperty)
        calibratedSecondField.setOnFocusLost(calibratedSecondField::commitValue)

        targetDelayField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetDelay) }
        targetDelayField.parent
            .disableProperty()
            .bind(state.runningProperty)
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.targetSecond) }
        targetSecondField.parent
            .disableProperty()
            .bind(state.runningProperty)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        delayHitField.valueFactory = LongValueFactory(min = 0)
            .also { it.bindBidirectional(model.delayHit) }
        delayHitField.parent
            .disableProperty()
            .bind(state.runningProperty)
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""
    }

    override fun calibrate() {
        timerFactory.calibrate()
        delayHitField.text = ""
    }
}