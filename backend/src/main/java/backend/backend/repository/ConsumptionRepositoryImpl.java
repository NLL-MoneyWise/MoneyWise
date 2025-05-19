package backend.backend.repository;

import backend.backend.dto.consumption.model.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static backend.backend.domain.QConsumption.consumption;
import static backend.backend.domain.QCategory.category;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConsumptionRepositoryImpl implements ConsumptionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression emailEq(String email) {
        return email != null ? consumption.email.eq(email) : null;
    }

    private BooleanExpression yearEq(Long year) {
        return year != null ? Expressions.numberTemplate(
                Long.class, //반환 타입, 자바에서 쿼리를 실행하기 때문에 받을 타입을 지정해야함
                "EXTRACT(YEAR FROM {0})", //sql조건문, 파라미터가 여러개일 경우 {1},{2} 등등 생성 가능
                consumption.consumption_date //{0}에 들어갈 실제 값
        ).eq(year) : null;
    }
    //날짜를 검사하는 또 다른 방법 month()의 반환은 Integer이기 때문에 형변환이 필요하다.
    private BooleanExpression monthEq(Long month) {
        return month != null ? consumption.consumption_date.month().eq(Math.toIntExact(month)) : null;
    }

    //Math.toIntExact는 Long타입만 Integer로 변환해줌
    private BooleanExpression dayEq(Long day) {
        return day != null ? consumption.consumption_date.dayOfMonth().eq(Math.toIntExact(day)) : null;
    }

    private BooleanExpression dayBetween(Long startDay, Long lastDay) {
        return startDay != null && lastDay != null ? consumption.consumption_date.dayOfMonth().between(startDay, lastDay) : null;
    }

    @Override
        public List<ByCategory> findByCategoryAndEmail(String email, Long year, Long month, Long day) {
        System.out.println("Email parameter: " + email);
        NumberExpression<Long> totalAmount = consumption.amount.sum();

        List<ByCategory> result = queryFactory
                .select(Projections.constructor(ByCategory.class,
                        category.name,
                        totalAmount.as("amount")))
                .from(consumption)
                .join(category).on(consumption.category_id.eq(category.id))
                .where(emailEq(email), yearEq(year), monthEq(month), dayEq(day))
                .groupBy(category.name)
                .orderBy(totalAmount.desc())
                .fetch();
        System.out.println("Query result: " + result);

            return result;
    }

    @Override
    public List<TopExpense> findTopExpenseByEmail(String email, Long year, Long month, Long day) {
        System.out.println("Email parameter: " + email);

        NumberExpression<Long> totalAmount = consumption.amount.sum();

        //JPAExpression: 서브쿼리 목적, queryFactory: 쿼리 단독 실행
        //서브 쿼리로 들어가기 때문에 order by사용불가, querydsl에서 서브쿼리에는 order by가 사용 불가능하다.
//        JPQLQuery<Long> maxAmount = JPAExpressions
//                .select(totalAmount)
//                .from(consumption)
//                .where(emailEq(email), yearEq(year), monthEq(month))
//                .groupBy(consumption.item_name)
//                .orderBy(totalAmount.desc())
//                .limit(1);

        //첫번째 해결방법, queryFactory를 사용할 경우 fetch를 사용하여 독립적인 쿼리로 실행 가능하기 떄문에 order by사용가능
//        Long maxAmount = queryFactory
//                .select(totalAmount)
//                .from(consumption)
//                .where(emailEq(email), yearEq(year), monthEq(month))
//                .groupBy(consumption.item_name)
//                .orderBy(totalAmount.desc())
//                .fetchFirst();

        //두번째 방법, 사용 시 .get(0)으로 최상위 값 가져오기
        List<Long> maxAmountList = queryFactory
                .select(totalAmount)
                .from(consumption)
                .where(emailEq(email), yearEq(year), monthEq(month), dayEq(day))
                .groupBy(consumption.item_name)
                .orderBy(totalAmount.desc())
                .fetch();

        if (maxAmountList.isEmpty()) {
            return Collections.emptyList(); // 또는 Collections.emptyList();
        }

        List<TopExpense> result = queryFactory
                .select(Projections.constructor(TopExpense.class,
                        consumption.item_name, totalAmount))
                .from(consumption)
                .where(emailEq(email), yearEq(year), monthEq(month), dayEq(day), consumption.item_name.isNotNull())
                .groupBy(consumption.item_name)
                .having(totalAmount.eq(maxAmountList.get(0)))
                .fetch();

        System.out.println("Query result: " + result);

        return result;
    }

    @Override
    public Optional<Long> sumAmountByEmail(String email, Long year, Long month, Long day) {
        NumberExpression<Long> totalAmount = consumption.amount.sum();

        //ofNullable메서드로 null값을 허용하고 서비스단에서 처리
        Optional<Long> result = Optional.ofNullable(queryFactory
                .select(totalAmount)
                .from(consumption)
                .where(emailEq(email), yearEq(year), monthEq(month), dayEq(day))
                .fetchOne());

        return result;
    }

    @Override
    public List<StoreExpense> findStoreExpenseByStoreName(String email, Long year, Long month, Long day) {
        List<StoreExpense> result = queryFactory
                .select(Projections.constructor(StoreExpense.class, consumption.storeName, consumption.amount.sum()))
                .from(consumption)
                .where(emailEq(email), yearEq(year), monthEq(month), dayEq(day), consumption.storeName.isNotNull())
                .groupBy(consumption.storeName)
                .orderBy(consumption.amount.sum().desc())
                .fetch();

        return result;
    }

    @Override
    public List<DailySumAmountQueryDTO> dailySumAmountByEmail(String email, Long year, Long month, Long startDay, Long lastDay) {
        List<DailySumAmountQueryDTO> result = queryFactory
                .select(Projections.constructor(DailySumAmountQueryDTO.class, consumption.consumption_date, consumption.amount.sum()))
                .from(consumption)
                .where(consumption.email.eq(email),
                        consumption.consumption_date.year().eq(Math.toIntExact(year)),
                        consumption.consumption_date.month().eq(Math.toIntExact(month)),
                        dayBetween(startDay, lastDay))
                .groupBy(consumption.consumption_date)
                .orderBy(consumption.consumption_date.asc())
                .fetch();

        return result;
    }

    @Override
    public List<DailyFindByCategoryQueryDTO> dailyFindByCategoryAndEmail(String email, Long year, Long month, Long startDay, Long lastDay) {
        List<DailyFindByCategoryQueryDTO> result = queryFactory
                .select(Projections.constructor(DailyFindByCategoryQueryDTO.class, consumption.consumption_date, category.name, consumption.amount.sum()))
                .from(consumption)
                .join(category).on(consumption.category_id.eq(category.id))
                .where(consumption.email.eq(email),
                        consumption.consumption_date.year().eq(Math.toIntExact(year)),
                        consumption.consumption_date.month().eq(Math.toIntExact(month)),
                        dayBetween(startDay, lastDay))
                .groupBy(consumption.consumption_date, category.name)
                .orderBy(consumption.consumption_date.asc())
                .fetch();

        return result;
    }

    @Override
    public List<DailyFindTopExpenseQueryDTO> dailyFindTopExpenseByEmail(String email, Long year, Long month, Long startDay, Long lastDay) {
        // 1. 날짜와 아이템별 총 지출 금액 계산
        List<Tuple> dateItemTotals = queryFactory
                .select(consumption.consumption_date, consumption.item_name, consumption.amount.sum())
                .from(consumption)
                .where(consumption.email.eq(email),
                        consumption.consumption_date.year().eq(Math.toIntExact(year)),
                        consumption.consumption_date.month().eq(Math.toIntExact(month)),
                        dayBetween(startDay, lastDay),
                        consumption.item_name.isNotNull())
                .groupBy(consumption.consumption_date, consumption.item_name)
                .fetch();

        if (dateItemTotals.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 날짜별로 그룹화하고 최대 금액 찾기
        Map<LocalDate, Long> dateToMaxAmountMap = new HashMap<>();
        Map<LocalDate, List<Tuple>> dateToItemsMap = new HashMap<>();

        // 날짜별로 아이템 리스트 그룹화
        for (Tuple tuple : dateItemTotals) {
            LocalDate date = tuple.get(consumption.consumption_date);
            dateToItemsMap.computeIfAbsent(date, k -> new ArrayList<>()).add(tuple);
        }

        // 각 날짜별 최대 금액 계산
        for (Map.Entry<LocalDate, List<Tuple>> entry : dateToItemsMap.entrySet()) {
            LocalDate date = entry.getKey();
            List<Tuple> items = entry.getValue();

            // 해당 날짜의 아이템 중 최대 금액 찾기
            Long maxAmount = items.stream()
                    .mapToLong(tuple -> tuple.get(consumption.amount.sum()))
                    .max()
                    .orElse(0L);

            dateToMaxAmountMap.put(date, maxAmount);
        }

        // 3. 각 날짜별로 최대 금액과 일치하는 아이템 찾기
        List<DailyFindTopExpenseQueryDTO> result = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Tuple>> entry : dateToItemsMap.entrySet()) {
            LocalDate date = entry.getKey();
            List<Tuple> items = entry.getValue();
            Long maxAmount = dateToMaxAmountMap.get(date);

            // 최대 금액과 일치하는 아이템만 선택
            for (Tuple tuple : items) {
                Long amount = tuple.get(consumption.amount.sum());
                if (maxAmount.equals(amount)) {
                    String itemName = tuple.get(consumption.item_name);
                    result.add(new DailyFindTopExpenseQueryDTO(date, itemName, amount));
                }
            }
        }

        // 4. 날짜 기준으로 정렬
        result.sort(Comparator.comparing(DailyFindTopExpenseQueryDTO::getDate));

        return result;
    }

    @Override
    public List<DailyFindStoreExpenseQueryDTO> dailyFindStoreExpenseByStoreName(String email, Long year, Long month, Long startDay, Long lastDay) {
        List<DailyFindStoreExpenseQueryDTO> result = queryFactory
                .select(Projections.constructor(DailyFindStoreExpenseQueryDTO.class, consumption.consumption_date, consumption.storeName, consumption.amount.sum()))
                .from(consumption)
                .where(consumption.email.eq(email),
                        consumption.consumption_date.year().eq(Math.toIntExact(year)),
                        consumption.consumption_date.month().eq(Math.toIntExact(month)),
                        dayBetween(startDay, lastDay),
                        consumption.storeName.isNotNull())
                .groupBy(consumption.consumption_date, consumption.storeName)
                .orderBy(consumption.amount.sum().desc())
                .fetch();

        return result;
    }
}
