#!/bin/bash
set -v

java --module-path /tmp/chap11_3/mods/ --show-module-resolution -m com.red30tech.driver/com.red30tech.driver.TestDriver
