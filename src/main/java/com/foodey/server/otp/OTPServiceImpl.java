package com.foodey.server.otp;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.foodey.server.notify.NotificationFactory;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final NotificationFactory notificationFactory;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void validate(String id, String otp, OTPType otpType) {
    if (otpRepository.get(id).orElseThrow(() -> new OTPException("OTP expires", otp)).equals(otp)) {
      otpRepository.remove(id);
      eventPublisher.publishEvent(new OTPValidatedEvent(this, id, otp, otpType));
    } else {
      throw new OTPException("Invalid OTP", otp);
    }
  }

  @Override
  @Transactional
  public String send(String notificationType, String id, OTPExpiration expiration) {
    String otp = generateOtp();
    otpRepository.put(id, otp, Duration.ofMillis(getExpirationTime(expiration)));
    notificationFactory.execute(notificationType, id, message(otp));
    return otp;
  }

  @Override
  @Transactional
  public String send(String notificationType, String id, long expiredAfterMs) {
    String otp = generateOtp();
    otpRepository.put(id, otp, Duration.ofMillis(expiredAfterMs));
    notificationFactory.execute(notificationType, id, message(otp));
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

  private String message(String otp) {
    return "Your Foodey OTP is: " + otp + ". Please do not share it with anyone.";
  }
}
