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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsumptionService {
    private final ConsumptionRepository consumptionRepository;

    public void save(String email, ConsumptionsSaveRequest request) {
        Map<String, Long> categoryMap = getCategoryMap();

        try {
            //request의 date는 String이므로 localDate로 변환 필요
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.parse(request.getDate(), dateTimeFormatter);

            //공통 입력 사항
            Consumption consumption = Consumption.builder()
                    .consumption_date(localDate)
                    .receipt_id(request.getReceiptId())
                    .email(email)
                    .storeName(request.getStoreName())
                    .build();

            for (ConsumptionItem item : request.getItems()) {
                Long categoryId = categoryMap.get(item.getCategory());

                consumption.setAmount(item.getAmount());
                consumption.setCategory_id(categoryId);
                consumption.setItem_name(item.getName());
                consumption.setQuantity(item.getQuantity());
                consumptionRepository.save(consumption);
            }
        } catch (DataAccessException e) {
            throw new DatabaseException(
                    "소비 저장 중 오류가 발생했습니다." + e.getMessage());
        } catch (NullPointerException e) {
            throw new ValidationException(
                    "아이템이 비어있습니다." + e.getMessage());
        }
    }

    private static Map<String, Long> getCategoryMap() {
        Map<String, Long> categoryMap = new HashMap<>();
        categoryMap.put("문구", 1L);
        categoryMap.put("식품", 2L);
        categoryMap.put("음료", 3L);
        categoryMap.put("기타", 4L);
        categoryMap.put("생활용품", 5L);
        categoryMap.put("패션/의류", 6L);
        categoryMap.put("건강/의약품", 7L);
        categoryMap.put("미용/화장품", 8L);
        categoryMap.put("전자기기", 9L);
        categoryMap.put("교통/주유", 10L);
        categoryMap.put("서비스", 11L);
        categoryMap.put("취미/여가", 12L);
        categoryMap.put("반려동물", 13L);
        categoryMap.put("유아/아동", 14L);
        return categoryMap;
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
