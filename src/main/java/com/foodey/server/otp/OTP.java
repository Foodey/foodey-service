package com.foodey.server.otp;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.security.Key;
import java.time.Instant;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "otps")
@CompoundIndexes({
  @CompoundIndex(
      name = "id_otp_expireAt",
      def = "{'id': 1, 'otp': 1, 'expiredAt': 1}",
      unique = true,
      background = true)
})
public class OTP implements Persistable<String> {

  @Transient private static final int MAX_EXPIRED_MS = 10 * 60 * 1000;

  // can be email or phone number or any other unique identifier
  @Id private String id;

  private String otp;

  @Indexed(expireAfterSeconds = MAX_EXPIRED_MS)
  @CreatedDate
  private Instant createAt;

  private Instant expiredAt;

  public OTP(String id, long expiredAfterMs) {
    assert expiredAfterMs <= MAX_EXPIRED_MS : "Expired time should be less than " + MAX_EXPIRED_MS;

    this.id = id;
    generateOtp();
    this.expiredAt = Instant.now().plusMillis(expiredAfterMs);
  }

  public OTP(String id, String otp, long expiredAfterMs) {
    assert expiredAfterMs <= MAX_EXPIRED_MS : "Expired time should be less than " + MAX_EXPIRED_MS;

    this.id = id;
    this.otp = otp;
    this.expiredAt = Instant.now().plusMillis(expiredAfterMs);
  }

  public OTP(String id, long expiredAfterMs, Key secretKey) {
    assert expiredAfterMs <= MAX_EXPIRED_MS : "Expired time should be less than " + MAX_EXPIRED_MS;

    this.id = id;
    generateOtp(secretKey);
    this.expiredAt = Instant.now().plusMillis(expiredAfterMs);
  }

  public boolean isExpired() {
    return Instant.now().isAfter(expiredAt);
  }

  public boolean isValid(String otp) {
    return this.otp.equals(otp) && !isExpired();
  }

  public void generateOtp() {
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
      otp = totp.generateOneTimePasswordString(key, now);

    } catch (Exception e) {
      System.out.println("Error while generating OTP: " + e.getMessage());
    }
  }

  public void generateOtp(Key key) {
    try {
      final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
      final Instant now = Instant.now().plusSeconds((int) (Math.random() * 100));
      otp = totp.generateOneTimePasswordString(key, now);
    } catch (Exception e) {
      System.out.println("Error while generating OTP: " + e.getMessage());
    }
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createAt == null;
  }
}
