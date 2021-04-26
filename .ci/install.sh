#!/bin/bash

git submodule update --init
if [ "$OS" == "linux" ]; then
  sudo apt-get update
  sudo apt-get python3-pip -y
fi
pip3 install qtsass
