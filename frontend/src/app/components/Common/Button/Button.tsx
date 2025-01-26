import { ReactNode } from '@tanstack/react-router';
import React from 'react';
import { clsx } from 'clsx';

interface ButtonProps {
    children: ReactNode;
    size?: 'md' | 'lg';
    handleClick: () => void;
}

const Button = ({ children, size, handleClick }: ButtonProps) => {
    return (
        <button
            className={clsx(
                'bg-primary text-white flex justify-center py-[10px] h-[40px] rounded-xl w-[100px]',
                { 'w-[200px]': size === 'lg' }
            )}
            onClick={handleClick}
        >
            {children}
        </button>
    );
};

export default Button;
