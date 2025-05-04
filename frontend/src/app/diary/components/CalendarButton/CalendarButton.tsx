import React, { useState } from 'react';
import DatePicker from '../DatePicker/DatePicker';
import Button from '@/app/common/components/Button/Button';
import { Calendar1 } from 'lucide-react';
import clsx from 'clsx';

interface CalendarButtonProps {
    className?: string;
    selectedDate: Date | undefined;
    onDateSelect: (date: Date | undefined) => void;
    editMode: 'create' | 'edit';
}

const CalendarButton = ({
    className,
    selectedDate,
    onDateSelect,
    editMode
}: CalendarButtonProps) => {
    const [isOpen, setIsOpen] = useState<boolean>(false);

    // 캘린더 버튼 클릭 시 토글
    const toggleCalendar = () => {
        setIsOpen(!isOpen);
    };

    // 날짜 변경 핸들러
    const handleDateChange = (date: Date | undefined) => {
        // 부모 컴포넌트에 선택된 날짜 전달
        if (onDateSelect) {
            onDateSelect(date);
        }
    };

    // 선택된 날짜를 형식화하는 함수
    const formatDate = (date: Date | undefined) => {
        if (!date) return '날짜 선택';
        return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    return (
        <div className={clsx(className)}>
            <Button
                className={clsx('flex items-center justify-center px-4 py-2')}
                onClick={toggleCalendar}
                type="button"
                disabled={editMode === 'edit' && true}
                variant={editMode === 'edit' ? 'block' : 'primary'}
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
