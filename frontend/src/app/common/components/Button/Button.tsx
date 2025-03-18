'use client';
import React from 'react';
import { clsx } from 'clsx';
import { ReactNode } from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    children: ReactNode;
    handleClick?: () => void;
}

// 차후에 수정
const Button = ({ children, handleClick, className, ...rest }: ButtonProps) => {
    return (
        <button
            className={clsx(
                'bg-primary/90 flex justify-center items-center py-2 rounded-xl',
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
