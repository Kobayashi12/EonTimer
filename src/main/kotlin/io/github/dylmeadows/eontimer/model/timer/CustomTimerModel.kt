package io.github.dylmeadows.eontimer.model.timer

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.time.Duration

class CustomTimerModel {
    val stages: ObservableList<Duration> = FXCollections.observableArrayList()
}