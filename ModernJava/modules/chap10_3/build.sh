#!/bin/bash   
set -v

javac -d /tmp/chap10_3/mods/ --module-source-path src $(find src -name "*.java")
