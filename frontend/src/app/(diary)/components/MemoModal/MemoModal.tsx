import { useToastStore } from '@/app/common/hooks/useToastStore';
import EventForm from '../EventForm/EventForm';
import { useRef } from 'react';
import useDiary from '../../hooks/useDiary';
import formatDate from '../../util/formatDate';

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
            .mutateAsync({ date: formatDate(selectedDate), content })
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
            .mutateAsync({ date: formatDate(selectedDate), content })
            .then(() => {
                closeModal();
            })
            .catch(() => {});
    };

    const deleteMemoAciton = async () => {
        await deleteMemo
            .mutateAsync({ date: formatDate(selectedDate) })
            .then(() => {
                closeModal();
            })
            .catch(() => {});
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
