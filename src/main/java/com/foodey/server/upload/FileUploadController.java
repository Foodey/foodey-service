// package com.foodey.server.upload;

// import com.foodey.server.annotation.PublicEndpoint;
// import com.foodey.server.utils.ConsoleUtils;
// import java.io.IOException;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// @RestController
// @RequestMapping("/api/v1/files")
// @PublicEndpoint
// public class FileUploadController {

//   private final CloudinaryService cloudinaryService;

//   public FileUploadController(CloudinaryService cloudinaryService) {
//     this.cloudinaryService = cloudinaryService;
//   }

//   @PostMapping("/generate-signature")
//   public ResponseEntity<?> generateSignature() {
//     ConsoleUtils.prettyPrint("test");

//     return ResponseEntity.ok(cloudinaryService.generateSignature("test", "test"));
//   }

//   // @PostMapping("/upload/image")
//   // public ResponseEntity<?> uploadImage(
//   //     @RequestParam("file") MultipartFile file, @RequestParam("folder") String folderName)
//   //     throws IOException {
//   //   return ResponseEntity.ok(cloudinaryService.uploadFile(file, folderName, ));
//   // }

//   @PostMapping("/upload/video")
//   public ResponseEntity<?> uploadVideo(
//       @RequestParam("file") MultipartFile file, @RequestParam("folder") String folderName)
//       throws IOException {
//     return ResponseEntity.ok(cloudinaryService.uploadVideo(file, folderName));
//   }
// }
