package backend.backend.service;

import backend.backend.dto.response.PreSignedUrlResponse;
import backend.backend.exception.PresignedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
//    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public PreSignedUrlResponse generatePreSignedUrl() {
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

            return PreSignedUrlResponse.builder()
                    .preSignedUrl(presignedUrl)
                    .accessUrl(accessUrl)
                    .build();
        } catch (S3Exception e) {
            System.err.println("\n=== S3 에러 발생 ===");
            System.err.println("에러 메시지: " + e.getMessage());
            System.err.println("에러 코드: " + e.awsErrorDetails().errorCode());
            throw new PresignedException("S3 URL 생성 실패: " + e.getMessage());
        }
    }

    private String generateUniqueFileName() {
        return UUID.randomUUID().toString() + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        //yyyyMMddHHmmss의 형식은 20240116212530이다. 이는 2024년 1월 16일 21시 25분 30초를 뜻한다.
    }
}
