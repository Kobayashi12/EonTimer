package io.eontimer.custom

import javafx.collections.FXCollections
import javafx.collections.ObservableList

class CustomTimerModel(
    storedSettings: StoredSettings = StoredSettings()
) {
    val stages: ObservableList<Long> = FXCollections.observableArrayList(storedSettings.stages)
}