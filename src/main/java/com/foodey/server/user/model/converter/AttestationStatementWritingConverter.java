package com.foodey.server.user.model.converter;

import com.webauthn4j.converter.util.CborConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.attestation.statement.AttestationStatement;
import com.webauthn4j.util.Base64UrlUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class AttestationStatementWritingConverter
    implements Converter<AttestationStatement, String> {

  private final CborConverter cborConverter;

  public AttestationStatementWritingConverter(ObjectConverter objectConverter) {
    this.cborConverter = objectConverter.getCborConverter();
  }

  @Override
  public String convert(AttestationStatement value) {
    AttestationStatementSerializationContainer container =
        new AttestationStatementSerializationContainer(value);
    return Base64UrlUtil.encodeToString(cborConverter.writeValueAsBytes(container));
  }
}
