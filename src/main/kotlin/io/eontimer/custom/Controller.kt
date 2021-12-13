package io.eontimer.custom

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.eontimer.model.TimerState
import io.eontimer.util.javafx.disableWhen
import io.eontimer.util.javafx.onKeyPressed
import io.eontimer.util.javafx.or
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.javafx.spinner.textProperty
import io.eontimer.util.removeIndices
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.Spinner
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.input.KeyCode
import javafx.util.StringConverter
import javafx.util.converter.LongStringConverter
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
        list.isEditable = true
        list.items = model.stages
        list.cellFactory = TextFieldListCell.forListView(LongStringConverter())
        list.onKeyPressed(KeyCode.DELETE, KeyCode.BACK_SPACE) {
            removeSelectedIndices()
        }
        list.selectionModel.selectionMode = SelectionMode.MULTIPLE
        list.disableWhen(timerState.running)

        valueField.valueFactory = LongValueFactory(min = 0)
        valueField.disableWhen(timerState.running)
        valueField.onKeyPressed(KeyCode.ENTER) {
            addValue()
        }
        valueField.setOnFocusLost(valueField::commitValue)
        valueField.text = ""

        valueAddBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.PLUS)
        valueAddBtn.disableWhen(
            valueField.textProperty.isEmpty
                or timerState.running
        )
        valueAddBtn.setOnAction {
            addValue()
        }

        valueRemoveBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.MINUS)
        valueRemoveBtn.disableWhen(
            list.selectionModel.selectedItemProperty().isNull
                or timerState.running
        )
        valueRemoveBtn.setOnAction {
            removeSelectedIndices()
        }
    }

    private fun addValue() {
        model.stages.add(valueField.value)
        valueField.text = ""
    }

    private fun removeSelectedIndices() {
        val selectedIndices = list.selectionModel.selectedIndices
        if (selectedIndices.isNotEmpty()) {
            model.stages.removeIndices(selectedIndices.toList())
            list.selectionModel.clearSelection()
        }
    }
}