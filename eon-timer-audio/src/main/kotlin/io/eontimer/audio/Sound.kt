package io.eontimer.audio

import io.github.landerlyoung.jenny.NativeClass
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.FileInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.LongBuffer
import java.nio.channels.Channels

class Sound private constructor(
    initializer: Init
) : NativeObject(initializer) {
    constructor(stream: InputStream) : this({
        Native.allocate(it.directBuffer)
        val buffer = ByteBuffer.allocateDirect(stream.available())
        Channels.newChannel(stream).read(buffer)
        Native.load(it.pointer, buffer)
        Pointer(it)
    })

    fun play() = Native.play(pointer.rawAddress)

    @NativeClass(namespace = "Sound")
    object Native {
        // @formatter:off
        @JvmStatic internal external fun allocate(buffer: LongBuffer)
        @JvmStatic internal external fun load(rawAddress: Long, buffer: ByteBuffer)
        @JvmStatic internal external fun play(rawAddress: Long)
        // @formatter:on
    }
}

fun main(args: Array<String>) {
    val library = Sound::class.java.classLoader.getResource("libEonTimerAudio.so")!!
    println(library.file)
    System.load(library.file)
    runBlocking {
        val stream = FileInputStream("/home/dmeadows/Workspace/EonTimer/src/main/resources/io/eontimer/sounds/beep.wav")
        val sound = Sound(stream)
        repeat(3) {
            delay(500)
            sound.play()
        }
    }
}