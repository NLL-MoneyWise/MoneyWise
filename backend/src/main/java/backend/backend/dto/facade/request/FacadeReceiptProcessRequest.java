package backend.backend.dto.facade.request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class FacadeReceiptProcessRequest {
    private String accessUrl;
}
