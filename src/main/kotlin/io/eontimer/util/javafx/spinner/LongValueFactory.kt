package io.eontimer.util.javafx.spinner

import io.eontimer.util.ifPresent
import io.eontimer.util.javafx.subscribe
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import io.eontimer.util.map
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.scene.control.SpinnerValueFactory
import javafx.util.converter.LongStringConverter

class LongValueFactory(
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    initialValue: Long = 0L,
    step: Int = 1
) : SpinnerValueFactory<Long>() {
    val stepProperty: IntegerProperty = SimpleIntegerProperty(step)
    val minProperty: LongProperty = object : SimpleLongProperty(min) {
        override fun invalidated() {
            val newMin = get()
            if (newMin > this@LongValueFactory.max) {
                this@LongValueFactory.min = this@LongValueFactory.max
                return
            }
            if (this@LongValueFactory.value < newMin) {
                this@LongValueFactory.value = newMin
            }
        }
    }
    val maxProperty: LongProperty = object : SimpleLongProperty(max) {
        override fun invalidated() {
            val newMax = get()
            if (newMax < this@LongValueFactory.min) {
                this@LongValueFactory.max = this@LongValueFactory.min
                return
            }

            if (this@LongValueFactory.value > newMax) {
                this@LongValueFactory.value = newMax
            }
        }
    }

    var step by stepProperty
    var min by minProperty
    var max by maxProperty

    init {
        converter = LongStringConverter()
        valueProperty().subscribe { newValue ->
            newValue?.ifPresent {
                this@LongValueFactory.value = it.coerceIn(
                    this@LongValueFactory.min,
                    this@LongValueFactory.max
                )
            }
        }
        value = initialValue.coerceIn(min, max)
    }

    override fun increment(steps: Int) {
        value?.map { it + (steps * step) }
            .ifPresent { newValue ->
                value = when {
                    newValue <= max -> newValue
                    isWrapAround -> wrapValue(newValue, min, max) - 1
                    else -> max
                }
            }
    }

    override fun decrement(steps: Int) {
        value?.map { it - (steps * step) }
            .ifPresent { newValue ->
                value = when {
                    newValue >= min -> newValue
                    isWrapAround -> wrapValue(newValue, min, max) + 1
                    else -> min
                }
            }
    }

    private fun wrapValue(value: Long, min: Long, max: Long): Long {
        if (max == 0L) {
            throw RuntimeException()
        }

        val r = value % max
        return when (min) {
            in (max + 1).until(r) -> {
                r + max - min
            }
            in (r + 1).until(max) -> {
                r + max - min
            }
            else -> r
        }
    }
}