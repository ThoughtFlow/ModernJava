#!/bin/bash   
set -v

javac -d /tmp/chap09_2/mods/ --module-source-path src $(find src -name "*.java")
