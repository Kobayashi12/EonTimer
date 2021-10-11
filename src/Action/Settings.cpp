//
// Created by Dylan Meadows on 2020-03-15.
//

#include "Settings.h"

#include <Util/QSettingsProperty.h>

namespace EonTimer::Action {
    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{
            Util::QSettingsProperty("action/mode", static_cast<i32>(Mode::AV)),
            Util::QSettingsProperty("action/sound", static_cast<i32>(Sound::BEEP)),
            Util::QSettingsProperty("action/color", QColor(Qt::blue)),
            Util::QSettingsProperty("action/interval", 500),
            Util::QSettingsProperty("action/count", 6)};
        return properties;
    }

    enum { MODE, SOUND, COLOR, INTERVAL, COUNT };

    Settings::Settings(QSettings *settings) : QObject(settings), settings(settings) {}

    Mode Settings::getMode() const {
        const auto &properties = getProperties();
        return getActionModes()[properties[MODE].getValue(*settings).toInt()];
    }

    void Settings::setMode(const int newValue) {
        auto &properties = getProperties();
        properties[MODE].setValue(*settings, newValue);
    }

    void Settings::setMode(const Mode newValue) { setMode(static_cast<i32>(newValue)); }

    Sound Settings::getSound() const {
        const auto &properties = getProperties();
        return getSounds()[properties[SOUND].getValue(*settings).toInt()];
    }

    void Settings::setSound(const int newValue) {
        auto &properties = getProperties();
        properties[SOUND].setValue(*settings, newValue);
    }

    void Settings::setSound(const Sound newValue) { setSound(static_cast<i32>(newValue)); }

    QColor Settings::getColor() const {
        const auto &properties = getProperties();
        return properties[COLOR].getValue(*settings).value<QColor>();
    }

    void Settings::setColor(const QColor &newValue) {
        if (getColor() != newValue) {
            auto &properties = getProperties();
            properties[COLOR].setValue(*settings, newValue);
            emit colorChanged(newValue);
        }
    }

    std::chrono::milliseconds Settings::getInterval() const {
        const auto &properties = getProperties();
        return std::chrono::milliseconds(properties[INTERVAL].getValue(*settings).toInt());
    }

    void Settings::setInterval(const i32 newValue) {
        auto &properties = getProperties();
        properties[INTERVAL].setValue(*settings, newValue);
    }

    u32 Settings::getCount() const {
        const auto &properties = getProperties();
        return properties[COUNT].getValue(*settings).toUInt();
    }

    void Settings::setCount(const i32 newValue) {
        auto &properties = getProperties();
        properties[COUNT].setValue(*settings, newValue);
    }
}  // namespace EonTimer::Action
