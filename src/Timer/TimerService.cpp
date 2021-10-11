//
// Created by Dylan Meadows on 2020-03-10.
//

#include "TimerService.h"
#include "Action/SoundService.h"
#include "Timer/Clock.h"
#include "Util/Functions.h"
#include <QThreadPool>
#include <iostream>
#include <numeric>
#include <stack>
#include <utility>

namespace EonTimer::Timer {
    TimerService::TimerService(Settings *timerSettings, Action::Settings *actionSettings, QObject *parent)
        : QObject(parent), running(false), timerSettings(timerSettings), actionSettings(actionSettings) {
        auto *sounds = new Action::SoundService(actionSettings, this);
        connect(this, &TimerService::actionTriggered, [sounds] { sounds->play(); });
    }

    TimerService::~TimerService() {
        if (running) {
            running = false;
            timerThread->quit();
            timerThread->wait();
        }
    }

    void TimerService::setStages(const std::vector<std::chrono::milliseconds> &newValue) {
        if (!running) {
            stages = newValue;
            totalDuration = std::accumulate(stages.begin(), stages.end(), 0ms);
            reset();
        }
    }

    void TimerService::start() {
        if (!running) {
            Clock clock;
            running = true;
            timerThread = QThread::create([this, clock] { run(clock); });
            timerThread->start();
            emit activated(true);
        }
    }

    void TimerService::stop() {
        if (running) {
            running = false;
            timerThread->quit();
            timerThread->wait();
            emit activated(false);
            reset();
        }
    }

    void TimerService::reset() {
        const auto currentStage = stages[0];
        emit stateChanged(TimerState(currentStage, currentStage));
        emit minutesBeforeTargetChanged(std::chrono::duration_cast<std::chrono::minutes>(totalDuration));
        emit nextStageChanged(stages.size() >= 2 ? stages[1] : 0ms);
    }

    void TimerService::run(Clock clock) {
        Clock runClock;
        const auto period = timerSettings->getRefreshInterval();
        const auto actionInterval = actionSettings->getInterval();

        u8 stageIndex = 0;
        auto preElapsed = 0us;
        while (running && stageIndex < stages.size()) {
            auto currentStage = stages[stageIndex];
            preElapsed = runStage(clock, period, actionInterval, preElapsed, currentStage) - currentStage;
            stageIndex++;
        }

        const auto tick = runClock.tick();
        std::cout << totalDuration.count() << std::endl;
        std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(tick).count() << std::endl;
        running = false;
        emit activated(false);
        reset();
    }

    std::chrono::microseconds TimerService::runStage(Clock clock,
                                                     const std::chrono::milliseconds period,
                                                     const std::chrono::milliseconds actionInterval,
                                                     const std::chrono::microseconds preElapsed,
                                                     const std::chrono::milliseconds stage) {
        std::stack<std::chrono::milliseconds> actionStack;
        for (u32 i = 0; i < actionSettings->getCount(); i++) {
            auto currentAction = actionInterval * i;
            if (currentAction < stage) actionStack.push(currentAction);
        }

        auto ticks = 0;
        auto elapsed = preElapsed;
        auto nextAction = actionStack.top();
        while (running && elapsed < stage) {
            const auto delta = clock.tick();
            const auto remaining = stage - elapsed - delta;
            if (remaining <= nextAction) {
                emit actionTriggered();
                actionStack.pop();
                if (!actionStack.empty()) {
                    nextAction = actionStack.top();
                }
            }

            if (ticks++ % 4 == 0)
                emit stateChanged(TimerState(stage, std::chrono::duration_cast<std::chrono::milliseconds>(remaining)));
            elapsed += delta;

            if (elapsed < stage) {
                auto adjustedPeriod = period;
                const auto remainingUntilAction = stage - elapsed - nextAction;
                if (remainingUntilAction < adjustedPeriod)
                    adjustedPeriod = std::chrono::duration_cast<std::chrono::milliseconds>(remainingUntilAction);
                std::this_thread::sleep_for(adjustedPeriod);
            }
        }
        return elapsed - stage;
    }

    bool TimerService::isRunning() const { return running; }
}  // namespace EonTimer::Timer