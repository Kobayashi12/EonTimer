//
// Created by Dylan Meadows on 2020-03-10.
//

#pragma once

#include <Action/ActionSettingsModel.h>
#include <Gen3/Gen3TimerPane.h>
#include <Gen4/Gen4TimerPane.h>
#include <Gen5/Gen5TimerPane.h>
#include <Timer/TimerService.h>
#include <Timer/TimerSettingsModel.h>

#include <QSettings>
#include <QWidget>

#include "Timer/TimerDisplayPane.h"

namespace EonTimer {
    class ApplicationPane : public QWidget {
        Q_OBJECT
    private:
        QSettings *settings;
        Action::ActionSettingsModel *actionSettings;
        Timer::TimerSettingsModel *timerSettings;
        Timer::TimerService *timerService;
        Timer::TimerDisplayPane *timerDisplayPane;
        Gen5::Gen5TimerPane *gen5TimerPane;
        Gen4::Gen4TimerPane *gen4TimerPane;
        Gen3::Gen3TimerPane *gen3TimerPane;

    public:
        ApplicationPane(QSettings *settings,
                        Action::ActionSettingsModel *actionSettings,
                        Timer::TimerSettingsModel *timerSettings,
                        Gen5::Gen5TimerModel *gen5Timer,
                        Gen4::Gen4TimerModel *gen4Timer,
                        Gen3::Gen3TimerModel *gen3Timer,
                        Timer::TimerService *timerService,
                        QWidget *parent = nullptr);

    private:
        void initComponents();

        uint getSelectedTab() const;

        void setSelectedTab(uint timerType);

        void updateTimer();

    private slots:
        void onUpdate();
    };
}  // namespace EonTimer
