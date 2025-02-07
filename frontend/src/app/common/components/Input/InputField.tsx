import React, { forwardRef } from 'react';

// 기본 공통 props
interface BaseInputFieldProps {
    element: 'input' | 'textarea';
    label: string;
    type?: React.HTMLInputTypeAttribute;
    placeholder?: string;
}

// 제어 컴포넌트용 props
interface ControlledInputProps extends BaseInputFieldProps {
    value: string;
    onChange: (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => void;
    defaultValue?: never;
}

// 비제어 컴포넌트용 props
interface UncontrolledInputProps extends BaseInputFieldProps {
    value?: never;
    onChange?: never;
    defaultValue?: string;
}

type InputFieldProps = ControlledInputProps | UncontrolledInputProps;

const InputField = forwardRef<
    HTMLInputElement | HTMLTextAreaElement,
    InputFieldProps
>(({ element = 'input', label, type, placeholder, ...rest }, ref) => {
    return (
        <div className="flex flex-col gap-2">
            <label htmlFor={type} className="text-sm font-medium text-primary">
                {label}
            </label>
            {element === 'input' ? (
                <input
                    ref={ref as React.RefObject<HTMLInputElement>}
                    id={type}
                    type={type}
                    placeholder={placeholder}
                    {...rest}
                    className="w-full px-3 py-2 border border-gray-300 rounded-xl shadow-sm focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary"
                />
            ) : (
                <textarea
                    ref={ref as React.RefObject<HTMLTextAreaElement>}
                    id={type}
                    placeholder={placeholder}
                    {...rest}
                    className="w-full px-3 py-2 border border-gray-300 rounded-xl shadow-sm focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary min-h-[100px] resize-y"
                />
            )}
        </div>
    );
});

InputField.displayName = 'InputField';
export default InputField;
