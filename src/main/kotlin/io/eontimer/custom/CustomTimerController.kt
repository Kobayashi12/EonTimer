package io.eontimer.custom

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.eontimer.TimerController
import io.eontimer.TimerState
import io.eontimer.TimerTab
import io.eontimer.util.javafx.onKeyPressed
import io.eontimer.util.javafx.setOnFocusLost
import io.eontimer.util.javafx.spinner.LongValueFactory
import io.eontimer.util.javafx.spinner.text
import io.eontimer.util.javafx.spinner.textProperty
import javafx.application.Platform
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.Spinner
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.input.KeyCode
import javafx.util.converter.LongStringConverter
import org.springframework.stereotype.Component

@Component("customTimerController")
class CustomTimerController(
    override val model: CustomTimerModel,
    override val state: TimerState,
    override val timerFactory: CustomControllerTimerFactory
) : TimerController<CustomTimerModel, CustomControllerTimerFactory> {
    override val timerTab = TimerTab.CUSTOM

    // @formatter:off
    @FXML private lateinit var list: ListView<Long>
    @FXML private lateinit var valueField: Spinner<Long>
    @FXML private lateinit var valueAddBtn: Button
    @FXML private lateinit var valueRemoveBtn: Button
    // @formatter:on

    fun initialize() {
        model.stages.addListener(ListChangeListener {
            Platform.runLater {
                state.stages = timerFactory.stages
            }
        })

        list.isEditable = true
        list.items = model.stages
        list.cellFactory = TextFieldListCell.forListView(LongStringConverter())
        list.onKeyPressed(KeyCode.DELETE, KeyCode.BACK_SPACE) {
            removeSelectedIndices()
        }
        list.selectionModel.selectionMode = SelectionMode.MULTIPLE
        list.disableProperty()
            .bind(state.runningProperty)

        valueField.valueFactory = LongValueFactory(min = 0)
        valueField.disableProperty()
            .bind(state.runningProperty)
        valueField.onKeyPressed(KeyCode.ENTER) {
            addValue()
        }
        valueField.setOnFocusLost(valueField::commitValue)
        valueField.text = ""

        valueAddBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.PLUS)
        valueAddBtn.disableProperty()
            .bind(
                valueField.textProperty.isEmpty
                    .or(state.runningProperty)
            )
        valueAddBtn.setOnAction {
            addValue()
        }

        valueRemoveBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.MINUS)
        valueRemoveBtn.disableProperty()
            .bind(
                list.selectionModel.selectedItemProperty().isNull
                    .or(state.runningProperty)
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

    private fun <T> MutableList<T>.removeIndices(indices: List<Int>) {
        indices.forEachIndexed { offset, selectedIndex -> removeAt(selectedIndex - offset) }
    }
}