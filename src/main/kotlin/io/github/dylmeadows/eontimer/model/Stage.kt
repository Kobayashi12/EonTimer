package io.github.dylmeadows.eontimer.model

interface Reference<T> {
    val value: T
}

interface MutableReference<T> : Reference<T> {
    override var value: T
}

private class ReferenceImpl<T>(
    override val value: T
) : Reference<T>

private class MutableReferenceImpl<T>(
    override var value: T
) : MutableReference<T>

fun <T> T.asReference(): Reference<T> = ReferenceImpl(this)
fun <T> T.asMutableReference(): MutableReference<T> = MutableReferenceImpl(this)

fun <T> Reference<T>.mutable(): MutableReference<T> = MutableReferenceImpl(value)
fun <T> MutableReference<T>.immutable(): Reference<T> = ReferenceImpl(value)