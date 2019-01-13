#!/usr/bin/env bash

java -Xlog:gc+heap=debug:file=/tmp/GC.log -Xms100M -Xmx200m -XX:+UseG1GC  -cp ../../../out/production/ModernJava lab05.fin.Infinite
java -Xlog:gc+heap=debug:file=/tmp/GC.log -Xms100M -Xmx200m -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC  -cp ../../../out/production/ModernJava lab05.fin.Infinite