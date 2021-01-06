package io.github.dylmeadows.eontimer.util.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.dylmeadows.commonkt.javafx.scene.paint.toHex
import javafx.scene.paint.Color
import org.springframework.stereotype.Component

@Component
class ColorAdapter : TypeAdapter<Color> {
    override fun serialize(value: Color, gen: JsonGenerator, provider: SerializerProvider) =
        gen.writeString(value.toHex())

    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Color =
        Color.web(parser.readValueAs(String::class.java))
}
