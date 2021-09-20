package io.eontimer.util

class Stack<T>(list: List<T>) {
    private val delegate = list.toMutableList()
    private val isEmpty: Boolean get() = delegate.isEmpty()

    fun push(value: T) {
        delegate.add(0, value)
    }

    fun peek(): T? = if (!isEmpty) delegate[0] else null

    fun pop(): T? = if (!isEmpty) delegate.removeAt(0) else null
}

fun <T> List<T>.asStack(): Stack<T> = Stack(this)
