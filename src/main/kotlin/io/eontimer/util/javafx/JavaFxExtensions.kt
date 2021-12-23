package io.eontimer.util.javafx

import com.sun.javafx.binding.BidirectionalBinding
import io.eontimer.model.resource.CssResource
import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.BooleanExpression
import javafx.beans.property.BooleanProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableIntegerValue
import javafx.beans.value.ObservableLongValue
import javafx.beans.value.ObservableValue
import javafx.scene.Scene
import javafx.scene.control.Label
import kotlin.reflect.KProperty

infix fun BooleanExpression.or(
    other: ObservableBooleanValue
): BooleanBinding = or(other)

infix fun BooleanExpression.and(
    other: ObservableBooleanValue
): BooleanBinding = and(other)

infix fun <T> ObservableValue<T>.isEqualTo(
    value: T
): BooleanBinding =
    Bindings.createBooleanBinding({
        this@isEqualTo.value == value
    }, this)

infix fun <T> ObservableValue<T>.isNotEqualTo(
    value: T
): BooleanBinding = !isEqualTo(value)

fun <T> ObservableValue<T>.onChange(scheduler: Scheduler = Scheduler.PLATFORM, fn: (T) -> Unit) =
    ChangeListener<T> { _, _, newValue -> scheduler.execute { fn(newValue) } }
        .also(this::addListener)

enum class Scheduler {
    IMMEDIATE {
        override fun execute(fn: () -> Unit) {
            fn()
        }
    },
    PLATFORM {
        override fun execute(fn: () -> Unit) {
            Platform.runLater(fn)
        }
    };

    abstract fun execute(fn: () -> Unit)
    operator fun invoke(fn: () -> Unit) = execute(fn)
}

operator fun <T> ObservableValue<T>.getValue(thisRef: Any, property: KProperty<*>): T = this.value!!
operator fun <T> Property<T>.setValue(thisRef: Any, property: KProperty<*>, value: T?) = setValue(value)

operator fun ObservableLongValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun LongProperty.setValue(thisRef: Any, property: KProperty<*>, value: Long) = set(value)

operator fun ObservableIntegerValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun IntegerProperty.setValue(thisRef: Any, property: KProperty<*>, value: Int) = set(value)

operator fun ObservableBooleanValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun BooleanProperty.setValue(thisRef: Any, property: KProperty<*>, value: Boolean) = set(value)

fun ObjectProperty<Int>.bindBidirectional(property: IntegerProperty) {
    BidirectionalBinding.bindNumber(this, property)
}

fun ObjectProperty<Long>.bindBidirectional(property: LongProperty) {
    BidirectionalBinding.bindNumber(this, property)
}

fun BooleanProperty.flip() {
    set(!get())
}

operator fun LongProperty.plusAssign(value: Long) {
    setValue(getValue() + value)
}

fun Scene.addCss(resource: CssResource) {
    stylesheets.add(resource.path)
}
