package backend.backend.service;

import backend.backend.domain.Consumption;
import backend.backend.domain.Receipt;
import backend.backend.dto.common.model.Item;
import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.StoreExpense;
import backend.backend.dto.consumption.model.TopExpense;
import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.repository.ConsumptionRepository;
import backend.backend.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsumptionService {
    private final ConsumptionRepository consumptionRepository;
    private final ReceiptRepository receiptRepository;

    public void save(String email, ConsumptionsSaveRequest request) {
        Map<String, Long> categoryMap = getCategoryMap();

        try {
            //request의 date는 String이므로 localDate로 변환 필요
            LocalDate localDate = LocalDate.parse(request.getDate());

            //공통 입력 사항
            Consumption consumption = Consumption.builder()
                    .consumption_date(localDate)
                    .email(email)
                    .access_url(request.getAccess_url())
                    .storeName(request.getStoreName())
                    .build();

            Receipt receipt = receiptRepository.findById(request.getAccess_url())
                    .orElseThrow(() -> new NotFoundException("해당하는 영수증이 없습니다."));

            if (request.getItems() != null && !request.getItems().isEmpty()) {
                for (Item item : request.getItems()) {
                    Long categoryId = categoryMap.get(item.getCategory());

                    consumption.setAmount(item.getAmount());
                    consumption.setCategory_id(categoryId);
                    consumption.setItem_name(item.getName());
                    consumption.setQuantity(item.getQuantity());
                    consumptionRepository.save(consumption);
                }
            } else {
                consumption.setAmount(receipt.getTotal_amount());
                consumptionRepository.save(consumption);
            }
        } catch (DataAccessException e) {
            throw new DatabaseException(
                    "소비 저장 중 오류가 발생했습니다." + e.getMessage());
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

    public Long getTotalAmountByEmail(String email, Long year, Long month, Long day) {
        return consumptionRepository.sumAmountByEmail(email, year, month, day).orElse(0L);
    }

    public List<ByCategory> getTotalAmountByEmailAndCategory(String email, Long year, Long month, Long day) {
        List<ByCategory> result = consumptionRepository.findByCategoryAndEmail(email, year, month, day);
        return result != null ? result : Collections.emptyList();
    }

    public List<TopExpense> getMaxAmountByEmailAndItemName(String email, Long year, Long month, Long day) {
        List<TopExpense> result = consumptionRepository.findTopExpenseByEmail(email, year, month, day);
        return result != null ? result : Collections.emptyList();
    }

    public List<StoreExpense> getStoreExpenseListByEmailAndStoreName(String email, Long year, Long month, Long day) {
        List<StoreExpense> result = consumptionRepository.findStoreExpenseByStoreName(email, year, month, day);
        //jpa나 querydsl은 리스트를 조회할 때 값을 찾지못하면 null이 아니라 빈 리스트를 반환함
        return result;
    }
}
