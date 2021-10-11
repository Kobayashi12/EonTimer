//
// Created by Dylan Meadows on 2020-03-10.
//

#pragma once

#include "Clock.h"
#include "Settings.h"
#include "TimerState.h"
#include <Action/Settings.h>
#include <QObject>
#include <QThread>
#include <memory>
#include <vector>

using namespace std::chrono_literals;

namespace EonTimer::Timer {
    class TimerService : public QObject {
        Q_OBJECT
    public:
        explicit TimerService(Settings *timerSettings, Action::Settings *actionSettings, QObject *parent = nullptr);
        ~TimerService() override;
        [[nodiscard]] bool isRunning() const;
        void setStages(const std::vector<std::chrono::milliseconds> &newValue);
        void start();
        void stop();

    private:
        void reset();
        void run(Clock clock);
        [[nodiscard]] std::chrono::microseconds runStage(Clock clock,
                                                         std::chrono::milliseconds period,
                                                         std::chrono::milliseconds actionInterval,
                                                         std::chrono::microseconds preElapsed,
                                                         std::chrono::milliseconds stage);

    signals:
        void activated(bool);
        void actionTriggered();
        void stateChanged(const EonTimer::Timer::TimerState &state);
        void minutesBeforeTargetChanged(const std::chrono::minutes &minutesBeforeTarget);
        void nextStageChanged(const std::chrono::milliseconds &nextStage);

    private:
        Settings *timerSettings;
        Action::Settings *actionSettings;
        std::vector<std::chrono::milliseconds> stages;
        std::chrono::milliseconds totalDuration = 0ms;
        std::atomic<bool> running = false;
        QThread *timerThread = nullptr;
    };
}  // namespace EonTimer::Timer
