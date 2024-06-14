package com.foodey.server.upload.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.foodey.server.config.CloudinaryConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CloudinaryImage {

  private String publicId = NanoIdUtils.randomNanoId();

  private String folder;

  private String url;

  private String displayName;

  public CloudinaryImage() {
    this.folder = this.getClass().getSuperclass().getSimpleName().toLowerCase() + "s";
    this.url =
        String.format(
            "https://res.cloudinary.com/%s/image/upload/%s/%s",
            CloudinaryConfig.CLOUD_NAME, folder, publicId);
  }

  public CloudinaryImage(String folder) {
    this.folder = folder;
    this.url =
        String.format(
            "https://res.cloudinary.com/%s/image/upload/%s/%s",
            CloudinaryConfig.CLOUD_NAME, folder, publicId);
  }

  public CloudinaryImage(String folder, String displayName) {
    this.folder = folder;
    this.url =
        String.format(
            "https://res.cloudinary.com/%s/image/upload/%s/%s",
            CloudinaryConfig.CLOUD_NAME, folder, publicId);
    this.displayName = displayName;
  }

  // public CloudinaryImage() {
  //   updateValueFromAnnotation();
  //   this.url =
  //       String.format(
  //           "https://res.cloudinary.com/%s/image/upload/%s/%s",
  //           CloudinaryConfig.CLOUD_NAME, folder, publicId);
  // }

  // private void updateValueFromAnnotation() {
  //   CloudinaryFolder annotation = this.getClass().getAnnotation(CloudinaryFolder.class);

  //   this.folder =
  //       StringUtils.hasText(annotation.folder())
  //           ? annotation.folder()
  //           : this.getClass().getSuperclass().getSimpleName().toLowerCase() + "s";

  //   if (StringUtils.hasText(annotation.displayName())) {
  //     this.displayName = annotation.displayName();
  //   }
  // }
}
