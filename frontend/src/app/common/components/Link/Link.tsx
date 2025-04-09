'use client';

import React, { ReactNode } from 'react';
import Button from '../Button/Button';
import { useRouter } from 'next/navigation';

interface LinkProps {
    address: string;
    children: ReactNode;
}

const Link = ({ address, children }: LinkProps) => {
    const router = useRouter();
    const handleClick = () => {
        router.push(address);
    };

    return (
        <Button onClick={handleClick} className="w-full">
            {children}
        </Button>
    );
};

export default Link;
