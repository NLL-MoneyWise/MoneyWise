'use client';
import FloatingActionButton from '@/app/common/components/FloatingButton/FloatingButton';
import Calendar from '@/app/diary/components/Calendar/Calendar';
import React from 'react';

const CalendarPage = () => {
    return (
        <div>
            <Calendar />
            <FloatingActionButton />
        </div>
    );
};

export default CalendarPage;
