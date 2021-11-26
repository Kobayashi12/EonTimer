//
// Created by Dylan Meadows on 2020-03-14.
//

#ifndef EONTIMER_SOUNDSERVICE_H
#define EONTIMER_SOUNDSERVICE_H

#include <models/settings/ActionSettingsModel.h>

#include <QObject>
#include <QSoundEffect>

namespace service {
    class SoundService : public QObject {
        Q_OBJECT
    private:
        std::map<model::Sound, QSoundEffect *> soundEffects;
        const model::settings::ActionSettingsModel *actionSettings;

    public:
        explicit SoundService(const model::settings::ActionSettingsModel *actionSettings, QObject *parent = nullptr);

    private:
        QSoundEffect *loadSound(const char *filename);
        // @formatter:off
    public slots:
        void play();
        // @formatter:on
    };
}  // namespace service

#endif  // EONTIMER_SOUNDSERVICE_H
