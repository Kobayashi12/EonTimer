package io.github.dylmeadows.eontimer.model

import io.github.dylmeadows.commonkt.javafx.beans.property.getValue
import io.github.dylmeadows.commonkt.javafx.beans.property.setValue
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class TimerState {
    final val totalTimeProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var totalTime: Duration by totalTimeProperty

    final val totalElapsedProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var totalElapsed: Duration by totalElapsedProperty

    final val currentStageProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var currentStage: Duration by currentStageProperty

    final val nextStageProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var nextStage: Duration by nextStageProperty

    final val currentRemainingProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var currentRemaining: Duration by currentRemainingProperty

    final val runningProperty: BooleanProperty = SimpleBooleanProperty(false)
    var running: Boolean by runningProperty
}
