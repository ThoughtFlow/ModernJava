package com.red30tech.movement.rubberpart;

/**
 * This class represents an abstraction of a Vavle that pumps air into a tire.
 */
class Valve {

  private RubberTire tire;

  public void attachTo(RubberTire tire) {
     this.tire = tire;     
  }

  public void pumpAir() {
     tire.setTirePressure(285);
  }
}
