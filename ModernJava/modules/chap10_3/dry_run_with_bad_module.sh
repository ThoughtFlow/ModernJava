#!/bin/bash
set -v

java --module-path doesnotexist --dry-run -m com.red30tech.driver/com.red30tech.driver.TestDriver
