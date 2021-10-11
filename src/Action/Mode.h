//
// Created by dmeadows on 5/18/20.
//

#pragma once

#include <QString>
#include <string>
#include <vector>

namespace EonTimer::Action {
    enum Mode { AUDIO, VISUAL, AV };

    const std::vector<Mode> &getActionModes();

    const QString &getName(Mode mode);
}  // namespace EonTimer
