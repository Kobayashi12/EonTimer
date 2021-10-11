//
// Created by Dylan Meadows on 2020-03-10.
//

#pragma once

#include <Action/Settings.h>
#include <Gen3/TimerPane.h>
#include <Gen4/TimerPane.h>
#include <Gen5/TimerPane.h>
#include <Timer/Settings.h>
#include <Timer/TimerService.h>

#include <QSettings>
#include <QWidget>

#include "Timer/TimerDisplayPane.h"

namespace EonTimer {
    class ApplicationPane : public QWidget {
        Q_OBJECT
    public:
        ApplicationPane(QSettings *settings,
                        Action::Settings *actionSettings,
                        Timer::Settings *timerSettings,
                        Gen5::TimerModel *gen5Timer,
                        Gen4::TimerModel *gen4Timer,
                        Gen3::TimerModel *gen3Timer,
                        Timer::TimerService *timerService,
                        QWidget *parent = nullptr);

    private:
        void initComponents();
        uint getSelectedTab() const;
        void setSelectedTab(uint timerType);
        void updateTimer();

    private slots:
        void onUpdate();

    private:
        QSettings *settings;
        Action::Settings *actionSettings;
        Timer::Settings *timerSettings;
        Timer::TimerService *timerService;
        Timer::TimerDisplayPane *timerDisplayPane;
        Gen5::TimerPane *gen5TimerPane;
        Gen4::TimerPane *gen4TimerPane;
        Gen3::TimerPane *gen3TimerPane;
    };
}  // namespace EonTimer
