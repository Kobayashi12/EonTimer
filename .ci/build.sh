#!/bin/bash

cmake -DCMAKE_BUILD_TYPE=RELEASE .
if [ "$OS" == "linux" ]; then
  make -j $(nproc)
elif [ "$OS" == "macOS" ]; then
  make -j $(sysctl -n hw.physicalcpu)
  macdeployqt EonTimer.app -dmg -verbose=2
fi
