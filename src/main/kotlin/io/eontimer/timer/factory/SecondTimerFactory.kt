package io.eontimer.timer.factory

import io.eontimer.util.toMinimumLength
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.milliseconds

@Service
class SecondTimerFactory : TimerFactory<SecondTimerFactory.Params> {
    override fun createStages(params: Params) =
        listOf(
            stage1(
                params.targetSecond,
                params.calibration
            )
        )

    private fun stage1(targetSecond: Long, calibration: Long) =
        (targetSecond * 1000 + calibration + 200)
            .toMinimumLength()
            .milliseconds

    fun calibrate(targetSecond: Long, secondHit: Long): Long {
        return when {
            secondHit < targetSecond -> (targetSecond - secondHit) * 1000 - 500
            secondHit > targetSecond -> (targetSecond - secondHit) * 1000 + 500
            else -> 0
        }
    }

    data class Params(
        val targetSecond: Long,
        val calibration: Long
    )
}