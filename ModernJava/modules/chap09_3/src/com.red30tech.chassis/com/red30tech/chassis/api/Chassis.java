package com.red30tech.chassis.api;

import com.red30tech.axle.api.Axle;
import com.red30tech.movement.api.Wheel;


/** 
 * This class is an abstact representation of a car Chassis.
 */
public class Chassis {

  private Axle frontAxle;
  private Axle rearAxle;

  public static Chassis buildStandardChassis() {
     Chassis chassis = new Chassis();
     chassis.frontAxle = Axle.buildStandardAxle();
     chassis.rearAxle = Axle.buildStandardAxle();

     return chassis;
  }

  public void increaseVelocity() {
     frontAxle.increaseRpm();
     rearAxle.increaseRpm();
  }

  public void decreaseVelocity() {
     frontAxle.decreaseRpm();
     rearAxle.decreaseRpm();
  }

  public double getVelocityInKph() {
     return (frontAxle.getVelocityInKph() + rearAxle.getVelocityInKph()) / 2;
  }

  public boolean isSafeTirePressure() {
     return isSafeTirePressure(frontAxle.getLeftWheel()) &&
            isSafeTirePressure(frontAxle.getRightWheel()) &&
            isSafeTirePressure(rearAxle.getLeftWheel()) &&
            isSafeTirePressure(rearAxle.getRightWheel());
       
  }

  public void pumpAir() {
     pumpAir(frontAxle.getLeftWheel());
     pumpAir(frontAxle.getRightWheel());
     pumpAir(rearAxle.getLeftWheel());
     pumpAir(rearAxle.getRightWheel());
  }

  private void pumpAir(Wheel wheel) {
     wheel.getRubberTire().pumpAir();
  }

  private boolean isSafeTirePressure(Wheel wheel) {
     return wheel.getRubberTire().getTirePressure() > 50;
  }
}
