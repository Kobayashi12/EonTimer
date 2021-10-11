//
// Created by Dylan Meadows on 2020-03-09.
//

#pragma once

#include <Action/Settings.h>
#include <Gen3/TimerModel.h>
#include <Gen4/TimerModel.h>
#include <Gen5/TimerModel.h>
#include <Timer/Settings.h>
#include <Timer/TimerService.h>

#include <QMainWindow>
#include <QSettings>

#include "ApplicationPane.h"

namespace EonTimer {
    class ApplicationWindow : public QMainWindow {
        Q_OBJECT
    public:
        explicit ApplicationWindow(QWidget *parent = nullptr);

    signals:
        void onClose();

    private:
        void initComponents();

    protected:
        void closeEvent(QCloseEvent *event) override;
    };
}  // namespace EonTimer
