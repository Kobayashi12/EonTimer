package io.github.dylmeadows.eontimer.util.jackson

import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.beans.factory.getBeansOfType
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class CustomModule(
    ctx: ApplicationContext
) : SimpleModule() {
    init {
        ctx.getBeansOfType<TypeAdapter<Any>>()
            .forEach { (_, adapter) ->
                addTypeAdapter(adapter)
            }
    }

    private inline fun <reified T> addTypeAdapter(adapter: TypeAdapter<T>) {
        addSerializer(T::class.java, adapter.toSerializer())
        addDeserializer(T::class.java, adapter.toDeserializer())
    }
}