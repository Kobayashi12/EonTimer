//
// Created by Dylan Meadows on 2020-03-10.
//

#include "ApplicationPane.h"
#include "SettingsDialog.h"
#include <QFontDatabase>
#include <QPushButton>
#include <utility>

namespace EonTimer {
    // TODO
    const u32 GEN5 = 0;
    const u32 GEN4 = 1;
    const u32 GEN3 = 2;
    const u32 CUSTOM = 3;
    namespace Fields {
        const char *SELECTED_TAB = "selectedTab";
    }

    ApplicationPane::ApplicationPane(QSettings *settings,
                                     Action::Settings *actionSettings,
                                     Timer::Settings *timerSettings,
                                     Gen5::TimerModel *gen5Timer,
                                     Gen4::TimerModel *gen4Timer,
                                     Gen3::TimerModel *gen3Timer,
                                     Timer::TimerService *timerService,
                                     QWidget *parent)
        : QWidget(parent),
          settings(settings),
          actionSettings(actionSettings),
          timerSettings(timerSettings),
          timerService(timerService) {
        const auto *calibrationService = new Util::CalibrationService(timerSettings);
        const auto *secondTimer = new Timer::Factory::SecondTimer();
        const auto *frameTimer = new Timer::Factory::FrameTimer(calibrationService);
        const auto *delayTimer = new Timer::Factory::DelayTimer(secondTimer, calibrationService);
        const auto *entralinkTimer = new Timer::Factory::EntralinkTimer(delayTimer);
        const auto *enhancedEntralinkTimer = new Timer::Factory::EnhancedEntralinkTimer(entralinkTimer);

        timerDisplayPane = new Timer::TimerDisplayPane(timerService, actionSettings);
        gen5TimerPane = new Gen5::TimerPane(gen5Timer,
                                            delayTimer,
                                            secondTimer,
                                            entralinkTimer,
                                            enhancedEntralinkTimer,
                                            calibrationService);
        gen4TimerPane = new Gen4::TimerPane(gen4Timer, delayTimer, calibrationService);
        gen3TimerPane = new Gen3::TimerPane(gen3Timer, frameTimer, calibrationService);
        connect(
            gen5TimerPane,
            &Gen5::TimerPane::timerChanged,
            [timerService](const std::vector<std::chrono::milliseconds> &stages) { timerService->setStages(stages); });
        connect(gen4TimerPane,
                &Gen4::TimerPane::timerChanged,
                [timerService](std::vector<std::chrono::milliseconds> stages) {
                    timerService->setStages(std::move(stages));
                });
        connect(gen3TimerPane,
                &Gen3::TimerPane::timerChanged,
                [timerService](std::vector<std::chrono::milliseconds> stages) {
                    timerService->setStages(std::move(stages));
                });
        initComponents();
    }

    void ApplicationPane::initComponents() {
        auto *layout = new QGridLayout(this);
        layout->setColumnMinimumWidth(0, 215);
        layout->setHorizontalSpacing(10);
        layout->setVerticalSpacing(10);
        // ----- timerDisplayPane -----
        {
            layout->addWidget(timerDisplayPane, 0, 0);
            timerDisplayPane->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        }
        // ----- tabPane -----
        {
            auto *tabPane = new QTabWidget();
            tabPane->setProperty("class", "themeable-panel themeable-border");
            connect(timerService, &Timer::TimerService::activated, [tabPane](const bool activated) {
                tabPane->setEnabled(!activated);
            });
            connect(tabPane, &QTabWidget::currentChanged, [this](const i32 index) { setSelectedTab((u32) index); });
            layout->addWidget(tabPane, 0, 1, 2, 2);
            tabPane->addTab(gen5TimerPane, "5");
            tabPane->addTab(gen4TimerPane, "4");
            tabPane->addTab(gen3TimerPane, "3");
            tabPane->setCurrentIndex(getSelectedTab());
        }
        // ----- settingsBtn -----
        {
            auto *settingsBtn = new QPushButton();
            settingsBtn->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
            const auto id = QFontDatabase::addApplicationFont(":/fonts/FontAwesome.ttf");
            const auto family = QFontDatabase::applicationFontFamilies(id)[0];
            settingsBtn->setFont(QFont(family));
            settingsBtn->setText(QChar(0xf013));
            connect(timerService, &Timer::TimerService::activated, [settingsBtn](const bool activated) {
                settingsBtn->setEnabled(!activated);
            });
            connect(settingsBtn, &QPushButton::clicked, [this] {
                SettingsDialog settingsDialog(timerSettings, actionSettings, this);
                if (settingsDialog.exec() == QDialog::Accepted) {
                    updateTimer();
                }
            });
            layout->addWidget(settingsBtn, 2, 0);
        }
        // ----- updateBtn -----
        {
            auto *updateBtn = new QPushButton("Update");
            connect(updateBtn, SIGNAL(clicked(bool)), this, SLOT(onUpdate()));
            connect(timerService, &Timer::TimerService::activated, [updateBtn](const bool activated) {
                updateBtn->setEnabled(!activated);
            });
            layout->addWidget(updateBtn, 2, 1);
            updateBtn->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        }
        // ----- startStopBtn -----
        {
            auto *startStopBtn = new QPushButton("Start");
            connect(timerService, &Timer::TimerService::activated, [startStopBtn](const bool activated) {
                startStopBtn->setText(activated ? "Stop" : "Start");
            });
            connect(startStopBtn, &QPushButton::clicked, [this] {
                if (!timerService->isRunning()) {
                    timerService->start();
                } else {
                    timerService->stop();
                }
            });
            layout->addWidget(startStopBtn, 2, 2);
            startStopBtn->setDefault(true);
            startStopBtn->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        }
    }

    void ApplicationPane::updateTimer() {
        std::vector<std::chrono::milliseconds> stages;
        switch (getSelectedTab()) {
            case GEN5:
                stages = gen5TimerPane->createStages();
                break;
            case GEN4:
                stages = gen4TimerPane->createStages();
                break;
            case GEN3:
                stages = gen3TimerPane->createStages();
                break;
            case CUSTOM:
                stages = std::vector<std::chrono::milliseconds>();
                stages.emplace_back(10s);
                break;
        }
        timerService->setStages(stages);
    }

    void ApplicationPane::onUpdate() {
        switch (getSelectedTab()) {
            case GEN5:
                gen5TimerPane->calibrate();
                break;
            case GEN4:
                gen4TimerPane->calibrate();
                break;
            case GEN3:
                gen3TimerPane->calibrate();
                break;
        }
    }

    u32 ApplicationPane::getSelectedTab() const { return settings->value(Fields::SELECTED_TAB, GEN5).toUInt(); }

    void ApplicationPane::setSelectedTab(u32 selectedTab) {
        settings->setValue(Fields::SELECTED_TAB, selectedTab);
        updateTimer();
    }
}  // namespace EonTimer
