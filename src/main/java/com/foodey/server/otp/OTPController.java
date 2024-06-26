package com.foodey.server.otp;

import com.foodey.server.annotation.PublicEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/otp")
@RequiredArgsConstructor
public class OTPController {

  private final OTPService otpService;

  @PostMapping("/{receiver}/validation/{otp}")
  @Operation(summary = "Validate OTP")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PublicEndpoint
  public void validate(
      @Param(value = "The ID of the OTP to validate (e.g. a phone number)")
          @PathVariable(required = true, name = "receiver")
          String receiver,
      @Param(value = "The OTP number to validate") @PathVariable(required = true, name = "otp")
          String otp,
      @Param(value = "The OTP properties payload") @RequestBody @Valid OTPProperties properties) {
    otpService.validate(receiver, otp, properties);
  }

  @PostMapping("/{receiver}")
  @Operation(summary = "Send OTP")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PublicEndpoint
  public void send(
      @Param(value = "The ID of the OTP to send (e.g. a phone number)")
          @PathVariable(required = true, name = "receiver")
          String receiver,
      @Param(value = "The OTP properties payload") @RequestBody @Valid OTPProperties properties) {

    otpService.send(receiver, properties);
  }
}
