package com.red30tech.driver;

import java.util.ServiceLoader;
import java.util.Optional;
import com.red30tech.chassis.api.Chassis;

/**
 * Uses the ServiceProvider to get an implementation of Chassis and takes it out for a test drive.
 */
public class TestDriver {
 
  private static Chassis getChassis() {
     ServiceLoader<Chassis> serviceLoader = ServiceLoader.load(Chassis.class);

     // new Airbag();
     System.out.println("Found " + serviceLoader.stream().count() + " chassis configured");

     Optional<Chassis> optional = serviceLoader.findFirst();
     optional.orElseThrow(() -> new RuntimeException("No service providers found"));
     Chassis chassis = optional.get();

     return chassis;
  }

  public static void testDrive(Chassis chassis) {
     while (chassis.getVelocityInKph() < 100) {
        System.out.println("Increasing velocity of Chassis..." + (int) chassis.getVelocityInKph());
        chassis.increaseVelocity();
     }

     while (chassis.getVelocityInKph() > 0) {
        System.out.println("Decreasing velocity of Chassis..." + (int) chassis.getVelocityInKph());
        chassis.decreaseVelocity();
     }
  }

  public static void main(String... args) {
     Chassis chassis = getChassis();
     System.out.println("Using Chassis: " + chassis.getChassisName());
     System.out.println("Testing airbags: " + chassis.testAirbag());
     testDrive(chassis);
  }
}
