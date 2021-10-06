//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_CONSOLE_H
#define EONTIMER_CONSOLE_H

#include <util/Types.h>
#include <vector>
#include <string>

namespace EonTimer {
    enum Console { GBA, NDS, NDS_GBA, DSI, _3DS, COUNT };

    const std::vector<Console> &getConsoles();

    const std::string &getName(Console console);

    double getFrameRate(Console console);
}  // namespace EonTimer

#endif  // EONTIMER_CONSOLE_H
