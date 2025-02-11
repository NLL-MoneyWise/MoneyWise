package backend.backend.repository;

import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.TopExpense;
import backend.backend.dto.consumption.response.ConsumptionsSummaryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static backend.backend.domain.QConsumption.consumption;
import static backend.backend.domain.QCategory.category;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConsumptionRepositoryImpl implements ConsumptionRepositoryCustom {
    private JPAQueryFactory queryFactory;

    @Override
        public List<ByCategory> findByCategoryAndEmail(String email) {
            return queryFactory
                    .select(Projections.constructor(ByCategory.class,
                            category.name,
                            consumption.amount.sum()))
                    .from(consumption)
                    .join(category).on(consumption.category_id.eq(category.id))
                    .where(consumption.email.eq(email))
                    .groupBy(category.name)
                    .fetch();
    }

    @Override
    public List<TopExpense> findTopExpenseByEmail(String email) {
        NumberExpression<Long> totalAmount = consumption.amount.sum();

        JPQLQuery<Long> maxAmount = JPAExpressions
                .select(consumption.amount.sum())
                .from(consumption)
                .where(consumption.email.eq(email))
                .groupBy(consumption.item_name)
                .orderBy(consumption.amount.sum().desc())
                .limit(1);


        return queryFactory
                .select(Projections.constructor(TopExpense.class,
                consumption.item_name, totalAmount))
                .from(consumption)
                .where(consumption.email.eq(email))
                .groupBy(consumption.item_name)
                .having(totalAmount.eq(maxAmount))
                .fetch();
    }
}
