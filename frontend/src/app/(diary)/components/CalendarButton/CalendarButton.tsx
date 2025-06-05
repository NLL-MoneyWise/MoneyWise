import React, { useState } from 'react';
import DatePicker from '../DatePicker/DatePicker';
import Button from '@/app/common/components/Button/Button';
import { Calendar1 } from 'lucide-react';
import clsx from 'clsx';

interface CalendarButtonProps {
    className?: string;
    selectedDate: Date;
    onDateSelect: (date: Date) => void;
    isBlock: boolean;
}

const CalendarButton = ({
    className,
    selectedDate,
    onDateSelect,
    isBlock = false
}: CalendarButtonProps) => {
    const [isOpen, setIsOpen] = useState<boolean>(false);

    const toggleCalendar = () => {
        setIsOpen(!isOpen);
    };

    const handleDateChange = (date: Date) => {
        onDateSelect(date);
    };

    const formatDate = (date: Date | undefined) => {
        if (!date) return '날짜 선택';
        return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    return (
        <div className={clsx(className, 'w-full')}>
            <Button
                className={'flex items-center justify-center px-4 py-2 '}
                onClick={toggleCalendar}
                type="button"
                disabled={isBlock}
                variant={isBlock ? 'block' : 'primary'}
            >
                <Calendar1 className="mr-3" />
                {formatDate(selectedDate)}
            </Button>

            {isOpen && (
                <DatePicker
                    selected={selectedDate}
                    onDateChange={handleDateChange}
                />
            )}
        </div>
    );
};

export default CalendarButton;
