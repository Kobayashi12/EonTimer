package io.eontimer.n8tive

import io.github.landerlyoung.jenny.NativeClass
import kotlinx.coroutines.CoroutineScope
import org.springframework.stereotype.Service

@Service
class TimerService(
    private val coroutineScope: CoroutineScope
) {
    fun start(stages: List<>)

    @NativeClass(namespace = "TimerService")
    object Natives {

    }
}