//
// Created by Dylan Meadows on 2020-03-16.
//

#pragma once

#include <QCheckBox>
#include <QComboBox>
#include <QSpinBox>
#include <QWidget>

#include "Settings.h"

namespace EonTimer::Timer {
    class SettingsPane : public QWidget {
        Q_OBJECT
    public:
        explicit SettingsPane(Settings *model, QWidget *parent = nullptr);
        void updateSettings();

    private:
        void initComponents();

    private:
        Settings *model;
        QComboBox *console;
        QCheckBox *precisionCalibrationEnabled;
        QSpinBox *refreshInterval;
    };
}  // namespace EonTimer::Timer
