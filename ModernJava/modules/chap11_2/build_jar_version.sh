#!/bin/bash   
set -v

rm -rf /tmp/chap11_2/bin
mkdir -p /tmp/chap11_2/bin

javac -d /tmp/chap11_2/mods/ --module-source-path src $(find src -name "*.java")

jar --create --file /tmp/chap11_2/bin/com.red30tech.axle.jar --module-version=123.02 -C /tmp/chap11_2/mods/com.red30tech.axle .
jar --create --file /tmp/chap11_2/bin/com.red30tech.movement.jar -C /tmp/chap11_2/mods/com.red30tech.movement .
jar --create --file /tmp/chap11_2/bin/com.red30tech.chassis.jar -C /tmp/chap11_2/mods/com.red30tech.chassis .
jar --create --file /tmp/chap11_2/bin/com.red30tech.driver.jar -C /tmp/chap11_2/mods/com.red30tech.driver .
jar --create --file /tmp/chap11_2/bin/com.red30tech.airbag.jar -C /tmp/chap11_2/mods/com.red30tech.airbag .
