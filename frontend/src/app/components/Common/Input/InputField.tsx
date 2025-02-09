import React from 'react';

interface InputFieldProps {
    element: 'input' | 'textarea';
    label: string;
    type: React.HTMLInputTypeAttribute;
    placeholder?: string;
}

const InputField: React.FC<InputFieldProps> = ({
    element = 'input',
    label,
    type,
    placeholder
}) => {
    return (
        <div className="flex flex-col gap-2">
            <label htmlFor={type} className="text-sm font-medium text-gray-700">
                {label}
            </label>

            {element === 'input' ? (
                <input
                    id={type}
                    type={type}
                    placeholder={placeholder}
                    className="w-full px-3 py-2 border border-gray-300 rounded-xl shadow-sm focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary"
                />
            ) : (
                <textarea
                    id={type}
                    placeholder={placeholder}
                    className="w-full px-3 py-2 border border-gray-300 rounded-xl shadow-sm focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary min-h-[100px] resize-y"
                />
            )}
        </div>
    );
};
export default InputField;
