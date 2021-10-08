//
// Created by Dylan Meadows on 2020-03-10.
//

#pragma once

#include <Action/ActionSettingsModel.h>

#include <QObject>
#include <QThread>
#include <memory>
#include <vector>

#include "Clock.h"
#include "TimerSettingsModel.h"
#include "TimerState.h"

namespace EonTimer::Timer {
    class TimerService : public QObject {
        Q_OBJECT
    public:
        explicit TimerService(TimerSettingsModel *timerSettings,
                              Action::ActionSettingsModel *actionSettings,
                              QObject *parent = nullptr);

        ~TimerService() override;

        void setStages(const std::shared_ptr<std::vector<i32>>& newValue);

        [[nodiscard]] bool isRunning() const;

        void start();

        void stop();

    private:
        void reset();

        void run(Clock clock);

        [[nodiscard]] std::chrono::microseconds runStage(Clock clock,
                                                         std::chrono::microseconds period,
                                                         std::chrono::microseconds actionInterval,
                                                         std::chrono::microseconds preElapsed,
                                                         std::chrono::microseconds stage);

    signals:
        void activated(bool);
        void actionTriggered();
        void stateChanged(const EonTimer::Timer::TimerState &state);
        void minutesBeforeTargetChanged(const std::chrono::minutes &minutesBeforeTarget);
        void nextStageChanged(const std::chrono::milliseconds &nextStage);

    private:
        TimerSettingsModel *timerSettings;
        Action::ActionSettingsModel *actionSettings;
        std::vector<std::chrono::microseconds> stages;
        QThread *timerThread = nullptr;
        bool running = false;
    };
}  // namespace EonTimer::Timer
