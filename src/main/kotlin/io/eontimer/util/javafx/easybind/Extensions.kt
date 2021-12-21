package io.eontimer.util.javafx.easybind

import javafx.beans.value.ObservableValue
import org.fxmisc.easybind.EasyBind

fun <T> ObservableValue<T>.monadic() = EasyBind.monadic(this)
fun <T, R> ObservableValue<T>.map(mapper: (T) -> R) = EasyBind.map(this, mapper)
fun <T> ObservableValue<T>.subscribe(subscriber: (T) -> Unit) = EasyBind.subscribe(this, subscriber)