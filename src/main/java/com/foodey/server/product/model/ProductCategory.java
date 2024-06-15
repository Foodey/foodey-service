package com.foodey.server.product.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.foodey.server.upload.model.CloudinaryImage;
import com.foodey.server.upload.model.CloudinaryImageManager;
import com.foodey.server.validation.annotation.OptimizedName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "product_categories")
@JsonIgnoreProperties(
    value = {"id"},
    allowGetters = true)
@NoArgsConstructor
@JsonPropertyOrder({"name"})
public class ProductCategory implements CloudinaryImageManager {

  @Id
  @Schema(description = "The unique identifier of the category")
  private String id;

  @Schema(description = "The name of the category")
  @OptimizedName
  private String name;

  private boolean deleted = false;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  @Transient
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(
      description =
          "The options for the Cloudinary image upload API. This field is used for payload only")
  private Map<String, Object> cldImageUploadApiOptions;

  @Schema(description = "The image url of the category")
  private CloudinaryImage cldImage;

  public String getImage() {
    return cldImage != null ? cldImage.getUrl() : "";
  }

  @Schema(description = "The description of the category")
  private String description = "";

  @JsonCreator
  public ProductCategory(@JsonProperty("name") String name) {
    this.name = name;
    this.cldImage = new CloudinaryImage(getCloudinaryFolder(), name);
  }

  public ProductCategory(String name, String description) {
    this.name = name;
    this.description = description;
    this.cldImage = new CloudinaryImage(getCloudinaryFolder(), name);
  }
}
