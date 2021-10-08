//
// Created by Dylan Meadows on 2020-03-10.
//

#pragma once

#include <Util/Types.h>

#include <QString>
#include <string>
#include <vector>

namespace EonTimer::Timer {
    enum Console { GBA, NDS, NDS_GBA, DSI, _3DS };

    const std::vector<Console> &getConsoles();

    const QString &getName(Console console);

    double getFrameRate(Console console);
}  // namespace EonTimer
