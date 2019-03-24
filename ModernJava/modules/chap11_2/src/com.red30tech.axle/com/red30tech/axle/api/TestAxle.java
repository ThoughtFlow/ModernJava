package com.red30tech.axle.api;

import java.util.Optional;
import java.lang.module.ModuleDescriptor;

/**
 * Test class for the Axle class.
 */
public class TestAxle {

  public static void main(String... args) {
    Axle axle = Axle.buildStandardAxle();

    while (axle.getVelocityInKph() < 100) {
       System.out.println("increasing speed of Axle..." + (int) axle.getVelocityInKph());
       axle.increaseRpm();
    }

    while (axle.getVelocityInKph() > 0) {
       System.out.println("decreasing speed of Axle..." + (int) axle.getVelocityInKph());
       axle.decreaseRpm();
    }

    Optional<ModuleDescriptor.Version> op =  axle.getClass().getModule().getDescriptor().version();
    String version = op.orElse(ModuleDescriptor.Version.parse("1")).toString();
    System.out.println("Module version: " + version);
  }
}
