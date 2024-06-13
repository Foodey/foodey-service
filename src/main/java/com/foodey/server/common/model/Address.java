package com.foodey.server.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  private String formattedAddress;
  private String streetNumber; // Số nhà hoặc số đường của địa chỉ.
  private String route; // Tên đường.
  private String city;
  private String state; // Tên tiểu bang, tỉnh thành hoặc khu vực hành chính.
  private String postalCode; // Mã bưu chính hoặc mã zip của địa chỉ.
  private String country;
  private Double latitude; // Vĩ độ của địa chỉ.
  private Double longitude; // Kinh độ của địa chỉ.
}
