#!/bin/bash
set -v

java --module-path /tmp/lab15/mods:/tmp/lab15/bin -cp /tmp/lab15/classes --add-modules ALL-MODULE-PATH -m com.lab15.driver/com.lab15.driver.TestDriver
