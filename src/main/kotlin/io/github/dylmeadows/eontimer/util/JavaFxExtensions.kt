package io.github.dylmeadows.eontimer.util

import com.sun.javafx.binding.BidirectionalBinding
import io.github.dylmeadows.eontimer.model.resource.CssResource
import javafx.beans.property.*
import javafx.beans.value.*
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage
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
    bindBidirectional(property.asIntegerProperty())
}

fun ObjectProperty<Int>.bindBidirectional(property: IntegerProperty) {
    bindBidirectional(property.asObject())
}

fun ObjectProperty<Long>.asLongProperty(): LongProperty {
    return LongProperty.longProperty(this)
}

fun LongProperty.bindBidirectional(property: ObjectProperty<Long>) {
    bindBidirectional(property.asLongProperty())
}

fun ObjectProperty<Long>.bindBidirectional(property: LongProperty) {
    bindBidirectional(property.asObject())
}

fun Scene.addCss(resource: CssResource) {
    stylesheets.add(resource.path)
}

fun Parent.asScene(): Scene {
    return Scene(this)
}

data class Dimension(val width: Double, val height: Double)

var Stage.size: Dimension
    get() = Dimension(width, height)
    set(value) {
        width = value.width
        height = value.height
    }

var Label.isActive: Boolean
    get() = this.styleClass.contains("active")
    set(value) {
        when (value) {
            true -> styleClass.add("active")
            false -> styleClass.remove("active")
        }
    }
