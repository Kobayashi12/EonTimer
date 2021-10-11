#!/bin/bash

cd $APPVEYOR_BUILD_FOLDER
git submodule update --init

case $OS in
  linux)
    sh .ci/linux/install.sh
    ;;  
  macOS)
    sh .ci/macos/install.sh
    ;;
esac
