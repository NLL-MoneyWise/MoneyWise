package backend.backend.controller;

import backend.backend.service.IncomeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Income")
@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;

}
