#!/bin/bash   
set -v

javac -d /tmp/chap09_1/classes/ $(find src/com.red30tech.movement -name "*.java")
