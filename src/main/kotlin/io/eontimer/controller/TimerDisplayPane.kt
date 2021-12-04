package io.eontimer.controller

import io.eontimer.model.TimerState
import io.eontimer.action.Settings
import io.eontimer.service.action.TimerActionService
import io.eontimer.util.INDEFINITE
import io.eontimer.util.javafx.isActive
import io.eontimer.util.javafx.toHex
import javafx.fxml.FXML
import javafx.scene.control.Label
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration

@ExperimentalCoroutinesApi
@Component
class TimerDisplayPane(
    private val timerState: TimerState,
    private val timerActionService: TimerActionService,
    private val actionSettingsModel: Settings,
    private val coroutineScope: CoroutineScope
) {
    // @formatter:off
    @FXML lateinit var currentStageLbl: Label
    @FXML lateinit var minutesBeforeTargetLbl: Label
    @FXML lateinit var nextStageLbl: Label
    // @formatter:on

    fun initialize() {
        coroutineScope.launch(Dispatchers.JavaFx) {
            timerState.currentStage.asFlow()
                .collect { currentStageLbl.text = formatTime(it) }
            timerState.currentRemaining.asFlow()
                .collect { currentStageLbl.text = formatTime(it) }

            timerState.totalTime.asFlow()
                .zip(timerState.totalElapsed.asFlow()) { totalTime, totalElapsed ->
                    formatMinutesBeforeTarget(totalTime, totalElapsed)
                }.collect {
                    minutesBeforeTargetLbl.text = it
                }

            timerState.nextStage.asFlow()
                .collect { nextStageLbl.text = formatTime(it) }
            timerActionService.active.asFlow()
                .collect { currentStageLbl.isActive = it }

            actionSettingsModel.color.asFlow()
                .collect {
                    currentStageLbl.style = "-theme-active: ${it.toHex()}"
                }
        }
    }

    private fun formatMinutesBeforeTarget(totalTime: Duration, totalElapsed: Duration): String {
        return when (totalTime) {
            INDEFINITE -> "?"
            else -> {
                val remaining = totalTime - totalElapsed
                remaining.toMinutes().toString()
            }
        }
    }

    private fun formatTime(duration: Duration): String {
        return when (duration) {
            INDEFINITE -> "?:??"
            else -> String.format(
                "%d:%02d",
                duration.toSeconds(),
                duration.toMillis()
                    / 10 % 100
            )
        }
    }
}