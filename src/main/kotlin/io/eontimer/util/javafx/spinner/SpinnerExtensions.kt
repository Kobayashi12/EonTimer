package io.eontimer.util.javafx.spinner

import io.eontimer.util.javafx.Disposable
import javafx.beans.property.ObjectProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.scene.control.Spinner

val <T> Spinner<T>.valueProperty: ObjectProperty<T>?
    get() = valueFactory?.valueProperty()

val <T> Spinner<T>.textProperty: StringProperty
    get() = editor.textProperty()
var <T> Spinner<T>.text: String
    get() = editor.text
    set(value) {
        editor.text = value
    }

fun <T> Spinner<T>.setValue(value: T) {
    valueFactory?.value = value
}

fun <T> Spinner<T>.setOnFocusLost(onFocusLost: () -> Unit): Disposable {
    val property = focusedProperty()
    val listener = ChangeListener<Boolean> { _, _, newValue ->
        if (!newValue) onFocusLost()
    }
    property.addListener(listener)
    return Disposable {
        property.removeListener(listener)
    }
}