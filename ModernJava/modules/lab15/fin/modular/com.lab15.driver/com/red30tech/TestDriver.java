package com.lab15.driver;

import com.lab15.chassis.api.Chassis;
import com.lab15.chassis.type.SuvChassis;
import com.lab15.airbag.api.Airbag; // Simulating a direct dependency on airbag.

/**
 * Uses the ServiceProvider to get an implementation of Chassis and takes it out for a test drive.
 */
public class TestDriver {

  //Simulate a direct dependency on Airbag for the sake of the exercise.
  private Airbag airbag = null;

  private static Chassis getChassis() {
      // Hard coded to SuvChassis
      return new SuvChassis();
  }

  public static void testDrive(Chassis chassis) throws Exception {
     while ((double) chassis.getVelocityInKph() < 100) {
        double currentSpeed = chassis.getVelocityInKph();
        System.out.println("Increasing velocity of Driver..." + (int) currentSpeed);
        chassis.increaseVelocity();
     }

     while ((double) chassis.getVelocityInKph() > 0) {
        double currentSpeed = (Double) chassis.getVelocityInKph();
        System.out.println("Decreasing velocity of Driver..." + (int) currentSpeed);
        chassis.decreaseVelocity();
     }
  }

  public static void main(String... args) {
     try {
        Chassis chassis = getChassis();
        testDrive(chassis);
        System.out.println("Airbags in working condition: " + chassis.testAirbag());
     }
     catch (Exception e) {
        e.printStackTrace();
     }
   }
}
