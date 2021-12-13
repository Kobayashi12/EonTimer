package io.eontimer

import io.eontimer.model.ApplicationModel
import io.eontimer.model.TimerState
import io.eontimer.model.timer.TimerType
import io.eontimer.service.TimerRunnerService
import io.eontimer.service.factory.TimerFactoryService
import io.eontimer.util.ifElse
import io.eontimer.util.javafx.and
import io.eontimer.util.javafx.disableWhen
import io.eontimer.util.javafx.mapped
import io.eontimer.util.javafx.onChange
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import io.eontimer.gen3.Controller as Gen3Controller
import io.eontimer.gen4.Controller as Gen4Controller
import io.eontimer.gen5.Controller as Gen5Controller

@Component
@ExperimentalCoroutinesApi
class TimerControlPane(
    private val model: ApplicationModel,
    private val timerState: TimerState,
    private val timerRunner: TimerRunnerService,
    private val timerFactory: TimerFactoryService,
    private val gen3Controller: Gen3Controller,
    private val gen4Controller: Gen4Controller,
    private val gen5Controller: Gen5Controller,
    private val coroutineScope: CoroutineScope
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

    private var timerType: TimerType
        get() = model.selectedTimerType
        set(value) {
            model.selectedTimerType = value
        }

    fun initialize() {
        coroutineScope.launch {
            timerTabPane.selectionModel.select(timerType.tab)
        }

        val selectedItem = timerTabPane.selectionModel.selectedItemProperty()
        gen3Tab.disableWhen(selectedItem.isNotEqualTo(gen3Tab) and timerState.running)
        gen4Tab.disableWhen(selectedItem.isNotEqualTo(gen4Tab) and timerState.running)
        gen5Tab.disableWhen(selectedItem.isNotEqualTo(gen5Tab) and timerState.running)
        customTab.disableWhen(selectedItem.isNotEqualTo(customTab) and timerState.running)

        timerTabPane.selectionModel
            .selectedItemProperty()
            .mapped { it.timerType }
            .onChange {
                timerType = it
            }
        timerState.running
            .mapped {
                it.ifElse(
                    whenTrue = { "Start" },
                    whenFalse = { "Stop" }
                )
            }
            .onChange(fn = timerBtn::setText)

        timerBtn.setOnAction {
            if (!timerState.running.get()) {
                timerRunner.start(timerFactory.stages)
            } else {
                timerRunner.stop()
            }
        }

        updateBtn.disableWhen(timerState.running)
        updateBtn.setOnAction {
            calibrate()
        }
    }

    private fun calibrate() {
        when (timerType) {
            TimerType.GEN3 -> gen3Controller.calibrate()
            TimerType.GEN4 -> gen4Controller.calibrate()
            TimerType.GEN5 -> gen5Controller.calibrate()
            else -> Unit
        }
    }

    private val Tab.timerType: TimerType
        get() {
            return when (this) {
                gen3Tab -> TimerType.GEN3
                gen4Tab -> TimerType.GEN4
                gen5Tab -> TimerType.GEN5
                customTab -> TimerType.CUSTOM
                else -> throw IllegalStateException("unable to find TimerType for selected tab")
            }
        }

    private val TimerType.tab: Tab
        get() {
            return when (this) {
                TimerType.GEN3 -> gen3Tab
                TimerType.GEN4 -> gen4Tab
                TimerType.GEN5 -> gen5Tab
                TimerType.CUSTOM -> customTab
            }
        }
}