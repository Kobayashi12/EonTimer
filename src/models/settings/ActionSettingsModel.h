//
// Created by Dylan Meadows on 2020-03-15.
//

#ifndef EONTIMER_ACTIONSETTINGSMODEL_H
#define EONTIMER_ACTIONSETTINGSMODEL_H

#include <models/ActionMode.h>
#include <models/Sound.h>

#include <QSettings>
#include <QtGui/QColor>
#include <chrono>

namespace EonTimer::settings {
    class ActionSettingsModel : public QObject {
        Q_OBJECT
    private:
        EonTimer::ActionMode mode;
        EonTimer::Sound sound;
        QColor color;
        unsigned int interval;
        unsigned int count;

    public:
        explicit ActionSettingsModel(QSettings *settings);

        void sync(QSettings *settings) const;

        EonTimer::ActionMode getMode() const;

        void setMode(EonTimer::ActionMode mode);

        EonTimer::Sound getSound() const;

        void setSound(EonTimer::Sound sound);

        const QColor &getColor() const;

        void setColor(const QColor &color);

        unsigned int getInterval() const;

        void setInterval(unsigned int interval);

        unsigned getCount() const;

        void setCount(unsigned int count);

    signals:
        void colorChanged(const QColor &color);
    };
}  // namespace EonTimer::settings

#endif  // EONTIMER_ACTIONSETTINGSMODEL_H
