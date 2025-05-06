package backend.backend.service;

import backend.backend.domain.Consumption;
import backend.backend.domain.Receipt;
import backend.backend.dto.common.model.Item;
import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.ConsumptionDTO;
import backend.backend.dto.consumption.model.StoreExpense;
import backend.backend.dto.consumption.model.TopExpense;
import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.dto.consumption.request.ConsumptionsUpdateRequest;
import backend.backend.dto.consumption.response.ConsumptionsSaveResponse;
import backend.backend.dto.consumption.response.ConsumptionsUpdateResponse;
import backend.backend.exception.AuthException;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.exception.ValidationException;
import backend.backend.repository.ConsumptionRepository;
import backend.backend.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsumptionService {
    private final ConsumptionRepository consumptionRepository;
    private final ReceiptRepository receiptRepository;

    public ConsumptionsSaveResponse save(String email, ConsumptionsSaveRequest request) {
        ConsumptionsSaveResponse response = new ConsumptionsSaveResponse();
        List<ConsumptionDTO> consumptionDTOList = new ArrayList<>();
        Consumption result;

        Map<String, Long> categoryMap = getCategoryMap();

        try {
            String date_string = request.getDate();
            //request의 date는 String이므로 localDate로 변환 필요
            LocalDate localDate = LocalDate.parse(date_string);

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
                    String category_string = item.getCategory();
                    Long categoryId = categoryMap.get(category_string);

                    consumption.setAmount(item.getAmount() * item.getQuantity());
                    consumption.setCategory_id(categoryId);
                    consumption.setItem_name(item.getName());
                    consumption.setQuantity(item.getQuantity());
                    result = consumptionRepository.save(consumption);

                    ConsumptionDTO consumptionDTO = new ConsumptionDTO();

                    consumptionDTO.setCategory(category_string);
                    consumptionDTO.setQuantity(result.getQuantity());
                    consumptionDTO.setAmount(result.getAmount());
                    consumptionDTO.setId(result.getId());
                    consumptionDTO.setName(result.getItem_name());

                    consumptionDTOList.add(consumptionDTO);
                }
            } else {
                consumption.setAmount(receipt.getTotal_amount());
                result = consumptionRepository.save(consumption);

                ConsumptionDTO consumptionDTO = new ConsumptionDTO();
                consumptionDTO.setId(result.getId());
                consumptionDTO.setAmount(result.getAmount());

                consumptionDTOList.add(consumptionDTO);
            }
            response.setConsumptionDTOList(consumptionDTOList);
            return response;
        } catch (DataAccessException e) {
            throw new DatabaseException("소비 저장 중 오류가 발생했습니다." + e.getMessage());
        } catch (DateTimeParseException e) {
            throw new ValidationException("잘못된 날짜 형식입니다. yyyy-MM-dd를 사용해주세요.");
        }
    }


    public ConsumptionsUpdateResponse update(String email, ConsumptionsUpdateRequest request) {
        try {
            Map<String, Long> categoryMap = getCategoryMap();

            LocalDate localDate = LocalDate.parse(request.getDate());
            String store_name = request.getStore_name();

            for (ConsumptionDTO consumptionDTO : request.getConsumptionDTOList()) {
                Consumption consumption = consumptionRepository.findById(consumptionDTO.getId())
                        .orElseThrow(() -> new NotFoundException(consumptionDTO.getId() + "번의 소비 내역이 없습니다."));

                if (!consumption.getEmail().equals(email)) {
                    throw new NotFoundException(consumptionDTO.getId() + "번의 소비 내역이 없습니다.");
                }

                consumption.setConsumption_date(localDate);
                consumption.setStoreName(store_name);
                consumption.setAmount(consumptionDTO.getAmount() * consumptionDTO.getQuantity());
                consumption.setCategory_id(categoryMap.get(consumptionDTO.getCategory()));
                consumption.setQuantity(consumptionDTO.getQuantity());
                consumption.setItem_name(consumptionDTO.getName());

                consumptionRepository.save(consumption);
            }

            return new ConsumptionsUpdateResponse();
        } catch (DateTimeParseException e) {
            throw new ValidationException("잘못된 날짜 형식입니다. yyyy-MM-dd를 사용해주세요.");
        } catch (DataAccessException e) {
            throw new DatabaseException("소비 내역 변경에 실패했습니다.");
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
