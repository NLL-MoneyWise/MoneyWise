package backend.backend.dto.facade.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FacadeReceiptProcessRequest {
    private String accessUrl;
}
