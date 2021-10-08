//
// Created by Dylan Meadows on 2020-03-14.
//

#pragma once

#include <QObject>
#include <SFML/Audio/Sound.hpp>

#include "ActionSettingsModel.h"

namespace EonTimer::Action {
    class SoundService : public QObject {
        Q_OBJECT
    public:
        explicit SoundService(const ActionSettingsModel *actionSettings, QObject *parent = nullptr);

    public slots:
        void play();

    private:
        const ActionSettingsModel *actionSettings;
        sf::Sound *mBeep;
        sf::Sound *mDing;
        sf::Sound *mTick;
        sf::Sound *mPop;
    };
}  // namespace EonTimer::Action
