import React, { ChangeEvent } from 'react';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import EventForm from '../EventForm/EventForm';
import { useRef, useState } from 'react';
import useDiary from '../../hooks/useDiary';
import { formatDateToString, getDay } from '../../util/formatDate';
import useCounsumption from '@/app/(user)/hooks/useCounsumption';

interface ModalContentProps {
    selectedDate: Date;
    closeModal: () => void;
}

export const CreateModalCotent = ({
    closeModal,
    selectedDate
}: ModalContentProps) => {
    const addToast = useToastStore((state) => state.addToast);
    const contentRef = useRef<HTMLInputElement | null>(null);

    const { createMemo } = useDiary();

    const createMemoAction = async () => {
        const content = contentRef.current?.value;

        if (!content) return addToast('내용을 입력해주세요', 'error');

        await createMemo
            .mutateAsync({ date: formatDateToString(selectedDate), content })
            .then(() => {
                closeModal();
            })
            .catch(() => {});
    };
    return (
        <EventForm handleEvent={createMemoAction}>
            <EventForm.Title>메모 작성</EventForm.Title>
            <EventForm.DaySelector initalDate={selectedDate} isBlock={false} />
            <EventForm.Content contentRef={contentRef} />
            <EventForm.SaveActionButtons handleClose={closeModal} />
        </EventForm>
    );
};

export const EditModalContent = ({
    selectedDate,
    closeModal
}: ModalContentProps) => {
    const addToast = useToastStore((state) => state.addToast);
    const contentRef = useRef<HTMLInputElement | null>(null);

    const { editMemo, deleteMemo } = useDiary();

    const modifyMemoAction = async () => {
        const content = contentRef.current?.value;

        if (!content) return addToast('내용을 입력해주세요', 'error');

        await editMemo
            .mutateAsync({ date: formatDateToString(selectedDate), content })
            .then(() => {
                closeModal();
            })
            .catch();
    };

    const deleteMemoAciton = async () => {
        await deleteMemo
            .mutateAsync({ date: formatDateToString(selectedDate) })
            .then(() => {
                closeModal();
            })
            .catch();
    };

    return (
        <EventForm handleEvent={modifyMemoAction}>
            <EventForm.Title>메모 수정</EventForm.Title>
            <EventForm.DaySelector initalDate={selectedDate} isBlock={true} />
            <EventForm.Content contentRef={contentRef} />
            <EventForm.EditCreateActionButtons
                handleClose={closeModal}
                handleDelete={deleteMemoAciton}
            />
        </EventForm>
    );
};

export const BudgetModalContent = ({
    selectedDate,
    closeModal
}: ModalContentProps) => {
    const addToast = useToastStore((state) => state.addToast);
    const [cost, setCost] = useState<string>('');
    const [isIncome, setIsIncome] = useState<boolean>(true);

    const { postIncome, PostFixedConsumption } = useCounsumption();

    const lable: string = isIncome ? '소득' : '소비';
    const reverseLable: string = isIncome ? '소비' : '소득';

    const handleChange = (e: ChangeEvent<HTMLInputElement>): void => {
        const raw = e.target.value.replace(/,/g, '');
        if (isNaN(Number(raw))) return addToast('숫자만 입력해주세요', 'error');
        if (/^0\d+/.test(raw))
            return addToast('앞자리에 0은 올 수 없습니다', 'error');

        setCost(raw);
    };

    const setisIncome = () => {
        setIsIncome(!isIncome);
    };

    const PostBudgetAction = async () => {
        if (!cost) return addToast('값을 입력해주세요', 'error');
        const data = {
            cost: cost,
            day: getDay(selectedDate)
        };
        if (isIncome) {
            await postIncome.mutateAsync(data).then(() => closeModal());
        } else {
            await PostFixedConsumption.mutateAsync(data).then(() =>
                closeModal()
            );
        }
    };

    return (
        <EventForm handleEvent={PostBudgetAction}>
            <EventForm.Title>{'고정' + lable + ' 등록'}</EventForm.Title>
            <EventForm.DaySelector initalDate={selectedDate} isBlock={false} />
            <EventForm.Input
                placeholder={lable + '을 등록해주세요'}
                handleChange={handleChange}
                value={cost}
                lable={lable}
            />
            <EventForm.BudgetButtons
                handleClose={closeModal}
                handleChange={() => setisIncome()}
                lable={reverseLable + ' 등록으로 전환'}
            />
        </EventForm>
    );
};
