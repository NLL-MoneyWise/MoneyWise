package backend.backend.dto.receipt.request;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptAnalyzeRequest {
    private String accessUrl;
}
