package com.foodey.server.user.model.converter;

import com.webauthn4j.converter.util.JsonConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.extension.client.CredentialPropertiesExtensionClientOutput;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class CredentialPropertiesExtensionClientOutputWritingConverter
    implements Converter<Map<String, CredentialPropertiesExtensionClientOutput>, String> {

  private final JsonConverter jsonConverter;

  public CredentialPropertiesExtensionClientOutputWritingConverter(
      ObjectConverter objectConverter) {
    this.jsonConverter = objectConverter.getJsonConverter();
  }

  @Override
  public String convert(Map<String, CredentialPropertiesExtensionClientOutput> source) {
    return jsonConverter.writeValueAsString(source);
  }
}
