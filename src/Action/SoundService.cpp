//
// Created by Dylan Meadows on 2020-03-14.
//

#include "SoundService.h"
#include "SFML/Audio/SoundBuffer.hpp"
#include <QResource>

namespace EonTimer::Action {
    static sf::Sound *loadSound(const QString &name) {
        QResource resource(name);
        auto *sound = new sf::Sound();
        auto *buffer = new sf::SoundBuffer();
        buffer->loadFromMemory(resource.data(), static_cast<size_t>(resource.size()));
        sound->setBuffer(*buffer);
        return sound;
    }

    static sf::Sound *getSound(const Sound sound) {
        static const std::map<Sound, sf::Sound *> sounds{{BEEP, loadSound(":/sounds/beep.wav")},
                                                         {DING, loadSound(":/sounds/ding.wav")},
                                                         {TICK, loadSound(":/sounds/tick.wav")},
                                                         {POP, loadSound(":/sounds/pop.wav")}};
        return sounds.find(sound)->second;
    }

    SoundService::SoundService(const Settings *actionSettings, QObject *parent)
        : QObject(parent), actionSettings(actionSettings) {
        currentSound = getSound(actionSettings->getSound());
        connect(actionSettings, &Settings::soundChanged, [this](const Sound newValue) {
            currentSound = getSound(newValue);
        });
    }

    void SoundService::play() {
        const auto mode = actionSettings->getMode();
        if (mode == AUDIO || mode == AV) {
            currentSound->play();
        }
    }
}  // namespace EonTimer::Action
