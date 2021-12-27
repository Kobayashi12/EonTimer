package io.eontimer.util.javafx

import com.sun.javafx.binding.BidirectionalBinding
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.BooleanProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableIntegerValue
import javafx.beans.value.ObservableLongValue
import javafx.beans.value.ObservableValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.withContext
import org.fxmisc.easybind.EasyBind
import org.fxmisc.easybind.Subscription
import kotlin.reflect.KProperty

fun anyChangesOf(
    vararg properties: ObservableValue<*>,
    onChange: (ObservableValue<*>) -> Unit
) {
    properties.forEach { observable ->
        observable.subscribe { onChange(observable) }
    }
}

suspend inline fun onPlatform(noinline block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.JavaFx, block)
}

fun ObservableBooleanValue.or(
    other: ObservableBooleanValue
): BooleanBinding = BooleanBinding.booleanExpression(this).or(other)

fun <T, R> ObservableValue<T>.map(
    mapper: (T) -> R
): ObservableValue<R> =
    EasyBind.map(this, mapper)

fun <T> ObservableValue<T>.mapToBoolean(
    mapper: (T) -> Boolean
): ObservableBooleanValue =
    Bindings.createBooleanBinding({ mapper(value) }, this)

fun <T, R> ObservableValue<T>.flatMap(
    mapper: (T) -> ObservableValue<R>
): ObservableValue<R> =
    EasyBind.monadic(this)
        .flatMap(mapper)

fun <T> ObservableValue<T>.filter(
    predicate: (T) -> Boolean
): ObservableValue<T> =
    EasyBind.monadic(this)
        .filter(predicate)

fun <T> ObservableValue<T>.subscribe(
    emitCurrentValue: Boolean = true,
    subscriber: (T) -> Unit
): Subscription {
    if (emitCurrentValue) subscriber(value)
    val listener = ChangeListener<T> { _, _, newValue -> subscriber(newValue) }
    addListener(listener)
    return Subscription {
        removeListener(listener)
    }
}

operator fun <T> ObservableValue<T>.getValue(thisRef: Any?, property: KProperty<*>): T = this.value!!
operator fun <T> Property<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T?) = setValue(value)

operator fun ObservableLongValue.getValue(thisRef: Any?, property: KProperty<*>) = get()
operator fun LongProperty.setValue(thisRef: Any?, property: KProperty<*>, value: Long) = set(value)

operator fun ObservableIntegerValue.getValue(thisRef: Any?, property: KProperty<*>) = get()
operator fun IntegerProperty.setValue(thisRef: Any?, property: KProperty<*>, value: Int) = set(value)

operator fun ObservableBooleanValue.getValue(thisRef: Any?, property: KProperty<*>) = get()
operator fun BooleanProperty.setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) = set(value)

fun ObjectProperty<Int>.bindBidirectional(property: IntegerProperty) {
    BidirectionalBinding.bindNumber(this, property)
}

fun ObjectProperty<Long>.bindBidirectional(property: LongProperty) {
    BidirectionalBinding.bindNumber(this, property)
}

operator fun LongProperty.plusAssign(value: Long) {
    setValue(getValue() + value)
}

fun BooleanProperty.flip() {
    set(!get())
}
