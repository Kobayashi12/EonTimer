//
// Created by Dylan Meadows on 2020-03-09.
//

#pragma once

#include <Action/ActionSettingsModel.h>
#include <Gen3/Gen3TimerModel.h>
#include <Gen4/Gen4TimerModel.h>
#include <Gen5/Gen5TimerModel.h>
#include <Timer/TimerService.h>
#include <Timer/TimerSettingsModel.h>

#include <QMainWindow>
#include <QSettings>

#include "ApplicationPane.h"

namespace EonTimer {
    class ApplicationWindow : public QMainWindow {
        Q_OBJECT
    public:
        explicit ApplicationWindow(QWidget *parent = nullptr);

    private:
        void initComponents();

    protected:
        void closeEvent(QCloseEvent *event) override;

    private:
        QSettings *settings;
        Action::ActionSettingsModel *actionSettings;
        Timer::TimerSettingsModel *timerSettings;
        Gen5::Gen5TimerModel *gen5Timer;
        Gen4::Gen4TimerModel *gen4Timer;
        Gen3::Gen3TimerModel *gen3Timer;
        Timer::TimerService *timerService;
        ApplicationPane *applicationPane;
    };
}  // namespace EonTimer

#pragma once
