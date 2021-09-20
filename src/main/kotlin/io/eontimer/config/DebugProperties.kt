package io.eontimer.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("debug")
data class DebugProperties(
    val tags: Set<String>
)