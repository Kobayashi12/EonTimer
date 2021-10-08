//
// Created by dmeadows on 5/18/20.
//

#pragma once

#include <QString>
#include <string>
#include <vector>

namespace EonTimer::Action {
    enum ActionMode { AUDIO, VISUAL, AV };

    const std::vector<ActionMode> &getActionModes();

    const QString &getName(ActionMode mode);
}  // namespace EonTimer
