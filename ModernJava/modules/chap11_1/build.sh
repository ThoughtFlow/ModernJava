#!/bin/bash   
set -v

javac -d /tmp/chap11_1/mods/ --module-source-path src $(find src -name "*.java")
