//
// Created by dmeadows on 5/18/20.
//

#include "ActionMode.h"

#include <string>

namespace EonTimer {
    const std::vector<ActionMode> &getActionModes() {
        static const std::vector<ActionMode> values{AUDIO, VISUAL, AV};
        return values;
    }

    const std::string &getName(ActionMode mode) {
        static const std::string names[ActionMode::COUNT] = {"Audio", "Visual", "A/V"};
        return names[mode];
    }
}  // namespace EonTimer
