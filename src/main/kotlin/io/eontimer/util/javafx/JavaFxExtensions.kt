package io.eontimer.util.javafx

import com.sun.javafx.binding.BidirectionalBinding
import io.eontimer.model.resource.CssResource
import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.FloatProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableDoubleValue
import javafx.beans.value.ObservableFloatValue
import javafx.beans.value.ObservableIntegerValue
import javafx.beans.value.ObservableLongValue
import javafx.beans.value.ObservableValue
import javafx.scene.Scene
import javafx.scene.control.Label
import kotlin.reflect.KProperty

operator fun <T> ObservableValue<T>.getValue(thisRef: Any, property: KProperty<*>): T = this.value!!
operator fun <T> Property<T>.setValue(thisRef: Any, property: KProperty<*>, value: T?) = setValue(value)

operator fun ObservableDoubleValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun DoubleProperty.setValue(thisRef: Any, property: KProperty<*>, value: Double) = set(value)

operator fun ObservableFloatValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun FloatProperty.setValue(thisRef: Any, property: KProperty<*>, value: Float) = set(value)

operator fun ObservableLongValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun LongProperty.setValue(thisRef: Any, property: KProperty<*>, value: Long) = set(value)

operator fun ObservableIntegerValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun IntegerProperty.setValue(thisRef: Any, property: KProperty<*>, value: Int) = set(value)

operator fun ObservableBooleanValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun BooleanProperty.setValue(thisRef: Any, property: KProperty<*>, value: Boolean) = set(value)

fun ObjectProperty<Int>.asIntegerProperty(): IntegerProperty {
    return IntegerProperty.integerProperty(this)
}

fun IntegerProperty.bindBidirectional(property: ObjectProperty<Int>) {
    BidirectionalBinding.bindNumber(this, property)
}

fun ObjectProperty<Int>.bindBidirectional(property: IntegerProperty) {
    BidirectionalBinding.bindNumber(this, property)
}

fun ObjectProperty<Long>.asLongProperty(): LongProperty {
    return LongProperty.longProperty(this)
}

fun LongProperty.bindBidirectional(property: ObjectProperty<Long>) {
    BidirectionalBinding.bindNumber(this, property)
}

fun ObjectProperty<Long>.bindBidirectional(property: LongProperty) {
    BidirectionalBinding.bindNumber(this, property)
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
