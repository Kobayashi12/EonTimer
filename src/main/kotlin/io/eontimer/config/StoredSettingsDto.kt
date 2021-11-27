package io.eontimer.config

import io.eontimer.gen3.StoredSettings

data class StoredSettingsDto(
    val gen3: StoredSettings = StoredSettings()
)
