package io.eontimer

import io.eontimer.action.ActionSettings
import io.eontimer.action.TimerActionService
import io.eontimer.util.javafx.flatMap
import io.eontimer.util.javafx.map
import io.eontimer.util.javafx.subscribe
import io.eontimer.util.javafx.toHex
import javafx.fxml.FXML
import javafx.scene.control.Label
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.fxmisc.easybind.EasyBind
import org.springframework.stereotype.Component
import kotlin.time.Duration

@ExperimentalCoroutinesApi
@Component
class TimerDisplayController(
    private val state: TimerState,
    private val timerActionService: TimerActionService,
    private val actionActionSettingsModel: ActionSettings,
) {
    // @formatter:off
    @FXML lateinit var currentStageLbl: Label
    @FXML lateinit var minutesBeforeTargetLbl: Label
    @FXML lateinit var nextStageLbl: Label
    // @formatter:on

    fun initialize() {
        currentStageLbl.textProperty()
            .bind(
                state.runningProperty
                    .flatMap { running ->
                        when (running) {
                            true -> state.currentRemainingProperty
                            false -> state.currentStageProperty
                        }
                    }
                    .map(::formatTime)
            )
        minutesBeforeTargetLbl.textProperty()
            .bind(
                EasyBind.combine(
                    state.totalTimerProperty,
                    state.totalElapsedProperty,
                    ::formatMinutesBeforeTarget
                )
            )
        nextStageLbl.textProperty()
            .bind(
                state.nextStageProperty
                    .map(::formatTime)
            )

        timerActionService.active
            .subscribe { currentStageLbl.isActive = it }
        currentStageLbl.styleProperty()
            .bind(
                actionActionSettingsModel.color
                    .map { "-theme-active: ${it.toHex()}" }
            )
    }

    private var Label.isActive: Boolean
        get() = this.styleClass.contains("active")
        set(value) {
            when (value) {
                true -> styleClass.add("active")
                false -> styleClass.remove("active")
            }
        }

    private fun formatTime(
        duration: Duration
    ) = when (duration.isFinite()) {
        true -> "%d:%02d".format(
            duration.inWholeSeconds,
            duration.inWholeMilliseconds / 10 % 100
        )
        false -> "?:??"
    }

    private fun formatMinutesBeforeTarget(
        totalTime: Duration = state.totalTimerProperty.value,
        totalElapsed: Duration = state.totalElapsedProperty.value
    ) = when (totalTime.isFinite()) {
        true -> (totalTime - totalElapsed)
            .inWholeMinutes
            .toString()
        false -> "?"
    }
}