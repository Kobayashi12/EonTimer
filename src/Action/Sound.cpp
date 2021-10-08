//
// Created by Dylan Meadows on 2020-03-16.
//

#include "Sound.h"

namespace EonTimer::Action {
    const std::vector<Sound> &getSounds() {
        static const std::vector<Sound> values{BEEP, DING, TICK, POP};
        return values;
    }

    const QString &getName(const Sound sound) {
        static const std::vector<QString> names{"Beep", "Ding", "Tick", "Pop"};
        return names[sound];
    }
}  // namespace EonTimer
