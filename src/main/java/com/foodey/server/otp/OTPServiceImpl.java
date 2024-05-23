package com.foodey.server.otp;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPServiceImpl implements OTPService {

  @Value("${foodey.otp.expiration.medium}")
  private long MEDIUM_EXPIRATION_TIME;

  @Value("${foodey.otp.expiration.fast}")
  private long FAST_EXPIRATION_TIME;

  @Value("${foodey.otp.expiration.long}")
  private long LONG_EXPIRATION_TIME;

  @Value("${foodey.otp.expiration.short}")
  private long SHORT_EXPIRATION_TIME;

  private final OTPRepository otpRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void validate(String receiver, String otp, OTPProperties properties) {
    if (otpRepository
        .get(receiver)
        .orElseThrow(() -> new OTPException("OTP expires", otp))
        .equals(otp)) {
      otpRepository.remove(receiver);
      eventPublisher.publishEvent(new OTPValidatedEvent(this, receiver, otp, properties));
    } else {
      throw new OTPException("Invalid OTP", otp);
    }
  }

  @Override
  public String send(String receiver, OTPProperties properties) {
    return send(receiver, properties, (otp) -> message(otp, properties.getOtpType()));
  }

  @Override
  public String send(
      String receiver, OTPProperties properties, Function<String, String> messageFunction) {

    String otp = generateOtp();
    if (properties.getOtpExpiration() == null)
      otpRepository.put(receiver, otp, Duration.ofMillis(properties.getTtl()));
    else
      otpRepository.put(
          receiver, otp, Duration.ofMillis(getExpirationTime(properties.getOtpExpiration())));

    eventPublisher.publishEvent(
        new OTPSendingEvent(this, receiver, otp, messageFunction.apply(otp), properties));
    return otp;
  }

  @Override
  public String generateOtp() {
    try {
      final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
      final Key key;
      {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
        // Key length should match the length of the HMAC output (160 bits for SHA-1, 256 bits
        // for SHA-256, and 512 bits for SHA-512). Note that while Mac#getMacLength() returns a
        // length in _bytes,_ KeyGenerator#init(int) takes a key length in _bits._
        final int macLengthInBytes = Mac.getInstance(totp.getAlgorithm()).getMacLength();
        keyGenerator.init(macLengthInBytes * 8);
        key = keyGenerator.generateKey();
      }
      final Instant now = Instant.now().plusSeconds((int) (Math.random() * 100));
      return totp.generateOneTimePasswordString(key, now);

    } catch (Exception e) {
      log.error("Error while generating OTP: " + e.getMessage());
      throw new OTPException(e.getMessage(), null);
    }
  }

  private String message(String otp, OTPType type) {
    switch (type) {
      case USER_REGISTRATION:
        return "Welcome to Foodey! Your OTP is: " + otp + ". Please do not share it with anyone.";
      default:
        return "Your Foodey OTP is: " + otp + ". Please do not share it with anyone.";
    }
  }

  private String message(String otp) {
    return "Your Foodey OTP is: " + otp + ". Please do not share it with anyone.";
  }

  private long getExpirationTime(OTPExpiration expiration) {
    switch (expiration) {
      case FAST:
        return FAST_EXPIRATION_TIME;
      case MEDIUM:
        return MEDIUM_EXPIRATION_TIME;
      case LONG:
        return LONG_EXPIRATION_TIME;
      case SHORT:
        return SHORT_EXPIRATION_TIME;
      default:
        return SHORT_EXPIRATION_TIME;
    }
  }
}
