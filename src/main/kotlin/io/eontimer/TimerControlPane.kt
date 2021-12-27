package io.eontimer

import io.eontimer.util.javafx.disableWhen
import io.eontimer.util.javafx.flip
import io.eontimer.util.javafx.map
import io.eontimer.util.javafx.subscribe
import javafx.application.Platform
import javafx.beans.property.ObjectProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import org.springframework.stereotype.Component

@Component
class TimerControlPane(
    private val state: TimerState,
    private val selectedTimerTab: ObjectProperty<TimerTab>,
    private val aggregateTimerFactory: AggregateControllerTimerFactory
) {

    // @formatter:off
    @FXML private lateinit var gen3Tab: Tab
    @FXML private lateinit var gen4Tab: Tab
    @FXML private lateinit var gen5Tab: Tab
    @FXML private lateinit var customTab: Tab
    @FXML private lateinit var timerTabPane: TabPane
    @FXML private lateinit var updateBtn: Button
    @FXML private lateinit var timerBtn: Button
    // @formatter:on

    fun initialize() {
        Platform.runLater {
            val selectedTimerTab = this.selectedTimerTab.get()
            timerTabPane.selectionModel.select(selectedTimerTab.ordinal)
        }
        selectedTimerTab.bind(
            timerTabPane.selectionModel
                .selectedIndexProperty()
                .map { TimerTab.values[it.toInt()] }
        )
        selectedTimerTab.subscribe {
            Platform.runLater {
                state.stagesProperty.set(aggregateTimerFactory.stages)
            }
        }

        gen3Tab.disableWhen(state.runningProperty.and(selectedTimerTab.isNotEqualTo(TimerTab.GEN3)))
        gen4Tab.disableWhen(state.runningProperty.and(selectedTimerTab.isNotEqualTo(TimerTab.GEN4)))
        gen5Tab.disableWhen(state.runningProperty.and(selectedTimerTab.isNotEqualTo(TimerTab.GEN5)))
        customTab.disableWhen(state.runningProperty.and(selectedTimerTab.isNotEqualTo(TimerTab.CUSTOM)))

        updateBtn.disableWhen(state.runningProperty)
        updateBtn.setOnAction {
            aggregateTimerFactory.calibrate()
        }

        timerBtn.textProperty()
            .bind(state.runningProperty
                .map { running ->
                    when (running) {
                        true -> "Stop"
                        false -> "Start"
                    }
                })
        timerBtn.setOnAction {
            state.runningProperty.flip()
        }
    }
}