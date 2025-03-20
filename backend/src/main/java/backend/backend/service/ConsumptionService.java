package backend.backend.service;

import backend.backend.domain.Consumption;
import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.ConsumptionItem;
import backend.backend.dto.consumption.model.TopExpense;
import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.ValidationException;
import backend.backend.repository.ConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsumptionService {
    private final ConsumptionRepository consumptionRepository;

    public void save(String email, ConsumptionsSaveRequest request) {
        try {
            //request의 date는 String이므로 localDate로 변환 필요
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.parse(request.getDate(), dateTimeFormatter);

            //공통 입력 사항
            Consumption consumption = Consumption.builder()
                    .consumption_date(localDate)
                    .receipt_id(request.getReceiptId())
                    .email(email)
                    .build();

            for (ConsumptionItem item : request.getItems()) {
                Long categoryId;
                //Request는 카테고리를 String 으로 주지만 Consumption은 Id로 입력해야함
                if (item.getCategory().equals("문구")) {
                    categoryId = 1L;
                } else if (item.getCategory().equals("식품")) {
                    categoryId = 2L;
                } else if (item.getCategory().equals("음료")) {
                    categoryId = 3L;
                } else {
                    categoryId = 4L;
                }

                consumption.setAmount(item.getAmount());
                consumption.setCategory_id(categoryId);
                consumption.setItem_name(item.getName());
                consumptionRepository.save(consumption);
            }
        } catch (DataAccessException e) {
            throw new DatabaseException(
                    "데이터베이스 오류가 발생했습니다." + e.getMessage());
        } catch (NullPointerException e) {
            throw new ValidationException(
                    "아이템이 비어있습니다." + e.getMessage());
        }
    }

    public Long getTotalAmountByEmail(String email) {
        return consumptionRepository.sumAmountByEmail(email).orElse(0L);
    }

    public Long getTotalAmountByEmailAndYear(String email, Long year) {
        return consumptionRepository.sumAmountByEmailAndYear(email, year).orElse(0L);
    }

    public Long getTotalAmountByEmailAndYearAndMonth(String email, Long year, Long month) {
        return consumptionRepository.sumAmountByEmailAndYearAndMonthToQuerydsl(email, year, month).orElse(0L);
    }

    public List<ByCategory> getTotalAmountByEmailAndCategory(String email) {
        List<ByCategory> result = consumptionRepository.findByCategoryAndEmail(email, null, null);
        return result != null ? result : Collections.emptyList();
    }

    public List<ByCategory> getTotalAmountByEmailAndCategoryAndYear(String email, Long year) {
        List<ByCategory> result = consumptionRepository.findByCategoryAndEmail(email, year, null);
        return result;
    }

    public List<ByCategory> getTotalAmountByEmailAndCategoryAndYearAndMonth(String email, Long year, Long month) {
        List<ByCategory> result = consumptionRepository.findByCategoryAndEmail(email, year, month);
        return result;
    }

    public List<TopExpense> getMaxAmountByEmailAndItemName(String email) {
        List<TopExpense> result = consumptionRepository.findTopExpenseByEmail(email, null, null);
        return result != null ? result : Collections.emptyList();
    }

    public List<TopExpense> getMaxAmountByEmailAndItemNameAndYear(String email, Long year) {
        List<TopExpense> result = consumptionRepository.findTopExpenseByEmail(email, year, null);
        return result;
    }

    public List<TopExpense> getMaxAmountByEmailAndItemNameAndYearAndMonth(String email, Long year, Long month) {
        List<TopExpense> result = consumptionRepository.findTopExpenseByEmail(email, year, month);
        return result;
    }
}
