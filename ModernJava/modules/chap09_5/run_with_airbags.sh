#!/bin/bash
set -v

java --module-path /tmp/chap09_5/mods/ --add-modules com.red30tech.airbag -m com.red30tech.driver/com.red30tech.driver.TestDriver
