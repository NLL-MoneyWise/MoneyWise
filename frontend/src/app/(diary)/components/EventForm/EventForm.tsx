import React, { useState, ReactNode, FormEvent } from 'react';
import InputField from '@/app/common/components/Input/InputField';
import Button from '@/app/common/components/Button/Button';
import Text from '@/app/common/components/Text/Text';
import CalendarButton from '../CalendarButton/CalendarButton';

interface EventFormProps {
    handleEvent: () => void;
    children: ReactNode;
}

const EventForm = ({ handleEvent, children }: EventFormProps) => {
    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        await handleEvent();
    };

    return (
        <div className="p-6 max-w-md mx-auto ">
            <form onSubmit={handleSubmit}>{children}</form>
        </div>
    );
};

const Title = ({ children }: { children: ReactNode }) => {
    return (
        <Text.SubTitle className="mb-6 text-black">{children}</Text.SubTitle>
    );
};

interface DaySelectorProps {
    initalDate: Date;
    isBlock: boolean;
}

const DaySelector = ({ initalDate, isBlock }: DaySelectorProps) => {
    const [selectedDate, setSelectedDate] = useState<Date | undefined>(
        initalDate || new Date()
    );

    return (
        <div className="mb-4">
            <Text.SemiBoldText className="mb-2">날짜</Text.SemiBoldText>
            <CalendarButton
                selectedDate={selectedDate}
                onDateSelect={setSelectedDate}
                isBlock={isBlock}
            />
        </div>
    );
};

interface ContentProps {
    contentRef: React.Ref<HTMLInputElement>;
}

const Content = ({ contentRef }: ContentProps) => {
    return (
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
    );
};

interface BaseActionProps {
    handleClose: () => void;
}

interface SaveActionButtonsProps extends BaseActionProps {}

interface EditCreateActionButtonsProps extends BaseActionProps {
    handleDelete: () => Promise<void>;
}

const SaveActionButtons = ({ handleClose }: SaveActionButtonsProps) => {
    return (
        <div className="modalbuttoncontainer">
            <Button type="button" onClick={handleClose} variant="secondary">
                취소
            </Button>

            <Button type="submit">저장</Button>
        </div>
    );
};

const EditCreateActionButtons = ({
    handleClose,
    handleDelete
}: EditCreateActionButtonsProps) => {
    return (
        <div className="modalbuttoncontainer">
            <Button type="button" onClick={handleClose} variant="secondary">
                취소
            </Button>

            <Button variant="delete" onClick={handleDelete} type="button">
                🗑️ 삭제하기
            </Button>

            <Button variant="modify" type="submit">
                ✏️ 수정하기
            </Button>
        </div>
    );
};

EventForm.Title = Title;
EventForm.DaySelector = DaySelector;
EventForm.Content = Content;
EventForm.EditCreateActionButtons = EditCreateActionButtons;
EventForm.SaveActionButtons = SaveActionButtons;

export default EventForm;
