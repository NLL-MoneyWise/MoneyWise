package backend.backend.service;

import backend.backend.dto.response.PreSignedUrlResponse;
import backend.backend.exception.PresignedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

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

            //AWS에 PresignedUrl을 요청하기 위한 설정
            //GetObjectPresignRequest는 설정정보를 담는 객체
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))  //URL유효시간 10분
                    //getObjectRequest는 버킷명, 파일명 등 상세 정보를 담는 객체
                    .getObjectRequest(GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(accessUrl)
                            .build())
                    .build();

            //S3Presigner가 AWS로 부터 받은 response를 저장하므로 presignedRequest는 개발자의 입장에서는 AWS에서 받은 response라고 볼 수 있다.
            PresignedGetObjectRequest presignedRequest =
                   s3Presigner.presignGetObject(presignRequest); //Presigner를 사용하여 presignedUrl생성

//        PreSignedUrlResponse response = new PreSignedUrlResponse();
//        response.setPreSignedUrl(presignedRequest.url().toString());
//        response.setAccessUrl(accessUrl);

            return PreSignedUrlResponse.builder()
                    .preSignedUrl(presignedRequest.url().toString())
                    .accessUrl(accessUrl)
                    .build();
        } catch (S3Exception e) {
            throw new PresignedException("S3 URL 생성 실패");
        }
    }

    private String generateUniqueFileName() {
        return UUID.randomUUID().toString() + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        //yyyyMMddHHmmss의 형식은 20240116212530이다. 이는 2024년 1월 16일 21시 25분 30초를 뜻한다.
    }
}
