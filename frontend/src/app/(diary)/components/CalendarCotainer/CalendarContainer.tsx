import React, { useState } from 'react';
import useDiary from '../../hooks/useDiary';
import Calendar from '../Calendar/Calendar';
import formatDate from '../../util/formatDate';
import useModal from '@/app/common/hooks/useModal';
import { DateClickArg } from '@fullcalendar/interaction';
import { CreateModalCotent, EditModalContent } from '../MemoModal/MemoModal';

const CalendarContainer = () => {
    const [selectedDate, setSelectedDate] = useState<Date>(new Date());

    const {
        getMemo: { data: memo }
    } = useDiary();

    // const {
    //     getAllIncome: { data: income },
    //     getFiexedCost: { data: fixedCost }
    // } = useCounsumption();

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

    const dateClick = (arg: DateClickArg) => {
        setSelectedDate(arg.date);
        const hasMemo = memo.memoDTOList.some(
            (memo) => memo.date === formatDate(arg.date)
        );
        if (hasMemo) {
            openEditModal();
        } else {
            openCreateModal();
        }
    };

    return (
        <React.Fragment>
            <Calendar memo={memo} dateClick={dateClick} />
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
        </React.Fragment>
    );
};

export default CalendarContainer;
