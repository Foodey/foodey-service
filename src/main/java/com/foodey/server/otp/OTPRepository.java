package com.foodey.server.otp;

import java.time.Instant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OTPRepository extends MongoRepository<OTP, String> {

  boolean existsByIdAndOtpAndExpiredAtAfter(String id, String otp, Instant now);
}
