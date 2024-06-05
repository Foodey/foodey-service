package com.foodey.server.user.model.converter;

import com.webauthn4j.converter.util.JsonConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.extension.client.RegistrationExtensionClientOutput;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class ClientExtensionsWritingConverter
    implements Converter<Map<String, RegistrationExtensionClientOutput>, String> {

  private final JsonConverter jsonConverter;

  public ClientExtensionsWritingConverter(ObjectConverter objectConverter) {
    this.jsonConverter = objectConverter.getJsonConverter();
  }

  @Override
  public String convert(Map<String, RegistrationExtensionClientOutput> source) {
    return jsonConverter.writeValueAsString(source);
  }
}
