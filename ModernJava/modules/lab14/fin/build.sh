#!/bin/bash
set -x

rm -rf /tmp/lab14/jmods /tmp/lab14/mods /tmp/lab14/jlink
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

#Create
jmod create /tmp/lab14/jmods/com.lab14.database.jmod --class-path /tmp/lab14/mods/com.lab14.database
jmod create /tmp/lab14/jmods/com.lab14.cache.jmod --class-path /tmp/lab14/mods/com.lab14.cache
jmod create /tmp/lab14/jmods/com.lab14.urlfetcher.jmod --class-path /tmp/lab14/mods/com.lab14.urlfetcher
jmod create /tmp/lab14/jmods/com.lab14.client.jmod --class-path /tmp/lab14/mods/com.lab14.client

#Describe the contents
jmod describe /tmp/lab14/jmods/com.lab14.database.jmod
jmod describe /tmp/lab14/jmods/com.lab14.cache.jmod
jmod describe /tmp/lab14/jmods/com.lab14.urlfetcher.jmod
jmod describe /tmp/lab14/jmods/com.lab14.client.jmod

#Jlink it
rm -rf /tmp/jlink
jlink --module-path $JAVA_HOME/jmods:/tmp/lab14/jmods --add-modules com.lab14.client,com.lab14.cache --output /tmp/lab14/jlink --launcher run=com.lab14.client/com.lab14.client.Client
