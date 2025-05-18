import React, { FormEvent, useState, useRef } from 'react';
import InputField from '@/app/common/components/Input/InputField';
import Button from '@/app/common/components/Button/Button';
import Text from '@/app/common/components/Text/Text';
import CalendarButton from '../CalendarButton/CalendarButton';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import formatDate from '../../util/formatDate';
import useDiary from '../../hooks/useDiary';

interface EventFormProps {
    closeModal: () => void;
    initalDate: Date;
    editMode: 'create' | 'edit';
    id?: number;
}

const EventForm: React.FC<EventFormProps> = ({
    closeModal,
    initalDate,
    editMode
}) => {
    const [selectedDate, setSelectedDate] = useState<Date | undefined>(
        initalDate || new Date()
    );

    const contentRef = useRef<HTMLInputElement | null>(null);

    const addToast = useToastStore((state) => state.addToast);
    const { saveMemo, editMemo, deleteMemo } = useDiary();

    // 날짜 변경 처리
    const handleDateSelect = (date: Date | undefined) => {
        setSelectedDate(date);
    };

    // 폼 제출 처리
    const handleCreateSubmit = async (e: FormEvent) => {
        e.preventDefault();

        const content = contentRef.current?.value?.trim();

        if (!selectedDate) {
            addToast('날짜를 선택해주세요', 'error');
            return;
        }

        if (!content) {
            addToast('내용을 입력해주세요', 'error');
            return;
        }

        await saveMemo
            .mutateAsync({
                date: formatDate(selectedDate),
                content: content
            })
            .catch((error) => {
                return;
            });

        closeModal();
    };

    const handleModifySubmit = async (e: FormEvent) => {
        e.preventDefault();

        const content = contentRef.current?.value?.trim();

        if (!content) {
            addToast('내용을 입력해주세요', 'error');
            return;
        }

        await editMemo
            .mutateAsync({
                date: formatDate(initalDate),
                content: content
            })
            .catch((error) => {
                return;
            });

        closeModal();
    };

    const handelDelete = async (e: FormEvent) => {
        e.stopPropagation();
        await deleteMemo.mutateAsync({ date: formatDate(initalDate) });
        closeModal();
    };

    return (
        <div className="p-6 max-w-md mx-auto ">
            {/* 선택된 모드에 따라 편집모드가 변경됩니다. */}
            <Text.SubTitle className="mb-6 text-black">
                {editMode === 'create' ? '메모 작성' : '메모 편집'}
            </Text.SubTitle>
            {/* 모드에따라 */}
            <form
                onSubmit={
                    editMode === 'create'
                        ? handleCreateSubmit
                        : handleModifySubmit
                }
            >
                {/* 날짜 선택 */}
                <div className="mb-4">
                    <Text.SemiBoldText className="mb-2">날짜</Text.SemiBoldText>
                    <CalendarButton
                        selectedDate={selectedDate}
                        onDateSelect={handleDateSelect}
                        className="w-full"
                        editMode={editMode}
                    />
                </div>

                {/* 내용 입력 */}
                <div className="mb-4">
                    <Text.SemiBoldText className="mb-2">내용</Text.SemiBoldText>
                    <InputField
                        ref={contentRef}
                        placeholder="메모 내용을 입력하세요"
                        element="textarea"
                        className="w-full"
                        defaultValue=""
                    />
                </div>

                {/* 버튼 모음 */}
                <div className="flex justify-end space-x-2 mt-6">
                    <Button
                        type="button"
                        onClick={closeModal}
                        className="px-4 py-2"
                        variant="secondary"
                    >
                        취소
                    </Button>
                    {editMode === 'create' ? (
                        <Button type="submit" className="px-4 py-2">
                            저장
                        </Button>
                    ) : (
                        <>
                            <Button
                                variant="delete"
                                className="px-4 py-2"
                                onClick={handelDelete}
                                type="button"
                            >
                                🗑️ 삭제하기
                            </Button>

                            <Button
                                variant="modify"
                                className="px-4 py-2"
                                type="submit"
                            >
                                ✏️ 수정하기
                            </Button>
                        </>
                    )}
                </div>
            </form>
        </div>
    );
};

export default EventForm;
