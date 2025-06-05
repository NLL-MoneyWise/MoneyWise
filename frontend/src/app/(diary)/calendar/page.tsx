'use client';
import { Fragment } from 'react';
import FloatingActionButton from '@/app/common/components/FloatingButton/FloatingButton';

import CalendarContainer from '../components/CalendarCotainer/CalendarContainer';
const CalendarPage = () => {
    return (
        <Fragment>
            <CalendarContainer />
            <FloatingActionButton />
        </Fragment>
    );
};

export default CalendarPage;
