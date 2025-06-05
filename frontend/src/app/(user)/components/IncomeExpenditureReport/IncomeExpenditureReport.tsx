'use client';
import Text from '@/app/common/components/Text/Text';
import React from 'react';

interface IncomeExpenditureReportProps {
    Expenditure: number;
}

const IncomeExpenditureReport = ({
    Expenditure
}: IncomeExpenditureReportProps) => {
    return (
        <div className="bg-primary flex justify-around p-3 text-white">
            <Text.LagreText>{`총 지출 ${Expenditure}원`}</Text.LagreText>
        </div>
    );
};

export default IncomeExpenditureReport;
