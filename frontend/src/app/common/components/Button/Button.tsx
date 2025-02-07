'use client';
import React from 'react';
import { clsx } from 'clsx';
import { ReactNode } from 'react';

interface ButtonProps {
    children: ReactNode;
    width: number;
    height: number;
    handleClick: () => void;
}

const Button = ({ children, handleClick }: ButtonProps) => {
    return (
        <button
            className={clsx(
                'bg-primary text-white flex justify-center items-center py-[10px] rounded-xl text-sm w-full h-[45px]'
            )}
            onClick={handleClick}
        >
            {children}
        </button>
    );
};

export default Button;
