#!/usr/bin/env bash

# Only works for Java 14
javac -d /tmp -cp . --release 14 --enable-preview MultiLineTest.java

#If there are no compilation errors - run the program
if [ $? -eq 0 ]; then
   java -cp /tmp --enable-preview chap12.MultiLineTest
fi

