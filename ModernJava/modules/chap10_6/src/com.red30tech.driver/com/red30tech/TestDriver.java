package com.red30tech.driver;

import java.util.Optional;
import java.util.ServiceLoader;
import java.lang.reflect.Method;
import com.red30tech.chassis.api.Chassis;
//import com.red30tech.chassis.type.SuvChassis; //This won't compile

/**
 * Uses the ServiceProvider to get an implementation of Chassis and takes it out for a test drive.
 */
public class TestDriver {

  private static Chassis getChassisViaServiceLoader() {
      ServiceLoader<Chassis> serviceLoader = ServiceLoader.load(Chassis.class);
      Optional<Chassis> optional = serviceLoader.findFirst();
      return optional.orElseThrow(() -> new IllegalStateException("No Chassis Found"));
  }

  // This won't compile because of JPMS strong encapsulation (that's good!)
  private static Chassis getChassisViaInstantiation() {
      return null;   
     //return new SuvChassis();
  }

  private static Object getChassisViaReflection() throws Exception {
    Class<?> clazz = Class.forName("com.red30tech.chassis.type.SuvChassis");
    return clazz.getDeclaredConstructor().newInstance();
  }

  private static Object executeMethod(Object chassis, String name) throws Exception {
     Method method = chassis.getClass().getMethod(name);
     return method.invoke(chassis);
  }

  public static void testDrive(Object chassis) throws Exception {
     while ((double) executeMethod(chassis, "getVelocityInKph") < 100) {
        double currentSpeed = (Double) executeMethod(chassis, "getVelocityInKph");
        System.out.println("Increasing velocity of Driver..." + (int) currentSpeed);
        executeMethod(chassis, "increaseVelocity");
     }

     while ((double) executeMethod(chassis, "getVelocityInKph") > 0) {
        double currentSpeed = (Double) executeMethod(chassis, "getVelocityInKph");
        System.out.println("Decreasing velocity of Driver..." + (int) currentSpeed);
        executeMethod(chassis, "decreaseVelocity");
     }
  }

  public static void main(String... args) {
     try {
        Object chassis = getChassisViaReflection();
        testDrive(chassis);
     }
     catch (Exception e) {
        e.printStackTrace();
     }

     try {
        Chassis chassis = getChassisViaServiceLoader();
        System.out.println("Airbags in working condition: " + chassis.testAirbag());
     }
     catch (Exception e) {
        e.printStackTrace();
     }
   }
}
