package io.eontimer.model.resource

import java.io.InputStream
import java.net.URL

const val BASE_RESOURCE_PATH = "io/eontimer"

interface Resource {
    val path: String

    val url: URL
        get() = loader.getResource(path)
            ?: error("Unable to find resource for $path")
    val inputStream: InputStream
        get() = loader.getResourceAsStream(path)
            ?: error("Unable to find resource for $path")
    val contents: String
        get() = String(inputStream.readAllBytes())

    companion object {
        private val loader: ClassLoader by lazy {
            Resource::class.java.classLoader
        }
    }
}

enum class CssResource(
    fileName: String
) : Resource {
    MAIN("main.css");

    override val path = "$BASE_RESOURCE_PATH/css/$fileName"
}

enum class FxmlResource(
    fileName: String
) : Resource {
    EonTimerPane("EonTimerPane.fxml"),
    Gen3TimerPane("timer/Gen3TimerPane.fxml"),
    Gen4TimerPane("timer/Gen4TimerPane.fxml"),
    Gen5TimerPane("timer/Gen5TimerPane.fxml"),
    CustomTimerPane("timer/CustomTimerPane.fxml"),
    TimerControlPane("timer/TimerControlPane.fxml"),
    ActionSettingsPane("settings/ActionSettingsPane.fxml"),
    TimerSettingsPane("settings/TimerSettingsPane.fxml"),
    SettingsControlPane("settings/SettingsControlPane.fxml"),
    TimerDisplayPane("TimerDisplayPane.fxml");

    override val path = "$BASE_RESOURCE_PATH/fxml/$fileName"
}

enum class ImageResource(
    fileName: String
) : Resource {
    DefaultBackgroundImage("default_background_image.png");

    override val path = "$BASE_RESOURCE_PATH/img/$fileName"
}

