#!/bin/bash   
set -v

rm -rf /tmp/lab15/bin /tmp/lab15/classes /tmp/lab15/mods
mkdir /tmp/lab15/bin

# Suppressing warning about module names ending in digits
javac -nowarn -d /tmp/lab15/mods/ --module-source-path modular $(find modular/com.lab15.movement -name "*.java")
javac -nowarn -d /tmp/lab15/mods/ --module-source-path modular $(find modular/com.lab15.airbag -name "*.java")
javac -nowarn -d /tmp/lab15/classes -cp /tmp/lab15/mods/com.lab15.movement:/tmp/lab15/mods/com.lab15.airbag  $(find non-modular -name "*.java")
jar --create --file /tmp/lab15/bin/com.lab15.chassis.jar -C /tmp/lab15/classes com/lab15/chassis
javac -nowarn -d /tmp/lab15/mods/ --module-path /tmp/lab15/bin:/tmp/lab15/mods --module-source-path modular $(find modular/com.lab15.driver -name "*.java")
