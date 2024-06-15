package com.foodey.server.upload.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodey.server.config.CloudinaryConfig;

public interface CloudinaryImageManager {

  @JsonIgnore
  default String getCloudinaryFolder() {
    return getClass().getSimpleName().toLowerCase() + "s";
  }

  default String getCloudinaryImageEndpoint(String cloud_name) {
    return "https://res.cloudinary.com/" + cloud_name + "/image/upload";
  }

  @JsonIgnore
  default String getCloudinaryImageEndpoint() {
    return "https://res.cloudinary.com/" + CloudinaryConfig.CLOUD_NAME + "/image/upload";
  }

  default String getCloudinaryImageURL(String publicId) {
    return String.format("%s/%s/%s", getCloudinaryImageEndpoint(), getCloudinaryFolder(), publicId);
  }
}
