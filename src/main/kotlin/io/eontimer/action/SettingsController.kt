package io.eontimer.action

import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.bindBidirectional
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.spinner.IntValueFactory
import io.eontimer.util.javafx.spinner.valueProperty
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.Spinner
import org.springframework.stereotype.Component

@Component("actionSettingsController")
class SettingsController(
    private val settings: Settings
) {
    // @formatter:off
    @FXML private lateinit var modeField: ChoiceBox<Mode>
    @FXML private lateinit var soundField: ChoiceBox<Sound>
    @FXML private lateinit var colorField: ColorPicker
    @FXML private lateinit var intervalField: Spinner<Int>
    @FXML private lateinit var countField: Spinner<Int>
    // @formatter:on

    fun initialize() {
        modeField.asChoiceField().valueProperty.bindBidirectional(settings.mode)
        soundField.asChoiceField().valueProperty.bindBidirectional(settings.sound)

        colorField.valueProperty().bindBidirectional(settings.color)

        intervalField.valueFactory = IntValueFactory(0, 1000)
        intervalField.valueProperty!!.bindBidirectional(settings.interval)
        intervalField.setOnFocusLost(intervalField::commitValue)

        countField.valueFactory = IntValueFactory(0, 50)
        countField.valueProperty!!.bindBidirectional(settings.count)
        countField.setOnFocusLost(countField::commitValue)
    }
}