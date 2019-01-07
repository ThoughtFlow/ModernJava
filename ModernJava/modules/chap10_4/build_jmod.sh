#!/bin/bash   
set -v

rm -rf /tmp/chap10_4/jmods /tmp/chap10_4/jlink
mkdir /tmp/chap10_4/jmods

#Compile
javac -d /tmp/chap10_4/mods/ --module-source-path src $(find src -name "*.java")

#Jmod
jmod create /tmp/chap10_4/jmods/com.red30tech.movement.jmod --class-path /tmp/chap10_4/mods/com.red30tech.movement
jmod create /tmp/chap10_4/jmods/com.red30tech.axle.jmod --class-path /tmp/chap10_4/mods/com.red30tech.axle
jmod create /tmp/chap10_4/jmods/com.red30tech.chassis.jmod --class-path /tmp/chap10_4/mods/com.red30tech.chassis
jmod create /tmp/chap10_4/jmods/com.red30tech.airbag.jmod --class-path /tmp/chap10_4/mods/com.red30tech.airbag
jmod create /tmp/chap10_4/jmods/com.red30tech.driver.jmod --class-path /tmp/chap10_4/mods/com.red30tech.driver
