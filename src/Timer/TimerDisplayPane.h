//
// Created by Dylan Meadows on 2020-03-10.
//

#pragma once

#include <Timer/TimerService.h>

#include <QGroupBox>
#include <QLabel>
#include <QTimer>

namespace EonTimer::Timer {
    class TimerDisplayPane : public QGroupBox {
        Q_OBJECT
    public:
        TimerDisplayPane(TimerService *newValue,
                         const Action::ActionSettingsModel *actionSettings,
                         QWidget *parent = nullptr);

    private:
        void initComponents();
        void updateCurrentStageLbl();
        void setVisualCue(const QColor &color);
        [[nodiscard]] bool isVisualCueEnabled() const;

    public slots:
        void activate();
        void deactivate();

    private:
        QLabel *currentStage;
        QLabel *minutesBeforeTarget;
        QLabel *nextStage;
        const Action::ActionSettingsModel *actionSettings;
        bool isActive = false;
        QTimer timer;
    };
}  // namespace EonTimer::Timer
