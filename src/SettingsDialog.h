//
// Created by Dylan Meadows on 2020-03-15.
//

#pragma once

#include <Action/ActionSettingsModel.h>
#include <Action/ActionSettingsPane.h>
#include <Timer/TimerSettingsModel.h>
#include <Timer/TimerSettingsPane.h>

#include <QDialog>

namespace EonTimer {
    class SettingsDialog : public QDialog {
        Q_OBJECT
    public:
        explicit SettingsDialog(Timer::TimerSettingsModel *timerSettings,
                                Action::ActionSettingsModel *actionSettings,
                                QWidget *parent = nullptr);

    private:
        void initComponents();

    private:
        Timer::TimerSettingsModel *timerSettings;
        Action::ActionSettingsModel *actionSettings;
        Timer::TimerSettingsPane *timerSettingsPane;
        Action::ActionSettingsPane *actionSettingsPane;
    };
}  // namespace EonTimer
