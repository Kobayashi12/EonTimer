package io.eontimer.model.timer

import io.eontimer.model.Stage
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class CustomTimer {
    val stages: ObservableList<Stage> = FXCollections.observableArrayList()
}