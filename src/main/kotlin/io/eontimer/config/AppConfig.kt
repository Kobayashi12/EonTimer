package io.eontimer.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.eontimer.model.ApplicationModel
import io.eontimer.model.settings.ActionSettings
import io.eontimer.model.settings.TimerSettings
import io.eontimer.model.timer.CustomTimer
import io.eontimer.model.timer.Gen3Timer
import io.eontimer.model.timer.Gen4Timer
import io.eontimer.model.timer.Gen5Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.javafx.JavaFx
import org.springframework.beans.factory.getBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.File
import java.io.FileOutputStream
import javax.annotation.PreDestroy

@Configuration
@EnableConfigurationProperties(AppProperties::class, DebugProperties::class)
class AppConfig(
    private val properties: AppProperties,
    private val context: ApplicationContext
) {
    @PreDestroy
    private fun destroy() {
        try {
            val settings = context.getBean<ApplicationModel>()
            val objectMapper = context.getBean<ObjectMapper>()
            objectMapper.writeValue(FileOutputStream("${properties.name}.json"), settings)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Bean
    fun settings(objectMapper: ObjectMapper): ApplicationModel {
        val file = File("${properties.name}.json")
        return when {
            file.exists() -> objectMapper.readValue(file)
            else -> ApplicationModel()
        }
    }

    @Bean
    fun gen3TimerModel(settings: ApplicationModel): Gen3Timer = settings.gen3

    @Bean
    fun gen4TimerModel(settings: ApplicationModel): Gen4Timer = settings.gen4

    @Bean
    fun gen5TimerModel(settings: ApplicationModel): Gen5Timer = settings.gen5

    @Bean
    fun customTimerModel(settings: ApplicationModel): CustomTimer = settings.custom

    @Bean
    fun actionSettingsModel(settings: ApplicationModel): ActionSettings = settings.actionSettings

    @Bean
    fun timerSettingsModel(settings: ApplicationModel): TimerSettings = settings.timerSettings

    @Bean
    fun defaultScope() = CoroutineScope(Dispatchers.Default + SupervisorJob())
}