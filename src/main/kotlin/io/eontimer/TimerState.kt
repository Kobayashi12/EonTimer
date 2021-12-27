package io.eontimer

import io.eontimer.util.getStage
import io.eontimer.util.javafx.map
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import io.eontimer.util.sum
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import org.fxmisc.easybind.EasyBind
import org.fxmisc.easybind.monadic.MonadicBinding
import org.springframework.stereotype.Component
import kotlin.time.Duration

@Component
class TimerState {
    final val runningProperty = SimpleBooleanProperty(false)
    var running by runningProperty

    final val stagesProperty = SimpleObjectProperty<List<Duration>>(emptyList())
    var stages: List<Duration> by stagesProperty

    final val stageIndexProperty = SimpleIntegerProperty(0)
    var stageIndex by stageIndexProperty

    final val totalTimerProperty: ObservableValue<Duration> = stagesProperty.map { it.sum() }
    val totalTime: Duration by totalTimerProperty

    final val totalElapsedProperty = SimpleObjectProperty(Duration.ZERO)
    var totalElapsed: Duration by totalElapsedProperty

    final val totalRemainingProperty: ObservableValue<Duration> =
        EasyBind.combine(totalTimerProperty, totalElapsedProperty) { totalTime, totalElapsed ->
            totalTime - totalElapsed
        }
    val totalRemaining: Duration by totalRemainingProperty

    final val currentStageProperty: MonadicBinding<Duration> =
        EasyBind.combine(stageIndexProperty, stagesProperty) { stageIndex, stages ->
            stages.getStage(stageIndex.toInt())
        }
    val currentStage: Duration by currentStageProperty

    final val currentElapsedProperty = SimpleObjectProperty(Duration.ZERO)
    var currentElapsed: Duration by currentElapsedProperty

    final val currentRemainingProperty: MonadicBinding<Duration> =
        EasyBind.combine(currentStageProperty, currentElapsedProperty) { stage, elapsed ->
            stage - elapsed
        }
    val currentRemaining: Duration by currentRemainingProperty

    final val nextStageProperty: MonadicBinding<Duration> =
        EasyBind.combine(stageIndexProperty, stagesProperty) { stageIndex, stages ->
            stages.getStage(stageIndex.toInt() + 1)
        }
    val nextStage: Duration by nextStageProperty
}

interface TimerStateAware {
    val state: TimerState
}
