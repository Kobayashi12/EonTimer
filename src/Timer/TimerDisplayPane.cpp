//
// Created by Dylan Meadows on 2020-03-10.
//

#include "TimerDisplayPane.h"

#include <QFontDatabase>
#include <QGroupBox>
#include <QStyle>
#include <QVBoxLayout>
#include <iostream>

namespace EonTimer::Timer {
    QString formatTime(const std::chrono::milliseconds milliseconds) {
        const auto ms = milliseconds.count();
        if (ms > 0) {
            return QString::number(ms / 1000) + ":" + QString::number(ms % 1000).rightJustified(3, '0');
        } else if (ms < 0) {
            return "?:???";
        } else {
            return "0:000";
        }
    }

    TimerDisplayPane::TimerDisplayPane(TimerService *timerService,
                                       const Action::Settings *actionSettings,
                                       QWidget *parent)
        : QGroupBox(parent), actionSettings(actionSettings) {
        currentStage = new QLabel("0:000");
        minutesBeforeTarget = new QLabel("0");
        nextStage = new QLabel("0:000");

        setVisualCue(actionSettings->getColor());
        connect(actionSettings, &Action::Settings::colorChanged, this, &TimerDisplayPane::setVisualCue);
        connect(timerService, &TimerService::stateChanged, [this](const TimerState &state) {
            currentStage->setText(formatTime(state.remaining));
        });
        connect(timerService, &TimerService::minutesBeforeTargetChanged, [this](const std::chrono::minutes newValue) {
            this->minutesBeforeTarget->setText(QString::number(newValue.count()));
        });
        connect(timerService, &TimerService::nextStageChanged, [this](const std::chrono::milliseconds newValue) {
            this->nextStage->setText(formatTime(newValue));
        });
        connect(timerService, &TimerService::actionTriggered, this, &TimerDisplayPane::activate);
        connect(&timer, &QTimer::timeout, this, &TimerDisplayPane::deactivate);
        initComponents();
    }

    void TimerDisplayPane::initComponents() {
        // --- group ---
        {
            setProperty("class", "themeable-panel themeable-border");
            auto *rootLayout = new QVBoxLayout(this);
            rootLayout->setSpacing(5);
            // ----- currentStage ----
            {
                rootLayout->addWidget(currentStage);
                rootLayout->setAlignment(currentStage, Qt::AlignLeft);
                const int font = QFontDatabase::addApplicationFont(":/fonts/RobotoMono-Regular.ttf");
                const QString family = QFontDatabase::applicationFontFamilies(font)[0];
                currentStage->setObjectName("currentStageLbl");
                currentStage->setFont(QFont(family, 36));
            }
            // ----- minutesBeforeTarget -----
            {
                auto *layout = new QHBoxLayout();
                layout->setSpacing(5);
                rootLayout->addLayout(layout);
                rootLayout->setAlignment(layout, Qt::AlignLeft);
                layout->addWidget(new QLabel("Minutes Before Target:"));
                layout->addWidget(minutesBeforeTarget);
            }
            // ----- nextStage -----
            {
                auto *layout = new QHBoxLayout();
                layout->setSpacing(5);
                rootLayout->addLayout(layout);
                rootLayout->setAlignment(layout, Qt::AlignLeft);
                layout->addWidget(new QLabel("Next Stage:"));
                layout->addWidget(nextStage);
            }
        }
    }

    void TimerDisplayPane::updateCurrentStageLbl() {
        currentStage->setProperty("active", isActive);
        auto *style = currentStage->style();
        style->unpolish(currentStage);
        style->polish(currentStage);
    }

    void TimerDisplayPane::setVisualCue(const QColor &color) {
        const QString style = "#currentStageLbl[active=\"true\"]{ background-color: rgb(%1, %2, %3); }";
        currentStage->setStyleSheet(style.arg(color.red()).arg(color.green()).arg(color.blue()));
    }

    bool TimerDisplayPane::isVisualCueEnabled() const {
        const auto mode = actionSettings->getMode();
        return mode == Action::VISUAL || mode == Action::AV;
    }

    void TimerDisplayPane::activate() {
        if (isVisualCueEnabled() && !isActive) {
            isActive = true;
            updateCurrentStageLbl();
            timer.start(75);
        }
    }

    void TimerDisplayPane::deactivate() {
        if (isVisualCueEnabled() && isActive) {
            isActive = false;
            updateCurrentStageLbl();
        }
    }
}  // namespace EonTimer::Timer
