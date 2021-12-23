package io.eontimer.util

fun <T> MutableList<T>.peek() = elementAtOrNull(0)

fun <T> MutableList<T>.pop(): T? = if (isEmpty()) null else removeAt(0)

fun <T> MutableList<T>.removeIndices(indices: List<Int>) {
    indices.forEachIndexed { offset, selectedIndex -> removeAt(selectedIndex - offset) }
}

fun <T> List<T>.toBuilder(
    builderAction: MutableList<T>.() -> Unit
): List<T> {
    val mutable = toMutableList()
    mutable.builderAction()
    return mutable
}
