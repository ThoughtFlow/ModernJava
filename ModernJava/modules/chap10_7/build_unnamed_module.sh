#!/bin/bash   
set -v

rm -rf /tmp/chap10_7/mods /tmp/chap10_7/bin /tmp/chap10_7/classes
mkdir /tmp/chap10_7/mods /tmp/chap10_7/bin /tmp/chap10_7/classes

javac -d /tmp/chap10_7/mods/ --module-source-path src_9 $(find src_9 -name "*.java")
javac -d /tmp/chap10_7/classes/ -cp /tmp/chap10_7/mods/com.red30tech.movement --source-path src_8 $(find src_8 -name "*.java")

jar --create --file /tmp/chap10_7/bin/com.red30tech.movement.jar -C /tmp/chap10_7/mods/com.red30tech.movement .
