#!/bin/bash
set -v

java --validate-modules --upgrade-module-path /tmp/chap10_3/mods2 --module-path /tmp/chap10_3/mods -m com.red30tech.axle/com.red30tech.axle.api.TestAxle
