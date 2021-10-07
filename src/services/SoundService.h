//
// Created by Dylan Meadows on 2020-03-14.
//

#ifndef EONTIMER_SOUNDSERVICE_H
#define EONTIMER_SOUNDSERVICE_H

#include <models/settings/ActionSettingsModel.h>

#include <QObject>
#include <SFML/Audio/Sound.hpp>

namespace EonTimer {
    class SoundService : public QObject {
        Q_OBJECT
    private:
        const EonTimer::settings::ActionSettingsModel *actionSettings;
        sf::Sound *mBeep;
        sf::Sound *mDing;
        sf::Sound *mTick;
        sf::Sound *mPop;

    public:
        explicit SoundService(const EonTimer::settings::ActionSettingsModel *actionSettings, QObject *parent = nullptr);

        ~SoundService();

        // @formatter:off
    public slots:
        void play();
        // @formatter:on
    };
}  // namespace EonTimer

#endif  // EONTIMER_SOUNDSERVICE_H
