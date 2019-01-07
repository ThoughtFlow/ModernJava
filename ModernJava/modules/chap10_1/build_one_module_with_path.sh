#!/bin/bash   
set -v

javac -d /tmp/chap10_1/mods/ --module-path mods --module com.red30tech.axle --module-source-path src
