//
// Created by Dylan Meadows on 2020-03-09.
//

#include "TimerPane.h"
#include "Util/FieldSet.h"
#include "Util/Types.h"
#include "Util/WidgetHelper.h"
#include <QGroupBox>
#include <QLabel>
#include <QVBoxLayout>

namespace EonTimer::Gen4 {
    TimerPane::TimerPane(TimerModel *model,
                         const Timer::Factory::DelayTimer *delayTimer,
                         const Util::CalibrationService *calibrationService,
                         QWidget *parent)
        : QWidget(parent), model(model), delayTimer(delayTimer), calibrationService(calibrationService) {
        initComponents();
    }

    void TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);
        // --- timer fields ---
        {
            // group
            auto *group = new QGroupBox();
            group->setProperty("class", "themeable-panel themeable-border");
            group->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
            auto *groupLayout = new QVBoxLayout(group);
            groupLayout->setContentsMargins(0, 0, 0, 0);
            groupLayout->setSpacing(0);
            rootLayout->addWidget(group);

            // form
            auto *form = new QWidget();
            form->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            groupLayout->addWidget(form, 0, Qt::AlignTop);
            // form layout
            auto *formLayout = new QGridLayout(form);
            formLayout->setSpacing(10);
            // ----- calibratedDelay -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(0, new QLabel("Calibrated Delay"), field);
                Util::setModel(field, INT_MIN, INT_MAX, model->getCalibratedDelay());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, &TimerModel::calibratedDelayChanged, field, &QSpinBox::setValue);
                connect(field, QOverload<int>::of(&QSpinBox::valueChanged), [this](const i32 newValue) {
                    model->setCalibratedDelay(newValue);
                    emit timerChanged(createStages());
                });
                Util::addFieldSet(formLayout, fieldSet);
            }
            // ----- calibratedSecond -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(1, new QLabel("Calibrated Second"), field);
                Util::setModel(field, INT_MIN, INT_MAX, model->getCalibratedSecond());
                field->setValue(model->getCalibratedSecond());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, &TimerModel::calibratedSecondChanged, field, &QSpinBox::setValue);
                connect(field, QOverload<int>::of(&QSpinBox::valueChanged), [this](const i32 newValue) {
                    model->setCalibratedSecond(newValue);
                    emit timerChanged(createStages());
                });
                Util::addFieldSet(formLayout, fieldSet);
            }
            // ----- targetDelay -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(2, new QLabel("Target Delay"), field);
                Util::setModel(field, 0, INT_MAX, model->getTargetDelay());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, &TimerModel::targetDelayChanged, field, &QSpinBox::setValue);
                connect(field, QOverload<int>::of(&QSpinBox::valueChanged), [this](const i32 newValue) {
                    model->setTargetDelay(newValue);
                    emit timerChanged(createStages());
                });
                Util::addFieldSet(formLayout, fieldSet);
            }
            // ----- targetSecond -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(3, new QLabel("Target Second"), field);
                Util::setModel(field, 0, 59, model->getTargetSecond());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, &TimerModel::targetSecondChanged, field, &QSpinBox::setValue);
                connect(field, QOverload<int>::of(&QSpinBox::valueChanged), [this](const i32 newValue) {
                    model->setTargetSecond(newValue);
                    emit timerChanged(createStages());
                });
                Util::addFieldSet(formLayout, fieldSet);
            }
            rootLayout->addWidget(group);
        }
        // ----- calibration fields -----
        {
            auto *layout = new QGridLayout();
            rootLayout->addLayout(layout);
            // ----- delayHit -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(0, new QLabel("Delay Hit"), field);
                field->setRange(0, INT_MAX);
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, &TimerModel::delayHitChanged, field, &QSpinBox::setValue);
                connect(field, QOverload<int>::of(&QSpinBox::valueChanged), model, &TimerModel::setDelayHit);
                Util::addFieldSet(layout, fieldSet);
            }
        }
    }

    std::vector<std::chrono::milliseconds> TimerPane::createStages() const {
        return delayTimer->createStages(model->getTargetDelay(), model->getTargetSecond(), getCalibration());
    }

    void TimerPane::calibrate() {
        model->setCalibratedDelay(
            model->getCalibratedDelay() +
            calibrationService->toDelays(std::chrono::milliseconds(delayTimer->calibrate(model->getTargetDelay(), model->getDelayHit()))));
        model->setDelayHit(0);
    }

    i32 TimerPane::getCalibration() const {
        return calibrationService->createCalibration(model->getCalibratedDelay(), model->getCalibratedSecond());
    }
}  // namespace EonTimer::Gen4
