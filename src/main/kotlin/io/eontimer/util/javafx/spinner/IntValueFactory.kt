package io.eontimer.util.javafx.spinner

import io.eontimer.util.ifPresent
import io.eontimer.util.javafx.subscribe
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import io.eontimer.util.map
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.SpinnerValueFactory
import javafx.util.converter.IntegerStringConverter

class IntValueFactory(
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    initialValue: Int = 0,
    step: Int = 1
) : SpinnerValueFactory<Int>() {

    val stepProperty: IntegerProperty = SimpleIntegerProperty(step)
    val minProperty: IntegerProperty = object : SimpleIntegerProperty(min) {
        override fun invalidated() {
            val newMin = get()
            if (newMin > this@IntValueFactory.max) {
                this@IntValueFactory.min = this@IntValueFactory.max
                return
            }
            if (this@IntValueFactory.value < newMin) {
                this@IntValueFactory.value = newMin
            }
        }
    }
    val maxProperty: IntegerProperty = object : SimpleIntegerProperty(max) {
        override fun invalidated() {
            val newMax = get()
            if (newMax < this@IntValueFactory.min) {
                this@IntValueFactory.max = this@IntValueFactory.min
                return
            }

            if (this@IntValueFactory.value > newMax) {
                this@IntValueFactory.value = newMax
            }
        }
    }

    var step by stepProperty
    var min by minProperty
    var max by maxProperty

    init {
        converter = IntegerStringConverter()
        valueProperty().subscribe { newValue ->
            // when the value is set, we need to react to ensure it is a
            // valid value (and if not, blow up appropriately)
            newValue?.ifPresent {
                this@IntValueFactory.value = it.coerceIn(
                    this@IntValueFactory.min,
                    this@IntValueFactory.max
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

    private fun wrapValue(value: Int, min: Int, max: Int): Int {
        if (max == 0) {
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
