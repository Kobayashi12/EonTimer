//
// Created by Dylan Meadows on 2020-03-14.
//

#include "SoundService.h"

#include <QResource>

namespace service {
    SoundService::SoundService(const model::settings::ActionSettingsModel *actionSettings, QObject *parent)
        : QObject(parent), actionSettings(actionSettings) {
        soundEffects[model::Sound::BEEP] = loadSound(":/sounds/beep.wav");
        soundEffects[model::Sound::DING] = loadSound(":/sounds/ding.wav");
        soundEffects[model::Sound::TICK] = loadSound(":/sounds/tick.wav");
        soundEffects[model::Sound::POP] = loadSound(":/sounds/pop.wav");
    }

    void SoundService::play() {
        const auto mode = actionSettings->getMode();
        if (mode == model::ActionMode::AUDIO || mode == model::ActionMode::AV) {
            auto sound = soundEffects.find(actionSettings->getSound());
            if (sound != soundEffects.end()) {
                sound->second->play();
            }
        }
    }

    QSoundEffect *SoundService::loadSound(const char *filename) {
        auto *sound = new QSoundEffect(this);
        sound->setSource(QUrl(filename));
        return sound;
    }
}  // namespace service
