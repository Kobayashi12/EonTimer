//
// Created by Dylan Meadows on 2020-03-26.
//

#include "TimerPane.h"
#include "Util/FieldSet.h"
#include "Util/WidgetHelper.h"
#include <QGroupBox>
#include <QLabel>
#include <QVBoxLayout>
#include <Util/Functions.h>

namespace EonTimer::Gen3 {
    TimerPane::TimerPane(TimerModel *model,
                         const Timer::Factory::FrameTimer *frameTimer,
                         const Util::CalibrationService *calibrationService,
                         QWidget *parent)
        : QWidget(parent), model(model), frameTimer(frameTimer), calibrationService(calibrationService) {
        initComponents();
    }

    void TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::*valueChanged)(i32) = &QSpinBox::valueChanged;
        // ----- timer fields -----
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
            // ----- calibration -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(0, new QLabel("Calibration"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getCalibration());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, &TimerModel::calibrationChanged, field, &QSpinBox::setValue);
                connect(field, valueChanged, [this](const i32 newValue) {
                    model->setCalibration(newValue);
                    emit timerChanged(createStages());
                });
                Util::addFieldSet(formLayout, fieldSet);
            }
            // ----- preTimer -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(1, new QLabel("Pre-Timer"), field);
                Util::setModel(field, 0, INT_MAX, model->getPreTimer().count());

                connect(model, &TimerModel::preTimerChanged, [field](const std::chrono::milliseconds ms) {
                    field->setValue(ms.count());
                });
                connect(field, valueChanged, [this](const i32 newValue) {
                    model->setPreTimer(newValue);
                    emit timerChanged(createStages());
                });
                Util::addFieldSet(formLayout, fieldSet);
            }
            // ----- targetFrame -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(2, new QLabel("Target Frame"), field);
                Util::setModel(field, 0, INT_MAX, model->getTargetFrame());
                connect(model, &TimerModel::targetFrameChanged, field, &QSpinBox::setValue);
                connect(field, valueChanged, [this](const i32 newValue) {
                    model->setTargetFrame(newValue);
                    emit timerChanged(createStages());
                });
                Util::addFieldSet(formLayout, fieldSet);
            }
        }
        // ----- calibration fields -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);
            form->setSpacing(10);
            // ----- frameHit -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(0, new QLabel("Frame Hit"), field);
                field->setRange(0, INT_MAX);
                connect(model, &TimerModel::frameHitChanged, field, &QSpinBox::setValue);
                connect(field, valueChanged, model, &TimerModel::setFrameHit);
                Util::addFieldSet(form, fieldSet);
            }
        }
    }

    std::vector<std::chrono::milliseconds> TimerPane::createStages() const {
        return frameTimer->createStages(model->getPreTimer(), model->getTargetFrame(), model->getCalibration());
    }

    void TimerPane::calibrate() {
        model->setCalibration(model->getCalibration() + getCalibration());
        model->setFrameHit(0);
    }

    i32 TimerPane::getCalibration() const {
        return frameTimer->calibrate(model->getTargetFrame(), model->getFrameHit());
    }
}  // namespace EonTimer::Gen3
