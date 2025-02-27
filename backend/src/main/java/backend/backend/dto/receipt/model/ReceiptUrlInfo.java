package backend.backend.dto.receipt.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceiptUrlInfo {
    private String accessUrl;
    private String cdnSignedUrl;
}
