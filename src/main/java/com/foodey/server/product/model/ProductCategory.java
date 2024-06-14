package com.foodey.server.product.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.foodey.server.upload.model.CloudinaryImage;
import com.foodey.server.upload.model.CloudinaryImageManager;
import com.foodey.server.utils.ConsoleUtils;
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
@JsonPropertyOrder({"name"})
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
  private CloudinaryImage cldImage;

  @JsonProperty("cldImage")
  public void setCldImage(CloudinaryImage cldImage) {
    if (cldImage == null) {
      this.cldImage = new CloudinaryImage(getCloudinaryFolder(), name);
      return;
    }
    this.cldImage = cldImage;
  }

  public String getImage() {
    return cldImage.getUrl();
  }

  @Schema(description = "The description of the category")
  private String description = "";

  @JsonCreator
  public ProductCategory(
      @JsonProperty("name") String name, @JsonProperty("description") String description) {

    ConsoleUtils.prettyPrint("init cldImage");
    this.name = name;
    this.description = description;
    this.cldImage = new CloudinaryImage(getCloudinaryFolder(), name);
  }
}
