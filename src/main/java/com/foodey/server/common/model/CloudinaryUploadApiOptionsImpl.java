package com.foodey.server.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodey.server.utils.CloudinaryUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CloudinaryUploadApiOptionsImpl implements CloudinaryUploadApiOptions {

  @JsonProperty("public_id")
  private String publicId;

  @JsonProperty("folder")
  private String folder;

  @JsonProperty("asset_folder")
  private String assetFolder;

  @JsonProperty("display_name")
  private String displayName;

  private boolean overwrite = true;

  public String signature;

  private long timestamp;

  public CloudinaryUploadApiOptionsImpl(String publicId, String folder) {
    this(publicId, folder, null, true);
  }

  public CloudinaryUploadApiOptionsImpl(String publicId, String folder, String displayName) {
    this(publicId, folder, displayName, true);
  }

  public CloudinaryUploadApiOptionsImpl(
      String publicId, String folder, String displayName, boolean overwrite) {
    this.publicId = publicId;
    this.folder = folder;
    this.assetFolder = folder;
    this.displayName = displayName;
    this.overwrite = overwrite;
    this.signature = CloudinaryUtils.generateSignature(getParams());
  }

  private Map<String, Object> getParams() {
    HashMap<String, Object> params = new HashMap<>();
    params.put("public_id", publicId);
    params.put("folder", folder);
    params.put("asset_folder", assetFolder);
    params.put("overwrite", overwrite);
    if (displayName != null) params.put("display_name", displayName);
    Map<String, Object> options = CloudinaryUtils.generateCloudinaryRequestOptions(params);
    this.timestamp = (long) options.get("timestamp");
    return options;
  }
}
