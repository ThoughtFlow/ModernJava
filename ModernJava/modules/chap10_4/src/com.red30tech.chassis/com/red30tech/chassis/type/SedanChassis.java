package com.red30tech.chassis.type;

import com.red30tech.axle.api.Axle;
import com.red30tech.movement.api.Wheel;
import com.red30tech.chassis.api.Chassis;

/**
 * This is the Sedan implementation of Chassis (no difference than the other implementation)
 */
public class SedanChassis implements Chassis {

  private Axle frontAxle;
  private Axle rearAxle;

  public SedanChassis() {
     frontAxle = Axle.buildStandardAxle();
     rearAxle = Axle.buildStandardAxle();
  }

  public String getChassisName() {
     return "Sedan";
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
