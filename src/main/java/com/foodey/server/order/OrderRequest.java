package com.foodey.server.order;

import com.esotericsoftware.kryo.serializers.FieldSerializer.NotNull;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.foodey.server.common.model.Address;
import com.foodey.server.common.payload.IBodyRequest;
import com.foodey.server.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest implements IBodyRequest {

  @NotBlank private String shopId;

  private PaymentMethod paymentMethod = PaymentMethod.CASH;

  @NotNull
  @JsonAlias({"address", "deliveryAddress", "shippingAddress"}) // for backward compatibility
  private Address shippingAddress;

  private String note;
}
