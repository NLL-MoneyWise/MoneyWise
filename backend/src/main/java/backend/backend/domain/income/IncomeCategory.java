package backend.backend.domain.income;

import lombok.Getter;

@Getter
public enum IncomeCategory {
    SALARY("급여"),           // 본업 월급
    SIDE_JOB("부업"),         // 프리랜싱, 알바 등
    INVESTMENT("투자수익"),     // 주식, 부동산 등
    INTEREST("이자소득"),      // 예적금 이자
    GIFT("용돈/선물"),        // 부모님 용돈, 축의금 등
    OTHER("기타");

    private String name;

    IncomeCategory(String name) {
        this.name = name;
    }
}
