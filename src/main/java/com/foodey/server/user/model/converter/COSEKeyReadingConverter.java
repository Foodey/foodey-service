package com.foodey.server.user.model.converter;

import com.webauthn4j.converter.util.CborConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.attestation.authenticator.COSEKey;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class COSEKeyReadingConverter implements Converter<byte[], COSEKey> {

  private final CborConverter cborConverter;

  public COSEKeyReadingConverter(ObjectConverter objectConverter) {
    this.cborConverter = objectConverter.getCborConverter();
  }

  @Override
  public COSEKey convert(byte[] source) {
    return cborConverter.readValue(source, COSEKey.class);
  }
}
