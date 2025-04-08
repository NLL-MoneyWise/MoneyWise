'use client';
import React, { ReactNode } from 'react';
import { clsx } from 'clsx';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    children: ReactNode;
    handleClick?: () => void;
    variant?: 'primary' | 'secondary' | 'kakao';
    className?: string;
}

const Button = ({
    variant = 'primary',
    children,
    handleClick,
    className,
    ...rest
}: ButtonProps) => {
    const KakaoIcon = () => (
        <svg
            width="21"
            height="20"
            viewBox="0 0 21 20"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
        >
            <path
                fillRule="evenodd"
                clipRule="evenodd"
                d="M0.5 9.00012C0.5 4.14312 5.156 0.500122 10.5 0.500122C15.844 0.500122 20.5 4.14312 20.5 9.00012C20.5 13.8571 15.844 17.5001 10.5 17.5001C9.974 17.5001 9.45867 17.4668 8.954 17.4001L6.054 19.3321C5.88945 19.4417 5.6961 19.5001 5.49839 19.4998C5.30068 19.4996 5.10749 19.4407 4.94321 19.3307C4.77894 19.2207 4.65097 19.0645 4.57546 18.8817C4.49995 18.699 4.48031 18.498 4.519 18.3041L4.964 16.0831C2.328 14.5901 0.5 12.0171 0.5 9.00012Z"
                fill="#181600"
            />
        </svg>
    );

    return (
        <button
            className={clsx(
                'flex justify-center items-center py-2 rounded-xl cursor-pointer font-medium',
                {
                    'bg-primary/80 text-white hover:bg-primary':
                        variant === 'primary',
                    'bg-gray-200 text-gray-800 hover:bg-gray-300':
                        variant === 'secondary',
                    'bg-[#FEE500] gap-2 text-[rgba(0,0,0,0.85)]':
                        variant === 'kakao'
                },
                className
            )}
            onClick={handleClick || rest.onClick}
            {...rest}
        >
            {variant === 'kakao' && <KakaoIcon />}
            {variant === 'kakao' && typeof children === 'string' ? (
                <span>{children}</span>
            ) : (
                children
            )}
        </button>
    );
};

export default Button;
