import React, {
    useState,
    ReactNode,
    FormEvent,
    ChangeEvent,
    useRef,
    useEffect
} from 'react';
import InputField from '@/app/common/components/Input/InputField';
import Button from '@/app/common/components/Button/Button';
import Text from '@/app/common/components/Text/Text';
import CalendarButton from '../CalendarButton/CalendarButton';
import { formatNumber } from '../../util/formatComma';

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

const Title = ({ children }: { children: string }) => {
    const ref = useRef<HTMLHeadingElement>(null);
    const prevTitle = useRef(children);

    useEffect(() => {
        if (prevTitle.current !== children && ref.current) {
            const el = ref.current;
            el.classList.add('highlight');

            const timeout = setTimeout(() => {
                el.classList.remove('highlight');
            }, 500);

            prevTitle.current = children;

            return () => clearTimeout(timeout);
        }
    }, [children]);

    return (
        <Text.Title className="mb-6  text-gray-500" ref={ref}>
            {children}
        </Text.Title>
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

interface InputProps {
    value: string;
    handleChange: (e: ChangeEvent<HTMLInputElement>) => void;
    placeholder: string;
    lable: string;
}

const Input = ({ value, placeholder, handleChange, lable }: InputProps) => {
    return (
        <div className="mb-4">
            <Text.SemiBoldText className="mb-2">{lable}</Text.SemiBoldText>
            <InputField
                value={formatNumber(value)}
                placeholder={placeholder}
                element="input"
                className="w-full"
                type="text"
                onInputChange={handleChange}
            />
        </div>
    );
};

interface BaseActionProps {
    handleClose: () => void;
}

interface SaveActionButtonsProps extends BaseActionProps {}

interface EditCreateActionButtonsProps extends BaseActionProps {
    handleDelete: () => void;
}

// 차후에 리펙

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

interface BudgetButtonsProps extends BaseActionProps {
    handleChange: () => void;
    lable: string;
}

const BudgetButtons = ({
    handleClose,
    handleChange,
    lable
}: BudgetButtonsProps) => {
    return (
        <div className="modalbuttoncontainer">
            <Button type="button" onClick={handleClose} variant="secondary">
                취소
            </Button>

            <Button variant="modify" onClick={handleChange} type="button">
                {lable}
            </Button>

            <Button type="submit">저장</Button>
        </div>
    );
};

EventForm.Title = Title;
EventForm.DaySelector = DaySelector;
EventForm.Content = Content;
EventForm.Input = Input;
EventForm.EditCreateActionButtons = EditCreateActionButtons;
EventForm.SaveActionButtons = SaveActionButtons;
EventForm.BudgetButtons = BudgetButtons;

export default EventForm;
