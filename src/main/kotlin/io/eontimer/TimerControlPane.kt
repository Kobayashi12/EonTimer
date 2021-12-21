package io.eontimer

import io.eontimer.model.ApplicationModel
import io.eontimer.model.TimerState
import io.eontimer.model.timer.TimerTab
import io.eontimer.service.TimerRunnerService
import io.eontimer.service.factory.TimerFactoryService
import io.eontimer.util.javafx.and
import io.eontimer.util.javafx.disableWhen
import io.eontimer.util.javafx.easybind.map
import io.eontimer.util.javafx.easybind.subscribe
import io.eontimer.util.javafx.flip
import io.eontimer.util.javafx.isNotEqualTo
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.fxmisc.easybind.EasyBind
import org.springframework.stereotype.Component

@Component
@ExperimentalCoroutinesApi
class TimerControlPane(
    private val model: ApplicationModel,
    private val timerState: TimerState,
    private val timerRunner: TimerRunnerService,
    private val timerFactory: TimerFactoryService,
    private val coroutineScope: CoroutineScope,
    timerControllers: List<TimerController<*>>,
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

    private val timerControllerMap = timerControllers.associateBy { it.timerTab }
    private lateinit var currentTimerController: TimerController<*>
    private lateinit var timerTabMap: Map<TimerTab, Tab>

    fun initialize() {
        timerTabMap = mapOf(
            TimerTab.GEN3 to gen3Tab,
            TimerTab.GEN4 to gen4Tab,
            TimerTab.GEN5 to gen5Tab,
            TimerTab.CUSTOM to customTab
        )

        val timerTab = timerTabPane.selectionModel
            .selectedItemProperty()
            .map(this::getTimerTab)
        timerTab.map(timerControllerMap::get)
            .subscribe { newValue ->

            }
        model.selectedTimerTab.bind(timerTab)

        gen3Tab.disableWhen(timerTab isNotEqualTo TimerTab.GEN3 and timerState.running)
        gen4Tab.disableWhen(timerTab isNotEqualTo TimerTab.GEN4 and timerState.running)
        gen5Tab.disableWhen(timerTab isNotEqualTo TimerTab.GEN5 and timerState.running)
        customTab.disableWhen(timerTab isNotEqualTo TimerTab.CUSTOM and timerState.running)

        timerBtn.setOnAction {
            timerState.running.flip()
        }
        timerBtn.textProperty()
            .bind(
                timerState.running
                    .map {
                        when (it) {
                            true -> "Start"
                            false -> "Stop"
                        }
                    }
            )
        timerState.running
            .subscribe { newValue ->
                Platform.runLater {
                    when (newValue) {
                        true -> timerRunner.start(timerFactory.stages)
                        false -> timerRunner.stop()
                    }
                }
            }

        // updateBtn
        updateBtn.disableWhen(timerState.running)
        updateBtn.setOnAction {
            currentTimerController.calibrate()
        }

        val tab = timerTabMap[model.selectedTimerTab.get()]
        Platform.runLater {
            timerTabPane.selectionModel.select(tab)
        }
    }

    private fun getTimerTab(tab: Tab): TimerTab =
        when (tab) {
            gen3Tab -> TimerTab.GEN3
            gen4Tab -> TimerTab.GEN4
            gen5Tab -> TimerTab.GEN5
            customTab -> TimerTab.CUSTOM
            else -> throw IllegalStateException("unable to find TimerType for selected tab")
        }
}