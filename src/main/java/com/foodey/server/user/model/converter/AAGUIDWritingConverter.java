package com.foodey.server.user.model.converter;

import com.webauthn4j.data.attestation.authenticator.AAGUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class AAGUIDWritingConverter implements Converter<AAGUID, byte[]> {

  @Override
  public byte[] convert(AAGUID source) {
    return source.getBytes();
  }
}
