package com.foodey.server.common.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.foodey.server.config.CloudinaryConfig;
import lombok.Getter;
import lombok.Setter;

/** CloudinaryImage */
@Getter
@Setter
public class CloudinaryImage {

  private String publicId = NanoIdUtils.randomNanoId();

  private String folder;

  private String url;

  public CloudinaryImage(String folder) {
    this.folder = folder;
    this.url =
        String.format(
            "https://res.cloudinary.com/%s/image/upload/%s/%s",
            CloudinaryConfig.CLOUD_NAME, folder, publicId);
  }
}
