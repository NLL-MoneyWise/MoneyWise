package backend.backend.scheduler;

import backend.backend.domain.Consumption;
import backend.backend.domain.fixedCost.FixedCost;
import backend.backend.repository.ConsumptionRepository;
import backend.backend.repository.FixedCostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FixedCostScheduler {

    private final FixedCostRepository fixedCostRepository;
    private final ConsumptionRepository consumptionRepository;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void processFixedCosts() {
        LocalDate today = LocalDate.now();
        log.info("고정지출 처리 시작: {}", today);

        // 오늘 날짜가 지난 고정지출들 조회
        List<FixedCost> dueFixedCosts = fixedCostRepository.findByFixedCostDateBefore(today);

        for (FixedCost fixedCost : dueFixedCosts) {
            try {
                // 1. Consumption 테이블에 실제 지출 기록 저장
                Consumption consumption = new Consumption();
                consumption.setEmail(fixedCost.getEmail());
                consumption.setCategory_id(fixedCost.getCategoryId());
                consumption.setName(fixedCost.getName());
                consumption.setAmount(fixedCost.getAmount());
                consumption.setConsumption_date(fixedCost.getFixedCostDate()); // 원래 예정일로 기록
                consumption.setQuantity(1L); // 기본값

                consumptionRepository.save(consumption);
                log.info("고정지출 소비 기록 생성: {} - {}", fixedCost.getName(), fixedCost.getAmount());

                // 2. FixedCost의 날짜를 다음 달로 업데이트
                LocalDate nextMonth = calculateNextMonth(fixedCost.getFixedCostDate());
                fixedCost.setFixedCostDate(nextMonth);
                fixedCostRepository.save(fixedCost);
                log.info("고정지출 날짜 업데이트: {} -> {}", fixedCost.getFixedCostDate(), nextMonth);

            } catch (Exception e) {
                log.error("고정지출 처리 중 오류 발생: {}", fixedCost.getName(), e);
            }
        }

        log.info("고정지출 처리 완료: {} 건 처리", dueFixedCosts.size());
    }

    /**
     * 다음 달 날짜 계산
     * 12월이면 다음 년도 1월로, 아니면 다음 달로
     */
    private LocalDate calculateNextMonth(LocalDate currentDate) {
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        int day = currentDate.getDayOfMonth();

        if (currentMonth == 12) {
            // 12월이면 다음 년도 1월로
            return LocalDate.of(currentYear + 1, 1, day);
        } else {
            // 아니면 다음 달로
            return LocalDate.of(currentYear, currentMonth + 1, day);
        }
    }
}