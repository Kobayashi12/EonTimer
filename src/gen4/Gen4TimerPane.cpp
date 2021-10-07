//
// Created by Dylan Meadows on 2020-03-09.
//

#include "Gen4TimerPane.h"

#include <gui/util/FieldSet.h>
#include <util/Types.h>

#include <QGroupBox>
#include <QLabel>
#include <QVBoxLayout>

namespace EonTimer::Gen4 {
    Gen4TimerPane::Gen4TimerPane(Gen4TimerModel *model,
                                 const EonTimer::timer::DelayTimer *delayTimer,
                                 const EonTimer::CalibrationService *calibrationService,
                                 QWidget *parent)
        : QWidget(parent), model(model), delayTimer(delayTimer), calibrationService(calibrationService) {
        initComponents();
    }

    void Gen4TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::*valueChanged)(i32) = &QSpinBox::valueChanged;
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
                auto fieldSet = gui::util::FieldSet<QSpinBox>(0, new QLabel("Calibrated Delay"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getCalibratedDelay());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, SIGNAL(calibratedDelayChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setCalibratedDelay(value);
                    emit timerChanged(createStages());
                });
                gui::util::addFieldSet(formLayout, fieldSet);
            }
            // ----- calibratedSecond -----
            {
                auto *field = new QSpinBox();
                auto fieldSet = gui::util::FieldSet<QSpinBox>(1, new QLabel("Calibrated Second"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getCalibratedSecond());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, SIGNAL(calibratedSecondChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setCalibratedSecond(value);
                    emit timerChanged(createStages());
                });
                gui::util::addFieldSet(formLayout, fieldSet);
            }
            // ----- targetDelay -----
            {
                auto *field = new QSpinBox();
                auto fieldSet = gui::util::FieldSet<QSpinBox>(2, new QLabel("Target Delay"), field);
                field->setRange(0, INT_MAX);
                field->setValue(model->getTargetDelay());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, SIGNAL(targetDelayChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setTargetDelay(value);
                    emit timerChanged(createStages());
                });
                gui::util::addFieldSet(formLayout, fieldSet);
            }
            // ----- targetSecond -----
            {
                auto *field = new QSpinBox();
                auto fieldSet = gui::util::FieldSet<QSpinBox>(3, new QLabel("Target Second"), field);
                field->setRange(0, 59);
                field->setValue(model->getTargetSecond());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, SIGNAL(targetSecondChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setTargetSecond(value);
                    emit timerChanged(createStages());
                });
                gui::util::addFieldSet(formLayout, fieldSet);
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
                auto fieldSet = gui::util::FieldSet<QSpinBox>(0, new QLabel("Delay Hit"), field);
                field->setRange(0, INT_MAX);
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(field, valueChanged, [this](const i32 value) { model->setDelayHit(value); });
                connect(model, SIGNAL(delayHitChanged(i32)), field, SLOT(setValue(i32)));
                gui::util::addFieldSet(layout, fieldSet);
            }
        }
        createStages();
    }

    void Gen4TimerPane::calibrate() {
        model->setCalibratedDelay(
            model->getCalibratedDelay() +
            calibrationService->toDelays(delayTimer->calibrate(model->getTargetDelay(), model->getDelayHit())));
        model->setDelayHit(0);
    }

    std::shared_ptr<std::vector<i32>> Gen4TimerPane::createStages() {
        return delayTimer->createStages(model->getTargetDelay(), model->getTargetSecond(), getCalibration());
    }

    i32 Gen4TimerPane::getCalibration() const {
        return calibrationService->createCalibration(model->getCalibratedDelay(), model->getCalibratedSecond());
    }
}  // namespace EonTimer::Gen4
