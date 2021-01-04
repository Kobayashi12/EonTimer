package io.github.dylmeadows.eontimer.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.dylmeadows.eontimer.model.ApplicationModel
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
@EnableConfigurationProperties(AppProperties::class)
class AppConfig(
    private val properties: AppProperties
) {
    @Bean
    fun settings(objectMapper: ObjectMapper): ApplicationModel {
        val file = File("${properties.name}.json")
        return if (file.exists()) {
            objectMapper.readValue(file)
        } else {
            ApplicationModel()
        }
    }

    @Bean
    fun gen3TimerModel(settings: ApplicationModel): Gen3TimerModel = settings.gen3

    @Bean
    fun gen4TimerModel(settings: ApplicationModel): Gen4TimerModel = settings.gen4

    @Bean
    fun gen5TimerModel(settings: ApplicationModel): Gen5TimerModel = settings.gen5

    @Bean
    fun customTimerModel(settings: ApplicationModel): CustomTimerModel = settings.custom

    @Bean
    fun actionSettingsModel(settings: ApplicationModel): ActionSettingsModel = settings.actionSettings

    @Bean
    fun timerSettingsModel(settings: ApplicationModel): TimerSettingsModel = settings.timerSettings
}