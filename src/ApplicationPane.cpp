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
                                     Timer::Settings *timerSettings,
                                     Action::Settings *actionSettings,
                                     Timer::TimerService *timerService,
                                     QWidget *parent)
        : QWidget(parent),
          settings(settings),
          timerSettings(timerSettings),
          actionSettings(actionSettings),
          timerService(timerService) {
        const auto *calibrationService = new Util::CalibrationService(timerSettings);

        const auto *secondTimer = new Timer::Factory::SecondTimer();
        const auto *delayTimer = new Timer::Factory::DelayTimer(secondTimer, calibrationService);
        const auto *entralinkTimer = new Timer::Factory::EntralinkTimer(delayTimer);
        const auto *enhancedEntralinkTimer = new Timer::Factory::EnhancedEntralinkTimer(entralinkTimer);

        timerDisplayPane = new Timer::TimerDisplayPane(timerService, actionSettings);
        gen5TimerPane = new Gen5::TimerPane(new Gen5::TimerModel(settings),
                                            delayTimer,
                                            secondTimer,
                                            entralinkTimer,
                                            enhancedEntralinkTimer,
                                            calibrationService);
        gen4TimerPane = new Gen4::TimerPane(new Gen4::TimerModel(settings), delayTimer, calibrationService);
        gen3TimerPane = new Gen3::TimerPane(new Gen3::TimerModel(settings),
                                            new Timer::Factory::FrameTimer(calibrationService),
                                            calibrationService);
        connect(
            gen5TimerPane,
            &Gen5::TimerPane::timerChanged,
            [timerService](const std::vector<std::chrono::milliseconds> &stages) { timerService->setStages(stages); });
        connect(
            gen4TimerPane,
            &Gen4::TimerPane::timerChanged,
            [timerService](const std::vector<std::chrono::milliseconds> &stages) { timerService->setStages(stages); });
        connect(
            gen3TimerPane,
            &Gen3::TimerPane::timerChanged,
            [timerService](const std::vector<std::chrono::milliseconds> &stages) { timerService->setStages(stages); });
        initComponents();
    }

    void ApplicationPane::initComponents() {
        auto *layout = new QVBoxLayout(this);
        layout->setSpacing(10);
        // ----- timerDisplayPane -----
        {
            layout->addWidget(timerDisplayPane);
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
            layout->addWidget(tabPane);
            tabPane->addTab(gen5TimerPane, "5");
            tabPane->addTab(gen4TimerPane, "4");
            tabPane->addTab(gen3TimerPane, "3");
            tabPane->setCurrentIndex(getSelectedTab());
        }
        // ----- btnBar -----
        {
            auto *btnBar = new QWidget();
            auto *btnBarLayout = new QHBoxLayout(btnBar);
            btnBarLayout->setSpacing(10);
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
                btnBarLayout->addWidget(settingsBtn);
            }
            // ----- updateBtn -----
            {
                auto *updateBtn = new QPushButton("Update");
                connect(updateBtn, SIGNAL(clicked(bool)), this, SLOT(onUpdate()));
                connect(timerService, &Timer::TimerService::activated, [updateBtn](const bool activated) {
                    updateBtn->setEnabled(!activated);
                });
                btnBarLayout->addWidget(updateBtn);
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
                btnBarLayout->addWidget(startStopBtn);
                startStopBtn->setDefault(true);
                startStopBtn->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            }
            layout->addWidget(btnBar);
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
