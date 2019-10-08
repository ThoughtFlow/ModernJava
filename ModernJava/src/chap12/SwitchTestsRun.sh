#!/usr/bin/env bash

javac -d /tmp -cp . --release 13 --enable-preview SwitchTests.java

if [ $? -eq 0 ]; then
   java -cp /tmp --enable-preview chap12.SwitchTests
fi

