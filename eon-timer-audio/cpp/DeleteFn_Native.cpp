//
// Created by dmeadows on 9/28/21.
//

#include "DeleteFn_Native.h"

namespace DeleteFn::Native {
    void delete$eon_timer_audio(JNIEnv *, jclass, jlong rawAddress, jlong pointer) {
        ((void (*)(jlong)) rawAddress)(pointer);
    }
}
