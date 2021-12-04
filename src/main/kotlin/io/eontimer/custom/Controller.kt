package io.eontimer.custom

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.eontimer.model.TimerState
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.setOnFocusLost
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.javafx.spinner.textProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.Spinner
import javafx.scene.input.KeyCode
import org.springframework.stereotype.Component

@Component("customController")
class Controller(
    private val model: Model,
    private val timerState: TimerState
) {
    // @formatter:off
    @FXML private lateinit var list: ListView<Long>
    @FXML private lateinit var valueField: Spinner<Long>
    @FXML private lateinit var valueAddBtn: Button
    @FXML private lateinit var valueRemoveBtn: Button
    // @formatter:on

    fun initialize() {
        list.items = model.stages
        list.selectionModel.selectionMode = SelectionMode.MULTIPLE
        list.disableProperty().bind(timerState.running)

        valueField.valueFactory = LongValueFactory(0L)
        valueField.disableProperty().bind(timerState.running)
        valueField.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) {
                model.stages.add(valueField.value)
                valueField.text = ""
            }
        }
        valueField.setOnFocusLost(valueField::commitValue)
        valueField.text = ""

        valueAddBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.PLUS)
        valueAddBtn.disableProperty().bind(
            valueField.textProperty.isEmpty
                .or(timerState.running)
        )
        valueAddBtn.setOnAction {
            model.stages.add(valueField.value)
            valueField.text = ""
        }

        valueRemoveBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.MINUS)
        valueRemoveBtn.disableProperty().bind(
            list.selectionModel.selectedItemProperty().isNull
                .or(timerState.running)
        )
        valueRemoveBtn.setOnAction {
            list.selectionModel.selectedIndices
                .map { model.stages[it] }
                .forEach { model.stages.remove(it) }
        }
    }
}