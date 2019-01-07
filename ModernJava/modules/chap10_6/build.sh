#!/bin/bash
set -v

javac -d /tmp/chap10_6/classes --source-path src $(find src -name "*.java")
