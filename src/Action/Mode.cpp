//
// Created by dmeadows on 5/18/20.
//

#include "Mode.h"

namespace EonTimer::Action {
    const std::vector<Mode> &getActionModes() {
        static const std::vector<Mode> values{AUDIO, VISUAL, AV};
        return values;
    }

    const QString &getName(Mode mode) {
        static const std::vector<QString> names{"Audio", "Visual", "A/V"};
        return names[mode];
    }
}  // namespace EonTimer
