import React from 'react';
import { IoIosArrowBack } from '@react-icons/all-files/io/IoIosArrowBack';
import Button from '../Button/Button';

import { useRouter } from 'next/router';

const TopBar = () => {
    const router = useRouter();

    const handleGoBack = () => {
        router.back();
    };

    const handleConfirm = () => {
        console.log('hello');
    };

    return (
        <div className="flex w-full justify-between">
            <IoIosArrowBack onClick={handleGoBack} />
            <Button handleClick={handleConfirm}> {'확인'}</Button>
        </div>
    );
};

export default TopBar;
