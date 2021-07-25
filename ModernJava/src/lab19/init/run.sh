#!/usr/bin/env bash

javac -d /tmp/lab19 ../../lab19/fin/Infinite.java

# Currently set to default GC. Change the setting accordingly:
#   a) Use the G1 garbage collector
#   b) Turn on debug heap profiling and output to file /tmp/GC.log
#   c) Set the minimum heap to 100Mb and the max heap to 200Mb
java -cp /tmp/lab19 lab19.fin.Infinite

# Currently set to default GC. Change the setting accordingly:
#   a) Use the epsilon garbage collector
#   b) Turn on debug heap profiling and output to file /tmp/GC.log
#   c) Set the minimum heap to 100Mb and the max heap to 200Mb
java -cp /tmp/lab19 lab19.fin.Infinite
