package io.eontimer.util.javafx

import javafx.beans.binding.BooleanExpression
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

fun Node.showWhen(
    condition: BooleanExpression
): Disposable = hideWhen(!condition)

fun Node.hideWhen(
    condition: BooleanExpression
): Disposable {
    visibleProperty().bind(!condition)
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

fun Node.enableWhen(
    condition: BooleanExpression
): Disposable = disableWhen(!condition)

fun Node.disableWhen(
    condition: BooleanExpression
): Disposable {
    disableProperty().bind(condition)
    return Disposable {
        disableProperty().unbind()
    }
}

fun Tab.disableWhen(
    condition: BooleanExpression
): Disposable {
    disableProperty().bind(condition)
    return Disposable {
        disableProperty().unbind()
    }
}

fun Node.setOnFocusLost(
    onFocusLost: () -> Unit
): Disposable {
    val property = focusedProperty()
    val listener = ChangeListener<Boolean> { _, _, newValue ->
        if (!newValue) onFocusLost()
    }
    property.addListener(listener)
    return Disposable {
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