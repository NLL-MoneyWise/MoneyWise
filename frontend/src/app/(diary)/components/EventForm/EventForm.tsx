import React, {
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
        <form
            onSubmit={handleSubmit}
            className="flex-1 flex flex-col gap-2 justify-between w-10/12 mx-auto"
        >
            {children}
        </form>
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
        <Text.Title className=" text-gray-500" ref={ref}>
            {children}
        </Text.Title>
    );
};

interface DaySelectorProps {
    initalDate: Date;
    isBlock: boolean;
    setSelectedDate: React.Dispatch<React.SetStateAction<Date>>;
}

const DaySelector = ({
    initalDate,
    isBlock,
    setSelectedDate
}: DaySelectorProps) => {
    return (
        <div>
            <Text.SemiBoldText className="mb-2">날짜</Text.SemiBoldText>
            <CalendarButton
                selectedDate={initalDate}
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
        <div>
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
        <div>
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

interface DropboxProps {
    label: string;
    options: string[]; // value/label 쌍 대신 string 배열
    value: string;
    onChange: (value: string) => void;
}

const Dropbox = ({
    label = '선택',
    options,
    value,
    onChange
}: DropboxProps) => {
    return (
        <div>
            <Text.SemiBoldText className="mb-2">{label}</Text.SemiBoldText>

            <select
                className="w-full border rounded-xl px-3 py-2 focus:outline-none"
                value={value}
                onChange={(e) => onChange(e.target.value)}
            >
                {options.map((opt) => (
                    <option key={opt} value={opt}>
                        {opt}
                    </option>
                ))}
            </select>
        </div>
    );
};

interface BaseActionProps {
    handleClose: () => void;
    handleDelete: () => void;
}

const ActionButtons = ({ handleClose, handleDelete }: BaseActionProps) => {
    return (
        <div className="flex gap-4 mt-2 justify-end">
            <Button type="button" onClick={handleClose} variant="secondary">
                취소
            </Button>

            <Button variant="delete" onClick={handleDelete} type="button">
                🗑️ 삭제하기
            </Button>

            <Button variant="modify" type="submit">
                ✏️ 저장하기
            </Button>
        </div>
    );
};

EventForm.Title = Title;
EventForm.DaySelector = DaySelector;
EventForm.Content = Content;
EventForm.Input = Input;
EventForm.ActionButtons = ActionButtons;
EventForm.Dropbox = Dropbox;

export default EventForm;
