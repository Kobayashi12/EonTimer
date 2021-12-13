package io.eontimer

import io.eontimer.action.Settings
import io.eontimer.model.TimerState
import io.eontimer.service.action.TimerActionService
import io.eontimer.util.INDEFINITE
import io.eontimer.util.javafx.isActive
import io.eontimer.util.javafx.onChange
import io.eontimer.util.javafx.toHex
import javafx.fxml.FXML
import javafx.scene.control.Label
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component
import java.time.Duration

@ExperimentalCoroutinesApi
@Component
class TimerDisplayController(
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
        with(timerState) {
            currentStage.onChange { currentStageLbl.text = formatTime(it) }
            currentRemaining.onChange { currentStageLbl.text = formatTime(it) }
            totalTime.onChange { updateMinutesBeforeTarget(totalTime = it) }
            totalElapsed.onChange { updateMinutesBeforeTarget(totalElapsed = it) }
            nextStage.onChange { nextStageLbl.text = formatTime(it) }

            timerActionService.active.onChange { currentStageLbl.isActive = it }
            actionSettingsModel.color.onChange { currentStageLbl.style = "-theme-active: ${it.toHex()}" }
        }
    }

    private fun updateMinutesBeforeTarget(
        totalTime: Duration = timerState.totalTime.get(),
        totalElapsed: Duration = timerState.totalElapsed.get()
    ) {
        minutesBeforeTargetLbl.text = when (totalTime) {
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