package backend.backend.controller;

import backend.backend.dto.upload.response.PutPresignedUrlResponse;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Image Upload Presigned Url")
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {
    private final S3Service s3Service;

    @Operation(summary = "이미지 업로드용 Presigned url발급", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PresignedUrl이 생성되었습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PutPresignedUrlResponse.class),
            examples = @ExampleObject("""
                    {
                    "presignedUrl": "https://nll-moneywise-receipt.s3.ap-northeast-2.amazonaws.com/60366bbb-b260-4f08-8c73-c16e0f974766_20250123222603?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250123T132603Z&X-Amz-SignedHeaders=host&X-Amz-Expires=600&X-Amz-Credential=AKIAYXWBOESLONZESYEU%2F20250123%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=114cba308fab297047ad577cbd997ba89b9fdbee78f476f4f2e4e0e1b8fb14b7",
                    "accessUrl": "60366bbb-b260-4f08-8c73-c16e0f974766_20250123222603",
                    "message": "PresignedUrl이 생성되었습니다."
                    }
                    """))),
            @ApiResponse(responseCode = "502", description = "S3 URL 생성 실패",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject("""
                    {
                    "typeName": "BAD_GATEWAY_ERROR",
                    "message": "S3 URL 생성 실패"
                    }
                    """)))
    })
    @GetMapping("presigned-url")
    public ResponseEntity<PutPresignedUrlResponse> getPutPresignedUrl() {
        PutPresignedUrlResponse response = s3Service.generatePutPreSignedUrl();
        response.setMessage("PresignedUrl이 생성되었습니다.");
        return ResponseEntity.ok(response);
    }
}
