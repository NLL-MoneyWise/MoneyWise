import React from 'react';
import { Memo } from '../../types/response';
import { Income } from '@/app/(user)/types/reponse';
import { DayCellContentArg } from '@fullcalendar/core/index.js';
import { getDay } from '../../util/formatDate';
import { ConsumptionDate } from '@/app/(user)/types/reponse';
import MemoPreview from './MemoPreview';
import IncomePreview from './IncomePreview';
import ConsumptionPreview from './ConsumptionPreview';

interface DayCellProps {
    memo: Memo[];
    arg: DayCellContentArg;
    income: Income[];
    consumption: ConsumptionDate[];
    viewType: 'dayGridMonth' | 'dayGridWeek';
}

const DayCell = ({
    arg,
    memo,
    income,
    consumption,
    viewType
}: DayCellProps) => {
    const memoForDate = memo.find(
        (memo) => new Date(memo.date).toDateString() === arg.date.toDateString()
    );
    const inComeForDate = income.find(
        (income) => income.day.toString() === getDay(arg.date)
    );

    const consumptionForDate = consumption.find(
        (consumption) =>
            new Date(consumption.date).toDateString() ===
            arg.date.toDateString()
    );
    const consumptionAmount = consumptionForDate
        ? consumptionForDate.totalAmount
        : null;

    const consumptionData = consumptionForDate
        ? consumptionForDate.topExpenses
        : null;

    return (
        <div className="fc-daygrid-day-frame ">
            <div className="fc-daygrid-day-top">
                <span className="fc-daygrid-day-number ">
                    {arg.dayNumberText}
                </span>
            </div>
            <div className="pl-1 space-y-1">
                {memoForDate && <MemoPreview content={memoForDate.content} />}
                {inComeForDate && <IncomePreview amount={inComeForDate.cost} />}
                {consumptionAmount && (
                    <ConsumptionPreview
                        totalAmount={consumptionAmount}
                        items={
                            viewType === 'dayGridWeek'
                                ? (consumptionData ?? [])
                                : undefined
                        }
                        showBreakdown={viewType === 'dayGridWeek'}
                    />
                )}
            </div>
        </div>
    );
};

export default DayCell;
