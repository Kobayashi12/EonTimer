package io.eontimer.model

import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class TimerState {
    final val totalTimeProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    final val totalElapsedProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    final val currentStageProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    final val nextStageProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    final val currentRemainingProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    final val runningProperty: BooleanProperty = SimpleBooleanProperty(false)

    var totalTime: Duration by totalTimeProperty
    var totalElapsed: Duration by totalElapsedProperty
    var currentStage: Duration by currentStageProperty
    var nextStage: Duration by nextStageProperty
    var currentRemaining: Duration by currentRemainingProperty
    var running: Boolean by runningProperty
}
