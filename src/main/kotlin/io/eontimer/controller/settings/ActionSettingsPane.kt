package io.eontimer.controller.settings

import io.eontimer.model.resource.SoundResource
import io.eontimer.model.settings.ActionMode
import io.eontimer.model.settings.ActionSettings
import io.eontimer.util.javafx.asChoiceField
import io.eontimer.util.javafx.bindBidirectional
import io.eontimer.util.javafx.spinner.IntValueFactory
import io.eontimer.util.javafx.spinner.setOnFocusLost
import io.eontimer.util.javafx.spinner.valueProperty
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.Spinner
import org.springframework.stereotype.Component

@Component
class ActionSettingsPane(
    private val model: ActionSettings
) {
    // @formatter:off
    @FXML private lateinit var modeField: ChoiceBox<ActionMode>
    @FXML private lateinit var soundField: ChoiceBox<SoundResource>
    @FXML private lateinit var colorField: ColorPicker
    @FXML private lateinit var intervalField: Spinner<Int>
    @FXML private lateinit var countField: Spinner<Int>
    // @formatter:on

    fun initialize() {
        modeField.asChoiceField().valueProperty.bindBidirectional(model.modeProperty)
        soundField.asChoiceField().valueProperty.bindBidirectional(model.soundProperty)

        colorField.valueProperty().bindBidirectional(model.colorProperty)

        intervalField.valueFactory = IntValueFactory(0, 1000)
        intervalField.valueProperty!!.bindBidirectional(model.intervalProperty)
        intervalField.setOnFocusLost(intervalField::commitValue)

        countField.valueFactory = IntValueFactory(0, 50)
        countField.valueProperty!!.bindBidirectional(model.countProperty)
        countField.setOnFocusLost(countField::commitValue)
    }
}