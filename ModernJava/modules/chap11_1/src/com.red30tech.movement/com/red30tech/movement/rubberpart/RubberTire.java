package com.red30tech.movement.rubberpart;

/**
 * This class is an abstract representation of a rubber tire.
 */
public class RubberTire {

   private static final int RADIUS_IN_CM = 83;
   private static final int MAX_RPM_LOW_PRESSURE = 160;
   private static final int LOW_TIRE_KPA = 70;

   private int tirePressureKpa = 205;
   private double rpm;
   private Valve valve;

   public static RubberTire buildRubberTire() {
      RubberTire tire = new RubberTire();
      Valve valve = new Valve();
      valve.attachTo(tire);
      tire.valve = valve;

      return tire;
   }

   public void pumpAir() {
      valve.pumpAir();
   }

   public void setTirePressure(int tirePressureKpa) {
      this.tirePressureKpa = tirePressureKpa;
   }

   public int getTirePressure() {
      return tirePressureKpa;
   }

   public void setRpm(double rpm) throws IllegalArgumentException {
      if (tirePressureKpa <= LOW_TIRE_KPA && rpm <= MAX_RPM_LOW_PRESSURE) {
            throw new IllegalArgumentException("Max RPM in low pressure is: " + MAX_RPM_LOW_PRESSURE);
      }

      this.rpm = rpm;
   }

   public double getRpm() {
      return rpm;
   }

   public int getRadiusCm() {
      return RADIUS_IN_CM;
   }
}
