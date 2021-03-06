package com.lab12.axle.api;

/**
 * This is the test class for Axle.
 */
public class TestAxle {

  public static void main(String... args) {
    Axle axle = Axle.buildStandardAxle();

    while (axle.getVelocityInKph() < 100) {
       System.out.println("increasing speed..." + (int) axle.getVelocityInKph());
       axle.increaseRpm();
    }

    while (axle.getVelocityInKph() > 0) {
       System.out.println("decreasing speed..." + (int) axle.getVelocityInKph());
       axle.decreaseRpm();
    }
  }
}
