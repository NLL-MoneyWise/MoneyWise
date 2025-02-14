package backend.backend.repository;

import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.TopExpense;
import backend.backend.dto.consumption.response.ConsumptionsSummaryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static backend.backend.domain.QConsumption.consumption;
import static backend.backend.domain.QCategory.category;

import java.util.List;
import java.util.Optional;

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

    @Override
        public List<ByCategory> findByCategoryAndEmail(String email, Long year, Long month) {
        System.out.println("Email parameter: " + email);
        NumberExpression<Long> totalAmount = consumption.amount.sum();

        List<ByCategory> result = queryFactory
                .select(Projections.constructor(ByCategory.class,
                        category.name,
                        totalAmount.as("amount")))
                .from(consumption)
                .join(category).on(consumption.category_id.eq(category.id))
                .where(emailEq(email), yearEq(year), monthEq(month))
                .groupBy(category.name)
                .orderBy(totalAmount.desc())
                .fetch();
        System.out.println("Query result: " + result);

            return result;
    }

    @Override
    public List<TopExpense> findTopExpenseByEmail(String email, Long year, Long month) {
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
                .where(emailEq(email), yearEq(year), monthEq(month))
                .groupBy(consumption.item_name)
                .orderBy(totalAmount.desc())
                .fetch();

        List<TopExpense> result = queryFactory
                .select(Projections.constructor(TopExpense.class,
                        consumption.item_name, totalAmount))
                .from(consumption)
                .where(emailEq(email), yearEq(year), monthEq(month))
                .groupBy(consumption.item_name)
                .having(totalAmount.eq(maxAmountList.get(0)))
                .fetch();

        System.out.println("Query result: " + result);

        return result;
    }

    @Override
    public Optional<Long> sumAmountByEmailAndYearAndMonthToQuerydsl(String email, Long year, Long month) {
        NumberExpression<Long> totalAmount = consumption.amount.sum();

        //ofNullable메서드로 null값을 허용하고 서비스단에서 처리
        Optional<Long> result = Optional.ofNullable(queryFactory
                .select(totalAmount)
                .from(consumption)
                .where(emailEq(email), yearEq(year), monthEq(month))
                .fetchOne());

        return result;
    }
}
