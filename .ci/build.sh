#!/bin/bash

if [ "$OS" == "linux" ]; then
  cmake -DCMAKE_BUILD_TYPE=RELEASE -DCMAKE_PREFIX_PATH="$HOME/Qt/latest/gcc_64" .
  make -j $(nproc)
elif [ "$OS" == "macOS" ]; then
  declare QT_HOME="$HOME/Qt/latest/clang_64"
  cmake -DCMAKE_BUILD_TYPE=RELEASE -DCMAKE_PREFIX_PATH="$QT_HOME" .
  make -j $(sysctl -n hw.physicalcpu)
  $QT_HOME/bin/macdeployqt EonTimer.app -dmg -verbose=2
fi
