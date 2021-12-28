package io.eontimer.custom

data class CustomTimerStoredSettings(
    val stages: List<Long> = emptyList()
) {
    constructor(
        model: CustomTimerModel
    ) : this(
        stages = model.stages.toList()
    )
}
