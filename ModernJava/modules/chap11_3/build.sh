#!/bin/bash   
set -v

rm -rf /tmp/chap11_3/mods /tmp/chap11_3/mods2

javac -d /tmp/chap11_3/mods/ --module-source-path src $(find src -name "*.java")
