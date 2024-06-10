package com.foodey.server.utils;

import com.cloudinary.Cloudinary;
import com.foodey.server.config.CloudinaryConfig;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryUtils {

  public static String generateSignature(Map<String, Object> paramsToSign) {
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
    return new Cloudinary().apiSignRequest(paramsToSign, CloudinaryConfig.API_SECRET);
  }

  public static Map<String, Object> generateCloudinaryRequestOptions(Map<String, Object> params) {
    try {
      params.put("signature", generateSignature(params));
      return params;
    } catch (UnsupportedOperationException e) {
      return new HashMap<>(params) {
        {
          put("signature", generateSignature(params));
        }
      };
    }
  }
}
