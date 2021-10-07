//
// Created by Dylan Meadows on 2020-03-26.
//

#include "Gen3TimerPane.h"

#include <gui/util/FieldSet.h>

#include <QGroupBox>
#include <QLabel>
#include <QVBoxLayout>

namespace EonTimer::Gen3 {
    Gen3TimerPane::Gen3TimerPane(Gen3TimerModel *model,
                                 const EonTimer::timer::FrameTimer *frameTimer,
                                 const EonTimer::CalibrationService *calibrationService,
                                 QWidget *parent)
        : QWidget(parent), model(model), frameTimer(frameTimer), calibrationService(calibrationService) {
        initComponents();
    }

    void Gen3TimerPane::initComponents() {
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
                auto fieldSet = gui::util::FieldSet<QSpinBox>(0, new QLabel("Calibration"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getCalibration());
                field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(model, SIGNAL(calibrationChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 newValue) {
                    model->setCalibration(newValue);
                    emit timerChanged(createStages());
                });
                gui::util::addFieldSet(formLayout, fieldSet);
            }
            // ----- preTimer -----
            {
                auto *field = new QSpinBox();
                auto fieldSet = gui::util::FieldSet<QSpinBox>(1, new QLabel("Pre-Gen3TimerModel"), field);
                field->setRange(0, INT_MAX);
                field->setValue(model->getPreTimer());
                connect(model, SIGNAL(preTimerChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 newValue) {
                    model->setPreTimer(newValue);
                    emit timerChanged(createStages());
                });
                gui::util::addFieldSet(formLayout, fieldSet);
            }
            // ----- targetFrame -----
            {
                auto *field = new QSpinBox();
                auto fieldSet = gui::util::FieldSet<QSpinBox>(2, new QLabel("Target Frame"), field);
                field->setRange(0, INT_MAX);
                field->setValue(model->getTargetFrame());
                connect(model, SIGNAL(targetFrameChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 newValue) {
                    model->setTargetFrame(newValue);
                    emit timerChanged(createStages());
                });
                gui::util::addFieldSet(formLayout, fieldSet);
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
                auto fieldSet = gui::util::FieldSet<QSpinBox>(0, new QLabel("Frame Hit"), field);
                field->setRange(0, INT_MAX);
                connect(model, SIGNAL(frameHitChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) { model->setFrameHit(value); });
                gui::util::addFieldSet(form, fieldSet);
            }
        }
    }

    std::shared_ptr<std::vector<i32>> Gen3TimerPane::createStages() {
        return frameTimer->createStages(model->getPreTimer(), model->getTargetFrame(), model->getCalibration());
    }

    void Gen3TimerPane::calibrate() {
        model->setCalibration(model->getCalibration() + getCalibration());
        model->setFrameHit(0);
    }

    i32 Gen3TimerPane::getCalibration() const {
        return frameTimer->calibrate(model->getTargetFrame(), model->getFrameHit());
    }
}  // namespace gui::timer
