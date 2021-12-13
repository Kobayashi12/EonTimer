package io.eontimer.util.javafx.spinner

import io.eontimer.util.javafx.bindBidirectional
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory

val <T> Spinner<T>.valueProperty: ObjectProperty<T>?
    get() = valueFactory?.valueProperty()

val <T> Spinner<T>.textProperty: StringProperty
    get() = editor.textProperty()
var <T> Spinner<T>.text: String
    get() = editor.text
    set(value) {
        editor.text = value
    }

fun SpinnerValueFactory<Int>.bindBidirectional(other: IntegerProperty) {
    valueProperty().bindBidirectional(other)
}

fun SpinnerValueFactory<Long>.bindBidirectional(other: LongProperty) {
    valueProperty().bindBidirectional(other)
}

fun <T> SpinnerValueFactory<T>.bindBidirectional(other: Property<T>) {
    valueProperty().bindBidirectional(other)
}

fun <T> Spinner<T>.setValue(value: T) {
    valueFactory?.value = value
}