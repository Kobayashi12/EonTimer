package io.github.dylmeadows.eontimer.model.settings

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.commonkt.javafx.beans.property.getValue
import io.github.dylmeadows.commonkt.javafx.beans.property.setValue
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color

class ActionSettings {
    @JsonIgnore
    val modeProperty: ObjectProperty<ActionMode> = SimpleObjectProperty(DEFAULT_MODE)
    var mode: ActionMode by modeProperty

    @JsonIgnore
    val colorProperty: ObjectProperty<Color> = SimpleObjectProperty(DEFAULT_COLOR)
    var color: Color by colorProperty

    @JsonIgnore
    val soundProperty: ObjectProperty<SoundResource> = SimpleObjectProperty(DEFAULT_SOUND)
    var sound: SoundResource by soundProperty

    @JsonIgnore
    val intervalProperty: IntegerProperty = SimpleIntegerProperty(DEFAULT_INTERVAL)
    var interval by intervalProperty

    @JsonIgnore
    val countProperty: IntegerProperty = SimpleIntegerProperty(DEFAULT_COUNT)
    var count by countProperty

    companion object {
        @JvmField
        val DEFAULT_MODE = ActionMode.AUDIO
        @JvmField
        val DEFAULT_SOUND = SoundResource.BEEP
        @JvmField
        val DEFAULT_COLOR: Color = Color.CYAN

        const val DEFAULT_INTERVAL = 500
        const val DEFAULT_COUNT = 6
    }
}