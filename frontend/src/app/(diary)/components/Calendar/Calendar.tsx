'use client';

import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { DateClickArg } from '@fullcalendar/interaction';

import './calendar.css';

import { memo, useRef, useState } from 'react';
import { DatesSetArg } from '@fullcalendar/core';

import DayCell from '../DayCell/DayCell';
import { GetMemoResponse as Memo } from '../../types/response';

interface CalendarProps {
    memo: Memo;
    dateClick: (arg: DateClickArg) => void;
}

const MemoCalendar = ({ memo, dateClick }: CalendarProps) => {
    const [currentViewType, setCurrentViewType] = useState('dayGridMonth');

    const calendarRef = useRef<FullCalendar>(null);

    // const {
    //     getAllIncome: { data: income },
    //     getFiexedCost: { data: fixedCost }
    // } = useCounsumption();

    const handleDatesSet = (arg: DatesSetArg) => {
        setCurrentViewType(arg.view.type);
    };

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
                    return <DayCell arg={arg} memo={memo} />;
                }}
                dateClick={dateClick}
            />
        </div>
    );
};

const Calendar = memo(MemoCalendar);
export default Calendar;
