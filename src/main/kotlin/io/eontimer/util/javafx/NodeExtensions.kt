package io.eontimer.util.javafx

import javafx.beans.binding.BooleanExpression
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Node

fun Node.showWhen(
    condition: BooleanExpression
): Disposable = hideWhen(condition.not())

fun Node.hideWhen(
    condition: BooleanExpression
): Disposable {
    visibleProperty().bind(condition.not())
    managedProperty().bind(visibleProperty())
    return object : Disposable, ChangeListener<Boolean> {
        init {
            condition.addListener(this)
        }

        override fun changed(
            observable: ObservableValue<out Boolean>?,
            oldValue: Boolean?,
            newValue: Boolean?
        ) {
            autosize()
        }

        override fun dispose() {
            condition.removeListener(this)
        }
    }
}

fun Node.setOnFocusLost(
    onFocusLost: () -> Unit
): Disposable {
    return object : Disposable, ChangeListener<Boolean> {
        private val property by lazy(::focusedProperty)

        init {
            property.addListener(this)
        }

        override fun changed(
            observable: ObservableValue<out Boolean>?,
            oldValue: Boolean?,
            newValue: Boolean?
        ) {
            if (newValue != null && !newValue) {
                onFocusLost()
            }
        }

        override fun dispose() {
            property.removeListener(this)
        }
    }
}