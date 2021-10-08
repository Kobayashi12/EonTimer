//
// Created by Dylan Meadows on 2020-03-16.
//

#include "TimerSettingsPane.h"

#include <Util/WidgetHelper.h>

#include <QFormLayout>
#include <QLabel>

namespace EonTimer::Timer {
    TimerSettingsPane::TimerSettingsPane(TimerSettingsModel *model, QWidget *parent) : QWidget(parent), model(model) {
        refreshInterval = new QSpinBox();
        precisionCalibrationEnabled = new QCheckBox();
        initComponents();
    }

    void TimerSettingsPane::initComponents() {
        auto *layout = new QFormLayout(this);
        layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
        // ----- console -----
        {
            console = new QComboBox();
            layout->addRow("Console", console);
            Util::addItems<Console>(console, getConsoles(), &getName);
            console->setCurrentText(getName(model->getConsole()));
            console->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        }
        // ----- refreshInterval -----
        {
            layout->addRow("Refresh Interval", refreshInterval);
            Util::setModel(refreshInterval, 1, 1000, model->getRefreshInterval().count());
            refreshInterval->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        }
        // ----- precisionCalibrationEnabled -----
        {
            layout->addRow("Precision Calibration", precisionCalibrationEnabled);
            precisionCalibrationEnabled->setChecked(model->isPrecisionCalibrationEnabled());
            precisionCalibrationEnabled->setTristate(false);
        }
    }

    void TimerSettingsPane::updateSettings() {
        model->setConsole(getConsoles()[console->currentIndex()]);
        model->setRefreshInterval(std::chrono::milliseconds(refreshInterval->value()));
        model->setPrecisionCalibrationEnabled(precisionCalibrationEnabled->isChecked());
    }
}  // namespace EonTimer::Timer
