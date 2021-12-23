package io.eontimer

import io.eontimer.util.getStage
import io.eontimer.util.javafx.easybind.map
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import io.eontimer.util.sum
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import org.fxmisc.easybind.EasyBind
import org.fxmisc.easybind.monadic.MonadicBinding
import org.springframework.stereotype.Component
import kotlin.time.Duration

@Component
class TimerState {
    val running = SimpleBooleanProperty(false)
    final val stages = SimpleObjectProperty<List<Duration>>(emptyList())
    final val stageIndex = SimpleIntegerProperty(0)

    final val totalTime: MonadicBinding<Duration> = stages.map { it.sum() }
    final val totalElapsed = SimpleObjectProperty(Duration.ZERO)
    val totalRemaining: MonadicBinding<Duration> =
        EasyBind.combine(totalTime, totalElapsed) { totalTime, totalElapsed ->
            totalTime - totalElapsed
        }

    final val stage: MonadicBinding<Duration> =
        EasyBind.combine(stageIndex, stages) { stageIndex, stages ->
            stages.getStage(stageIndex.toInt())
        }
    final val elapsed = SimpleObjectProperty(Duration.ZERO)
    val remaining: MonadicBinding<Duration> =
        EasyBind.combine(stage, elapsed) { stage, elapsed ->
            stage - elapsed
        }

    val nextStage: MonadicBinding<Duration> =
        EasyBind.combine(stageIndex, stages) { stageIndex, stages ->
            stages.getStage(stageIndex.toInt() + 1)
        }
}

class TimerStateProxy(
    state: TimerState
) {
    var running by state.running
    var stages: List<Duration> by state.stages
    var stageIndex by state.stageIndex

    val totalTime: Duration by state.totalTime
    var totalElapsed: Duration by state.totalElapsed
    val totalRemaining: Duration by state.totalRemaining

    val stage: Duration by state.stage
    var elapsed: Duration by state.elapsed
    val remaining: Duration by state.remaining

    val nextStage: Duration by state.nextStage
}

interface TimerStateAware {
    val state: TimerState
}
