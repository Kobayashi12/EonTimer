//
// Created by dmeadows on 5/18/20.
//

#ifndef EONTIMER_ACTIONMODE_H
#define EONTIMER_ACTIONMODE_H

#include <string>
#include <vector>

namespace EonTimer {
    enum ActionMode { AUDIO, VISUAL, AV, COUNT };

    const std::vector<ActionMode> &getActionModes();

    const std::string &getName(ActionMode mode);
}  // namespace EonTimer

#endif  // EONTIMER_ACTIONMODE_H
