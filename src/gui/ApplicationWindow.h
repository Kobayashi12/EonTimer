//
// Created by Dylan Meadows on 2020-03-09.
//

#ifndef EONTIMER_APPLICATIONWINDOW_H
#define EONTIMER_APPLICATIONWINDOW_H

#include <models/settings/ActionSettingsModel.h>
#include <models/settings/TimerSettingsModel.h>
#include <models/timers/Gen3TimerModel.h>
#include <models/timers/Gen4TimerModel.h>
#include <models/timers/Gen5TimerModel.h>
#include <services/TimerService.h>

#include <QMainWindow>
#include <QSettings>

#include "ApplicationPane.h"

namespace gui {
    class ApplicationWindow : public QMainWindow {
        Q_OBJECT
    private:
        QSettings *settings;
        EonTimer::settings::ActionSettingsModel *actionSettings;
        EonTimer::settings::TimerSettingsModel *timerSettings;
        EonTimer::timer::Gen5TimerModel *gen5Timer;
        EonTimer::timer::Gen4TimerModel *gen4Timer;
        EonTimer::timer::Gen3TimerModel *gen3Timer;
        service::TimerService *timerService;
        ApplicationPane *applicationPane;

    public:
        explicit ApplicationWindow(QWidget *parent = nullptr);

    private:
        void initComponents();

    protected:
        void closeEvent(QCloseEvent *event) override;
    };
}  // namespace gui

#endif  // EONTIMER_APPLICATIONWINDOW_H
