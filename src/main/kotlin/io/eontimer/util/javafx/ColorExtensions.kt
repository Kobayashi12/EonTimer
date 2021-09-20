package io.eontimer.util.javafx

import javafx.scene.paint.Color

fun Color.toHex(
    withOpacity: Boolean = false
): String = asSequence(withOpacity)
    .map { (it * 255).toInt() and 0xff }
    .map { String.format("%02x", it) }
    .fold("#", String::plus)

fun Color.withRed(
    red: Double
): Color = Color(red, green, blue, opacity)

fun Color.withGreen(
    green: Double
): Color = Color(red, green, blue, opacity)

fun Color.withBlue(
    blue: Double
): Color = Color(red, green, blue, opacity)

fun Color.withOpacity(
    opacity: Double
): Color = Color(red, green, blue, opacity)

fun Color.withHue(
    hue: Double
): Color = Color.hsb(hue, saturation, brightness, opacity)

fun Color.withSaturation(
    saturation: Double
): Color = Color.hsb(hue, saturation, brightness, opacity)

fun Color.withBrightness(
    brightness: Double
): Color = Color.hsb(hue, saturation, brightness, opacity)

fun Color.deriveHue(
    hueShift: Double
): Color = withHue(derive(hue, hueShift))

fun Color.deriveSaturation(
    percent: Double
): Color = withSaturation(derive(saturation, percent))

fun Color.deriveBrightness(
    percent: Double
): Color = withBrightness(derive(brightness, percent))

private fun derive(value: Double, percent: Double): Double {
    val sum = value + percent
    return when {
        percent < 0 -> sum.coerceAtLeast(0.0)
        percent > 0 -> sum.coerceAtMost(1.0)
        else -> value
    }
}

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
