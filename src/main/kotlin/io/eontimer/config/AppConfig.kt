package io.eontimer.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.eontimer.gen4.Gen4Timer
import io.eontimer.model.ApplicationModel
import io.eontimer.model.settings.ActionSettings
import io.eontimer.model.settings.TimerSettings
import io.eontimer.model.timer.CustomTimer
import io.eontimer.model.timer.Gen5Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileOutputStream
import io.eontimer.gen3.Model as Gen3Model
import io.eontimer.gen3.StoredSettings as Gen3StoredSettings

@Configuration
@EnableConfigurationProperties(AppProperties::class, DebugProperties::class)
class AppConfig {
    @Bean
    fun shutdownHook(
        properties: AppProperties,
        gen3Model: Gen3Model,
        objectMapper: ObjectMapper
    ) = DisposableBean {
        try {
            val storedSettings = StoredSettingsDto(Gen3StoredSettings(gen3Model))
            objectMapper.writeValue(FileOutputStream("${properties.name}.json"), storedSettings)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Bean
    fun storedSettings(
        properties: AppProperties,
        objectMapper: ObjectMapper
    ): StoredSettingsDto =
        File("${properties.name}.json")
            .takeIf(File::exists)
            ?.let(objectMapper::readValue)
            ?: StoredSettingsDto()

    @Bean
    fun applicationModel() = ApplicationModel()

    @Bean
    fun gen3TimerModel(
        storedSettings: StoredSettingsDto
    ) = Gen3Model(storedSettings.gen3)

    @Bean
    fun gen4TimerModel(): Gen4Timer = Gen4Timer()

    @Bean
    fun gen5TimerModel(): Gen5Timer = Gen5Timer()

    @Bean
    fun customTimerModel(): CustomTimer = CustomTimer()

    @Bean
    fun actionSettingsModel(): ActionSettings = ActionSettings()

    @Bean
    fun timerSettingsModel(): TimerSettings = TimerSettings()

    @Bean
    fun defaultScope() = CoroutineScope(Dispatchers.Default + SupervisorJob())
}