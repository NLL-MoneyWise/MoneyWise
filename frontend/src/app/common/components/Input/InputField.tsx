import clsx from 'clsx';
import React, { forwardRef } from 'react';

// 기본 공통 props
interface BaseInputFieldProps {
    element: 'input' | 'textarea';
    label?: string;
    type?: React.HTMLInputTypeAttribute;
    placeholder?: string;
    isError?: boolean;
}

// 제어 컴포넌트용 props
interface ControlledInputProps extends BaseInputFieldProps {
    value: string;
    onChange: (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => void;
}

type InputFieldProps = ControlledInputProps | BaseInputFieldProps;

const InputField = forwardRef<
    HTMLInputElement | HTMLTextAreaElement,
    InputFieldProps
>(({ element = 'input', label, type, placeholder, isError, ...rest }, ref) => {
    return (
        <div className="flex flex-col gap-2">
            {label && (
                <label
                    htmlFor={type}
                    className={clsx(
                        'text-sm font-medium text-primary relative',
                        `${isError && 'opacity-0'}`
                    )}
                >
                    {label}
                </label>
            )}
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
