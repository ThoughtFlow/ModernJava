#!/bin/bash   
set -v

rm -rf /tmp/chap10_3/mods /tmp/chap10_3/mods2

javac -d /tmp/chap10_3/mods/ --module-source-path src $(find src -name "*.java")
