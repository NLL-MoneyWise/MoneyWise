'use client';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import './calendar.css';
import { useState } from 'react';
import { DatesSetArg } from '@fullcalendar/core';
import useModal from '@/app/common/hooks/useModal';
import EventForm from '../EventForm/EventForm';

const events = [
    {
        id: '4',
        title: '35000',
        start: '2025-03-17',
        extendedProps: {
            amount: 15000,
            category: 'food',
            location: '홍콩반점',
            memo: '동료와 점심 식사'
        }
    },
    {
        id: '4',
        title: '35000',
        start: '2025-03-17',
        extendedProps: {
            amount: 15000,
            category: 'food',
            location: '홍콩반점',
            memo: '동료와 점심 식사'
        }
    }
];

const Calendar = () => {
    const [currentViewType, setCurrentViewType] = useState('dayGridMonth');
    const [selectedDate, setSelectedDate] = useState<Date>(new Date());

    const { ModalComponent, closeModal, openModal } = useModal();

    const handleDatesSet = (arg: DatesSetArg) => {
        setCurrentViewType(arg.view.type);
        console.log('현재 뷰 타입:', arg.view.type);
    };

    return (
        <div className="calendar-container m-auto ">
            <FullCalendar
                plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
                initialView="dayGridMonth"
                events={events}
                datesSet={handleDatesSet}
                dayMaxEvents={1}
                headerToolbar={{
                    start: 'prev next',
                    center: 'title',
                    end: 'dayGridMonth dayGridWeek'
                }}
                locale={'ko'}
                height={'100vh'}
                dateClick={(data) => {
                    setSelectedDate(data.date);
                    openModal();
                }}
            />
            <ModalComponent>
                <EventForm initalDate={selectedDate} closeModal={closeModal} />
            </ModalComponent>
        </div>
    );
};

export default Calendar;
