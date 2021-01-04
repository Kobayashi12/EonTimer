package io.github.dylmeadows.eontimer.model.timer

import io.github.dylmeadows.eontimer.model.Reference
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class CustomTimerModel {
    val stages: ObservableList<Reference<Long>> = FXCollections.observableArrayList()
}