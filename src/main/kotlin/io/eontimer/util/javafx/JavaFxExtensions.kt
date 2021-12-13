package io.eontimer.util.javafx

import com.sun.javafx.binding.BidirectionalBinding
import io.eontimer.model.resource.CssResource
import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.BooleanExpression
import javafx.beans.binding.ObjectExpression
import javafx.beans.property.BooleanProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableIntegerValue
import javafx.beans.value.ObservableLongValue
import javafx.beans.value.ObservableValue
import javafx.scene.Scene
import javafx.scene.control.Label
import java.util.WeakHashMap
import kotlin.reflect.KProperty

infix fun BooleanExpression.or(
    other: ObservableBooleanValue
): BooleanBinding = or(other)

infix fun BooleanExpression.and(
    other: ObservableBooleanValue
): BooleanBinding = and(other)

infix fun <T> ReadOnlyObjectProperty<T>.isEqualTo(
    other: ObservableBooleanValue
): BooleanBinding = isEqualTo(other)

infix fun <T> ReadOnlyObjectProperty<T>.isNotEqualTo(
    other: BooleanExpression
): BooleanBinding = isNotEqualTo(other)

fun <T, R> ObservableValue<T>.mapped(mapper: (T) -> R): ObservableValue<R> =
    object : ObservableValue<R> {
        private val listenerMapping = WeakHashMap<ChangeListener<in R>, ChangeListener<T>>()

        override fun addListener(listener: ChangeListener<in R>) {
            if (listener !in listenerMapping) {
                val delegate = ChangeListener<T> { observable, oldValue, newValue ->
                    listener.changed(this, mapper(oldValue), mapper(newValue))
                }
                this@mapped.addListener(delegate)
                listenerMapping[listener] = delegate
            }
        }

        override fun addListener(listener: InvalidationListener) {
            this@mapped.addListener(listener)
        }

        override fun removeListener(listener: ChangeListener<in R>?) {
            listenerMapping[listener]?.also {
                this@mapped.removeListener(it)
                listenerMapping.remove(listener)
            }
        }

        override fun removeListener(listener: InvalidationListener) {
            this@mapped.removeListener(listener)
        }

        override fun getValue(): R =
            mapper(this@mapped.value)
    }

fun <T> ObservableValue<T>.onChange(onPlatform: Boolean = true, fn: (T) -> Unit) =
    ChangeListener<T> { _, _, newValue -> fxInvoke(onPlatform) { fn(newValue) } }
        .also(this::addListener)

private inline fun fxInvoke(
    onPlatform: Boolean,
    crossinline fn: () -> Unit
) = if (onPlatform) {
    Platform.runLater { fn() }
} else {
    fn()
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

operator fun LongProperty.plusAssign(value: Long) {
    setValue(getValue() + value)
}

fun Scene.addCss(resource: CssResource) {
    stylesheets.add(resource.path)
}

var Label.isActive: Boolean
    get() = this.styleClass.contains("active")
    set(value) {
        when (value) {
            true -> styleClass.add("active")
            false -> styleClass.remove("active")
        }
    }
