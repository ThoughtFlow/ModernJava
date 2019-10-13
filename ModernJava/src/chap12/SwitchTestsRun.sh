#!/usr/bin/env bash

#Only works for Java 13
javac -d /tmp -cp . --release 13 --enable-preview SwitchTests.java

#If there are no compilation errors - run the program
if [ $? -eq 0 ]; then
   java -cp /tmp --enable-preview chap12.SwitchTests
fi

