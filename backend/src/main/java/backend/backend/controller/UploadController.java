package backend.backend.controller;

import backend.backend.dto.response.PutPresignedUrlResponse;
import backend.backend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    private final S3Service s3Service;

    @GetMapping("presigned-url")
    public ResponseEntity<PutPresignedUrlResponse> getPresignedUrl() {
        return ResponseEntity.ok(s3Service.generatePutPreSignedUrl());
    }
}
