package backend.backend.scheduler;

import backend.backend.domain.FixedIncome;
import backend.backend.domain.income.Income;
import backend.backend.repository.FixedIncomeRepository;
import backend.backend.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FixedIncomeScheduler {

    private final FixedIncomeRepository fixedIncomeRepository;
    private final IncomeRepository incomeRepository;

    /**
     * 매일 00:00에 실행되는 고정 소득 처리 스케줄러
     * fixedIncome의 날짜가 현재 날짜와 같거나 작다면:
     * 1. Income 테이블에 새 레코드 삽입
     * 2. FixedIncome의 date를 한 달 증가
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 00:00:00에 실행
    @Transactional
    public void processFixedIncomes() {
        log.info("고정 소득 처리 스케줄러 시작");

        try {
            LocalDate today = LocalDate.now();

            // 현재 날짜와 같거나 작은 날짜를 가진 모든 고정 소득 조회
            List<FixedIncome> targetFixedIncomes = fixedIncomeRepository.findByDateLessThanEqual(today);

            log.info("처리 대상 고정 소득 개수: {}", targetFixedIncomes.size());

            for (FixedIncome fixedIncome : targetFixedIncomes) {
                try {
                    // Income 테이블에 새 레코드 생성
                    Income income = new Income();
                    income.setEmail(fixedIncome.getEmail());
                    income.setAmount(fixedIncome.getAmount());
                    income.setName(fixedIncome.getName());
                    income.setIncome_date(fixedIncome.getDate());
                    income.setSortation("고정");   // 고정 소득으로 분류

                    // Income 저장
                    incomeRepository.save(income);

                    // FixedIncome의 날짜를 한 달 증가
                    LocalDate nextMonth = fixedIncome.getDate().plusMonths(1);
                    fixedIncome.setDate(nextMonth);

                    // FixedIncome 업데이트
                    fixedIncomeRepository.save(fixedIncome);

                    log.info("고정 소득 처리 완료 - ID: {}, 이름: {}, 다음 실행일: {}",
                            fixedIncome.getId(), fixedIncome.getName(), nextMonth);

                } catch (Exception e) {
                    log.error("고정 소득 처리 중 오류 발생 - ID: {}, 오류: {}",
                            fixedIncome.getId(), e.getMessage(), e);
                    // 개별 처리 실패는 전체 스케줄러를 중단하지 않음
                }
            }

            log.info("고정 소득 처리 스케줄러 완료 - 처리된 건수: {}", targetFixedIncomes.size());

        } catch (Exception e) {
            log.error("고정 소득 스케줄러 실행 중 전체 오류 발생: {}", e.getMessage(), e);
        }
    }

    /**
     * 테스트용 메서드 - 수동 실행 가능
     */
    public void executeManually() {
        log.info("고정 소득 스케줄러 수동 실행");
        processFixedIncomes();
    }
}