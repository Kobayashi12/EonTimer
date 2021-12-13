package io.eontimer.util

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