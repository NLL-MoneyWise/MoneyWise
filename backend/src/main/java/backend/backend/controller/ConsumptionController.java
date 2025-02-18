package backend.backend.controller;

import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.dto.consumption.response.ConsumptionsSaveResponse;
import backend.backend.dto.consumption.response.ConsumptionsSummaryResponse;
import backend.backend.dto.consumption.response.ConsumptionsYearMonthResponse;
import backend.backend.dto.consumption.response.ConsumptionsYearResponse;
import backend.backend.service.ConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumptions")
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @PostMapping("/save")
    public ResponseEntity<ConsumptionsSaveResponse> consumptionSave(@AuthenticationPrincipal String email
            ,@RequestBody ConsumptionsSaveRequest request) {
        consumptionService.save(email, request);
        ConsumptionsSaveResponse consumptionsSaveResponse = ConsumptionsSaveResponse.builder()
                .message("소비내역 저장이 완료되었습니다.").build();
        return ResponseEntity.ok(consumptionsSaveResponse);
    }

    @GetMapping("/summary")
    public ResponseEntity<ConsumptionsSummaryResponse> consumptionSummary(@AuthenticationPrincipal String email) {
        ConsumptionsSummaryResponse response = ConsumptionsSummaryResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategory(email))
                .totalAmount(consumptionService.getTotalAmountByEmail(email))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemName(email))
                .message("전체 기간 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{year}")
    public ResponseEntity<ConsumptionsYearResponse> consumptionYear(@AuthenticationPrincipal String email, @PathVariable("year") Long year) {
        ConsumptionsYearResponse response = ConsumptionsYearResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategoryAndYear(email, year))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemNameAndYear(email, year))
                .totalAmount(consumptionService.getTotalAmountByEmailAndYear(email, year))
                .message(year + "년도의 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<ConsumptionsYearMonthResponse> consumptionYearMonth(@AuthenticationPrincipal String email, @PathVariable("year") Long year, @PathVariable("month") Long month) {
        ConsumptionsYearMonthResponse response = ConsumptionsYearMonthResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategoryAndYearAndMonth(email, year, month))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemNameAndYearAndMonth(email, year, month))
                .totalAmount(consumptionService.getTotalAmountByEmailAndYearAndMonth(email, year, month))
                .message(year + "년 " + month + "월 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }
}
