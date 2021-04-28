#!/bin/bash

set -e

git submodule update --init
./vcpkg/bootstrap-vcpkg.sh

if [ "$OS" == "linux" ]; then
  sudo apt-get update
  sudo apt-get install python3 python3-pip libx11-dev libxrandr-dev libxi-dev libudev-dev libgl1-mesa-dev -y
fi

pip3 install qtsass
python3 -m qtsass -o resources/styles resources/styles
