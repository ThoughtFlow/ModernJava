#!/bin/bash   
set -v

javac -d /tmp/chap10_1/mods/ --module-source-path src --module-version OneTwoThree.01 $(find src -name "*.java")

