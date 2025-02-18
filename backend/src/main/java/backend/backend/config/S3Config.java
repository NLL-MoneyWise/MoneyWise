package backend.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${aws.access-key}")
    private String accessKey;
    @Value("${aws.secret-key}")
    private String secretKey;
    @Value("${aws.s3.region}")
    private String region;

//    @Bean
//    public S3Client s3Client() {
//        return S3Client.builder()
//                .region(Region.of(region))
//                //credentialsProvider는 AWS SDK가 AWS서비스에 접근 할 때 필요한 인증 정보를 제공하는 인터페이스
//                //StaticCredentialsProvider는 정적 자격증명 제공자로 자격증명이 변경되지 않고 계속 사용됌
//                .credentialsProvider(StaticCredentialsProvider.create(
//                //AwsBasicCredentials는 AWS의 기본 인증 정보를 나타내는 클래스
//                        AwsBasicCredentials.create(accessKey, secretKey)))
//                .build();
//        }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .region(Region.of(region))
                .endpointOverride(URI.create("https://s3.ap-northeast-2.amazonaws.com"))
                .build();
    }
}
