package io.eontimer.custom

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.eontimer.TimerController
import io.eontimer.TimerState
import io.eontimer.TimerTab
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
import javafx.util.converter.LongStringConverter
import org.springframework.stereotype.Component

@Component("customController")
class Controller(
    override val model: Model,
    override val state: TimerState,
    override val timerFactory: ControllerTimerFactory
) : TimerController<Model, ControllerTimerFactory> {
    override val timerTab = TimerTab.CUSTOM

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
        list.disableWhen(state.running)

        valueField.valueFactory = LongValueFactory(min = 0)
        valueField.disableWhen(state.running)
        valueField.onKeyPressed(KeyCode.ENTER) {
            addValue()
        }
        valueField.setOnFocusLost(valueField::commitValue)
        valueField.text = ""

        valueAddBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.PLUS)
        valueAddBtn.disableWhen(
            valueField.textProperty.isEmpty
                or state.running
        )
        valueAddBtn.setOnAction {
            addValue()
        }

        valueRemoveBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.MINUS)
        valueRemoveBtn.disableWhen(
            list.selectionModel.selectedItemProperty().isNull
                or state.running
        )
        valueRemoveBtn.setOnAction {
            removeSelectedIndices()
        }
    }

    override fun calibrate() = Unit

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