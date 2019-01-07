#!/bin/bash
set -v

jdeps --list-reduced-deps --module-path /tmp/chap10_3/mods/ mods/com.red30tech.driver
