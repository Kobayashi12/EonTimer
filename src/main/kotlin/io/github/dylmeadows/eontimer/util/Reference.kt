package io.github.dylmeadows.eontimer.util

sealed class Reference<T> {
    abstract val value: T
}

class MutableReference<T>(
    override var value: T
) : Reference<T>()

class ImmutableReference<T>(
    override val value: T
) : Reference<T>()

fun <T> T.mutableRef(): MutableReference<T> = MutableReference(this)
fun <T> T.immutableRef(): ImmutableReference<T> = ImmutableReference(this)

fun <T> Reference<T>.mutable(): MutableReference<T> = value.mutableRef()
fun <T> MutableReference<T>.immutable(): ImmutableReference<T> = value.immutableRef()