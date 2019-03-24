package com.red30tech.axle.api;

/**
 * This class tests the Axle class.
 */
public class TestAxle {

  public static void main(String... args) {
    Axle axle = Axle.buildStandardAxle();

    while (axle.getVelocityInKph() < 100) {
       System.out.println("increasing speed of sxle..." + (int) axle.getVelocityInKph());
       axle.increaseRpm();
    }

    while (axle.getVelocityInKph() > 0) {
       System.out.println("decreasing speed of axle..." + (int) axle.getVelocityInKph());
       axle.decreaseRpm();
    }
  }
}
