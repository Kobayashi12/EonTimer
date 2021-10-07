//
// Created by Dylan Meadows on 2020-03-14.
//

#include "SoundService.h"

#include <QResource>
#include <SFML/Audio/SoundBuffer.hpp>

namespace EonTimer {

    static sf::Sound *loadSound(const char *filename);

    SoundService::SoundService(const EonTimer::settings::ActionSettingsModel *actionSettings, QObject *parent)
        : QObject(parent), actionSettings(actionSettings) {
        mBeep = loadSound(":/getSounds/beep.wav");
        mDing = loadSound(":/getSounds/ding.wav");
        mTick = loadSound(":/getSounds/tick.wav");
        mPop = loadSound(":/getSounds/pop.wav");
    }

    SoundService::~SoundService() {
        delete mBeep;
    }

    void SoundService::play() {
        const auto mode = actionSettings->getMode();
        if (mode == EonTimer::ActionMode::AUDIO || mode == EonTimer::ActionMode::AV) {
            switch (actionSettings->getSound()) {
                case EonTimer::Sound::BEEP:
                    mBeep->play();
                    break;
                case EonTimer::Sound::DING:
                    mDing->play();
                    break;
                case EonTimer::Sound::TICK:
                    mTick->play();
                    break;
                case EonTimer::Sound::POP:
                    mPop->play();
                    break;
            }
        }
    }

    sf::Sound *loadSound(const char *filename) {
        QResource resource(filename);
        auto *sound = new sf::Sound();
        auto *buffer = new sf::SoundBuffer();
        buffer->loadFromMemory(resource.data(), static_cast<size_t>(resource.size()));
        sound->setBuffer(*buffer);
        return sound;
    }
}  // namespace EonTimer
