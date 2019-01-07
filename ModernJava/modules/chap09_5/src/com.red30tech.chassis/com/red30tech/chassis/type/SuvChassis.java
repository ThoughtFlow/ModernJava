package com.red30tech.chassis.type;

import com.red30tech.axle.api.Axle;
import com.red30tech.movement.api.Wheel;
import com.red30tech.chassis.api.Chassis;
import com.red30tech.airbag.api.Airbag;

/**
 * Suv implementation of the Chassis.
 */
public class SuvChassis implements Chassis {

  private final Axle frontAxle;
  private final Axle rearAxle;
  private final Airbag frontDriverAirbag;
  private final Airbag sideDriverAirbag;
  private final Airbag sidePassengerAirbag;
  private final Airbag frontPassengerAirbag;

  public SuvChassis() {
     frontAxle = Axle.buildStandardAxle();
     rearAxle = Axle.buildStandardAxle();

     frontDriverAirbag = safeInstantiateAirbag();
     sideDriverAirbag = safeInstantiateAirbag();
     sidePassengerAirbag = safeInstantiateAirbag();
     frontPassengerAirbag = safeInstantiateAirbag();
  }

  private Airbag safeInstantiateAirbag() {
     // It is safe to declare a type from an optional module
     Airbag airbag;

     try {
        // Check if airbag module is available
        // It is not safe to instantiate or use a type from an optional module.
        airbag = new Airbag();
     }
     catch (NoClassDefFoundError exception) {
        // The airbag module is not available
        airbag = null;
     }

     return airbag;
  }

  @Override
  public String getChassisName() {
     return "Suv";
  }

  @Override
  public void increaseVelocity() {
     frontAxle.increaseRpm();
     rearAxle.increaseRpm();
  }

  @Override
  public void decreaseVelocity() {
     frontAxle.decreaseRpm();
     rearAxle.decreaseRpm();
  }

  @Override
  public double getVelocityInKph() {
     return (frontAxle.getVelocityInKph() + rearAxle.getVelocityInKph()) / 2;
  }

  @Override
  public boolean isSafeTirePressure() {
     return isSafeTirePressure(frontAxle.getLeftWheel()) &&
            isSafeTirePressure(frontAxle.getRightWheel()) &&
            isSafeTirePressure(rearAxle.getLeftWheel()) &&
            isSafeTirePressure(rearAxle.getRightWheel());
       
  }

  @Override
  public void pumpAir() {
     pumpAir(frontAxle.getLeftWheel());
     pumpAir(frontAxle.getRightWheel());
     pumpAir(rearAxle.getLeftWheel());
     pumpAir(rearAxle.getRightWheel());
  }

  @Override
  public boolean testAirbag() {
     boolean isAirbagOn;
     if (frontDriverAirbag != null) {
        isAirbagOn = frontDriverAirbag.test() && sideDriverAirbag.test() && sidePassengerAirbag.test() && frontPassengerAirbag.test();
     }
     else {
        isAirbagOn = false;
     }

     return isAirbagOn;
  }
     
  private void pumpAir(Wheel wheel) {
     wheel.getRubberTire().pumpAir();
  }

  private boolean isSafeTirePressure(Wheel wheel) {
     return wheel.getRubberTire().getTirePressure() > 50;
  }
}
