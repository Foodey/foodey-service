package com.foodey.server.order;

import com.foodey.server.payment.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

  String shopId;

  PaymentMethod paymentMethod = PaymentMethod.CASH;

  String voucherCode;

  String address;
}
