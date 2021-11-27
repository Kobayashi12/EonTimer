package io.eontimer.timer

import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.bindBidirectional
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.setOnFocusLost
import io.eontimer.util.javafx.spinner.valueProperty
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import org.springframework.stereotype.Component

@Component("timerSettingsController")
class SettingsController(
    private val settings: Settings
) {
    // @formatter:off
    @FXML private lateinit var consoleField: ChoiceBox<Console>
    @FXML private lateinit var refreshIntervalField: Spinner<Long>
    @FXML private lateinit var precisionCalibrationField: CheckBox
    // @formatter:on

    fun initialize() {
        consoleField.asChoiceField().valueProperty
            .bindBidirectional(settings.console)

        refreshIntervalField.valueFactory = LongValueFactory(0L, 1000L)
        refreshIntervalField.valueProperty!!.bindBidirectional(settings.refreshInterval)
        refreshIntervalField.setOnFocusLost(refreshIntervalField::commitValue)

        precisionCalibrationField.selectedProperty()
            .bindBidirectional(settings.precisionCalibrationMode)
    }
}