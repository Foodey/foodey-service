package com.foodey.server.order;

import com.foodey.server.common.payload.IBodyRequest;
import com.foodey.server.payment.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest implements IBodyRequest {

  String shopId;

  PaymentMethod paymentMethod = PaymentMethod.CASH;

  String address;

  String note;
}
