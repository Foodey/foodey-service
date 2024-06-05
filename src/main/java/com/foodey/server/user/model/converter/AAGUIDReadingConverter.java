package com.foodey.server.user.model.converter;

import com.webauthn4j.data.attestation.authenticator.AAGUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class AAGUIDReadingConverter implements Converter<byte[], AAGUID> {

  @Override
  public AAGUID convert(byte[] source) {
    return new AAGUID(source);
  }
}
