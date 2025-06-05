'use client';
import React, { useState } from 'react';
import useDiary from '../../hooks/useDiary';
import Calendar from '../Calendar/Calendar';
import useModal from '@/app/common/hooks/useModal';
import { DateClickArg } from '@fullcalendar/interaction';
import useIncome from '@/app/(user)/hooks/useIncome';
import useFiexCost from '@/app/(user)/hooks/useFiexCost';
import useConsumption from '@/app/(user)/hooks/useCounsumption';
import CalendarModalContentSelector from '../CalendarModalContentSelector/CalendarModalContentSelector';
import { DatesSetArg } from '@fullcalendar/core';
import { OverlayLoadingSpinner } from '@/app/common/components/LoadingSpinner/LoadingSpinner';

const CalendarContainer = () => {
    const [selectedDate, setSelectedDate] = useState<Date>(new Date(2024, 8));
    const [currentViewDate, setCurrentViewDate] = useState<Date>(
        new Date(2024, 8)
    );
    const [viewType, setViewType] = useState<'dayGridMonth' | 'dayGridWeek'>(
        'dayGridMonth'
    );

    const {
        getMemo: { data: memo, isLoading: isMemoLoading }
    } = useDiary();

    const {
        getAllIncome: { data: income, isLoading: isIncomeLoading }
    } = useIncome();

    const {
        getFiexedCost: { data: fixedCost, isLoading: isFixedCostLoading }
    } = useFiexCost();

    const { ModalComponent, openModal, closeModal } = useModal();

    const {
        getConsumption: { data: consumption, isLoading: isConsumptionLoading },
        prefetchNeighborMonths
    } = useConsumption({
        year: currentViewDate.getFullYear().toString(),
        month: (currentViewDate.getMonth() + 1).toString()
    });

    console.log(consumption);

    const dateClick = (arg: DateClickArg) => {
        setSelectedDate(arg.date);
        openModal();
    };

    const handleDatesSet = (arg: DatesSetArg) => {
        setCurrentViewDate(arg.view.currentStart);
        prefetchNeighborMonths(arg.view.currentStart);
        setViewType(
            arg.view.type === 'dayGridMonth' ? 'dayGridMonth' : 'dayGridWeek'
        );
    };

    console.log('consumption', consumption);

    const isLoading =
        isMemoLoading ||
        isIncomeLoading ||
        isFixedCostLoading ||
        isConsumptionLoading;

    return (
        <React.Fragment>
            {isLoading && <OverlayLoadingSpinner />}
            <Calendar
                dateClick={dateClick}
                handleDatesSet={handleDatesSet}
                memo={memo?.memoDTOList ?? []}
                income={income?.incomeDTOList ?? []}
                fixedCost={fixedCost?.fixedCostDTOList ?? []}
                initialDate={currentViewDate}
                consumption={consumption?.dailyList ?? []}
                viewType={viewType}
            />
            <ModalComponent>
                <CalendarModalContentSelector
                    closeModal={closeModal}
                    selectedDate={selectedDate}
                    dataSet={{
                        memo: memo?.memoDTOList ?? [],
                        income: income?.incomeDTOList ?? [],
                        fixedCost: fixedCost?.fixedCostDTOList ?? []
                    }}
                />
            </ModalComponent>
        </React.Fragment>
    );
};

export default CalendarContainer;
