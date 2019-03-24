package com.red30tech.chassis.api;

/**
 * Test class for Chassis
 */
public class TestChassis {

   public static void main(String... args) {

      Chassis chassis = Chassis.buildStandardChassis();

      while (chassis.getVelocityInKph() < 100) {
         System.out.println("increasing speed..." + (int) chassis.getVelocityInKph());
         chassis.increaseVelocity();
      }

      while (chassis.getVelocityInKph() > 0) {
         System.out.println("decreasing speed..." + (int) chassis.getVelocityInKph());
         chassis.decreaseVelocity();
      }

      System.out.println("Pumping air...");
      chassis.pumpAir();
      System.out.println("Pumping air...done");
      System.out.println("Testing tire pressure...Safe: " + chassis.isSafeTirePressure());
   }
}
