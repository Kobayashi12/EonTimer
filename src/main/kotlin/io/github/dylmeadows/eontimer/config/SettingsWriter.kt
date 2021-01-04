package io.github.dylmeadows.eontimer.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.dylmeadows.eontimer.model.ApplicationModel
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PreDestroy

@Component
class SettingsWriter(
    private val appProperties: AppProperties,
    private val settings: ApplicationModel,
    private val objectMapper: ObjectMapper
) {
    @PreDestroy
    private fun onDestroy() {
        // persist settings
        objectMapper.writeValue(File("${appProperties.name}.json"), settings)
    }
}