'use client';

import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { DateClickArg } from '@fullcalendar/interaction';
import { DatesSetArg } from '@fullcalendar/core';
import { ConsumptionDate } from '@/app/(user)/types/reponse';
import './calendar.css';

import { memo, useRef } from 'react';

import DayCell from '../DayCell/DayCell';
import { Memo } from '../../types/response';
import { Income } from '@/app/(user)/types/reponse';
import { FixedCost } from '@/app/(user)/types/reponse';

interface CalendarProps {
    memo: Memo[];
    dateClick: (arg: DateClickArg) => void;
    handleDatesSet: (args: DatesSetArg) => void;
    income: Income[];
    fixedCost: FixedCost[];
    initialDate: Date;
    consumption: ConsumptionDate[];
    viewType: 'dayGridMonth' | 'dayGridWeek';
}

const MemoCalendar = ({
    memo,
    dateClick,
    handleDatesSet,
    income,
    initialDate,
    consumption,
    viewType
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
                dayMaxEvents={true}
                dayCellContent={(arg) => {
                    return (
                        <DayCell
                            arg={arg}
                            memo={memo}
                            income={income}
                            consumption={consumption}
                            viewType={viewType}
                        />
                    );
                }}
                dateClick={dateClick}
                initialDate={initialDate}
                aspectRatio={1.2}
                dayMaxEventRows={true}
                height="100vh"
            />
        </div>
    );
};

const Calendar = memo(MemoCalendar);
export default Calendar;
