package com.foodey.server.user.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.webauthn4j.converter.util.JsonConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.extension.client.RegistrationExtensionClientOutput;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class ClientExtensionsReadingConverter
    implements Converter<String, Map<String, RegistrationExtensionClientOutput>> {

  private final JsonConverter jsonConverter;

  public ClientExtensionsReadingConverter(ObjectConverter objectConverter) {
    this.jsonConverter = objectConverter.getJsonConverter();
  }

  @Override
  public Map<String, RegistrationExtensionClientOutput> convert(String source) {
    return jsonConverter.readValue(
        source, new TypeReference<Map<String, RegistrationExtensionClientOutput>>() {});
  }
}
