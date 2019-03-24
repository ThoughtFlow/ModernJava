#!/bin/bash   
set -v

rm -rf /tmp/chap11_5/jmods /tmp/chap11_5/jlink
mkdir -p /tmp/chap11_5/jmods

#Compile
javac -d /tmp/chap11_5/mods/ --module-source-path src $(find src -name "*.java")

#Jmod
jmod create /tmp/chap11_5/jmods/com.red30tech.movement.jmod --class-path /tmp/chap11_5/mods/com.red30tech.movement
jmod create /tmp/chap11_5/jmods/com.red30tech.axle.jmod --class-path /tmp/chap11_5/mods/com.red30tech.axle
jmod create /tmp/chap11_5/jmods/com.red30tech.chassis.jmod --class-path /tmp/chap11_5/mods/com.red30tech.chassis
jmod create /tmp/chap11_5/jmods/com.red30tech.airbag.jmod --class-path /tmp/chap11_5/mods/com.red30tech.airbag
jmod create /tmp/chap11_5/jmods/com.red30tech.driver.jmod --class-path /tmp/chap11_5/mods/com.red30tech.driver

#Hash
jmod hash --module-path /tmp/chap11_5/jmods --hash-modules .*

#describe
jmod list /tmp/chap11_5/jmods/com.red30tech.movement.jmod
jmod list /tmp/chap11_5/jmods/com.red30tech.axle.jmod
jmod list /tmp/chap11_5/jmods/com.red30tech.chassis.jmod
jmod list /tmp/chap11_5/jmods/com.red30tech.airbag.jmod
jmod list /tmp/chap11_5/jmods/com.red30tech.driver.jmod
jmod describe /tmp/chap11_5/jmods/com.red30tech.movement.jmod
jmod describe /tmp/chap11_5/jmods/com.red30tech.axle.jmod
jmod describe /tmp/chap11_5/jmods/com.red30tech.chassis.jmod
jmod describe /tmp/chap11_5/jmods/com.red30tech.airbag.jmod
jmod describe /tmp/chap11_5/jmods/com.red30tech.driver.jmod

#Jlink
jlink --module-path $JAVA_HOME/jmods:/tmp/chap11_5/jmods --add-modules com.red30tech.driver --output /tmp/chap11_5/jlink --launcher run=com.red30tech.driver/com.red30tech.driver.TestDriver
