package backend.backend.dto.receipt.response;

import backend.backend.dto.receipt.model.ReceiptUrlInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetAllReceiptPresignedUrlResponse {
    private List<ReceiptUrlInfo> allReceiptUrlInfo;
    private String message;
}
