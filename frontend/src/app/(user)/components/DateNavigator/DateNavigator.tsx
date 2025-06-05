'use client';
import {
    addMonths,
    subMonths,
    getWeekOfMonth,
    endOfMonth,
    startOfMonth,
    subYears,
    addYears
} from 'date-fns';
import Text from '@/app/common/components/Text/Text';

import DateSelector from '../DateSelector/DateSelector';
import { Dispatch, useState } from 'react';
import { ViewType } from '../../types/viewtype';
import ArrowNavigator from '@/app/common/components/ArrowNavigator/ArrowNavigator';

const dateFormat = (date: Date, viewType: ViewType) => {
    if (viewType === '월간') {
        return `${date.getFullYear()}년 ${date.getMonth() + 1}월`;
    } else if (viewType === '주간') {
        return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${getWeekOfMonth(date)}주차`;
    } else {
        return `${date.getFullYear()}년`;
    }
};

interface DateNavigatorProps {
    currentDate: Date;
    setCurrentDate: React.Dispatch<Date>;
    viewType: ViewType;
    setViewType: Dispatch<ViewType>;
}

const DateNavigator = ({
    currentDate,
    setCurrentDate,
    viewType,
    setViewType
}: DateNavigatorProps) => {
    const [isDropDonwOpen, setIsDropDonwOpen] = useState(false);

    const handleNext = () => {
        let newDate;

        if (viewType === '전체') return;

        if (viewType === '월간') {
            newDate = addMonths(currentDate, 1);
        } else if (viewType === '주간') {
            newDate = new Date(currentDate);
            newDate.setDate(newDate.getDate() + 7);

            // 월이 바뀌었다면 다음 달의 첫째 주로 이동
            if (newDate.getMonth() !== currentDate.getMonth()) {
                const startDayOfMonth = startOfMonth(newDate);
                newDate = startOfMonth(startDayOfMonth);
            }
        } else {
            newDate = addYears(currentDate, 1);
        }

        setCurrentDate(newDate);
    };

    const handlePrevious = () => {
        let newDate;

        if (viewType === '전체') return;

        if (viewType === '월간') {
            newDate = subMonths(currentDate, 1);
        } else if (viewType === '주간') {
            newDate = new Date(currentDate);
            newDate.setDate(newDate.getDate() - 7);

            // 월이 바뀌었다면 이전 달의 마지막째 주로 이동
            if (newDate.getMonth() !== currentDate.getMonth()) {
                const lastDayOfMonth = endOfMonth(newDate);
                newDate = endOfMonth(lastDayOfMonth);
            }
        } else {
            newDate = subYears(currentDate, 1);
        }

        setCurrentDate(newDate);
    };

    return (
        <header className="w-full bg-primary p-3 rounded-t-2xl flex justify-between text-white items-center">
            <ArrowNavigator
                handleNext={handleNext}
                handlePrevious={handlePrevious}
                className="h-fit gap-3 w-[280px]"
            >
                <Text.LagreText>
                    {viewType === '전체'
                        ? '전체'
                        : dateFormat(currentDate, viewType)}
                </Text.LagreText>
            </ArrowNavigator>
            <DateSelector
                isOpen={isDropDonwOpen}
                handleIsOpenChange={setIsDropDonwOpen}
                viewType={viewType}
                handleViewTypeChange={setViewType}
            />
        </header>
    );
};

export default DateNavigator;
