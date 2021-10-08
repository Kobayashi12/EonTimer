//
// Created by Dylan Meadows on 2020-03-15.
//

#include "ActionSettingsModel.h"

#include <Util/QSettingsProperty.h>

#include <iostream>

namespace EonTimer::Action {
    static const QString &getGroup() {
        static const QString group = "action";
        return group;
    }

    static std::vector<Util::QSettingsProperty> &getProperties() {
        static std::vector<Util::QSettingsProperty> properties{
            Util::QSettingsProperty("mode", static_cast<i32>(ActionMode::AV)),
            Util::QSettingsProperty("sound", static_cast<i32>(Sound::BEEP)),
            Util::QSettingsProperty("color", QColor(Qt::blue)),
            Util::QSettingsProperty("interval", 500),
            Util::QSettingsProperty("count", 6)};
        return properties;
    }

    enum { MODE, SOUND, COLOR, INTERVAL, COUNT };

    ActionSettingsModel::ActionSettingsModel(QSettings *settings, QObject *parent) : QObject(parent) {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        mode = ActionMode::AUDIO;
        sound = Sound::BEEP;
//        mode = getActionModes()[properties[MODE].getValue(*settings).toInt()];
//        sound = getSounds()[properties[SOUND].getValue(*settings).toInt()];
        color = properties[COLOR].getValue(*settings).value<QColor>();
        interval = std::chrono::milliseconds(properties[INTERVAL].getValue(*settings).toUInt());
        count = properties[COUNT].getValue(*settings).toUInt();
        settings->endGroup();
    }

    void ActionSettingsModel::sync(QSettings *settings) const {
        settings->beginGroup(getGroup());
        auto &properties = getProperties();
        properties[MODE].setValue(*settings, static_cast<i32>(mode));
        properties[SOUND].setValue(*settings, static_cast<i32>(sound));
        properties[COLOR].setValue(*settings, color);
        properties[INTERVAL].setValue(*settings, interval.count());
        properties[COUNT].setValue(*settings, count);
        settings->endGroup();
    }

    ActionMode ActionSettingsModel::getMode() const { return mode; }

    Sound ActionSettingsModel::getSound() const { return sound; }

    const QColor &ActionSettingsModel::getColor() const { return color; }

    std::chrono::milliseconds ActionSettingsModel::getInterval() const { return interval; }

    u32 ActionSettingsModel::getCount() const { return count; }

    void ActionSettingsModel::setMode(const int newValue) {
        setMode(static_cast<ActionMode>(newValue));
    }

    void ActionSettingsModel::setSound(const int newValue) {
        setSound(static_cast<Sound>(newValue));
    }

    void ActionSettingsModel::setMode(const ActionMode newValue) { this->mode = newValue; }

    void ActionSettingsModel::setSound(const Sound newValue) { this->sound = newValue; }

    void ActionSettingsModel::setColor(const QColor &newValue) {
        if (this->color != newValue) {
            this->color = newValue;
            emit colorChanged(newValue);
        }
    }

    void ActionSettingsModel::setInterval(const i32 newValue) {
        this->interval = std::chrono::milliseconds(newValue);
    }

    void ActionSettingsModel::setCount(const i32 newValue) { this->count = newValue; }
}  // namespace EonTimer::Action
