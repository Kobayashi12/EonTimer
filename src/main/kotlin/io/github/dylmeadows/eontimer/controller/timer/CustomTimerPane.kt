package io.github.dylmeadows.eontimer.controller.timer

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.github.dylmeadows.commonkt.javafx.node.setOnFocusLost
import io.github.dylmeadows.commonkt.javafx.node.spinner.LongValueFactory
import io.github.dylmeadows.commonkt.javafx.node.spinner.text
import io.github.dylmeadows.commonkt.javafx.node.spinner.textProperty
import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.Spinner
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.input.KeyCode
import javafx.util.StringConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CustomTimerPane @Autowired constructor(
    private val model: CustomTimerModel,
    private val timerState: TimerState
) {

    @FXML
    private lateinit var list: ListView<Duration>

    @FXML
    private lateinit var valueField: Spinner<Long>

    @FXML
    private lateinit var valueAddBtn: Button

    @FXML
    private lateinit var valueRemoveBtn: Button

    fun initialize() {
        list.items = model.stages
        list.selectionModel.selectionMode = SelectionMode.MULTIPLE
        list.cellFactory = TextFieldListCell.forListView(DurationStringConverter)
        list.disableProperty().bind(timerState.runningProperty)

        valueField.valueFactory = LongValueFactory(0L)
        valueField.disableProperty().bind(timerState.runningProperty)
        valueField.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) {
                model.stages.add(Duration.ofMillis(valueField.value))
                valueField.text = ""
            }
        }
        valueField.setOnFocusLost(valueField::commitValue)
        valueField.text = ""

        valueAddBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.PLUS)
        valueAddBtn.disableProperty().bind(
            valueField.textProperty.isEmpty
                .or(timerState.runningProperty)
        )
        valueAddBtn.setOnAction {
            model.stages.add(Duration.ofMillis(valueField.value))
            valueField.text = ""
        }

        valueRemoveBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.MINUS)
        valueRemoveBtn.disableProperty().bind(
            list.selectionModel.selectedItemProperty().isNull
                .or(timerState.runningProperty)
        )
        valueRemoveBtn.setOnAction {
            list.selectionModel.selectedIndices
                .map { model.stages[it] }
                .forEach { model.stages.remove(it) }
        }
    }

    object DurationStringConverter : StringConverter<Duration>() {
        override fun toString(duration: Duration): String =
            duration.toMillis().toString()

        override fun fromString(string: String): Duration =
            Duration.ofMillis(string.toLong())
    }
}