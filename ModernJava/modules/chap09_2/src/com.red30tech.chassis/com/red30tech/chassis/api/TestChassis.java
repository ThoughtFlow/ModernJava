package com.red30tech.chassis.api;

/**
 * This is the test class for Chassis
 */
public class TestChassis {

   public static void main(String... args) {

      Chassis chassis = Chassis.buildStandardChassis();

      while (chassis.getVelocityInKph() < 100) {
         System.out.println("increasing speed of chassis..." + (int) chassis.getVelocityInKph());
         chassis.increaseVelocity();
      }

      while (chassis.getVelocityInKph() > 0) {
         System.out.println("decreasing speed of chassis..." + (int) chassis.getVelocityInKph());
         chassis.decreaseVelocity();
      }

      System.out.println("Testing tire pressure...Safe: " + chassis.isSafeTirePressure());
   }
}
