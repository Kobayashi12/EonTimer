package io.eontimer.util

class ReadOnlyArray<T>(
    private val array: Array<T>
) : Iterable<T> {
    val size: Int get() = array.size

    operator fun get(index: Int): T = array[index]

    override fun iterator(): Iterator<T> = array.iterator()
}