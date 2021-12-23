package io.eontimer.action

import io.eontimer.model.resource.BASE_RESOURCE_PATH
import io.eontimer.util.javafx.getValue
import javafx.application.Platform
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.net.URL
import javax.annotation.PostConstruct

@Component
class SoundPlayer(
    actionSettings: Settings,
    private val coroutineScope: CoroutineScope
) {
    private val sound by actionSettings.sound
    private lateinit var mediaPlayer: MediaPlayer

    @PostConstruct
    private fun initialize() {
        Platform.runLater {
            mediaPlayer = createMediaPlayer(sound.url)
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