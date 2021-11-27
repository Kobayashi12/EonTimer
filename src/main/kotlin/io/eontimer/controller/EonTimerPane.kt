package io.eontimer.controller

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.eontimer.controller.settings.SettingsDialog
import io.eontimer.model.TimerState
import javafx.fxml.FXML
import javafx.scene.control.Button
import org.springframework.stereotype.Component

@Component
class EonTimerPane(
    private val settingsDialog: SettingsDialog,
    private val timerState: TimerState
) {
    // @formatter:off
    @FXML private lateinit var settingsBtn: Button
    // @formatter:on

    fun initialize() {
        settingsBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.GEAR)
        settingsBtn.disableProperty().bind(timerState.runningProperty)
        settingsBtn.setOnAction {
            settingsDialog.showAndWait()
        }
    }
}