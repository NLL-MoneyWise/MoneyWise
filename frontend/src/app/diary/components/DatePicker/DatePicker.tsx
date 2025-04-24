import React, { useEffect, useState } from 'react';
import { DayPicker } from 'react-day-picker';
import './DatePicker.css';
import 'react-day-picker/dist/style.css';
import './DatePicker.css';

const DatePicker = () => {
    const [selected, setSelected] = useState<Date | undefined>(new Date());

    useEffect(() => {
        console.log(selected);
    }, [selected]);

    return (
        <div className="p-4 rounded-lg shadow">
            <DayPicker
                mode="single"
                selected={selected}
                onSelect={setSelected}
            />
        </div>
    );
};

export default DatePicker;
