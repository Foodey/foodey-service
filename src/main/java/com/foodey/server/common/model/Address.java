package com.foodey.server.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

  private String street;

  private String city;

  private double[] coords = new double[2];

  private String zipCode;
}
