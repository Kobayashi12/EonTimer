//
// Created by Dylan Meadows on 2020-03-10.
//

#include "TimerService.h"

#include <Timer/Clock.h>
#include <Util/Functions.h>

#include <QThreadPool>
#include <stack>
#include <utility>

#include "Action/SoundService.h"

using namespace std::literals::chrono_literals;

namespace EonTimer::Timer {
    TimerService::TimerService(TimerSettingsModel *timerSettings,
                               Action::ActionSettingsModel *actionSettings,
                               QObject *parent)
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

    void TimerService::setStages(const std::shared_ptr<std::vector<i32>> &newValue) {
        if (!running) {
            stages.clear();
            for (auto stage : *newValue) {
                stages.push_back(
                    std::chrono::duration_cast<std::chrono::microseconds>(std::chrono::milliseconds(stage)));
            }
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
        auto totalTime = 0ms;
        for (auto stage : stages) {
            totalTime += std::chrono::duration_cast<std::chrono::milliseconds>(stage);
        }
        const auto currentStage = std::chrono::duration_cast<std::chrono::milliseconds>(stages[0]);
        emit stateChanged(TimerState(currentStage, currentStage));
        emit minutesBeforeTargetChanged(std::chrono::duration_cast<std::chrono::minutes>(totalTime));
        emit nextStageChanged(stages.size() >= 2 ? std::chrono::duration_cast<std::chrono::milliseconds>(stages[1])
                                                 : 0ms);
    }

    void TimerService::run(Clock clock) {
        const auto period = std::chrono::duration_cast<std::chrono::microseconds>(timerSettings->getRefreshInterval());
        const auto actionInterval = std::chrono::duration_cast<std::chrono::microseconds>(actionSettings->getInterval());

        u8 stageIndex = 0;
        auto preElapsed = 0us;
        while (running && stageIndex < stages.size()) {
            auto currentStage = stages[stageIndex];
            preElapsed = runStage(clock, period, actionInterval, preElapsed, currentStage) - currentStage;
            stageIndex++;
        }

        running = false;
        emit activated(false);
        reset();
    }

    std::chrono::microseconds TimerService::runStage(Clock clock,
                                                     const std::chrono::microseconds period,
                                                     const std::chrono::microseconds actionInterval,
                                                     const std::chrono::microseconds preElapsed,
                                                     const std::chrono::microseconds stage) {
        std::stack<std::chrono::microseconds> actionStack;
        for (u32 i = 0; i < actionSettings->getCount(); i++) {
            actionStack.push(actionInterval * i);
        }

        auto ticks = 0;
        auto elapsed = preElapsed;
        auto nextAction = actionStack.top();
        while (running && elapsed < stage) {
            auto adjustedPeriod = period;
            const auto remainingUntilAction = stage - elapsed - nextAction;
            if (remainingUntilAction < adjustedPeriod) adjustedPeriod = remainingUntilAction;
            std::this_thread::sleep_for(adjustedPeriod);

            const auto delta = clock.tick();
            const auto remaining = stage - elapsed - delta;
            if (remaining <= nextAction) {
                emit actionTriggered();
                actionStack.pop();
                if (!actionStack.empty()) {
                    nextAction = actionStack.top();
                }
            }
            if (ticks % 4 == 0)
                emit stateChanged(TimerState(std::chrono::duration_cast<std::chrono::milliseconds>(stage),
                                             std::chrono::duration_cast<std::chrono::milliseconds>(remaining)));
            elapsed += delta;
            ticks++;
        }
        return elapsed;
    }

    bool TimerService::isRunning() const { return running; }
}  // namespace EonTimer::Timer