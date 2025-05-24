'use client';
import { Fragment } from 'react';
import FloatingActionButton from '@/app/common/components/FloatingButton/FloatingButton';

import SSRSafeSuspense from '@/app/common/components/SSRSafeSuspense/SSRSafeSuspense';
import CalendarContainer from '../components/CalendarCotainer/CalendarContainer';
const CalendarPage = () => {
    return (
        <Fragment>
            <SSRSafeSuspense fallback={'로딩 중'}>
                <CalendarContainer />
            </SSRSafeSuspense>
            <FloatingActionButton />
        </Fragment>
    );
};

export default CalendarPage;
