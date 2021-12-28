package io.eontimer.custom

import javafx.collections.FXCollections
import javafx.collections.ObservableList

class CustomTimerModel(
    storedSettings: CustomTimerStoredSettings = CustomTimerStoredSettings()
) {
    val stages: ObservableList<Long> = FXCollections.observableArrayList(storedSettings.stages)
}