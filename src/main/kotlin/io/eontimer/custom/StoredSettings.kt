package io.eontimer.custom

data class StoredSettings(
    val stages: List<Long> = emptyList()
) {
    constructor(
        model: Model
    ) : this(
        stages = model.stages.toList()
    )
}
