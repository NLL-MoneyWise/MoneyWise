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

    @Override
        public List<ByCategory> findByCategoryAndEmail(String email, Long year) {
        System.out.println("Email parameter: " + email);
        NumberExpression<Long> totalAmount = consumption.amount.sum();

        List<ByCategory> result = queryFactory
                .select(Projections.constructor(ByCategory.class,
                        category.name,
                        totalAmount.as("amount")))
                .from(consumption)
                .join(category).on(consumption.category_id.eq(category.id))
                .where(emailEq(email), yearEq(year))
                .groupBy(category.name)
                .orderBy(totalAmount.desc())
                .fetch();
        System.out.println("Query result: " + result);

            return result;
    }

    @Override
    public List<TopExpense> findTopExpenseByEmail(String email, Long year) {
        System.out.println("Email parameter: " + email);

        NumberExpression<Long> totalAmount = consumption.amount.sum();

        JPQLQuery<Long> maxAmount = JPAExpressions
                .select(totalAmount)
                .from(consumption)
                .where(emailEq(email), yearEq(year))
                .groupBy(consumption.item_name)
                .limit(1);

        List<TopExpense> result = queryFactory
                .select(Projections.constructor(TopExpense.class,
                        consumption.item_name, totalAmount))
                .from(consumption)
                .where(emailEq(email), yearEq(year))
                .groupBy(consumption.item_name)
                .having(totalAmount.eq(maxAmount))
                .fetch();

        System.out.println("Query result: " + result);

        return result;
    }


}
