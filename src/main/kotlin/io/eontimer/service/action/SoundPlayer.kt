package io.eontimer.service.action

import io.eontimer.model.resource.BASE_RESOURCE_PATH
import io.eontimer.model.settings.ActionSettings
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.net.URL
import javax.annotation.PostConstruct

@Component
@ExperimentalCoroutinesApi
class SoundPlayer(
    private val actionSettings: ActionSettings,
    private val coroutineScope: CoroutineScope
) {
    private lateinit var mediaPlayer: MediaPlayer

    @PostConstruct
    private fun initialize() {
        coroutineScope.launch {
            actionSettings.soundProperty.asFlow()
                .collect { mediaPlayer = createMediaPlayer(it.url) }
        }
        // NOTE: this buffers the MediaPlayer.
        // Without this buffering, the first time
        // audio is played, it is delayed.
        coroutineScope.launch {
            val path = "$BASE_RESOURCE_PATH/sounds/silence.wav"
            val resource = javaClass.classLoader.getResource(path) ?: error("$path does not exist")
            createMediaPlayer(resource).play()
        }
    }

    private fun createMediaPlayer(url: URL): MediaPlayer =
        MediaPlayer(Media(url.toExternalForm()))

    fun play() {
        mediaPlayer.startTime = Duration.ZERO
        mediaPlayer.seek(Duration.ZERO)
        mediaPlayer.play()
    }

    fun stop() {
        mediaPlayer.stop()
    }
}