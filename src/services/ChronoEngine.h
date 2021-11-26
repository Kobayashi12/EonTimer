//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_CHRONOENGINE_H
#define EONTIMER_CHRONOENGINE_H

#include <models/TimerState.h>
#include <models/settings/ActionSettingsModel.h>
#include <models/settings/TimerSettingsModel.h>
#include <util/Clock.h>
#include <util/Types.h>

#include <QObject>
#include <QThread>
#include <memory>
#include <vector>

namespace service {
    class ChronoEngine : public QObject {
        Q_OBJECT
    private:
        bool running;
        QThread *timerThread;

    public:
        explicit ChronoEngine(QObject *parent = nullptr);

        ~ChronoEngine() override;

        void run();

        void stop();

        [[nodiscard]] bool isRunning() const;

    private:
        void runStages(std::shared_ptr<std::vector<Microseconds>> stages, util::Clock &clock);

        Microseconds runStage(const Microseconds &stage, util::Clock &clock);

        // @formatter:off
    signals:
        void activated(bool);
        void actionTriggered();
        void stateChanged(const Microseconds &elapsed, const Microseconds &stage);
        void minutesBeforeTargetChanged(const Minutes &minutesBeforeTarget);
        void nextStageChanged(const Microseconds &nextStage);
        // @formatter:on

    private:
        class ChronoThread : public QThread {

        };
    };
}  // namespace service

#endif  // EONTIMER_CHRONOENGINE_H
