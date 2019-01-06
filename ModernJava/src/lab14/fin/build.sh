#!/bin/bash
set -x

rm -rf /tmp/jmods
mkdir /tmp/jmods

#Create
jmod create /tmp/jmods/com.lab13.database.jmod --class-path ../../../out/production/com.lab13.database
jmod create /tmp/jmods/com.lab13.cache.jmod --class-path ../../../out/production/com.lab13.cache
jmod create /tmp/jmods/com.lab13.urlfetcher.jmod --class-path ../../../out/production/com.lab13.urlfetcher
jmod create /tmp/jmods/com.lab13.client.jmod --class-path ../../../out/production/com.lab13.client

#Describe the contents
jmod describe /tmp/jmods/com.lab13.database.jmod 
jmod describe /tmp/jmods/com.lab13.cache.jmod 
jmod describe /tmp/jmods/com.lab13.urlfetcher.jmod  
jmod describe /tmp/jmods/com.lab13.client.jmod 

#Jlink it
rm -rf /tmp/jlink
jlink --module-path $JAVA_HOME/jmods:/tmp/jmods --add-modules com.lab13.client --output /tmp/jlink --launcher run=com.lab13.client/com.lab13.client.Client
