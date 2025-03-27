'use client';
import React from 'react';

import { useRouter } from 'next/navigation';

const TopBar = () => {
    const router = useRouter();

    const handleGoBack = () => {
        router.back();
    };

    return (
        <p
            className="text-gray-400 whitespace-pre-line w-full  text-xl font-bold fixed mt-4 ml-4 cursor-pointer"
            onClick={handleGoBack}
        >
            뒤로가기
        </p>
    );
};

export default TopBar;
