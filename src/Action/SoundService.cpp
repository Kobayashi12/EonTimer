//
// Created by Dylan Meadows on 2020-03-14.
//

#include "SoundService.h"

#include <QResource>
#include <SFML/Audio/SoundBuffer.hpp>

namespace EonTimer::Action {
    sf::Sound *loadSound(const QString &filename) {
        QResource resource(filename);
        auto *sound = new sf::Sound();
        auto *buffer = new sf::SoundBuffer();
        buffer->loadFromMemory(resource.data(), static_cast<size_t>(resource.size()));
        sound->setBuffer(*buffer);
        return sound;
    }

    SoundService::SoundService(const ActionSettingsModel *actionSettings, QObject *parent)
        : QObject(parent), actionSettings(actionSettings) {
        mBeep = loadSound(":/sounds/beep.wav");
        mDing = loadSound(":/sounds/ding.wav");
        mTick = loadSound(":/sounds/tick.wav");
        mPop = loadSound(":/sounds/pop.wav");
    }

    void SoundService::play() {
        const auto mode = actionSettings->getMode();
        if (mode == AUDIO || mode == AV) {
            switch (actionSettings->getSound()) {
                case BEEP:
                    mBeep->play();
                    break;
                case DING:
                    mDing->play();
                    break;
                case TICK:
                    mTick->play();
                    break;
                case POP:
                    mPop->play();
                    break;
            }
        }
    }
}  // namespace EonTimer::Action
