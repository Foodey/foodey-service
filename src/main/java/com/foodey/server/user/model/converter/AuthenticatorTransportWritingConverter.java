package com.foodey.server.user.model.converter;

import com.webauthn4j.data.AuthenticatorTransport;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class AuthenticatorTransportWritingConverter
    implements Converter<AuthenticatorTransport, String> {

  @Override
  public String convert(AuthenticatorTransport source) {
    return source.getValue();
  }
}
