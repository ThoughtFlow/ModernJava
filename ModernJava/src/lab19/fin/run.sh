#!/usr/bin/env bash

javac -d /tmp/lab19 ../../lab19/fin/Infinite.java

# Configured to:
#   a) Use the G1 garbage collector
#   b) Turn on debug heap profiling and output to file /tmp/GC.log
#   c) Set the minimum heap to 100Mb and the max heap to 200Mb
java -Xlog:gc+heap=debug:file=/tmp/GC.log -Xms100M -Xmx200m -XX:+UseG1GC  -cp /tmp/lab19 lab19.fin.Infinite

# Configured to:
#   a) Use the epsilon garbage collector
#   b) Turn on debug heap profiling and output to file /tmp/GC.log
#   c) Set the minimum heap to 100Mb and the max heap to 200Mb
java -Xlog:gc+heap=debug:file=/tmp/GC.log -Xms100M -Xmx200m -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC  -cp /tmp/lab19 lab19.fin.Infinite
