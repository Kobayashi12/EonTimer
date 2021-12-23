package io.eontimer

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.eontimer.util.javafx.disableWhen
import javafx.fxml.FXML
import javafx.scene.control.Button
import org.springframework.stereotype.Component

@Component
class EonTimerController(
    private val timerState: TimerState,
    private val settingsDialog: SettingsDialog
) {
    // @formatter:off
    @FXML private lateinit var settingsBtn: Button
    // @formatter:on

    fun initialize() {
        settingsBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.GEAR)
        settingsBtn.disableWhen(timerState.running)
        settingsBtn.setOnAction {
            settingsDialog.showAndWait()
        }
    }
}