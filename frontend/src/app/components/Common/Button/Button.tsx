import { ReactNode } from '@tanstack/react-router';
import React from 'react';
import { clsx } from 'clsx';

interface ButtonProps {
    children: ReactNode;
    size?: 'sm' | 'md' | 'lg';
    handleClick: () => void;
}

const Button = ({ children, size, handleClick }: ButtonProps) => {
    return (
        <button
            className={clsx(
                'bg-primary text-white flex justify-center items-center py-[10px] rounded-xl w-[100px] text-sm',
                {
                    'w-[200px] h-[40px]': size === 'lg',
                    'w-[60px] h-[30px]': size === 'sm',
                    'w-[100px] h-[40px]': size === 'md' || !size
                }
            )}
            onClick={handleClick}
        >
            {children}
        </button>
    );
};

export default Button;
