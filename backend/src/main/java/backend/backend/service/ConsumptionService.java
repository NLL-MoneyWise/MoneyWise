package backend.backend.service;

import backend.backend.domain.Consumption;
import backend.backend.domain.Receipt;
import backend.backend.domain.fixedCost.FixedCost;
import backend.backend.dto.common.model.Item;
import backend.backend.dto.consumption.model.*;
import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.dto.consumption.request.ConsumptionsUpdateRequest;
import backend.backend.dto.consumption.response.*;
import backend.backend.dto.fixedCost.model.FixedCostDTO;
import backend.backend.dto.fixedCost.request.FixedCostSaveRequest;
import backend.backend.dto.fixedCost.request.FixedCostUpdateRequest;
import backend.backend.dto.fixedCost.response.*;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.exception.ValidationException;
import backend.backend.repository.ConsumptionRepository;
import backend.backend.repository.FixedCostRepository;
import backend.backend.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsumptionService {
    private final ConsumptionRepository consumptionRepository;
    private final ReceiptRepository receiptRepository;
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
                    .accessUrl(request.getAccess_url())
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
                    consumption.setName(item.getName());
                    consumption.setQuantity(item.getQuantity());
                    result = consumptionRepository.save(consumption);

                    ConsumptionDTO consumptionDTO = new ConsumptionDTO();

                    consumptionDTO.setCategory(category_string);
                    consumptionDTO.setQuantity(result.getQuantity());
                    consumptionDTO.setAmount(result.getAmount());
                    consumptionDTO.setId(result.getId());
                    consumptionDTO.setName(result.getName());

                    consumptionDTOList.add(consumptionDTO);
                }
            } else {
                consumption.setAmount(receipt.getTotalAmount());
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

            // 모든 소비 내역의 첫 번째 항목에서 access_url 가져오기
            Consumption firstConsumption = consumptionRepository.findById(request.getConsumptionDTOList().get(0).getId())
                    .orElseThrow(() -> new NotFoundException("소비 내역을 찾을 수 없습니다."));

            String accessUrl = firstConsumption.getAccessUrl();

            // 소비 내역 업데이트
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
                consumption.setName(consumptionDTO.getName());

                consumptionRepository.save(consumption);
            }

            // 영수증 테이블 업데이트
            updateReceiptTotalAmount(accessUrl);

            return new ConsumptionsUpdateResponse();
        } catch (DateTimeParseException e) {
            throw new ValidationException("잘못된 날짜 형식입니다. yyyy-MM-dd를 사용해주세요.");
        } catch (DataAccessException e) {
            throw new DatabaseException("소비 내역 변경에 실패했습니다.");
        }
    }

    // 영수증 합계 업데이트 메서드 추가
    private void updateReceiptTotalAmount(String accessUrl) {
        Long totalAmount = consumptionRepository.sumAmountByAccessUrl(accessUrl).orElse(0L);

        if (totalAmount > 0) {
            // 소비 내역이 있는 경우 합계 업데이트
            receiptRepository.updateTotalAmountByAccessUrl(accessUrl, totalAmount);
        } else {
            // 소비 내역이 없는 경우 영수증 삭제
            receiptRepository.deleteById(accessUrl);
        }
    }


    public ConsumptionsFindAllResponse findAll(String email, String access_url) {
        try {
            List<Consumption> consumptions = consumptionRepository.findByEmailAndAccessUrl(email, access_url);
            ConsumptionsFindAllResponse response = new ConsumptionsFindAllResponse();
            List<ConsumptionDTO> consumptionDTOList = new ArrayList<>();

            if (!consumptions.isEmpty()) {
                Map<Long, String> categoryMap = getReverseCategoryMap();

                String date = consumptions.get(0).getConsumption_date().toString();
                String store_name = consumptions.get(0).getStoreName();

                response.setDate(date);
                response.setStore_name(store_name);

                for (Consumption consumption : consumptions) {
                    ConsumptionDTO consumptionDTO = new ConsumptionDTO();

                    consumptionDTO.setName(consumption.getName());
                    consumptionDTO.setQuantity(consumption.getQuantity());
                    consumptionDTO.setCategory(categoryMap.get(consumption.getCategory_id()));
                    consumptionDTO.setId(consumption.getId());
                    consumptionDTO.setAmount(consumption.getAmount());

                    consumptionDTOList.add(consumptionDTO);
                }

                response.setConsumptionDTOList(consumptionDTOList);
            }
            return response;
        } catch (DataAccessException e) {
            throw new NotFoundException("해당 영수증의 소비 내역을 찾을 수 없습니다.");
        }
    }


    public ConsumptionsFindOneResponse findOne(String email, Long id) {
        Consumption consumption = consumptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 id의 소비 내역이 없습니다."));

        ConsumptionDTO consumptionDTO = new ConsumptionDTO();
        ConsumptionsFindOneResponse response = new ConsumptionsFindOneResponse();

        Map<Long, String> categoryReverseMap = getReverseCategoryMap();

        consumptionDTO.setAmount(consumption.getAmount());
        consumptionDTO.setName(consumption.getName());
        consumptionDTO.setQuantity(consumption.getQuantity());
        consumptionDTO.setId(consumption.getId());
        consumptionDTO.setCategory(categoryReverseMap.get(consumption.getCategory_id()));

        response.setConsumptionDTO(consumptionDTO);
        response.setDate(consumption.getConsumption_date().toString());
        response.setStore_name(consumption.getStoreName());

        return response;
    }


    public ConsumptionsDeleteAllResponse deleteAll(String email, String accessUrl) {
        try {
            int d_cnt =  consumptionRepository.deleteByEmailAndAccessUrl(email, accessUrl);

            if (d_cnt > 0) {
                receiptRepository.deleteById(accessUrl);
            } else {
                throw new NotFoundException("소비 내역을 찾을 수 없습니다.");
            }

            return new ConsumptionsDeleteAllResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("소비 내역 삭제에 실패했습니다.");
        }
    }


    public ConsumptionsDeleteOneResponse deleteOne(String email, Long id) {
        try {
            Consumption consumption = consumptionRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("해당 소비 내역을 찾을 수 없습니다."));

            if (!consumption.getEmail().equals(email)) {
                throw new NotFoundException("해당 소비 내역을 찾을 수 없습니다.");
            }

            int d_cnt = consumptionRepository.deleteByEmailAndId(email, id);

            if (d_cnt > 0) {
                updateReceiptTotalAmount(consumption.getAccessUrl());
            }

            return new ConsumptionsDeleteOneResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("소비 내역 삭제에 실패했습니다.");
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

    public Long getTotalAmountByEmail(String email, Long year, Long month, Long week) {
        Long startDay = null, lastDay = null;

        if (week != null) {
            int[] days = calculateWeekDaysMondayStart(year, month, week);
            startDay = (long) days[0];
            lastDay = (long) days[1];
        }
        return consumptionRepository.sumAmountByEmail(email, year, month, startDay, lastDay).orElse(0L);
    }

    public List<ByCategory> getTotalAmountByEmailAndCategory(String email, Long year, Long month, Long week) {
        Long startDay = null, lastDay = null;

        if (week != null) {
            int[] days = calculateWeekDaysMondayStart(year, month, week);
            startDay = (long) days[0];
            lastDay = (long) days[1];
        }
        List<ByCategory> result = consumptionRepository.findByCategoryAndEmail(email, year, month, startDay, lastDay);
        return result != null ? result : Collections.emptyList();
    }

    public List<TopExpense> getMaxAmountByEmailAndItemName(String email, Long year, Long month, Long week) {
        Long startDay = null, lastDay = null;

        if (week != null) {
            int[] days = calculateWeekDaysMondayStart(year, month, week);
            startDay = (long) days[0];
            lastDay = (long) days[1];
        }
        List<TopExpense> result = consumptionRepository.findTopExpenseByEmail(email, year, month, startDay, lastDay);
        return result != null ? result : Collections.emptyList();
    }

    public List<StoreExpense> getStoreExpenseListByEmailAndStoreName(String email, Long year, Long month, Long week) {
        Long startDay = null, lastDay = null;

        if (week != null) {
            int[] days = calculateWeekDaysMondayStart(year, month, week);
            startDay = (long) days[0];
            lastDay = (long) days[1];
        }
        List<StoreExpense> result = consumptionRepository.findStoreExpenseByStoreName(email, year, month, startDay, lastDay);
        //jpa나 querydsl은 리스트를 조회할 때 값을 찾지못하면 null이 아니라 빈 리스트를 반환함
        return result;
    }

    public ConsumptionsDailyAnalyzeResponse getDailyAnalyze(String email, Long year, Long month, Long startDay, Long lastDay) {
        try {
            Map<LocalDate, Long> totalAmountMap = getTotalAmountMap(consumptionRepository.dailySumAmountByEmail(email, year, month, startDay, lastDay));
            Map<LocalDate, List<ByCategory>> byCategoryMap = getByCategoryMap(consumptionRepository.dailyFindByCategoryAndEmail(email, year, month, startDay, lastDay));
            Map<LocalDate, List<TopExpense>> topExpenseMap = getTopExpenseMap(consumptionRepository.dailyFindTopExpenseByEmail(email, year, month, startDay, lastDay));
            Map<LocalDate, List<StoreExpense>> storeExpenseMap = getStoreExpenseMap(consumptionRepository.dailyFindStoreExpenseByStoreName(email, year, month, startDay, lastDay));

            Set<LocalDate> allDates = new HashSet<>();
            allDates.addAll(totalAmountMap.keySet());
            allDates.addAll(byCategoryMap.keySet());
            allDates.addAll(topExpenseMap.keySet());
            allDates.addAll(storeExpenseMap.keySet());

            List<DailyList> dailyLists = new ArrayList<>();

            for (LocalDate date : allDates) {
                DailyList dailyList = new DailyList();
                dailyList.setDate(date.toString());

                dailyList.setTotalAmount(totalAmountMap.getOrDefault(date, 0L));
                dailyList.setByCategory(byCategoryMap.getOrDefault(date, new ArrayList<>()));
                dailyList.setTopExpenses(topExpenseMap.getOrDefault(date, new ArrayList<>()));
                dailyList.setStoreExpenses(storeExpenseMap.getOrDefault(date, new ArrayList<>()));

                dailyLists.add(dailyList);
            }

            dailyLists.sort(Comparator.comparing(dailyList -> LocalDate.parse(dailyList.getDate())));

            ConsumptionsDailyAnalyzeResponse response = new ConsumptionsDailyAnalyzeResponse();
            response.setDailyList(dailyLists);

            return response;
        } catch (DataAccessException e) {
            throw new DatabaseException("소비 분석에 실패했습니다.");
        }
    }

    public Map<LocalDate, Long> getTotalAmountMap(List<DailySumAmountQueryDTO> sumAmountQueryDTOList) {
        Map<LocalDate, Long> totalAmountMap = sumAmountQueryDTOList.stream()
                .collect(Collectors.toMap(
                        DailySumAmountQueryDTO::getDate,
                        DailySumAmountQueryDTO::getTotalAmount
                ));

        return totalAmountMap;
    }

    public Map<LocalDate, List<ByCategory>> getByCategoryMap(List<DailyFindByCategoryQueryDTO> byCategoryQueryDTOList) {
        Map<LocalDate, List<ByCategory>> byCategoryMap = byCategoryQueryDTOList.stream()
                .collect(Collectors.groupingBy(
                        DailyFindByCategoryQueryDTO::getDate,
                        Collectors.mapping(
                                dto -> {
                                    ByCategory category = new ByCategory();
                                    category.setName(dto.getName());
                                    category.setAmount(dto.getAmount());
                                    return category;
                                },
                                Collectors.toList()
                        )
                ));

        return byCategoryMap;
    }

    public Map<LocalDate, List<TopExpense>> getTopExpenseMap(List<DailyFindTopExpenseQueryDTO> topExpenseQueryDTOList) {
        Map<LocalDate, List<TopExpense>> topExpenseMap = topExpenseQueryDTOList.stream()
                .collect(Collectors.groupingBy(
                        DailyFindTopExpenseQueryDTO::getDate,
                        Collectors.mapping(dto -> {
                            TopExpense topExpense = new TopExpense();
                            topExpense.setName(dto.getName());
                            topExpense.setAmount(dto.getAmount());
                            return topExpense;
                        },
                        Collectors.toList())
                ));

        return topExpenseMap;
    }

    public Map<LocalDate, List<StoreExpense>> getStoreExpenseMap(List<DailyFindStoreExpenseQueryDTO> storeExpenseQueryDTOList) {
        Map<LocalDate, List<StoreExpense>> storeExpenseMap = storeExpenseQueryDTOList.stream()
                .collect(Collectors.groupingBy(
                        DailyFindStoreExpenseQueryDTO::getDate,
                        Collectors.mapping(dto -> {
                            StoreExpense storeExpense = new StoreExpense();
                            storeExpense.setName(dto.getName());
                            storeExpense.setAmount(dto.getAmount());
                            return storeExpense;
                        },
                        Collectors.toList())
                ));

        return storeExpenseMap;
    }

    private int[] calculateWeekDaysMondayStart(Long year, Long month, Long week) {
        LocalDate firstDayOfMonth = LocalDate.of(year.intValue(), month.intValue(), 1);
        //firstDayOfMonth.lengthOfMonth()는 해당 월의 총 일수 반환
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        // 해당 월의 모든 주차 계산
        List<int[]> allWeeks = calculateAllMondayWeeks(firstDayOfMonth, lastDayOfMonth);

        // 요청한 주차가 유효한지 확인
        if (week > allWeeks.size() || week < 1) {
            throw new ValidationException(
                    String.format("유효하지 않은 주차입니다. %d년 %d월은 1주차부터 %d주차까지 있습니다.",
                            year, month, allWeeks.size())
            );
        }

        return allWeeks.get(week.intValue() - 1);
    }

    /**
     * 해당 월의 모든 주차를 월요일 기준으로 계산
     */
    private List<int[]> calculateAllMondayWeeks(LocalDate firstDay, LocalDate lastDay) {
        List<int[]> weeks = new ArrayList<>();

        LocalDate currentMonday = firstDay;

        // 1일이 월요일이 아니면 다음 월요일 찾기
        //getDayOfWeek()는 해당 날짜의 요일 반환
        while (currentMonday.getDayOfWeek() != DayOfWeek.MONDAY) {
            currentMonday = currentMonday.plusDays(1);
        }

        // 1일이 월요일이 아닌 경우, 1일부터 첫 번째 월요일 전날까지가 첫째주
        if (!firstDay.equals(currentMonday)) {
            int firstWeekStart = firstDay.getDayOfMonth();
            int firstWeekEnd = currentMonday.minusDays(1).getDayOfMonth();
            weeks.add(new int[]{firstWeekStart, firstWeekEnd});
        }

        // 월요일부터 시작하는 주차들 계산
        while (!currentMonday.isAfter(lastDay)) {
            LocalDate weekEnd = currentMonday.plusDays(6); // 일요일까지

            // 해당 월을 벗어나면 월의 마지막 날까지만
            if (weekEnd.isAfter(lastDay)) {
                weekEnd = lastDay;
            }

            weeks.add(new int[]{currentMonday.getDayOfMonth(), weekEnd.getDayOfMonth()});

            currentMonday = currentMonday.plusDays(7); // 다음 주 월요일
        }

        return weeks;
    }
}
