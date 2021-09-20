package io.eontimer.controller.settings

import io.eontimer.model.settings.Console
import io.eontimer.model.settings.TimerSettings
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.bindBidirectional
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.setOnFocusLost
import io.eontimer.util.javafx.spinner.valueProperty
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TimerSettingsPane @Autowired constructor(
    private val model: TimerSettings
) {

    @FXML
    private lateinit var consoleField: ChoiceBox<Console>

    @FXML
    private lateinit var refreshIntervalField: Spinner<Long>

    @FXML
    private lateinit var precisionCalibrationField: CheckBox

    fun initialize() {
        consoleField.asChoiceField().valueProperty
            .bindBidirectional(model.consoleProperty)

        refreshIntervalField.valueFactory = LongValueFactory(0L, 1000L)
        refreshIntervalField.valueProperty!!.bindBidirectional(model.refreshIntervalProperty)
        refreshIntervalField.setOnFocusLost(refreshIntervalField::commitValue)

        precisionCalibrationField.selectedProperty()
            .bindBidirectional(model.precisionCalibrationModeProperty)
    }
}