package io.eontimer.util.javafx

import javafx.scene.paint.Color

fun Color.toHex(
    withOpacity: Boolean = false
): String = asSequence(withOpacity)
    .map { (it * 255).toInt() and 0xff }
    .map { String.format("%02x", it) }
    .fold("#", String::plus)

private fun Color.asSequence(
    withOpacity: Boolean = false
): Sequence<Double> = sequence {
    yield(red)
    yield(green)
    yield(blue)
    if (withOpacity) {
        yield(opacity)
    }
}
