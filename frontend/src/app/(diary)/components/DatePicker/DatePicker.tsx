import React, { useEffect } from 'react';
import { DayPicker } from 'react-day-picker';
import 'react-day-picker/dist/style.css';
import './DatePicker.css';
import clsx from 'clsx';

interface DatePickerProps {
    className?: string;
    selected?: Date;
    onDateChange: (date: Date) => void;
}

const DatePicker = ({ className, selected, onDateChange }: DatePickerProps) => {
    useEffect(() => {
        if (selected) {
            console.log('선택된 날짜:', selected);
        }
    }, [selected]);

    return (
        <div
            className={clsx(
                `p-4 rounded-lg shadow absolute bg-white `,
                className
            )}
        >
            <DayPicker
                mode="single"
                selected={selected}
                onSelect={(date) => {
                    if (onDateChange) {
                        onDateChange(date);
                    }
                }}
            />
        </div>
    );
};

export default DatePicker;
