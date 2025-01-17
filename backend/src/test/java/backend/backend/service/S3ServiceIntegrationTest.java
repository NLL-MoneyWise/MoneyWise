package backend.backend.service;

import backend.backend.dto.response.PreSignedUrlResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class S3ServiceIntegrationTest {
    @Autowired
    private S3Service s3Service;  // 실제 S3Service 사용

    @Test
    @DisplayName("실제 Pre-signed URL 생성 테스트")
    void generatePreSignedUrlTest() {
        // when
        PreSignedUrlResponse response = s3Service.generatePreSignedUrl(); //presignedUrl생성
        // then
        assertNotNull(response.getPreSignedUrl());
        assertTrue(response.getPreSignedUrl().startsWith("https://"));
        assertNotNull(response.getAccessUrl());

        // URL 유효성 검증
        try {
            URL url = new URL(response.getPreSignedUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Pre-signed URL이 유효한지 확인 (403이 아닌지)
            //Pre-signed URL이 제대로 생성되지 않았거나, AWS 자격증명이 잘못되었거나 권한이 없는 경우 AWS S3가 403으로 응답
            assertNotEquals(403, conn.getResponseCode());
            System.out.println("presignedUrl : " + response.getPreSignedUrl());
            System.out.println("accessUrl : " + response.getAccessUrl());
        } catch (IOException e) {
            fail("Invalid URL generated");
        }
    }
}