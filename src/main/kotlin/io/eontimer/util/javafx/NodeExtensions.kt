package io.eontimer.util.javafx

import javafx.beans.binding.BooleanExpression
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.fxmisc.easybind.Subscription

fun Node.showWhen(
    condition: BooleanExpression
): Subscription = hideWhen(!condition)

fun Node.hideWhen(
    condition: BooleanExpression
): Subscription {
    visibleProperty().bind(!condition)
    managedProperty().bind(visibleProperty())
    return object : Subscription, ChangeListener<Boolean> {
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

        override fun unsubscribe() {
            condition.removeListener(this)
        }
    }
}

fun Node.setOnFocusLost(
    onFocusLost: () -> Unit
): Subscription {
    val property = focusedProperty()
    val listener = ChangeListener<Boolean> { _, _, newValue ->
        if (!newValue) onFocusLost()
    }
    property.addListener(listener)
    return Subscription {
        property.removeListener(listener)
    }
}

fun Node.onKeyPressed(vararg keys: KeyCode, onKeyPressed: (KeyEvent) -> Unit) {
    setOnKeyPressed {
        if (it.code in keys) {
            onKeyPressed(it)
        }
    }
}