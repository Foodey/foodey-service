package com.foodey.server.upload.service;

import com.foodey.server.config.CloudinaryConfig;
import com.foodey.server.upload.model.CloudinaryImage;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

  default String getPrefixUrl() {
    return "https://res.cloudinary.com/" + CloudinaryConfig.CLOUD_NAME + "/image/upload/";
  }

  Map<String, Object> upload(MultipartFile file, Map<String, Object> options);

  default Map<String, Object> uploadFile(MultipartFile file, String folderName, String id) {
    Map<String, Object> options = new HashMap<>();
    options.put("folder", folderName);
    options.put("overwrite", true);
    options.put("asset_folder", folderName);
    options.put("public_id", id);
    return this.upload(file, options);
  }

  default Map<String, Object> uploadVideo(MultipartFile file, String folderName) {
    Map<String, Object> options = new HashMap<>();
    options.put("resource_type", "video");
    options.put("folder", folderName);
    options.put("overwrite", true);
    options.put("asset_folder", folderName);
    return this.upload(file, options);
  }

  String generateSignature(Map<String, Object> paramsToSign);

  default String generateSignature(String folder, String publicId) {
    Map<String, Object> params = new HashMap<>();
    params.put("asset_folder", folder);
    params.put("public_id", publicId);
    return this.generateSignature(params);
  }

  default String generateSignature(String folder, String publicId, String displayName) {
    Map<String, Object> params = new HashMap<>();
    params.put("asset_folder", folder);
    params.put("public_id", publicId);
    params.put("display_name", displayName);
    return this.generateSignature(params);
  }

  Map<String, Object> getUploadApiOptions(Map<String, Object> paramsToSign);

  default Map<String, Object> getUploadApiOptions(String folder, String publicId) {
    Map<String, Object> options = new HashMap<>();
    options.put("timestamp", System.currentTimeMillis() / 1000L);
    options.put("folder", folder);
    options.put("asset_folder", folder);
    options.put("overwrite", true);
    options.put("public_id", publicId);
    return getUploadApiOptions(options);
  }

  default Map<String, Object> getUploadApiOptions(
      String folder, String publicId, String displayName) {
    Map<String, Object> options = new HashMap<>();
    options.put("timestamp", System.currentTimeMillis() / 1000L);
    options.put("folder", folder);
    options.put("asset_folder", folder);
    options.put("overwrite", true);
    options.put("public_id", publicId);
    options.put("display_name", displayName);
    return getUploadApiOptions(options);
  }

  default Map<String, Object> getUploadApiOptions(CloudinaryImage cldImage) {
    if (cldImage.getDisplayName() != null) {
      return getUploadApiOptions(
          cldImage.getFolder(), cldImage.getPublicId(), cldImage.getDisplayName());
    }
    return getUploadApiOptions(cldImage.getFolder(), cldImage.getPublicId());
  }
}
