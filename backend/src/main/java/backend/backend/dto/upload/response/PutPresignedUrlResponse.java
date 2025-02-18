package backend.backend.dto.upload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//빌더는 새로운 객체를 반환한다. 내부 static클래스로 builder를 생성하므로 내가 만든 클래스가 static이 아니여도 new 연산자 없이 .builder를 사용할 수 있다.
@Builder
public class PutPresignedUrlResponse {
    private String preSignedUrl;
    private String accessUrl;
    private String message;

    public static PutPresignedUrlResponse of(String preSignedUrl, String accessUrl) {
        return PutPresignedUrlResponse.builder()
                .preSignedUrl(preSignedUrl)
                .accessUrl(accessUrl)
                .build();
    }
}
