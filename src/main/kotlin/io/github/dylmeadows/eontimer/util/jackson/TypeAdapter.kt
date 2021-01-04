package io.github.dylmeadows.eontimer.util.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer

interface TypeAdapter<T> {
    fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider)
    fun deserialize(parser: JsonParser, ctxt: DeserializationContext): T
}

inline fun <reified T> TypeAdapter<T>.toSerializer(): JsonSerializer<T> =
    object : StdSerializer<T>(T::class.java) {
        override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider) {
            this@toSerializer.serialize(value, gen, provider)
        }
    }

inline fun <reified T> TypeAdapter<T>.toDeserializer(): JsonDeserializer<T> =
    object : StdDeserializer<T>(T::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T =
            this@toDeserializer.deserialize(p, ctxt)
    }

inline fun <reified T> ObjectMapper.addTypeAdapter(adapter: TypeAdapter<T>): ObjectMapper =
    registerModule(object : SimpleModule() {
        init {
            addSerializer(T::class.java, adapter.toSerializer())
            addDeserializer(T::class.java, adapter.toDeserializer())
        }
    })