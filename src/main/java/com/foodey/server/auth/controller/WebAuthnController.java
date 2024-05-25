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

  // private final WebAuthnManager webAuthnManager;

  // @PostMapping("/register/start")
  // public PublicKeyCredentialCreationOptions startRegistration(
  //     @RequestBody Fido2RegistrationRequest request) {
  //   // Xử lý logic đăng ký người dùng mới
  //   // Tạo và trả về PublicKeyCredentialCreationOptions cho client
  //   //
  //   return null;
  // }

  // @PostMapping("/authenticate")
  // public PublicKeyCredentialRequestOptions startAuthentication(
  //     @RequestBody StartAuthenticationRequest request) {
  //   // Xử lý logic xác thực người dùng
  //   // Tạo và trả về PublicKeyCredentialRequestOptions cho client
  // }

  // @PostMapping("/register/finish")
  // public void finishRegistration(@RequestBody FinishRegistrationRequest request) {
  //   // Xử lý logic hoàn tất đăng ký
  //   // Xác thực phản hồi từ thiết bị xác thực và lưu trữ thông tin người dùng
  // }

  // @PostMapping("/authenticate/finish")
  // public void finishAuthentication(@RequestBody FinishAuthenticationRequest request) {
  //   // Xử lý logic hoàn tất xác thực
  //   // Xác thực phản hồi từ thiết bị xác thực
  // }
}
