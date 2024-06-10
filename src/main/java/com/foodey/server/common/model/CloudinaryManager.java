package com.foodey.server.common.model;

import com.foodey.server.config.CloudinaryConfig;

public interface CloudinaryManager {

  default String getCloudinaryFolder() {
    return getClass().getSimpleName().toLowerCase() + "s";
  }

  default String getCloudinaryImageEndpoint(String cloud_name) {
    return "https://res.cloudinary.com/" + cloud_name + "/image/upload";
  }

  default String getCloudinaryImageEndpoint() {
    return "https://res.cloudinary.com/" + CloudinaryConfig.CLOUD_NAME + "/video/upload";
  }

  default String getCloudinaryImageURL(String publicId) {
    return String.format("%s/%s/%s", getCloudinaryImageEndpoint(), getCloudinaryFolder(), publicId);
  }
}
