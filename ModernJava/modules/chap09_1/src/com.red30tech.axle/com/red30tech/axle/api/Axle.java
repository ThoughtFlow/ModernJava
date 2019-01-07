package com.red30tech.axle.api;

import com.red30tech.movement.api.Wheel;

/**
 * This class represents the abstraction for an axle.
 */
public class Axle {

  private Wheel leftWheel;
  private Wheel rightWheel;

  public static Axle buildStandardAxle() {
     Axle axle = new Axle();
     axle.leftWheel = Wheel.buildStandardWheel();
     axle.rightWheel = Wheel.buildStandardWheel();

     return axle;
  }

  public void increaseRpm() {
     leftWheel.increaseRpm();
     rightWheel.increaseRpm();
  }

  public void decreaseRpm() {
     leftWheel.decreaseRpm();
     rightWheel.decreaseRpm();
  }

  public double getVelocityInKph() {
    // Returns the average of the two wheels in case of differences
    return (leftWheel.getVelocityInKph() + rightWheel.getVelocityInKph()) / 2;
  }
}
