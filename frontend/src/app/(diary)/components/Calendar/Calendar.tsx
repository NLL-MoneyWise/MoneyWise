'use client';

import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { DateClickArg } from '@fullcalendar/interaction';
import { DatesSetArg } from '@fullcalendar/core';

import './calendar.css';

import { memo, useRef } from 'react';

import DayCell from '../DayCell/DayCell';
import { GetMemoResponse as Memo } from '../../types/response';
import { GetAllIncomeResponse as Income } from '@/app/(user)/types/reponse';
interface CalendarProps {
    memo: Memo;
    dateClick: (arg: DateClickArg) => void;
    handleDatesSet: (args: DatesSetArg) => void;
    income: Income;
}

const MemoCalendar = ({
    memo,
    dateClick,
    handleDatesSet,
    income
}: CalendarProps) => {
    const calendarRef = useRef<FullCalendar>(null);

    return (
        <div className="calendar-container m-auto ">
            <FullCalendar
                ref={calendarRef}
                plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
                initialView="dayGridMonth"
                datesSet={handleDatesSet}
                headerToolbar={{
                    start: 'prev next',
                    center: 'title',
                    end: 'dayGridMonth dayGridWeek'
                }}
                locale={'ko'}
                height={'100vh'}
                dayCellContent={(arg) => {
                    return <DayCell arg={arg} memo={memo} income={income} />;
                }}
                dateClick={dateClick}
            />
        </div>
    );
};

const Calendar = memo(MemoCalendar);
export default Calendar;
