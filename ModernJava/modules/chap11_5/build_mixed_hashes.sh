#!/bin/bash   
set -v

rm -rf /tmp/chap11_5/jmods /tmp/chap11_5/jlink /tmp/chap11_5/jmods2
mkdir -p /tmp/chap11_5/jmods /tmp/chap11_5/jmods2

#Compile
javac -d /tmp/chap11_5/mods/ --module-source-path src $(find src -name "*.java")

#Jmod
jmod create /tmp/chap11_5/jmods/com.red30tech.movement.jmod --class-path /tmp/chap11_5/mods/com.red30tech.movement
jmod create /tmp/chap11_5/jmods/com.red30tech.axle.jmod --class-path /tmp/chap11_5/mods/com.red30tech.axle
jmod create /tmp/chap11_5/jmods/com.red30tech.chassis.jmod --class-path /tmp/chap11_5/mods/com.red30tech.chassis
jmod create /tmp/chap11_5/jmods/com.red30tech.airbag.jmod --class-path /tmp/chap11_5/mods/com.red30tech.airbag
jmod create /tmp/chap11_5/jmods/com.red30tech.driver.jmod --class-path /tmp/chap11_5/mods/com.red30tech.driver
jmod hash --module-path /tmp/chap11_5/jmods --hash-modules .*

#Jmod again to a different directory
jmod create /tmp/chap11_5/jmods2/com.red30tech.movement.jmod --class-path /tmp/chap11_5/mods/com.red30tech.movement
jmod create /tmp/chap11_5/jmods2/com.red30tech.axle.jmod --class-path /tmp/chap11_5/mods/com.red30tech.axle
jmod create /tmp/chap11_5/jmods2/com.red30tech.chassis.jmod --class-path /tmp/chap11_5/mods/com.red30tech.chassis
jmod create /tmp/chap11_5/jmods2/com.red30tech.airbag.jmod --class-path /tmp/chap11_5/mods/com.red30tech.airbag
jmod create /tmp/chap11_5/jmods2/com.red30tech.driver.jmod --class-path /tmp/chap11_5/mods/com.red30tech.driver

#Hash again
jmod hash --module-path /tmp/chap11_5/jmods2 --hash-modules .*

#describe
jmod describe /tmp/chap11_5/jmods/com.red30tech.movement.jmod
jmod describe /tmp/chap11_5/jmods2/com.red30tech.movement.jmod

#Jlink
jlink --module-path $JAVA_HOME/jmods:/tmp/chap11_5/jmods2/com.red30tech.movement.jmod:/tmp/chap11_5/jmods/com.red30tech.axle.jmod:/tmp/chap11_5/jmods/com.red30tech.chassis.jmod:/tmp/chap11_5/jmods/com.red30tech.airbag.jmod:/tmp/chap11_5/jmods/com.red30tech.driver.jmod --add-modules com.red30tech.driver --output jlink --launcher run=com.red30tech.driver/com.red30tech.driver.TestDriver
