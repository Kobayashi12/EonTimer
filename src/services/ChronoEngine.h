//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERSERVICE_H
#define EONTIMER_TIMERSERVICE_H

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
    class TimerService : public QObject {
        Q_OBJECT
    private:
        bool running;
        QThread *timerThread;
        std::shared_ptr<std::vector<Microseconds>> stages;
        model::settings::TimerSettingsModel *timerSettings;
        model::settings::ActionSettingsModel *actionSettings;

    public:
        explicit TimerService(model::settings::TimerSettingsModel *timerSettings,
                              model::settings::ActionSettingsModel *actionSettings,
                              QObject *parent = nullptr);

        ~TimerService() override;

        [[nodiscard]] bool isRunning() const;

        void setStages(std::shared_ptr<std::vector<Microseconds>> stages);

        void start();

        void stop();

    private:
        void reset();

        void run(util::Clock &clock);

        Microseconds runStage(util::Clock &clock,
                              const Microseconds &stage);

        // @formatter:off
    signals:
        void activated(bool);
        void actionTriggered();
        void stateChanged(const Microseconds &elapsed, const Microseconds &stage);
        void minutesBeforeTargetChanged(const Minutes &minutesBeforeTarget);
        void nextStageChanged(const Microseconds &nextStage);
        // @formatter:on
    };
}  // namespace service

#endif  // EONTIMER_TIMERSERVICE_H
