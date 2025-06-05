'use client';

import React, { ReactNode } from 'react';
import Button from '../Button/Button';
import { useRouter } from 'next/navigation';
import clsx from 'clsx';

interface LinkProps {
    address: string;
    children: ReactNode;
    className?: string;
}

const Link = ({ address, children, className }: LinkProps) => {
    const router = useRouter();
    const handleClick = () => {
        router.push(address);
    };

    return (
        <Button onClick={handleClick} className={clsx(` `, className)}>
            {children}
        </Button>
    );
};

export default Link;
