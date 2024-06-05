package com.foodey.server.user.model.converter;

import com.webauthn4j.converter.util.CborConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.attestation.statement.AttestationStatement;
import com.webauthn4j.util.Base64UrlUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class AttestationStatementReadingConverter
    implements Converter<String, AttestationStatement> {

  private final CborConverter cborConverter;

  public AttestationStatementReadingConverter(ObjectConverter objectConverter) {
    this.cborConverter = objectConverter.getCborConverter();
  }

  @Override
  public AttestationStatement convert(String value) {
    byte[] data = Base64UrlUtil.decode(value);
    AttestationStatementSerializationContainer container =
        cborConverter.readValue(data, AttestationStatementSerializationContainer.class);
    return container.getAttestationStatement();
  }
}
