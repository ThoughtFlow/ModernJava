#!/bin/bash   
set -v

javac -d /tmp/chap10_1/mods/ --module-source-path src --module-version 1TwoThree.01  $(find src -name "*.java")

