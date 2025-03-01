package backend.backend.dto.receipt.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetPresignedUrlResponse {
    private String preSignedUrl;
    private String message;
}
