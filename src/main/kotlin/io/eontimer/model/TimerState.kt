package io.eontimer.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class TimerState {
    val totalTime = SimpleObjectProperty(Duration.ZERO)
    val totalElapsed = SimpleObjectProperty(Duration.ZERO)
    val currentStage = SimpleObjectProperty(Duration.ZERO)
    val nextStage = SimpleObjectProperty(Duration.ZERO)
    val currentRemaining = SimpleObjectProperty(Duration.ZERO)
    val running = SimpleBooleanProperty(false)
}

interface TimerStateAware {
    val timerState: TimerState
}
