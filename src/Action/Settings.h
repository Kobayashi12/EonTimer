//
// Created by Dylan Meadows on 2020-03-15.
//

#pragma once

#include <Util/Types.h>

#include <QSettings>
#include <QtGui/QColor>
#include <chrono>

#include "Mode.h"
#include "Sound.h"

namespace EonTimer::Action {
    class Settings : public QObject {
        Q_OBJECT
    public:
        explicit Settings(QSettings *settings);
        [[nodiscard]] Mode getMode() const;
        [[nodiscard]] Sound getSound() const;
        [[nodiscard]] QColor getColor() const;
        [[nodiscard]] std::chrono::milliseconds getInterval() const;
        [[nodiscard]] u32 getCount() const;

        void setMode(Mode newValue);
        void setSound(Sound newValue);

    signals:
        void colorChanged(const QColor &newValue);
    public slots:
        void setMode(int newValue);
        void setSound(int newValue);
        void setColor(const QColor &newValue);
        void setInterval(int newValue);
        void setCount(int newValue);

    private:
        QSettings *settings;
    };
}  // namespace EonTimer::Action
