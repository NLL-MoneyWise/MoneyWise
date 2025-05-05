'use client';
import Text from '@/app/common/components/Text/Text';
import React from 'react';

const IncomeExpenditureReport = () => {
    const income = 1000000;
    const Expenditure = 1000000;
    return (
        <div className="bg-primary flex justify-around p-3 text-white">
            <Text.LagreText>{`수입 ${income}원`}</Text.LagreText>
            <Text.LagreText>{`지출 ${Expenditure}원`}</Text.LagreText>
        </div>
    );
};

export default IncomeExpenditureReport;
