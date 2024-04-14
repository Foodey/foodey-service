package com.foodey.server.otp;

import com.foodey.server.exceptions.TooManyRequestsException;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

  @Value("${foodey.otp.expiration.medium}")
  private long MEDIUM_EXPIRATION_TIME;

  @Value("${foodey.otp.expiration.fast}")
  private long FAST_EXPIRATION_TIME;

  @Value("${foodey.otp.resend-interval}")
  private long RESEND_INTERVAL;

  private final OTPRepository otpRepository;

  @Override
  public OTP generate(String id) {
    return generate(id, MEDIUM_EXPIRATION_TIME);
  }

  @Override
  public OTP generate(String id, boolean fastExpire) {
    return generate(id, fastExpire ? FAST_EXPIRATION_TIME : MEDIUM_EXPIRATION_TIME);
  }

  @Override
  public OTP generate(String id, long expiredAfterMs) {
    OTP otp = new OTP(id, expiredAfterMs);

    return otpRepository.save(otp);
  }

  @Override
  public void validate(String id, String otp) {
    if (otpRepository.existsByIdAndOtpAndExpiredAtAfter(id, otp, Instant.now()))
      otpRepository.deleteById(id);
    else throw new OTPException("OTP expired", otp);
  }

  @Override
  public void send(String id) {
    send(id, MEDIUM_EXPIRATION_TIME);
  }

  @Override
  public void send(String id, boolean fastExpire) {
    send(id, fastExpire ? FAST_EXPIRATION_TIME : MEDIUM_EXPIRATION_TIME);
  }

  @Override
  public void send(String id, long expiredAfterMs) {
    Optional<OTP> optional = otpRepository.findById(id);

    optional.ifPresentOrElse(
        o -> {
          validate_resendable(o);
          otpRepository.deleteById(id);
          generate(id, expiredAfterMs);
        },
        () -> generate(id, expiredAfterMs));
  }

  private void validate_resendable(OTP otp) {
    if (otp.getCreateAt().isAfter(Instant.now().minusMillis(RESEND_INTERVAL)))
      throw new TooManyRequestsException("Resend interval not reached");
  }
}
