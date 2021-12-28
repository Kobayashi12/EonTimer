package io.eontimer.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.eontimer.action.ActionSettings
import io.eontimer.action.ActionStoredSettings
import io.eontimer.custom.CustomTimerModel
import io.eontimer.custom.CustomTimerStoredSettings
import io.eontimer.gen3.Gen3TimerModel
import io.eontimer.gen3.Gen3TimerStoredSettings
import io.eontimer.gen4.Gen4TimerModel
import io.eontimer.gen4.Gen4TimerStoredSettings
import io.eontimer.gen5.Gen5TimerModel
import io.eontimer.gen5.Gen5TimerStoredSettings
import io.eontimer.timer.TimerSettings
import io.eontimer.timer.TimerStoredSettings
import javafx.beans.property.SimpleObjectProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileOutputStream

@Configuration
@EnableConfigurationProperties(AppProperties::class, DebugProperties::class)
class AppConfig {
    @Bean
    fun shutdownHook(
        properties: AppProperties,
        objectMapper: ObjectMapper,
        gen3TimerModel: Gen3TimerModel,
        gen4TimerModel: Gen4TimerModel,
        gen5TimerModel: Gen5TimerModel,
        customTimerModel: CustomTimerModel,
        actionSettings: ActionSettings,
        timerSettings: TimerSettings,
    ) = DisposableBean {
        try {
            objectMapper.writeValue(
                FileOutputStream("${properties.name}.json"),
                StoredSettings(
                    gen3 = Gen3TimerStoredSettings(gen3TimerModel),
                    gen4 = Gen4TimerStoredSettings(gen4TimerModel),
                    gen5 = Gen5TimerStoredSettings(gen5TimerModel),
                    custom = CustomTimerStoredSettings(customTimerModel),
                    actionSettings = ActionStoredSettings(actionSettings),
                    timerSettings = TimerStoredSettings(timerSettings),
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Bean
    fun storedSettings(
        properties: AppProperties,
        objectMapper: ObjectMapper
    ): StoredSettings =
        File("${properties.name}.json")
            .takeIf(File::exists)
            ?.let(objectMapper::readValue)
            ?: StoredSettings()

    @Bean
    fun selectedTimerTab(
        storedSettings: StoredSettings
    ) = SimpleObjectProperty(storedSettings.selectedTimerTab)

    @Bean
    fun gen3TimerModel(
        storedSettings: StoredSettings
    ) = Gen3TimerModel(storedSettings.gen3)

    @Bean
    fun gen4TimerModel(
        storedSettings: StoredSettings
    ) = Gen4TimerModel(storedSettings.gen4)

    @Bean
    fun gen5TimerModel(
        storedSettings: StoredSettings
    ) = Gen5TimerModel(storedSettings.gen5)

    @Bean
    fun customTimerModel(
        storedSettings: StoredSettings
    ) = CustomTimerModel(storedSettings.custom)

    @Bean
    fun actionSettings(
        storedSettings: StoredSettings
    ) = ActionSettings(storedSettings.actionSettings)

    @Bean
    fun timerSettings(
        storedSettings: StoredSettings
    ) = TimerSettings(storedSettings.timerSettings)

    @Bean
    fun defaultScope() = CoroutineScope(Dispatchers.Default + SupervisorJob())
}