//
// Created by Dylan Meadows on 2020-03-16.
//

#pragma once

#include <QCheckBox>
#include <QComboBox>
#include <QSpinBox>
#include <QWidget>

#include "TimerSettingsModel.h"

namespace EonTimer::Timer {
    class TimerSettingsPane : public QWidget {
        Q_OBJECT
    public:
        explicit TimerSettingsPane(TimerSettingsModel *model, QWidget *parent = nullptr);
        void updateSettings();

    private:
        void initComponents();

    private:
        TimerSettingsModel *model;
        QComboBox *console;
        QCheckBox *precisionCalibrationEnabled;
        QSpinBox *refreshInterval;
    };
}  // namespace EonTimer::Timer
