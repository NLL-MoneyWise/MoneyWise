import React from 'react';
import { GetMemoResponse as Memo } from '../../types/response';
import { GetAllIncomeResponse as Income } from '@/app/(user)/types/reponse';

import { DayCellContentArg } from '@fullcalendar/core/index.js';
import { getDay } from '../../util/formatDate';
interface DayCellProps {
    memo: Memo;
    arg: DayCellContentArg;
    income: Income;
}

const DayCell = ({ arg, memo, income }: DayCellProps) => {
    const memoForDate = memo.memoDTOList.find(
        (memo) => new Date(memo.date).toDateString() === arg.date.toDateString()
    );
    const inComeForDate = income.incomeDTOList.find(
        (income) => income.day.toString() === getDay(arg.date)
    );
    console.log(income);

    return (
        <div className="fc-daygrid-day-frame">
            <div className="fc-daygrid-day-top">
                <span className="fc-daygrid-day-number ">
                    {arg.dayNumberText}
                </span>
            </div>
            {memoForDate && (
                <div className="memo-content flex">
                    {memoForDate.content.slice(0, 20)}
                    {memoForDate.content.length > 20 ? '...' : ''}
                </div>
            )}
            {inComeForDate && (
                <div className=" flex text-blue-700">{inComeForDate.cost}</div>
            )}
        </div>
    );
};

export default DayCell;
