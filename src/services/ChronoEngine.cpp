//
// Created by Dylan Meadows on 2020-03-10.
//

#include "ChronoEngine.h"

#include <util/Clock.h>
#include <util/Functions.h>

#include <QThreadPool>
#include <stack>
#include <utility>

#include "SoundService.h"

using namespace std::literals::chrono_literals;

constexpr CStr UPDATE = "update";

namespace service {
    ChronoEngine::ChronoEngine(QObject *parent) : QObject(parent),
                                                  running(false),
                                                  timerThread(nullptr) {
        /*auto *sounds = new SoundService(actionSettings, this);
        connect(this, &ChronoEngine::actionTriggered, [sounds] { sounds->play(); });*/
    }

    ChronoEngine::~ChronoEngine() {
        if (running) {
            timerThread->quit();
            timerThread->wait();
            delete timerThread;
        }
    }

    void ChronoEngine::run() {
        if (!running) {
            running = true;
            util::Clock clock;
            timerThread = QThread::create(&ChronoEngine::runStages, this, clock);
            timerThread->start();
            emit activated(true);
        }
    }

    void ChronoEngine::stop() {
        if (running) {
            running = false;
            timerThread->quit();
            timerThread->wait();
            delete timerThread;
            timerThread = nullptr;
            emit activated(false);
        }
    }

    //    void ChronoEngine::reset() {
    //        auto totalTime = 0us;
    //        for (Microseconds stage : (*stages)) {
    //            totalTime += stage;
    //        }
    //        const auto currentStage = (*stages)[0];
    //        emit stateChanged(0us, currentStage);
    //        emit minutesBeforeTargetChanged(MINUTES(totalTime));
    //        emit nextStageChanged(stages->size() >= 2 ? MILLIS((*stages)[1]) : 0ms);
    //    }

    void ChronoEngine::runStages(std::shared_ptr<std::vector<Microseconds>> stages, util::Clock &clock) {
        // create checkpoint for "update"
        clock.checkpoint(UPDATE);
        // initialize required variables
        auto elapsed = 0us;
        uint8_t stageIndex = 0;
        const auto stagesSize = stages->size();
        const auto refreshInterval = timerSettings->getRefreshInterval();
        // run stages while engine is running
        while (running) {
            auto currentStage = (*stages)[stageIndex];
            elapsed += runStage(currentStage, clock);
            if (stageIndex + 1 >= stagesSize) break;
            stageIndex++;
        }
        stop();
    }

    Microseconds ChronoEngine::runStage(const Microseconds &stage, util::Clock &clock) {
        // set up variables
        const auto period = MICROS(timerSettings->getRefreshInterval());
        // set up scheduled actions
        std::stack<Microseconds> actionStack;
        const auto actionInterval = actionSettings->getInterval();
        for (uint i = 0; i < actionSettings->getCount(); i++) {
            actionStack.push(actionInterval * i);
        }
        auto nextAction = actionStack.top();
        // get elapsed time since last tick
        auto elapsed = clock.tick();
        while (running && elapsed < stage) {
            // update elapsed with delta time
            elapsed += clock.tick();
            // calculate adjustedPeriod based on nextAction
            const auto remainingUntilAction = stage - elapsed - nextAction;
            const auto adjustedPeriod = remainingUntilAction < period ? remainingUntilAction : period;
            // sleep for configured duration
            std::this_thread::sleep_for(adjustedPeriod);
            // update elapsed with delta time
            elapsed += clock.tick();
            // calculate time remaining for current stage
            const auto remaining = stage - elapsed;

            // if can emit actionTriggered
            if (remaining <= nextAction) {
                emit actionTriggered();
                // remove current action
                actionStack.pop();
                // get next action if stack is not empty
                if (!actionStack.empty()) {
                    nextAction = actionStack.top();
                }
            }

            // if can emit stateChanged
            if (clock.sinceCheckpoint(UPDATE) >= 8ms) {
                emit stateChanged(elapsed, stage);
                // update the UPDATE checkpoint
                clock.checkpoint(UPDATE);
            }
            if (elapsed >= stage) break;
        }
        return elapsed;
    }

    bool ChronoEngine::isRunning() const { return timerThread != nullptr; }
}  // namespace service