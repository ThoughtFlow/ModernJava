#!/bin/bash   
set -v

javac -d /tmp/chap10_3/mods2/ --module-version 123.02 --module com.red30tech.axle --module-source-path src
