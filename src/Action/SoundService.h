//
// Created by Dylan Meadows on 2020-03-14.
//

#pragma once

#include "SFML/Audio/Sound.hpp"
#include "Settings.h"
#include <QObject>

namespace EonTimer::Action {
    class SoundService : public QObject {
        Q_OBJECT
    public:
        explicit SoundService(const Settings *actionSettings, QObject *parent = nullptr);

    public slots:
        void play();

    private:
        const Settings *actionSettings;
        sf::Sound *currentSound;
    };
}  // namespace EonTimer::Action
