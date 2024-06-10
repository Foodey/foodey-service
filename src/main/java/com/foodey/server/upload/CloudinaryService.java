package com.foodey.server.upload;

import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

  Map<String, Object> upload(MultipartFile file, Map<String, Object> options);

  @SuppressWarnings({"unchecked", "rawtypes"})
  default Map uploadFile(MultipartFile file, String folderName, String id) throws IOException {
    return this.upload(
        file,
        ObjectUtils.asMap(
            "folder", folderName, "overwrite", true, "asset_folder", folderName, "public_id", id));
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  default Map uploadVideo(MultipartFile file, String folderName) throws IOException {
    return this.upload(
        file,
        ObjectUtils.asMap(
            "resource_type",
            "video",
            "folder",
            folderName,
            "overwrite",
            true,
            "asset_folder",
            folderName));
  }

  String generateSignature(Map<String, Object> paramsToSign);

  default String generateSignature(String folder, String fileName) {
    HashMap<String, Object> params =
        new HashMap<>() {
          {
            put("asset_folder", folder);
            put("public_id", fileName);
          }
        };
    return this.generateSignature(params);
  }

  default String generateSignature(String folder, String fileName, String displayName) {
    HashMap<String, Object> params =
        new HashMap<>() {
          {
            put("asset_folder", folder);
            put("public_id", fileName);
            put("display_name", displayName);
          }
        };
    return this.generateSignature(params);
  }

  Map<String, Object> generateCloudinaryRequestOptions(Map<String, Object> params);
}
