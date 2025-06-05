import React, { ChangeEvent, useRef, useState, useCallback } from 'react';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import EventForm from '../EventForm/EventForm';
import useDiary from '../../hooks/useDiary';
import { formatDateToString, getDay } from '../../util/formatDate';
import useIncome from '@/app/(user)/hooks/useIncome';
import useFiexCost from '@/app/(user)/hooks/useFiexCost';
import { Memo } from '../../types/response';
import { Income } from '@/app/(user)/types/reponse';
import { FixedCost } from '@/app/(user)/types/reponse';

/* 차후에 리펙 */
interface ModalContentProps {
    selectedDate: Date;
    closeModal: () => void;
    data: Memo[] | Income[] | FixedCost[];
}

// 공통 입력 훅
function useCostInput(
    addToast: (
        message: string,
        variant: 'error' | 'success' | 'warning'
    ) => void
) {
    const [cost, setCost] = useState<string>('');
    const handleChange = useCallback(
        (e: ChangeEvent<HTMLInputElement>) => {
            const raw = e.target.value.replace(/,/g, '');
            if (isNaN(Number(raw)))
                return addToast('숫자만 입력해주세요', 'error');
            if (/^0\d+/.test(raw))
                return addToast('앞자리에 0은 올 수 없습니다', 'error');
            setCost(raw);
        },
        [addToast]
    );
    return { cost, handleChange };
}

interface MemoModalContentProps extends ModalContentProps {
    data: Memo[];
}

// 메모 모달 (작성/수정 자동 판별)
export const MemoModalContent = ({
    closeModal,
    selectedDate,
    data
}: MemoModalContentProps) => {
    const addToast = useToastStore((state) => state.addToast);
    const contentRef = useRef<HTMLInputElement | null>(null);
    const { createMemo, deleteMemo, editMemo } = useDiary();

    const [dateData, setDateData] = useState<Date>(selectedDate);

    const hasMemo = data.some((m) => m.date === formatDateToString(dateData));

    const handleMemoAction = useCallback(async () => {
        const content = contentRef.current?.value;
        if (!content) return addToast('내용을 입력해주세요', 'error');
        try {
            if (hasMemo) {
                await editMemo.mutateAsync({
                    date: formatDateToString(dateData),
                    content
                });
            } else {
                await createMemo.mutateAsync({
                    date: formatDateToString(dateData),
                    content
                });
            }
            closeModal();
        } catch {}
    }, [addToast, closeModal, createMemo, editMemo, dateData, hasMemo]);

    const handleDelete = useCallback(async () => {
        if (!hasMemo) return addToast('삭제할 메모가 없습니다', 'error');

        try {
            await deleteMemo.mutateAsync({
                date: formatDateToString(dateData)
            });
            closeModal();
        } catch {}
    }, [closeModal, deleteMemo, dateData, hasMemo, addToast]);

    return (
        <EventForm handleEvent={handleMemoAction}>
            <EventForm.DaySelector
                initalDate={dateData}
                isBlock={false}
                setSelectedDate={setDateData}
            />
            <EventForm.Content contentRef={contentRef} />
            <EventForm.ActionButtons
                handleClose={closeModal}
                handleDelete={handleDelete}
            />
        </EventForm>
    );
};

interface IncomeModalContentProps extends ModalContentProps {
    data: Income[];
}

// 소득 모달
export const IncomeModalContent = ({
    selectedDate,
    closeModal,
    data
}: IncomeModalContentProps) => {
    const addToast = useToastStore((state) => state.addToast);
    const { postIncome, deleteIncome } = useIncome();
    const { cost, handleChange } = useCostInput(addToast);
    const [dateData, setDateData] = useState<Date>(selectedDate);

    const handleIncomeAction = useCallback(async () => {
        if (!cost) return addToast('값을 입력해주세요', 'error');
        const data = { cost, day: getDay(dateData) };
        try {
            await postIncome.mutateAsync(data);
            closeModal();
        } catch {}
    }, [addToast, closeModal, cost, postIncome, dateData]);

    const handleDelete = useCallback(async () => {
        try {
            await deleteIncome.mutateAsync({
                day: getDay(dateData)
            });
            closeModal();
        } catch {}
    }, [closeModal, dateData, deleteIncome]);

    return (
        <EventForm handleEvent={handleIncomeAction}>
            <EventForm.DaySelector
                initalDate={dateData}
                isBlock={false}
                setSelectedDate={setDateData}
            />
            <EventForm.Input
                placeholder="소득을 등록해주세요"
                handleChange={handleChange}
                value={cost}
                lable="소득"
            />
            <EventForm.ActionButtons
                handleClose={closeModal}
                handleDelete={handleDelete}
            />
        </EventForm>
    );
};

interface ConsumptionModalContentProps extends ModalContentProps {
    data: FixedCost[];
}

const CONSUMPTION_CATEGORIES = [
    '문구',
    '식품',
    '음료',
    '기타',
    '생활용품',
    '패션/의류',
    '건강/의약품',
    '미용/화장품',
    '전자기기',
    '교통/주유',
    '서비스',
    '취미/여가',
    '반려동물',
    '유아/아동'
];

// 소비 모달
export const ConsumptionModalContent = ({
    selectedDate,
    closeModal
}: ConsumptionModalContentProps) => {
    const addToast = useToastStore((state) => state.addToast);

    const [dateData, setDateData] = useState<Date>(selectedDate);
    const [category, setCategory] = useState<string>(CONSUMPTION_CATEGORIES[0]);
    const [name, setName] = useState<string>('');

    const { postFixedConsumption } = useFiexCost();
    const { cost, handleChange } = useCostInput(addToast);

    const handleConsumptionAction = useCallback(async () => {
        if (!cost) return addToast('값을 입력해주세요', 'error');
        const data = {
            amount: cost,
            day: getDay(dateData),
            category: category,
            name
        };
        try {
            await postFixedConsumption.mutateAsync(data);
            closeModal();
        } catch {}
    }, [
        addToast,
        closeModal,
        cost,
        postFixedConsumption,
        dateData,
        category,
        name
    ]);

    const handleDelete = useCallback(async () => {
        if (!dateData) return;
    }, [dateData]);

    return (
        <EventForm handleEvent={handleConsumptionAction}>
            <EventForm.DaySelector
                initalDate={dateData}
                isBlock={false}
                setSelectedDate={setDateData}
            />
            <EventForm.Input
                placeholder="소비액 등록하세요"
                handleChange={handleChange}
                value={cost}
                lable="소비액"
            />
            <EventForm.Input
                placeholder="소비명을 등록해주세요"
                handleChange={(e) => setName(e.target.value)}
                value={name}
                lable="소비명"
            />
            <EventForm.Dropbox
                label="카테고리"
                options={CONSUMPTION_CATEGORIES}
                value={category}
                onChange={setCategory}
            />
            <EventForm.ActionButtons
                handleClose={closeModal}
                handleDelete={handleDelete}
            />
        </EventForm>
    );
};
