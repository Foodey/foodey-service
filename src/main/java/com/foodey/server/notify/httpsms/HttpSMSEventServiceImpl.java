package com.foodey.server.notify.httpsms;

import com.foodey.server.auth.enums.TokenType;
import com.foodey.server.auth.jwt.JwtService;
import com.foodey.server.exceptions.InvalidTokenRequestException;
import com.foodey.server.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HttpSMSEventServiceImpl implements HttpSMSEventService {

  @Value("${foodey.sms.httpsms.secret-key}")
  private String SECRET_KEY;

  private final JwtService jwtService;

  @Override
  public void handleMessagePhoneReceived(HttpServletRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handleMessagePhoneReceived'");
  }

  @Override
  public void handleMessagePhoneSent(HttpServletRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handleMessagePhoneSent'");
  }

  @Override
  public void handleMessagePhoneDelivered(HttpServletRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handleMessagePhoneDelivered'");
  }

  @Override
  public void handleMessagePhoneFailed(HttpServletRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handleMessagePhoneFailed'");
  }

  @Override
  public void handleMessageSendExpired(HttpServletRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handleMessageSendExpired'");
  }

  @Override
  public void handlePhoneHeartbeatOffline(HttpServletRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handlePhoneHeartbeatOffline'");
  }

  @Override
  public void handlePhoneHeartbeatOnline(HttpServletRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handlePhoneHeartbeatOnline'");
  }

  @Override
  public void validateJWT(String jwt) {
    String signingKey = JwtUtils.extractSubject(jwt, SECRET_KEY);

    if (!signingKey.equals(SECRET_KEY)) {
      log.warn("Invalid JWT token in request header for verify request from httpSMS server");
      throw new InvalidTokenRequestException(
          TokenType.BEARER, jwt, "Token send by httpSMS server is invalid");
    }
  }

  @Override
  public void listenEvent(String eventType, Object payload) {
    log.info("Received event from httpSMS server: {}", eventType);
  }
}
