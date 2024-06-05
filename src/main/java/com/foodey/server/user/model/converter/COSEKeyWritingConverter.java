package com.foodey.server.user.model.converter;

import com.webauthn4j.converter.util.CborConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.attestation.authenticator.COSEKey;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class COSEKeyWritingConverter implements Converter<COSEKey, byte[]> {

  private final CborConverter cborConverter;

  public COSEKeyWritingConverter(ObjectConverter objectConverter) {
    this.cborConverter = objectConverter.getCborConverter();
  }

  @Override
  public byte[] convert(COSEKey source) {
    return cborConverter.writeValueAsBytes(source);
  }
}
