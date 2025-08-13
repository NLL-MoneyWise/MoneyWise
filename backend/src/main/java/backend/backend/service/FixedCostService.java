package backend.backend.service;

import backend.backend.domain.FixedCost;
import backend.backend.dto.fixedCost.model.FixedCostDTO;
import backend.backend.dto.fixedCost.request.FixedCostSaveRequest;
import backend.backend.dto.fixedCost.request.FixedCostUpdateRequest;
import backend.backend.dto.fixedCost.response.*;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.exception.ValidationException;
import backend.backend.repository.FixedCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class FixedCostService {
    private final FixedCostRepository fixedCostRepository;

    public FixedCostSaveResponse createFixedCost(String email, FixedCostSaveRequest request) {
        try {
            if (request.getDay() > 28 || request.getDay() < 1) {
                throw new ValidationException("day는 1과 28 사이의 값 이어야 합니다.");
            }

            Map<String, Long> categoryMap = getCategoryMap();

            LocalDate date = LocalDate.now();
            LocalDate requestDate = date.withDayOfMonth(request.getDay());


            FixedCost fixedCost = new FixedCost();
            fixedCost.setEmail(email);
            fixedCost.setCategoryId(categoryMap.get(request.getCategory()));
            fixedCost.setName(request.getName());
            fixedCost.setFixedCostDate(requestDate);
            fixedCost.setAmount(request.getAmount());

            fixedCostRepository.save(fixedCost);

            return new FixedCostSaveResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("고정 지출액 저장에 실패했습니다.");
        }
    }

    public FIxedCostUpdateResponse updateFixedCost(String email, FixedCostUpdateRequest request) {
        try {
            FixedCost fixedCost = fixedCostRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException("해당하는 고정 지출을 찾을 수 없습니다."));

            fixedCost.setAmount(request.getAmount());

            fixedCostRepository.save(fixedCost);

            return new FIxedCostUpdateResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("고정 지출액 변경에 실패했습니다.");
        }
    }

    public FixedCostFindOneResponse findOneFixedCost(String email, Long id) {
        try {
            Map<Long, String> reverceCategoryMap = getReverseCategoryMap();

            FixedCost fixedCost = fixedCostRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("해당하는 고정 지출을 찾을 수 없습니다."));

            FixedCostFindOneResponse response = new FixedCostFindOneResponse();

            response.setName(fixedCost.getName());
            response.setAmount(fixedCost.getAmount());
            response.setCategory(reverceCategoryMap.get(fixedCost.getCategoryId()));
            response.setDate(fixedCost.getFixedCostDate().toString());

            return response;
        } catch (DataAccessException e) {
            throw new DatabaseException("고정 지출액 조회에 실패했습니다.");
        }
    }

    public FixedCostFindAllResponse findAllFixedCost(String email) {
        try {
            Map<Long, String> reverseCategoryMap = getReverseCategoryMap();

            List<FixedCost> fixedCostList = fixedCostRepository.findByEmail(email);
            List<FixedCostDTO> fixedCostDTOList = new ArrayList<>();

            for (FixedCost fixedCost : fixedCostList) {
                FixedCostDTO fixedCostDTO = new FixedCostDTO();

                fixedCostDTO.setName(fixedCost.getName());
                fixedCostDTO.setId(fixedCost.getId());
                fixedCostDTO.setCategory(reverseCategoryMap.get(fixedCost.getCategoryId()));
                fixedCostDTO.setDate(fixedCost.getFixedCostDate().toString());
                fixedCostDTO.setAmount(fixedCost.getAmount());

                fixedCostDTOList.add(fixedCostDTO);
            }

            FixedCostFindAllResponse response = new FixedCostFindAllResponse();
            response.setFixedCostDTOList(fixedCostDTOList);

            return response;
        } catch (DataAccessException e) {
            throw new DatabaseException("고정 지출액 조회에 실패했습니다.");
        }
    }

    public FixedCostDeleteOneResponse deleteOneFixedCost(String email, Long id) {
        try {
            fixedCostRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 고정 지출을 찾을 수 없습니다."));
            fixedCostRepository.deleteById(id);

            return new FixedCostDeleteOneResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("고정 지출액 삭제에 실패했습니다.");
        }
    }

    public FixedCostDeleteAllResponse deleteAllFixedCost(String email) {
        try {
            fixedCostRepository.deleteByEmail(email);

            return new FixedCostDeleteAllResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("고정 지출액 삭제에 실패했습니다.");
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

    private static Map<Long, String> getReverseCategoryMap() {
        Map<Long, String> reverseCategoryMap = new HashMap<>();
        reverseCategoryMap.put(1L, "문구");
        reverseCategoryMap.put(2L, "식품");
        reverseCategoryMap.put(3L, "음료");
        reverseCategoryMap.put(4L, "기타");
        reverseCategoryMap.put(5L, "생활용품");
        reverseCategoryMap.put(6L, "패션/의류");
        reverseCategoryMap.put(7L, "건강/의약품");
        reverseCategoryMap.put(8L, "미용/화장품");
        reverseCategoryMap.put(9L, "전자기기");
        reverseCategoryMap.put(10L, "교통/주유");
        reverseCategoryMap.put(11L, "서비스");
        reverseCategoryMap.put(12L, "취미/여가");
        reverseCategoryMap.put(13L, "반려동물");
        reverseCategoryMap.put(14L, "유아/아동");
        return reverseCategoryMap;
    }
}
