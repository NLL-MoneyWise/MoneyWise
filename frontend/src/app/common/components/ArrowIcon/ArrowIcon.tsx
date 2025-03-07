'use client';
import React from 'react';

const ArrowIcon = ({
    color = '#E7E6EB',
    className = '',
    size = { width: 46, height: 16 }
}) => {
    return (
        <svg
            fill="none"
            color={color}
            viewBox="0 0 46 16"
            xmlns="http://www.w3.org/2000/svg"
            className={className}
            width={size.width}
            height={size.height}
        >
            <path
                d="M44 2L23 14L2 2"
                stroke={color}
                strokeWidth="4"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
        </svg>
    );
};
export default ArrowIcon;
