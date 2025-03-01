package backend.backend.service;

import backend.backend.dto.upload.response.PutPresignedUrlResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3ServiceIntegrationTest {
    @Autowired
    private S3Service s3Service;  // 실제 S3Service 사용

    @Test
    @DisplayName("실제 Put-Pre-signed URL 생성 테스트")
    void generatePutPreSignedUrlTest() {
        // when
        PutPresignedUrlResponse response = s3Service.generatePutPreSignedUrl(); //presignedUrl생성
        // then
        assertNotNull(response.getPreSignedUrl());
        assertTrue(response.getPreSignedUrl().startsWith("https://"));
        assertNotNull(response.getAccessUrl());
        // URL 유효성 검증
        try {
            URL url = new URL(response.getPreSignedUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            System.out.println("presignedUrl : " + response.getPreSignedUrl());
            System.out.println("accessUrl : " + response.getAccessUrl());
            // Pre-signed URL이 유효한지 확인 (403이 아닌지)
            //Pre-signed URL이 제대로 생성되지 않았거나, AWS 자격증명이 잘못되었거나 권한이 없는 경우 AWS S3가 403으로 응답
            assertNotEquals(403, conn.getResponseCode());

        } catch (IOException e) {
            fail("Invalid URL generated");
        }
    }

    @Test
    @DisplayName("실제 Get-Cloud-Front URL 접속 테스트")
    public void testCloudFrontSignedUrl() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        String url = s3Service.generateGetSignedUrlWithCloudFront("receipt.jpeg");
        System.out.println("Testing URL: " + url);

        // 실제 URL 접근 테스트 - 신뢰할 수 없는 인증서일 경우
//        CloseableHttpClient httpClient = HttpClients.custom() //HttpClient의 사용자 정의 시작
//                        .setSSLContext(SSLContexts.custom() //SSL/TSL연결에 대한 사용자 정의 시작
//                                //null: 기본 신뢰 저장소 사용, (x509Certificates, s) -> true모든 인증서 신뢰, 보안상 위험하지만 사용해야될 경우도 존재
//                                .loadTrustMaterial(null, (x509Certificates, s) -> true)
//                                .build())
//                        .build();

        //신뢰할 수 있는 경우
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        }



        //RestTemplate사용 - s3설정이 https만 허용하는데 restTemplate는 http요청이라 사용 불가
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<byte[]> result = restTemplate.getForEntity(url, byte[].class);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("CDN Signed Url 생성 성공")
    public void generateSignedUrlWithCloudFrontSuccess() {
        System.out.println(s3Service.generateGetSignedUrlWithCloudFront("receipt.jpeg"));
    }
}