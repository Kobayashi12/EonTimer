//
// Created by Dylan Meadows on 2020-03-15.
//

#pragma once

#include <Action/Settings.h>
#include <Action/SettingsPane.h>
#include <Timer/Settings.h>
#include <Timer/SettingsPane.h>

#include <QDialog>

namespace EonTimer {
    class SettingsDialog : public QDialog {
        Q_OBJECT
    public:
        explicit SettingsDialog(Timer::Settings *timerSettings,
                                Action::Settings *actionSettings,
                                QWidget *parent = nullptr);

    private:
        void initComponents(Timer::Settings *timerSettings, Action::Settings *actionSettings);
    };
}  // namespace EonTimer
