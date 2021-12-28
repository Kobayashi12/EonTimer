package io.eontimer.timer

import io.eontimer.AggregateControllerTimerFactory
import io.eontimer.TimerState
import io.eontimer.TimerStateAware
import io.eontimer.resetTimerState
import io.eontimer.util.javafx.anyChangesOf
import io.eontimer.util.javafx.initializeChoices
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import org.springframework.stereotype.Component

@Component("timerSettingsController")
class TimerSettingsController(
    override val state: TimerState,
    private val settings: TimerSettings,
    private val timerFactory: AggregateControllerTimerFactory
) : TimerStateAware {
    // @formatter:off
    @FXML private lateinit var consoleField: ChoiceBox<Console>
    @FXML private lateinit var refreshIntervalField: Spinner<Long>
    @FXML private lateinit var precisionCalibrationField: CheckBox
    // @formatter:on

    fun initialize() {
        anyChangesOf(
            settings.console,
            settings.refreshInterval,
            settings.precisionCalibration
        ) {
            resetTimerState(timerFactory)
        }

        consoleField.initializeChoices()
        consoleField.valueProperty()
            .bindBidirectional(settings.console)

        refreshIntervalField.valueFactory = LongValueFactory(min = 0L, max = 1000L)
            .also { it.bindBidirectional(settings.refreshInterval) }
        refreshIntervalField.setOnFocusLost(refreshIntervalField::commitValue)

        precisionCalibrationField.selectedProperty()
            .bindBidirectional(settings.precisionCalibration)
    }
}