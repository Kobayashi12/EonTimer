//
// Created by dmeadows on 5/18/20.
//

#include "ActionMode.h"

namespace EonTimer::Action {
    const std::vector<ActionMode> &getActionModes() {
        static const std::vector<ActionMode> values{AUDIO, VISUAL, AV};
        return values;
    }

    const QString &getName(ActionMode mode) {
        static const std::vector<QString> names{"Audio", "Visual", "A/V"};
        return names[mode];
    }
}  // namespace EonTimer
