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

const events = [
    {
        title: '35000',
        start: '2025-04-27',
        type: 'memo',
        extendedProps: {
            amount: 15000,
            category: 'food',
            location: '홍콩반점',
            memo: '동료와 점심 식사'
        }
    },
    {
        title: '35000',
        start: '2025-04-27',
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
    const calendarRef = useRef<FullCalendar>(null);

    const { ModalComponent, closeModal, openModal } = useModal();

    const handleDatesSet = (arg: DatesSetArg) => {
        setCurrentViewType(arg.view.type);
        console.log('현재 뷰 타입:', arg.view.type);
    };

    const getEventsOnDateUsingAPI = (date: Date) => {
        if (!calendarRef.current) return [];

        const calendarApi = calendarRef.current.getApi();
        return calendarApi.getEvents().filter((event) => {
            const eventDate = event.start;
            return (
                eventDate && eventDate.toDateString() === date.toDateString()
            );
        });
    };

    return (
        <div className="calendar-container m-auto ">
            <FullCalendar
                ref={calendarRef}
                plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
                initialView="dayGridMonth"
                events={events}
                datesSet={handleDatesSet}
                dayMaxEvents={2}
                headerToolbar={{
                    start: 'prev next',
                    center: 'title',
                    end: 'dayGridMonth dayGridWeek'
                }}
                locale={'ko'}
                height={'100vh'}
                dateClick={(data) => {
                    const eventsOnThisDate = getEventsOnDateUsingAPI(data.date);

                    // 이미 메모가 작성되어있으면 클릭할 수 없음
                    if (eventsOnThisDate.length > 0) return;

                    setSelectedDate(data.date);
                    openModal();
                }}
                eventClick={() => {
                    alert('안뇽');
                }}
            />
            <ModalComponent>
                <EventForm initalDate={selectedDate} closeModal={closeModal} />
            </ModalComponent>
        </div>
    );
};

export default Calendar;
