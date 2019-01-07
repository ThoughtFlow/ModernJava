package com.red30tech.airbag.api;

/**
 * This class abstracts the concept of an airbag.
 */
public class Airbag {

  public boolean isDeployed;

  public void deploy() {
     isDeployed = true; 
  }

  public boolean test() {
     return !isDeployed();
  }

  public boolean isDeployed() {
     return isDeployed;
  }
}
