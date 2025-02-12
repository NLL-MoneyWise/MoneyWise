package backend.backend.dto.auth.request;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptAnalyzeRequest {
    private String accessUrl;
}
