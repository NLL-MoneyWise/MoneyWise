'use client';

import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import './calendar.css';

import { useRef, useState } from 'react';
import { DatesSetArg } from '@fullcalendar/core';
import useModal from '@/app/common/hooks/useModal';
import EventForm from '../EventForm/EventForm';
import useDiary from '../../hooks/useDiary';
import formatDate from '../../util/formatDate';

const Calendar = () => {
    const [currentViewType, setCurrentViewType] = useState('dayGridMonth');
    const [selectedDate, setSelectedDate] = useState<Date>(new Date());
    const [isEditMode, setIsEditMode] = useState<boolean>(false);

    const calendarRef = useRef<FullCalendar>(null);

    const { ModalComponent, closeModal, openModal } = useModal();

    const {
        getMemo: { data: response, refetch, isLoading }
    } = useDiary();

    if (isLoading) {
        return <div>로딩중</div>;
    }

    console.log(response);

    const handleDatesSet = (arg: DatesSetArg) => {
        setCurrentViewType(arg.view.type);
        console.log('현재 뷰 타입:', arg.view.type);
    };

    const setMemo = () => {
        refetch();
        closeModal();
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
                    const memoForDate = response?.memoDTOList.find(
                        (memo) =>
                            new Date(memo.date).toDateString() ===
                            arg.date.toDateString()
                    );

                    return (
                        <div className="fc-daygrid-day-frame">
                            <div className="fc-daygrid-day-top">
                                <span className="fc-daygrid-day-number">
                                    {arg.dayNumberText}
                                </span>
                            </div>
                            {memoForDate && (
                                <div className="memo-content">
                                    {memoForDate.content.slice(0, 20)}
                                    {memoForDate.content.length > 20
                                        ? '...'
                                        : ''}
                                </div>
                            )}
                        </div>
                    );
                }}
                dateClick={(arg) => {
                    setSelectedDate(arg.date);

                    const hasMemo = response?.memoDTOList.some(
                        (memo) => memo.date === formatDate(arg.date)
                    );

                    if (hasMemo) {
                        setIsEditMode(true);
                    }

                    openModal();
                }}
            />
            <ModalComponent>
                <EventForm
                    initalDate={selectedDate}
                    closeModal={setMemo}
                    isEditMode={isEditMode}
                />
            </ModalComponent>
        </div>
    );
};

export default Calendar;
