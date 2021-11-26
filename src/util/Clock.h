//
// Created by dmeadows on 5/2/20.
//

#ifndef EONTIMER_CLOCK_H
#define EONTIMER_CLOCK_H

#include <chrono>
#include <map>
#include <string>
#include "Types.h"

namespace util {
    class Clock {
    public:
        Clock();

        Microseconds tick();

        [[nodiscard]] Microseconds sinceInit() const;
        [[nodiscard]] Microseconds sinceInit(const Instant &instant) const;

        [[nodiscard]] Microseconds sinceLastTick() const;
        [[nodiscard]] Microseconds sinceLastTick(const Instant &instant) const;

        void checkpoint(CStr checkpoint);
        [[nodiscard]] Microseconds sinceCheckpoint(CStr checkpoint) const;
        [[nodiscard]] Microseconds sinceCheckpoint(CStr checkpoint, const Instant &instant) const;

    private:
        Instant lastTick;
        const Instant init;
        std::map<CStr, Instant> checkpoints;
    };
}  // namespace util

#endif  // EONTIMER_CLOCK_H
