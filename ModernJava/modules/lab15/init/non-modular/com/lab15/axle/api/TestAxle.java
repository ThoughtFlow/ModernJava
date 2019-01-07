package com.lab15.axle.api;

import com.lab15.movement.api.Wheel;

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
  }
}
