#!/bin/bash   
set -v

javac -d /tmp/chap11_3/mods/ --module-version 123.01 --module-source-path src $(find src -name "*.java")
