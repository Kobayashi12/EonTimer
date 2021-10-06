//
// Created by dmeadows on 9/28/21.
//

#include "Sound_Native.h"
#include "DeleteFn_Native.h"

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *) {
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_10) != JNI_OK) {
        return -1;
    }
    Sound::Native::registerNativeFunctions(env);
    DeleteFn::Native::registerNativeFunctions(env);
    return JNI_VERSION_10;
}