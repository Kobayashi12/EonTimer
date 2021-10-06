//
// Created by Dylan Meadows on 2020-03-16.
//

#include "Sound.h"

#include <string>

namespace EonTimer {
    const std::string &getName(Sound sound) {
        static const std::string names[COUNT] = {"Beep", "Ding", "Tick", "Pop"};
        return names[sound];
    }

    const std::vector<Sound> &getSounds() {
        static const std::vector<Sound> values{BEEP, DING, TICK, POP};
        return values;
    }
}  // namespace EonTimer
