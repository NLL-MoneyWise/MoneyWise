package backend.backend.repository;

import backend.backend.dto.income.model.DailyAnalyzeQueryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static backend.backend.domain.QIncome.income;

@RequiredArgsConstructor
@Repository
public class IncomeRepositoryImpl implements IncomeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression emailEq(String email) {
        return email != null ? income.email.eq(email) : null;
    }

    private BooleanExpression yearEq(Long year) {
        return year != null ? income.income_date.year().eq(Math.toIntExact(year)) : null;
    }

    private BooleanExpression monthEq(Long month) {
        return month != null ? income.income_date.month().eq(Math.toIntExact(month)) : null;
    }

    private BooleanExpression dayBetween(Long startDay, Long lastDay) {
        return startDay != null && lastDay != null ? income.income_date.dayOfMonth().between(startDay, lastDay) : null;
    }

    public Long analyzeIncomeTotalAmount(String email, Long year, Long month, Long startDay, Long lastDay) {
        Long totalAmount = queryFactory.select(income.amount.sum())
                .from(income)
                .where(emailEq(email), yearEq(year), monthEq(month), dayBetween(startDay, lastDay))
                .fetchOne();

        return totalAmount != null ? totalAmount : 0L;
    }

    public List<DailyAnalyzeQueryDto> analyzeIncomeDaily(String email, Long year, Long month, Long startDay, Long lastDay) {
        List<DailyAnalyzeQueryDto> result = queryFactory
                .select(Projections.constructor(DailyAnalyzeQueryDto.class,
                        income.income_date, income.amount.sum()))
                .from(income)
                .where(emailEq(email), yearEq(year), monthEq(month), dayBetween(startDay, lastDay))
                .groupBy(income.income_date)
                .orderBy(income.income_date.asc())
                .fetch();

        return result;
    }
}
