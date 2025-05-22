'use client';
import React, { useState } from 'react';
import ConsumptioneChartChart from '../components/Chart/ConsumptioneChartChart';
import DateNavigator from '../components/DateNavigator/DateNavigator';
import IncomeExpenditureReport from '../components/IncomeExpenditureReport/IncomeExpenditureReport';
import useCounsumption from '../hooks/useCounsumption';
import { ErrorBoundary } from 'react-error-boundary';
import NotFoundData from '../components/NotFoundData/NotFoundData';
import { ViewType } from '../types/viewtype';
import { PeriodType } from '../types/request/requset-consumptione';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import Text from '@/app/common/components/Text/Text';
import Ranking from '../components/Ranking/Ranking';
import useAnalyzeConsumption from '../hooks/useAnalyzeCounsumption';

const AnalyzePage = () => {
    const [currentDate, setCurrentDate] = useState<Date>(new Date(2024, 8));
    const [viewType, setViewType] = useState<ViewType>('전체');

    return (
        <div className="rounded-t-lg text-center h-screen bg-slate-50 flex flex-col overflow-hidden">
            <DateNavigator
                currentDate={currentDate}
                setCurrentDate={setCurrentDate}
                viewType={viewType}
                setViewType={setViewType}
            />
            <IncomeExpenditureReport />

            <QueryErrorResetBoundary>
                {({ reset }) => (
                    <ErrorBoundary
                        onReset={reset}
                        fallbackRender={({ resetErrorBoundary }) => (
                            <NotFoundData
                                handleClick={() => resetErrorBoundary()}
                            />
                        )}
                    >
                        <AnalyzeContent
                            date={currentDate}
                            viewType={viewType}
                        />
                    </ErrorBoundary>
                )}
            </QueryErrorResetBoundary>
        </div>
    );
};

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
    주간: 'day'
});

const AnalyzeContent = ({ date, viewType }: AnalyzeContentProps) => {
    const { data, isLoading } = useAnalyzeConsumption({
        period: CONVERT_NAMING_TO_KO[viewType],
        year: date.getFullYear().toString(),
        month:
            viewType === '월간' || viewType === '주간'
                ? date.getMonth().toString()
                : undefined
    });

    if (isLoading) return <div>로딩중 ..</div>;
    if (!data) return <NotFoundData handleClick={() => {}} />;

    return (
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

            <Ranking data={data.topExpenses} title={'가장 많이 구매한 제품'} />
        </div>
    );
};

export default AnalyzePage;
