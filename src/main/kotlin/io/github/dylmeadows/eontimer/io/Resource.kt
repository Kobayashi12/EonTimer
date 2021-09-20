package io.github.dylmeadows.eontimer.io

import java.io.InputStream
import java.net.URL

interface Resource {
    val path: String

    val url: URL
        get() = loader.getResource(path)
            ?: error("Unable to find resource for $path")
    val inputStream: InputStream
        get() = loader.getResourceAsStream(path)
            ?: error("Unable to find resource for $path")
    val contents: String
        get() = String(inputStream.readAllBytes())

    private companion object {
        val loader: ClassLoader by lazy {
            Resource::class.java.classLoader
        }
    }
}
