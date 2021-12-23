package io.eontimer

import io.eontimer.action.Settings
import io.eontimer.action.TimerActionService
import io.eontimer.util.isFinite
import io.eontimer.util.javafx.easybind.flatMap
import io.eontimer.util.javafx.easybind.map
import io.eontimer.util.javafx.easybind.subscribe
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
    private val actionSettingsModel: Settings,
) {
    // @formatter:off
    @FXML lateinit var currentStageLbl: Label
    @FXML lateinit var minutesBeforeTargetLbl: Label
    @FXML lateinit var nextStageLbl: Label
    // @formatter:on

    fun initialize() {
        currentStageLbl.textProperty()
            .bind(
                state.running
                    .flatMap { running ->
                        when (running) {
                            true -> state.elapsed
                            false -> state.stage
                        }
                    }
                    .map(::formatTime)
            )
        minutesBeforeTargetLbl.textProperty()
            .bind(
                EasyBind.combine(
                    state.totalTime,
                    state.totalElapsed,
                    ::formatMinutesBeforeTarget
                )
            )
        nextStageLbl.textProperty()
            .bind(
                state.nextStage
                    .map(::formatTime)
            )

        timerActionService.active
            .subscribe { currentStageLbl.isActive = it }
        currentStageLbl.styleProperty()
            .bind(
                actionSettingsModel.color
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
    ) = when (duration.isFinite) {
        true -> "%d:%02d".format(
            duration.inWholeSeconds,
            duration.inWholeMilliseconds / 10 % 100
        )
        false -> "?:??"
    }

    private fun formatMinutesBeforeTarget(
        totalTime: Duration = state.totalTime.get(),
        totalElapsed: Duration = state.totalElapsed.get()
    ) = when (totalTime.isFinite) {
        true -> (totalTime - totalElapsed)
            .inWholeMinutes
            .toString()
        false -> "?"
    }
}