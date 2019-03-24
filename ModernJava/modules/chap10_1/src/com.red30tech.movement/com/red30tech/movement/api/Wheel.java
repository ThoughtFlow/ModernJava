package com.red30tech.movement.api;

import com.red30tech.movement.rubberpart.RubberTire;
import com.red30tech.movement.metalpart.Rim;

/** 
 * This class represents an abstraction of the Wheel and uses the Rim and RubberTire class.
 */
public class Wheel {

   private Rim rim;
   private RubberTire tire;

   public static Wheel buildStandardWheel() {
      Wheel wheel = new Wheel();
      wheel.attach(new Rim(), new RubberTire());

      return wheel;
   }

   public void attach(Rim rim, RubberTire tire) {
      rim.attach(tire);
      this.rim = rim;
      this.tire = tire;
   }

   public void increaseRpm() {
     setRpm(getVelocityInKph() + 1);
   }

   public void decreaseRpm() {
     setRpm(getVelocityInKph() - 1);
   }

   public double getVelocityInKph() {
      double circumferenceInMetres = Math.PI * 2 * tire.getRadiusCm() / 100;
      return rim.getRpm() * 60 * circumferenceInMetres / 1000;
   }

   public double getRpm() {
      return rim.getRpm();
   }

   private void setRpm(double velocityRequested) {
      double circumferenceInMetres = Math.PI * 2 * tire.getRadiusCm() / 100;
      double rpm = velocityRequested * 1000 / 60.0d / circumferenceInMetres;
      rim.setRpm(rpm);
   }
}
