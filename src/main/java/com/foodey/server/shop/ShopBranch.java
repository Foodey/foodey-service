package com.foodey.server.shop;

import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "branchs")
public class ShopBranch {
  @Id private String id;

  @OptimizedName private String name;

  private String logo = "";
  private String wallpaper = "";

  @PhoneNumber private String phone;

  @Email private String email;

  private String ownerId;
}
