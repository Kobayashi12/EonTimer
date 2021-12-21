package io.eontimer.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.eontimer.model.ApplicationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileOutputStream
import io.eontimer.action.Settings as ActionSettings
import io.eontimer.action.StoredSettings as ActionStoredSettings
import io.eontimer.custom.Model as CustomModel
import io.eontimer.custom.StoredSettings as CustomStoredSettings
import io.eontimer.gen3.Model as Gen3Model
import io.eontimer.gen3.StoredSettings as Gen3StoredSettings
import io.eontimer.gen4.Model as Gen4Model
import io.eontimer.gen4.StoredSettings as Gen4StoredSettings
import io.eontimer.gen5.Model as Gen5Model
import io.eontimer.gen5.StoredSettings as Gen5StoredSettings
import io.eontimer.timer.Settings as TimerSettings
import io.eontimer.timer.StoredSettings as TimerStoredSettings

@Configuration
@EnableConfigurationProperties(AppProperties::class, DebugProperties::class)
class AppConfig {
    @Bean
    fun shutdownHook(
        properties: AppProperties,
        objectMapper: ObjectMapper,
        gen3Model: Gen3Model,
        gen4Model: Gen4Model,
        gen5Model: Gen5Model,
        customModel: CustomModel,
        actionSettings: ActionSettings,
        timerSettings: TimerSettings,
    ) = DisposableBean {
        try {
            objectMapper.writeValue(
                FileOutputStream("${properties.name}.json"),
                StoredSettings(
                    gen3 = Gen3StoredSettings(gen3Model),
                    gen4 = Gen4StoredSettings(gen4Model),
                    gen5 = Gen5StoredSettings(gen5Model),
                    custom = CustomStoredSettings(customModel),
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
    fun applicationModel(
        storedSettings: StoredSettings
    ) = ApplicationModel()

    @Bean
    fun gen3Model(
        storedSettings: StoredSettings
    ) = Gen3Model(storedSettings.gen3)

    @Bean
    fun gen4Model(
        storedSettings: StoredSettings
    ) = Gen4Model(storedSettings.gen4)

    @Bean
    fun gen5Model(
        storedSettings: StoredSettings
    ) = Gen5Model(storedSettings.gen5)

    @Bean
    fun customModel(
        storedSettings: StoredSettings
    ) = CustomModel(storedSettings.custom)

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