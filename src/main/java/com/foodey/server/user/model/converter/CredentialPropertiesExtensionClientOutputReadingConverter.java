package com.foodey.server.user.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.webauthn4j.converter.util.JsonConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.extension.client.CredentialPropertiesExtensionClientOutput;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class CredentialPropertiesExtensionClientOutputReadingConverter
    implements Converter<String, Map<String, CredentialPropertiesExtensionClientOutput>> {

  // MappingMongoConverter

  private final JsonConverter jsonConverter;

  public CredentialPropertiesExtensionClientOutputReadingConverter(
      ObjectConverter objectConverter) {
    this.jsonConverter = objectConverter.getJsonConverter();
  }

  @Override
  public Map<String, CredentialPropertiesExtensionClientOutput> convert(String source) {
    return jsonConverter.readValue(
        source, new TypeReference<Map<String, CredentialPropertiesExtensionClientOutput>>() {});
  }
}
