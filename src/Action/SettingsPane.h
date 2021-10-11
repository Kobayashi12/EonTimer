//
// Created by Dylan Meadows on 2020-03-16.
//

#pragma once

#include <Util/Types.h>

#include <QColor>
#include <QComboBox>
#include <QPushButton>
#include <QSpinBox>
#include <QWidget>

#include "Settings.h"

namespace EonTimer::Action {
    class SettingsPane : public QWidget {
        Q_OBJECT
    public:
        explicit SettingsPane(Settings *settings, QWidget *parent = nullptr);

        void updateSettings();

    private:
        void initComponents();

    signals:
        void modeChanged(EonTimer::Action::Mode newValue);

    public slots:
        void setMode(int newValue);
        void setMode(EonTimer::Action::Mode newValue);
        void setSound(int newValue);
        void setSound(EonTimer::Action::Sound newValue);
        void setInterval(int newValue);
        void setCount(int newValue);

    private:
        Settings *settings;
        Mode mode;
        Sound sound;
        QColor color;
        u32 interval;
        u32 count;
    };
}  // namespace EonTimer::Action
