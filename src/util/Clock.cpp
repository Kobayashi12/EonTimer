//
// Created by dmeadows on 5/2/20.
//

#include "Clock.h"

namespace util {
    Clock::Clock() : init(NOW) { lastTick = init; }

    Microseconds Clock::tick() {
        const auto now = NOW;
        const auto elapsed = ELAPSED(Microseconds, lastTick, now);
        lastTick = now;
        return elapsed;
    }

    Microseconds Clock::sinceInit() const { return sinceInit(NOW); }
    Microseconds Clock::sinceInit(const Instant& instant) const { return ELAPSED(Microseconds, init, instant); }

    Microseconds Clock::sinceLastTick() const { return sinceLastTick(NOW); }
    Microseconds Clock::sinceLastTick(const Instant& instant) const { return ELAPSED(Microseconds, lastTick, instant); }

    void Clock::checkpoint(CStr checkpoint) { checkpoints[checkpoint] = NOW; }
    Microseconds Clock::sinceCheckpoint(CStr checkpoint) const { return sinceCheckpoint(checkpoint, NOW); }
    Microseconds Clock::sinceCheckpoint(CStr checkpoint, const Instant& instant) const {
        return ELAPSED(Microseconds, checkpoints.find(checkpoint)->second, instant);
    }
}  // namespace util
