//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerPane.h"

#include <QLabel>
#include <QScrollArea>

namespace EonTimer::Gen5 {
    Gen5TimerPane::Gen5TimerPane(EonTimer::Gen5::Gen5TimerModel *model,
                                 const EonTimer::timer::DelayTimer *delayTimer,
                                 const EonTimer::timer::SecondTimer *secondTimer,
                                 const EonTimer::timer::EntralinkTimer *entralinkTimer,
                                 const EonTimer::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer,
                                 const EonTimer::CalibrationService *calibrationService,
                                 QWidget *parent)
        : QWidget(parent),
          model(model),
          delayTimer(delayTimer),
          secondTimer(secondTimer),
          entralinkTimer(entralinkTimer),
          enhancedEntralinkTimer(enhancedEntralinkTimer),
          calibrationService(calibrationService) {
        initComponents();
    }

    void Gen5TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::*valueChanged)(i32) = &QSpinBox::valueChanged;
        void (*setVisible)(QGridLayout *, gui::util::FieldSet<QSpinBox> &, const bool) = gui::util::setVisible;
        // ----- mode -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);

            gui::util::FieldSet<QComboBox> mode(0, new QLabel("Mode"), new QComboBox());
            for (const auto currentMode : EonTimer::Gen5::getGen5TimerModes()) {
                mode.field->addItem(EonTimer::Gen5::getName(currentMode), currentMode);
            }
            mode.field->setCurrentText(EonTimer::getName(model->getMode()));
            connect(mode.field, QOverload<i32>::of(&QComboBox::currentIndexChanged), [this](const i32 currentIndex) {
                model->setMode(EonTimer::Gen5::getGen5TimerModes()[currentIndex]);
                emit timerChanged(createStages());
            });
            gui::util::addFieldSet(form, mode);
        }
        // ----- timer fields -----
        {
            auto *scrollPane = new QWidget();
            scrollPane->setProperty("class", "themeable-panel");
            scrollPane->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
            auto *scrollPaneLayout = new QVBoxLayout(scrollPane);
            scrollPaneLayout->setContentsMargins(0, 0, 0, 0);
            scrollPaneLayout->setSpacing(10);
            auto *scrollArea = new QScrollArea();
            scrollArea->setFrameShape(QFrame::NoFrame);
            scrollArea->setProperty("class", "themeable-panel themeable-border");
            scrollArea->setVerticalScrollBarPolicy(Qt::ScrollBarPolicy::ScrollBarAsNeeded);
            scrollArea->setHorizontalScrollBarPolicy(Qt::ScrollBarPolicy::ScrollBarAsNeeded);
            scrollArea->setWidgetResizable(true);
            scrollArea->setWidget(scrollPane);
            rootLayout->addWidget(scrollArea);

            auto *formWidget = new QWidget();
            formWidget->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            scrollPaneLayout->addWidget(formWidget, 0, Qt::AlignTop);
            auto *form = new QGridLayout(formWidget);
            form->setSpacing(10);
            // ----- calibration -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new gui::util::FieldSet<QSpinBox>(0, new QLabel("Calibration"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getCalibration());
                connect(model, SIGNAL(calibrationChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setCalibration(value);
                    emit timerChanged(createStages());
                });
                gui::util::addFieldSet(form, *fieldSet);
            }
            // ----- targetDelay -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new gui::util::FieldSet<QSpinBox>(1, new QLabel("Target Delay"), field);
                field->setRange(0, INT_MAX);
                field->setValue(model->getTargetDelay());
                connect(model, SIGNAL(targetDelayChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setTargetDelay(value);
                    emit timerChanged(createStages());
                });
                connect(model,
                        &EonTimer::Gen5::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const EonTimer::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value != EonTimer::Gen5TimerMode::STANDARD);
                        });
                gui::util::addFieldSet(form, *fieldSet);
            }
            // ----- targetSecond -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new gui::util::FieldSet<QSpinBox>(2, new QLabel("Target Second"), field);
                field->setRange(0, 59);
                field->setValue(model->getTargetSecond());
                connect(model, SIGNAL(targetSecondChanged(i32)), field, SLOT(setValue(i32)));
                connect(fieldSet->field, valueChanged, [this](const i32 value) {
                    model->setTargetSecond(value);
                    emit timerChanged(createStages());
                });
                gui::util::addFieldSet(form, *fieldSet);
            }
            // ----- entralinkCalibration -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new gui::util::FieldSet<QSpinBox>(3, new QLabel("Entralink Calibration"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getEntralinkCalibration());
                connect(model, SIGNAL(entralinkCalibrationChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setEntralinkCalibration(value);
                    emit timerChanged(createStages());
                });
                connect(model,
                        &EonTimer::Gen5::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const EonTimer::Gen5TimerMode value) {
                            setVisible(form,
                                       *fieldSet,
                                       value == EonTimer::Gen5TimerMode::ENTRALINK ||
                                           value == EonTimer::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                gui::util::addFieldSet(form, *fieldSet);
            }
            // ----- frameCalibration -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new gui::util::FieldSet<QSpinBox>(4, new QLabel("Frame Calibration"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getFrameCalibration());
                connect(model, SIGNAL(frameCalibrationChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 frameCalibration) {
                    model->setFrameCalibration(frameCalibration);
                    emit timerChanged(createStages());
                });
                connect(model,
                        &EonTimer::Gen5::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const EonTimer::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value == EonTimer::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                gui::util::addFieldSet(form, *fieldSet);
            }
            // ----- targetAdvances -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new gui::util::FieldSet<QSpinBox>(5, new QLabel("Target Advances"), field);
                field->setRange(0, INT_MAX);
                field->setValue(model->getTargetAdvances());
                connect(model, SIGNAL(targetAdvancesChanged(i32)), field, SLOT(setValue(i32)));
                connect(fieldSet->field, valueChanged, [this](const i32 value) {
                    model->setTargetAdvances(value);
                    emit timerChanged(createStages());
                });
                connect(model,
                        &EonTimer::Gen5::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const EonTimer::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value == EonTimer::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                gui::util::addFieldSet(form, *fieldSet);
            }
        }
        // ----- calibration fields -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);
            form->setSpacing(10);
            // ----- delayHit -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new gui::util::FieldSet<QSpinBox>(0, new QLabel("Delay Hit"), field);
                field->setRange(0, INT_MAX);
                field->setSpecialValueText("");
                connect(model, SIGNAL(delayHitChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) { model->setDelayHit(value); });
                connect(model,
                        &EonTimer::Gen5::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const EonTimer::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value != EonTimer::Gen5TimerMode::STANDARD);
                        });
                gui::util::addFieldSet(form, *fieldSet);
            }
            // ----- secondHit -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new gui::util::FieldSet<QSpinBox>(1, new QLabel("Second Hit"), field);
                field->setRange(0, 59);
                connect(model, SIGNAL(secondHitChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) { model->setSecondHit(value); });
                connect(model,
                        &EonTimer::Gen5::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const EonTimer::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value != EonTimer::Gen5TimerMode::C_GEAR);
                        });
                gui::util::addFieldSet(form, *fieldSet);
            }
            // ----- advancesHit -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new gui::util::FieldSet<QSpinBox>(2, new QLabel("Advances Hit"), field);
                field->setRange(0, INT_MAX);
                connect(model, SIGNAL(advancesHitChanged(i32)), field, SLOT(setValue(i32)));
                connect(field, valueChanged, [this](const i32 value) { model->setAdvancesHit(value); });
                connect(model,
                        &EonTimer::Gen5::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const EonTimer::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value == EonTimer::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                gui::util::addFieldSet(form, *fieldSet);
            }
        }
        // force all fieldsets to update visibility
        emit model->modeChanged(model->getMode());
    }

    std::shared_ptr<std::vector<i32>> Gen5TimerPane::createStages() {
        std::shared_ptr<std::vector<i32>> stages;
        switch (model->getMode()) {
            case EonTimer::Gen5TimerMode::STANDARD:
                stages = secondTimer->createStages(model->getTargetSecond(),
                                              calibrationService->calibrateToMilliseconds(model->getCalibration()));
                break;
            case EonTimer::Gen5TimerMode::C_GEAR:
                stages = delayTimer->createStages(model->getTargetDelay(),
                                                  model->getTargetSecond(),
                                                  calibrationService->calibrateToMilliseconds(model->getCalibration()));
                break;
            case EonTimer::Gen5TimerMode::ENTRALINK:
                stages = entralinkTimer->createStages(
                    model->getTargetDelay(),
                    model->getTargetSecond(),
                    calibrationService->calibrateToMilliseconds(model->getCalibration()),
                    calibrationService->calibrateToMilliseconds(model->getEntralinkCalibration()));
                break;
            case EonTimer::Gen5TimerMode::ENTRALINK_PLUS:
                stages = enhancedEntralinkTimer->createStages(
                    model->getTargetDelay(),
                    model->getTargetSecond(),
                    model->getTargetAdvances(),
                    calibrationService->calibrateToMilliseconds(model->getCalibration()),
                    calibrationService->calibrateToMilliseconds(model->getEntralinkCalibration()),
                    model->getFrameCalibration());
                break;
        }
        return stages;
    }

    void Gen5TimerPane::calibrate() {
        switch (model->getMode()) {
            case EonTimer::Gen5TimerMode::STANDARD:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getSecondCalibration()));
                break;
            case EonTimer::Gen5TimerMode::C_GEAR:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getDelayCalibration()));
                break;
            case EonTimer::Gen5TimerMode::ENTRALINK:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getSecondCalibration()));
                model->setEntralinkCalibration(model->getEntralinkCalibration() +
                                               calibrationService->calibrateToDelays(getEntralinkCalibration()));
                break;
            case EonTimer::Gen5TimerMode::ENTRALINK_PLUS:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getSecondCalibration()));
                model->setEntralinkCalibration(model->getEntralinkCalibration() +
                                               calibrationService->calibrateToDelays(getEntralinkCalibration()));
                model->setFrameCalibration(model->getFrameCalibration() + getAdvancesCalibration());
                break;
        }
        model->setDelayHit(0);
        model->setSecondHit(0);
        model->setAdvancesHit(0);
    }

    i32 Gen5TimerPane::getDelayCalibration() const {
        return delayTimer->calibrate(model->getTargetDelay(), model->getDelayHit());
    }

    i32 Gen5TimerPane::getSecondCalibration() const {
        return secondTimer->calibrate(model->getTargetSecond(), model->getSecondHit());
    }

    i32 Gen5TimerPane::getEntralinkCalibration() const {
        return entralinkTimer->calibrate(model->getTargetDelay(), model->getDelayHit() - getSecondCalibration());
    }

    i32 Gen5TimerPane::getAdvancesCalibration() const {
        return enhancedEntralinkTimer->calibrate(model->getTargetAdvances(), model->getAdvancesHit());
    }
}  // namespace gui::timer
