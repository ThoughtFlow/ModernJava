#!/bin/bash   
set -v

javac -d /tmp/chap09_5/mods/ --module-source-path src $(find src -name "*.java")
