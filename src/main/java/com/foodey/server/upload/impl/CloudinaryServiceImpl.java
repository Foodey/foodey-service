package com.foodey.server.upload.impl;

import com.cloudinary.Cloudinary;
import com.foodey.server.config.CloudinaryConfig;
import com.foodey.server.upload.CloudinaryService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

  private final Cloudinary cloudinary;

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> upload(MultipartFile file, Map<String, Object> options) {
    try {
      Map<String, Object> data = this.cloudinary.uploader().upload(file.getBytes(), options);
      return data;
    } catch (IOException io) {
      throw new RuntimeException("Image upload fail");
    }
  }

  @Override
  public String generateSignature(Map<String, Object> paramsToSign) {
    assert paramsToSign != null;
    long timestamp = System.currentTimeMillis() / 1000L;
    try {
      paramsToSign.put("timestamp", timestamp);
    } catch (UnsupportedOperationException e) {
      paramsToSign =
          new HashMap<>(paramsToSign) {
            {
              put("timestamp", timestamp);
            }
          };
    }
    return cloudinary.apiSignRequest(paramsToSign, CloudinaryConfig.API_SECRET);
  }

  @Override
  public Map<String, Object> generateCloudinaryRequestOptions(Map<String, Object> params) {
    assert params != null;

    String signature = generateSignature(params);
    try {
      params.put("signature", signature);
      return params;
    } catch (UnsupportedOperationException e) {
      return new HashMap<>(params) {
        {
          put("signature", signature);
        }
      };
    }
  }
}
