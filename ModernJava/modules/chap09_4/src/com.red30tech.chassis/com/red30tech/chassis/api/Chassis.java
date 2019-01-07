package com.red30tech.chassis.api;

/**
 * Interface used by chassis to implement different types of platforms for SUVs, Sedans, Trucks, etc.
 */
public interface Chassis {

  public String getChassisName(); 

  public void increaseVelocity(); 

  public void decreaseVelocity();

  public double getVelocityInKph();

  public boolean isSafeTirePressure(); 

  public void pumpAir();
}
