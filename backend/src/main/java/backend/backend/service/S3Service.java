package backend.backend.service;

import backend.backend.dto.upload.response.PutPresignedUrlResponse;
import backend.backend.exception.BadGateWayException;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
//    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.cloudfront.domain}")
    private String cloudFrontDomain;

    @Value("${aws.cloudfront.keyPairId}")
    private String keyPairId;

    public PutPresignedUrlResponse generatePutPreSignedUrl() {
        try {
            String accessUrl = generateUniqueFileName();
            System.out.println("\n=== Pre-signed URL 생성 시작 ===");
            System.out.println("버킷명: " + bucketName);
            System.out.println("파일명: " + accessUrl);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(accessUrl)
                    .build();
            //AWS에 PresignedUrl을 요청하기 위한 설정
            //PutObjectPresignRequest는 설정정보를 담는 객체
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(putObjectRequest)
                    .build();

            System.out.println("Pre-sign Request 생성 완료");
            //S3Presigner가 AWS로 부터 받은 response를 저장하므로 presignedRequest는 개발자의 입장에서는 AWS에서 받은 response라고 볼 수 있다.
            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest); //Presigner를 사용하여 presignedUrl생성

            String presignedUrl = presignedRequest.url().toString();
            System.out.println("Pre-signed URL 생성 성공!");
            System.out.println("Generated URL: " + presignedUrl);

//        PreSignedUrlResponse response = new PreSignedUrlResponse();
//        response.setPreSignedUrl(presignedRequest.url().toString());
//        response.setAccessUrl(accessUrl);

            return PutPresignedUrlResponse.builder()
                    .preSignedUrl(presignedUrl)
                    .accessUrl(accessUrl)
                    .build();
        } catch (S3Exception e) {
            System.err.println("\n=== S3 에러 발생 ===");
            System.err.println("에러 메시지: " + e.getMessage());
            System.err.println("에러 코드: " + e.awsErrorDetails().errorCode());
            throw new BadGateWayException("S3 URL 생성 실패: " + e.getMessage());
        }
    }

//    public String generateGetPreSignedUrl(String accessUrl) {
//        System.out.println("Generating GET PreSigned URL for: " + accessUrl);
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(accessUrl)
//                .build();
//
//        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(10))
//                .getObjectRequest(getObjectRequest)
//                .build();
//
//        PresignedGetObjectRequest presignedGetObjectRequest =
//                s3Presigner.presignGetObject(getObjectPresignRequest);
//
//        return presignedGetObjectRequest.url().toString();
//    }

    public String generateGetSignedUrlWithCloudFront(String accessUrl) {
        try{
            System.out.println("Cloud Front Signed Url for: " + accessUrl);

            Date expirationTime = new Date(System.currentTimeMillis() + 10 * 60 * 1000);

            PrivateKey privateKey = loadPrivateKeyFromDER();

            String signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                    cloudFrontDomain + "/" + accessUrl,
                    keyPairId,
                    privateKey,
                    expirationTime
            );

            System.out.println("Signed Url gen success");

            return signedUrl;
        }catch (Exception e) {
            System.err.println("Error generating CloudFront Signed URL: " + e.getMessage());
            throw new BadGateWayException("AWS Cloud Front 호출 실패");
        }
    }

    private static PrivateKey loadPrivateKeyFromDER() throws IOException, GeneralSecurityException {
        //jar로 패키징 되면 resource는 클래스패스 내에서 찾기 때문에 파일을 찾아갈 수 있지만 File 클래스는 절대 경로를 사용하고, jar내부에서 찾는 것이 아니기 때문에 resource를 사용해야한다.
        Resource resource = new ClassPathResource("keys/private_key.der");
        byte[] derData;

        try (InputStream inputStream = resource.getInputStream()) {
            derData = inputStream.readAllBytes();
        }

        //KeyFactory는 key를 다양한 형식간의 변환하는 기능을 제공한다.
        //여기서는 der바이너리 데이터를 읽어온 PKCS8객체를 privateKey로 만드는 과정을 보여준다.
        //PKCS#8은 개인 키를 저장하기 위한 표준 형식이며, DER은 이 형식의 바이너리 인코딩이다.
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(derData);
        //RSA알고리즘으로 작성된 keySpec을 변환하기 위해 RSA알고리즘을 처리하는 keyFactory를 Bouncy Castle제공자를 명시적으로 지정하여 연다.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA","BC");
        //실제 privateKey로 변환
        return keyFactory.generatePrivate(keySpec);
    }

    private String generateUniqueFileName() {
        return UUID.randomUUID().toString() + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        //yyyyMMddHHmmss의 형식은 20240116212530이다. 이는 2024년 1월 16일 21시 25분 30초를 뜻한다.
    }
}
