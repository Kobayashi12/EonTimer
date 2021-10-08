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

#include "ActionSettingsModel.h"

namespace EonTimer::Action {
    class ActionSettingsPane : public QWidget {
        Q_OBJECT
    public:
        explicit ActionSettingsPane(ActionSettingsModel *settings, QWidget *parent = nullptr);

        void updateSettings();

    private:
        void initComponents();

    signals:
        void modeChanged(EonTimer::Action::ActionMode newValue);

    public slots:
        void setMode(int newValue);
        void setMode(EonTimer::Action::ActionMode newValue);
        void setSound(int newValue);
        void setSound(EonTimer::Action::Sound newValue);
        void setInterval(int newValue);
        void setCount(int newValue);

    private:
        ActionSettingsModel *settings;
        ActionMode mode;
        Sound sound;
        QColor color;
        u32 interval;
        u32 count;
    };
}  // namespace EonTimer::Action
