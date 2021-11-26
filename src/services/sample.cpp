//
// Created by Dylan Meadows on 6/2/21.
//
#include "io_eontimer_ChronoEngineNatives.h"
#include "protocol.pb.h"
#include "Clock.h"

#include <io/eontimer/ChronoEngine.h>
#include <io/eontimer/OnUpdate.h>

#include <thread>

using namespace std::chrono;
using namespace std::literals::chrono_literals;
using namespace io::eontimer;

microseconds runStage(jobject $this,
                      Clock *clock,
                      const microseconds refreshInterval,
                      const microseconds updateInterval,
                      const microseconds stage,
                      jobject $onUpdate) {
    // set up variables
    const OnUpdate onUpdate($onUpdate);
    const auto period = refreshInterval;
    // get elapsed time since last tick
    auto elapsed = clock->tick();
    while (ChronoEngine::getRunning($this)) {
        // update elapsed with delta time
        elapsed += clock->tick();
        // sleep for configured duration
        std::this_thread::sleep_for(period);
        // update elapsed with delta time
        elapsed += clock->tick();

        // if can trigger $onUpdate
        if ($onUpdate != nullptr) {
            // if should trigger $onUpdate
            if (clock->sinceCheckpoint("update") >= updateInterval) {
                clock->checkpoint("update");
                auto remainingMicros = duration_cast<microseconds>(stage - elapsed);
                onUpdate.invoke(stage.count(), remainingMicros.count());
            }
        }
        if (elapsed >= stage) break;
    }
    return elapsed;
}

[[maybe_unused]] JNIEXPORT jlong JNICALL Java_io_eontimer_n8tive_NativeChronoEngine_run(JNIEnv *env,
                                                                                        jclass,
                                                                                        jobject $this,
                                                                                        jlong clockPtr,
                                                                                        jobject buffer,
                                                                                        jobject onUpdate) {
    // cast clockPtr to actual Clock*
    auto *clock = (Clock *) clockPtr;
    // create checkpoint for "update"
    clock->checkpoint("update");
    // parse config
    EngineConfig cfg;
    cfg.ParseFromArray(
        env->GetDirectBufferAddress(buffer),
        static_cast<int>(env->GetDirectBufferCapacity(buffer))
    );
    // initialize required variables
    auto elapsed = 0us;
    uint8_t stageIndex = 0;
    const auto stagesSize = cfg.stages_size();
    const auto refreshInterval = microseconds(cfg.refreshinterval() * 1000);
    const auto updateInterval = microseconds(cfg.updateinterval() * 1000);
    // run stages while engine is running
    while (ChronoEngine::getRunning($this)) {
        const auto currentStage = microseconds(cfg.stages(stageIndex) * 1000);
        elapsed += runStage($this, clock, refreshInterval, updateInterval, currentStage, onUpdate);
        if (stageIndex + 1 >= stagesSize) break;
        stageIndex++;
    }
    // return total time elapsed
    return elapsed.count();
}
