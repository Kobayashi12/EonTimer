package io.eontimer

import io.eontimer.config.AppProperties
import io.eontimer.util.FxmlResource
import javafx.scene.Parent
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import org.springframework.stereotype.Component

@Component
class SettingsDialog(
    private val properties: AppProperties,
    loader: SpringFxmlLoader
) {
    private val settingsControlPane = loader.load<Parent>(FxmlResource.SettingsControlPane)

    fun showAndWait() {
        val dialog = Dialog<Unit>()
        dialog.title = properties.fullApplicationName
        dialog.dialogPane.content = settingsControlPane
        dialog.dialogPane.buttonTypes.setAll(ButtonType.OK)
        dialog.showAndWait()
    }
}