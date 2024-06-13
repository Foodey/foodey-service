package com.foodey.server.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.foodey.server.common.model.CloudinaryImage;
import com.foodey.server.common.model.CloudinaryImageManager;
import com.foodey.server.validation.annotation.OptimizedName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "product_categories")
@JsonIgnoreProperties(
    value = {"id"},
    allowGetters = true)
@NoArgsConstructor
public class ProductCategory implements CloudinaryImageManager {

  @Id
  @Schema(description = "The unique identifier of the category")
  private String id;

  @Schema(description = "The name of the category")
  @OptimizedName
  private String name;

  @Transient
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(
      description =
          "The options for the Cloudinary image upload API. This field is used for payload only")
  private Map<String, Object> cldImageUploadApiOptions;

  @Schema(description = "The image url of the category")
  private CloudinaryImage cldImage = new CloudinaryImage(getCloudinaryFolder());

  public String getImage() {
    return cldImage.getUrl();
  }

  @Schema(description = "The description of the category")
  private String description = "";

  public ProductCategory(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
