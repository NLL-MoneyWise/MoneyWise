import React from 'react';
import Button from '../Button/Button';
import { useRouter } from 'next/router';

interface LinkProps {
    address: string;
    addressName: string;
}

const Link = ({ address, addressName }: LinkProps) => {
    const router = useRouter();
    const handleClick = () => {
        router.push(address);
    };

    return <Button onClick={handleClick}>{addressName}</Button>;
};

export default Link;
