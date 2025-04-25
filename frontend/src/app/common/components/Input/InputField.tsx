import clsx from 'clsx';
import React, { forwardRef } from 'react';

// 기본 공통 props
interface BaseInputFieldProps {
    element: 'input' | 'textarea';
    label?: string;
    type?: React.HTMLInputTypeAttribute;
    placeholder?: string;
    className?: string;
}

// 제어 컴포넌트용 props
interface ControlledInputProps extends BaseInputFieldProps {
    value: string;
    onChange: (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => void;
    defaultValue?: never;
}

// 비제어 컴포넌트용 props 추가
interface UncontrolledInputProps extends BaseInputFieldProps {
    defaultValue?: string;
    value?: never;
    onChange?: never;
}

type InputFieldProps = ControlledInputProps | UncontrolledInputProps;

const commonInputStyles =
    'w-full px-3 py-2 border border-gray-300  rounded-xl shadow-sm focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary text-xl ';

const InputField = forwardRef<
    HTMLInputElement | HTMLTextAreaElement,
    InputFieldProps
>(
    (
        { element = 'input', label, type, placeholder, className, ...rest },
        ref
    ) => {
        return (
            <div className={clsx('flex flex-col gap-2 font-medium', className)}>
                {label && (
                    <label
                        htmlFor={type}
                        className={'text-lg font-medium text-primary relative'}
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
                        className={`${commonInputStyles} `}
                    />
                ) : (
                    <textarea
                        ref={ref as React.RefObject<HTMLTextAreaElement>}
                        id={type}
                        placeholder={placeholder}
                        {...rest}
                        className={`${commonInputStyles} min-h-[100px] resize-y`}
                    />
                )}
            </div>
        );
    }
);

InputField.displayName = 'InputField';
export default InputField;
