'use client';
import React from 'react';
import { clsx } from 'clsx';
import { ReactNode } from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    children: ReactNode;
    width?: string | number;
    height?: string | number;
    fontSize?: string | number;
    handleClick?: () => void;
}

// 차후에 수정
const Button = ({
    children,
    handleClick,
    width = '100%',
    height = '45px',
    className,
    ...rest
}: ButtonProps) => {
    return (
        <button
            className={clsx(
                'bg-primary/90 flex justify-center items-center py-2 rounded-xl text-xl',
                className
            )}
            style={{
                width: typeof width === 'number' ? `${width}px` : width,
                height: typeof height === 'number' ? `${height}px` : height
            }}
            onClick={handleClick}
            {...rest}
        >
            {children}
        </button>
    );
};

export default Button;
