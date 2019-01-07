package com.red30tech.movement.api;

/** 
 * Test class for Wheel.
 */
public class TestWheel {

  public static void main(String... args) {
    Wheel wheel = Wheel.buildStandardWheel();

    while (wheel.getVelocityInKph() < 100) {
       System.out.println("increasing speed..." + (int) wheel.getVelocityInKph() + " " + (int) wheel.getRpm());
       wheel.increaseRpm();
    }

    while (wheel.getVelocityInKph() > 0) {
       System.out.println("decreasing speed..." + (int) wheel.getVelocityInKph() + " " + (int) wheel.getRpm());
       wheel.decreaseRpm();
    }
  }
}
