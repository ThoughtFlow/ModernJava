#!/usr/bin/env bash

javac -d /tmp/lab19 ../../lab19/fin/Infinite.java

java -Xlog:gc+heap=debug:file=/tmp/GC.log -Xms100M -Xmx200m -XX:+UseG1GC  -cp /tmp/lab19 lab19.fin.Infinite
java -Xlog:gc+heap=debug:file=/tmp/GC.log -Xms100M -Xmx200m -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC  -cp /tmp/lab19 lab19.fin.Infinite
