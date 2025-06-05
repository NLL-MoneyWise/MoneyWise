import React, { useState } from 'react';
import {
    MemoModalContent,
    ConsumptionModalContent,
    IncomeModalContent
} from '../CalendarModalContents/CalendarModalContents';
import ArrowNavigator from '@/app/common/components/ArrowNavigator/ArrowNavigator';
import EventForm from '../EventForm/EventForm';
import { Memo } from '../../types/response';
import { Income } from '@/app/(user)/types/reponse';
import { FixedCost } from '@/app/(user)/types/reponse';

interface CalendarModalContentSelectorProps {
    closeModal: () => void;
    selectedDate: Date;
    dataSet: {
        memo: Memo[];
        income: Income[];
        fixedCost: FixedCost[];
    };
}

const CalendarModalContentSelector = ({
    closeModal,
    selectedDate,
    dataSet = { memo: [], income: [], fixedCost: [] }
}: CalendarModalContentSelectorProps) => {
    const MODALS = [
        {
            label: '메모',
            Component: (
                <MemoModalContent
                    data={dataSet.memo}
                    selectedDate={selectedDate}
                    closeModal={closeModal}
                />
            )
        },
        {
            label: '소득',
            Component: (
                <IncomeModalContent
                    data={dataSet.income}
                    selectedDate={selectedDate}
                    closeModal={closeModal}
                />
            )
        },
        {
            label: '소비',
            Component: (
                <ConsumptionModalContent
                    data={dataSet.fixedCost}
                    selectedDate={selectedDate}
                    closeModal={closeModal}
                />
            )
        }
    ];

    const [currentidx, setCurrentidx] = useState<number>(0);

    const handlePrevious = () =>
        setCurrentidx((prev) => (prev - 1 + MODALS.length) % MODALS.length);
    const handleNext = () =>
        setCurrentidx((prev) => (prev + 1) % MODALS.length);

    const { Component, label } = MODALS[currentidx];

    return (
        <React.Fragment>
            <ArrowNavigator
                handleNext={handleNext}
                handlePrevious={handlePrevious}
                className="w-40 justify-center mx-auto text-gray-500"
            >
                <EventForm.Title>{label}</EventForm.Title>
            </ArrowNavigator>

            {Component}
        </React.Fragment>
    );
};

export default CalendarModalContentSelector;
