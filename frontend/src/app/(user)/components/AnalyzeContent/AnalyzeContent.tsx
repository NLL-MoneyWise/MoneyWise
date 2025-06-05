import React from 'react';
import Text from '@/app/common/components/Text/Text';
import Ranking from '../Ranking/Ranking';
import useAnalyzeConsumption from '../../hooks/useAnalyzeCounsumption';
import NotFoundData from '../NotFoundData/NotFoundData';
import ConsumptioneChartChart from '../Chart/ConsumptioneChartChart';
import { ViewType } from '../../types/viewtype';
import { PeriodType } from '../../types/request/requset-consumptione';
import { LoadingSpinner } from '@/app/common/components/LoadingSpinner/LoadingSpinner';
import { getWeekOfMonth } from 'date-fns';
import IncomeExpenditureReport from '../IncomeExpenditureReport/IncomeExpenditureReport';
interface AnalyzeContentProps {
    date: Date;
    viewType: ViewType;
}

type ConverterType = {
    [K in ViewType]: PeriodType;
};

/* 백엔드 명세 바뀌면 수정 */
const CONVERT_NAMING_TO_KO: ConverterType = Object.freeze({
    전체: 'all',
    연간: 'year',
    월간: 'month',
    주간: 'week'
});

const AnalyzeContent = ({ date, viewType }: AnalyzeContentProps) => {
    const { data, isLoading } = useAnalyzeConsumption({
        period: CONVERT_NAMING_TO_KO[viewType],
        year: date.getFullYear().toString(),
        month:
            viewType === '월간' || viewType === '주간'
                ? (date.getMonth() + 1).toString()
                : undefined,
        week: viewType === '주간' ? getWeekOfMonth(date).toString() : undefined
    });

    if (isLoading) {
        return <LoadingSpinner />;
    }

    if (!data) {
        return <NotFoundData />;
    }
    return (
        <React.Fragment>
            <IncomeExpenditureReport Expenditure={data.totalAmount} />
            <div className="p-4 flex-1 grid grid-cols-2 grid-rows-[auto_minmax(300px,1fr)] gap-3 overflow-hidden">
                <div className="col-span-2">
                    <div className="flex flex-col shadow-lg p-3 rounded-2xl bg-white">
                        <Text>{'카테고리별 구입한 항목'}</Text>
                        <ConsumptioneChartChart data={data} />
                    </div>
                </div>

                <Ranking
                    data={data.storeExpenses}
                    title={'가장 많은 지출의 가게'}
                />

                <Ranking
                    data={data.topExpenses}
                    title={'가장 많이 구매한 제품'}
                />
            </div>
        </React.Fragment>
    );
};

export default AnalyzeContent;
