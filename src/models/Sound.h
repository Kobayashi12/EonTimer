//
// Created by Dylan Meadows on 2020-03-14.
//

#ifndef EONTIMER_SOUND_H
#define EONTIMER_SOUND_H

#include <QtGlobal>
#include <string>
#include <vector>

namespace EonTimer {
    enum Sound { BEEP, DING, POP, TICK, COUNT };

    const std::string &getName(Sound sound);

    const std::vector<Sound> &getSounds();
}  // namespace EonTimer

#endif  // EONTIMER_SOUND_H
