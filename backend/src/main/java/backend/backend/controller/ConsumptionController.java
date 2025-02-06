package backend.backend.controller;

import backend.backend.dto.request.ConsumptionsSaveRequest;
import backend.backend.dto.response.ConsumptionsSaveResponse;
import backend.backend.service.ConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumptions")
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @PostMapping("/save")
    public ResponseEntity<ConsumptionsSaveResponse> consumptionSave(@AuthenticationPrincipal String email
            ,@RequestBody ConsumptionsSaveRequest request) {
        consumptionService.save(email, request);
        ConsumptionsSaveResponse consumptionsSaveResponse = new ConsumptionsSaveResponse();
        consumptionsSaveResponse.setStatus("Success");
        return ResponseEntity.ok(consumptionsSaveResponse);
    }

}
