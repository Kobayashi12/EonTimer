#!/bin/bash

if [ "$OS" == "linux" ]; then
  zip -r EonTimer-$OS.zip EonTimer
  sha256sum EonTimer-$OS.zip > EonTimer-$OS.zip.sha256
elif [ "$OS" == "macOS" ]; then
  zip -r EonTimer-$OS.zip EonTimer.dmg
  shasum -a 256 EonTimer-$OS.zip > EonTimer-$OS.zip.sha256
fi