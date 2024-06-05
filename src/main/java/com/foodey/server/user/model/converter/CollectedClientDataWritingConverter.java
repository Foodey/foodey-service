package com.foodey.server.user.model.converter;

import com.webauthn4j.converter.CollectedClientDataConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.client.CollectedClientData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class CollectedClientDataWritingConverter implements Converter<CollectedClientData, String> {

  private final CollectedClientDataConverter converter;

  public CollectedClientDataWritingConverter(ObjectConverter objectConverter) {
    this.converter = new CollectedClientDataConverter(objectConverter);
  }

  @Override
  public String convert(CollectedClientData source) {
    if (source == null) return null;
    return converter.convertToBase64UrlString(source);
  }
}
