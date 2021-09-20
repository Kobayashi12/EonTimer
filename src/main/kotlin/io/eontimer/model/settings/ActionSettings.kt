package io.eontimer.model.settings

import io.eontimer.model.resource.SoundResource
import io.eontimer.util.javafx.getValue
import io.eontimer.util.javafx.setValue
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color

class ActionSettings {
    val modeProperty: ObjectProperty<ActionMode> = SimpleObjectProperty(Defaults.MODE)
    val colorProperty: ObjectProperty<Color> = SimpleObjectProperty(Defaults.COLOR)
    val soundProperty: ObjectProperty<SoundResource> = SimpleObjectProperty(Defaults.SOUND)
    val intervalProperty: IntegerProperty = SimpleIntegerProperty(Defaults.INTERVAL)
    val countProperty: IntegerProperty = SimpleIntegerProperty(Defaults.COUNT)

    var mode: ActionMode by modeProperty
    var color: Color by colorProperty
    var sound: SoundResource by soundProperty
    var interval by intervalProperty
    var count by countProperty

    object Defaults {
        // @formatter:off
        @JvmField val MODE = ActionMode.AUDIO
        @JvmField val SOUND = SoundResource.BEEP
        @JvmField val COLOR: Color = Color.CYAN
        const val INTERVAL = 500
        const val COUNT = 6
        // @formatter:on
    }
}