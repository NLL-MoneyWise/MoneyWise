import React, { useState } from 'react';
import useDiary from '../../hooks/useDiary';
import Calendar from '../Calendar/Calendar';
import { formatDateToString } from '../../util/formatDate';
import useModal from '@/app/common/hooks/useModal';
import { DateClickArg } from '@fullcalendar/interaction';
import {
    BudgetModalContent,
    CreateModalCotent,
    EditModalContent
} from '../MemoModal/MemoModal';
import useCounsumption from '@/app/(user)/hooks/useCounsumption';
import { DatesSetArg } from '@fullcalendar/core';

const CalendarContainer = () => {
    const [selectedDate, setSelectedDate] = useState<Date>(new Date());
    const [currentViewType, setCurrentViewType] =
        useState<string>('dayGridMonth');

    const {
        getMemo: { data: memo }
    } = useDiary();

    const {
        getAllIncome: { data: income },
        getFiexedCost: { data: fixedCost }
    } = useCounsumption();

    console.log(income);

    const {
        ModalComponent: CreateModal,
        openModal: openCreateModal,
        closeModal: closeCreateModal
    } = useModal();

    const {
        ModalComponent: EditModal,
        openModal: openEditModal,
        closeModal: closeEditModal
    } = useModal();

    const {
        ModalComponent: BudgetModal,
        openModal: openBudgetModal,
        closeModal: closeBudgetModal
    } = useModal();

    const dateClick = (arg: DateClickArg) => {
        setSelectedDate(arg.date);

        if (currentViewType === 'dayGridWeek') {
            openBudgetModal();
        } else {
            const hasMemo = memo.memoDTOList.some(
                (memo) => memo.date === formatDateToString(arg.date)
            );
            if (hasMemo) {
                openEditModal();
            } else {
                openCreateModal();
            }
        }
    };

    const handleDatesSet = (arg: DatesSetArg) => {
        setCurrentViewType(arg.view.type);
        console.log(arg.view.type);
    };

    return (
        <React.Fragment>
            <Calendar
                memo={memo}
                dateClick={dateClick}
                handleDatesSet={handleDatesSet}
                income={income}
            />
            <CreateModal>
                <CreateModalCotent
                    closeModal={() => closeCreateModal()}
                    selectedDate={selectedDate}
                />
            </CreateModal>
            <EditModal>
                <EditModalContent
                    closeModal={() => closeEditModal()}
                    selectedDate={selectedDate}
                />
            </EditModal>
            <BudgetModal>
                <BudgetModalContent
                    closeModal={() => closeBudgetModal()}
                    selectedDate={selectedDate}
                ></BudgetModalContent>
            </BudgetModal>
        </React.Fragment>
    );
};

export default CalendarContainer;
