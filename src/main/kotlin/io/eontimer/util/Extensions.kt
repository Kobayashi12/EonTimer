package io.eontimer.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Boolean.ifElse(
    whenTrue: () -> T,
    whenFalse: () -> T
): T =
    when (this) {
        true -> whenTrue()
        false -> whenFalse()
    }

fun <T> MutableList<T>.removeIndices(indices: List<Int>) {
    indices.forEachIndexed { offset, selectedIndex -> removeAt(selectedIndex - offset) }
}