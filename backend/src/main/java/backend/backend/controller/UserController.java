package backend.backend.controller;

import backend.backend.dto.user.response.UserFixedCostUpdateResponse;
import backend.backend.dto.user.response.UserIncomeSaveAndUpdateResponse;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.service.UserService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 정보 관련", description = "소득액, 고정 지출 등의 정보를 관리")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(summary = "소득액 변경", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소득액이 변경되었습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserIncomeSaveAndUpdateResponse.class),
            examples = @ExampleObject("""
                    {
                    "message": "소득액이 변경되었습니다."
                    }
                    """))),

            @ApiResponse(responseCode = "500", description = "소득액 변경에 실패했습니다",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject("""
                    {
                    "typeName": "DATABASE_ERROR",
                    "message": "소득액 변경에 실패했습니다."
                    }
                    """)))
    })
    @PutMapping("/income/{income}")
    public ResponseEntity<UserIncomeSaveAndUpdateResponse> incomeUpdate(@AuthenticationPrincipal String email, @PathVariable Long income) {
        userService.incomeUpdate(email, income);
        UserIncomeSaveAndUpdateResponse response = new UserIncomeSaveAndUpdateResponse();
        response.setMessage("소득액이 변경되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "고정 지출액 변경", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 지출액이 변경되었습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserFixedCostUpdateResponse.class),
            examples = @ExampleObject("""
                    {
                    "message": "고정 지출액이 변경되었습니다."
                    }
                    """))),

            @ApiResponse(responseCode = "500", description = "고정 지출액 변경에 실패했습니다",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                    {
                    "typeName": "DATABASE_ERROR",
                    "message": "고정 지출액 변경에 실패했습니다."
                    }
                    """)))
    })
    @PutMapping("/fixed-cost/{fixed_cost}")
    public ResponseEntity<UserFixedCostUpdateResponse> fixedCostUpdate(@AuthenticationPrincipal String email, @PathVariable Long fixed_cost){
        userService.fixedCostUpdate(email, fixed_cost);

        UserFixedCostUpdateResponse response = new UserFixedCostUpdateResponse();
        response.setMessage("고정 지출액이 변경되었습니다.");
        return ResponseEntity.ok(response);
    }
}
