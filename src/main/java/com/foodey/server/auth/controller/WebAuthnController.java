package com.foodey.server.auth.controller;

import com.foodey.server.annotation.PublicEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@PublicEndpoint
@RequestMapping("/api/v1/auth/webauthn")
public class WebAuthnController {
  // Bước 1: Khởi tạo đăng ký

  // Client: Gửi yêu cầu đăng ký đến server.
  // Server: Tạo PublicKeyCredentialCreationOptions bao gồm challenge, thông tin về người dùng,
  // thông tin về relying party (RP), và các thông số khác. Sau đó gửi các tùy chọn này lại cho
  // client.
  // Bước 2: Tạo thông tin xác thực

  // Client: Trình bày PublicKeyCredentialCreationOptions cho authenticator.
  // Authenticator: Tạo cặp khóa công khai/riêng tư, đăng ký khóa công khai, và gửi lại attestation
  // (chứng thực) về client.
  // Client: Gửi lại attestation tới server.
  // Bước 3: Xác minh và lưu trữ thông tin đăng ký

  // Server: Xác minh attestation, kiểm tra tính hợp lệ của thông tin, và nếu thành công, lưu trữ
  // khóa công khai và các thông tin liên quan khác.
  // 2. Giai đoạn Xác thực
  // Bước 1: Khởi tạo xác thực

  // Client: Gửi yêu cầu xác thực tới server.
  // Server: Tạo PublicKeyCredentialRequestOptions bao gồm challenge và gửi lại cho client.
  // Bước 2: Tạo phản hồi xác thực

  // Client: Trình bày PublicKeyCredentialRequestOptions cho authenticator.
  // Authenticator: Ký challenge bằng khóa riêng tư và gửi lại assertion (bằng chứng) về client.
  // Client: Gửi lại assertion tới server.
  // Bước 3: Xác minh và hoàn tất xác thực

  // Server: Xác minh assertion, kiểm tra tính hợp lệ của thông tin, và nếu thành công, hoàn tất quá
  // trình xác thực.

}
