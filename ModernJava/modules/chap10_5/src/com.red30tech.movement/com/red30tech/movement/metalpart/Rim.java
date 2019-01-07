package com.red30tech.movement.metalpart;

import com.red30tech.movement.rubberpart.RubberTire;

/**
 * This class is an abstract representation of a rim.
 */
public class Rim {

  private RubberTire tire;
  private boolean isAntiLockMode;

  public void attach(RubberTire tire) {
     this.tire = tire;
  }

  public void setRpm(double rpm) throws IllegalStateException {
     if (!isAntiLockMode) {
        tire.setRpm(rpm);
     }
     else {
        throw new IllegalStateException("Cannot change rotation rate while anti lock is activated");
     }
  }

  public double getRpm() {
     return tire.getRpm();
  }

  public void setAntiLockMode(boolean isActivated) {
     this.isAntiLockMode = isActivated;
  }

  public boolean isAntiLockMode() {
     return isAntiLockMode;
  }
}
