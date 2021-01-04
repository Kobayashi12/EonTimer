package io.github.dylmeadows.eontimer.model.settings

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color

class ActionSettingsModel {
    @JsonIgnore
    val modeProperty: ObjectProperty<ActionMode> = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_MODE)
    var mode: ActionMode by modeProperty

    @JsonIgnore
    val colorProperty: ObjectProperty<Color> = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_COLOR)
    var color: Color by colorProperty

    @JsonIgnore
    val soundProperty: ObjectProperty<SoundResource> = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_SOUND)
    var sound: SoundResource by soundProperty

    @JsonIgnore
    val intervalProperty: IntegerProperty = SimpleIntegerProperty(ActionSettingsConstants.DEFAULT_INTERVAL)
    var interval by intervalProperty

    @JsonIgnore
    val countProperty: IntegerProperty = SimpleIntegerProperty(ActionSettingsConstants.DEFAULT_COUNT)
    var count by countProperty
}