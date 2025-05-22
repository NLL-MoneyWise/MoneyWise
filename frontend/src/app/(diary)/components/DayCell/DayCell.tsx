import React, { useEffect } from 'react';
import { GetMemoResponse } from '../../types/response';
import { DayCellContentArg } from '@fullcalendar/core/index.js';
interface DayCellProps {
    memo: GetMemoResponse;
    arg: DayCellContentArg;
}

const DayCell = ({ arg, memo }: DayCellProps) => {
    useEffect(() => {
        console.log('데이 셀 리렌더링!');
    }, []);

    const memoForDate = memo.memoDTOList.find(
        (memo) => new Date(memo.date).toDateString() === arg.date.toDateString()
    );

    return (
        <div className="fc-daygrid-day-frame">
            <div className="fc-daygrid-day-top">
                <span className="fc-daygrid-day-number">
                    {arg.dayNumberText}
                </span>
            </div>
            {memoForDate && (
                <div className="memo-content flex">
                    {memoForDate.content.slice(0, 20)}
                    {memoForDate.content.length > 20 ? '...' : ''}
                </div>
            )}
        </div>
    );
};

export default DayCell;
