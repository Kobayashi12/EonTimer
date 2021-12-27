package io.eontimer.action

import io.eontimer.util.javafx.initializeChoices
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.spinner.IntValueFactory
import io.eontimer.util.javafx.spinner.bindBidirectional
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.Spinner
import org.springframework.stereotype.Component

@Component("actionSettingsController")
class ActionSettingsController(
    private val actionSettings: ActionSettings
) {
    // @formatter:off
    @FXML private lateinit var modeField: ChoiceBox<ActionMode>
    @FXML private lateinit var soundField: ChoiceBox<Sound>
    @FXML private lateinit var colorField: ColorPicker
    @FXML private lateinit var intervalField: Spinner<Int>
    @FXML private lateinit var countField: Spinner<Int>
    // @formatter:on

    fun initialize() {
        modeField.initializeChoices()
        modeField.valueProperty()
            .bindBidirectional(actionSettings.mode)

        soundField.initializeChoices()
        soundField.valueProperty()
            .bindBidirectional(actionSettings.sound)

        colorField.valueProperty()
            .bindBidirectional(actionSettings.color)

        intervalField.valueFactory = IntValueFactory(0, 1000)
            .also { it.bindBidirectional(actionSettings.interval) }
        intervalField.setOnFocusLost(intervalField::commitValue)

        countField.valueFactory = IntValueFactory(0, 50)
            .also { it.bindBidirectional(actionSettings.count) }
        countField.setOnFocusLost(countField::commitValue)
    }
}