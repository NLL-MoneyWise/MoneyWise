'use client';

import { useToastStore } from '@/app/common/hooks/useToastStore';
import { useEffect, useState } from 'react';

const useIsLoggedIn = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [loading, setLoading] = useState(true);
    const { addToast } = useToastStore();
    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                const response = await fetch('/api/auth/check');
                const data = await response.json();
                setIsLoggedIn(data.isLoggedIn);
            } catch (err) {
                addToast('데이터를 패칭 중 실패했습니다.', 'error');
                setIsLoggedIn(false);
            } finally {
                setLoading(false);
            }
        };

        checkLoginStatus();
    }, []);

    return { isLoggedIn, loading };
};

export default useIsLoggedIn;
