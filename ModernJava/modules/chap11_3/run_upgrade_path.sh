#!/bin/bash
set -v

java --upgrade-module-path /tmp/chap11_3/mods2 --module-path /tmp/chap11_3/mods -m com.red30tech.axle/com.red30tech.axle.api.TestAxle
