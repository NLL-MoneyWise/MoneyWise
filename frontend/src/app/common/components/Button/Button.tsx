'use client';
import React from 'react';
import { clsx } from 'clsx';
import { ReactNode } from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    children: ReactNode;
    handleClick?: () => void;
    variant?: 'primary' | 'secondary';
}

// 차후에 수정
const Button = ({
    variant = 'primary',
    children,
    handleClick,
    className,
    ...rest
}: ButtonProps) => {
    return (
        <button
            className={clsx(
                'flex justify-center items-center py-2 rounded-xl cursor-pointer',
                {
                    'bg-primary/80 text-white  hover:bg-primary':
                        variant === 'primary',
                    'bg-gray-200 text-gray-800  hover:bg-gray-300':
                        variant === 'secondary'
                },
                className
            )}
            onClick={handleClick}
            {...rest}
        >
            {children}
        </button>
    );
};

export default Button;
