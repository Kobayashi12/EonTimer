//
// Created by Dylan Meadows on 2020-03-15.
//

#pragma once

#include <Util/Types.h>

#include <QSettings>
#include <QtGui/QColor>
#include <chrono>

#include "ActionMode.h"
#include "Sound.h"

namespace EonTimer::Action {
    class ActionSettingsModel : public QObject {
        Q_OBJECT
    public:
        explicit ActionSettingsModel(QSettings *settings, QObject *parent = nullptr);
        void sync(QSettings *settings) const;
        [[nodiscard]] ActionMode getMode() const;
        [[nodiscard]] Sound getSound() const;
        [[nodiscard]] const QColor &getColor() const;
        [[nodiscard]] std::chrono::milliseconds getInterval() const;
        [[nodiscard]] u32 getCount() const;

    signals:
        void colorChanged(const QColor &newValue);

    public slots:
        void setMode(int newValue);
        void setMode(EonTimer::Action::ActionMode newValue);
        void setSound(int newValue);
        void setSound(EonTimer::Action::Sound newValue);
        void setColor(const QColor &newValue);
        void setInterval(int newValue);
        void setCount(int newValue);

    private:
        ActionMode mode;
        Sound sound;
        QColor color;
        std::chrono::milliseconds interval;
        u32 count;
    };
}  // namespace EonTimer::Action
