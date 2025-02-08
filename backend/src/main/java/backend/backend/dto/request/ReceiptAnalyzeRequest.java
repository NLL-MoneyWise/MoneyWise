package backend.backend.dto.request;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptAnalyzeRequest {
    private String accessUrl;
}
