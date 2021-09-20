package io.github.dylmeadows.eontimer.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("application")
data class AppProperties(
    val name: String,
    val version: String
) {
    val fullApplicationName = "$name v$version"
}