package io.eontimer.util

import kotlin.reflect.KProperty

inline fun <T> List<T>.toBuilder(
    crossinline builderAction: MutableList<T>.() -> Unit
): List<T> {
    val mutable = toMutableList()
    mutable.builderAction()
    return mutable
}

inline fun <T, R> T.map(crossinline block: (T) -> R): R = block(this)
inline fun <T> T?.ifPresent(crossinline block: (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}

operator fun <T, Fn : () -> T> Fn.getValue(thisRef: Any?, property: KProperty<*>): T = invoke()
