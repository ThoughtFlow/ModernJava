#!/usr/bin/env bash

javac -d /tmp -cp . --release 12 --enable-preview SwitchTests.java

java -cp /tmp --enable-preview chap12.SwitchTests
