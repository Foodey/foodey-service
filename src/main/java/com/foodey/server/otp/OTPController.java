package com.foodey.server.otp;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/otp")
@RequiredArgsConstructor
public class OTPController {

  private final OTPService otpService;

  @PostMapping("/{id}/validation/{otp}")
  @Operation(summary = "Validate OTP")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void validate(
      @Param(value = "The ID of the OTP to validate (e.g. a phone number)")
          @PathVariable(required = true, name = "id")
          String id,
      @Param(value = "The OTP number to validate") @PathVariable(required = true, name = "otp")
          String otp) {
    otpService.validate(id, otp);
  }
}
