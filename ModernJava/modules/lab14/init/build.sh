#!/bin/bash
set -x

rm -rf /tmp/lab14/jmods /tmp/lab14/mods
mkdir -p /tmp/lab14/jmods /tmp/lab14/mods

#Compile
cd com.lab14.database
javac -nowarn -d /tmp/lab14/mods/com.lab14.database $(find . -name "*.java")
cd ../com.lab14.cache
javac -nowarn -d /tmp/lab14/mods/com.lab14.cache --module-path /tmp/lab14/mods $(find . -name "*.java")
cd ../com.lab14.urlfetcher
javac -nowarn -d /tmp/lab14/mods/com.lab14.urlfetcher --module-path /tmp/lab14/mods $(find . -name "*.java")
cd ../com.lab14.client
javac -nowarn -d /tmp/lab14/mods/com.lab14.client --module-path /tmp/lab14/mods $(find . -name "*.java")

#Create the jmod files

#Describe the contents

#Jlink it

