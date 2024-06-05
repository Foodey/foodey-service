package com.foodey.server.user.model.converter;

import com.webauthn4j.converter.CollectedClientDataConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.client.CollectedClientData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class CollectedClientDataReadingConverter implements Converter<String, CollectedClientData> {

  private final CollectedClientDataConverter converter;

  public CollectedClientDataReadingConverter(ObjectConverter objectConverter) {
    this.converter = new CollectedClientDataConverter(objectConverter);
  }

  @Override
  public CollectedClientData convert(String source) {
    if (source == null) return null;
    return converter.convert(source);
  }
}
