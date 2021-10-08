//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerPane.h"

#include <Util/WidgetHelper.h>

#include <QLabel>
#include <QScrollArea>

namespace EonTimer::Gen5 {
    Gen5TimerPane::Gen5TimerPane(Gen5TimerModel *model,
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
        // ----- mode -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);

            auto *field = new QComboBox();
            Util::FieldSet<QComboBox> fieldSet(0, new QLabel("Mode"), field);
            Util::addItems<Gen5TimerMode>(field, getGen5TimerModes(), &getName);
            field->setCurrentText(getName(model->getMode()));
            connect(field, QOverload<i32>::of(&QComboBox::currentIndexChanged), [this](const i32 currentIndex) {
                model->setMode(getGen5TimerModes()[currentIndex]);
                emit timerChanged(createStages());
            });
            Util::addFieldSet(form, fieldSet);
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
                Util::FieldSet<QSpinBox> fieldSet(0, new QLabel("Calibration"), field);
                Util::setModel(field, INT_MIN, INT_MAX, model->getCalibration());
                connect(model, SIGNAL(calibrationChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setCalibration(value);
                    emit timerChanged(createStages());
                });
                Util::addFieldSet(form, fieldSet);
            }
            // ----- targetDelay -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new Util::FieldSet<QSpinBox>(1, new QLabel("Target Delay"), field);
                Util::setModel(field, 0, INT_MAX, model->getTargetDelay());
                connect(model, SIGNAL(targetDelayChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setTargetDelay(value);
                    emit timerChanged(createStages());
                });
                connect(model, &Gen5TimerModel::modeChanged, [form, fieldSet](const Gen5TimerMode value) {
                    Util::setVisible(form, *fieldSet, value != STANDARD);
                });
                Util::addFieldSet(form, *fieldSet);
            }
            // ----- targetSecond -----
            {
                auto *field = new QSpinBox();
                Util::FieldSet<QSpinBox> fieldSet(2, new QLabel("Target Second"), field);
                Util::setModel(field, 0, 59, model->getTargetSecond());
                connect(model, SIGNAL(targetSecondChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const i32 value) {
                    model->setTargetSecond(value);
                    emit timerChanged(createStages());
                });
                Util::addFieldSet(form, fieldSet);
            }
            // ----- entralinkCalibration -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new Util::FieldSet<QSpinBox>(3, new QLabel("Entralink Calibration"), field);
                Util::setModel(field, INT_MIN, INT_MAX, model->getEntralinkCalibration());
                connect(model, SIGNAL(entralinkCalibrationChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const i32 newValue) {
                    model->setEntralinkCalibration(newValue);
                    emit timerChanged(createStages());
                });
                connect(model, &Gen5TimerModel::modeChanged, [form, fieldSet](const Gen5TimerMode value) {
                    Util::setVisible(form, *fieldSet, value == ENTRALINK || value == ENTRALINK_PLUS);
                });
                Util::addFieldSet(form, *fieldSet);
            }
            // ----- frameCalibration -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new Util::FieldSet<QSpinBox>(4, new QLabel("Frame Calibration"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getFrameCalibration());
                connect(model, SIGNAL(frameCalibrationChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const i32 frameCalibration) {
                    model->setFrameCalibration(frameCalibration);
                    emit timerChanged(createStages());
                });
                connect(model, &Gen5TimerModel::modeChanged, [form, fieldSet](const Gen5TimerMode value) {
                    Util::setVisible(form, *fieldSet, value == ENTRALINK_PLUS);
                });
                Util::addFieldSet(form, *fieldSet);
            }
            // ----- targetAdvances -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new Util::FieldSet<QSpinBox>(5, new QLabel("Target Advances"), field);
                field->setRange(0, INT_MAX);
                field->setValue(model->getTargetAdvances());
                connect(model, SIGNAL(targetAdvancesChanged(int)), field, SLOT(setValue(int)));
                connect(fieldSet->field, valueChanged, [this](const i32 value) {
                    model->setTargetAdvances(value);
                    emit timerChanged(createStages());
                });
                connect(model, &Gen5TimerModel::modeChanged, [form, fieldSet](const Gen5TimerMode value) {
                    Util::setVisible(form, *fieldSet, value == ENTRALINK_PLUS);
                });
                Util::addFieldSet(form, *fieldSet);
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
                auto *fieldSet = new Util::FieldSet<QSpinBox>(0, new QLabel("Delay Hit"), field);
                field->setRange(0, INT_MAX);
                field->setSpecialValueText("");
                connect(model, SIGNAL(delayHitChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const i32 value) { model->setDelayHit(value); });
                connect(model, &Gen5TimerModel::modeChanged, [form, fieldSet](const Gen5TimerMode value) {
                    Util::setVisible(form, *fieldSet, value != STANDARD);
                });
                Util::addFieldSet(form, *fieldSet);
            }
            // ----- secondHit -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new Util::FieldSet<QSpinBox>(1, new QLabel("Second Hit"), field);
                field->setRange(0, 59);
                connect(model, SIGNAL(secondHitChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const i32 value) { model->setSecondHit(value); });
                connect(model, &Gen5TimerModel::modeChanged, [form, fieldSet](const Gen5TimerMode value) {
                    Util::setVisible(form, *fieldSet, value != C_GEAR);
                });
                Util::addFieldSet(form, *fieldSet);
            }
            // ----- advancesHit -----
            {
                auto *field = new QSpinBox();
                auto *fieldSet = new Util::FieldSet<QSpinBox>(2, new QLabel("Advances Hit"), field);
                field->setRange(0, INT_MAX);
                connect(model, SIGNAL(advancesHitChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const i32 value) { model->setAdvancesHit(value); });
                connect(model, &Gen5TimerModel::modeChanged, [form, fieldSet](const Gen5TimerMode value) {
                    Util::setVisible(form, *fieldSet, value == ENTRALINK_PLUS);
                });
                Util::addFieldSet(form, *fieldSet);
            }
        }
        // force all fieldsets to update visibility
        emit model->modeChanged(model->getMode());
    }

    std::shared_ptr<std::vector<i32>> Gen5TimerPane::createStages() {
        std::shared_ptr<std::vector<i32>> stages;
        switch (model->getMode()) {
            case STANDARD:
                stages =
                    secondTimer->createStages(model->getTargetSecond(),
                                              calibrationService->calibrateToMilliseconds(model->getCalibration()));
                break;
            case C_GEAR:
                stages = delayTimer->createStages(model->getTargetDelay(),
                                                  model->getTargetSecond(),
                                                  calibrationService->calibrateToMilliseconds(model->getCalibration()));
                break;
            case ENTRALINK:
                stages = entralinkTimer->createStages(
                    model->getTargetDelay(),
                    model->getTargetSecond(),
                    calibrationService->calibrateToMilliseconds(model->getCalibration()),
                    calibrationService->calibrateToMilliseconds(model->getEntralinkCalibration()));
                break;
            case ENTRALINK_PLUS:
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
            case STANDARD:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getSecondCalibration()));
                break;
            case C_GEAR:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getDelayCalibration()));
                break;
            case ENTRALINK:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getSecondCalibration()));
                model->setEntralinkCalibration(model->getEntralinkCalibration() +
                                               calibrationService->calibrateToDelays(getEntralinkCalibration()));
                break;
            case ENTRALINK_PLUS:
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
}  // namespace EonTimer::Gen5
