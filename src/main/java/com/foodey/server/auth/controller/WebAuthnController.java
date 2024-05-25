package com.foodey.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth/fido2")
public class WebAuthnController {

  // @GetMapping("/register/options")
  // public PublicKeyCredentialCreationOptions getRegistrationOptions() {
  //   // Tạo và trả về các tùy chọn đăng ký
  // }

  // @PostMapping("/register")
  // public void register(@RequestBody PublicKeyCredentialCreationResponse response) {
  //   // Xử lý phản hồi đăng ký và lưu khóa công khai
  // }

  // @GetMapping("/authenticate/options")
  // public PublicKeyCredentialRequestOptions getAuthenticationOptions() {
  //   // Tạo và trả về các tùy chọn xác thực
  // }

  // @PostMapping("/authenticate")
  // public void authenticate(@RequestBody PublicKeyCredentialRequestResponse response) {
  //   // Xác minh phản hồi xác thực
  // }
}
